package cn.edu.cuc.logindemo.logic;


import android.content.Context;

import java.util.List;

import cn.edu.cuc.logindemo.dao.NewsItemDao;
import cn.edu.cuc.logindemo.domain.Enums;
import cn.edu.cuc.logindemo.domain.MitiException;
import cn.edu.cuc.logindemo.domain.NewsItem;
import cn.edu.cuc.logindemo.domain.NewsQueryConditions;
import cn.edu.cuc.logindemo.domain.Pager;
import cn.edu.cuc.logindemo.http.RemoteCaller;

public class NewsItemLogic {

	private Context context;
	private NewsItemDao dao;

	public NewsItemLogic(Context context){
		this.context = context;
		dao = new NewsItemDao(this.context);
	}

	/**
	 * 添加一条新闻(部分内容)到本地数据库
	 * @param newsItem
	 * @return
	 */
	public boolean add(NewsItem newsItem) {

		return dao.add(newsItem);
	}

	/**
	 * 把一个栏目下面的新闻(部分内容)全部删除
	 * @param channelId
	 * @return
	 */
	public boolean deleteByChannel(int channelId) {

		return dao.deleteByChannel(channelId);
	}

	/**
	 * 获取单条新闻(部分内容)
	 * @param id
	 * @param channelId
	 * @return
	 */
	public NewsItem get(String id, int channelId) {
		return dao.get(id, channelId);
	}

	/**
	 * 获取单条新闻(部分内容)
	 * @param name
	 * @param channelId
	 * @return
	 */
	public NewsItem getByName(String name, int channelId) {
		return dao.getByName(name, channelId);
	}

	/**
	 * 清空所有新闻(部分内容)
	 * @return
	 */
	public boolean deleteAll() {

		return dao.deleteAll();
	}

	/**
	 * 根据栏目Id获取下属所有新闻(部分内容)列表
	 * @param channelId
	 * @return
	 */
	public List<NewsItem> getNewsItems(int channelId) {

		return dao.getNewsItems(channelId);
	}

	/**
	 * 根据栏目Id获取下属所有新闻(部分内容)列表
	 * @return
	 * @throws MitiException
	 */
	public List<NewsItem> getNewsFromRemote(NewsQueryConditions conditions) throws MitiException {
		return RemoteCaller.getNewsByChannel(conditions);
	}

	/**
	 * 根据栏目Id获取下属所有新闻(部分内容)列表
	 * @return
	 * @throws MitiException
	 */
	public List<NewsItem> getNewsFromBaiduRemote(NewsQueryConditions conditions) throws MitiException{
		return RemoteCaller.getNewsByChannel(conditions);
	}

	/**
	 * 根据栏目Id和Pager信息分页获取下属所有新闻(部分内容)列表
	 * @param channelId
	 * @return
	 */
	public List<NewsItem> getNewsItems(int channelId, Enums.NewsFocus newsFocusType, Pager pager) {

		return dao.getNewsItems(channelId,newsFocusType,pager);
	}

	/**
	 * 根据起始新闻和Pager信息(传入的是起始新闻(部分内容)Id)分页获取下属所有新闻(部分内容)列表
	 * @param channelId
	 * @return
	 */
	public List<NewsItem> getNewsItemsByStartIndex(int channelId, Enums.NewsFocus newsFocusType, Pager pager) {

		return dao.getNewsItemsByStartIndex(channelId,newsFocusType,pager);
	}

	/**
	 * 获取channelId栏目下的某一条新闻(部分内容)之后的新闻
	 * @param channelId
	 * @param newsId
	 * @return
	 */
	public NewsItem getNextNewsItem(int channelId, long newsId) {

		return dao.getNextNewsItem(channelId, newsId);
	}

	/**
	 * 获取channelId栏目下的某一条新闻(部分内容)之前的新闻
	 * @param channelId
	 * @param newsId
	 * @return
	 */
	public NewsItem getPreNewsItem(int channelId, long newsId) {

		return dao.getPreNewsItem(channelId, newsId);
	}

	/**
	 * 获取channelId栏目下的某一条新闻之后还有多少条新闻
	 * @param newsId
	 * @param channelId
	 * @return
	 */
	public int getRemainNewsItemsCount(long newsId, int channelId){

		return dao.getRemainNewsItemsCount(newsId, channelId);
	}

	/**
	 * 获取所有新闻条目数
	 *
	 * @return
	 */
	public int getTotalNewsCount() {

		return dao.getTotalNewsCount();
	}

	/**
	 * 获取本地还有的新闻数量
	 * @param newsId
	 * @param channelId
	 * @return
	 */
	public int getRemainItemsCount(long newsId, int channelId){

		return dao.getRemainItemsCount(newsId, channelId);
	}

}
