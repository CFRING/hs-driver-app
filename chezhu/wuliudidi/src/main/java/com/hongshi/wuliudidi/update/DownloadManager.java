package com.hongshi.wuliudidi.update;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadManager {
	/**
	 * 下载文件的操作?
	 * @param fileurl  文件的路径?
	 * @param path 保存文件的路径?
	 * @param pd 下载进度的对话框
	 * @return  返回null下载失败
	 */
	public static File download(String fileurl, String path, Handler handler){
		try {
			URL url = new URL(fileurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			int code = conn.getResponseCode();
			if(code == 200){
				int max = conn.getContentLength();
//				if(pd != null){
//					pd.setMax(max/1024);
//				}
				InputStream is = conn.getInputStream();
				File file = new File(path);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				int total=0;
				while((len = is.read(buffer))!=-1){
					fos.write(buffer, 0, len);
					total+=len;
//					if(pd != null){
//						pd.setProgress(total/1024);
//					}
					if(handler != null){
						Message message = new Message();
						message.what = 1001;
						message.arg1 = total;
						message.arg2 = max;
						handler.sendMessage(message);
					}
				}
				is.close();
				fos.close();
				return file;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
