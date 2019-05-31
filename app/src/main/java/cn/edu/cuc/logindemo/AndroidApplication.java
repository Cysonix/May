package cn.edu.cuc.logindemo;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import cn.edu.cuc.logindemo.Utils.CacheUtils;
import cn.edu.cuc.logindemo.Utils.LogUtils;
import cn.edu.cuc.logindemo.domain.Enums;

/**
 * 人工实现单例Applcation对象（系统通常会自动创建），Android程序的真正入口
 * 启动时，创建一个PID，所有的Activity都会在该进程上运行
 * 适合定义全局变量，生命周期就是Android应用程序的生命周期
 * @author SongQing
 * @date 2019-4-29
 */

public class AndroidApplication extends Application{

    public static String TAG = "MITI_NEWS";
    private static final String DEFAULT_USER = "mitier";			//默认用户
    private static String CURRENT_USER = "";
    public static boolean isStop = true;

    // 应用实例
    private static AndroidApplication instance;
    public static Object mLock;
    // 打开过的Activity列表
    private List<Activity> activityList = new LinkedList<Activity>();

    private static Enums.NetStatus netStatus = null; // 当前网络状态

    public static AndroidApplication getInstance() {
        return instance;
    }

    /**
     * 获取当前登录的用户
     * @return
     */
    public static String getCurrentUser() {
        return CURRENT_USER.isEmpty()?DEFAULT_USER:CURRENT_USER;
    }

    /**
     * 设置当前用户
     * @param userName
     */
    public void setCurrentUser(String userName){
        this.CURRENT_USER = userName;
    }

    /**
     * 添加Activity到容器中
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    /**
     * 从Activity容器中删除
     * @param activity
     */
    public void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        mLock = new Object();

        //程序异常终止时取消系统弹出的出错对话框
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                LogUtils.e(throwable);
                exit();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        LogUtils.d("MitiDemo applcaiton has been created");

    }

    /**
     * 退出应用调用
     */
    public void exit() {
        for (Activity activity : activityList) {
            try {
                activity.finish();
            } catch (Exception e) {
                LogUtils.e(e.getCause());
            }
        }

        activityList.clear();

        AndroidApplication.isStop = true;

        CacheUtils.cleanRunningCache();
    }

    public static Enums.NetStatus getNetStatus() {
        return netStatus;
    }

    public static void setNetStatus(Enums.NetStatus netStatus) {
        AndroidApplication.netStatus = netStatus;
    }
}
