package com.hongshi.wuliudidi.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.LogUtil;

/**
 * @author huiyuan
 * //默认的相机为横平，所以Activity设置为横屏，拍出的照片才正确
 */
@SuppressLint("NewApi")

public class ActivityCapture extends Activity implements
		View.OnClickListener, CaptureSensorsObserver.RefocuseListener {
	private ImageView bnToggleCamera;
	private ImageView bnCapture;

	private FrameLayout framelayoutPreview;
	private CameraPreview preview;
	private CameraCropBorderView cropBorderView;
	private Camera camera;
	private PictureCallback pictureCallBack;
	private Camera.AutoFocusCallback focusCallback;
	private CaptureSensorsObserver observer;
	private View focuseView;

	private int currentCameraId;
	private int frontCameraId;
	private boolean _isCapturing;

	CaptureOrientationEventListener _orientationEventListener;
	private int _rotation;
	private ImageView mLightImage;

	public static final int kPhotoMaxSaveSideLen = 1600;
	public static final String kPhotoPath = "photo_path";
	private TextView mCancel;
	private boolean isOpen = false;

	final static String TAG = "capture";
	private SharedPreferences mSharedPreferences;
	private Editor edit;
	private int amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		amount = getIntent().getIntExtra("amount",0);
		setContentView(R.layout.activity_capture);
		mCancel = (TextView) findViewById(R.id.cancle);
		mLightImage = (ImageView) findViewById(R.id.light);
		observer = new CaptureSensorsObserver(this);
		_orientationEventListener = new CaptureOrientationEventListener(this);
		mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		edit = mSharedPreferences.edit();
		isOpen = mSharedPreferences.getBoolean("light_state", false);

		if(isOpen){
			mLightImage.setImageResource(R.drawable.light);
		}else{
			mLightImage.setImageResource(R.drawable.no_light);
		}
		
		mLightImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOpen){
					mLightImage.setImageResource(R.drawable.light);
					edit.putBoolean("light_state", true).commit();
					isOpen = false;
				}else{
					mLightImage.setImageResource(R.drawable.no_light);
					edit.putBoolean("light_state", false).commit();
					isOpen = true;
				}
				
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		getViews();
		initViews();
		setListeners();
		setupDevice();
	}

	@Override
	protected void onDestroy() {
		if (null != observer) {
			observer.setRefocuseListener(null);
			observer = null;
		}
		_orientationEventListener = null;

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera(); // release the camera immediately on pause event

		observer.stop();
		_orientationEventListener.disable();
	}

	@Override
	protected void onResume() {
		super.onResume();
		openCamera();
	}

	private void releaseCamera() {
		if (camera != null) {
			camera.release(); // release the camera for other applications
			camera = null;
		}

		if (null != preview) {
			framelayoutPreview.removeAllViews();
			preview = null;
		}
	}

	protected void getViews() {
		bnToggleCamera = (ImageView) findViewById(R.id.bnToggleCamera);
		bnCapture = (ImageView) findViewById(R.id.bnCapture);
		framelayoutPreview = (FrameLayout) findViewById(R.id.cameraPreview);
		focuseView = findViewById(R.id.viewFocuse);
	}

	protected void initViews() {
		bnCapture.setRotation(-90);
//		bnToggleCamera.setRotation(90);
	}

	protected void setListeners() {
		bnToggleCamera.setOnClickListener(this);
		bnCapture.setOnClickListener(this);
		observer.setRefocuseListener(this);
		pictureCallBack = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				_isCapturing = false;
//				Bitmap bitmap = null;
//				try {
				WindowManager windowManager = getWindowManager();    
			    Display display = windowManager.getDefaultDisplay();    
			    int screenWidth = display.getWidth();    
				int screenHeight = display.getHeight();

				Bitmap srcImg = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				float rate = 1.0f * 700 / srcImg.getHeight();
				int x = screenHeight / 2 - screenWidth / 2;
				int y = screenHeight / 2 + screenWidth / 2;

				Matrix m = new Matrix();

				m.setScale(rate, rate);
				srcImg = srcImg.createBitmap(srcImg, 0, 0, srcImg.getWidth(),
						srcImg.getHeight(), m, true);
				// srcImg = cropPhotoImage(srcImg);
				// srcImg=srcImg.createBitmap(srcImg,
				// screenHeight/2-screenWidth/2, screenHeight/2-screenWidth/2,
				// screenWidth ,screenWidth);
				if(currentCameraId == 0){
					srcImg = adjustPhotoRotation(srcImg,90);
				}else{
					srcImg = adjustPhotoRotation(srcImg,270);
				}
				// 保存并且拿到保存的路径
				String saveBitmap = BitmapUtil.saveBitmap(srcImg);

				Intent intent = new Intent();
				intent.putExtra(kPhotoPath, saveBitmap);
				intent.putExtra("amount",amount);
				ActivityCapture.this.setResult(RESULT_OK, intent);
				ActivityCapture.this.finish();
				
			}
		};
		focusCallback = new Camera.AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean successed, Camera camera) {
				focuseView.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree)
	{

		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		float targetX, targetY;
		if (orientationDegree == 90) {
			targetX = bm.getHeight();
			targetY = 0;
		} else {
			targetX = 0;
			targetY = bm.getHeight();
		}

		final float[] values = new float[9];
		m.getValues(values);

		float x1 = values[Matrix.MTRANS_X];
		float y1 = values[Matrix.MTRANS_Y];

		m.postTranslate(targetX - x1, targetY - y1);

		Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bm1);
		canvas.drawBitmap(bm, m, paint);

		return bm1;
	}
	private void setupDevice() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			int cameraCount = Camera.getNumberOfCameras();

			if (cameraCount < 1) {
				Toast.makeText(this, "你的设备木有摄像头。。。", Toast.LENGTH_SHORT).show();
				finish();
				return;
			} else if (cameraCount == 1) {
				bnToggleCamera.setVisibility(View.INVISIBLE);
			} else {
				bnToggleCamera.setVisibility(View.VISIBLE);
			}

			currentCameraId = 0;
			frontCameraId = findFrontFacingCamera();
			if (-1 == frontCameraId) {
				bnToggleCamera.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void openCamera() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			try {
				camera = Camera.open(currentCameraId);
			} catch (Exception e) {
				Toast.makeText(this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			setCameraDisplayOrientation(this, 0, camera);
		} else {
			try {
				camera = Camera.open();
			} catch (Exception e) {
				Toast.makeText(this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}

		Camera.Parameters camParmeters = camera.getParameters();
		List<Size> sizes = camParmeters.getSupportedPreviewSizes();
		for (Size size : sizes) {
			LogUtil.v(TAG, "w:" + size.width + ",h:" + size.height);
		}

		preview = new CameraPreview(this, camera);
		cropBorderView = new CameraCropBorderView(this);
//		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		WindowManager windowManager = getWindowManager();
		int width = windowManager.getDefaultDisplay().getWidth();
		int height = 9*width/16;
		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(width,height);
		params1.gravity = Gravity.CENTER;
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		framelayoutPreview.addView(preview, params1);
		framelayoutPreview.addView(cropBorderView, params1);
		observer.start();
		_orientationEventListener.enable();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bnToggleCamera:
			switchCamera();
			break;
		case R.id.cancel:
			finish();
			break;
		case R.id.bnCapture:
			isOpen = mSharedPreferences.getBoolean("light_state", false);
			if(isOpen){
				//打开闪光灯
				try {
					Camera.Parameters mParameters;
					mParameters = camera.getParameters();
					mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					camera.setParameters(mParameters);
				} catch (Exception ex) {
				}
			}else{
				//关闭闪光灯
				try {
					Camera.Parameters mParameters;
					mParameters = camera.getParameters();
					mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					camera.setParameters(mParameters);
				} catch (Exception ex) {
				}
			}
			bnCaptureClicked();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	//横竖屏切换的时候
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void switchCamera() {
		if (currentCameraId == 0) {
			currentCameraId = frontCameraId;
		} else {
			currentCameraId = 0;
		}
		releaseCamera();
		openCamera();
	}

	private void bnCaptureClicked() {
		if (_isCapturing) {
			return;
		}
		_isCapturing = true;
		focuseView.setVisibility(View.INVISIBLE);

		try {
			camera.takePicture(null, null, pictureCallBack);
		} catch (RuntimeException e) {
			e.printStackTrace();
			_isCapturing = false;
		}
	}

	/**
	 * A basic Camera preview class
	 */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;

		@SuppressWarnings("deprecation")
		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (Exception e) {
				LogUtil.d(TAG, "Error setting camera preview: " + e.getMessage());
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// empty. Take care of releasing the Camera preview in your
			// activity.
			if (camera != null) {
				camera.setPreviewCallback(null);
				// 停止预览
				camera.stopPreview();
				// 释放摄像头资源
				camera.release();
				camera = null;
			}
		}

		private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
			final double ASPECT_TOLERANCE = 0.05;
			double targetRatio = (double) w / h;
			if (sizes == null)
			{
				return null;
			}

			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;

			int targetHeight = h;

			// Try to find an size match aspect ratio and size
			for (Size size : sizes) {
				double ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				{
					continue;
				}
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}

			// Cannot find the one match the aspect ratio, ignore the
			// requirement
			if (optimalSize == null) {
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes) {
					if (Math.abs(size.height - targetHeight) < minDiff) {
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}

			return optimalSize;
		}

		private Size getOptimalPictureSize(List<Size> sizes, double targetRatio) {
			final double ASPECT_TOLERANCE = 0.05;

			if (sizes == null)
			{
				return null;
			}

			Size optimalSize = null;
			int optimalSideLen = 0;
			double optimalDiffRatio = Double.MAX_VALUE;

			for (Size size : sizes) {

				int sideLen = Math.max(size.width, size.height);
				//LogEx.i("size.width: " + size.width + ", size.height: " + size.height);
				boolean select = false;
				if (sideLen < kPhotoMaxSaveSideLen) {
					if (0 == optimalSideLen || sideLen > optimalSideLen) {
						select = true;
					}
				} else {
					if (kPhotoMaxSaveSideLen > optimalSideLen) {
						select = true;
					} else {
						double diffRatio = Math.abs((double) size.width / size.height - targetRatio);
						if (diffRatio + ASPECT_TOLERANCE < optimalDiffRatio) {
							select = true;
						} else if (diffRatio < optimalDiffRatio + ASPECT_TOLERANCE && sideLen < optimalSideLen) {
							select = true;
						}
					}
				}

				if (select) {
					optimalSize = size;
					optimalSideLen = sideLen;
					optimalDiffRatio = Math.abs((double) size.width / size.height - targetRatio);
				}
			}

			return optimalSize;
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

			Debug.debug("surfaceChanged format:" + format + ", w:" + w + ", h:" + h);
			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			try {
				Camera.Parameters parameters = mCamera.getParameters();

				List<Size> sizes = parameters.getSupportedPreviewSizes();
				Size optimalSize = getOptimalPreviewSize(sizes, w, h);
				parameters.setPreviewSize(optimalSize.width, optimalSize.height);
				double targetRatio = (double) w / h;
				sizes = parameters.getSupportedPictureSizes();
				optimalSize = getOptimalPictureSize(sizes, targetRatio);
				parameters.setPictureSize(optimalSize.width, optimalSize.height);
				parameters.setRotation(0);
				mCamera.setParameters(parameters);
			} catch (Exception e) {
				Debug.debug(e.toString());
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
			} catch (Exception e) {
				Debug.debug("Error starting camera preview: " + e.getMessage());
			}
		}
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				LogUtil.d(TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	private static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		default:
			break;
		}

		int result;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}

		//LogEx.i("result: " + result);
		camera.setDisplayOrientation(result);
	}

	@Override
	public void needFocuse() {

		//LogEx.i("_isCapturing: " + _isCapturing);
		if (null == camera || _isCapturing) {
			return;
		}

		//LogEx.i("autoFocus");
		camera.cancelAutoFocus();
		try {
			camera.autoFocus(focusCallback);
		} catch (Exception e) {
			Debug.debug(e.toString());
			return;
		}

		if (View.INVISIBLE == focuseView.getVisibility()) {
			focuseView.setVisibility(View.VISIBLE);
			focuseView.getParent().requestTransparentRegion(preview);
		}
	}

	/**
	 * 相机旋转监听的类，最后保存图片时用到
	 */
	private class CaptureOrientationEventListener extends OrientationEventListener {
		public CaptureOrientationEventListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(int orientation) {
			if (null == camera)
			{
				return;
			}
			if (orientation == ORIENTATION_UNKNOWN)
			{
				return;
			}

			orientation = (orientation + 45) / 90 * 90;
			if (android.os.Build.VERSION.SDK_INT <= 8) {
				_rotation = (90 + orientation) % 360;
				return;
			}

			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(currentCameraId, info);

			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				_rotation = (info.orientation - orientation + 360) % 360;
			} else { // back-facing camera
				_rotation = (info.orientation + orientation) % 360;
			}
		}
	}
}
