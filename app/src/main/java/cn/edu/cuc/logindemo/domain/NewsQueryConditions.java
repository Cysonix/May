package cn.edu.cuc.logindemo.domain;

/**
 * 新闻查询条件类(网络)
 * @author SongQing
 *
 */
public class NewsQueryConditions {

	private int ChannelId;  		//栏目id
	private int page;					//当前页
	private int count;					//需要多少条
	private int type;				   //新闻类型   0 非焦点/1 焦点/2 全部

	public NewsQueryConditions(){
		this.ChannelId =-1;
		this.page = -1;
		this.count = -1;
		this.type = -1;
	}
	public int getChannelId() {return ChannelId;}
	public void setChannelId(int channelId) {ChannelId = channelId;}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

}
