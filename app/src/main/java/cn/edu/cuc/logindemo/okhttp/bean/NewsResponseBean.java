package cn.edu.cuc.logindemo.okhttp.bean;

import java.util.List;

import cn.edu.cuc.logindemo.domain.NewsItem;

/**
 * 新闻列表响应实体类
 * Json格式
 * Created by SongQing on 2019/5/20.
 */

public class NewsResponseBean {
    public int code;
    public String msg;
    public int total;
    public List<NewsItem> newsList;
}
