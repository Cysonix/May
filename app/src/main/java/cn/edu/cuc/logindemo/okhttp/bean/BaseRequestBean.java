package cn.edu.cuc.logindemo.okhttp.bean;

/**
 * 网络请求对象基类
 * Created by SongQing on 2019/5/20.
 */

public class BaseRequestBean<T> {

    private String version = "v1";
    private T obj;

    public String getVersion() {return version;}
    public void setVersion(String version) {this.version = version;}

    public T getObj() {return obj;}
    public void setObj(T obj) {this.obj = obj;}

    @Override
    public String toString() {
        return super.toString();
    }
}
