package cn.edu.cuc.logindemo.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import cn.edu.cuc.logindemo.domain.Enums;
import cn.edu.cuc.logindemo.domain.NewsItem;
import cn.edu.cuc.logindemo.domain.Pager;

/**
 * 新闻(部分内容)实体的数据访问类
 * @author SongQing
 *
 */
public class NewsItemDao {

	private static SQLiteHelper sqlHelper;

	public NewsItemDao(Context context) {
		sqlHelper = new SQLiteHelper(context);
	}

	/**
	 * 添加一条新闻(部分内容)到本地数据库
	 * @param newsItem
	 * @return
	 */
	public boolean add(NewsItem newsItem) {

		int count = 0;

		if (newsItem != null) {
			String sql = "insert into NewsItem(newsId, channelId, title,createdate"
					+ ",abstract,source, imagehref,contenthref) "
					+ "values(?, ?, ?, ?,?,?,?,?)";
			Object[] params = { newsItem.getNewsId(), newsItem.getChannelId(),newsItem.getTitle(),
					newsItem.getCreateDate(), newsItem.getAbstractString(),
					newsItem.getSource(),	newsItem.getImageHref(),newsItem.getContentHref()};

			count = sqlHelper.insert(sql, params);
		}

		return count == 1 ? true : false;
	}

	/**
	 * 把一个栏目下面的新闻(部分内容)全部删除
	 * @param channelId
	 * @return
	 */
	public boolean deleteByChannel(int channelId) {

		int count = 0;

		if (channelId != 0) {
			String sql = "delete from NewsItem where channelId = ?";
			Object[] params = { channelId };

			count = sqlHelper.delete(sql, params);
		}

		return count == 1 ? true : false;
	}

	/**
	 * 获取单条新闻(部分内容)
	 * @param id
	 * @param channelId
	 * @return
	 */
	public NewsItem get(String id, int channelId) {
		NewsItem newsItem = null;

		String sql = "select * from NewsItem where newsId = '" + id + "'"
				+ " and channelId = " + channelId;

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				newsItem = new NewsItem();
				populateNewsItem(cursor, newsItem);
				newsItem.setChannelId(channelId);
			}
		}
		cursor.close();

		return newsItem;
	}

	/**
	 * 获取单条新闻(部分内容)
	 * @param name
	 * @param channelId
	 * @return
	 */
	public NewsItem getByName(String name, int channelId) {
		NewsItem newsItem = null;

		String sql = "select * from NewsItem where title = '" + name + "'"
				+ " and channelId = " + channelId;

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				newsItem = new NewsItem();
				populateNewsItem(cursor, newsItem);
				newsItem.setChannelId(channelId);
			}
		}
		cursor.close();

		return newsItem;
	}


	/**
	 * 清空所有新闻(部分内容)
	 * @return
	 */
	public boolean deleteAll() {

		int count = 0;

		String sql = "delete from NewsItem";

		count = sqlHelper.deleteAll(sql);

		return count == 1 ? true : false;
	}

	/**
	 * 根据栏目Id获取下属所有新闻(部分内容)列表
	 * @param channelId
	 * @return
	 */
	public List<NewsItem> getNewsItems(int channelId) {
		List<NewsItem> newsItemList = null;

		String sql= String.format("%s '%s' %s",
				"select * from NewsItem where channelId =", channelId,
				"order by createDate DESC");

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			newsItemList = new ArrayList<NewsItem>();

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				NewsItem newsItem = new NewsItem();
				populateNewsItem(cursor, newsItem);
				newsItem.setChannelId(channelId);

				newsItemList.add(newsItem);
			}

			cursor.close();

		}
		return newsItemList;
	}

	/**
	 * 根据栏目Id和Pager信息分页获取下属所有新闻(部分内容)列表
	 * @param channelId
	 * @return
	 */
	public List<NewsItem> getNewsItems(int channelId, Enums.NewsFocus newsFocusType, Pager pager) {

		List<NewsItem> newsItemList = null;

		if (pager != null) {

			int firstResult = ((pager.getCurrentPage() - 1) * pager.getPageSize()); // 从第几条数据开始查询
			int maxResult = pager.getPageSize();

			String sql1 = String.format("%s '%s' %s",
					"select * from NewsItem where channelId =", channelId,
					"order by createDate DESC");

			String sql2 = String.format("%s limit %s, %s", sql1,
					String.valueOf(firstResult), String.valueOf(maxResult));

			Cursor cursor = sqlHelper.findQuery(sql2);

			if (cursor != null && cursor.getCount() > 0) {
				newsItemList = new ArrayList<NewsItem>();

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					NewsItem newsItem = new NewsItem();
					populateNewsItem(cursor, newsItem);
					newsItem.setChannelId(channelId);
					newsItemList.add(newsItem);
				}

				cursor.close();

				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				// pager.setCurrentPage(1);
				pager.setTotalNum(0);
			}
		}
		return newsItemList;
	}

	/**
	 * 根据起始新闻和Pager信息(传入的是起始新闻(部分内容)Id)分页获取下属所有新闻(部分内容)列表
	 * @param channelId
	 * @return
	 */
	public List<NewsItem> getNewsItemsByStartIndex(int channelId, Enums.NewsFocus newsFocusType, Pager pager) {

		List<NewsItem> newsItemList = null;

		if (pager != null) {

			int firstResult = pager.getStartIndex()+1; // 从第几条数据开始查询
			int maxResult = pager.getPageSize();

			String sql1 = String.format("%s '%s' %s",
					"select * from NewsItem where channelId =", channelId,
					"order by createDate DESC");

//			String sql1 = String.format("%s '%s' %s",
//					"select * from NewsItem where channelId =", channelId,
//					"order by createDate desc");

			String sql2 = String.format("%s LIMIT %s,%s", sql1,
					String.valueOf(firstResult), String.valueOf(firstResult+maxResult));

			Cursor cursor = sqlHelper.findQuery(sql2);

			if (cursor != null && cursor.getCount() > 0) {
				newsItemList = new ArrayList<NewsItem>();

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					NewsItem newInPart = new NewsItem();
					populateNewsItem(cursor, newInPart);
					newInPart.setChannelId(channelId);

					newsItemList.add(newInPart);
				}

				cursor.close();

				pager.setTotalNum(sqlHelper.rowCount(sql1));
			} else {
				pager.setTotalNum(0);
			}
		}
		return newsItemList;
	}

	/**
	 * 获取channelId栏目下的某一条新闻(部分内容)之后的新闻
	 * @param channelId
	 * @param newsId
	 * @return
	 */
	public NewsItem getNextNewsItem(int channelId, long newsId) {

		NewsItem newsItem = null;

		String sql = String.format("%s %s %s %s %s",
				"select * from NewsItem where channelId =",channelId,
				"and createDate > (select createDate from NewsItem where newsId = ",
				newsId, ") order by isTop DESC,createDate asc limit 1");

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				newsItem = new NewsItem();
				populateNewsItem(cursor, newsItem);
				newsItem.setChannelId(channelId);
			}

			cursor.close();
		}

		return newsItem;
	}

	/**
	 * 获取channelId栏目下的某一条新闻(部分内容)之前的新闻
	 * @param channelId
	 * @param newsId
	 * @return
	 */
	public NewsItem getPreNewsItem(int channelId, long newsId) {

		NewsItem newsItem = null;

		String sql = String
				.format("%s %s %s %s %s",
						"select * from NewsItem where channelId =",
						channelId,
						"and createdate < (select createdate from NewsItem where newsId = ",
						newsId, ") order by isTop DESC,createdate DESC limit 1");

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				newsItem = new NewsItem();
				populateNewsItem(cursor, newsItem);
				newsItem.setChannelId(channelId);
			}

			cursor.close();
		}

		return newsItem;
	}

	/**
	 * 获取channelId栏目下的某一条新闻之后还有多少条新闻
	 * @param newsId
	 * @param channelId
	 * @return
	 */
	public int getRemainNewsItemsCount(long newsId, int channelId){

		String sql = String.format("%s %s %s %s %s",
				"select count(*) total from NewsItem where channelId =",channelId,
				"and createdate < (select createdate from NewsItem where newsId = ",
				newsId, ") order by isTop DESC,createdate DESC");

		Cursor cursor = sqlHelper.findQuery(sql);

		int total = 0;

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				total = cursor.getInt(cursor.getColumnIndex("total"));
			}
		}

		return total;
	}

	/**
	 * 获取所有新闻条目数
	 *
	 * @return
	 */
	public int getTotalNewsCount() {
		String sql = String.format("%s %s",
				"select count(*) total from NewsItem ",
				"order by createdate desc");

		Cursor cursor = sqlHelper.findQuery(sql);

		int total = 0;

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				total = cursor.getInt(cursor.getColumnIndex("total"));
			}
		}

		return total;
	}

	/**
	 * 获取本地还有的新闻数量
	 * @param newsId
	 * @param channelId
	 * @return
	 */
	public int getRemainItemsCount(long newsId, int channelId){

		String sql = String.format("%s %s %s %s %s",
				"select count(*) total from NewsItem where channelId =",channelId,
				"and createdate < (select createdate from NewsItem where newsId = ",
				newsId, ") order by createdate desc");

		Cursor cursor = sqlHelper.findQuery(sql);

		int total = 0;

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				total = cursor.getInt(cursor.getColumnIndex("total"));
			}
		}

		return total;
	}


	/**
	 * 从游标中获取新闻(部分内容)对象
	 * @param cursor
	 * @param newsItem
	 */
	public void populateNewsItem(Cursor cursor, NewsItem newsItem) {

		newsItem.setAbstractString(cursor.getString(cursor.getColumnIndex("abstract")));
		newsItem.setCreateDate(cursor.getString(cursor.getColumnIndex("createdate")));
		newsItem.setImageHref(cursor.getString(cursor.getColumnIndex("imagehref")));
		newsItem.setContentHref(cursor.getString(cursor.getColumnIndex("contenthref")));
		newsItem.setSource(cursor.getString(cursor.getColumnIndex("source")));
		newsItem.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		newsItem.setChannelId(cursor.getInt(cursor.getColumnIndex("channelId")));
		newsItem.setNewsId(cursor.getString(cursor.getColumnIndex("newsId")));
	}
}
