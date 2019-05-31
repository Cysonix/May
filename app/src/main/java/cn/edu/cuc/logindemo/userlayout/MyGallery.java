package cn.edu.cuc.logindemo.userlayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.DeviceInfoUtils;
import cn.edu.cuc.logindemo.Utils.MediaUtils;
import cn.edu.cuc.logindemo.Utils.SyncImageLoader;
import cn.edu.cuc.logindemo.domain.Channel;
import cn.edu.cuc.logindemo.domain.Showing;
import cn.edu.cuc.logindemo.ui.NewsDetailsActivity;

/**
 * 多图左右轮换显示
 * @Package cn.edu.cuc.logindemo.userlayout
 * @Author SongQing
 * @date 2019-4-9
 * @version v1.0
 *
 */
@SuppressWarnings("deprecation")
public class MyGallery extends FrameLayout {
	List<Showing> showings = new ArrayList<Showing>();
	private static final double GALLERY_IMAGE_HORIZONTAL_RATIO = 1.0;
	private static final double GALLERY_IMAGE_VERTICAL_RATIO = 1.0;
	private static final int GALLERY_SPACING = 2;
	private static final int TIP_FONT_SIZE=22;
	private float scalesizeWidth;
	private Context mContext;
	private PromotionImages promotionImages;
	private LinearLayout mBottomLayout;
	private View viewNavigation1,viewNavigation2,viewNavigation3,viewNavigation4,viewNavigation5;
	private TextView tips;
	private int mPcount;
	private ImageView[] icons;
	private View[] navigations = new View[5];
	private static final int[] navigationColors={Color.parseColor("#6C6C6C"),
			Color.parseColor("#8E8E8E"),
			Color.parseColor("#ADADAD"),
			Color.parseColor("#d0d0d0"),
			Color.parseColor("#F0F0F0")};
	private SyncImageLoader syncImageLoader;
	Bitmap defaultLoadingBitmap = MediaUtils.getDefaultLoadingBitmap();

	private Channel currentChannel;

	public MyGallery(Context context) {
		super(context);
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setScreenSize();
		promotionImages = new PromotionImages(context);
		LayoutParams layoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.bottomMargin = 20;
		promotionImages.setFadingEdgeLength(0);
		this.addView(promotionImages, layoutParams);

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View indicator = inflater.inflate(R.layout.promotion_hint, null);

		this.addView(indicator);
		//
		mBottomLayout = (LinearLayout) indicator.findViewById(R.id.promotion_index_layout);
		tips = (TextView) indicator.findViewById(R.id.promotion_tip);
		initBottomIcons();
		syncImageLoader = new SyncImageLoader(this.mContext);
	}

	/**
	 * 获取屏幕的宽高，依照此算出缩放比例
	 */
	private void setScreenSize(){
		DeviceInfoUtils dInfoHelper = new DeviceInfoUtils();
		float[] wdValues = dInfoHelper.getScreenWHPx(540.0f);

		scalesizeWidth = wdValues[2];
	}

	@SuppressLint("InflateParams")
	public void initMyGallery(Context context, List<Showing> showings, Channel currentChannel) {

		this.currentChannel = currentChannel;

		this.removeAllViews();

		this.showings = showings;
		mContext = context;
		promotionImages = new PromotionImages(context);
		LayoutParams layoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.bottomMargin = 0;

		promotionImages.setFadingEdgeLength(0);

		this.addView(promotionImages, layoutParams);
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View indicator = inflater.inflate(R.layout.promotion_hint, null);
		this.addView(indicator);

		mBottomLayout = (LinearLayout) indicator.findViewById(R.id.promotion_index_layout);
		tips = (TextView) indicator.findViewById(R.id.promotion_tip);
		tips.setTextSize(TypedValue.COMPLEX_UNIT_PX,TIP_FONT_SIZE * scalesizeWidth);
		initBottomIcons();

		this.viewNavigation1 = (View)indicator.findViewById(R.id.main_view_gallery_navigation1);
		navigations[0]=viewNavigation1;
		this.viewNavigation2 = (View)indicator.findViewById(R.id.main_view_gallery_navigation2);
		navigations[1]=viewNavigation2;
		this.viewNavigation3 = (View)indicator.findViewById(R.id.main_view_gallery_navigation3);
		navigations[2]=viewNavigation3;
		this.viewNavigation4 = (View)indicator.findViewById(R.id.main_view_gallery_navigation4);
		navigations[3]=viewNavigation4;
		this.viewNavigation5 = (View)indicator.findViewById(R.id.main_view_gallery_navigation5);
		navigations[4]=viewNavigation5;
	}

	public void notifyDataSetChanged() {
		promotionImages.promotionAdapter.notifyDataSetChanged();
	}

	class PromotionImages extends Gallery {

		PromotionImagesAdapter promotionAdapter;

		public PromotionImages(Context context) {
			super(context);
			this.setSpacing(GALLERY_SPACING);
			promotionAdapter = new PromotionImagesAdapter(context);
			this.setAdapter(promotionAdapter);

			this.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view,
										   int position, long id) {

					update(position % showings.size());
				}

				public void onNothingSelected(AdapterView<?> parent) {

				}

			});
			this.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
					Showing s = showings.get(position % showings.size());
					Intent intent = null;
					intent = new Intent(mContext,NewsDetailsActivity.class);

					intent.putExtra("ChannelID", currentChannel.getId());
					intent.putExtra("NewsID", s.getNewsid());
					mContext.startActivity(intent);
				}
			});
		}

		private final void update(int selected) {
			tips.setText(showings.get(selected).getText());
			for (int i = 0; i < mPcount; i++) {
				if (selected == i) {
					icons[i].setBackground(getResources().getDrawable(
							R.drawable.main_imagegallery_activation_of_headlines));
					navigations[i].setBackground(getResources().getDrawable(R.drawable.main_imagegallery_selected));
				} else {
					icons[i].setBackground(getResources().getDrawable(
							R.drawable.main_imagegallery_headline_is_not_activated));
					navigations[i].setBackgroundColor(navigationColors[i]);
				}
			}
		}

		private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
			return e2.getX() > e1.getX();
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							   float velocityY) {
			int keyCode;
			if (isScrollingLeft(e1, e2)) {
				keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			} else {
				keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			}
			onKeyDown(keyCode, null);
			return true;
		}

		public void moveNext(){
			onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		}
	}

	private final void initBottomIcons() {
		mPcount = showings.size();
		icons = new ImageView[mPcount];
		mBottomLayout.removeAllViews();

		for (int i = 0; i < mPcount; i++) {
			icons[i] = new ImageView(mContext);
			if (i == 0){
				icons[i].setBackground(getResources().getDrawable(
						R.drawable.main_imagegallery_activation_of_headlines));
				this.navigations[i].setBackground(getResources().getDrawable(R.drawable.main_imagegallery_selected));
			}else{
				icons[i].setBackground(getResources().getDrawable(
						R.drawable.main_imagegallery_headline_is_not_activated));
				navigations[i].setBackgroundColor(navigationColors[i]);
			}
			icons[i].setPadding(0, 0, 5, 0);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.rightMargin = 10;

			mBottomLayout.addView(icons[i], layoutParams);
		}
	}

	private final class PromotionImagesAdapter extends BaseAdapter {
		public PromotionImagesAdapter(Context context) {

		}

		public int getCount() {
			return showings.size();
//			if (showings.size() == 0)
//				return 0;
//			else
//				return Integer.MAX_VALUE;
		}

		public Showing getItem(int position) {
			return showings.get(position);

		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, final View convertView,
							ViewGroup parent) {
			// System.out.println("showings = " + showings.size());
			// System.out.println("position=" + position);
			final Showing showing = showings.get(position % showings.size());
//			final Showing showing = showings.get(position);
			ImageView promotionImage = (ImageView) convertView;

			if (promotionImage == null) {
				promotionImage = new ImageView(mContext);
			}

			int width = (int) (MyGallery.this.getWidth() * GALLERY_IMAGE_HORIZONTAL_RATIO);
			int height = (int) (MyGallery.this.getHeight() * GALLERY_IMAGE_VERTICAL_RATIO);

			// 可以根据保存在SharePreference中的值判断是否无图浏览模式
			boolean photostate = true;
			if (photostate == true) {

				if (showing.getPicUrl() == null) {
					promotionImage.setImageBitmap(defaultLoadingBitmap);
					promotionImage.setScaleType(ImageView.ScaleType.FIT_XY);
					// promotionImage.setVisibility(View.GONE);
				} else {
					// promotionImage.setVisibility(View.VISIBLE);
					Bitmap bitmap = syncImageLoader.getCacheBitmap(showing.getPicUrl());

					// if (bitmap == null) {
					// bitmap = CacheHelper.fetchImageFromRemote(picurl);
					// }
					if (bitmap == null) {
						promotionImage.setImageBitmap(defaultLoadingBitmap);
						promotionImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
						syncImageLoader.loadImage(position, showing.getPicUrl(),
								imageLoadListener, promotionImage);
					} else {
						promotionImage.setImageBitmap(bitmap);
					}

					promotionImage.setLayoutParams(new Gallery.LayoutParams(
							width, height));
					if (bitmap != null) {
						if (bitmap.getHeight() > bitmap.getWidth())
							promotionImage
									.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
						else {
							promotionImage
									.setScaleType(ImageView.ScaleType.CENTER_CROP);
						}
					}
				}
			} else {
				promotionImage.setImageBitmap(defaultLoadingBitmap);
				promotionImage.setScaleType(ImageView.ScaleType.FIT_XY);
				promotionImage.setLayoutParams(new Gallery.LayoutParams(width,height));
			}

			return promotionImage;
		}
	}

	/**
	 * @return the showings
	 */
	public List<Showing> getShowings() {
		return showings;
	}

	/**
	 * 声明一个监听器，用作异步加载图片的回调
	 */
	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

		public void onImageLoad(int position, Bitmap bitmap, ImageView imageView) {

			if (bitmap != null) {
				if (bitmap.getHeight() > bitmap.getWidth())
					imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				else {
					imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				}

				imageView.setImageBitmap(bitmap);
			}

		}
	};

	public void setShowings(List<Showing> showings2) {
		this.showings = showings2;
	}
}
