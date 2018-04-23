package com.hongshi.wuliudidi.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;


public class LocalPropertyUtil {
	public static final String FILENAME = "config.dat";
	public static final String AllArea = "allArea";
	public static final String AddressVersion = "addressVersion";
	
	public static String getProperty(Context mContext, String propertyName){
		try {
			return getProperties(mContext).getProperty(propertyName, "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static void saveProperty(Context mContext, String propertyName, String value) {
		Properties prop = getProperties(mContext);
		prop.setProperty(propertyName, value);
		try {  
			FileOutputStream fstream = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			prop.store(fstream, "");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static Properties getProperties(Context mContext){
		Properties prop = new Properties();
		try {
			FileInputStream fstream = mContext.openFileInput(FILENAME);
			prop.load(fstream);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

//	public static void writeFiles(String content, Context context) {
//		try {
//			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_APPEND);
//			fos.write(content.getBytes());
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static String readFiles(Context context) {
//		String content = "";
//		try {
//			FileInputStream fis = context.openFileInput(FILENAME);
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = fis.read(buffer)) != -1) {
//				baos.write(buffer, 0, len);
//			}
//			content = baos.toString();
//			fis.close();
//			baos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return content;
//	}
//	
//	public static void cityDeleteFile(Context context){
//		context.deleteFile(FILENAME);
//	}
}
