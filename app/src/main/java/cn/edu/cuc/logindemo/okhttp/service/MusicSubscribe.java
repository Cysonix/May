package cn.edu.cuc.logindemo.okhttp.service;

import cn.edu.cuc.logindemo.okhttp.RetrofitFactory;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * 音乐列表请求
 * Created by SongQing on 2019/4/21.
 */

public class MusicSubscribe {
    /**
     * 获取音乐列表数据@Query
     */
    public static void getMusicListForQuery(DisposableObserver<ResponseBody> subscriber) {

        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getMusicListForQuery();
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
