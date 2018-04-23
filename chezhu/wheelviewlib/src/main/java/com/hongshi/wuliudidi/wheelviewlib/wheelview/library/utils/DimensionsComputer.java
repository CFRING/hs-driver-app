package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.utils;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**屏幕尺寸相关计算的类，通过对当前屏幕与1280x720的屏幕的宽高比来进行伸缩。<p>可以通过此类获得经过伸缩后的尺寸，以达到对不同屏幕的适应。<p>此类必须调用{@link #(Context)}激活此类，才能正常调用非静态方法
 * <p>典型应用
 * <ui>
 * 		<li>通过{@link #getWidth(int)}}来获得伸缩后的宽度
 * 		<li>通过{@link #getHeight(int)}}来获得伸缩后的高度
 * 		<li>通过{@link #getTextSize(int)}来获得伸缩后的字体大小
 * <ui>
 *
 */
public class DimensionsComputer {
	
	private static DimensionsComputer instance = null;
	
	private int screenWidth = 0;
	private int screenHeight = 0;
	
	private Context context;
	
	private DimensionsComputer(){};
	
	public static synchronized DimensionsComputer getInstance(){
		
		if(instance == null){
			instance = new DimensionsComputer();
		}
		return instance;
	}
	
	/**
	 * 判断当前分辨率是否符合1280*720
	 * @return
	 * @time   :2012-11-16上午11:12:44
	 */
	public boolean IS_Dimensions_720P(){
		if(screenWidth == 1280 && screenHeight == 720){
			return true;
		}
		return false;
	}
	
	/**激活此类
	 * @param context 上下文对象
	 * @return
	 */
	public DimensionsComputer activate(Context context){
		this.context = context;
		if(screenWidth == 0 || screenHeight == 0){
			if(context instanceof Activity){
				screenWidth = ((Activity) context).getWindow().getDecorView().getMeasuredWidth();
				screenHeight = ((Activity) context).getWindow().getDecorView().getMeasuredHeight();
				if(screenWidth <= 0){
					screenWidth = getDisplayMetrics().widthPixels;
					screenHeight = getDisplayMetrics().heightPixels;
				}
			}else{
				screenWidth = getDisplayMetrics().widthPixels;
				screenHeight = getDisplayMetrics().heightPixels;
			}
		}
//		WindowManager windowManager =context.getWindowManager();
//		Display display = windowManager.getDefaultDisplay();
//		screenWidth = display.getWidth();
//		screenHeight = display.getHeight();
		Log.i("lihe","screen widht is "+screenWidth+" screen height is "+screenHeight);
		return instance;
	}
	
	public  DisplayMetrics getDisplayMetrics(){
		return context.getResources().getDisplayMetrics();
	}
	
	
	/**获得屏幕宽度
	 * @return
	 */
	public  int getScreenWidth(){
		return screenWidth;
	}
	
	/**获取屏幕高度
	 * @return
	 */
	public int getScreenHeight(){
		return screenHeight;
	}
	
	/**获得当前屏幕宽度相对于720的正比率
	 * @return
	 */
	public  float getPositiveRatioOfWidthOfCurrentScreenTo1280(){
		//Log.v("screen","getPositiveRatioOfWidthOfCurrentScreenTo1280 is ")
		return ((float)getScreenWidth()) / 720;
	}
	/**获得当前屏幕高度相对于1280的正比率
	 * @return
	 */
	public  float getPositiveRatioOfHeightOfCurrentScreenTo720(){
		
		return ((float)getScreenHeight()) / 1280;
	}
	
	/**获得view伸缩后的尺寸的值
	 * @param sourceValue
	 * @return
	 */
	public  int getWidth(int sourceValue){
		
		return (int) (sourceValue * getPositiveRatioOfWidthOfCurrentScreenTo1280());
	}
	/**获得view伸缩后的尺寸的值
	 * @param sourceValue
	 * @return
	 */
	public  int getHeight(int sourceValue){
		
		return (int) (sourceValue * getPositiveRatioOfHeightOfCurrentScreenTo720());
	}
	
	/**
	 * 
	 * @param sourceValue
	 * @return
	 * @time   :2012-11-1下午4:42:15
	 */
	public float getReflectionRatio(float sourceValue){
		return sourceValue * getPositiveRatioOfHeightOfCurrentScreenTo720();
	}
	
	/**获得文本缩放后大小
	 * @param sourceValue
	 * @return
	 */
	public  int getTextSize(int sourceValue){
		int size = (int) (( (float)(getScreenHeight() - 720) / getScreenHeight()) * sourceValue + sourceValue);
		return size;
	}

}
