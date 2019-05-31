package cn.edu.cuc.logindemo.logic;

import android.content.Context;

import java.util.List;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.ToastHelper;
import cn.edu.cuc.logindemo.dao.ChannelDao;
import cn.edu.cuc.logindemo.domain.Channel;

public class ChannelLogic {

	private Context context;
	private ChannelDao dao;

	public ChannelLogic(Context context){
		this.context = context;
		dao = new ChannelDao(this.context);
	}

	/**
	 * 添加栏目
	 * @param channel
	 */
	public boolean add(Channel channel){
		return dao.add(channel);
	}

	/**
	 * 根据Id获取栏目
	 * @param id
	 * @return
	 */
	public Channel get(int id) {
		return dao.get(id);
	}

	/**
	 * 根据名称获取栏目
	 * @param name
	 * @return
	 */
	public Channel get(String name) {
		return dao.get(name);
	}

	/**
	 * 更新栏目
	 * @param channel
	 * @return
	 */
	public boolean update(Channel channel) {
		return dao.update(channel);
	}

	/**
	 * 根据ID删除栏目
	 * @param id
	 * @return
	 */
	public boolean delete(int id) {

		return dao.delete(id);
	}

	/**
	 * 删除所有栏目
	 * @return
	 */
	public boolean deleteAll() {

		return dao.deleteAll();
	}

	/**
	 * 删除指定ParentId下面的所有子栏目信息
	 * @return
	 */
	public boolean deleteByParentId(int parentId){

		return dao.deleteByParentId(parentId);
	}

	/**
	 * 根据栏目类型获取栏目信息
	 * @param type
	 * @return
	 */
	public List<Channel> getChannelByType(int type){

		return dao.getChannelByType(type);
	}

	/**
	 * 获取某一栏目下的子栏目列表
	 * @param username
	 * @param parentId
	 * @return
	 */
	public List<Channel> getChannels(int parentId) {

		return dao.getChannels(parentId);
	}

	/**
	 * 获取首页加载时显示的栏目(栏目对象中有sortFlag排序)
	 * @return
	 */
	public List<Channel> getChannelsForMain(){

		List<Channel> channelList = null;
		Channel channel = dao.get(ToastHelper.getStringFromResources(R.string.main_slidemenu_parentchannelname));
		if(channel !=null){
			channelList = dao.getChannels(channel.getId());
		}

		return channelList;
	}

	/**
	 * 获取首页加载时显示的第一个栏目(栏目对象中有sortFlag排序)
	 * @return
	 */
	public Channel getFirstChannelForMain(){
		List<Channel> channelList = this.getChannelsForMain();
		if(channelList !=null && channelList.size()>0){
			return channelList.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 获取首页加载时显示的焦点栏目
	 * @return
	 */
	public Channel getTopChannelForMain(){
		return this.get("大众");
	}

	/**
	 * 判断是否为头栏目(只有头栏目才显示Gallery)
	 * @param sortFlag
	 * @return
	 */
	public boolean isFirstChannel(long sortFlag){

		return dao.isFirstChannel(sortFlag);
	}
}
