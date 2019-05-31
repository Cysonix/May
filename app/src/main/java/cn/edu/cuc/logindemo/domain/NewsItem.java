package cn.edu.cuc.logindemo.domain;

/**
 * 新闻列表实体类
 * @author SongQing
 *
 */
public class NewsItem {

	private String newsId;						//ID
	private int channelId;						//所属栏目ID
	private String title;						//标题
	private String createDate;					//新闻创建时间
	private String abstractString;				//摘要
	private String source;						//来源 “来自新华社”
	private String imageHref;					//小图链接
	private String author;						//作者
	private String contentHref;				//内容详情页网页链接

	public NewsItem(){
		this.channelId =-1;
		this.newsId="";
		this.title="";
		this.createDate = "";
		this.abstractString = "";
		this.source = "";
		this.imageHref = "";
		this.contentHref="";
	}

	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {this.newsId = newsId;}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageHref() {
		return imageHref;
	}
	public void setImageHref(String imageHref) {
		this.imageHref = imageHref;
	}
	public String getContentHref() {
		return contentHref;
	}
	public void setContentHref(String contentHref) {
		this.contentHref = contentHref;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getAbstractString() {
		return abstractString;
	}
	public void setAbstractString(String abstractString) {
		this.abstractString = abstractString;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAuthor() { return author;}
	public void setAuthor(String author) { this.author = author; }

}
