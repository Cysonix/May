package cn.edu.cuc.logindemo.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DELL on 2019/4/29.
 */

public class TimeUtils {
    public static Date addDate(Date d, int day) throws ParseException {

        long time = d.getTime();
        day = day * 24 * 60 * 60 * 1000;
        time += day;
        return new Date(time);

    }

    public static int DateInterval(Date st, Date ed) {
        long sl = st.getTime();
        long el = ed.getTime();
        long ei = el - sl;

        int interval = (int) ei / (1000 * 60 * 60 * 24);

        return interval;
    }

    public static String getFormatTime(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS Z");
        return formatter.format(date);
    }

    /**
     * 转换为字符串，时间格式yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getFormatTimeV2(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * 转换为字符串，时间格式yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getFormatTimeV3(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 转换为字符串，时间格式yyyy年MM月dd日
     * @param date
     * @return
     */
    public static String getFormatTimeV4(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd");
        return formatter.format(date);
    }

    /**
     * 转换为字符串，时间格式hh:mm
     * @param date
     * @return
     */
    public static String getFormatTimeV5(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    /**
     * 转换为字符串，时间格式MM-dd
     * @param date
     * @return
     */
    public static String getFormatTimeV6(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        return formatter.format(date);
    }


    public static String getShortFormatTime(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * modify by SongQing ,增加从GMT 到本地时间的对应转换，解决显示GMT时间的问题
     *
     * @param date
     * @return
     */
    public static Date parse(String date) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS Z");
        Date temp = null;
        try {
            temp = format.parse(date);
            // double zone = TimeZone.getDefault().getRawOffset();

            // temp = new Date((long) (temp.getTime() - zone));
            temp = new Date((long) (temp.getTime()));

        } catch (Exception e) {
            LogUtils.e(e);
        }
        return temp;
    }

    /**
     * modify by SongQing ,时间转换，从yyyy-MM-dd HH:mm:ss格式转换到Date类型
     *
     * @param date
     * @return
     */
    public static Date parseV2(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = null;
        try {
            temp = format.parse(date);

            temp = new Date((long) (temp.getTime()));

        } catch (Exception e) {
            LogUtils.e(e);
        }
        return temp;
    }

    /**
     * 获取GMT时间，时间格式：20061218.094246.930+0800
     *
     * @param date
     * @return
     */
    public static String getGMTTime(Date date) {

        if (date == null)
            return "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");

        SimpleDateFormat timeZoneFormatter = new SimpleDateFormat("Z");

        String timeZone = timeZoneFormatter.format(date);

        int position = timeZone.indexOf("-");

        if (position == -1)
            position = timeZone.indexOf("+");

        timeZone = timeZone.substring(position);

        String dateGMT = formatter.format(date);

        dateGMT = dateGMT.concat(timeZone);

        dateGMT = dateGMT.replace(":", "");

        return dateGMT;
    }

    /**
     * 获取GMT时间，时间格式：20061218.094246.930+0800
     *
     * @param date
     *            传入时间：20061218.094246.930+0800
     * @return
     */
    public static String getGMTTime(String date) {

        Date temp = parse(date);

        if (temp == null)
            return "";

        return getGMTTime(temp);
    }

    /**
     * 将毫秒转换为标准时间格式
     *
     * @param ms
     *            eg：1340590080000
     * @return
     */
    public static String convertLongToDate(String ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(Long.parseLong(ms));

            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            LogUtils.e(e);
            return formatter.format(new Date());
        }
    }


    /**
     * 将标准时间格式转换为毫秒
     *
     * @param ms
     * @return
     */
    public static long convertDateToLong(String ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date myDate = formatter.parse(ms);

            return myDate.getTime();
        } catch (Exception e) {
            LogUtils.e(e);
            return new Date().getTime();
        }
    }

    /***
     * 转换时间为新华国际时间 时间差距为一分钟内，表示为x秒前 时间差距为一小时内，表示为x分钟前 时间差距为一天内，表示为x小时前
     * 时间差距为三天内，表示为x天前 其余显示x.x
     *
     * @param date
     * @return
     */
    public static String convertDateToXHITime(Date date) {
        Date currentDate = new Date();

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;

        try {
            long time1 = date.getTime();
            long time2 = currentDate.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = "";

        String dayString = "天前";
        String hourString = "小时前";
        String minuteString = "分钟前";
        String secondString = "秒前";

        if(day > 3){
            result = getFormatTimeV3(date);
        }
        else if(day > 0){
            result = String.valueOf(day) + dayString;
        }else if(hour > 0){
            result = String.valueOf(hour) + hourString;
        }else if(min > 0){
            result = String.valueOf(min) + minuteString;
        }
        else if(sec > 0){
            result = String.valueOf(sec) + secondString;
        }
        return result;
    }
}
