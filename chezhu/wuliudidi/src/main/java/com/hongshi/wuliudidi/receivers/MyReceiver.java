package com.hongshi.wuliudidi.receivers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AllAuctionOrderNewActivity;
import com.hongshi.wuliudidi.activity.AuctionDetailsActivity;
import com.hongshi.wuliudidi.activity.DriverMainActivity;
import com.hongshi.wuliudidi.activity.MainActivity;
import com.hongshi.wuliudidi.activity.WinBidActivity;
import com.hongshi.wuliudidi.model.PushParamModel;
import com.hongshi.wuliudidi.utils.LogUtil;

import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

    private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        this.context = context;
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			LogUtil.d("huiyuan", "regitratied id = " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			LogUtil.d("huiyuan", "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            [MyReceiver] 接收到推送下来的通知
//            [MyReceiver] 接收到推送下来的通知的ID: " + notifactionId
			LogUtil.d("huiyuan", "[MyReceiver] 接收到推送下来的自定义通知:" + bundle.getString(JPushInterface.EXTRA_ALERT));
			processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            [MyReceiver] 用户点击打开了通知
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            [MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA)
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//        	"[MyReceiver]" + intent.getAction() +" connected state change to "+connected
        } else {
//        	"[MyReceiver] Unhandled intent - " + intent.getAction()
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			}
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}


	  /**
	   * 处理push消息，通过通知栏跳转到新的货源详情页面
	   * @param context
	   * @param bundle
	     */
	  private void processCustomMessage(Context context, Bundle bundle) {

		  // 利用JPushInterface.EXTRA_MESSAGE 推送消息的获取
//		  String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//		  String defaultTitle = context.getString(R.string.app_name);
		  String notificationTitle = "";
		  String notificationContent = "";
		  String jumpTarget = "";
		  notificationContent = bundle.getString(JPushInterface. EXTRA_ALERT);
		  notificationTitle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);

//		  int notifactionId =  (int) System.currentTimeMillis();
		  int  notifactionId = bundle.getInt(JPushInterface. EXTRA_NOTIFICATION_ID );
		  String all = bundle.getString(JPushInterface.EXTRA_EXTRA);
		  LogUtil.d("huiyuan", " received push content = " + notificationContent + " 通知ID = " + notifactionId);
		  try {
			  JSONObject extrasObject = new JSONObject(all);

			  int size = extrasObject.length();
			  String usefulMessage = extrasObject.optString("" + (size - 1));
			  PushParamModel model = JSON.parseObject(usefulMessage,PushParamModel.class);
			  jumpTarget = model.getJumpTarget();
			  LogUtil.d("huiyuan", " received push all = " + all + " extra = " + usefulMessage
					  + " jumpTarget = " + jumpTarget);
			  JSONObject paramsJson = null;
			  if(jumpTarget.equals(CommonRes.CZ_AUCTION_DETAIL) || jumpTarget.equals(CommonRes.CZ_CONSIGN_DETAIL)){
				  JSONObject usefulMsgJSON = new JSONObject(usefulMessage);
//				  jumpTarget = usefulMsgJSON.optString("jumpTarget");
				  String params = usefulMsgJSON.optString("params");
				  paramsJson = new JSONObject(params);
			  }

			  LogUtil.d("huiyuan", "size =  " + size + " jumpTarget = " + jumpTarget);
			  if(jumpTarget.equals(CommonRes.CZ_ALL_TASK)){
				  Intent intent = new Intent(context,MainActivity.class);
				  intent.putExtra("from","jpush");
				  showNotification(intent,notificationTitle,notificationContent,notifactionId);
			  }else if(jumpTarget.equals(CommonRes.CZ_ALL_CONSIGN)){
				  Intent intent = new Intent(context,AllAuctionOrderNewActivity.class);
				  showNotification(intent, notificationTitle, notificationContent, notifactionId);
			  }else if(jumpTarget.equals(CommonRes.CZ_AUCTION_DETAIL)){
				  Intent intent = new Intent(context,AuctionDetailsActivity.class);
				  Bundle bundle1 = new Bundle();
				  bundle1.putString("auctionId",paramsJson.optString("auctionId"));
				  intent.putExtras(bundle1);
				  showNotification(intent, notificationTitle, notificationContent, notifactionId);
			  }else if(jumpTarget.equals(CommonRes.CZ_CONSIGN_DETAIL)){
				  Intent intent = new Intent(context,WinBidActivity.class);
				  Bundle bundle2 = new Bundle();
				  bundle2.putString("AuctionId", paramsJson.optString("bidItemId"));
				  intent.putExtras(bundle2);
				  showNotification(intent, notificationTitle, notificationContent, notifactionId);
			  }else if(jumpTarget.equals(CommonRes.CZ_INDEX)){
				  Intent intentRefreshGoods = new Intent();
				  intentRefreshGoods.setAction("push_goods_from_server");
				  context.sendBroadcast(intentRefreshGoods);
				  if(DidiApp.isUserAowner){
					  Intent intent = new Intent(context,MainActivity.class);
					  Bundle bundle3 = new Bundle();
					  bundle3.putString("from","xxxx");
					  intent.putExtras(bundle3);
					  showNotification(intent, notificationTitle, notificationContent, notifactionId);
				  }else {
					  Intent intent = new Intent(context,DriverMainActivity.class);
					  Bundle bundle4 = new Bundle();
					  bundle4.putString("from","xxxx");
					  intent.putExtras(bundle4);
					  showNotification(intent, notificationTitle, notificationContent, notifactionId);
				  }
			  }
			  startVibrateAndSound(context);
		  }catch (Exception e){
			  e.printStackTrace();
			  LogUtil.d("huiyuan"," 错误信息：" + e.getMessage());
		  }

	  }

	private void startVibrateAndSound(Context context){
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		if(vibrator != null){
			vibrator.vibrate(2000);
		}

		boolean shouldPlaySound = true;
//		Log.d("huiyuan", "正在响铃");
		// 使用通知路径
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		AudioManager audioService = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			shouldPlaySound = false;
		}
		boolean voice_switch = context.getSharedPreferences("config", Context.MODE_PRIVATE)
				.getBoolean("voice_switch",true);

		MediaPlayer mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		if(mMediaPlayer == null)
			mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer player) {
							player.seekTo(0);
						}
					});
			mMediaPlayer.setDataSource(context, uri);
//			mMediaPlayer.setLooping(true); //循环播放
			mMediaPlayer.prepare();
			if(shouldPlaySound && voice_switch && mMediaPlayer != null)
			mMediaPlayer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void showNotification(Intent intent, String notificationTitle,String notificationContent,int notifactionId){
		// 使用广播或者通知进行内容的显示
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);

		PendingIntent contentIndent = null;
		contentIndent = PendingIntent
				.getActivity(context, (int)(Math.random() * 100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//设置状态栏里面的图标（小图标）
		builder.setContentIntent(contentIndent).setSmallIcon(R.drawable.app_icon)
				//下拉列表里面的图标（大图标） 　　　　　　　
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
				//设置状态栏的显示的信息
				.setTicker(context.getString(R.string.push_des))
				//设置时间发生时间
				.setWhen(System.currentTimeMillis())
				//设置可以清除
				.setAutoCancel(true)
				//设置下拉列表里的标题
				.setContentTitle(notificationTitle)
				//设置上下文内容
				.setContentText(notificationContent);
		Notification notification = builder.getNotification();
		//加notifactionId是为了显示多条Notification
		notificationManager.notify(notifactionId, notification);
	}

}
