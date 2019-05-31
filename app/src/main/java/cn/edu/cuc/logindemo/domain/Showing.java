package cn.edu.cuc.logindemo.domain;

import android.graphics.Bitmap;

/**
 * @author SongQing
 */
public class Showing {
	private String text;						//对应News title
	private Bitmap bitmap;					//大图
	private String picUrl;						//图片地址
	private String newsid;					//对应新闻Id
	private String prop;						//新闻摘要

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * @return the picUrl
	 */
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * @param picUrl the picUrl to set
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	/**
	 * @return the prop
	 */
	public String getProp() {
		return prop;
	}

	/**
	 * @param prop the prop to set
	 */
	public void setProp(String prop) {
		this.prop = prop;
	}

	public String getNewsid() {
		return newsid;
	}

	public void setNewsid(String newsid) {
		this.newsid = newsid;
	}

}
