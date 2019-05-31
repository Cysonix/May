package cn.edu.cuc.logindemo.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.domain.Enums;

/**
 * Created by DELL on 2019/4/29.
 */
public class DeviceInfoUtils {

    private TelephonyManager mTelephonyManager;
    private Context mContext;
    private static String ANDROID_DEFAULT_DEVICE_ID = "Android.IMEI.2012";
    private static final int ERROR = -1;

    public DeviceInfoUtils() {
        this.mTelephonyManager = (TelephonyManager) AndroidApplication
                .getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        this.mContext = AndroidApplication.getInstance();
    }

    /**
     * 获取当前Android终端线路一的电话号码
     *
     * @return
     */
    public String getPhoneNumberString() {
        try {
            String number = mTelephonyManager.getLine1Number() == null ? ""
                    : mTelephonyManager.getLine1Number();
            return number;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID,没有3G模块的Pad返回android设备号
     *
     * @return Return null if device ID is not available.
     */
    public String getDeviceId() {
        String phoneDeviceID = mTelephonyManager.getDeviceId();
        String padDeviceID = Secure.getString(
                this.mContext.getContentResolver(), Secure.ANDROID_ID);
        if (phoneDeviceID != null) {
            return phoneDeviceID;
        } else if (padDeviceID != null) {
            return padDeviceID;
        } else {
            return ANDROID_DEFAULT_DEVICE_ID;
        }
    }

    /**
     * 设备的软件版本号： 例如：the IMEI/SV(software version) for GSM phones.
     *
     * @return Return null if the software version is not available.
     */
    public String getDeviceSoftwareVersion() {
        return mTelephonyManager.getDeviceSoftwareVersion();
    }

    /**
     * 设备的屏幕分辨率
     *
     * @return 如800x480
     */
    public String getDisplayMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels + "x" + dm.widthPixels;
    }

    public int getWindowWidth() {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels;

    }

    public int getWindowHeight() {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;

    }

    public float getWindowWidthDp() {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);

        return dm.xdpi;

    }

    public float getWindowHeightDp() {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);

        return dm.ydpi;

    }


    /**
     * 根据美工作图的标准宽度，计算出页面控件的缩放比例值和当前设备的屏幕宽高(单位:DP)
     * @param standardWidth
     * @return
     */
    public float[] getScreenWHDp(float standardWidth){
        float [] returnValue = new float[3];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        returnValue[0] = dm.xdpi;
        returnValue[1] = dm.ydpi;
        returnValue[1] = dm.xdpi/standardWidth;
        return returnValue;
    }

    /**
     * 根据美工作图的标准宽度，计算出页面控件的缩放比例值和当前设备的屏幕宽高(单位:PX)
     * @param standardWidth
     * @return
     */
    public float[] getScreenWHPx(float standardWidth){
        float [] returnValue = new float[3];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        returnValue[0] = dm.widthPixels;
        returnValue[1] = dm.heightPixels;
        returnValue[2] = dm.widthPixels/standardWidth;
        return returnValue;
    }

    public int dip2px(float dpValue) {
        final float scale = this.mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float px) {
        final float scale = this.mContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public int sp2px(float spValue) {

        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 返回当前手机的型号
     *
     * @return 如 Mailstone
     */
    public String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取设备系统SDK版本号
     *
     * @return eg:8
     */
    public static String getDeviceVersionSDK() {
        return android.os.Build.VERSION.SDK;
    }

    /**
     * 获取设备系统发布版本号
     *
     * @return eg:2.3.3
     */
    public static String getDeviceVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 返回当前程序版本名
     *
     * @param context
     * @return android:versionCode="1" android:versionName="1.0" {1,1.0}
     */
    public static String[] getAppVersionName(Context context) {
        String version[] = new String[2];
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            version[0] = String.valueOf(pi.versionCode);
            version[1] = pi.versionName;

        } catch (Exception e) {
            LogUtils.e(e);
        }
        return version;
    }

    /**
     * 获取当前网络状态
     */
    public Enums.NetStatus getNetStatus() {
        ConnectivityManager connMgr = null;
        NetworkInfo activeInfo = null;

        try {
            connMgr = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            activeInfo = connMgr.getActiveNetworkInfo(); // 当前网络连接类型判断
        } catch (Exception e) {
            LogUtils.e(e);
        }

        if (activeInfo != null && activeInfo.isConnected()) { // 如果网络可用(无论什么网络类型)
            switch (activeInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE: // 移动网络
                    return Enums.NetStatus.MOBILE;
                case ConnectivityManager.TYPE_WIFI: // Wi-Fi网络
                    return Enums.NetStatus.WIFI;
                default:
                    return Enums.NetStatus.Disable;
            }
        } else { // 网络不可用
            return Enums.NetStatus.Disable;
        }
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     *
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * SDCARD是否可用
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String GetBuildProproperties(String propertiesName) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(
                    new File("/system/build.prop")));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strTemp = "";
            while ((strTemp = br.readLine()) != null) {// 如果文件没有读完则继续
                if (strTemp.indexOf(propertiesName) != -1) {
                    return strTemp.substring(strTemp.indexOf("=") + 1);
                }
            }
            br.close();
            is.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前语言环境
     *
     * @return
     */
    public static String getLocalLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;

        String language = locale.getLanguage(); // 获得语言码

        return language;
    }
}

