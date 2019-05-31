package cn.edu.cuc.logindemo.Utils;

import android.widget.Toast;

import cn.edu.cuc.logindemo.AndroidApplication;

/**
 * 统一Toast显示
 * @author SongQing
 *
 */
public class ToastHelper {

	private static Toast toast;
	/**
	 * Toast显示相关信息
	 *
	 * @param message
	 */
	public static void showToast(String message,int toastLength) {
		if(message.equals("")){	//说明不想再显示Toast了
			if(toast!=null){
				toast.cancel();
			}
			return;
		}
		if(toast == null){
			toast = Toast.makeText(AndroidApplication.getInstance(), message, toastLength);
		}else{
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 通过ResourceID获取对应内容
	 * @param resId
	 * @return
	 */
	public static String getStringFromResources(int resId){
		return AndroidApplication.getInstance().getString(resId);
	}
}
