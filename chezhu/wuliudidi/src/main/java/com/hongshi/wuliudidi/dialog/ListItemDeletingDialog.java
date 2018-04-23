package com.hongshi.wuliudidi.dialog;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ListItemDeletingDialog extends Dialog implements OnClickListener{
	private Context mContext;
	private Button mDelete, mCancel;
	private ImageView example_img;
	private Handler mHandler;
	//要被删除的对象id
	private String itemId = "";
	//向Activity发送的提示码
	private int msgNum;
	public ListItemDeletingDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	protected ListItemDeletingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
		init();
	}
	
	public ListItemDeletingDialog(Context context, int theme, Handler mHandler) {
		super(context, theme);
		this.mContext = context;
		this.mHandler = mHandler;
		init();
	}
	
	public ListItemDeletingDialog(Context context, int theme) {
		super(context, theme);
	}

	private void init(){
		setContentView(R.layout.data_filling_dialog);
		mDelete = (Button) findViewById(R.id.choice1);
		mCancel = (Button) findViewById(R.id.choice2);
		example_img = (ImageView) findViewById(R.id.example_img);
		mDelete.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		
		Button mButton = (Button) findViewById(R.id.cancel);
		mButton.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.choice1:
				Message msg = mHandler.obtainMessage();
				msg.what = msgNum;
				Bundle bundle = new Bundle();    
                bundle.putString("itemId", itemId);
                msg.setData(bundle);
				mHandler.sendMessage(msg);
				dismiss();
				break;
			case R.id.choice2:
				dismiss();
				break;
			default:
				break;
		}
	}
	
	
	public void setText(String str1, String str2){
		mDelete.setText(str1);
		mCancel.setText(str2);
	}
	
	public void setItemId(String id){
		this.itemId = id;
	}
	public void setMsgNum(int msgNum){
		this.msgNum = msgNum;
	}

	public ImageView getExampleImg(){
		return example_img;
	}
}
