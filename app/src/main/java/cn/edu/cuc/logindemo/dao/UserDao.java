package cn.edu.cuc.logindemo.dao;

import android.content.Context;
import android.database.Cursor;

import cn.edu.cuc.logindemo.domain.User;

/**
 * Created by DELL on 2019/4/30.
 */

public class UserDao {

    private static SQLiteHelper sqlHelper;

    public UserDao(Context context) {
        sqlHelper = new SQLiteHelper(context);
    }

    public boolean checkUser(User user){

        boolean result = false;
        if(user!=null) {
            String sql = "select * from User where username =? and passwords=?";
            String[] params = { user.getUsername(), user.getPassword()};

            Cursor cursor = sqlHelper.findQuery(sql,params);

            if (cursor != null && cursor.getCount() > 0) {
                result =  true;
            }
            cursor.close();
        }

        return result;
    }
}
