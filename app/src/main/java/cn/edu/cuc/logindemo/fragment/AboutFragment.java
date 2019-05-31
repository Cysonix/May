package cn.edu.cuc.logindemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.DeviceInfoUtils;

/**
 * 关于我们页面
 */
public class AboutFragment extends Fragment{

    private LinearLayout lLayoutContainer;
    private RelativeLayout rlayoutContainer;
    private TextView tViewVersion,
            tViewCopyRight;

    private static int screenWidth;
    private static int screenHeight;
    private static float scalesizeWidth;

    private String[] versionStrings;

    private final static int TEXT_FONT_SIZE = 24;			//banner区域内的文字大小(对于960*540的标准)
    private final static int MARGIN_BOTTOM = 50;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about, null);
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

        versionStrings = DeviceInfoUtils.getAppVersionName(getContext());
        this.setScreenSize();
        this.setUpViews();
    }

    /**
     * 获取屏幕的宽高，依照此算出缩放比例
     */
    private void setScreenSize(){
        DeviceInfoUtils dInfoHelper = new DeviceInfoUtils();
        float[] wdValues = dInfoHelper.getScreenWHPx(540.0f);
        screenWidth = (int) wdValues[0];
        screenHeight = (int) wdValues[1];
        scalesizeWidth = wdValues[2];
    }

    /**
     * 初始化页面控件
     */
    private void setUpViews(){

        RelativeLayout.LayoutParams layoutParams_info;

        rlayoutContainer =  (RelativeLayout)getView().findViewById(R.id.about_relativelayout_container);

        lLayoutContainer = (LinearLayout)getView().findViewById(R.id.about_linearlayout_container);

        layoutParams_info = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams_info.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams_info.setMargins(0, 0, 0, (int)(MARGIN_BOTTOM*scalesizeWidth));
        lLayoutContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        lLayoutContainer.setLayoutParams(layoutParams_info);


        tViewVersion = (TextView)getView().findViewById(R.id.about_textview_version);
        tViewVersion.setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXT_FONT_SIZE * scalesizeWidth);
        tViewVersion.setText(getResources().getString(R.string.about_version) + versionStrings[1]);

        tViewCopyRight = (TextView)getView().findViewById(R.id.about_textview_copyright);
        tViewCopyRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXT_FONT_SIZE * scalesizeWidth);
        tViewCopyRight.setText(getResources().getString(R.string.about_copyright));
    }
}
