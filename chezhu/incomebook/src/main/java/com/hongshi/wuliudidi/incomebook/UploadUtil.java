package com.hongshi.wuliudidi.incomebook;

import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class UploadUtil {
	public static final int TYPE_BOTTOM  = 0;//dialog底部弹出标记
	public static final int TYPE_CENTER  = 1;//居中显示
	/**
	 * 设置dialog从弹出,type可以为Gravity.BOTTOM或Gravity.CENTER
	 * @param dialog
	 */
	public static void setAnimation(Dialog dialog, int type, boolean isWidMatchParent){
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.dialog);
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
				if(isWidMatchParent){
					lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				}else{
					lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
				}
		        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		        window.setAttributes(lp);
		        if(type == TYPE_BOTTOM){
		        	window.setGravity(Gravity.BOTTOM); 
			        //此处可以设置dialog显示的位置 
		        }else if(type == TYPE_CENTER){
		        	window.setGravity(Gravity.CENTER); 
		        }
	}

}
