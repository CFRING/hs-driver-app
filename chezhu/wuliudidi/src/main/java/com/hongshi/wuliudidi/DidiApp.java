package com.hongshi.wuliudidi;

import android.app.Application;
import android.content.Context;

import com.hongshi.wuliudidi.http.HttpManager;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.thread.ThreadPool;
import com.morgoo.droidplugin.PluginHelper;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalHttp;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class DidiApp extends Application{
	private static ThreadPool 	mThreadPool;
	private static HttpManager	mHttpManager;
	private static FinalHttp 	mFinalHttp;
	
	public static DidiApp context;
	public static List<String> list = new ArrayList<String>();
	public static HttpURLConnection mHttpURLConnection = null;
	public static String  sessionId;
	@Override
	public void onCreate() {
		super.onCreate();
		//插件启动框架初始化必须在super.onCreate方法之后，顺序不能变
		PluginHelper.getInstance().applicationOnCreate(getBaseContext());
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		MobclickAgent.openActivityDurationTrack(false);
		context = this;
		mHttpURLConnection = null;
	}
	public static synchronized ThreadPool getThreadPool() {
        if (mThreadPool == null) {
            mThreadPool = new ThreadPool();
        }
        return mThreadPool;
    }
	public static HttpManager getHttpManager() {
		if(mHttpManager ==null){
			mHttpManager = new HttpManager(context);
		}
		return mHttpManager;
	}
	public static FinalHttp getAfinalHttp() {
		if(mFinalHttp ==null){
			mFinalHttp = new FinalHttp();
		}
		return mFinalHttp;
	}

	@Override
	protected void attachBaseContext(Context base) {
		PluginHelper.getInstance().applicationAttachBaseContext(base);
		super.attachBaseContext(base);
	}

	public static  boolean isDriverHasCommonLines = false;
	//用户是否是车主
	public static boolean isUserAowner = true;
	public static void setUserOwnerRole(boolean isUserAowner1){
		isUserAowner = isUserAowner1;
		if(!isUserAowner){
			GloableParams.HOST = GloableParams.DRIVER_HOST;
		}else {
			GloableParams.HOST = GloableParams.CHE_ZHU_HOST;
		}
	}
}