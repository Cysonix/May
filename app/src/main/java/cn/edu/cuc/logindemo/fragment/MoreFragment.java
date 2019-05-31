package cn.edu.cuc.logindemo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.CacheUtils;
import cn.edu.cuc.logindemo.Utils.DeviceInfoUtils;
import cn.edu.cuc.logindemo.Utils.DialogHelper;
import cn.edu.cuc.logindemo.Utils.LogUtils;
import cn.edu.cuc.logindemo.Utils.SharedPreferencesHelper;
import cn.edu.cuc.logindemo.Utils.StandardizationDataUtils;
import cn.edu.cuc.logindemo.Utils.ToastHelper;
import cn.edu.cuc.logindemo.domain.Enums;

/**
 * 设置页面
 */
public class MoreFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout rLayoutBanner,
            rLayoutTab,
            rLayoutContent,
            rLayoutFontsize,
            rLayoutSetNoimage,
            rLayoutSetCleancache,
            rLayoutSetContactus,
            rLayoutSetUpdate,
            rLayoutSetAlarmVoice,
            rLayoutSetPushSwitch,
            rLayoutSetBindWeibo,
            rLayoutSetVote;

    private ScrollView scrollViewContainer;

    private TextView tViewBanner,
            tViewCollecton,
            tViewMsgpush,
            tViewFontSizeTitle,
            tViewFontSizeValue,
            tViewSetNoimage,
            tViewSetCleancache,
            tViewSetContactus,
            tViewCacheSize,
            tViewSetUpdate,
            tViewSetAlarmVoiceTitle,
            tViewSetAlarmVoiceValue,
            tViewSetPushSwitch,
            tViewSetBindWeibo,
            tViewSetVote;

    private ImageView imViewFontSize,
            imViewSetNoimageBg,
            imViewSetNoimageIcon,
            imViewSetContactus,
            imViewSetUpdate,
            imViewSetAlarmVoice,
            imViewTabDivider,
            imViewSetBindWeibo,
            imViewSetPushSwitchBg,
            imViewSetPushSwitchIcon,
            imViewSetVote,
            imViewContentDivider1,
            imViewContentDivider2,
            imViewContentDivider3,
            imViewContentDivider4,
            imViewContentDivider5,
            imViewContentDivider6,
            imViewContentDivider7,
            imViewContentDivider8;

    private static int screenWidth;
    private static float scalesizeWidth;

    private final static int BANNER_FONT_SIZE = 34;			//banner区域内的文字大小(对于960*540的标准)
    private final static int TAB_FONT_SIZE = 20;			//Tab条区域内的的文字大小(对于960*540的标准)
    private final static int CONTENT_FONT_SIZE = 22;		//设置内容区域内的文字大小(对于960*540的标准)
    private final static int DISTANCE_BETWEEN_TEXTVIEW_TABDIVIDER = 60;		//设置tab区域 文字和中间分割线的距离(对于960*540的标准)
    private final static int DISTANCE_TO_TEXTVIEW_LEFT = 10;			//设置功能区域 设置标题的左边距
    private final static int DISTANCE_TO_IMAGEVIEW_RIGHT = 15;			//设置功能区域 设置按钮的右边距
    private final static int DISTANCE_BETWEEN_IMAGEVIEW_TEXTVIEW = 10;  //设置功能区域 设置按钮和设置值之间的距离
    private final static int DISTANCE_DIVIDER_TO_CONTAINER = 5;  		//设置功能区域 横向分隔线的左右边距

    private final static int IMAGEVIEW_PADING = 12;	//图片按钮增加点击区域

    private static final int CACHE_CLEAR_COMPLETE = 0;
    private static final int UPDATE_APP = 1;


    private SharedPreferencesHelper sHelper;
    private String currentNoimageStatus = "true";		//有图
    private int currentFontSize = 1;					//中号
    private long currentCacheSize = 0;				//0MB
    private int currentAlarmVoice = 0;				//男声
    private String currentPushSwitchStatus = "true";	//推送打开

    private MediaPlayer mediaPlayer;

    private DialogHelper dialogHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.initCacheSize();
    }

    /**
     * 初始化
     */
    private void initialize(){

        sHelper = new SharedPreferencesHelper(getContext());
        dialogHelper = new DialogHelper(getContext());

        this.setScreenSize();
        this.setUpViews();
        this.initValue();
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
     * 初始化页面控件
     */
    private void setUpViews(){

        RelativeLayout.LayoutParams layoutParams_info;

        /*******Banner区域******/
        rLayoutBanner = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_banner);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rLayoutBanner.setLayoutParams(layoutParams_info);

        tViewBanner = (TextView)getView().findViewById(R.id.more_textview_banner_title);
        tViewBanner.setTextSize(TypedValue.COMPLEX_UNIT_PX,BANNER_FONT_SIZE * scalesizeWidth);

        scrollViewContainer = (ScrollView)getView().findViewById(R.id.more_scrollview_content);

        /*******Tab区域******/
        rLayoutTab = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_tab_container);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(150 * scalesizeWidth));
        layoutParams_info.setMargins((int)(scalesizeWidth*15), (int)(scalesizeWidth*10), (int)(scalesizeWidth*15), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_banner);
        rLayoutTab.setLayoutParams(layoutParams_info);

        imViewTabDivider = (ImageView)getView().findViewById(R.id.more_imageview_tab_divider);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams_info.setMargins(0, (int)(scalesizeWidth*2), 0, (int)(scalesizeWidth*2));
        layoutParams_info.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imViewTabDivider.setLayoutParams(layoutParams_info);

        tViewCollecton = (TextView)getView().findViewById(R.id.more_textview_tab_mycollection);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_BETWEEN_TEXTVIEW_TABDIVIDER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.LEFT_OF, R.id.more_imageview_tab_divider);
        layoutParams_info.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        tViewCollecton.setLayoutParams(layoutParams_info);
        tViewCollecton.setTextSize(TypedValue.COMPLEX_UNIT_PX,TAB_FONT_SIZE * scalesizeWidth);
        tViewCollecton.setOnClickListener(this);

        tViewMsgpush = (TextView)getView().findViewById(R.id.more_textview_tab_msgpush);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_BETWEEN_TEXTVIEW_TABDIVIDER*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.RIGHT_OF, R.id.more_imageview_tab_divider);
        layoutParams_info.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        tViewMsgpush.setLayoutParams(layoutParams_info);
        tViewMsgpush.setTextSize(TypedValue.COMPLEX_UNIT_PX,TAB_FONT_SIZE * scalesizeWidth);
        tViewMsgpush.setOnClickListener(this);


        int imagePadding = (int)(IMAGEVIEW_PADING*scalesizeWidth);

        /*******设置功能区域******/
        rLayoutContent = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_content_container);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_tab_container);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        layoutParams_info.setMargins((int)(scalesizeWidth*15), (int)(scalesizeWidth*10), (int)(scalesizeWidth*15), (int)(scalesizeWidth*10));
        rLayoutContent.setLayoutParams(layoutParams_info);

        //正文字号
        rLayoutFontsize = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_fontsize);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        rLayoutFontsize.setLayoutParams(layoutParams_info);

        tViewFontSizeTitle = (TextView)getView().findViewById(R.id.more_textview_fontsize_title);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewFontSizeTitle.setLayoutParams(layoutParams_info);
        tViewFontSizeTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewFontSizeTitle.setOnClickListener(this);

        imViewFontSize = (ImageView)getView().findViewById(R.id.more_imageview_fontsize_arrow);
        imViewFontSize.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewFontSize.setLayoutParams(layoutParams_info);
        imViewFontSize.setOnClickListener(this);

        tViewFontSizeValue = (TextView)getView().findViewById(R.id.more_textview_fontsize);
        tViewFontSizeValue.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_BETWEEN_IMAGEVIEW_TEXTVIEW*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.LEFT_OF, R.id.more_imageview_fontsize_arrow);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewFontSizeValue.setLayoutParams(layoutParams_info);
        tViewFontSizeValue.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewFontSizeValue.setOnClickListener(this);

        imViewContentDivider1 = (ImageView)getView().findViewById(R.id.more_imageview_divider1);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_fontsize);
        imViewContentDivider1.setLayoutParams(layoutParams_info);

        //无图模式
        rLayoutSetNoimage = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_setnoimage);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider1);
        rLayoutSetNoimage.setLayoutParams(layoutParams_info);

        tViewSetNoimage = (TextView)getView().findViewById(R.id.more_textview_setnoimage);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetNoimage.setLayoutParams(layoutParams_info);
        tViewSetNoimage.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetNoimage.setOnClickListener(this);

        imViewSetNoimageBg = (ImageView)getView().findViewById(R.id.more_imageview_setnoimage_selectbg);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetNoimageBg.setLayoutParams(layoutParams_info);
        imViewSetNoimageBg.setOnClickListener(this);

        imViewSetNoimageIcon = (ImageView)getView().findViewById(R.id.more_imageview_setnoimage_selecticon);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.addRule(RelativeLayout.ALIGN_RIGHT, R.id.more_imageview_setnoimage_selectbg);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetNoimageIcon.setLayoutParams(layoutParams_info);
        imViewSetNoimageIcon.setOnClickListener(this);

        imViewContentDivider2 = (ImageView)getView().findViewById(R.id.more_imageview_divider2);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_setnoimage);
        imViewContentDivider2.setLayoutParams(layoutParams_info);

        //清理缓存
        rLayoutSetCleancache = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_cleancache);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider2);
        rLayoutSetCleancache.setLayoutParams(layoutParams_info);

        tViewSetCleancache = (TextView)getView().findViewById(R.id.more_textview_cleancache);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetCleancache.setLayoutParams(layoutParams_info);
        tViewSetCleancache.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetCleancache.setOnClickListener(this);

        tViewCacheSize = (TextView)getView().findViewById(R.id.more_textview_cachesize);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewCacheSize.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
        tViewCacheSize.setLayoutParams(layoutParams_info);
        tViewCacheSize.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewCacheSize.setOnClickListener(this);

        imViewContentDivider3 = (ImageView)getView().findViewById(R.id.more_imageview_divider3);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_cleancache);
        imViewContentDivider3.setLayoutParams(layoutParams_info);

        //版本检测
        rLayoutSetUpdate = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_update);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider3);
        rLayoutSetUpdate.setLayoutParams(layoutParams_info);

        tViewSetUpdate = (TextView)getView().findViewById(R.id.more_textview_update);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetUpdate.setLayoutParams(layoutParams_info);
        tViewSetUpdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetUpdate.setOnClickListener(this);

        imViewSetUpdate = (ImageView)getView().findViewById(R.id.more_imageview_update_arrow);
        imViewSetUpdate.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetUpdate.setLayoutParams(layoutParams_info);
        imViewSetUpdate.setOnClickListener(this);

        imViewContentDivider4 = (ImageView)getView().findViewById(R.id.more_imageview_divider4);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_update);
        imViewContentDivider4.setLayoutParams(layoutParams_info);

        //闹铃提示声音
        rLayoutSetAlarmVoice = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_alarmvoice);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider4);
        rLayoutSetAlarmVoice.setLayoutParams(layoutParams_info);

        tViewSetAlarmVoiceTitle = (TextView)getView().findViewById(R.id.more_textview_alarmvoice_title);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetAlarmVoiceTitle.setLayoutParams(layoutParams_info);
        tViewSetAlarmVoiceTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetAlarmVoiceTitle.setOnClickListener(this);

        imViewSetAlarmVoice = (ImageView)getView().findViewById(R.id.more_imageview_alarmvoice_arrow);
        imViewSetAlarmVoice.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetAlarmVoice.setLayoutParams(layoutParams_info);
        imViewSetAlarmVoice.setOnClickListener(this);

        tViewSetAlarmVoiceValue = (TextView)getView().findViewById(R.id.more_textview_alarmvoice);
        tViewSetAlarmVoiceValue.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_BETWEEN_IMAGEVIEW_TEXTVIEW*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.LEFT_OF, R.id.more_imageview_alarmvoice_arrow);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetAlarmVoiceValue.setLayoutParams(layoutParams_info);
        tViewSetAlarmVoiceValue.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetAlarmVoiceValue.setOnClickListener(this);

        imViewContentDivider5 = (ImageView)getView().findViewById(R.id.more_imageview_divider5);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_alarmvoice);
        imViewContentDivider5.setLayoutParams(layoutParams_info);

        //消息推送
        rLayoutSetPushSwitch = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_pushswitch);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider5);
        rLayoutSetPushSwitch.setLayoutParams(layoutParams_info);

        tViewSetPushSwitch = (TextView)getView().findViewById(R.id.more_textview_pushswitch);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetPushSwitch.setLayoutParams(layoutParams_info);
        tViewSetPushSwitch.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetPushSwitch.setOnClickListener(this);

        imViewSetPushSwitchBg = (ImageView)getView().findViewById(R.id.more_imageview_pushswitch_selectbg);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetPushSwitchBg.setLayoutParams(layoutParams_info);
        imViewSetPushSwitchBg.setOnClickListener(this);

        imViewSetPushSwitchIcon = (ImageView)getView().findViewById(R.id.more_imageview_pushswitch_selecticon);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.addRule(RelativeLayout.ALIGN_RIGHT, R.id.more_imageview_pushswitch_selectbg);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetPushSwitchIcon.setLayoutParams(layoutParams_info);
        imViewSetPushSwitchIcon.setOnClickListener(this);

        imViewContentDivider6 = (ImageView)getView().findViewById(R.id.more_imageview_divider6);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_pushswitch);
        imViewContentDivider6.setLayoutParams(layoutParams_info);

        //绑定微博
        rLayoutSetBindWeibo = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_bindweibo);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider6);
        rLayoutSetBindWeibo.setLayoutParams(layoutParams_info);

        tViewSetBindWeibo = (TextView)getView().findViewById(R.id.more_textview_bindweibo);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetBindWeibo.setLayoutParams(layoutParams_info);
        tViewSetBindWeibo.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetBindWeibo.setOnClickListener(this);

        imViewSetBindWeibo = (ImageView)getView().findViewById(R.id.more_imageview_bindweibo_arrow);
        imViewSetBindWeibo.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetBindWeibo.setLayoutParams(layoutParams_info);
        imViewSetBindWeibo.setOnClickListener(this);

        imViewContentDivider7 = (ImageView)getView().findViewById(R.id.more_imageview_divider7);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_bindweibo);
        imViewContentDivider7.setLayoutParams(layoutParams_info);

        //投票资料
        rLayoutSetVote = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_vote);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider7);
        rLayoutSetVote.setLayoutParams(layoutParams_info);

        tViewSetVote = (TextView)getView().findViewById(R.id.more_textview_vote);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetVote.setLayoutParams(layoutParams_info);
        tViewSetVote.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetVote.setOnClickListener(this);

        imViewSetVote = (ImageView)getView().findViewById(R.id.more_imageview_vote_arrow);
        imViewSetVote.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetVote.setLayoutParams(layoutParams_info);
        imViewSetVote.setOnClickListener(this);

        imViewContentDivider8 = (ImageView)getView().findViewById(R.id.more_imageview_divider8);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0, (int)(DISTANCE_DIVIDER_TO_CONTAINER*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.BELOW, R.id.more_relativelayout_vote);
        imViewContentDivider8.setLayoutParams(layoutParams_info);

        //联系我们
        rLayoutSetContactus = (RelativeLayout)getView().findViewById(R.id.more_relativelayout_contactus);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(scalesizeWidth*75));
        layoutParams_info.addRule(RelativeLayout.BELOW,R.id.more_imageview_divider8);
        rLayoutSetContactus.setLayoutParams(layoutParams_info);

        tViewSetContactus = (TextView)getView().findViewById(R.id.more_textview_contactus);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins((int)(DISTANCE_TO_TEXTVIEW_LEFT*scalesizeWidth), 0, 0, 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tViewSetContactus.setLayoutParams(layoutParams_info);
        tViewSetContactus.setTextSize(TypedValue.COMPLEX_UNIT_PX,CONTENT_FONT_SIZE * scalesizeWidth);
        tViewSetContactus.setOnClickListener(this);

        imViewSetContactus = (ImageView)getView().findViewById(R.id.more_imageview_contactus_arrow);
        imViewSetContactus.setPadding(imagePadding,imagePadding,imagePadding,imagePadding);
        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.setMargins(0, 0, (int)(DISTANCE_TO_IMAGEVIEW_RIGHT*scalesizeWidth), 0);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetContactus.setLayoutParams(layoutParams_info);
        imViewSetContactus.setOnClickListener(this);
    }

    /**
     * 从SharePreference中取出相关配置值，并为页面控件赋值
     */
    private void initValue(){

        initFontSize();
        initNoimageIcon();
        initCacheSize();
        initAlarmVoice();
        initPushSwitch();
    }
    /**
     * 设置正文字体大小当前值
     */
    private void initFontSize(){

        String fontSizeString = sHelper.getPreferenceValue(Enums.PreferenceKeys.Sys_FontSize.toString());
        currentFontSize = (fontSizeString==""?Integer.parseInt("1"):Integer.parseInt(fontSizeString));

        switch(currentFontSize){
            case 0:
                tViewFontSizeValue.setText(getResources().getString(R.string.more_content_fontsize_small));
                break;
            case 1:
                tViewFontSizeValue.setText(getResources().getString(R.string.more_content_fontsize_middle));
                break;
            case 2:
                tViewFontSizeValue.setText(getResources().getString(R.string.more_content_fontsize_large));
                break;
        }

    }

    /**
     * 设置正文字体大小弹出选择窗口
     */
    private void setFontSize(){

        String fontSizeString = sHelper.getPreferenceValue(Enums.PreferenceKeys.Sys_FontSize.toString());
        currentFontSize = (fontSizeString==""?Integer.parseInt("1"):Integer.parseInt(fontSizeString));
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.more_content_fontsize)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(new String[] {getResources().getString(R.string.more_content_fontsize_small),
                                getResources().getString(R.string.more_content_fontsize_middle),
                                getResources().getString(R.string.more_content_fontsize_large)},currentFontSize,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0:
                                        tViewFontSizeValue.setText(getResources().getString(R.string.more_content_fontsize_small));
                                        break;
                                    case 1:
                                        tViewFontSizeValue.setText(getResources().getString(R.string.more_content_fontsize_middle));
                                        break;
                                    case 2:
                                        tViewFontSizeValue.setText(getResources().getString(R.string.more_content_fontsize_large));
                                        break;
                                }

                                sHelper.saveCommonPreferenceSettings(Enums.PreferenceKeys.Sys_FontSize.toString(), Enums.PreferenceType.String, String.valueOf(which));

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .show();
    }

    /**
     * 设置无图模式当前值
     */
    private void initNoimageIcon(){

        currentNoimageStatus = sHelper.getPreferenceValue(Enums.PreferenceKeys.Sys_PhotoState.toString());
        if(currentNoimageStatus==""){
            currentNoimageStatus = "true";
        }

        setNoimageIcon(true);
    }

    /**
     * 设置无图状态button
     */
    private void setNoimageIcon(boolean isinitialize){

        RelativeLayout.LayoutParams layoutParams_info;

        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if(isinitialize){
            if(currentNoimageStatus =="true"){
                layoutParams_info.addRule(RelativeLayout.ALIGN_LEFT, R.id.more_imageview_setnoimage_selectbg);
            }else{
                layoutParams_info.addRule(RelativeLayout.ALIGN_RIGHT, R.id.more_imageview_setnoimage_selectbg);
            }
        }else{
            if(currentNoimageStatus =="true"){
                currentNoimageStatus ="false";
                layoutParams_info.addRule(RelativeLayout.ALIGN_RIGHT, R.id.more_imageview_setnoimage_selectbg);
            }else{
                currentNoimageStatus ="true";
                layoutParams_info.addRule(RelativeLayout.ALIGN_LEFT, R.id.more_imageview_setnoimage_selectbg);
            }
        }

        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetNoimageIcon.setLayoutParams(layoutParams_info);

        sHelper.saveCommonPreferenceSettings(Enums.PreferenceKeys.Sys_PhotoState.toString(), Enums.PreferenceType.String,currentNoimageStatus);

    }

    /**
     * 设置当前缓存大小
     */
    private void initCacheSize(){
        currentCacheSize = 0;
        String fileSizeString = "0.00M";
        try {
            File file = new File(StandardizationDataUtils.getBitmapCacheStorePath());
            currentCacheSize = CacheUtils.getFileSize(file);
            fileSizeString = CacheUtils.FormetFileSize(currentCacheSize);
        } catch (Exception e) {
            LogUtils.e(e);
        }

        tViewCacheSize.setText(fileSizeString);

    }

    /**
     * 设置闹铃声音当前值
     */
    private void initAlarmVoice(){

        String alarmVoiceString = sHelper.getPreferenceValue(Enums.PreferenceKeys.Sys_AlarmVoice.toString());
        currentAlarmVoice = (alarmVoiceString==""?Integer.parseInt("0"):Integer.parseInt(alarmVoiceString));

        switch(currentAlarmVoice){
            case 0:
                tViewSetAlarmVoiceValue.setText(getResources().getString(R.string.more_content_alarmvoice_men));
                break;
            case 1:
                tViewSetAlarmVoiceValue.setText(getResources().getString(R.string.more_content_alarmvoice_women));
                break;
        }

    }

    /**
     * 设置闹铃声音弹出选择窗口
     */
    private void setAlarmVoice(){

        String alarmVoiceString = sHelper.getPreferenceValue(Enums.PreferenceKeys.Sys_AlarmVoice.toString());
        currentAlarmVoice = (alarmVoiceString==""?Integer.parseInt("0"):Integer.parseInt(alarmVoiceString));
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.more_content_alarmvoice)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(new String[] {getResources().getString(R.string.more_content_alarmvoice_men),
                                getResources().getString(R.string.more_content_alarmvoice_women)},currentAlarmVoice,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0:
                                        tViewSetAlarmVoiceValue.setText(getResources().getString(R.string.more_content_alarmvoice_men));
                                        playVoice(0);
                                        break;
                                    case 1:
                                        tViewSetAlarmVoiceValue.setText(getResources().getString(R.string.more_content_alarmvoice_women));
                                        playVoice(1);
                                        break;
                                }

                                sHelper.saveCommonPreferenceSettings(Enums.PreferenceKeys.Sys_AlarmVoice.toString(), Enums.PreferenceType.String, String.valueOf(which));

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .show();
    }

    /**
     * 选择闹铃声音时，试听
     * @param voiceId
     */
    private void playVoice(int voiceId){
        int alarmVoiceResourceId = R.raw.alarm_women;
        switch(voiceId){
            case 0:
                alarmVoiceResourceId = R.raw.alarm_men;
                break;
            case 1:
                alarmVoiceResourceId = R.raw.alarm_women;
                break;
        }

        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(getContext(), alarmVoiceResourceId);
        mediaPlayer.setLooping(false);

        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mediaPlayer = null;
            }
        });
    }

    /**
     * 设置推送接收状态当前值
     */
    private void initPushSwitch(){

        currentPushSwitchStatus = sHelper.getPreferenceValue(Enums.PreferenceKeys.Sys_PushState.toString());
        if(currentPushSwitchStatus==""){
            currentPushSwitchStatus = "true";
        }

        setPushSwitchIcon(true);
    }

    /**
     * 设置推送接收状态button
     */
    private void setPushSwitchIcon(boolean isinitialize){

        RelativeLayout.LayoutParams layoutParams_info;

        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if(isinitialize){
            if(currentPushSwitchStatus.equals("true")){
                layoutParams_info.addRule(RelativeLayout.ALIGN_RIGHT, R.id.more_imageview_pushswitch_selectbg);
            }else{
                layoutParams_info.addRule(RelativeLayout.ALIGN_LEFT, R.id.more_imageview_pushswitch_selectbg);
            }
        }else{
            if(currentPushSwitchStatus.equals("true")){
                currentPushSwitchStatus ="false";
                layoutParams_info.addRule(RelativeLayout.ALIGN_LEFT, R.id.more_imageview_pushswitch_selectbg);
            }else{
                currentPushSwitchStatus ="true";
                layoutParams_info.addRule(RelativeLayout.ALIGN_RIGHT, R.id.more_imageview_pushswitch_selectbg);
            }
        }

        layoutParams_info.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imViewSetPushSwitchIcon.setLayoutParams(layoutParams_info);

        sHelper.saveCommonPreferenceSettings(Enums.PreferenceKeys.Sys_PushState.toString(), Enums.PreferenceType.String,currentPushSwitchStatus);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            //我的收藏
            case R.id.more_textview_tab_mycollection:

                break;
            //消息推送
            case R.id.more_textview_tab_msgpush:

                break;
            //正文字号
            case R.id.more_textview_fontsize:
            case R.id.more_imageview_fontsize_arrow:
            case R.id.more_textview_fontsize_title:
                setFontSize();
                break;
            //无图模式
            case R.id.more_imageview_setnoimage_selectbg:
            case R.id.more_imageview_setnoimage_selecticon:
            case R.id.more_textview_setnoimage:
                setNoimageIcon(false);
                break;
            //接收推送消息状态
            case R.id.more_imageview_pushswitch_selectbg:
            case R.id.more_imageview_pushswitch_selecticon:
            case R.id.more_textview_pushswitch:
                setPushSwitchIcon(false);
                break;
            //清理缓存
            case R.id.more_textview_cachesize:
            case R.id.more_textview_cleancache:
                dialog(getResources().getString(R.string.more_content_msg_clearcache), onCacheClickListener);
                break;
            case R.id.more_imageview_bindweibo_arrow:
            case R.id.more_textview_bindweibo:
                break;
            case R.id.more_textview_alarmvoice:
            case R.id.more_imageview_alarmvoice_arrow:
            case R.id.more_textview_alarmvoice_title:
                setAlarmVoice();
                break;
            case R.id.more_textview_vote:
            case R.id.more_imageview_vote_arrow:
                break;
            case R.id.more_textview_update:
            case R.id.more_imageview_update_arrow:
                compareVerson();
                break;
            //联系我们
            case R.id.more_textview_contactus:
            case R.id.more_imageview_contactus_arrow:
                break;
        }
    }

    private void dialog(String message,	android.content.DialogInterface.OnClickListener listener) {
        dialogHelper.dialog(
                message,
                listener,
                getResources().getString(R.string.tips),
                getResources().getString(R.string.yes),
                getResources().getString(R.string.no));
    }

    android.content.DialogInterface.OnClickListener onCacheClickListener = new android.content.DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();

            dialogHelper.showLoadingDialog(getResources().getString(R.string.waiting));

            clearCache();
        }
    };

    /**
     * 清空缓存(TEMP文件夹下面的数据)
     */
    private void clearCache() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                CacheUtils.removeCache();
                sendMessage(null, CACHE_CLEAR_COMPLETE);
            }

        }).start();
    }

    /**
     * 版本校验，如果低于最低要求就提示升级
     */
    private void compareVerson() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    synchronized (AndroidApplication.mLock) {
                        while (AndroidApplication.getNetStatus() == Enums.NetStatus.Disable) {
                            AndroidApplication.mLock.wait();
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                    e.printStackTrace();
                }

                try {
                    String versionCurrent = "1";
                    String versionServer = "0";

                    if (!versionCurrent.equals(versionServer)) // 当前版本不等于服务器版本时，提示更新
                    {
                        sendMessage(null,UPDATE_APP);
                    }

                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }
        }).start();
    }


    private void sendMessage(Object obj, int msgCode) {
        Message msg = moreHandler.obtainMessage(msgCode);
        msg.obj = obj;
        msg.sendToTarget();
    }

    private Handler moreHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CACHE_CLEAR_COMPLETE:

                    initCacheSize();
                    dialogHelper.closeLoadingDialog();
                    ToastHelper.showToast(getResources().getString(R.string.more_content_msg_clearcache_finished), Toast.LENGTH_SHORT);

                    break;
            }
        }
    };
}
