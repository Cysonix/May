package cn.edu.cuc.logindemo.domain;

public class Enums {

	/**
	 * 栏目新闻的焦点类型
	 * @author
	 *
	 */
	public enum NewsFocus{

		NORMAL(0),
		FOCUS(1),
		ALL(-1);

		private final int status;

		public int getValue() {
			return status;
		}

		NewsFocus(int value) {
			this.status = value;
		}
	}

	/**
	 * 手机网络状态
	 * @author Songqing
	 *
	 */
	public enum NetStatus
	{
		Disable("不可用"),
		WIFI("WIFI可用"),
		MOBILE("手机网络可用");

		private final String svalue;

		public String getValue() {
			return svalue;
		}

		NetStatus(String value) {
			this.svalue = value;
		}
	}

	/**
	 * 日志类型
	 * @author SongQing
	 *
	 */
	public enum LogType{
		System("系统日志"),
		Operation("用户操作日志");

		private final String svalue;

		public String getValue() {
			return svalue;
		}

		LogType(String value) {
			this.svalue = value;
		}
	}

	/**
	 * Preference数据存储的类型
	 * @author SongQing
	 *
	 */
	public enum PreferenceType{
		Boolean("布尔型"),
		String("字符串型"),
		Int("整型"),
		Float("浮点型"),
		Long("长整型");
		
		private final java.lang.String svalue;

		public java.lang.String getValue() {
			return svalue;
		}
		
		PreferenceType(java.lang.String value) {
				this.svalue = value;
		}
	} 
	
	/**
	 * 语种
	 * @author SongQing
	 *
	 */
	public enum Language{
		CN("中文"),
		EN("英文"),
		UNDEFINE("未指定");
		
		private final String status;

		public String getValue() {
			return status;
		}
		
		Language(String value) {
				this.status = value;
		}
	}

    /**
     * 附件类型
     * @author SongQing
     *
     */
    public enum AccessoryType{
        Picture("图片"),
        Video("视频"),
        Voice("声音"),
        Complex("复杂文档"),
        Graph("图表"),
        Text("文字"),
        Cache("Cache");

        private final String svalue;

        public String getValue() {
            return svalue;
        }

        AccessoryType(String value) {
            this.svalue = value;
        }
    }

    /**
     * 新闻图片的大小
     * @author
     *
     */
    public enum NewsPictureSize{

        SMALL("small"),
        MIDDLE("middle"),
        LARGE("large");

        private final String status;

        public String getValue() {
            return status;
        }

        NewsPictureSize(String value) {
            this.status = value;
        }
    }

	/**
	 * 配置项枚举
	 * @author 宇俊
	 *
	 */
	public enum PreferenceKeys{
		Sys_SystemVersion("当前系统版本"),
		Sys_ColumnVersion("当前栏目版本"),
		Sys_ColumnVersion_En("当前英文栏目版本"),
		Sys_DeviceID("当前设备ID"),
		Sys_NetStatus("当前网络状态"),
		Sys_IsStop("当前系统状态"),
		Sys_FlashNewsColCatCode("快讯分类栏目代码"),
		Sys_MainPageColCatCode("快讯分类栏目代码"),

		Sys_NewsLanguage("语种"),
		Sys_LastLoginUserName("上次登录的用户名"),
		Sys_LastLoginPassword("上次登录的用户密码"),

		Sys_UserName("上次投票用户名"),
		Sys_UserPhone("用户联系电话"),
		Sys_UserEmail("用户邮箱"),

		Sys_FontSize("正文字号"),					//0表示小字号 1表示中字号 2表示大子号
		Sys_PhotoState("图像浏览模式"),				//true有图浏览 		false 无图浏览
		Sys_AlarmVoice("闹钟声音"),				//0表示男声   1表示女声
		Sys_PushState("接收消息推送状态"),			//true 接收服务器推送消息  false 不接受

		User_IsAutoLogin("是否自动登录"),
		User_PhotoState("图像浏览模式"),			//true有图浏览 		false 无图浏览
		User_OfflineDownloadState("离线下载开关"),	//true wifi离线下载 false 任何时候都不离线下载

		Sys_isFirstRunning("是否为第一次运行程序");

		private final String svalue;

		public String getValue() {
			return svalue;
		}

		PreferenceKeys(String value) {
			this.svalue = value;
		}
	}
}
