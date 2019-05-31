package cn.edu.cuc.logindemo.okhttp;

import java.util.Map;

import cn.edu.cuc.logindemo.okhttp.bean.BaseRequestBean;
import cn.edu.cuc.logindemo.okhttp.bean.NewsRequestBean;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 所有的网络访问Api
 * Created by SongQing on 2019/5/20.
 */

public interface HttpApi {

    @GET("music.xml")
    Observable<ResponseBody> getMusicListForQuery();

    @GET("api")
    Observable<ResponseBody> getNewsListForQuery(@Query("key") String key, @Query("num") int number, @Query("page") int page);

    @GET("api")
    Observable<ResponseBody> getNewsListForMap(@QueryMap Map<String, String> map);

    //天气预报接口测试
    //@GET 不支持@Body类型
    @POST("api")
    Observable<ResponseBody> getNewsListForMap(@Body BaseRequestBean<NewsRequestBean> requestBean);



}
