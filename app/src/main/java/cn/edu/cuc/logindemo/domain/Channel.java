package cn.edu.cuc.logindemo.domain;

import java.util.List;

/**
 * 新闻栏目
 */
public class Channel {

    private int id;				        //栏目id
    private String name;				//栏目名称
    private int sortFlag;				//栏目序号（根据此属性进行排序）
    private String url; 		        //外链接
    private int parentId;				//父栏目Id
    private List<Channel> sons;			//子栏目

    public Channel(){
        this.id = -1;
        this.name = "";
        this.sortFlag = -1;
        this.url = "";
        this.parentId = -1;
        this.sons = null;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSortFlag() {
        return sortFlag;
    }
    public void setSortFlag(int sortFlag) {
        this.sortFlag = sortFlag;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<Channel> getSons() {
        return sons;
    }
    public void setSons(List<Channel> sons) {
        this.sons = sons;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

}
