package com.hongshi.wuliudidi.http;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.ErrorDialog;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.thread.Future;
import com.hongshi.wuliudidi.thread.FutureListener;
import com.hongshi.wuliudidi.thread.ThreadPool;
import com.hongshi.wuliudidi.thread.ThreadPool.JobContext;
import com.hongshi.wuliudidi.utils.ErrorCodeUtil;
import com.hongshi.wuliudidi.utils.LogUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @title http管理器
 * @description 用户对http数据请求等操作
 * @version 1.0
 * @changeRecord [修改记录]<br />
 * 
 */
public class HttpManager {
	private SharedPreferences sp;
	private ErrorDialog mErrorDialog = null;
//	private SSLSocketFactory sslSocketFactory = null;
	// 不带header的请求
	public void post(final Context mContext, String url, AjaxParams params,
			final AfinalHttpCallBack mCallBack) {
		FinalHttp mFinalHttp = new FinalHttp();
		mFinalHttp.configCharset("UTF-8");
		mFinalHttp.configTimeout(10000);
		try {
			mFinalHttp.configSSLSocketFactory(setCertificates(mContext.getAssets().open("HSWLROOTCAforInternalTest.crt"), mContext.getAssets().open("HSWLROOTCA.crt")));
		}catch(IOException e){
			e.printStackTrace();
		}
		LogUtil.i("http", "post request:   " + url + params);
		mFinalHttp.post(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				LogUtil.i("http", "post receive:   " + t);
				try {
					JSONObject jsonObject = new JSONObject(t);
					if (jsonObject != null) {
						try{
							String sessionId = jsonObject.optString("newSessionId");
							SharedPreferences sp = mContext.getSharedPreferences(
									"config", Activity.MODE_PRIVATE);
							SharedPreferences.Editor edit = sp.edit();
							if(sessionId != null && !sessionId.equals("")){
								edit.putString("session_id", sessionId);
								edit.commit();
							}
							LogUtil.i("http", "newSessionId====" + sessionId);
						}catch (Exception e){
							LogUtil.i("http", "no session id");
						}finally {
							boolean isSuccess = jsonObject.getBoolean("success");
							if (isSuccess) {
								mCallBack.data(t);
							}else{
								String errMsg = jsonObject.getString("errMsg");
								Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
								try {
									((ChildAfinalHttpCallBack) mCallBack).onFailure("", errMsg, false);
								} catch (Exception e) {

								}
							}
						}
					}else {
						try {
							((ChildAfinalHttpCallBack) mCallBack).onFailure("", "", false);
						} catch (Exception e) {

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					try {
						((ChildAfinalHttpCallBack) mCallBack).onFailure("", "", false);
					} catch (Exception excep) {

					}
				}
			}

			// 加载失败
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(mContext, strMsg, Toast.LENGTH_SHORT).show();
				try {
					((ChildAfinalHttpCallBack) mCallBack).onFailure("", strMsg, false);
				} catch (Exception e) {

				}
			}
		}); 
	}

	// 带header的请求
	public void sessionPost(final Context mContext, String url,
		AjaxParams params, final AfinalHttpCallBack mCallBack) {
		FinalHttp mFinalHttp = new FinalHttp();
		sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
		String sessionID = sp.getString("session_id", "");
		mFinalHttp.configCharset("UTF-8");
		mFinalHttp.configTimeout(10000);
		try {
			mFinalHttp.configSSLSocketFactory(setCertificates(mContext.getAssets().open("HSWLROOTCAforInternalTest.crt"), mContext.getAssets().open("HSWLROOTCA.crt")));//设置https请求，暂时为单向验证
		}catch(IOException e){
			e.printStackTrace();
		}
		if(sessionID != null && !sessionID.equals("")){
			mFinalHttp.addHeader("sessionId", sessionID);
		}
		LogUtil.i("http", "sessionPost request:   " + url + params);
		mFinalHttp.post(url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				LogUtil.i("http", "sessionPost receive:   " + t);
				try {
					JSONObject jsonObject = new JSONObject(t);
					if (jsonObject != null) {
						try{
							String sessionId = jsonObject.getString("newSessionId");
							SharedPreferences sp = mContext.getSharedPreferences(
									"config", Activity.MODE_PRIVATE);
							SharedPreferences.Editor edit = sp.edit();
							if(sessionId != null && !sessionId.equals("")){
								edit.putString("session_id", sessionId);
								edit.commit();
							}
							LogUtil.i("http", "newSessionId====" + sessionId);
						}catch (Exception e){
							LogUtil.i("http", "no session id");
						}finally {
							boolean isSuccess = jsonObject.getBoolean("success");
							if (isSuccess) {
								mCallBack.data(t);
							}else{
								String errMsg = jsonObject.getString("errMsg");
								String errCode = jsonObject.getString("errCode");
								boolean errSerious = jsonObject.getBoolean("errSerious");
								if(errSerious){
									//要弹出错误框
									if(mErrorDialog == null){
										mErrorDialog = new ErrorDialog(mContext, R.style.data_filling_dialog,errMsg);
									}
									if(!mErrorDialog.isShowing()){
										mErrorDialog.show();
									}
								}else{
									//特殊处理错误框
									ErrorCodeUtil.errorCode(errCode, mContext, errMsg);
								}
								try {
									((ChildAfinalHttpCallBack) mCallBack).onFailure(errCode, errMsg, errSerious);
								} catch (Exception e) {
								}
							}
						}
					}else{
						try {
							((ChildAfinalHttpCallBack) mCallBack).onFailure("", "", false);
						} catch (Exception excep) {

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						((ChildAfinalHttpCallBack) mCallBack).onFailure("", "", false);
					} catch (Exception excep) {

					}
				}
			}

			// 加载失败
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
//				Toast.makeText(mContext, "失败请求"+strMsg, Toast.LENGTH_SHORT).show();
				try {
					((ChildAfinalHttpCallBack) mCallBack).onFailure("", strMsg, false);
				} catch (Exception e) {
				}
			}
		});
	}

	public HttpManager(DidiApp app) {
		// this.mApp = app;
	}

	public String get(String url) throws Exception {
		return HttpHandler.get(url);
	}

	public String get(String url, Map<String, Object> params) throws Exception {
		return HttpHandler.get(url, params);
	}

	public void getAsync(String url, HttpCallBack listener) {
		DidiApp.getThreadPool().submit(new httpLoader(url, null), listener);
	}

	public void getAsync(String url, Map<String, ? extends Object> params,
			HttpCallBack listener) {
		DidiApp.getThreadPool().submit(new httpLoader(url, params), listener);
	}

	public String post(String url) throws Exception {
		return HttpHandler.post(url);
	}

	public String post(String url, Map<String, ? extends Object> params)
			throws Exception {
		return HttpHandler.post(url, params);
	}

	public void postAsync(String url, HttpCallBack listener) {
		DidiApp.getThreadPool().submit(new httpLoader(url, null), listener);
	}

	public void postAsync(String url, Map<String, Object> params,
			HttpCallBack listener) {
		DidiApp.getThreadPool().submit(new httpLoader(url, params), listener);
	}

	public class httpLoader implements ThreadPool.Job<HttpContent> {

		private final String mUrl;
		private final Map<String, ? extends Object> mParams;

		public httpLoader(String strUrl, Map<String, ? extends Object> params) {
			this.mUrl = strUrl;
			this.mParams = params;

		}

		@Override
		public HttpContent run(JobContext jc) {
			HttpContent content = new HttpContent();
			content.setUrl(mUrl);
			content.setParams(mParams);
			try {
				String str = HttpHandler.post(mUrl, mParams);
				content.setContent(str.getBytes());
			} catch (Exception e) {
				content.setException(e);
			}
			return content;
		}
	}

	public static abstract class HttpCallBack implements
			FutureListener<HttpContent> {
		@Override
		public void onFutureDone(Future<HttpContent> future) {
			if (future != null && future.get() != null) {
				if (future.get().getException() != null) {
					httpError(future.get().getException());
				} else {
					httpDone(future.get().getContentAsString());
				}
			} else {
				httpError(new Exception("未知网络加载错误"));
			}
		}

		public abstract void httpDone(String httpContent);

		public abstract void httpError(Exception exception);
	}

	/**
	 * for https-way authentication
	 *
	 * @param certificates
	 */
	public  SSLSocketFactory setCertificates(InputStream...certificates) {
		SSLSocketFactory sslSocketFactory = HttpsUtilsForFinalHttp.getSslSocketFactory(certificates, null, null);
		return sslSocketFactory;
	}

	/**
	 * for https mutual authentication
	 *
	 * @param certificates
	 * @param bksFile
	 * @param password
	 */
	public SSLSocketFactory setCertificates(InputStream[] certificates, InputStream bksFile, String password){
		SSLSocketFactory sslSocketFactory = HttpsUtilsForFinalHttp.getSslSocketFactory(certificates, bksFile, password);
		return sslSocketFactory;
	}

}
