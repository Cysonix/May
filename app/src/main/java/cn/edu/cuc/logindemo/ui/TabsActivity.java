package cn.edu.cuc.logindemo.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.BackHandlerHelper;
import cn.edu.cuc.logindemo.fragment.AboutFragment;
import cn.edu.cuc.logindemo.fragment.Fragment1;
import cn.edu.cuc.logindemo.fragment.Fragment2;
import cn.edu.cuc.logindemo.fragment.HomeFragment;
import cn.edu.cuc.logindemo.fragment.MoreFragment;
import cn.edu.cuc.logindemo.fragment.MusicListFragment;
import cn.edu.cuc.logindemo.fragment.MusicListFragment0;

public class TabsActivity extends FragmentActivity{
	
	/**
	 * FragmentTabhost
	 */
	private FragmentTabHost mTabHost;
	
	/**
	 * 布局填充器
	 * 
	 */
	private LayoutInflater mLayoutInflater;

	private Fragment fg;

	/**
	 * Fragment数组界面 
	 * 数组中的成员是 Fragment 相当于 TabActivity中的Activity(就是一个页面)
	 * 
	 */
	private Class mFragmentArray[] = { MusicListFragment0.class, HomeFragment.class,
			Fragment2.class, MoreFragment.class, AboutFragment.class };
	
	/**
	 * 存放图片数组
	 * 
	 */
	private int mImageArray[] = { R.drawable.tab_home_btn,
			R.drawable.tab_message_btn, R.drawable.tab_selfinfo_btn,
			R.drawable.tab_square_btn, R.drawable.tab_more_btn };

	/**
	 * 选项卡文字
	 * 
	 */
	private String mTextArray[] = { "音乐", "体育", "科技", "设置", "关于" };
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);

		initView();
	}
	
	/**
	 * 初始化组件
	 */
	private void initView() {
		//LayoutInflater这个类类似于findViewById()
	    //不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；
	    //而findViewById()是找xml布局文件下的具体widget控件(如Button、TextView等)
		mLayoutInflater = LayoutInflater.from(this);

		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);

		}
	}
	
	/**
	 *
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);

		return view;
	}

	@Override
	public void onBackPressed() {
		if (!BackHandlerHelper.handleBackPress(this)) {
			super.onBackPressed();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		fg = getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
		((Fragment1) fg).onKeyDownChild(event.getKeyCode(), event);
		return super.dispatchKeyEvent(event);
	}
}
