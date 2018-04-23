package com.hongshi.wuliudidi.utils;

import java.io.File;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class UploadUtil {
	// 拍照
	public static final int PHOTO_REQUEST_CAMERA = 1;
	// 从相册中选择
	public static final int PHOTO_REQUEST_GALLERY = 2;
	public static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	public static File tempFile;

	/*
	 * 从相机获取
	 */
	public static void camera(Activity mContext,Handler mHandler) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		mContext.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}
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
		        if(type == CommonRes.TYPE_BOTTOM){
		        	window.setGravity(Gravity.BOTTOM); 
			        //此处可以设置dialog显示的位置 
		        }else if(type == CommonRes.TYPE_CENTER){
		        	window.setGravity(Gravity.CENTER); 
		        }
	}

	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	// 从图库选择
	public static void photo(Activity mContext,Handler mHandler) {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		mContext.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}
}
