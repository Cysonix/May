package cn.edu.cuc.logindemo.http;

/**
 * 网络访问基础配置信息
 * @author SongQing
 *
 */
public class Configuration {

	private static String DEFAULT_SERVER = "http://202.84.17.194/";
	private final String DEFAULT_REPORT_SERVER = "http://202.84.17.80/rnews/";
	private static String accessKey = "";
	private final static int REQUEST_TIMEOUT = 5000;
	private final static int retryCount = 3;

	public String getDefaultServer(){
		return DEFAULT_SERVER;
	}

	public String getDefaultReportServer() {
		return DEFAULT_REPORT_SERVER;
	}

	public String getAppId()
	{
		return "201";
	}

	public String getAppDes()
	{
		return "XhgjAndroid";
	}

	public String getAppChannelUrl(){
		return getDefaultServer() + "cm/aicnew/app/v1/" + getAppId() + "/tree";
	}

	public String getAppChannelNewsUrl() {
		return getDefaultServer() + "cm/aicnew/app/v1/" + getAppId() + "/tree/";
	}

	public String getAppContentsUrl() {
		return getDefaultServer() + "cm/aicnew/app/v1/" + getAppId() + "/tree/";
	}

	public String getAppUrl() {
		return getDefaultServer() + "cm/aicnew/app/v1/" + getAppId();
	}

	public String getLoginUrl() {
		return getDefaultServer() + "cm/aicnew/app/v1/" + getAppId();
	}

	public void setAcceessKey(String value) {
		accessKey = value;
	}

	public String getAcceessKey() {
		return accessKey;
	}
	public static int getRetryCount() {
		return retryCount;
	}

	public static int getREQUEST_TIMEOUT()
	{
		return REQUEST_TIMEOUT;
	}
}
