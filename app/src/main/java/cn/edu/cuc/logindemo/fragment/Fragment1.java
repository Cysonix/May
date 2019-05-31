package cn.edu.cuc.logindemo.fragment;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.ToastHelper;

public class Fragment1 extends Fragment {

	private EditText eTxtURL;
	private WebView webViewContent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment1, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initialize();
	}

	/**
	 * 初始化
	 */
	private void initialize(){

		eTxtURL = (EditText) getView().findViewById(R.id.fragment1_etxt_url);

		webViewContent = (WebView)getView().findViewById(R.id.fragment1_webview);
	}

	public void onKeyDownChild(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
			// 监听到回车键，会执行2次该方法。按下与松开
			String url = eTxtURL.getText().toString().trim();
			if(Patterns.WEB_URL.matcher(url).matches()){
				//符合url标准
				WebSettings webSettings = webViewContent.getSettings();
				//设置WebView属性，能够执行Javascript脚本
				webSettings.setJavaScriptEnabled(true);
				//设置可以访问文件
				webSettings.setAllowFileAccess(true);
				//设置支持缩放
				webSettings.setBuiltInZoomControls(true);
				//加载需要显示的新闻内容
				webViewContent.loadUrl(url);
				//设置Web视图
				webViewContent.setWebViewClient(new webViewClient ());
			}else{
				ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.url_validate_error),Toast.LENGTH_LONG);
			}
		}
	}
	//Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
