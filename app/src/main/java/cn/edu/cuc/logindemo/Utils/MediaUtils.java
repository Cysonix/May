package cn.edu.cuc.logindemo.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.domain.Enums;


/**
 * 媒体文件工具类
 */
public class MediaUtils {

    /**
     * 缩放图片
     * @param img
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImg(String img, int newWidth, int newHeight) {
        // 图片源
        Bitmap bm = BitmapFactory.decodeFile(img);
        if (null != bm) {
            return zoomImg(bm, newWidth, newHeight);
        }
        return null;
    }

    /**
     * 缩放图片
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 缩放图片
     * @param bm
     * @param newWidth
     * @return
     */
    public static Bitmap zoomImgByWidth(Bitmap bm, int newWidth) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scale = ((float) newWidth) / width;
        // float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 缩放图片
     * @param bm
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImgByHeight(Bitmap bm, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        // float scale = ((float) newWidth) / width;
        float scale = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    public static int getImageDetailsWidth(Activity activity) {

        DisplayMetrics dm = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels / 4 * 3;

    }

    public static int getImageDetailsHeight(Activity activity) {

        DisplayMetrics dm = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels / 3;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private static Bitmap audioBitmap = null;

    /**
     * 获取文件类型
     *
     * @param 文件路径
     * @return
     */
    public static Enums.AccessoryType checkFileType(String name) {

        Enums.AccessoryType accType = Enums.AccessoryType.Cache;

        String end = name.substring(name.lastIndexOf(".") + 1, name.length())
                .toLowerCase();

        if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")
                || end.equals("bmp") || end.equals("tiff") || end.equals("ico")
                || end.equals("gif")) {
            accType = Enums.AccessoryType.Picture;
        } else if (end.equals("3gp") || end.equals("mov") || end.equals("avi")
                || end.equals("mpg") || end.equals("mpeg") || end.equals("mp4")) {
            accType = Enums.AccessoryType.Video;
        } else if (end.equals("amr") || end.equals("aac") || end.equals("mp3")
                || end.equals("wav") || end.equals("aif") || end.equals("m4a")
                || end.equals("mid")) {
            accType = Enums.AccessoryType.Voice;
        } else {
            accType = accType.Complex;
        }

        return accType;
    }

    private static Bitmap defaultLoadingBitmap;

    public static Bitmap getDefaultLoadingBitmap() {

        if (defaultLoadingBitmap != null)
            return defaultLoadingBitmap;
        else {
            defaultLoadingBitmap = BitmapFactory.decodeResource(AndroidApplication.getInstance().getResources(), R.drawable.default_image);

            return defaultLoadingBitmap;
        }
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath
     *            图像的路径
     * @param width
     *            指定输出图像的宽度
     * @param height
     *            指定输出图像的高度
     * @return 生成的图片
     */
    public static Bitmap getImageThumbnail(String imagePath, int width,
                                           int height) {
        return getImageThumbnail(imagePath, width, height, true);
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath
     *            图像的路径
     * @param width
     *            指定输出图像的宽度
     * @param height
     *            指定输出图像的高度
     * @param createThumbnail
     *            是否生成缩略图
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width,
                                           int height, boolean createThumbnail) {

        int degree = getExifRotate(imagePath);

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);

        // 计算缩放比
        options.inSampleSize = 4;
        if (createThumbnail) {
            int h = options.outHeight;
            int w = options.outWidth;

            int beHeight = h / height;
            int beWidth = w / width;

            int be = 1;
            if (beWidth < beHeight) {
                be = beWidth;
//				height = h / be;
            } else {
                be = beHeight;
//				width = w / be;
            }
            if (be <= 0) {
                be = 1;
            }
            options.inSampleSize = be;
        }
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath, options);

        bitmap = rotateBitmap(degree, bitmap);

        if (createThumbnail)
            // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        if (degree != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
        }
        return bitmap;
    }

    private static int getExifRotate(String imagePath) {
        ExifInterface exifInterface;
        int degree = 0;
        try {
            exifInterface = new ExifInterface(imagePath);
            int tag = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);

            if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
                degree = 90;
            } else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
                degree = 180;
            } else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
                degree = 270;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath
     *            视频的路径
     * @param width
     *            指定输出视频缩略图的宽度
     * @param height
     *            指定输出视频缩略图的高度度
     * @param kind
     *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width,
                                           int height) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
                MediaStore.Images.Thumbnails.MICRO_KIND);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取Bitmap的字节数组作为微信分享图片的
     * @param bitmap
     * @param paramBoolean
     * @return
     */
    public static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
                    80, 80), null);
            if (paramBoolean)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {

            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }


    /**
     * 获取图片附件的详细信息
     *
     * @param imagePath
     * @return
     */
    public static String getImageInfo(String imagePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);

        String result = options.outWidth + "," + options.outHeight;

        return result;
    }

    public static String getVideoInfo(String videoPath) {

        // MediaMetadataRetriver retriver = new MediaMetadataRetriver();

        return "null";
    }

    public static String getVoiceInfo(String voicePath) {
        return "null";
    }

    /**
     * 将媒体文件拷贝至临时文件夹供上传用
     *
     * @param imagePath
     */
    public static String copy2TempStore(String sourcePath) throws IOException {

        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists())
            return "";

        // 迁移文件前进行重命名
        String prefix = sourceFile.getName().substring(
                sourceFile.getName().lastIndexOf(".") + 1);
        String desName = String.valueOf(System.currentTimeMillis()) + "."
                + prefix;
        ;
        String destination = StandardizationDataUtils
                .getAccessoryFileTempStorePath() + "//" + desName;

        File targetFile = new File(destination);

        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }

        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

            return destination;
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    /**
     * 将媒体文件剪切至临时文件夹供上传用
     *
     * @param imagePath
     */
    public static String move2TempStore(String sourcePath) throws IOException {

        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists())
            return "";

        String destination = StandardizationDataUtils
                .getAccessoryFileTempStorePath() + "//" + sourceFile.getName();

        File targetFile = new File(destination);

        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }

        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

            // 删除源文件
            sourceFile.delete();

            return destination;
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }

    }

    /**
     * 获取媒体文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {

        File file = new File(path);

        if (file.exists())
            return file.getName();
        else
            return "";
    }

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String getFileSize(String path) throws IOException {
        File file = new File(path);

        int fileLen = 0;

        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);

            fileLen = fis.available();
        }
        return String.valueOf(fileLen);
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 判断是否URI格式的路径信息
     *
     * @param uri
     * @return
     */
    public static boolean isUri(String uri) {

        uri = uri.toLowerCase();

        if (uri.startsWith("content"))
            return true;
        else
            return false;
    }
}
