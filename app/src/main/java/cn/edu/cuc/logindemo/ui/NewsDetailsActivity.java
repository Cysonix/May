package cn.edu.cuc.logindemo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.domain.NewsItem;
import cn.edu.cuc.logindemo.logic.NewsItemLogic;

/**
 * WebView显示具体新闻网页内容
 *
 *  @author songqing
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class NewsDetailsActivity extends Activity{
	private String newsID;
	private int channelID;
	private WebView webview;
	private NewsItemLogic newsItemLogic;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.details);

		getIntentExtras();
		initialize();
	}

	/**
	 * 获取新闻列表页的传值
	 */
	private void getIntentExtras(){

		channelID = getIntent().getIntExtra("ChannelID",0);
		newsID =  getIntent().getStringExtra("NewsID");
	}

	/**
	 * 初始化
	 */
	private void initialize(){
		newsItemLogic = new NewsItemLogic(this);
		setUpViews();
	}

	/**
	 * 初始化页面控件
	 */
	private void setUpViews(){

		webview = (WebView) findViewById(R.id.details_webview);
		WebSettings webSettings = webview.getSettings();
		//设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		//设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		//加载需要显示的新闻内容
		webview.loadUrl(getNewsContentUrl());
		//设置Web视图
		webview.setWebViewClient(new webViewClient ());
	}

	/**
	 * 获取新闻详细内容URL
	 * @return
	 */
	private String getNewsContentUrl(){
		String returnValueString = "";
		NewsItem item = newsItemLogic.get(newsID, channelID);
		if(item !=null){
			returnValueString = item.getContentHref();
		}

		return returnValueString;
	}
	//Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
