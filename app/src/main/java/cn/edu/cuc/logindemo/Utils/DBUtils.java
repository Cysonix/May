package cn.edu.cuc.logindemo.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import cn.edu.cuc.logindemo.R;

/**
 * 把已有的数据库(res/raw/)传入到/data/data/(package name)/ 目录下
 *
 * @author SongQing
 * @date 2019-04-29
 *
 */
public class DBUtils {
    private final int BUFFER_SIZE = 40000;

    public static final String DB_NAME = "demo.db"; // 数据库文件名
    public static final String PACKGE_NAME = "cn.edu.cuc.logindemo";
    private static final String TAG="DBUtils";
    public static String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKGE_NAME + "/" + "databases"; // 在手机里存放数据库的位置

    public static String appPath = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKGE_NAME; // 在手机里存放程序的位置

//	 public static String DB_PATH =
//	 StandardizationDataHelper.getConfigFileStorePath();

    private Context context;

    public DBUtils(Context context) {
        this.context = context;
    }

    /**
     * 创建数据库(手机中未安装系统的情况下)
     *
     * @param dbfile
     * @return
     */
    public boolean createDatabase(){
        boolean mark = false;       //DB路径、文件有效性标识
        boolean result = false;     //数据库创建结果
        InputStream is = null;
        FileOutputStream fos = null;
        try {
             // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            // 首先判断数据库的文件目录是否存在，不存在则创建
            File fileDirectory = new File(DB_PATH);
            //Step1：检查数据库文件目录
            if(!fileDirectory.exists()){
                //创建数据库文件存储目录
                mark =  fileDirectory.mkdirs(); // 目录创建
            }else{
                mark = true;
            }

            if(!mark){
                return result;
            }

            //Step2：检查数据库文件本身
            File fileDatabase = new File(DB_PATH + "/" + DB_NAME);
            if (!fileDatabase.exists()) { // 如果不存在
                is = this.context.getResources().openRawResource(R.raw.demo); // 欲导入的数据库
                fos = new FileOutputStream(DB_PATH + "/" + DB_NAME, false);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            result = true;
            Log.i(TAG,"creat database");
        } catch (Exception ex) {
            Log.e(TAG,ex.toString());
        }

        return result;
    }

    /**
     * 删除现有数据库
     */
    public void deleteDatabase() {
        // 先删除数据库文件
        File file = new File(DB_PATH + "/" + DB_NAME);
        if (file.exists()) {
            file.delete();
        }

        file = new File(DB_PATH + "/" + "in.db-journal"); // 数据库运行临时文件，如果程序错误中断，此文件有可能保留，所以需要删除
        if (file.exists()) {
            file.delete();
        }
        // 再删除数据库文件所在的目录
        File fileDirectory = new File(DB_PATH);
        if (fileDirectory.exists()) {
            fileDirectory.delete();
        }
    }
}

