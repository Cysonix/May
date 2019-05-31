package cn.edu.cuc.logindemo.Utils;

import android.os.Debug;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.cuc.logindemo.domain.Enums;

/**
 * 软件日志工具类
 * @author songqing
 *
 */
public class LogUtils {

    /**
     * 日志标识
     */
    private static final String TAG = "MITI_DEMO";

    /**
     * 系统日志保存路径
     */
    private static final String SYSTEM_LOG_SAVE_PATH = StandardizationDataUtils.getLogFileStorePath(Enums.LogType.System);
    /**
     * 操作日志保存路径
     */
    private static final String OPERATION_LOG_SAVE_PATH = StandardizationDataUtils.getLogFileStorePath(Enums.LogType.Operation);

    /**
     * 日志：记录调试信息
     *
     * @param message
     */
    public static void d(String message) {
        Log.d(TAG, message);
    }

    /**
     * 日志：记录系统异常、错误信息
     *
     * @param e
     */
    public static void e(Throwable e) {
        Log.e(TAG, e.getMessage(), e);

        File file = checkSystemLogExist();

        Log(buildSystemMessage(e), file);
    }

    /**
     * 日志：记录操作信息
     *
     * @param message
     */
    public static void o(String message) {

        Log.v(TAG, message);

        File file = checkOperationLogExist();

        Log(message, file);
    }

    /**
     * 删除系统日志
     * @param startDate 开始时间
     * @param endDate 结束时间
     */
    public static void ReomveSystemLogs(Date startDate, Date endDate) {
        DeleteFiles(startDate, endDate, SYSTEM_LOG_SAVE_PATH);
    }

    public static void logHeap(Class<?> clazz) {
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
        Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
        Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        Log.d(TAG, "debug. =================================");
        Log.d(TAG, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free) in [" + clazz.getName().replaceAll("com.myapp.android.","") + "]");
        Log.d(TAG, "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
        System.gc();
        System.gc();
    }


    /**
     * 删除日志文件
     * @param startDate
     * @param endDate
     * @param logPath
     */
    private static void DeleteFiles(Date startDate, Date endDate, String logPath){
        long sl = startDate.getTime();
        long el = endDate.getTime();
        long ei = el - sl;

        int interval = (int)ei / (1000 * 60 * 60 * 24);

        for(int i = 0; i <= interval; i++){
            try {

                Date computeDate = TimeUtils.addDate(startDate, i);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String dateStr = sdf.format(computeDate);

                String logFileName = dateStr + ".txt";

                File file = checkLogFileIsExist(logPath, logFileName);

                if(file != null)
                    file.delete();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        }
    }

    /**
     * 根据异常对象拼写异常日志信息
     *
     * @param e
     * @return
     */
    private static String buildSystemMessage(Throwable e) {

        String message = null;

        message = String.format("%s,%s,%s,%s",
                e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[0].getMethodName(),
                e.getStackTrace()[0].getLineNumber(), e.toString());

        return message;
    }

    /**
     * 记录日志信息
     *
     * @param message
     *            当次日志信息
     * @param file
     *            日志文件类型
     */
    private static void Log(String message, File file) {
        if (file == null)
            return;

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(file, true);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            fos.write((sdf.format(new Date()) + "," + message).getBytes("gbk"));

            fos.write("\r\n".getBytes("gbk"));

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            try {

                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (IOException e) {

                e.printStackTrace();

            }
            fos = null;
            file = null;
        }
    }

    /**
     * 检查操作日志文件是否已经存在，不存在则建立此日志文件对象
     *
     * @return 返回日志文件对象，出异常返回null
     */
    private static File checkSystemLogExist() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateStr = sdf.format(new Date());

        String logFileName = dateStr + ".txt";

        return checkLogFileIsExist(SYSTEM_LOG_SAVE_PATH, logFileName);
    }

    /**
     * 检查操作日志文件是否已经存在，不存在则建立此日志文件对象
     *
     * @return 返回日志文件对象，出异常返回null
     */
    private static File checkOperationLogExist() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateStr = sdf.format(new Date());

        String logFileName = dateStr + ".txt";

        return checkLogFileIsExist(OPERATION_LOG_SAVE_PATH, logFileName);
    }

    /**
     * 检测sd卡内是否存在日志文件
     *
     * @param path
     *            检测路径
     * @return 寻找或创建的日志文件对象，如果无sd卡，将返回null
     */
    private static File checkLogFileIsExist(String path, String LogFileName) {

        if(path == "")
            return null;

        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(path + "//" + LogFileName);

        // 检查日志文件是否存在
        if (!file.exists()) {
            try {

                file.createNewFile();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return file;
    }
}
