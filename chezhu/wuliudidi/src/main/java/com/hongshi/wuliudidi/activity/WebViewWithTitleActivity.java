package com.hongshi.wuliudidi.activity;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

/**
 * @author huiyuan
 */
public class WebViewWithTitleActivity extends Activity {
	
	private DiDiTitleView mTitleView;
	private WebView mWebView;
	private String mLoadUrl,mTitle;
	
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_with_title_activity);
		
		Bundle mBundle = getIntent().getExtras();
		mLoadUrl = mBundle.getString("url");
		mTitle = mBundle.getString("title");
		mTitleView = (DiDiTitleView) findViewById(R.id.about_us_title);
		mWebView = (WebView) findViewById(R.id.intruction_webview);
		
		mTitleView.setTitle(mTitle);
		mTitleView.setBack(this);
		if("汽车商城".equals(mTitle)){
//			mTitleView.setVisibility(View.GONE);
			mTitleView.hideBack();
			mTitleView.getRightImage().setImageResource(R.drawable.bid_cancle_icon);
			mTitleView.getRightImage().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient());
		if(getApplicationContext().getCacheDir() != null
				&& getApplicationContext().getCacheDir().getAbsolutePath() != null){
			String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			mWebView.getSettings().setDomStorageEnabled(true);
			mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
			mWebView.getSettings().setAppCachePath(appCachePath);
			mWebView.getSettings().setAllowFileAccess(true);
			mWebView.getSettings().setAppCacheEnabled(true);
		}
		mWebView.loadUrl(mLoadUrl);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(mTitle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(mTitle);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mWebView != null){
			mWebView.removeAllViews();
			mWebView = null;
		}
	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()){
			if(mWebView.getUrl().equals(mLoadUrl)){
				super.onBackPressed();
			}else{
				mWebView.goBack();
			}
		}else{
			super.onBackPressed();
		}
	}

//	private void clearWebViewCache(){
//
//		//清理Webview缓存数据库
//		try {
//			deleteDatabase("webview.db");
//			deleteDatabase("webviewCache.db");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		//WebView 缓存文件
//		File appCacheDir = new File(appCachePath);
////		Log.e(TAG, "appCacheDir path="+appCacheDir.getAbsolutePath());
//
//		File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"/webviewCache");
//
//		//删除webview 缓存目录
//		if(webviewCacheDir.exists()){
//			deleteFile(webviewCacheDir);
//		}
//		//删除webview 缓存 缓存目录
//		if(appCacheDir.exists()){
//			deleteFile(appCacheDir);
//		}
//	}
//
//	private void deleteFile(File file) {
//
//		if (file.exists()) {
//			if (file.isFile()) {
//				file.delete();
//			} else if (file.isDirectory()) {
//				File files[] = file.listFiles();
//				for (int i = 0; i < files.length; i++) {
//					deleteFile(files[i]);
//				}
//			}
//			file.delete();
//		}
//	}
}















