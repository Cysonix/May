package cn.edu.cuc.logindemo.Utils;

import java.io.File;

import android.os.Environment;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.domain.Enums;

/**
 * 	负责生成手机客户端所需的标准化信息，按照既定规则生成文件名、文件路径等
 *
 * @author SongQing
 *
 */
public class StandardizationDataUtils {

	private static final String PROGRAM_FOLDER = "MitiDemo"; // 包名做程序资源文件夹的根目录

	private static final String LOG_FOLDER = "Logs"; // 日志文件目录名
	private static final String CONFIG_FOLDER = "Configs"; // 相关配置XML文件目录名
	private static final String TEMP_FOLDER = "Temp"; // 文件缓存目录名
	private static final String ACCESSORY_FOLDER = "Accessories"; // 文件附件目录名
	private static final String BASEDATA_FOLDER = "BaseDatas"; // 基础数据XML文件目录名

	/**
	 * 获取日志文件保存路径
	 *
	 * @param logType
	 *            日志类型(Operation/System)
	 * @return
	 */
	public static String getLogFileStorePath(Enums.LogType logType) {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//").append(LOG_FOLDER)
				.append("//").append(logType.toString()).toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	/**
	 * 获取相关配置XML文件目录名
	 *
	 * @return
	 */
	public static String getConfigFileStorePath() {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//")
				.append(CONFIG_FOLDER).toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	/**
	 * 获取新闻图片文件缓存永久保存路径
	 *
	 * @return
	 */
	public static String getBitmapCacheStorePath() {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//").append(PROGRAM_FOLDER).append("//").append(TEMP_FOLDER).toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	public static String getSDCardStorePath() {
		String sdCardsPath = GetSDCardPath();

		if (sdCardsPath == "") {
			return "";
		} else {
			return sdCardsPath;
		}
	}

	/**
	 * 获取基础数据XML文件保存路径
	 *
	 * @return
	 */
	public static String GetBaseDataFileStorePath() {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//").append(BASEDATA_FOLDER)
				.toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}


	/**
	 * 判断是否有可读的SD卡存在，并返回SD卡路径
	 *
	 * @return 存在_SD根目录 || 不存在_空字符串
	 */
	private static String GetSDCardPath() {
		// 判断外部存储SD卡是否存在，并且可写
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) { // 说明挂载着SDCARD
			return Environment.getExternalStorageDirectory().toString(); // 获取外部存储SDCard的根目录
		} else {
			if (DeviceInfoUtils.getAvailableInternalMemorySize() > 200 * 1024 * 1024) { // 内部存储路径大于200MB认为可用
				// String pathString =
				// DeviceInfoHelper.GetBuildProproperties("ro.additionalmounts");
				return "//mnt/emmc";
			} else {
				return "";
			}
		}
		// return "";
	}

	/**
	 * 验证路径是否存在，如果不存在就创建
	 *
	 * @param path
	 * @return
	 */
	private static boolean PathValidate(String path) {
		if (path == null || path == "") {
			return false;
		}

		File folder = new File(path);
		if (!folder.exists()) {
			if (folder.mkdirs()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * 获取附件文件和稿件缓存永久保存路径
	 *
	 * @return
	 */
	public static String getAccessoryFileTempStorePath() {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//").append(TEMP_FOLDER)
				.toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}

	/**
	 * 获取附件文件和稿件缓存保存路径
	 *
	 * @param accessoryType
	 *            附件类型(Picture/Video/Voice/Complex/Graph)
	 * @return
	 */
	public static String getAccessoryFileUploadStorePath(Enums.AccessoryType accessoryType) {
		String sdCardsPath = GetSDCardPath();
		if (sdCardsPath == "") {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String path = sb.append(sdCardsPath).append("//")
				.append(PROGRAM_FOLDER).append("//")
				.append(AndroidApplication.getCurrentUser()).append("//")
				.append(ACCESSORY_FOLDER).append("//")
				.append(accessoryType.toString()).toString();

		if (PathValidate(path)) {
			return path;
		} else {
			return "";
		}
	}


}
