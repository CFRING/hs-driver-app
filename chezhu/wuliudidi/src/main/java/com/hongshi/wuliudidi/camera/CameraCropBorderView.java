package com.hongshi.wuliudidi.camera;

import com.hongshi.wuliudidi.R;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author huiyuan
 */
public class CameraCropBorderView extends View {
	/**
	 * 绘制的矩形的宽度
	 */
	private int mWidth = 640;
	/**
	 * 绘制的矩形的高度
	 */
	private int mHeight = 640;
	/**
	 * 边框的颜色，默认为白色
	 */
	private int mBorderColor = Color.parseColor("#FFFFFF");
	/**
	 * 边框以外的颜色
	 */
	private int mFillColor = Color.parseColor("#d6000000");
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 1;
	/**
	 * border的画笔
	 */
	private Paint mPaint;
	/**
	 * fill的画笔
	 */
	private Paint mPaintFill;
	/**
	 * 绘制的矩形的范围
	 */
	private Rect rect = new Rect();
	/**
	 * 第一次绘制时取得rect的范围
	 */
	private boolean isFirst = true;
	private Context context;

	public CameraCropBorderView(Context context) {
		this(context, null);
		this.context = context;
	}

	public CameraCropBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public CameraCropBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mBorderColor);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);

		mPaintFill = new Paint();
		mPaintFill.setAntiAlias(true);
		mPaintFill.setColor(mFillColor);
		mPaintFill.setStyle(Style.FILL);
		this.context = context;
	}

	public Rect getRect() {
		return rect;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isFirst) {
			int srceenW = getWidth();
			int screenH = getHeight();

			int size=srceenW;
			//shuping
			int left=0;
			int right=size;
			int top=screenH/2 - srceenW/2;
			int bottom=screenH/2+srceenW/2;
				
//			mWidth = srceenW - 30;
//			mHeight = screenH - 30;
//			int left = srceenW / 2 - context.getResources().getDimensionPixelSize(R.dimen.width_148);
//			int top = screenH / 2 - context.getResources().getDimensionPixelSize(R.dimen.height_75);
//			int right = srceenW / 2 + context.getResources().getDimensionPixelSize(R.dimen.width_148);
//			int bottom = screenH / 2 + context.getResources().getDimensionPixelSize(R.dimen.height_75);
//			int left = srceenW / 2 - context.getResources().getDimensionPixelSize(R.dimen.width_160);
//			int top = screenH / 2 - context.getResources().getDimensionPixelSize(R.dimen.width_160);
//			int right = srceenW / 2 + context.getResources().getDimensionPixelSize(R.dimen.width_160);
//			int bottom = screenH / 2 + context.getResources().getDimensionPixelSize(R.dimen.width_160);
			rect.set(left, top, right, bottom);
			isFirst = false;
		}
		// 绘制外边框
		canvas.drawRect(rect, mPaint);
		//绘制上边
		canvas.drawRect(0, 0, getWidth(), rect.top, mPaintFill);
		//绘制下边
		canvas.drawRect(0, rect.bottom, getWidth(), getHeight(), mPaintFill);
		//绘制左边
		canvas.drawRect(0, rect.top, rect.left, rect.bottom, mPaintFill);
		//绘制右边
		canvas.drawRect(rect.right, rect.top, getWidth(), rect.bottom, mPaintFill);
		
		Paint mPaintFill1 = new Paint();
		mPaintFill1.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.width_16));
		mPaintFill1.setTextAlign(Paint.Align.CENTER);
		mPaintFill1.setColor(Color.WHITE);
//		canvas.drawText("请把证件放入框中", rect.centerX(), getHeight() / 2 + context.getResources().getDimensionPixelSize(R.dimen.height_90), mPaintFill1);
	}
}
