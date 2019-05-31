package cn.edu.cuc.logindemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.DBUtils;
import cn.edu.cuc.logindemo.Utils.LogUtils;
import cn.edu.cuc.logindemo.Utils.ToastHelper;

/**
 * 程序的启动页
 */
public class SplashActivity extends Activity {

    private static final long TIME_DELAY = 3000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题

        setContentView(R.layout.activity_splash);

        initialize();
    }

    /**
     * 初始化
     */
    private void initialize(){
        if(databaseCreate()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            },TIME_DELAY);}
        else{

        }
    }

    /**
     * 创建数据库，将db拷贝到指定目录
     * @return
     */
    private boolean databaseCreate(){
        try {
            DBUtils db = new DBUtils(SplashActivity.this);
            return db.createDatabase();
        } catch (Exception e) {
            LogUtils.e(e);
            ToastHelper.showToast(ToastHelper.getStringFromResources(R.string.splash_db_create_error), Toast.LENGTH_SHORT);
            return false;
        }
    }
}
