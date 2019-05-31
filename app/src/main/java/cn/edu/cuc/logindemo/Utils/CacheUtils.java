package cn.edu.cuc.logindemo.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.domain.Enums;

/**
 * 应用缓存相关工具类
 */
public class CacheUtils {

    private final static int FREE_SD_SPACE_NEEDED_TO_CACHE = 200;
    private final static int MB = 1024 * 1024;
    public final static String DEFAULT_PHOTO_URL = "file:///android_asset/defaultphoto.png";

    /**
     * 获取缓存中的图片文件
     *
     * @param url
     * @return
     */
    public static Bitmap createBitmapFromCache(String url) {

        String fileName = convertUrl2FileName(url);

        Bitmap bitmap = BitmapFactory.decodeFile(StandardizationDataUtils
                .getBitmapCacheStorePath() + "/" + fileName);

        if (bitmap != null) {
            updateFileTime(StandardizationDataUtils.getBitmapCacheStorePath(),fileName );
        }

        return bitmap;
    }

    public static Bitmap createBitmapFromCacheResize(String url) {

        String fileName = convertUrl2FileName(url);
        String imagePath = StandardizationDataUtils
                .getBitmapCacheStorePath() + "/" + fileName;

        DeviceInfoUtils device = new DeviceInfoUtils();
        int height = device.getWindowWidth() / 2 * 9 / 16;
        int width = device.getWindowWidth() / 2 ;
        Bitmap bitmap = MediaUtils.getImageThumbnail(imagePath, width, height, true);

        return bitmap;
    }

    /**
     * 判断文件是否已经存在
     *
     * @param url
     * @return
     */
    public static boolean isBitmapInCache(String url) {

        String fileName = convertUrl2FileName(url);

        File file = new File(StandardizationDataUtils.getBitmapCacheStorePath() + "/" + fileName);

        return file.exists();
    }

    /**
     * 清除已使用的图片缓存
     */
    public static void cleanRunningCache() {

        System.gc();
        System.runFinalization();
    }

    /**
     * 获取缓存中的图片路径，若该图片不存在，则返回"file:///android_asset/defaultphoto.png"
     * @param url
     * @param type
     * @return
     */
    public static String getSrcFromCache(String url, Enums.NewsPictureSize picSize) {

        return getSrcFromCache(url, 0, picSize);
    }

    public static String getSrcFromCache(String url, int index, Enums.NewsPictureSize picSize) {

        String fileName = convertUrl2FileName(url, index, picSize);

        String src = StandardizationDataUtils.getBitmapCacheStorePath() + "/" + fileName;

        File file = new File(StandardizationDataUtils.getBitmapCacheStorePath() + "/"+ fileName);

        if (file.exists())
            return src;
        else
            return DEFAULT_PHOTO_URL;
    }

    public static String convertUrl2FileName(String id, int index, Enums.NewsPictureSize picSize) {

        String fileName = String.valueOf(id) + "." + String.valueOf(index);

        fileName = fileName + "." + picSize.getValue().toString();

        return fileName;
    }

    /**
     * 获取缓存中的图片路径
     *
     * @param url
     * @return
     */
    public static String getBitmapSrcFromCache(String url) {

        String fileName = convertUrl2FileName(url);

        return StandardizationDataUtils.getBitmapCacheStorePath() + "/" + fileName;
    }

    public static long getBitmapLength(String url) {

        String fileName = convertUrl2FileName(url);

        File file = new File(
                StandardizationDataUtils.getBitmapCacheStorePath() + "/" + fileName);

        if (file.exists())
            return file.length();
        else
            return 0;
    }

    /**
     * 将url转换为文件名形式
     *
     * @param url
     * @return
     */
    public static String convertUrl2FileName(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);

        return fileName;
    }

    /**
     * 将缩略图地址转换为中图地址
     *
     * @param url
     * @return
     */
    public static String convertThumburl2Middleurl(String url) {

        if (TextUtils.isEmpty(url))
            return "";

        int index = url.lastIndexOf('.');

        String suffix = url.substring(index + 1);

        url = url.substring(0, index - 1);

        String result = url + "m." + suffix;

        return result;
    }

    public static String convertThumburl2Largeurl(String url) {

        if (TextUtils.isEmpty(url))
            return "";

        int index = url.lastIndexOf('.');

        String suffix = url.substring(index + 1);

        url = url.substring(0, index - 1);

        String result = url + "l." + suffix;

        return result;
    }

    /**
     * 将缩略图地址转换为中图地址
     *
     * @param url
     * @return
     */
    public static String convertIosUrl2Android(String url) {

        if (TextUtils.isEmpty(url))
            return "";

        int index = url.lastIndexOf('.');

        String suffix = "mp4";

        url = url.substring(0, index);

        String result = url + "A001." + suffix;

        return result;
    }

    /**
     * 将已下载的图片资源缓存到SD卡上
     *
     * @param bitmap
     * @param url
     */
    public static String saveSource2Cache(Bitmap bitmap, String url) {
        if (bitmap == null) {
            LogUtils.d("trying to save null bitmap");
        }

        // TODO 判断已经占有的空间，不可占用太多
        // 将url转换为文件名
        String fileName = convertUrl2FileName(url);

        String directory = StandardizationDataUtils.getBitmapCacheStorePath();

        File file = new File(directory + "/" + fileName);

        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            LogUtils.d("Image saved to sd");

            updateFileTime(directory, fileName);

            return file.getPath();

        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (IOException e) {
            LogUtils.e(e);
        }

        return "";
    }

    /**
     * 从远程下载图片到Bitmap对象中(还没有网SD卡上存储)
     *
     * @param url
     * @return
     */
    public static Bitmap fetchImageFromRemote(String url) {

        URL m;
        InputStream is = null;

        if(AndroidApplication.getNetStatus() == Enums.NetStatus.Disable){
            return null;
        }
        try {
            m = new URL(url);
            is = (InputStream) m.getContent();
        } catch (MalformedURLException e1) {
            LogUtils.e(e1);
        } catch (IOException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        }


        if(is == null)
            return null;

        try {
            Bitmap tempBitmap = BitmapFactory.decodeStream(is);

            return tempBitmap;

        } catch (Exception e) {
            LogUtils.e(e);
        }

        return null;
    }

    /**
     * 清理过期文件，规则，发现缓存超过FREE_SD_SPACE_NEEDED_TO_CACHE，则按时间从早到晚排序，删除40%
     */
    public static void removeExpiredCache() {
        File dir = new File(StandardizationDataUtils.getBitmapCacheStorePath());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            dirSize += files[i].length();
        }

        if (dirSize > FREE_SD_SPACE_NEEDED_TO_CACHE * MB) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());

            LogUtils.d("Clear some expiredcache files ");

            for (int i = 0; i < removeFactor; i++) {
                files[i].delete();

                LogUtils.d("Delete file " + files[i].getName());
            }
        }
    }

    /**
     * 将缓存全部删除
     */
    public static void removeCache() {

        removeFiles(StandardizationDataUtils.getBitmapCacheStorePath());

        removeFiles(StandardizationDataUtils.getConfigFileStorePath());

        removeFiles(StandardizationDataUtils.getLogFileStorePath(Enums.LogType.System));

        //TODO 底层方法
//		logic.delete();
    }

    private static void removeFiles(String path){
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        LogUtils.d("Clear all files ");

        for (int i = 0; i < files.length; i++) {
            files[i].delete();

            LogUtils.d("Delete file " + files[i].getName());
        }
    }

    /**
     * 更新最后修改时间
     *
     * @param dir
     * @param fileName
     */
    private static void updateFileTime(String dir, String fileName) {
        File file = new File(dir, fileName);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 获取文件大小
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f){
        long s = 0;
        try {
            if (f.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(f);
                s = fis.available();
            }else{
                f.createNewFile();
                System.out.println("文件不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e);
        }

        return s;
    }

    /**
     * 获取文件夹大小
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSize(File f){
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if(flist[i].isDirectory()){
                size = size + getFileSize(flist[i]);
            }else{
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小单位(b/kb/mb/gb)
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        // 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }else if(fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "K";
        }else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        }else{
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }

        return fileSizeString;
    }

    /**
     * 获取文件个数
     * @param f
     * @return
     */
    public static long getlist(File f) {
        // 递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++){
            if(flist[i].isDirectory()){
                size = size + getlist(flist[i]);
                size--;
            }
        }
        return size;
    }
}
