package cn.edu.cuc.logindemo.okhttp;

/**
 * 请求成功失败接口
 * Created by SongQing on 2019/5/20.
 */

public interface  OnSuccessAndFaultListener {
    void onSuccess(String result);
    void onFault(String errorMsg);
}
