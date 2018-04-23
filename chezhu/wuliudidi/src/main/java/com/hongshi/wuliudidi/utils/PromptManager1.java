package com.hongshi.wuliudidi.utils;



import android.app.ProgressDialog;
import android.content.Context;

/**
 * 提示信息的管
 */

public class PromptManager1 {
	private static ProgressDialog dialog;
	public static void showProgressDialog(Context context,String string) {
		closeProgressDialog();
		dialog = new ProgressDialog(context);
		dialog.setMessage(string);
		dialog.show();
	}
	public static void showProgressDialog1(Context context,String string) {
		closeProgressDialog();
		dialog = new ProgressDialog(context);
		dialog.setMessage(string);
		//dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress));
		dialog.setCancelable(false);
		dialog.show();
	}

	public static void setCancelable(boolean b) {
		// 在android高版本中，单击进度对话以部分会，进度对话框会消息，在里设置false不会消失
		dialog.setCancelable(b);
//		dialog.setCanceledOnTouchOutside(b);
	}

	public static boolean isShow() {
		if(dialog != null){
			if(dialog.isShowing()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
