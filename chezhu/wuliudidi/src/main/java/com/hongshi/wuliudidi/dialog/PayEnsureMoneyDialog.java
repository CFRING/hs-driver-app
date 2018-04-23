package com.hongshi.wuliudidi.dialog;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class PayEnsureMoneyDialog extends Dialog implements android.view.View.OnClickListener{
	private Context mContext;
	private Button queryServer, cancel;
	
	public PayEnsureMoneyDialog(Activity context, int style) {
		super(context, style);
		this.mContext = context;
		init();
	}
	private void init(){
		setContentView(R.layout.pay_ensure_money_dialog);
		queryServer = (Button)findViewById(R.id.query_service);
		queryServer.setOnClickListener(this);
		cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.query_service:
			Util.call(mContext, mContext.getResources().getString(R.string.contact_number));
			break;

		case R.id.cancel:
			dismiss();
			break;
		default:
			break;
		}
		
	}
}
