package cn.edu.cuc.logindemo.http;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.edu.cuc.logindemo.Utils.LogUtils;
import cn.edu.cuc.logindemo.domain.Channel;
import cn.edu.cuc.logindemo.domain.MitiException;
import cn.edu.cuc.logindemo.domain.NewsItem;
import cn.edu.cuc.logindemo.domain.NewsQueryConditions;

/**
 * 远程调用接口类
 *
 * @author SongQing
 *
 */
public class RemoteCaller {
	private static int msgForOa_totalCount = 0; //OA信息总数

	static HttpClient httpClient = new HttpClient();
	private static int NORMAL_TIMEOUT = 1000;			//普通网络请求超时

	/**
	 * 获取所有一级栏目
	 * @return
	 * @throws MitiException
	 */
	public static ArrayList<Channel> getChannelList(String local) throws MitiException {
		ArrayList<Channel> returnValue =new ArrayList<Channel>();
		Channel channel=new Channel();

		channel.setId(1);
		channel.setName("栏目");
		channel.setSortFlag(1);
		channel.setUrl("");
		channel.setParentId(0);
		returnValue.add(channel);

		return returnValue;
	}

	public static ArrayList<Channel> getChannelListByParentId(int parentId,String local) throws MitiException{
		ArrayList<Channel> returnValue = new ArrayList<Channel>();
		Channel channel = new Channel();
		channel.setId(2);
		channel.setName("大众");
		channel.setSortFlag(1);
		channel.setUrl("https://api.tianapi.com/generalnews/");
		channel.setParentId(parentId);
		returnValue.add(channel);

		channel = new Channel();
		channel.setId(3);
		channel.setName("社会");
		channel.setSortFlag(2);
		channel.setUrl("http://apis.baidu.com/txapi/social/social");
		channel.setParentId(parentId);
		returnValue.add(channel);

		channel = new Channel();
		channel.setId(4);
		channel.setName("体育");
		channel.setSortFlag(3);
		channel.setUrl("https://api.tianapi.com/tiyu/");
		channel.setParentId(parentId);
		returnValue.add(channel);

		channel = new Channel();
		channel.setId(5);
		channel.setName("科技");
		channel.setSortFlag(4);
		channel.setUrl("https://api.tianapi.com/keji/");
		channel.setParentId(parentId);
		returnValue.add(channel);

		channel = new Channel();
		channel.setId(6);
		channel.setName("苹果");
		channel.setSortFlag(5);
		channel.setUrl("https://api.tianapi.com/apple/");
		channel.setParentId(parentId);
		returnValue.add(channel);

		return returnValue;
	}

	/**
	 * 根据栏目ID获取新闻列表
	 * @param conditions
	 * @return
	 * @throws MitiException
	 */
	public static ArrayList<NewsItem> getNewsByChannel(NewsQueryConditions conditions) throws MitiException{
		ArrayList<NewsItem> returnValue = null;
		String urlString =  "";
		try{
			switch (conditions.getChannelId()) {
				case 2:
					urlString = "https://api.tianapi.com/world/";
					break;
				case 4:
					urlString = "https://api.tianapi.com/tiyu/";
					break;
				case 5:
					urlString = "https://api.tianapi.com/keji/";
					break;
				case 6:
					urlString = "http://api.tianapi.com/apple/";
					break;
				case 3:
					urlString = "http://api.tianapi.com/social/";
					break;
				default:
					break;
			}
			PostParameter[] postParameters = new PostParameter[] {
					new PostParameter("num", conditions.getCount()),
					new PostParameter("key", "c8915ce03dd5267f0af7d4fef3589fcd")};
			String JSONResult = httpClient.doGet(postParameters,urlString,NORMAL_TIMEOUT);

			if (JSONResult!=null && JSONResult.length()>0) {
				returnValue = new ArrayList<NewsItem>();

				JSONObject object = new JSONObject(JSONResult);

				JSONArray arr = object.getJSONArray("newslist");

				for(int i=0;i<arr.length();i++){

					NewsItem newsItem = new NewsItem();
					JSONObject obj=arr.getJSONObject(i);

					String tempTitleString = obj.getString("title");

					newsItem.setNewsId(UUID.randomUUID().toString());
					newsItem.setAbstractString(obj.getString("description"));
					newsItem.setTitle(tempTitleString);
					newsItem.setImageHref(obj.getString("picUrl"));
					newsItem.setContentHref(obj.getString("url"));
					newsItem.setCreateDate(obj.getString("ctime"));
					newsItem.setChannelId(conditions.getChannelId());

					returnValue.add(newsItem);
				}
			}
		}catch (Exception e) {
			exceptionTypeJudge(e);
		}

		return returnValue;
	}

	/**
	 * 异常判断
	 * @param exception
	 * @throws MitiException
	 */
	private static void exceptionTypeJudge(Exception exception) throws MitiException{
		LogUtils.e(exception);
		if(exception.getMessage().equals(MitiException.CONNECT_TIMEOUT_EXCEPTION) || exception.getMessage().equals(MitiException.INTERRUPTED_IO_EXCEPTION)){
			throw new MitiException(MitiException.TIMEOUT);
		}else{
			throw new MitiException(exception.getMessage());
		}
	}
	/**
	 * 设置OA信息当前总条数
	 * @param msgForOa_totalCount
	 */
	public static void setMsgForOa_totalCount(int msgForOa_totalCount) {
		RemoteCaller.msgForOa_totalCount = msgForOa_totalCount;
	}
	/**
	 * 返回OA信息当前总条数
	 * @return
	 */
	public static int getMsgForOa_totalCount() {
		return msgForOa_totalCount;
	}
}
