package com.hongshi.wuliudidi.dialog;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.params.GloableParams;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ErrorDialog extends Dialog implements OnClickListener{
	private Context mContext;
	private TextView mContentText;
	private ImageView mKnowImage;
	private String content;
	AjaxParams params = new AjaxParams();
	public ErrorDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	public ErrorDialog(Context context, int theme,String content) {
		super(context, theme);
		this.mContext = context;
		this.content = content;
		init();
	}
	
	private void init(){
		setContentView(R.layout.error_dialog);
		mKnowImage = (ImageView) findViewById(R.id.know);
		mContentText = (TextView) findViewById(R.id.content_text);
		mContentText.setText(content);
		mKnowImage.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.know:
				dismiss();
				break;
			default:
				break;
		}
	}
}
