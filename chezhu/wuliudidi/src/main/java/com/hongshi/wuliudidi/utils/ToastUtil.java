/**
 * 
 */
package com.hongshi.wuliudidi.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}
	
	public static void doubleNonpositiveError(Context context, String subject){
		Toast.makeText(context, "保留两位小数后" + subject + "应大于0", Toast.LENGTH_SHORT).show();
	}
	
	public static void doubleParseError(Context context, String subject){
		Toast.makeText(context, subject + "应为数字（最多两位小数）", Toast.LENGTH_SHORT).show();
	}
	
	public static void integerNonpositiveError(Context context, String subject){
		Toast.makeText(context, subject + "应大于0", Toast.LENGTH_SHORT).show();
	}
	
	public static void integerParseError(Context context, String subject){
		Toast.makeText(context, subject + "应为整数", Toast.LENGTH_SHORT).show();
	}
	
}
