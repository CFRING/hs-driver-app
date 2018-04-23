package com.hongshi.wuliudidi.activity;

import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.PayEnsureMoneyDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.MoneyAccountModel;
import com.hongshi.wuliudidi.model.MoneyChildAccountModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class TransportEnsureMoneyActivity extends Activity implements OnClickListener{
	private DiDiTitleView mTitle;
	private LinearLayout ensureMoneyIntroductionLayout;
	private Button bottomButton;
	private TextView ensureMoneyAmount;
	private PayEnsureMoneyDialog mDialog;
	private EnsureType type = EnsureType.noEnsureMoney;
	private final String introduction_url = GloableParams.WEB_URL + "guaranteed.html";
	private final String query_url = GloableParams.HOST + "carrier/app/acct/findByUserIdAndAcctType.do?";
	private MoneyAccountModel acctModel;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("TransportEnsureMoneyActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("TransportEnsureMoneyActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		try {
			String typeStr = getIntent().getStringExtra("type");
			type = EnsureType.valueOf(typeStr);
		} catch (Exception e) {
			
		}
		
		switch (type) {
		case noEnsureMoney:
			setContentView(R.layout.transport_ensure_money_activity_no);
			init();
			break;

		case haveEnsureMoney:
			setContentView(R.layout.transport_ensure_money_activity);
			init();
			break;
		default:
			break;
		}
	}
	
	private void init(){
		mTitle = (DiDiTitleView)findViewById(R.id.transport_ensure_money_title);
		mTitle.setTitle(getResources().getString(R.string.transport_ensure_money));
		mTitle.setBack(this);
		
		bottomButton = (Button)findViewById(R.id.pay_button);
		bottomButton.setOnClickListener(this);
		
		switch (type) {
		case haveEnsureMoney:
			ensureMoneyAmount = (TextView)findViewById(R.id.ensure_money_amount);
			bottomButton.setText(getResources().getString(R.string.recharge_ensure_money));
			break;

		case noEnsureMoney:
			ensureMoneyIntroductionLayout = (LinearLayout)findViewById(R.id.ensure_money_introduction_layout);
			ensureMoneyIntroductionLayout.setOnClickListener(this);
			break;
		default:
			break;
		}
		getData();
		
		mDialog = new PayEnsureMoneyDialog(TransportEnsureMoneyActivity.this, R.style.data_filling_dialog);
		UploadUtil.setAnimation(mDialog, CommonRes.TYPE_BOTTOM, true);
		mDialog.setCanceledOnTouchOutside(true);
	}
	
	private void getData(){
		if(type == EnsureType.haveEnsureMoney){
			AjaxParams params = new AjaxParams();
			DidiApp.getHttpManager().sessionPost(TransportEnsureMoneyActivity.this, query_url, params, new ChildAfinalHttpCallBack() {
				@Override
				public void data(String t) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(t);
						String all = jsonObject.getString("body");
						acctModel = JSON.parseObject(all, MoneyAccountModel.class);
						showData();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(String errCode, String errMsg, Boolean errSerious) {
					
				}
			});
		}
	}
	
	private void showData(){
		double total = 0;
		List<MoneyChildAccountModel> childList = acctModel.getAmountVOList();
		if(childList != null){
			for(int i = 0; i < childList.size(); i++){
				total += childList.get(i).getAmount();
			}
		}
		//需求要求金额保留整数，所以给参数“件”让其保留整数
		ensureMoneyAmount.setText(Util.formatDoubleToString(total, "件"));
	}
	
	public enum EnsureType{
		haveEnsureMoney, noEnsureMoney;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.pay_button:
			mDialog.show();
			break;
		case R.id.ensure_money_introduction_layout:
			Intent intent = new Intent(TransportEnsureMoneyActivity.this, WebViewWithTitleActivity.class);
			intent.putExtra("url", introduction_url);
			intent.putExtra("title", "运输保证金");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
