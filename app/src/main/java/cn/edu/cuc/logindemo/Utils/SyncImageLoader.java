package cn.edu.cuc.logindemo.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.domain.Enums;

/**
 * 图片同步加载
 */
public class SyncImageLoader {

	private Object lock = new Object();
	private boolean mAllowLoad = true;	//当前是否允许加载图片（滑动和点击的时候不可以）
	private boolean firstLoad = true;		//是否是第一次加载
	private int mStartLoadLimit = 0;
	private int mStopLoadLimit = 0;

	private Context context = null;
	private boolean resize = false;
	final Handler handler = new Handler();

	private HashMap<String, Bitmap> imageCache = null;

	/**
	 * 构造函数，创建Cache对象image
	 * @param context
	 */
	public SyncImageLoader(Context context) {
		this.context = context;
		if (imageCache == null)
			imageCache = new HashMap<String, Bitmap>();
	}

	/**
	 * 构造函数，创建ImageCache对象，
	 * 可以指定图像大小是否可以改变 resize标识位
	 * @param context
	 * @param resize
	 */
	public SyncImageLoader(Context context, boolean resize) {
		this.context = context;
		this.resize = resize;
		if (imageCache == null)
			imageCache = new HashMap<String, Bitmap>();
	}

	/**
	 * 图片加载监听接口
	 */
	public interface OnImageLoadListener {
		public void onImageLoad(int position, Bitmap bitmap, ImageView imageView);
	}

	/**
	 * 清空ImageCache
	 */
	public void clearCacheBitmap() {

		if (imageCache == null)
			return;

		Iterator iter = imageCache.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			try {
				Bitmap bitmap = (Bitmap) entry.getValue();
				if (!bitmap.isRecycled())
					bitmap.recycle();
			} catch (Exception e) {
				LogUtils.d(e.getMessage());
			}
		}
		imageCache.clear();
	}

	/**
	 * 以键值对的方式将图片缓存到imageCache中
	 * @param bitMap
	 * @param url
	 */
	public	void putCaheBitmap(Bitmap bitMap,String url)
	{
		imageCache.put(url, bitMap);
	}

	/**
	 * 通过url从图片缓存中获取图片
	 * @param url
	 * @return
	 */
	public Bitmap getCacheBitmap(String url) {

		if (imageCache == null)
			return null;

		Bitmap bitmap = imageCache.get(url);

		if (bitmap == null)
			return bitmap;

		try {
			if (bitmap.isRecycled()) {//清除BitmaapCache中缓存的已经被系统回收的键值对
				bitmap = null;
				imageCache.remove(url);
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return bitmap;
	}

	/**
	 * 设置加载
	 * @param startLoadLimit
	 * @param stopLoadLimit
	 */
	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
		if (startLoadLimit > stopLoadLimit) {
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStopLoadLimit = stopLoadLimit;
	}

	public void restore() {
		mAllowLoad = true;
		firstLoad = true;
	}

	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	public void unlock() {
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	/**
	 * 加载图片
	 * @param t
	 * @param url
	 * @param listener
	 * @param imageView
	 */
	public void loadImage(int t, final String url,OnImageLoadListener listener, ImageView imageView) {
		final OnImageLoadListener mListener = listener;
		final int mt = t;
		final ImageView mImageView = imageView;

		new Thread(new Runnable() {

			public void run() {
				if (!mAllowLoad) {
					Log.i(AndroidApplication.TAG, "prepare to load");
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (mAllowLoad && firstLoad) {
					loadImage(url, mListener, mImageView, mt);
				}
				else if (mAllowLoad && mt >= mStartLoadLimit
						&& mt <= mStopLoadLimit) {
					loadImage(url, mListener, mImageView, mt);
				}
			}

		}).start();
	}

	/**
	 * 加载图片
	 * @param url
	 * @param mListener
	 * @param imageView
	 * @param position
	 */
	private void loadImage(final String url,final OnImageLoadListener mListener, final ImageView imageView,final int position) {

		try {

			Bitmap tempBitmap = null;

			if (resize == false)
				tempBitmap = CacheUtils.createBitmapFromCache(url);
			else
				tempBitmap = CacheUtils.createBitmapFromCacheResize(url);

			if (tempBitmap == null && AndroidApplication.getNetStatus() != Enums.NetStatus.Disable) {

				tempBitmap = CacheUtils.fetchImageFromRemote(url);

				// ���浽����
				if (tempBitmap != null)
					CacheUtils.saveSource2Cache(tempBitmap, url);
			}

			final Bitmap bitmap = tempBitmap;

			if (bitmap != null) {
				// ���浽�ڴ�
				// imageCache.put(url, new SoftReference<Bitmap>(bitmap));
				imageCache.put(url, bitmap);
			}

			handler.post(new Runnable() {
				public void run() {
					if (mAllowLoad) {
						mListener.onImageLoad(position, bitmap, imageView);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.e(e);
		}
	}
}
