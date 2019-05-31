package cn.edu.cuc.logindemo.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.MediaUtils;
import cn.edu.cuc.logindemo.Utils.SyncImageLoader;
import cn.edu.cuc.logindemo.Utils.ToastHelper;
import cn.edu.cuc.logindemo.domain.NewsItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 新闻列表Adapter
 */
public class ChannelNewsListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<NewsItem> newsList;

	private Bitmap defaultLoadingBitmap;
	private ListView mListView;

	private SyncImageLoader syncImageLoader;

	private int layout;

	public ChannelNewsListAdapter(Context context, ListView listview) {
		this.mContext = context;
		this.newsList = new ArrayList<NewsItem>();
		this.mInflater = LayoutInflater.from(mContext);

		defaultLoadingBitmap = MediaUtils.getDefaultLoadingBitmap();

		mListView = listview;
		syncImageLoader = new SyncImageLoader(this.mContext);
		mListView.setOnScrollListener(onScrollListener);

		layout = R.layout.article_item;
	}

	public ChannelNewsListAdapter(Context context, ListView listview, int layout) {
		this.mContext = context;
		this.newsList = new ArrayList<NewsItem>();
		this.mInflater = LayoutInflater.from(mContext);

		defaultLoadingBitmap = MediaUtils.getDefaultLoadingBitmap();

		mListView = listview;
		syncImageLoader = new SyncImageLoader(this.mContext);
		mListView.setOnScrollListener(onScrollListener);	

		this.layout = layout;
	}

	public void setItems(List<NewsItem> tempList) {
		newsList = tempList;

		syncImageLoader.restore();
	}

	public void deleteAllItems(){
		newsList.clear();
	}
	
	public void delete(int position){
		newsList.remove(position);
	}
	
	public void addItems(List<NewsItem> tempList) {
		newsList.addAll(tempList);
	}

	public void addItems(int position, List<NewsItem> tempList) {

		newsList.addAll(0, tempList);
	}
	
	public void clearCache(){
		syncImageLoader.clearCacheBitmap();
	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public NewsItem getItem(int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(this.layout, parent, false);
		}

		if (position >= this.newsList.size())
			return convertView;

		final NewsItem news = this.newsList.get(position);

		TextView textViewTitle_NewsSubItem = (TextView) convertView.findViewById(R.id.textViewTitle);
		TextView textViewContent_NewsSubItem = (TextView) convertView.findViewById(R.id.textViewContent);
		TextView textViewTime_NewsSubItem = (TextView) convertView.findViewById(R.id.textViewTime);

		ImageView imageViewTitle_NewsSubItem = (ImageView) convertView.findViewById(R.id.imageViewTitle);
		imageViewTitle_NewsSubItem.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ToastHelper.showToast(news.getTitle(), Toast.LENGTH_LONG);}
		});
		if (news == null)
			return null;

		String title = news.getTitle();
		if (title != null) {
			textViewTitle_NewsSubItem.setText(title);
		}
		if (news.getAbstractString() != null) {
			String content = news.getAbstractString();
			textViewContent_NewsSubItem.setText(content);
		}
		if(news.getCreateDate()!=null){
			String date = news.getCreateDate();
			textViewTime_NewsSubItem.setText(date);
		}

		boolean photostate = true;
		if (photostate) {
			if (news.getImageHref() == null) {
				imageViewTitle_NewsSubItem.setImageBitmap(defaultLoadingBitmap);
				imageViewTitle_NewsSubItem.setVisibility(View.GONE);
			} else {
				imageViewTitle_NewsSubItem.setVisibility(View.VISIBLE);
				Bitmap bitmap = syncImageLoader.getCacheBitmap(news.getImageHref());

				if (bitmap == null) {
					imageViewTitle_NewsSubItem.setImageBitmap(defaultLoadingBitmap);
					syncImageLoader.loadImage(position,news.getImageHref(),
							imageLoadListener, imageViewTitle_NewsSubItem);
				} else {
					imageViewTitle_NewsSubItem.setImageBitmap(bitmap);
				}
			}
		} else {
			if (news.getImageHref() != null) {
				imageViewTitle_NewsSubItem.setImageBitmap(defaultLoadingBitmap);
				imageViewTitle_NewsSubItem.setVisibility(View.VISIBLE);
			} else {
				imageViewTitle_NewsSubItem.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

		public void onImageLoad(int position, Bitmap bitmap, ImageView imageView) {

			if (bitmap != null)
				imageView.setImageBitmap(bitmap);
		}
	};

	public void loadImage() {
		int start = mListView.getFirstVisiblePosition();
		int end = mListView.getLastVisiblePosition();
		if (end >= getCount()) {
			end = getCount() - 1;
		}
		syncImageLoader.setLoadLimit(start, end);
		syncImageLoader.unlock();
	}

	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				syncImageLoader.lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				loadImage();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				syncImageLoader.lock();
				break;

			default:
				break;
			}
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	};
}
