package cn.edu.cuc.logindemo.userlayout;

/**
 * 展示提示信息接口类
 * Created by DELL on 2019/4/2.
 */

public interface PromptStatus {
    /**
     * 展示警告语
     *
     * @param msgs
     */
    void show(String... msgs);

    /**
     * 隐藏警告语
     */
    void hide();
}
