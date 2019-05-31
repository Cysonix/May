package cn.edu.cuc.logindemo.okhttp.bean;

/**
 * 新闻请求实体类
 * Created by SongQing on 2019/5/20.
 */

public class NewsRequestBean {

    private String num;
    private int page;
    private String key;

    public void setNum(String num) {this.num = num;}
    public void setPage(int page) {this.page = page;}
    public void setKey(String key) {this.key = key;}

}
