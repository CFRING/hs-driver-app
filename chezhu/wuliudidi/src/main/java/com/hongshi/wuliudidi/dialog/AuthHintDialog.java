package com.hongshi.wuliudidi.dialog;

import com.hongshi.wuliudidi.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AuthHintDialog extends Dialog {
	private Context mContext;
	private TextView mTitle, mContent;
	private RelativeLayout mIknow;
	
	public AuthHintDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		init();
	}

	protected AuthHintDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
		init();
	}

	public AuthHintDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public void setHint(int resid){
		mContent.setText(resid);
	}
	
	public void setHint(String hint){
		mContent.setText(hint);
	}
	
	public void setTitle(String title){
		mTitle.setText(title);
	}
	
	private void init(){
		setContentView(R.layout.auth_hint_dialog);
		mContent = (TextView) findViewById(R.id.content);
		mTitle = (TextView) findViewById(R.id.title);
		mIknow = (RelativeLayout) findViewById(R.id.i_know_layout);
		mContent.setText("");
		
		mIknow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AuthHintDialog.this.dismiss();
			}
		});
		
	}
}
