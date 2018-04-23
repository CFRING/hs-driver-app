package com.hongshi.wuliudidi.cashier;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

import com.hongshi.wuliudidi.R;


public class SdkTimeCounterButton extends Button {

	public SdkTimeCounterButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SdkTimeCounterButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SdkTimeCounterButton(Context context) {
		super(context);
	}
	
	public void timeCounting(int seconds){
		
		//set the default waiting time to 60s
		if(seconds<0)
			seconds = 60;
		
		CountDownTimer mCntTimer = new CountDownTimer(seconds*1000, 1000){
			
			@Override
			public void onTick(long millisUntilFinished) {
				setText("剩余" + millisUntilFinished / 1000 + "秒");
				setEnabled(false);
			}
			
			@Override
			public void onFinish() {
				setText(getResources().getString(R.string.captchas_refresh));
				setEnabled(true);
			}
		};
		mCntTimer.start();
	}
	
}




















