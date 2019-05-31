package cn.edu.cuc.logindemo.userlayout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.DeviceInfoUtils;
import cn.edu.cuc.logindemo.domain.Channel;

/**
 * 顶部滑动菜单布局设置
 * @Description 顶部滑动菜单布局设置
 * @FileName SlideMenuLayout.java
 * @Author SongQing
 * @version v1.0
 *
 */
public class SlideMenuLayout {
	// 包含菜单的ArrayList
	private ArrayList<View> menuList = null;

	private Activity activity;
	private TextView textView = null;
	private int channelcount = 0;  		//菜单计数
	private final static int SLIDEMENU_TEXTSIZE = 24;

	private Resources mResources;

	private static float scalesizeWidth;	//缩放比例(960*540的比例)
	private static int screenWidth;

	public SlideMenuLayout(Activity activity,ArrayList<Channel> channellist){

		this.activity = activity;
		menuList = new ArrayList<View>();
		mResources = activity.getResources();

		this.activity = activity;
		menuList = new ArrayList<View>();
		mResources = activity.getResources();

		this.setScreenSize();
	}

	/**
	 * 获取屏幕的宽高，依照此算出缩放比例
	 */
	private void setScreenSize(){
		DeviceInfoUtils dInfoHelper = new DeviceInfoUtils();
		float[] wdValues = dInfoHelper.getScreenWHPx(540.0f);
		screenWidth = (int) wdValues[0];
		scalesizeWidth = wdValues[2];
	}

	/**
	 * 顶部滑动菜单布局（基于Java的动态布局生成）
	 * @param menuTextViews
	 * @param layoutWidth
	 */
	public View getSlideMenuLinerLayout(ArrayList<Channel> channels,int layoutWidth){
		// 包含TextView的LinearLayout
		LinearLayout menuLinerLayout = new LinearLayout(activity);
		menuLinerLayout.setOrientation(LinearLayout.HORIZONTAL);
		menuLinerLayout.setGravity(Gravity.CENTER_VERTICAL);

		// 参数设置
		LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				1);

		LinearLayout.LayoutParams menuDividerLayoutParames = new LinearLayout.LayoutParams(
				(int)(scalesizeWidth*3), LinearLayout.LayoutParams.WRAP_CONTENT,1);
		menuDividerLayoutParames.gravity=Gravity.CENTER;
//		menuLinerLayoutParames.gravity = Gravity.CENTER_HORIZONTAL;
		menuLinerLayoutParames.gravity = Gravity.CENTER;
		// 添加TextView控件
		for(Channel channel:channels){
			TextView tvMenu = new TextView(activity);
			// 设置标识值
			tvMenu.setTag(channel.getId());
			tvMenu.setLayoutParams(new LayoutParams((int)(scalesizeWidth*(layoutWidth-3*channels.size()) / 5),LayoutParams.MATCH_PARENT));
			tvMenu.setPadding((int)(12*scalesizeWidth), (int)(5*scalesizeWidth), (int)(12*scalesizeWidth), (int)(5*scalesizeWidth));
			tvMenu.setText(channel.getName());
			tvMenu.setTextSize(TypedValue.COMPLEX_UNIT_PX,SLIDEMENU_TEXTSIZE * scalesizeWidth);
			ColorStateList colorList = (ColorStateList)mResources.getColorStateList(R.color.slidemenu_text);
			tvMenu.setTextColor(colorList);
			tvMenu.setGravity(Gravity.CENTER_HORIZONTAL);
			tvMenu.setOnClickListener(SlideMenuOnClickListener);

			// 菜单项计数
			channelcount ++;

			// 设置第一个菜单项背景
			if(channelcount == 1){
				tvMenu.setBackgroundResource(R.drawable.main_slidemenu_selected_bg);
				tvMenu.setSelected(true);
			}

			menuLinerLayout.addView(tvMenu,menuLinerLayoutParames);

			ImageView imageView = new ImageView(activity);
			imageView.setImageResource(R.drawable.main_slidemenu_divider);
			menuLinerLayout.addView(imageView,menuDividerLayoutParames);
			menuList.add(tvMenu);
		}

		return menuLinerLayout;
	}

	// 单个菜单事件
	OnClickListener SlideMenuOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String menuTag = v.getTag().toString();

			if(v.isClickable()){
				textView = (TextView)v;

				textView.setBackgroundResource(R.drawable.main_slidemenu_selected_bg);
				textView.setSelected(true);

				for(int i = 0;i < menuList.size();i++){
					if(!menuTag.equals(menuList.get(i).getTag().toString())){
						menuList.get(i).setBackground(null);
						menuList.get(i).setSelected(false);
					}
				}

				// 点击菜单时改变内容
				slideMenuOnChange(menuTag);
			}
		}
	};


	/**
	 * 点击时改内容
	 * @param menuTag
	 */
	private void slideMenuOnChange(String menuTag){
//		LayoutInflater inflater = activity.getLayoutInflater();
//		ViewGroup llc = (ViewGroup)activity.findViewById(R.id.linearLayoutContent);
//		llc.removeAllViews();

		Button btnCallBack = (Button)activity.findViewById(R.id.main_button_oncallback);
		btnCallBack.setContentDescription(menuTag);
		btnCallBack.performClick();

		//根据不同的TextView的Tag值，用对应的栏目id获取新闻列表(首先需要判断栏目类型，普通和图片加载的页面时有区别的)
		//新闻有焦点新闻，展示为多图的形式(中图)，剩下的就是新闻列表(小图)
//		ToastHelper.showToast(menuTag, Toast.LENGTH_SHORT);
	}
}