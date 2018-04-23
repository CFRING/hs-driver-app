package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆角 圆形 矩形 图片控件
 */
public class CircleImageView extends ImageView {

	/**
	 * 是否是圆形边框
	 */
	private boolean isRound;
	/**
	 * 是否是矩形边框
	 */
	private boolean isRect;
	/**
	 * 边框颜色
	 */
	private int mBorderColor = 0xFF000000;
	/**
	 * 是否是圆角边框
	 */
	private boolean isRoundCorner;
	/**
	 * 圆角程度
	 */
	private float radii;
	/**
	 * 左上圆角程度
	 */
	private float radiiTopLeft;
	/**
	 * 左下圆角程度
	 */
	private float radiiBottomLeft;
	/**
	 * 右上圆角程度
	 */
	private float radiiTopRight;
	/**
	 * 右下圆角程度
	 */
	private float radiiBottomRight;
	/**
	 * 边框厚度
	 */
	private float borderWidth = 0;

	public CircleImageView(Context context) {
		this(context, null);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.XImageView);
		isRound = a.getBoolean(R.styleable.XImageView_round, true);
		isRoundCorner = a.getBoolean(R.styleable.XImageView_roundCorner, false);
		isRect = a.getBoolean(R.styleable.XImageView_rect, false);
		mBorderColor = a.getColor(R.styleable.XImageView_borderColor, Color.BLACK);
		radii = a.getFloat(R.styleable.XImageView_radii, 15f);
		radiiTopLeft = a.getFloat(R.styleable.XImageView_radiiTopLeft, 0f);
		radiiTopRight = a.getFloat(R.styleable.XImageView_radiiTopRight, 0f);
		radiiBottomLeft = a
				.getFloat(R.styleable.XImageView_radiiBottomLeft, 0f);
		radiiBottomRight = a.getFloat(R.styleable.XImageView_radiiBottomRight,
				0f);
		borderWidth = a.getFloat(R.styleable.XImageView_borderWidth, 0f);
		a.recycle();
		if (radii != 0) {
			// 如果该值不为0 则圆角程度统一按照该值
			radiiTopLeft = radiiTopRight = radiiBottomLeft = radiiBottomRight = radii;
			isRoundCorner = true;
		}
		if (getDrawable() != null) {
			setImageDrawable(getDrawable());
		}

		if (isRound || isRoundCorner || isRect) {
			invalidate();
		}
	}
	
	/**
	 * 设置矩形效果
	 * @param isRect
	 * @param mColor
	 */
	public void setRect(boolean isRect, int mColor) {
		this.isRect = isRect;
		if(this.isRect){
			this.isRound = false;
			this.isRoundCorner = false;
		}
		this.mBorderColor = mColor;
	}
	/**
	 * 设置圆形效果
	 * @param isRound
	 * @param mColor
	 */
	public void setRound(boolean isRound, int mColor) {
		this.isRound = isRound;
		if(this.isRound){
			this.isRect = false;
			this.isRoundCorner = false;
		}
		this.mBorderColor = mColor;
	}
	/**
	 * 设置圆角效果
	 * @param isRoundCorner
	 * @param mColor
	 */
	public void setRoundCorner(boolean isRoundCorner, int mColor) {
		this.isRoundCorner = isRoundCorner;
		if(this.isRoundCorner){
			this.isRect = false;
			this.isRound = false;
		}
		this.mBorderColor = mColor;
	}
	
	/**
	 * 设置圆角半径
	 * @param radius
	 */
	public void setCornerRadius(float radius){
		if(!this.isRoundCorner){
			return;
		}
		radiiTopLeft = radius;
		radiiTopRight = radius;
		radiiBottomLeft = radius;
		radiiBottomRight = radius;
	}
	
	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}
	
	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int mBorderColor) {
		this.mBorderColor = mBorderColor;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		float h = getHeight();
		float w = getWidth();

		float x = w / 2.0f;
		float y = h / 2.0f;
		RectF rect = null;
		if (isRound) {
			//圆形
			Path clipPath = new Path();
			clipPath.addCircle(x, y, (float) Math.min(x, y), Path.Direction.CW);
			canvas.clipPath(clipPath);
		}
		if(isRect){
			//矩形
			Path clipPath = new Path();
			clipPath.addRect(0, 0, w, h, Path.Direction.CCW);
			canvas.clipPath(clipPath);
		} 
		if (isRoundCorner) {
			//圆角矩形
			Path clipPath = new Path();
			rect = new RectF(0, 0, (int) w, (int) h);
			float topLeft = radiiTopLeft * 1f / w * h;
			float topRight = radiiTopLeft * 1f / w * h;
			float bottomRight = radiiTopLeft * 1f / w * h;
			float bottomLeft = radiiTopLeft * 1f / w * h;
			clipPath.addRoundRect(rect, new float[] { radiiTopLeft,
					topLeft, radiiTopRight, topRight, radiiBottomRight,
					bottomRight, radiiBottomLeft, bottomLeft },
					Path.Direction.CW);
			canvas.clipPath(clipPath);
		}
		//调用此方法  根据路径画图片
		super.onDraw(canvas);

		if(borderWidth > 0){
			//画边框
			Paint p = new Paint();
			//设置画笔颜色
			p.setColor(mBorderColor);
			//设置画笔为无锯齿
			p.setAntiAlias(true);
			//空心效果
			p.setStyle(Paint.Style.STROKE);
			//线宽
			p.setStrokeWidth(borderWidth);
			if (isRound) {
				//圆形
				canvas.drawCircle(x, y, (float) Math.min(x, y), p);
			}else if(isRect){
				//矩形
				canvas.drawRect(0, 0, w, h, p);
			}else if(isRoundCorner){
				//圆角矩形;
				canvas.drawRoundRect(rect, borderWidth, borderWidth, p);
			}
		}
	}
}
