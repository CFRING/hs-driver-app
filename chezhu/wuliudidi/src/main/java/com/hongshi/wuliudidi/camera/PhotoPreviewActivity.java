package com.hongshi.wuliudidi.camera;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author huiyuan
 */
@SuppressLint("NewApi")
public class PhotoPreviewActivity extends Activity{
	private static final String KImagePath = "image_path";
	private String imagePath = "";
	private ImageView image;
	public Bitmap bitmap;
	public static final int CameraRequestCode = 1001;
	private TextView mStartAgain,mSure;
	private int tag;
	private int finalAmount;
	public static void open(Activity activity, String imagePath,int tag) {
		Intent intent = new Intent(activity, PhotoPreviewActivity.class);
		intent.putExtra(KImagePath, imagePath);
		intent.putExtra("tag", tag);
		activity.startActivity(intent);
	}
	public static void open(Activity activity, String imagePath,int tag,int finalAmount) {
		Intent intent = new Intent(activity, PhotoPreviewActivity.class);
		intent.putExtra(KImagePath, imagePath);
		intent.putExtra("tag", tag);
		intent.putExtra("amount",finalAmount);
		activity.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finalAmount = getIntent().getIntExtra("amount",0);
		setContentView(R.layout.photo_preview_activity);
		if (getIntent() != null) {
			Intent intent = getIntent();
			imagePath = intent.getStringExtra(KImagePath);
			tag = intent.getIntExtra("tag", -1);
		}
		image = (ImageView) findViewById(R.id.image);
		mSure = (TextView) findViewById(R.id.sure);
		mStartAgain = (TextView) findViewById(R.id.start_again);
		bitmap = BitmapUtil.loadBitmap(imagePath);
		image.setImageBitmap(bitmap);
		mStartAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PhotoPreviewActivity.this, ActivityCapture.class);
				startActivityForResult(intent, CameraRequestCode);
			}
		});
		mSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("imagePath", imagePath);
				intent.putExtra("tag", tag);
				intent.putExtra("amount",finalAmount);
				intent.setAction(CommonRes.ACTIONPHOTOPATH);
				sendBroadcast(intent);
				finish();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CameraRequestCode && resultCode == RESULT_OK) {
			String path = data.getStringExtra(ActivityCapture.kPhotoPath);
			imagePath = path;
			bitmap = BitmapUtil.loadBitmap(path);
			image.setImageBitmap(bitmap);
			return;
		}
	}
}
