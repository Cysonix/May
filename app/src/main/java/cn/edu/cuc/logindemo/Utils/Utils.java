package cn.edu.cuc.logindemo.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
//				is.close();
//				os.close();
			}
		} catch (Exception ex) {
		}
	}
	
	/**
	 * 进行dp到px的转换
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int  convertDp2Pixel(Context context, int dp){
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(dp*displayMetrics.density);
	}
	
	/**
	 * 进行px到dp的转换
	 * @param context
	 * @param pixel
	 * @return
	 */
	public static int  convertPixel2Dp(Context context, int pixel){
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(pixel/displayMetrics.density);
	}

}
