package com.hongshi.wuliudidi.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.MainFragmentPagerAdapter;
import com.hongshi.wuliudidi.camera.ActivityCapture;
import com.hongshi.wuliudidi.camera.PhotoPreviewActivity;
import com.hongshi.wuliudidi.fragment.GoodsFragmentForDriver;
import com.hongshi.wuliudidi.fragment.HomeFragmentForDriver;
import com.hongshi.wuliudidi.fragment.MessageFragment;
import com.hongshi.wuliudidi.fragment.MyWalletFragment;
import com.hongshi.wuliudidi.fragment.TaskModifyFragment;
import com.hongshi.wuliudidi.model.InviteModel;
import com.hongshi.wuliudidi.model.RefreshMessage;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.photo.GetPhotoUtil;
import com.hongshi.wuliudidi.plugin.Util.NetworkUtils;
import com.hongshi.wuliudidi.plugin.Util.PluginUtil;
import com.hongshi.wuliudidi.plugin.bean.PluginParams;
import com.hongshi.wuliudidi.share.BannerModelList;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.JpushUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huiyuan
 */

public class MainActivity extends FragmentActivity {
	private ImageView mGoodsImage, mTaskImage, mMyWalletImage,mMessageImage,mHomeImage;
	private LinearLayout mGoodsLayout, mTaskLayout, mMyWalletLayout,mMessageLayout,bottom_container;
	private TextView mGoodsText, mTaskText, mMyWalleteText,mMessageText,mHomeText;
	public static ViewPager mPager;
//	public static volatile boolean isShowEvalution = false;
	private ArrayList<Fragment> mFragmentList;
	private long mkeyTime;
	private String message_url = GloableParams.COMET_HOST + "comet.do?";
	private boolean isComet = true;
	private RelativeLayout mTaskNewsLayout,mMessageNewsLayout;
	private TextView mTaskNews,mMessageNews;
	private SharedPreferences sharedPreferences;
	private String sessionID;
	private int auction_message = 0;
	private int friendShip_message =0;
	private int system_message = 0;
	private int transit_message = 0;
	//任务总数
	private int task_message = 0;
	//消息总数
	private int message = 0;
	private RefreshMessage mList;
//	private HttpURLConnection httpURLConnection;
	
	//监控线程的广播
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {  
 	    @Override  
 	     public void onReceive(Context context, Intent intent) {  
 	         String action = intent.getAction();  
			if (action.equals(CommonRes.CometStop)) {
				mTaskNewsLayout.setVisibility(View.GONE);
				task_message = 0;
				mMessageNewsLayout.setVisibility(View.GONE);
				DidiApp.sessionId = "";
				message = 0;
				isComet = false;
				if(DidiApp.mHttpURLConnection!=null){
					try {
						DidiApp.mHttpURLConnection.disconnect();
						DidiApp.mHttpURLConnection.getInputStream().close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else if(action.equals(CommonRes.CometStart)){
				sessionID = sharedPreferences.getString("session_id", "");
				DidiApp.sessionId = sessionID;
				isComet = true;
				if(Util.isLogin(MainActivity.this)){
					startThread();
				}
			}else if(action.equals(CommonRes.ClickSystemMessage)){
				int minus_system_number = intent.getIntExtra("system_number", 0);
				message = message - minus_system_number;
				if(message!=0&& message >0){
					mMessageNews.setText(""+message);
				}else{
					mMessageNewsLayout.setVisibility(View.GONE);
					message = 0;
				}
			}else if(action.equals(CommonRes.ClickAuctionMessage)){
				//消息fragment点击了竞价消息
				int minus_auction_number = intent.getIntExtra("auction_number", 0);
				message = message - minus_auction_number;
				if(message!=0&& message >0){
					mMessageNews.setText(""+message);
				}else{
					mMessageNewsLayout.setVisibility(View.GONE);
					message = 0;
				}
			}else if(action.equals(CommonRes.ClickInviteMessage)){
				//消息fragment点击了车队邀请消息
				int minus_invite_number = intent.getIntExtra("invite_number", 0);
				message = message - minus_invite_number;
				if(message!=0 && message >0){
					mMessageNews.setText(""+message);
				}else{
					mMessageNewsLayout.setVisibility(View.GONE);
					message = 0;
				}
			}else if(action.equals(CommonRes.ClickTransitMessage)){
				int transit_number = intent.getIntExtra("transit_number", 0);
				task_message = task_message - transit_number;
				if(task_message>0){
					mTaskNews.setText(""+task_message);
				}else{
					mTaskNewsLayout.setVisibility(View.GONE);
					task_message = 0;
				}
			}else if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
				if(NetworkUtils.isNetworkConnectedOnHighSpeed(MainActivity.this)){
					PluginUtil.checkPluginUpdate(getApplicationContext(), PluginParams.CHENGYUNTONG, PluginParams.APPTYPE);
				}
				if(Util.isLogin(getApplicationContext())){
					BannerModelList.getAdvertList(getApplicationContext());
				}
			}else if("from_task_fragment".equals(action)){
				setText(0);
			}else if(CommonRes.CZ_INDEX.equals(action)){
				// 设置当前显示标签页为第一页
				mPager.setCurrentItem(2);
				setText(2);
				bottom_container.setVisibility(View.GONE);
			}
 	      }  
 	  };
 	  
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
						
			switch (msg.what) {
			case CommonRes.NEWMESSAGE:
				mMessageNewsLayout.setVisibility(View.VISIBLE);
				mMessageNews.setText(""+message);
				Intent news_intent = new Intent();
				news_intent.setAction(CommonRes.NewMessage);
				sendBroadcast(news_intent);
				break;
			case CommonRes.TASKMESSAGE:
				mTaskNewsLayout.setVisibility(View.VISIBLE);
				mTaskNews.setText(""+task_message);
				break;
			case CommonRes.SYSTEMMESSAGE:
				Intent system_intent = new Intent();
				system_intent.putExtra("system_number", system_message);
				system_intent.setAction(CommonRes.MessageFromSystem);
				system_intent.putExtra("system", mList);
				sendBroadcast(system_intent);
				break;
			case CommonRes.AUCTIONMESSAGE:
				Intent auction_intent = new Intent();
				auction_intent.putExtra("auction_number", auction_message);
				auction_intent.setAction(CommonRes.MessageFromAuction);
				auction_intent.putExtra("auction", mList);
				sendBroadcast(auction_intent);
				break;
			case CommonRes.INVITEMESSAGE:
				Intent invite_intent = new Intent();
				invite_intent.putExtra("friend_number", friendShip_message);
				invite_intent.putExtra("invite", mList);
				invite_intent.setAction(CommonRes.MessageFromInvite);
				sendBroadcast(invite_intent);
				break;
			case CommonRes.TRANSITMESSAGE:
				Intent transit_intent = new Intent();
				transit_intent.putExtra("transit_number", transit_message);
				transit_intent.setAction(CommonRes.MessageFromTransit);
				sendBroadcast(transit_intent);
				break;
			case CommonRes.STOPTHREAD:
				isComet = false;
				if(DidiApp.mHttpURLConnection!=null ){
					try {
						DidiApp.mHttpURLConnection.getInputStream().close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
		sessionID = sharedPreferences.getString("session_id", "");
		DidiApp.sessionId = sessionID;
		initView();
		//初始化Jpush
		JpushUtil mJpushUtil = new JpushUtil();
		mJpushUtil.initJpush(getApplicationContext(), "", CommonRes.UserId);
		if(Util.isLogin(MainActivity.this)){
			startThread();
		}

		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.CometStop);
		intentFilter.addAction(CommonRes.CometStart);
		intentFilter.addAction(CommonRes.ClickSystemMessage);
		intentFilter.addAction(CommonRes.ClickAuctionMessage);
		intentFilter.addAction(CommonRes.ClickInviteMessage);
		intentFilter.addAction(CommonRes.ClickTransitMessage);
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		intentFilter.addAction("from_task_fragment");
		intentFilter.addAction(CommonRes.RefreshData);
		intentFilter.addAction(CommonRes.CZ_INDEX);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		if(NetworkUtils.isNetworkConnectedOnHighSpeed(MainActivity.this)){
			PluginUtil.checkPluginUpdate(getApplicationContext(), PluginParams.CHENGYUNTONG, PluginParams.APPTYPE);
		}
		ActivityManager.getInstance().addActivity(this);
	}

	private void startThread(){
		Thread thread  = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isComet) {
					try {
						if(!Util.isLogin(MainActivity.this)){
							if(DidiApp.mHttpURLConnection!=null){
								try {
									DidiApp.mHttpURLConnection.getInputStream().close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							break;
						}
						String string = DidiApp.getHttpManager().post(message_url);
						if(null != string){
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(string);
								boolean isSuccess = jsonObject.getBoolean("success");
								if(isSuccess){
									String body = jsonObject.getString("body");
									mList = JSON.parseObject(body,RefreshMessage.class);
									updateMessage();
								}else{
									String errCode = jsonObject.getString("errCode");
									if(errCode != null && errCode.equals("080003")){
										//未登录
										Editor edit = sharedPreferences.edit();
//										edit.putString("cellphone", "");
										edit.putString("userId", "");
										edit.putString("session_id", "");
										edit.commit();
										mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
									}
								}
							} catch (JSONException e) {
								mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
								e.printStackTrace();
							}
						}else{
							mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
						}
					} catch (Exception e) {
						mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
						e.printStackTrace();
					}
				}
			}
		});
		if(isComet){
			thread.start();	
		}else{
			if(thread.isInterrupted()){
				thread.interrupt();
			}
		}
	}
	private void updateMessage(){
		List<InviteModel> auctionList = mList.getREFRESH_MSG_AUCTION();
		List<InviteModel> friendShipList = mList.getREFRESH_MSG_FRIENDSHIP();
		List<InviteModel> systemList = mList.getREFRESH_MSG_SYSTEM();
		List<InviteModel> transitList = mList.getREFRESH_TASK_TRANSIT();
		if (auctionList != null) {
			auction_message = auctionList.size();
			if(message != 0){
				message = message + auction_message;
			}else{
				message = auction_message;
			}
			mHandler.sendEmptyMessage(CommonRes.NEWMESSAGE);
			mHandler.sendEmptyMessage(CommonRes.AUCTIONMESSAGE);
			
		} else if (friendShipList != null) {
			friendShip_message = friendShipList.size();
			if(message != 0){
				message = message + friendShip_message;
			}else{
				message = friendShip_message;
			}
			mHandler.sendEmptyMessage(CommonRes.NEWMESSAGE);
			mHandler.sendEmptyMessage(CommonRes.INVITEMESSAGE);
		} else if (systemList != null) {
			system_message = systemList.size();
			if(message != 0){
				message = message + system_message;
			}else{
				message = system_message;
			}
			mHandler.sendEmptyMessage(CommonRes.NEWMESSAGE);
			mHandler.sendEmptyMessage(CommonRes.SYSTEMMESSAGE);
		} else if (transitList != null) {
			transit_message = transitList.size();
			if(task_message != 0){
				task_message = task_message + transit_message;
			}else{
				task_message = transit_message;
			}
			//添加消息到
			for(int i=0;i<transit_message;i++){
				CommonRes.taskIdList.add(transitList.get(i).getParams().get(0));
			}
			mHandler.sendEmptyMessage(CommonRes.TASKMESSAGE);//更新主界面的消息
			mHandler.sendEmptyMessage(CommonRes.TRANSITMESSAGE);//更新子界面的消息
		}
	}
	private void initView() {
		initTextView();
	}
	private void initTextView() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		mGoodsImage = (ImageView) findViewById(R.id.goods_image);
		mTaskImage = (ImageView) findViewById(R.id.task_image);
		mMessageImage = (ImageView) findViewById(R.id.message_image);
		mMyWalletImage = (ImageView) findViewById(R.id.my_wallet_image);
		mHomeImage = (ImageView) findViewById(R.id.home_image);

		mGoodsLayout = (LinearLayout) findViewById(R.id.goods_layout);
		mTaskLayout = (LinearLayout) findViewById(R.id.task_layout);
		mMessageLayout = (LinearLayout) findViewById(R.id.message_layout);
		mMyWalletLayout = (LinearLayout) findViewById(R.id.my_wallet_layout);
		bottom_container = (LinearLayout) findViewById(R.id.bottom_container);

		mGoodsText = (TextView) findViewById(R.id.goods_text);
		mTaskText = (TextView) findViewById(R.id.task_text);
		mMessageText = (TextView) findViewById(R.id.message_text);
		mMyWalleteText = (TextView) findViewById(R.id.my_wallet_text);
		mHomeText = (TextView) findViewById(R.id.home_text);
		//显示消息
		mTaskNewsLayout = (RelativeLayout) findViewById(R.id.task_news_layout);
		mMessageNewsLayout = (RelativeLayout) findViewById(R.id.message_news_layout);
		mTaskNews = (TextView) findViewById(R.id.task_news);
		mMessageNews = (TextView) findViewById(R.id.message_news);
		
		mFragmentList = new ArrayList<>();
		Fragment mGoodsFragment = new GoodsFragmentForDriver();
		Fragment mTaskFragment = new TaskModifyFragment();
		Fragment mMessageFragment = new MessageFragment();
		Fragment mMyFragment = new MyWalletFragment();
		Fragment mHomeFragment = new HomeFragmentForDriver();

		mFragmentList.add(mTaskFragment);
		mFragmentList.add(mGoodsFragment);
		mFragmentList.add(mHomeFragment);
		mFragmentList.add(mMessageFragment);
		mFragmentList.add(mMyFragment);

		mPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));

		String push = null;
		Bundle bundle;
		bundle = getIntent().getExtras();
		if (bundle != null) {
			push = bundle.getString("from");
		}
		if(push != null && push.equals("jpush")){
			mPager.setCurrentItem(0);//任务页面
			setText(0);
		}else if("from_driver".equals(push)){
			mPager.setCurrentItem(2);
			setText(2);
		}else if("commit_receipt".equals(push)){
//			isShowEvalution = false;
			mPager.setCurrentItem(0);//任务页面
			setText(0);
		}else if("evaluation".equals(push)){
//			Log.i("http","isShowEvaluation in MainActivity = " + isShowEvalution);
//			isShowEvalution = true;
			mPager.setCurrentItem(0);//任务页面
			setText(0);
		}else {
			mPager.setCurrentItem(2);// 设置当前显示标签页为第一页
			setText(2);
			bottom_container.setVisibility(View.GONE);
		}

		mPager.setOffscreenPageLimit(4);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器

		mGoodsLayout.setOnClickListener(new TextListener(1));
		mTaskLayout.setOnClickListener(new TextListener(0));
		mHomeImage.setOnClickListener(new TextListener(2));
		mMessageLayout.setOnClickListener(new TextListener(3));
		mMyWalletLayout.setOnClickListener(new TextListener(4));
	}
	public void setText(int pos) {
		switch (pos) {
		case 1:
			mGoodsImage.setImageResource(R.drawable.goods_press);
			mGoodsText.setTextColor(getResources().getColor(R.color.home_text_press));
			
			mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));
			mMyWalleteText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mHomeImage.setImageResource(R.drawable.home_none);
			mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
			mMessageImage.setImageResource(R.drawable.news_none);
			
			mTaskImage.setImageResource(R.drawable.task_none);
			mMyWalletImage.setImageResource(R.drawable.my_wallet_img);
			break;
		case 0:
			mGoodsImage.setImageResource(R.drawable.goods_none);
			mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mTaskText.setTextColor(getResources().getColor(R.color.home_text_press));
			mMyWalleteText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mHomeImage.setImageResource(R.drawable.home_none);
			mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
			mMessageImage.setImageResource(R.drawable.news_none);
			
			mTaskImage.setImageResource(R.drawable.task_press);
			mMyWalletImage.setImageResource(R.drawable.my_wallet_img);
			break;
		case 2:
			mGoodsImage.setImageResource(R.drawable.goods_none);
			mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mHomeImage.setImageResource(R.drawable.home_press);
			mHomeText.setTextColor(getResources().getColor(R.color.home_text_press));
			
			mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));
			mMyWalleteText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
			mMessageImage.setImageResource(R.drawable.news_none);
			
			mTaskImage.setImageResource(R.drawable.task_none);
			mMyWalletImage.setImageResource(R.drawable.my_wallet_img);
			break;
		case 3:
			mGoodsImage.setImageResource(R.drawable.goods_none);
			mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mTaskImage.setImageResource(R.drawable.task_none);
			mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mHomeImage.setImageResource(R.drawable.home_none);
			mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mMessageText.setTextColor(getResources().getColor(R.color.home_text_press));
			mMessageImage.setImageResource(R.drawable.news_press);
			
			mMyWalletImage.setImageResource(R.drawable.my_wallet_img);
			mMyWalleteText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			break;
		case 4:
			mGoodsImage.setImageResource(R.drawable.goods_none);
			mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));
			mMyWalleteText.setTextColor(getResources().getColor(R.color.home_text_press));
			
			mHomeImage.setImageResource(R.drawable.home_none);
			mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));
			
			mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
			mMessageImage.setImageResource(R.drawable.news_none);
			
			mTaskImage.setImageResource(R.drawable.task_none);
			mMyWalletImage.setImageResource(R.drawable.my_wallet_selected_img);
			break;

		default:
			break;
		}
	}
	public class TextListener implements View.OnClickListener {
		private int index = 0;

		public TextListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			if(index == 0){//如果点了任务栏，任务栏上的消息红点消失
				mTaskNewsLayout.setVisibility(View.GONE);
				task_message = 0;
			}

			if (mPager != null){
				mPager.setCurrentItem(index);
				if(index == 3){
					Intent intent = new Intent();
					intent.setAction(CommonRes.GetALlMessage);
					sendBroadcast(intent);
				}else if(index == 4){
//					Intent intent = new Intent();
//					intent.setAction(CommonRes.RefreshMyWalletData);
//					sendBroadcast(intent);
				}else if(index == 0){
//					Intent intent = new Intent();
//					intent.setAction(CommonRes.MessageFromTransit);
//					sendBroadcast(intent);
				}else if(index == 1){
					Intent intent = new Intent();
					intent.setAction(CommonRes.RefreshGoodsList);
					sendBroadcast(intent);
				}
				setText(index);	
			}
		}
	}
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageSelected(int arg0) {
			setText(arg0);
			if(arg0 == 2){
				Intent intent = new Intent();
				intent.setAction(CommonRes.GetRecGoodsSourceAndFinishOrder);
				sendBroadcast(intent);
				bottom_container.setVisibility(View.GONE);
			}else {bottom_container.setVisibility(View.VISIBLE);}
			if(arg0 == 0){
//				Log.i("http","taskFragment init from mainActivity!!!");
				Intent intent = new Intent();
				intent.setAction(CommonRes.MessageFromTransit);
				sendBroadcast(intent);
			}else if(arg0 == 4){
				Intent intent = new Intent();
				intent.setAction("com.refresh.my_wallet_items");
				sendBroadcast(intent);
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode ==UploadUtil.PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				String path = ImageUtil.getImageAbsolutePath(MainActivity.this, uri);
				Intent intent = new Intent();
				intent.putExtra("path", path);
				intent.putExtra("isCamera", false);
				intent.setAction(CommonRes.UploadPhoto);
				sendBroadcast(intent);
			}
		}else if (requestCode == UploadUtil.PHOTO_REQUEST_CAMERA &&  resultCode == RESULT_OK) {
			Intent intent = new Intent();
			intent.putExtra("isCamera", true);
			intent.setAction(CommonRes.UploadPhoto);
			sendBroadcast(intent);
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CAMERA && resultCode == RESULT_OK) {
			if (data != null) {
				// 得到图片的全路径
				String path = data.getStringExtra(ActivityCapture.kPhotoPath);
				int amount = data.getIntExtra("amount",0);
				PhotoPreviewActivity.open(this, path, CommonRes.TYPE_UPLOAD_HUIDAN,amount);
			}
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_GALLERY &&  resultCode == RESULT_OK) {
			if (UploadUtil.hasSdcard()) {
				Uri uri = data.getData();
				try {
					ContentResolver cr = getContentResolver();
					InputStream inputStream = cr.openInputStream(Uri.parse(uri.toString()));
					File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					byte byteBuffer[]=new byte[4096];
					int amount;
					while ((amount = inputStream.read(byteBuffer)) != -1) {
						fileOutputStream.write(byteBuffer, 0, amount); }
					inputStream.close();
					Intent intent = new Intent();
					intent.putExtra("tag", CommonRes.TYPE_UPLOAD_HUIDAN);
					intent.putExtra("pic", Environment.getExternalStorageDirectory() + "/pic.jpg");
					intent.setAction(CommonRes.ACTIONPHOTOPATH);
					sendBroadcast(intent);
				} catch (Exception e) {
					Log.e("Exception", e.getMessage(),e);
				}
//				GetPhotoUtil.crop(MainActivity.this, uri,CommonRes.tempFile);
			}
		}else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CUT && resultCode == RESULT_OK){
			String path = ImageUtil.getImageAbsolutePath(MainActivity.this, Uri.fromFile(CommonRes.tempFile));
			Intent intent = new Intent();
			intent.putExtra("imagePath", path);
			intent.putExtra("tag", CommonRes.TYPE_UPLOAD_HUIDAN);
			int amount = data.getIntExtra("amount",0);
			intent.putExtra("amount",amount);
			intent.setAction(CommonRes.ACTIONPHOTOPATH);
			sendBroadcast(intent);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mkeyTime) > 2000) {
				mkeyTime = System.currentTimeMillis();
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
			} else {
				if(DidiApp.list.size()>0){
					String json = JSON.toJSONString(DidiApp.list);
					Editor edit = sharedPreferences.edit();
					edit.putString("addressInfo", json);
					edit.commit();
				}
				ActivityManager.getInstance().exit();
				finish();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}

	@Override
	protected void onPause() {
		super.onPause();
		isMainActivityOnResume = false;
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isMainActivityOnResume = true;
		MobclickAgent.onResume(this);
//		if(false){
//			sharedPreferences.edit().putBoolean("start_time",true);
//			Intent intent = new Intent(MainActivity.this,AdvertisementActivity.class);
//			intent.putExtra("url","www.baidu.com");
//			intent.putExtra("title","xxx活动详情");
//			startActivity(intent);
//		}
	}

	public static boolean isMainActivityOnResume = false;
	public boolean isMainActivityOnResume(){
		return  isMainActivityOnResume;
	}
}
