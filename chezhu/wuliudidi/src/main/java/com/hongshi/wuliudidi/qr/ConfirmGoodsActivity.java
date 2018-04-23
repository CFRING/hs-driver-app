package com.hongshi.wuliudidi.qr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Vector;

import net.tsz.afinal.http.AjaxParams;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hongshi.wuliudidi.AES;
import com.hongshi.wuliudidi.Base64Decoder;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.PaymentDetailActivity;
import com.hongshi.wuliudidi.activity.TransitDetailActivity;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.ConsignDetailModel;
import com.hongshi.wuliudidi.model.QrResultModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.qr.zxing.camera.CameraManager;
import com.hongshi.wuliudidi.qr.zxing.decoding.ActivityHandler;
import com.hongshi.wuliudidi.qr.zxing.decoding.InactivityTimer;
import com.hongshi.wuliudidi.qr.zxing.view.ViewfinderView;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.MD5Util;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class ConfirmGoodsActivity extends Activity implements Callback {
	private String qr_url = GloableParams.HOST + "carrier/transit/task/signup.do?";
	private ActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private DiDiTitleView mTitle;
	private String taskId;
	private String license;
	private Button mCall;
	private RelativeLayout mCallLayout;
	//1:上传单据 2：别的情况
	private int type = 1;
	private int finalAmount = 0;
	private String fromWhere = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.confirm_goods_activity);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mTitle = (DiDiTitleView) findViewById(R.id.qr_title);
		mTitle.setBack(this);
		
		mCall = (Button) findViewById(R.id.call);
		mCallLayout = (RelativeLayout) findViewById(R.id.buttom_layout);

		fromWhere = intent.getStringExtra("from");

		if("".equals(fromWhere)){
			this.finalAmount = intent.getIntExtra("amount",0);
			try {
				type = intent.getExtras().getInt("type", 1);
			} catch (Exception e) {
				type = 1;
			}

			if(type == 1){
				taskId = intent.getExtras().getString("taskId");
				license = intent.getExtras().getString("license");
				mTitle.setTitle("扫码确认");
				viewfinderView.setScanHint(getResources().getString(R.string.scan_text));
			}else if(type == 2){
				mCallLayout.setVisibility(View.GONE);
				mTitle.setTitle("扫描二维码");
			}

			mCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Util.call(ConfirmGoodsActivity.this, getResources().getString(R.string.contact_number));
					finish();
				}
			});
		}else {
			mTitle.setTitle("扫码二维码");
			mCallLayout.setVisibility(View.GONE);
		}

		hasSurface = false;
		CameraManager.init(getApplication());
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	//扫描的结果
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		AES aes = new AES();

		if("my_wallet".equals(fromWhere) || "oil_activity".equals(fromWhere)
				|| "tyre_activity".equals(fromWhere) ||
				"cash_activity".equals(fromWhere) ||
				"consume_account_activity".equals(fromWhere) ||
				"my_integral".equals(fromWhere)){
			try {
				String resultStr = aes.decrypt(resultString);
//				Toast.makeText(ConfirmGoodsActivity.this, resultStr,
//						Toast.LENGTH_SHORT).show();
				if(Util.isLogin(ConfirmGoodsActivity.this)){
					Intent intent = new Intent(ConfirmGoodsActivity.this, PaymentDetailActivity.class);
					intent.putExtra("payeeUid",resultStr);
					intent.putExtra("from",fromWhere);
					startActivity(intent);
				}else {
					Intent login_intent = new Intent(this,LoginActivity.class);
					startActivity(login_intent);
				}

			} catch (Exception e) {
				Toast.makeText(ConfirmGoodsActivity.this, "您扫描的二维码类型系统无法识别", Toast.LENGTH_SHORT).show();
				return;
			}finally {
				this.finish();
				return;
			}
		}
		
		if(type == 1){
			if (resultString.equals("")) {
				Toast.makeText(ConfirmGoodsActivity.this, "Scan failed!",
						Toast.LENGTH_SHORT).show();
			}else{
				try {
					resultString = aes.decrypt(resultString);
				} catch (Exception e) {
					Toast.makeText(ConfirmGoodsActivity.this, "您扫描的二维码类型系统无法识别", Toast.LENGTH_SHORT).show();
					this.finish();
					return;
				}
				//解析扫描到的结果
//				QrResultModel mQrResultModel = JSON.parseObject(resultString,QrResultModel.class);
//				String json = JSON.toJSONString(mQrResultModel);
//				mQrResultModel.setTaskId(taskId);
//				mQrResultModel.setLicence(license);
				String resultStr = resultString;
				
				// 加密
				resultStr = aes.encrypt(resultStr.getBytes());
				try {
					resultStr = URLEncoder.encode(resultStr, "utf-8");
					AjaxParams params = new AjaxParams();
					params.put("weighJson", resultStr);
					DidiApp.getHttpManager().sessionPost(ConfirmGoodsActivity.this,qr_url, params, new AfinalHttpCallBack() {
								@Override
								public void data(String t) {
									Intent intent = new Intent();
									intent.setAction(CommonRes.RefreshWinBid);
									sendBroadcast(intent);
//									ToastUtil.show(ConfirmGoodsActivity.this, "上传成功");
									Intent transite_detail_intent = new Intent(ConfirmGoodsActivity.this,TransitDetailActivity.class);
									transite_detail_intent.putExtra("taskId", taskId);
									startActivity(transite_detail_intent);
									finish();
								}
							});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(type == 2){
			Intent intent=new Intent();
            intent.putExtra("scanResult", resultString);  
            setResult(RESULT_OK, intent);  
            finish();
		}

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new ActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
}