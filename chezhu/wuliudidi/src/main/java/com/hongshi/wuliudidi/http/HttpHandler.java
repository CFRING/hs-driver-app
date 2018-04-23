package com.hongshi.wuliudidi.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.hongshi.wuliudidi.DidiApp;

/**
 * @title Http处理�?
 * @description 用于做http请求处理
 * @version 1.0
 * @changeRecord [修改记录]<br />
 */
class HttpHandler {
	
	private static final String TAG = "HttpHandler";
	private static final int mReadTimeOut = 1000 * 60 * 20; // 20�?
	private static final int mConnectTimeOut = 1000 * 60 * 20; // 5�?
	private static final String CHAR_SET = "utf-8";
	private static final int mRetry = 3 ;//默认尝试访问次数
	
	

	public static String get(String url) throws Exception {
		return get(url,null);
	}

	public static String get(String url, Map<String, ? extends Object> params) throws Exception {
		if(url == null || url.trim().length() == 0){
			throw  new Exception(TAG+": get url is null or empty!");
		}
		
		if(params!=null &&  params.size() > 0){
			
			//判断提交的URL格式�?
			//	1：http://api.launcher.com/xxx?abc=1
			//	2：http://api.launcher.com/xxx
			//	3:如果�?http://api.launcher.com/xxx , 则让其变�?http://api.launcher.com/xxx? (�?��边多�?��问号)
			if(!url.contains("?"))
				url += "?";
			
			StringBuilder sbContent = new StringBuilder();
			if(url.charAt(url.length()-1)=='?'){ //�?���?��字符�??
				for(Map.Entry<String, ? extends Object> entry : params.entrySet()){
					if(entry.getKey()!=null)
						sbContent.append(entry.getKey().trim()).append("=").append(entry.getValue()).append("&");	
				}
				
				//去掉�?���?�� "&" 符号
				if(sbContent.charAt(sbContent.length()-1)=='&'){
					sbContent.deleteCharAt(sbContent.length()-1);
				}
			}else{
				for(Map.Entry<String, ? extends Object> entry : params.entrySet()){
					if(entry.getKey()!=null)
						sbContent.append("&").append(entry.getKey().trim()).append("=").append(entry.getValue());	
				}
			}
			
			//添加提交内容
			url += sbContent.toString();
			
		}
		
		return doGet(url);
	}

	public static String post(String url) throws Exception {
		return post(url, null);
	}

	public static String post(String url, Map<String, ? extends Object> params) throws Exception {
		if(url == null || url.trim().length() == 0){
			throw  new Exception(TAG+": post url is null or empty!");
		}
		
		if(params!=null && params.size() > 0){
			StringBuilder sbContent = new StringBuilder();
			for(Map.Entry<String, ? extends Object> entry : params.entrySet()){
				if(entry.getKey()!=null)
					sbContent.append("&").append(entry.getKey().trim()).append("=").append(entry.getValue());
			}
			return tryToPost(url,sbContent.substring(1));
			
		}else{
			return tryToPost(url,null);
		}
		
	}
	
	private static String tryToPost(String url ,String postContent) throws Exception{
		int tryTime = 0;
		Exception ex = null;
		while (tryTime < mRetry ) {
			try {
				return doPost(url,postContent);
			} catch (Exception e) {
				if(e!=null)
					ex = e;
				tryTime ++;
			}
		}
		if(ex != null)
			throw ex;
		else
			throw new Exception("未知网络错误 ");
	}

	
	/**
	 * 做http �?post 请求
	 * @param strUrl
	 * @param postContent
	 * @return
	 * @throws Exception
	 */
	private static String doPost(String strUrl, String postContent) throws Exception {
		
		HttpURLConnection request = null;
		BufferedReader br = null;
		try {
			request = getConnection(strUrl);
			configConnection(request);
			request.setRequestMethod("POST");
			DidiApp.mHttpURLConnection = request;
			DataOutputStream dos = new DataOutputStream(request.getOutputStream());
//			dos.writeUTF(postContent);
//			dos.write(postContent.getBytes("UTF-8"));
//			dos.writeBytes(postContent);
			if (request.getResponseCode() != 200) {
				br = new BufferedReader(new InputStreamReader(request.getErrorStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			}
			
			String line = null;
			StringBuffer responseBuffer = new StringBuffer();
			
			while ((line = br.readLine()) != null) {
				responseBuffer.append(line);
			}
			dos.flush();
			dos.close();
			
			return responseBuffer.toString();
		
		} catch(Exception e){
			e.printStackTrace();
			return "";
		}finally {
			if (request != null) {
				request.disconnect();
				request = null;
			}
			
			if(br!=null){
				br.close();
				br = null;
			}
		}
		
	}

	/**
	 * 做http的get请求
	 * @param strUrl 
	 * @return
	 * @throws Exception
	 */
	private static String doGet(String strUrl) throws Exception {
		
		HttpURLConnection request = null;
		BufferedReader br = null;
		try {
			
			request = getConnection(strUrl);
			configConnection(request);
			
			if (request.getResponseCode() != 200) {
				br = new BufferedReader(new InputStreamReader(
						request.getErrorStream(), CHAR_SET));
			} else {
				br = new BufferedReader(new InputStreamReader(
						request.getInputStream(), CHAR_SET));
			}
			
			StringBuffer responseBuffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				responseBuffer.append(line);
			}
			
			return responseBuffer.toString();
			
		}finally {
			if (request != null) {
				request.disconnect();
				request = null;
			}
			
			if(br!=null){
				br.close();
				br = null;
			}
		}
		
	}
	
	/**
	 * 配置基本的http连接信息
	 * @param connection
	 */
	private static void configConnection(HttpURLConnection connection){
		if(connection == null) return;
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setReadTimeout(mReadTimeOut);
		connection.setConnectTimeout(mConnectTimeOut);
		connection.setUseCaches(false);
		connection.setRequestProperty("sessionId", DidiApp.sessionId);
	}

	/**
	 * 获取 http 请求连接
	 * @param strUrl
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getConnection(String strUrl) throws Exception  {
		if(strUrl== null){
			return null;
		}
		
		if (strUrl.toLowerCase().startsWith("https")){
			try {
				return  getHttpsConnection(strUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}	
			return null;
		}else{
			return getHttpConnection(strUrl);
		}
			
	}

	private static HttpURLConnection getHttpConnection(String urlStr) throws Exception{
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return conn;
	}

	private static HttpsURLConnection getHttpsConnection(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setHostnameVerifier(hnv);
		SSLContext sslContext = SSLContext.getInstance("TLS");
//		SSLContext sslContext = SSLContext.getInstance("SSL");
		if(sslContext!=null){
			TrustManager[] tm = { xtm };
			sslContext.init(null, tm, null);
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			conn.setSSLSocketFactory(ssf);
		}
		
		return conn;
	}

	
	private static X509TrustManager xtm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	
	private static HostnameVerifier hnv = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

}
