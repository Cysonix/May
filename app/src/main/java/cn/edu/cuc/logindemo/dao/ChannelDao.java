package cn.edu.cuc.logindemo.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import cn.edu.cuc.logindemo.domain.Channel;

public class ChannelDao {

	private static SQLiteHelper sqlHelper;

	public ChannelDao(Context context) {
		sqlHelper = new SQLiteHelper(context);
	}

	/**
	 * 添加栏目
	 * @param channel
	 */
	public boolean add(Channel channel){
		int count = 0;

		if (channel != null) {
			String sql = "insert into Channel(channelId, text, sortFlag,"
					+ " parentID, channelUrl) "
					+ "values(?,?,?,?,?)";
			Object[] params = { channel.getId(), channel.getName(),channel.getSortFlag(),
					channel.getParentId(),channel.getUrl()};

			count = sqlHelper.insert(sql, params);
		}

		return count == 1 ? true : false;
	}

	/**
	 * 根据Id获取栏目
	 * @param id
	 * @return
	 */
	public Channel get(int id) {
		Channel channel = null;

		String sql = "select * from Channel where channelId =" + String.valueOf(id);

		Cursor cursor = sqlHelper.findQuery(sql, null);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				channel = new Channel();
				populateChannel(cursor, channel);
			}
		}
		cursor.close();

		return channel;
	}

	/**
	 * 根据名称获取栏目
	 * @param name
	 * @return
	 */
	public Channel get(String name) {
		Channel channel = null;

		String sql = "select * from Channel where text = '" + name + "'";

		Cursor cursor = sqlHelper.findQuery(sql, null);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				channel = new Channel();
				populateChannel(cursor, channel);
			}
		}
		cursor.close();

		return channel;
	}

	/**
	 * 更新栏目
	 * @param channel
	 * @return
	 */
	public boolean update(Channel channel) {
		int count = 0;

		if (channel != null) {
			String sql = "update Channel set text = ?, "
					+ "parentID = ?, sortFlag = ?, channelUrl= ? where channelId = ?";
			Object[] params = { channel.getName(),channel.getParentId(),channel.getSortFlag(),
					channel.getUrl(),channel.getId() };

			count = sqlHelper.update(sql, params);
		}
		return count == 1 ? true : false;
	}

	/**
	 * 根据ID删除栏目
	 * @param id
	 * @return
	 */
	public boolean delete(int id) {

		int count = 0;

		if (id != 0) {
			String sql = "delete from Channel where channelId = ?";
			Object[] params = { id };

			count = sqlHelper.delete(sql, params);
		}

		return count == 1 ? true : false;
	}

	/**
	 * 删除所有栏目
	 * @return
	 */
	public boolean deleteAll() {

		int count = 0;

		String sql = "delete from Channel";

		count = sqlHelper.deleteAll(sql);

		return count == 1 ? true : false;
	}

	/**
	 * 删除指定ParentId下面的所有子栏目信息
	 * @return
	 */
	public boolean deleteByParentId(int parentId){

		int count = 0;

		if (parentId != -1) {
			String sql = "delete from Channel where parentId = ?";
			Object[] params = { parentId };

			count = sqlHelper.delete(sql, params);
		}

		return count >= 1 ? true : false;
	}

	/**
	 * 根据栏目类型获取栏目信息
	 * @param type
	 * @return
	 */
	public List<Channel> getChannelByType(int type){

		List<Channel> channels = new ArrayList<Channel>();

		String sql = "select * from Channel "
				+ "where channelType = " + type
				+ " order by sortFlag";

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				Channel channel = new Channel();
				populateChannel(cursor, channel);
				channels.add(channel);
			}
		}
		cursor.close();

		return channels;
	}

	/**
	 * 获取某一栏目下的子栏目列表
	 * @param parentId
	 * @return
	 */
	public List<Channel> getChannels(int parentId) {

		List<Channel> channels = new ArrayList<Channel>();

		String sql = "select * from Channel where parentID = " + parentId + " order by sortFlag";

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				Channel channel = new Channel();
				populateChannel(cursor, channel);
				channels.add(channel);
			}
		}
		cursor.close();

		return channels;
	}

	/**
	 * 判断是否为头栏目(只有头栏目才显示Gallery)
	 * @param sortFlag
	 * @return
	 */
	public boolean isFirstChannel(long sortFlag){

		String sql = "select * from Channel where sortFlag < " + sortFlag;

		Cursor cursor = sqlHelper.findQuery(sql);

		if (cursor != null && cursor.getCount() > 0) {
			return false;
		}else{
			return true;
		}
	}


	/**
	 * 从数据库返回的Cursor中提取并生成Channel对象
	 * @param cursor
	 * @param channel
	 */
	private void populateChannel(Cursor cursor, Channel channel) {

		channel.setId(cursor.getInt(cursor.getColumnIndex("channelId")));
		channel.setName(cursor.getString(cursor.getColumnIndex("text")));
		channel.setSortFlag(cursor.getInt(cursor.getColumnIndex("sortFlag")));
		channel.setParentId(cursor.getInt(cursor.getColumnIndex("parentID")));
		channel.setUrl(cursor.getString(cursor.getColumnIndex("channelUrl")));
	}

}
