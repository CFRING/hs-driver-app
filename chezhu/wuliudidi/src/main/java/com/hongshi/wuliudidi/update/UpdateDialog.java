package com.hongshi.wuliudidi.update;

import com.hongshi.wuliudidi.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class UpdateDialog extends Dialog {
	private Context mContext;
	private TextView mContentText, mCancel, mUpdate;
	private Handler mUpdateHandler;
	private String mContent;
	public UpdateDialog(Context context, int style, Handler mUpdateHandler,String mContent) {
		super(context, style);
		this.mContext = context;
		this.mUpdateHandler = mUpdateHandler;
		this.mContent = mContent;
		init();
	}

	public void init() {
		setContentView(R.layout.update_dialog);
		mContentText = (TextView) findViewById(R.id.update_content);
		mCancel = (TextView) findViewById(R.id.cancel);
		mUpdate = (TextView) findViewById(R.id.update_button);
		String ss = "";
		if(mContent != null) {
			ss = mContent.replace("\\n", "\n");
		}
		mContentText.setText(ss); 

		mCancel.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mUpdateHandler.sendEmptyMessage(2);
				UpdateDialog.this.dismiss();
			}
		});
		mUpdate.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mUpdateHandler.sendEmptyMessage(1);
				UpdateDialog.this.dismiss();
			}
		});
	}

}
