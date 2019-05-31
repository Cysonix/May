package cn.edu.cuc.logindemo.Utils;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.domain.Enums;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 提供简单数据的持久化存储，存储目标是程序的SharedPreference
 * @author SongQing
 *
 */
public class SharedPreferencesHelper{

	private SharedPreferences settings;
	private SharedPreferences userSettings;
	private SharedPreferences.Editor editor ;
	private Context context;
	private String userName;

	public SharedPreferencesHelper(Context context){
		this.context = context;
		//获取一个活动的Preferences 对象
		settings=PreferenceManager.getDefaultSharedPreferences(context);
		try {
			userName = AndroidApplication.getCurrentUser();
			userSettings = this.context.getSharedPreferences(userName, Activity.MODE_PRIVATE);
		} catch (Exception e) {
		}
	}

	public void setUserSetting(){
		userName = AndroidApplication.getCurrentUser();
		userSettings = this.context.getSharedPreferences(userName, Activity.MODE_PRIVATE);
	}

	public void setUserSetting(String username){
		this.userName = username;
		userSettings = this.context.getSharedPreferences(userName, Activity.MODE_PRIVATE);
	}

	/**
	 * 根据传入的Preference Key返回系统偏好设置中对应的Value
	 * @param key    	Preferences Key 唯一标示一个配置项
	 * @return
	 */
	public String getPreferenceValue(String key){
		String returnValue = "";
		try {
			returnValue = settings.getString(key, "");
		} catch (Exception ex) {
			LogUtils.e(ex);
			ex.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * 根据传入的Preference Key返回系统偏好设置中对应的Value
	 * @param key    	Preferences Key 唯一标示一个配置项
	 * @param defaultVaue 如果不存在，则返回默认值
	 * @return
	 */
	public String getPreferenceValue(String key,String defaultValue){
		String returnValue = "";
		try {
			returnValue = settings.getString(key, defaultValue);
		} catch (Exception ex) {
			LogUtils.e(ex);
			ex.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * 保存全局的偏好配置(与用户无关)
	 * @param key		Preferences Key 唯一标示一个配置项
	 * @param pType	Preference支持多种配置数据类型：Boolean/Int/Float/Long/String
	 * @param value	Preference配置项对应值
	 */
	public boolean saveCommonPreferenceSettings(String key, Enums.PreferenceType pType, String value){
		try{
			//settings=this.context.getSharedPreferences("systemuser", Activity.MODE_PRIVATE);
			editor = settings.edit();
			switch(pType){
				case Boolean :
					editor.putBoolean(key, Boolean.parseBoolean(value));
					break;
				case Int:
					editor.putInt(key, Integer.parseInt(value));
					break;
				case Float:
					editor.putFloat(key, Float.parseFloat(value));
					break;
				case Long:
					editor.putLong(key, Long.parseLong(value));
					break;
				case String:
					editor.putString(key, value);
					break;
				default:
					break;
			}
			editor.commit();
			return true;
		}catch(Exception ex){
			LogUtils.e(ex);
			return false;
		}
	}


	/**
	 * 根据传入的Preference Key返回用户偏好设置中对应的Value
	 * @param key    	Preferences Key 唯一标示一个配置项
	 * @return
	 */
	public String getUserPreferenceValue(String key){
		String returnValue = "";
		try {
			returnValue = userSettings.getString(key, "");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			LogUtils.e(ex);
			ex.printStackTrace();
		}

		return returnValue;
	}

	/**
	 * 根据传入的Preference Key返回用户偏好设置中对应的Value
	 * @param key    	Preferences Key 唯一标示一个配置项
	 * @param defaultVaue 如果不存在，则返回默认值
	 * @return
	 */
	public String getUserPreferenceValue(String key,String defaultValue){
		String returnValue = "";
		try {
			returnValue = userSettings.getString(key, defaultValue);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			LogUtils.e(ex);
			ex.printStackTrace();
		}

		return returnValue;
	}

	/**
	 * 负责保存用户自己的偏好设置(每个用户都以一个专用的偏好设置文件)，目前暂无用户一说，如果有再扩展
	 * @param key		Preferences Key 唯一标示一个配置项
	 * @param pType		Preference支持多种配置数据类型：Boolean/Int/Float/Long/String
	 * @param value		Preference配置项对应值
	 * @return
	 */
	public boolean saveUserPreferenceSettings(String key, Enums.PreferenceType pType, String value){
		try{
			editor = userSettings.edit();
			switch(pType){
				case Boolean :
					editor.putBoolean(key, Boolean.parseBoolean(value));
					break;
				case Int:
					editor.putInt(key, Integer.parseInt(value));
					break;
				case Float:
					editor.putFloat(key, Float.parseFloat(value));
					break;
				case Long:
					editor.putLong(key, Long.parseLong(value));
					break;
				case String:
					editor.putString(key, value);
					break;
				default:
					break;
			}
			editor.commit();
			return true;
		}catch(Exception ex){
			LogUtils.e(ex);
			return false;
		}
	}
}
