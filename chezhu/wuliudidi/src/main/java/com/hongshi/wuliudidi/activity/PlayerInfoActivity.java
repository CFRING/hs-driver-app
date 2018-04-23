package com.hongshi.wuliudidi.activity;


import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TeamMemberModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class PlayerInfoActivity extends Activity implements OnClickListener{

	private DiDiTitleView mTitle;
	private TextView mTitleRight,mPlayerNameText;
	private AuctionItem mMarkNameItem, mPhoneNumberItem,mMarkNumberItem,mBindItem;
	private Button mDelBtn;
	private ImageView photoImage;
//	private EditText mNameEdit;
	private String userId;
	//获取队员信息
	private String info_url = GloableParams.HOST + "uic/fleet/getMember.do?";
	//删除对员
	private String del_url = GloableParams.HOST + "uic/fleet/deleteMember.do?";
	//编辑完成
	private String finish_url = GloableParams.HOST + "uic/fleet/editMember.do?";
	private boolean isModify = false;
	private String mCellNumber,mMarkNumber;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("PlayerInfoActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("PlayerInfoActivity");
	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.TeamMemberModify)) {
				String teamMemberId = intent.getStringExtra("teamMemberUserId");
				if(teamMemberId != null && teamMemberId.equals(userId)){
					String truckNumber = intent.getStringExtra("truckNumber");
						if(truckNumber != null){
							mBindItem.setContent(truckNumber);
						}
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.player_info_activity);
		initViews();
		getData(userId);
	}

	private void initViews(){
		mTitle = (DiDiTitleView) findViewById(R.id.player_info_title);
		mTitle.setBack(this);
		mTitle.setTitle("队员信息");
		mTitleRight = mTitle.getRightTextView();
		mTitleRight.setText("编辑");
		mDelBtn = (Button) findViewById(R.id.del_btn);
		//拿到数据的model
//		TeamMemberModel	mTeamMemberModel = (TeamMemberModel) getIntent().getSerializableExtra("TeamMemberModel");

		userId = getIntent().getStringExtra("userId");
		// 备注昵称item
		mMarkNameItem = (AuctionItem) findViewById(R.id.mark_item);
		mMarkNameItem.setName("备注昵称");

		photoImage = (ImageView) findViewById(R.id.player_photo_image);

		//名字编辑
//		mNameEdit = (EditText) findViewById(R.id.player_name_edit);
		mPlayerNameText = (TextView) findViewById(R.id.player_name);
		// 手机号码
		mPhoneNumberItem = (AuctionItem) findViewById(R.id.phone_number_item);
		mPhoneNumberItem.setName("手机号码");
		mPhoneNumberItem.getRightImage().setImageResource(R.drawable.call);
		// 备注号码
		mMarkNumberItem = (AuctionItem) findViewById(R.id.mark_number_item);
		mMarkNumberItem.setName("备注号码");
		mMarkNumberItem.getRightImage().setImageResource(R.drawable.call);
		//绑定车辆
		mBindItem = (AuctionItem) findViewById(R.id.bind_item);
		mBindItem.setName("绑定车辆");

		//注册刷新广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.TeamMemberModify);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);

		mTitleRight.setOnClickListener(this);
		//绑定车辆item
		mBindItem.setOnClickListener(this);
	}

	private void initViewData(TeamMemberModel mTeamMemberModel){
		mPlayerNameText.setText(mTeamMemberModel.getName());
		mCellNumber = mTeamMemberModel.getCellphone();
		mMarkNumber = mTeamMemberModel.getBackNumber();
		
		if(mTeamMemberModel.getUserFace() != null){
			FinalBitmap mFinalBitmap = FinalBitmap.create(PlayerInfoActivity.this);
			//头像
			mFinalBitmap.display(photoImage, mTeamMemberModel.getUserFace());
		}
		
		//备注name
		if(null!=mTeamMemberModel.getNick() && !mTeamMemberModel.getNick().equals("")){
			mMarkNameItem.setContent(mTeamMemberModel.getNick());
		}else{
			mMarkNameItem.setVisibility(View.GONE);
		}
		if(null!=mTeamMemberModel.getCellphone()){
			mPhoneNumberItem.setContent(mTeamMemberModel.getCellphone());
			mPhoneNumberItem.setOnClickListener(this);
		}
		//备注号码
		if(null!=mTeamMemberModel.getBackNumber()&& !mTeamMemberModel.getBackNumber().equals("")){
			mMarkNumberItem.setContent(mTeamMemberModel.getBackNumber());
			mMarkNumberItem.setOnClickListener(this);
		}else{
			mMarkNumberItem.setVisibility(View.GONE);
		}
		// 绑定判断
		if(mTeamMemberModel.getTruckNumber() == null){
			mBindItem.setContent("未绑定");
		}else{
			mBindItem.setContent(mTeamMemberModel.getTruckNumber());
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_text:
			if(mTitleRight.getText().toString().equals("编辑")){
				mTitleRight.setText("完成");
				mMarkNameItem.setHint("请输入备注昵称");
				mMarkNumberItem.setHint("请输入备注电话号码");
				//电话图标隐藏
				mMarkNumberItem.getRightImage().setVisibility(View.GONE);
				mPhoneNumberItem.getRightImage().setVisibility(View.GONE);
				//备注名字
				mMarkNameItem.setVisibility(View.VISIBLE);
				mMarkNumberItem.setVisibility(View.VISIBLE);
				
				mBindItem.getRightImage().setVisibility(View.VISIBLE);
				mDelBtn.setVisibility(View.VISIBLE);
				mDelBtn.setOnClickListener(this);
				isModify = true;
			}else if(mTitleRight.getText().toString().equals("完成")){
				isModify = false;
				mTitleRight.setText("编辑");
				mBindItem.getRightImage().setVisibility(View.GONE);
				mDelBtn.setVisibility(View.GONE);
				modifyFinish(userId,mMarkNameItem.getEditContent(),mMarkNumberItem.getEditContent());
			}
			break;

		case R.id.del_btn:
			delPlayer(userId);
			break;
		case R.id.bind_item:
			if(isModify){
				Intent intent = new Intent(PlayerInfoActivity.this,BindTruckListActivity.class);
				intent.putExtra("userId", userId);
				startActivity(intent);
			}
			break;
		case R.id.phone_number_item:
			Util.call(PlayerInfoActivity.this, mCellNumber);
			break;
		case R.id.mark_number_item:
			Util.call(PlayerInfoActivity.this, mMarkNumber);
			break;
		default:
			break;
		}
	}
	/**
	 * 修改完成提交
	 * @param userId 被修改人的id
	 * @param nick   备注昵称
	 * @param backNumber 备注号码
	 */
	private void modifyFinish(String userId,String nick,String backNumber){
		AjaxParams params = new AjaxParams();
		params.put("subUserId", userId);
		params.put("nick", nick);
		params.put("backNumber", backNumber);
		DidiApp.getHttpManager().sessionPost(PlayerInfoActivity.this, finish_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				ToastUtil.show(PlayerInfoActivity.this, "提交成功");
				//通知车队信息页面更新
				Intent intent = new Intent();
				intent.setAction(CommonRes.TeamMemberModify);
				sendBroadcast(intent);
				finish();
			}
		});
	}
	/**
	 * 获取用户信息
	 * @param userId 传入用户id
	 */
	private void getData(String userId){
		AjaxParams params = new AjaxParams();
		params.put("subUserId", userId);
		DidiApp.getHttpManager().sessionPost(PlayerInfoActivity.this, info_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				try {
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					TeamMemberModel mData = JSON.parseObject(all,TeamMemberModel.class);
					initViewData(mData);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void delPlayer(String userId){
		AjaxParams params = new AjaxParams();
		params.put("subUserId", userId);
		DidiApp.getHttpManager().sessionPost(PlayerInfoActivity.this, del_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				ToastUtil.show(PlayerInfoActivity.this, "删除成功");
				//通知车队信息页面更新
				Intent intent = new Intent();
				intent.setAction(CommonRes.TeamMemberModify);
				sendBroadcast(intent);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
