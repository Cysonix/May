package cn.edu.cuc.logindemo.userlayout;

/**
 * Created by SongQing on 2019/4/2.
 * 自定义文本编辑器控件类型
 */
public interface EditTextType{

    //手机校验类型
    int TYPE_OF_MOBILE=0;
    //座机校验类型
    int TYPE_OF_TEL=1;
    //邮箱校验类型
    int TYPE_OF_EMAIL=2;
    //url校验类型
    int TYPE_OF_URL=3;
    //汉字校验类型
    int TYPE_OF_CHZ=4;
    //用户名校验类型
    int TYPE_OF_USERNAME=5;
    //用户自定义
    int TYPE_OF_USER_DEFINE=6;
    //无需校验
    int TYPE_OF_NULL=7;
}
