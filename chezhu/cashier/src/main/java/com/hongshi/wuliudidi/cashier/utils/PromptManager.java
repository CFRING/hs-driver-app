package com.hongshi.wuliudidi.cashier.utils;



import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

/**
 * 提示信息的管
 */

public class PromptManager {
	private ProgressDialog dialog;
	public void showProgressDialog(Context context, String string) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(string);
		dialog.show();
	}
	public void showProgressDialog1(Context context, String string) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(string);
		//dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress));
		dialog.setCancelable(false);
		dialog.show();
	}

	public void setCancelable(boolean b) {
		dialog.setCancelable(b); // 在android高版本中，单击进度对话以部分会，进度对话框会消息，在里设置false不会消失
//		dialog.setCanceledOnTouchOutside(b);
	}
	
	public boolean isShow() {
		if(dialog != null){
			if(dialog.isShowing()){
				return true;	
			}else{
				return false;	
			}
		}
		return false;
	}
	public void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	/**
	 * 当判断当前手机没有网络时使用
	 * @param context
	 */
	public static void showNoNetWork(final Context context) {
		Builder builder = new Builder(context);
		builder
				.setTitle("网络状态")//
				.setMessage("当前无网").setPositiveButton("设置", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(android.os.Build.VERSION.SDK_INT>10){
						     Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						     context.startActivity(intent);
						  }else{
						       Intent intent = new Intent();
						       ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
						       intent.setComponent(component);
						       intent.setAction("android.intent.action.VIEW");
						       context.startActivity(intent);
						     }						
					}					
				})
				.setNegativeButton("知道了", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();
	}

}
