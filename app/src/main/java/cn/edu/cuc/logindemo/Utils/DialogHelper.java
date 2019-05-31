package cn.edu.cuc.logindemo.Utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {

	private Context context;
	private ProgressDialog dialog = null; 
	public DialogHelper(Context context){
		
		this.context = context;
		dialog = null;
	}
	
	/**
	 * 弹出阻塞对话
	 * @param message
	 * @param listener
	 * @param title
	 * @param yes
	 * @param no
	 */
	public void dialog(String message,DialogInterface.OnClickListener listener,
			String title, String yes, String no) {
		
		Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);

		builder.setPositiveButton(yes, listener);
		builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	/**
	 * 显示等待对话
	 */
	public void showLoadingDialog(String message) {
		
		dialog = ProgressDialog.show(context, "", message, true);
	}
	
	/**
	 * 关闭等待对话
	 */
	public void closeLoadingDialog() {
		
		if (dialog != null&&dialog.isShowing())
			dialog.dismiss();
	}
}
