package cn.edu.cuc.logindemo.okhttp.service;

import cn.edu.cuc.logindemo.okhttp.URLConstant;

import cn.edu.cuc.logindemo.domain.NewsQueryConditions;
import cn.edu.cuc.logindemo.okhttp.RetrofitFactory;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * 网络请求获取新闻相关对象获取业务类
 * 建议：不同的业务实体都分别使用不同的请求类，比如登录注册类LoginService、新闻栏目ChannelService等
 * Created by SongQing on 2019/5/20.
 */

public class NewsSubscribe {

    /**
     * 获取新闻列表数据@Query
     */
    public static void getNewsListForQuery(NewsQueryConditions conditions, DisposableObserver<ResponseBody> subscriber) {

        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getNewsListForQuery(URLConstant.NEWS_KEY,conditions.getPage(),conditions.getCount());
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
