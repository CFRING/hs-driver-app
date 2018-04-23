package com.hongshi.wuliudidi.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.PlayerModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.view.CircleImageView;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.EditView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class InvatePlayerActivity extends Activity implements OnClickListener{
	private DiDiTitleView mInvateTitle;
	private EditView mPhoneEditView;
	private TextView mInvateBtn, mUserName, mUserPhoneNumber, mHintText;
	private LinearLayout mInvateLayout;
	private RelativeLayout mOkInvateLayout;
	//根据手机号查询信息
	private String info_url = GloableParams.HOST + "uic/fleet/queryUserByCellphone.do?";
	//司机队长邀请队员加入车队
	private String invite_url = GloableParams.HOST + "uic/fleet/inviteMember.do?";
	//根据手机号查询司机
	private String inquire_driver_url = GloableParams.HOST + "carrier/mydrivers/checkdriver.do?";
	private CircleImageView mCircleImageView;
	//被邀请队员的ID
	private String mUserId;
	InviteType inviteType;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("InvatePlayerActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("InvatePlayerActivity");
	}

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		
		try {
			String type = getIntent().getExtras().getString("inviteType", "");
			inviteType = InviteType.valueOf(type);
		} catch (Exception e) {
			inviteType = InviteType.invitePlayer;
		}
		
		setContentView(R.layout.invite_player_activity);
		initViews();
	}

	private void initViews(){
		mHintText = (TextView) findViewById(R.id.hint_text);
		mInvateTitle = (DiDiTitleView) findViewById(R.id.invate_player_title);
		if(inviteType == InviteType.invitePlayer){
			mInvateTitle.setTitle("邀请队员");
		}else if(inviteType == InviteType.addDriver){
			mInvateTitle.setTitle("添加司机");
			mHintText.setVisibility(View.GONE);
		}
		mInvateTitle.setBack(this);
		mPhoneEditView = (EditView) findViewById(R.id.phone_number_item);
		mPhoneEditView.setEditName("对方手机号");
		mPhoneEditView.setEditHint("请输入手机号");
		mPhoneEditView.getEditTextWidget().setInputType(InputType.TYPE_CLASS_NUMBER);
		mPhoneEditView.getEditTextWidget().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
		//邀请按钮和确认邀请按钮
		mInvateBtn = (TextView) findViewById(R.id.invate_btn);
		mInvateBtn.setOnClickListener(this);
		if(inviteType == InviteType.addDriver){
			mInvateBtn.setText(getResources().getString(R.string.next_step));
		}
		//邀请布局
		mInvateLayout = (LinearLayout) findViewById(R.id.invate_layout);
		//确认邀请布局
		mOkInvateLayout = (RelativeLayout) findViewById(R.id.ok_invate_layout);
		mCircleImageView = (CircleImageView) findViewById(R.id.user_photo);
		mCircleImageView.setImageResource(R.drawable.default_photo);
		mUserName = (TextView) findViewById(R.id.user_name);
		mUserPhoneNumber = (TextView) findViewById(R.id.user_phone_number);
	}

	private void getInfo(){
		AjaxParams params = new AjaxParams();
		params.put("cellphone", mPhoneEditView.getEditText());
		DidiApp.getHttpManager().sessionPost(InvatePlayerActivity.this, info_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					PlayerModel mPlayerModel = JSON.parseObject(body, PlayerModel.class);
					FinalBitmap mBitmap = FinalBitmap.create(InvatePlayerActivity.this);
					mBitmap.display(mCircleImageView, mPlayerModel.getUserFace());
					mUserName.setText(mPlayerModel.getName());
					mUserPhoneNumber.setText(mPlayerModel.getCellphone());
					mUserId = mPlayerModel.getUserId();
					mOkInvateLayout.setVisibility(View.VISIBLE);
					mInvateLayout.setVisibility(View.GONE);
					mInvateBtn.setText("确认邀请");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void invate(){
		AjaxParams params = new AjaxParams();
		params.put("subUserId", mUserId);	
		DidiApp.getHttpManager().sessionPost(InvatePlayerActivity.this, invite_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				ToastUtil.show(InvatePlayerActivity.this, "已发出邀请");
				Intent result_intent = new Intent(InvatePlayerActivity.this,ResultActivity.class);
				result_intent.putExtra("result", 2);
				startActivity(result_intent);
				finish();
			}
		});
	}
	
	private void addDriver(){
		Intent intent = new Intent(InvatePlayerActivity.this, DriverInfoActivity.class);
		intent.putExtra("isNew", true);
		intent.putExtra("cellPhone", mPhoneEditView.getEditText());
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invate_btn:
			if(inputJudge()){
				if(inviteType == InviteType.invitePlayer){
					if(mInvateBtn.getText().toString().equals("邀请队员")){
						getInfo();
					}else if(mInvateBtn.getText().toString().equals("确认邀请")){
						invate();
					}
				}else if(inviteType == InviteType.addDriver){
					addDriver();
				}
			}
			break;
		default:
			break;
		}
		
	}
	
	private boolean inputJudge(){
		if(mPhoneEditView.getEditText().length() <= 0){
			ToastUtil.show(InvatePlayerActivity.this, "请输入手机号");
			return false;
		}else if(mPhoneEditView.getEditText().length() != 11){
			ToastUtil.show(InvatePlayerActivity.this, "请正确输入手机号");
			return false;
		}
		return true;
	}
	
	public enum InviteType{
		invitePlayer, addDriver;
	}
}
