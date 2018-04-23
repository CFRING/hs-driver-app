package com.hongshi.wuliudidi.photo;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.camera.ActivityCapture;
import com.hongshi.wuliudidi.utils.UploadUtil;


public class GetPhotoUtil {
	//请求码新定义
	// 拍照
	public static final int PHOTO_REQUEST_CAMERA = 10;
	// 从相册中选择
	public static final int PHOTO_REQUEST_GALLERY = 20;
	// 剪裁结果
	public static final int PHOTO_REQUEST_CUT = 30;
	public static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	/**
	 * 向相册获取照片。执行结束后会回调mIntentde的onActivityResult方法，requestCode等于GetPhotoUtil.PHOTO_REQUEST_GALLERY。
	 * 从相册选中的图片全路径将以Uri对象的形式保存在回调函数onActivityResult的参数data中，
	 */
	public static void callGallery(Activity mIntent){
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		mIntent.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}
	/**
	 * 通过摄像头拍摄获取照片。执行结束后会回调mIntentde的onActivityResult方法，requestCode等于GetPhotoUtil.PHOTO_REQUEST_CAMERA。
	 * 摄像头拍摄到的图片路径将以String形式保存在回调函数onActivityResult的参数data中.
	 */
	public static void callCamera(Activity mIntent) {
		//拍照带自动剪切框的
		Intent intent = new Intent(mIntent, ActivityCapture.class);
		// 判断存储卡是否可以用，可用进行存储
		if (UploadUtil.hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		mIntent.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	/**
	 * 带上竞拍量
	 */
	public static void callCamera(Activity mIntent,int amount) {
		//拍照带自动剪切框的
		Intent intent = new Intent(mIntent, ActivityCapture.class);
		// 判断存储卡是否可以用，可用进行存储
		if (UploadUtil.hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
			intent.putExtra("amount",amount);
		}
		mIntent.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}
	
	/**
	 * 对图片进行裁剪。执行结束后会回调mIntentde的onActivityResult方法，requestCode等于GetPhotoUtil.PHOTO_REQUEST_CUT。
	 * 传入的File型参数在执行结束后会保存下所得的图片内容。
	 */
	public static void crop(Activity mActivity, Uri uri, File tempFile) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
////		// 裁剪框的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
//        File temp = new File("/sdcard/wuliudidi/");//自已项目 文件夹
//        if (!temp.exists()) {
//            temp.mkdir();
//        }
		// 图片格式
		// 传入目标文件
		intent.putExtra("output", Uri.fromFile(tempFile));
		intent.putExtra("outputFormat", "JPEG");
		// 取消人脸识别
		intent.putExtra("noFaceDetection", true);
		// true:不返回uri，false：返回uri
		intent.putExtra("return-data", true);
		mActivity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
}
