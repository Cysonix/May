package cn.edu.cuc.logindemo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.DeviceInfoUtils;
import cn.edu.cuc.logindemo.Utils.LogUtils;
import cn.edu.cuc.logindemo.Utils.ToastHelper;
import cn.edu.cuc.logindemo.adapter.ChannelNewsListAdapter;
import cn.edu.cuc.logindemo.domain.Channel;
import cn.edu.cuc.logindemo.domain.Enums;
import cn.edu.cuc.logindemo.domain.MitiException;
import cn.edu.cuc.logindemo.domain.NewsItem;
import cn.edu.cuc.logindemo.domain.NewsQueryConditions;
import cn.edu.cuc.logindemo.domain.Pager;
import cn.edu.cuc.logindemo.domain.Showing;
import cn.edu.cuc.logindemo.logic.ChannelLogic;
import cn.edu.cuc.logindemo.logic.NewsItemLogic;
import cn.edu.cuc.logindemo.ui.NewsDetailsActivity;
import cn.edu.cuc.logindemo.userlayout.ElasticScrollView;
import cn.edu.cuc.logindemo.userlayout.MyGallery;
import cn.edu.cuc.logindemo.userlayout.SlideMenuLayout;

/**
 * 首页（新闻列表页）
 *
 * @author SongQing
 *
 */
public class HomeFragment extends Fragment implements OnClickListener,
		OnItemClickListener, ElasticScrollView.OnRefreshListener {

	private LayoutInflater inflater;
	private LinearLayout llayoutLoading;

	// 左右导航图片按钮
	private ImageView imgViewPrevious = null;
	private ImageView imgViewNext = null;

	private ViewPager viewPager = null;

	// 当前ViewPager索引
	private int pagerIndex = 0;
	private ArrayList<View> menuViews = null;
	private ArrayList<Channel> channelList = null; // 需要放在导航栏上的栏目列表
	private ArrayList<Channel> perPageMenus = null;

	private ElasticScrollView elasticScrollView;

	// 图片跑马灯
	private LinearLayout galleryContainer;
	private MyGallery galleryNews;

	// 新闻列表
	private LinearLayout newsContainer;
	private ListView listViewNews;
	private ChannelNewsListAdapter mAdapter;
	private LinearLayout newsFooterContainer;
	private TextView textviewMoreNews;
	private ProgressBar progressMore;
	private LinearLayout footerLayout;

	private Channel mainChannel;
	private Channel topChannel;
	private Pager pagerMainData = null;
	private Pager pagerTopData = null;
	private int newsPage = 0; // 当前新闻页数

	private Button btnOnCallBack; // 回调按钮

	private int lastPositionY = 0;

	private static int screenWidth;

	private NewsItemLogic newsItemLogic;
	private ChannelLogic channelLogic;

	private static final int LOAD_TOPNEWS_DATA_FINISHED = 1;
	private static final int LOAD_MAINNEWS_DATA_FINISHED = 2;
	private static final int REFRESH_TOPNEWS_DATA_FINISHED = 3;
	private static final int REFRESH_MAINNEWS_DATA_FINISHED = 4;
	private static final int LOAD_MORE_MAINNEWS_FINISHED = 5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		return inflater.inflate(R.layout.main, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initialize();
	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){//相当于Fragment的onResume 
			if (newsItemLogic.getTotalNewsCount() == 0) {// 表明做了清空操作，需要重新加载
				mAdapter.deleteAllItems();
				mAdapter.notifyDataSetChanged();
				llayoutLoading.setVisibility(View.VISIBLE);
				elasticScrollView.setVisibility(View.INVISIBLE);
				onRefresh();
			}
		}else{//相当于Fragment的onPause 

		}
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		newsItemLogic = new NewsItemLogic(getActivity());
		channelLogic = new ChannelLogic(getActivity());

		pagerMainData = Pager.getDefault();
		pagerTopData = Pager.getTopDefault();

		mainChannel = channelLogic.getFirstChannelForMain();
		topChannel = channelLogic.getTopChannelForMain();

		this.setScreenSize();
		this.setUpViews();
		llayoutLoading.setVisibility(View.VISIBLE);
		elasticScrollView.setVisibility(View.INVISIBLE);

		loadMainData();
	}

	/**
	 * 获取屏幕的宽高，依照此算出缩放比例
	 */
	private void setScreenSize() {
		WindowManager windowManager = getActivity().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
	}

	/**
	 * 初始化栏目菜单
	 */
	private void setupMenus() {

		menuViews = new ArrayList<View>();
		channelList = (ArrayList<Channel>) channelLogic.getChannelsForMain();
		SlideMenuLayout menu = new SlideMenuLayout(getActivity(), channelList);

		int viewpagerNumber = channelList.size() / 5;

		for (int i = 0; i < viewpagerNumber; i++) {// 看有导航栏有几页(每页5个)
			perPageMenus = new ArrayList<Channel>();
			for (int j = 0; j < 5; j++) {
				if ((i * 5 + j) < channelList.size()) {
					perPageMenus.add(channelList.get(i * 5 + j));
				}
			}
			menuViews.add(menu.getSlideMenuLinerLayout(perPageMenus,
					screenWidth));
		}

		// 左右导航图片按钮
		imgViewPrevious = (ImageView) getView().findViewById(R.id.main_slidemenu_imageview_previous);
		imgViewPrevious.setVisibility(View.INVISIBLE);
		imgViewNext = (ImageView) getView().findViewById(R.id.main_slidemenu_imageview_next);
		imgViewPrevious.setOnClickListener(this);
		imgViewNext.setOnClickListener(this);

		if (menuViews.size() > 1) { // 如果栏目超过5个，就需要出现向后箭头
			imgViewNext.setVisibility(View.VISIBLE);
		}

		// 加载移动菜单下内容
		viewPager = (ViewPager) getView().findViewById(R.id.main_slidemenu_viewpager_container);
		viewPager.setAdapter(new SlideMenuAdapter());
		viewPager.setOnPageChangeListener(new SlideMenuChangeListener());
	}

	/**
	 * 初始化图片跑马灯
	 */
	private void setupGalleryNews() {
		galleryContainer = (LinearLayout) inflater.inflate(
				R.layout.main_gallery, null);
		galleryNews = (MyGallery) galleryContainer
				.findViewById(R.id.galleryNews);

		galleryNews.initMyGallery(getActivity(), new ArrayList<Showing>(), null);

		DeviceInfoUtils deviceInfoHelper = new DeviceInfoUtils();
		int width = deviceInfoHelper.getWindowWidth();
		int screenHeight = deviceInfoHelper.getWindowHeight();

		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width,
				screenHeight / 4);
		galleryNews.setLayoutParams(p);
	}

	/**
	 * 初始化新闻列表
	 */
	private void setupNewsListView() {
		newsContainer = (LinearLayout) inflater.inflate(
				R.layout.main_listview_news, null);
		listViewNews = (ListView) newsContainer.findViewById(R.id.listViewNews);
		listViewNews.setOnItemClickListener(this);

		mAdapter = new ChannelNewsListAdapter(getActivity(), listViewNews);
		listViewNews.setAdapter(mAdapter);
	}

	/**
	 * 初始化更新条
	 */
	public void setupMoreView() {
		newsFooterContainer = (LinearLayout) inflater.inflate(
				R.layout.scrollover_footer, null);
		textviewMoreNews = (TextView) newsFooterContainer
				.findViewById(R.id.textviewFetchMore);

		progressMore = (ProgressBar) newsFooterContainer
				.findViewById(R.id.progressBar);
		progressMore.setIndeterminate(true);
		newsFooterContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				progressMore.setVisibility(View.VISIBLE);

				getMoreNews();
			}
		});

		progressMore.setVisibility(View.VISIBLE);

		textviewMoreNews.setText(getResources().getString(
				R.string.news_loading));

		footerLayout = new LinearLayout(getActivity());
		footerLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 1));
		footerLayout.setBackgroundColor(Color.BLACK);
		footerLayout.setOrientation(LinearLayout.VERTICAL);
	}

	/**
	 * 初始化下拉滚动容器
	 */
	public void setupScrollView() {
		elasticScrollView = (ElasticScrollView) getView().findViewById(R.id.scrolviewwContent);
		elasticScrollView.setonRefreshListener(this);

		try {
			elasticScrollView.addChild(galleryContainer, 1);
			elasticScrollView.addChild(newsContainer, 2);
			elasticScrollView.addChild(footerLayout, 3);
			elasticScrollView.addChild(newsFooterContainer, 4);
		} catch (Exception e) {
			LogUtils.d(e.getMessage());
		}
	}

	/**
	 * 初始化控件
	 */
	private void setUpViews() {

		inflater = LayoutInflater.from(getActivity());

		setupMenus();

		setupGalleryNews();

		setupNewsListView();
		setupMoreView();
		setupScrollView();

		// 加载数据提示区域
		llayoutLoading = (LinearLayout) getView().findViewById(R.id.main_layout_loading);

		btnOnCallBack = (Button) getView().findViewById(R.id.main_button_oncallback);
		btnOnCallBack.setOnClickListener(this);
	}

	/**
	 * 加载焦点新闻列表
	 */
	private void loadTopData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 先从网上获取数据保存到本地，然后读取本地数据
				if (AndroidApplication.getNetStatus() != Enums.NetStatus.Disable) {
					fetchRemoteNews(topChannel, 1, Enums.NewsFocus.FOCUS, false);
				}
				List<NewsItem> newInPartList = new ArrayList<NewsItem>();

				if (topChannel != null) {
					newInPartList = newsItemLogic.getNewsItems(
							topChannel.getId(), Enums.NewsFocus.FOCUS, pagerTopData);
				}
				sendMessage(newInPartList, LOAD_TOPNEWS_DATA_FINISHED);
			}
		}).start();// 第一次初始化首页新闻列表
	}

	/**
	 * 加载普通新闻列表
	 */
	private void loadMainData() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				// 先从网上获取数据保存到本地，然后读取本地数据
				if (AndroidApplication.getNetStatus() != Enums.NetStatus.Disable) {
					newsPage = 1;
					fetchRemoteNews(mainChannel, newsPage, Enums.NewsFocus.NORMAL,
							true);
				}
				List<NewsItem> newsItemList = new ArrayList<NewsItem>();

				if (mainChannel != null) {
					newsItemList = newsItemLogic.getNewsItems(
							mainChannel.getId(), Enums.NewsFocus.NORMAL,
							pagerMainData);
				}
				sendMessage(newsItemList, LOAD_MAINNEWS_DATA_FINISHED);
			}
		}).start();// 第一次初始化首页新闻列表
	}

	/**
	 * 从网络获取远程新闻列表，并保存至本地数据库
	 *
	 * @return
	 */
	private List<NewsItem> fetchRemoteNews(Channel channel, int page,
										   Enums.NewsFocus isFocus, boolean clearDB) {

		if (channel == null)
			return null;

		NewsQueryConditions conditions = new NewsQueryConditions();
		conditions.setChannelId(channel.getId());
		conditions.setPage(page);
		if (isFocus.equals(Enums.NewsFocus.FOCUS)) {
			conditions.setCount(5); // 首页的焦点新闻，一次最多拿5条
			conditions.setType(Enums.NewsFocus.FOCUS.getValue()); // FOCUS表示某一频道下面的焦点新闻
		} else if (isFocus.equals(Enums.NewsFocus.NORMAL)) {
			conditions.setCount(20); // 首页的新闻列表，一次最多拿20条
			conditions.setType(Enums.NewsFocus.NORMAL.getValue()); // NORMAL表示某一频道下面普通新闻
		}

		List<NewsItem> results = null;

		try {
			// List<NewsItem> newsItemList =
			// newsItemLogic.getNewsFromRemote(conditions);
			List<NewsItem> newsItemList = newsItemLogic.getNewsFromBaiduRemote(conditions);

			if (newsItemList != null && newsItemList.size() > 1 && clearDB) { // 需要清空数据库数据
				newsItemLogic.deleteAll();
			}
			// 新的数据保存到本地，并去重
			results = new ArrayList<NewsItem>();
			saveData(newsItemList, results, channel.getId());
		} catch (MitiException e) {
			LogUtils.e(e);
			return new ArrayList<NewsItem>();
		}
		return results;
	}

	/**
	 * 保存新闻到本地数据库缓存
	 *
	 * @param newsList
	 */
	private void saveData(List<NewsItem> newsList, List<NewsItem> results,
						  int channelId) {

		if (newsList == null) {
			return;
		}

		for (NewsItem news : newsList) {
			if (null == newsItemLogic.get(news.getNewsId(), channelId)) { // 说明在同一栏目下没有重复的新闻
				news.setChannelId(channelId);
				newsItemLogic.add(news);
			}
			results.add(0, news);
		}
	}

	/**
	 * 绑定普通新闻列表
	 *
	 * @param list
	 */
	private void bindNewsList(List<NewsItem> list) {
		if (list != null) {

			mAdapter.deleteAllItems();
			mAdapter.setItems(list);
			mAdapter.notifyDataSetChanged();

			setListViewHeightBasedOnChildren(listViewNews);
		}
	}

	/**
	 * 根据新闻列表内容动态设置listView的高度
	 *
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}

	/**
	 * 绑定焦点新闻列表
	 *
	 * @param list
	 */
	private void bindGallery(List<NewsItem> list) {

		if (list == null || list.size() == 0)
			return;

		ArrayList<Showing> showings = new ArrayList<Showing>();

		for (int i = 0; i < list.size(); i++) {

			NewsItem news = list.get(i);

			if (TextUtils.isEmpty(news.getImageHref()))
				continue;

			Showing s = new Showing();
			s.setNewsid(news.getNewsId());
			s.setProp(news.getAbstractString());
			s.setText(news.getTitle());
			s.setPicUrl(news.getImageHref());

			showings.add(s);

			if (showings.size() >= 5) // 限制焦点新闻的数量
				break;
		}
		galleryNews.initMyGallery(getActivity(), showings, topChannel);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.main_slidemenu_imageview_previous: // 栏目导航向前翻页
				pagerIndex--;
				viewPager.setCurrentItem(pagerIndex);
				break;
			case R.id.main_slidemenu_imageview_next: // 栏目导航向后翻页
				pagerIndex++;
				viewPager.setCurrentItem(pagerIndex);
				break;
			case R.id.main_button_oncallback: // 点击栏目菜单的回调按钮事件
				slideMenuOnChange(this.btnOnCallBack.getContentDescription()
						.toString());
			default:
				break;
		}
	}

	/**
	 * 点击时栏目时改变主页新闻列表和大图列表的状态和内容
	 *
	 * @param menuTag
	 *            栏目Id
	 */
	public void slideMenuOnChange(String menuTag) {

		this.mainChannel = channelLogic.get(Integer.parseInt(menuTag));
		this.topChannel = channelLogic.get(Integer.parseInt(menuTag));
		if (this.mainChannel != null) {
			llayoutLoading.setVisibility(View.VISIBLE);
			elasticScrollView.setVisibility(View.INVISIBLE);
			onRefresh();
		}
	}

	/**
	 * 新闻条目点击响应事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
							long arg3) {

		Intent intent = null;
		String newsId = mAdapter.getItem(position).getNewsId();
		// 需要判断新闻的类型，图片新闻和普通新闻加载的详情页不一样
		intent = new Intent(getActivity(), NewsDetailsActivity.class);

		intent.putExtra("NewsID", newsId);
		intent.putExtra("ChannelID", mainChannel.getId());

		startActivity(intent);
	}

	/*************** 刷新页面功能*********begin **********/

	@Override
	public void onRefresh() {
		onRefreshMainData();
	}

	/**
	 * 对新闻列表进行刷新操作(刷新就是向服务器端传入-1和分页大小，拿最新的新闻，到本地去重入库，再取出显示)
	 */
	private void onRefreshMainData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// try {
				// Thread.sleep(100);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }

				List<NewsItem> results = null;

				if (mainChannel != null) {
					newsPage = 1;
					fetchRemoteNews(mainChannel, newsPage, Enums.NewsFocus.NORMAL,
							true);

					results = newsItemLogic.getNewsItems(
							mainChannel.getId(), Enums.NewsFocus.NORMAL,
							pagerMainData);
				}

				sendMessage(results, REFRESH_MAINNEWS_DATA_FINISHED);
			}
		}).start();
	}

	/**
	 * 对焦点新闻大图进行刷新操作(刷新就是向服务器端传入-1和分页大小，拿最新的焦点新闻，到本地去重入库，再取出显示)
	 */
	private void onRefreshTopData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				List<NewsItem> results = null;

				if (topChannel != null) {
					fetchRemoteNews(topChannel, 1, Enums.NewsFocus.FOCUS, false);

					results = newsItemLogic.getNewsItems(
							topChannel.getId(), Enums.NewsFocus.FOCUS, pagerTopData);
				}

				sendMessage(results, REFRESH_TOPNEWS_DATA_FINISHED);
			}
		}).start();
	}

	/**
	 * 刷新新闻列表
	 *
	 * @param list
	 */
	private void refreshNews(List<NewsItem> list) {

		if (list == null || list.size() == 0)
			return;

		mAdapter.deleteAllItems();
		mAdapter.addItems(0, list);
		mAdapter.notifyDataSetChanged();

		setListViewHeightBasedOnChildren(listViewNews);
	}

	/*************** 刷新页面功能************end *******/

	/*************** 获取更多新闻功能(实际上是从目前页面上的最后一条，时间往前再拿分页条数的新闻)*********begin **********/

	/**
	 * 获取更多的新闻(从目前页面新闻列表的最后一条(时间最早)，取分页大小条数的新闻，也就是往前拿20条，附件在之前的后面，页面上就有40条新闻，
	 * 具体的获取新闻逻辑：
	 * 1.取得当前页面上最后一条新闻的id，到本地数据库中倒排时间，看看能不能取到该id之前的20条数据，如果有，直接绑定，如果没有，进入2
	 * 2.从网上获取从指定id 指定条数的新闻，先保存至本地 3.从本地拿出来，附件在最初的新闻列表之后，进行展示)
	 */
	private void getMoreNews() {

		textviewMoreNews.setText(R.string.pulldown_load_more);
		progressMore.setVisibility(View.VISIBLE);
		lastPositionY = elasticScrollView.getScrollY();

		new Thread(new Runnable() {

			@Override
			public void run() {

				int startIndex = mAdapter.getCount();

				List<NewsItem> newsItemList = null;

				pagerMainData.setStartIndex(startIndex);
				// newsItemList =
				// newsItemLogic.getNewsItemsByStartIndex(mainChannel.getId(),
				// NewsFocus.NORMAL, pagerMainData);
				newsPage = newsPage + 1;
				if (mainChannel != null && mAdapter.getCount() > 0) {
					// fetchRemoteNews(mainChannel,mAdapter.getItem(mAdapter.getCount()
					// - 1).getNewsId(),NewsFocus.NORMAL,false);
					fetchRemoteNews(mainChannel, newsPage, Enums.NewsFocus.NORMAL,
							false);
				}

				newsItemList = newsItemLogic.getNewsItemsByStartIndex(
						mainChannel.getId(), Enums.NewsFocus.NORMAL, pagerMainData);

				// 本地数据库中的startIndex后的新闻数量为0，或者少于每页的数量要求
				// if (newsItemList == null || newsItemList.size() <
				// pagerMainData.getPageSize()) {
				//
				// if (mainChannel != null && mAdapter.getCount() > 0){
				// fetchRemoteNews(mainChannel,mAdapter.getItem(mAdapter.getCount()
				// - 1).getNewsId(),NewsFocus.NORMAL,false);
				// //
				// fetchRemoteNews(mainChannel,mAdapter.getItem(mAdapter.getCount()).getNewsId(),NewsFocus.NORMAL);
				//
				// }
				//
				// newsItemList =
				// newsItemLogic.getNewsItemsByStartIndex(mainChannel.getId(),NewsFocus.NORMAL,pagerMainData);
				// }

				sendMessage(newsItemList, LOAD_MORE_MAINNEWS_FINISHED);
			}
		}).start();
	}

	/*************** 刷新页面功能************end *******/

	/**
	 * 程序的控制handler
	 */
	private Handler mainPageUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case LOAD_MAINNEWS_DATA_FINISHED:
					if (msg.obj != null) {
						@SuppressWarnings("unchecked")
						List<NewsItem> newsList = (List<NewsItem>) msg.obj;
						bindNewsList(newsList);
					}
					progressMore.setVisibility(View.GONE);
					textviewMoreNews.setText(getResources().getString(
							R.string.pulldown_footer_more));

					if (topChannel != null) {
						loadTopData();
					}
					break;
				case LOAD_TOPNEWS_DATA_FINISHED:

					if (msg.obj != null) {
						@SuppressWarnings("unchecked")
						List<NewsItem> newsList = (List<NewsItem>) msg.obj;

						// 图片跑马灯控件显示
						galleryContainer.setVisibility(View.VISIBLE);
						bindGallery(newsList);
						// 告诉elasticScrollView更新完毕
						elasticScrollView.onRefreshComplete();
					} else {
						galleryContainer.setVisibility(View.GONE);
					}

					mHandler.post(mScrollToPosition);

					llayoutLoading.setVisibility(View.INVISIBLE);
					elasticScrollView.setVisibility(View.VISIBLE);

					break;
				case REFRESH_MAINNEWS_DATA_FINISHED:
					boolean topStatus = true;
					if (msg.obj != null) {
						@SuppressWarnings("unchecked")
						List<NewsItem> newsList = (List<NewsItem>) msg.obj;
						refreshNews(newsList);
						if (newsList.get(0).getChannelId() != 2) { // 模拟控制只有一些栏目才有焦点图
							topStatus = false;
						}
					}

					if (topStatus) {
						onRefreshTopData();
					} else {
						elasticScrollView.onRefreshComplete();
						galleryContainer.setVisibility(View.GONE);
						mHandler.post(mScrollToPosition);
					}

					llayoutLoading.setVisibility(View.INVISIBLE);
					elasticScrollView.setVisibility(View.VISIBLE);
					// elasticScrollView.onRefreshComplete();
					// llayoutLoading.setVisibility(View.INVISIBLE);
					// elasticScrollView.setVisibility(View.VISIBLE);
					break;
				case REFRESH_TOPNEWS_DATA_FINISHED:
					if (msg.obj != null) {
						@SuppressWarnings("unchecked")
						List<NewsItem> newsList = (List<NewsItem>) msg.obj;
						galleryContainer.setVisibility(View.VISIBLE);
						bindGallery(newsList);
						elasticScrollView.onRefreshComplete();
						lastPositionY = 0;
						mHandler.post(mScrollToPosition);
					} else {
						elasticScrollView.onRefreshComplete();
						galleryContainer.setVisibility(View.GONE);
						mHandler.post(mScrollToPosition);
					}

					llayoutLoading.setVisibility(View.INVISIBLE);
					elasticScrollView.setVisibility(View.VISIBLE);

					break;
				case LOAD_MORE_MAINNEWS_FINISHED:

					progressMore.setVisibility(View.GONE);
					textviewMoreNews.setText(getResources().getString(
							R.string.pulldown_footer_more));

					if (msg.obj != null) {
						@SuppressWarnings("unchecked")
						List<NewsItem> newsList = (List<NewsItem>) msg.obj;

						mAdapter.addItems(newsList);
						mAdapter.notifyDataSetChanged();

						setListViewHeightBasedOnChildren(listViewNews);
					} else {
						ToastHelper.showToast(
										ToastHelper
												.getStringFromResources(R.string.news_details_nomorepage_end),
										Toast.LENGTH_SHORT);

					}

					elasticScrollView.onRefreshComplete();

					elasticScrollView.scrollTo(0, lastPositionY);

					break;
			}
		}
	};

	private final Handler mHandler = new Handler();
	private Runnable mScrollToPosition = new Runnable() {
		@Override
		public void run() {

			elasticScrollView.scrollTo(0, lastPositionY);
		}
	};

	private void sendMessage(Object obj, int msgCode) {
		Message msg = mainPageUIHandler.obtainMessage(msgCode);
		msg.obj = obj;
		msg.sendToTarget();
	}

	/**
	 * 滑动菜单数据适配器
	 *
	 * @author songqing
	 *
	 */
	class SlideMenuAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return menuViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(menuViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(menuViews.get(arg1));

			return menuViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 滑动菜单更改事件监听器
	 *
	 * @author songqing
	 *
	 */
	class SlideMenuChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			int pageCount = menuViews.size() - 1;
			pagerIndex = arg0;

			// 显示右边导航图片
			if (arg0 >= 0 && arg0 < pageCount) {
				imgViewNext.setVisibility(View.VISIBLE);
			} else {
				imgViewNext.setVisibility(View.INVISIBLE);
			}

			// 显示左边导航图片
			if (arg0 > 0 && arg0 <= pageCount) {
				imgViewPrevious.setVisibility(View.VISIBLE);
			} else {
				imgViewPrevious.setVisibility(View.INVISIBLE);
			}
		}
	}
}
