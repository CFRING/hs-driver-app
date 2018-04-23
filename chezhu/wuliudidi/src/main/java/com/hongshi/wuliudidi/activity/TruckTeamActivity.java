package com.hongshi.wuliudidi.activity;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.TruckTeamMemberAdapter;
import com.hongshi.wuliudidi.dialog.ListItemDeletingDialog;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.MemberModel;
import com.hongshi.wuliudidi.model.TeamMemberModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class TruckTeamActivity extends Activity implements OnClickListener{

	private LinearLayout mNoTruckLayout,mShowHeaderLayot, mTeamMemberListLayout;
	private DiDiTitleView mTeamTitle;
	private TextView mInfoText;
	private ImageView mInviteImage;
	private AuctionItem mOneItem,mTwoItem,mThreeItem;
	private ListView mTeamMenbersListview;
	private TextView mContactHeaderText,mContactUsText;
	//车队长查看车队列表
	private String leader_url = GloableParams.HOST + "uic/fleet/getMembers.do?";
	//队员查看车队信息
	private String member_url = GloableParams.HOST + "uic/fleet/getFleetInfo.do?";
	
	private TextView mTitleAdd;
	List<TeamMemberModel> mData;
	private final int add_idd = 6745678;
	//删除队员接口
	private String memberDeletingUrl = GloableParams.HOST + "uic/fleet/deleteMember.do?";
	private ListItemDeletingDialog mDeletingDialog;
	TruckTeamMemberAdapter mAdapter;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("TruckTeamActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("TruckTeamActivity");
	}

	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.DELETE_TEAM_MEMBER:
				String deletedMemberId;
				try {
					deletedMemberId = msg.getData().getString("itemId");
				} catch (Exception e) {
					ToastUtil.show(TruckTeamActivity.this, "删除失败");
					return;
				}
				AjaxParams params = new AjaxParams();
				params.put("subUserId", deletedMemberId);
				DidiApp.getHttpManager().sessionPost(TruckTeamActivity.this, memberDeletingUrl, params, new ChildAfinalHttpCallBack() {
					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						ToastUtil.show(TruckTeamActivity.this, "删除失败");
					}
					@Override
					public void data(String t) {
						getFleetList();
					}
				});
			default:
				break;
			}
		}
	};
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.TeamMemberModify)) {
				init();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_team_activity);
		//获取标题
		mTeamTitle = (DiDiTitleView) findViewById(R.id.transport_ensure_title);
		mTitleAdd = mTeamTitle.getRightTextView();
		mTitleAdd.setId(add_idd);
		mTitleAdd.setText("添加");
		mTitleAdd.setOnClickListener(this);
		mTitleAdd.setVisibility(View.GONE);
		//显示车队长layout
		mShowHeaderLayot = (LinearLayout) findViewById(R.id.header_layout);
		//3个item
		mOneItem = (AuctionItem) findViewById(R.id.one_item);
		mTwoItem = (AuctionItem) findViewById(R.id.two_item);
		mThreeItem = (AuctionItem) findViewById(R.id.three_item);
		//离队的两种方式
		mContactHeaderText = (TextView) findViewById(R.id.contact_header_text);
		mContactUsText = (TextView) findViewById(R.id.contact_us_text);
		
		mTeamTitle.setBack(this);
		mTeamTitle.setTitle("车队信息");
		
		//注册刷新广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.TeamMemberModify);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		
		init();
	}
	void init(){
		if(CommonRes.organizationType == CommonRes.enterpriseType){
			//企业
			if(CommonRes.roleType == 1){
				//普通司机无权限
				//显示布局
				mNoTruckLayout = (LinearLayout) findViewById(R.id.no_truck_layout);
				mNoTruckLayout.setVisibility(View.VISIBLE);
				//提示信息
				mInfoText = (TextView) findViewById(R.id.info_text);
				mInfoText.setText("您还没有相关的车队信息");
				//邀请按钮
				mInviteImage = (ImageView) findViewById(R.id.add_image);
				mInviteImage.setVisibility(View.GONE);
			}else if(CommonRes.roleType == 2){
				//司机队长
				type(1);
			}else if(CommonRes.roleType == 3){
				//司机队员
				type(3);
			}
		}else if(CommonRes.organizationType == CommonRes.personType){
			//个人
			if(CommonRes.roleType == 1){
				//普通司机无权限
				//显示布局
				mNoTruckLayout = (LinearLayout) findViewById(R.id.no_truck_layout);
				mNoTruckLayout.setVisibility(View.VISIBLE);
				//提示信息
				mInfoText = (TextView) findViewById(R.id.info_text);
				mInfoText.setText("您还没有相关的车队信息");
				//邀请按钮
				mInviteImage = (ImageView) findViewById(R.id.add_image);
				mInviteImage.setVisibility(View.GONE);
			}else if(CommonRes.roleType == 2){
				//司机队长
				type(1);
			}else if(CommonRes.roleType == 3){
				//司机队员
				type(2);
			}
		}
	}
	private void type(int type){
		switch (type) {
		case 0:
			//显示布局
			mNoTruckLayout = (LinearLayout) findViewById(R.id.no_truck_layout);
			mNoTruckLayout.setVisibility(View.VISIBLE);
			//提示信息
			mInfoText = (TextView) findViewById(R.id.info_text);
			mInfoText.setText("暂无车队人员，赶快去邀请队友吧");
			//邀请按钮
			mInviteImage = (ImageView) findViewById(R.id.add_image);
			mInviteImage.setImageResource(R.drawable.invited_players);
			mInviteImage.setOnClickListener(this);
			break;
		case 1:	
			//显示布局
			mTeamMemberListLayout = (LinearLayout) findViewById(R.id.team_member_list_layout);
			mTeamMemberListLayout.setVisibility(View.VISIBLE);
			//设置队员列表信息
			mTeamMenbersListview = (ListView) findViewById(R.id.member_list);
			getFleetList();
			mTeamMenbersListview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(TruckTeamActivity.this,PlayerInfoActivity.class);
					intent.putExtra("userId", mData.get(position).getUserId());
					startActivity(intent);
				}
			});
			mTeamMenbersListview.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					mDeletingDialog = new ListItemDeletingDialog(TruckTeamActivity.this, R.style.data_filling_dialog, mHandler);
					mDeletingDialog.setCanceledOnTouchOutside(true);
					mDeletingDialog.setText("删除所选队员", "取消");
					mDeletingDialog.setItemId(mData.get(position).getUserId());
					mDeletingDialog.setMsgNum(CommonRes.DELETE_TEAM_MEMBER);
					setAnimation(mDeletingDialog);
					mDeletingDialog.getExampleImg().setVisibility(View.GONE);
					UploadUtil.setAnimation(mDeletingDialog, CommonRes.TYPE_BOTTOM, false);
					mDeletingDialog.show();
					return true;
				}
			});
			break;
		case 2:
			personalLookInfo();
			break;
		case 3:
			personalLookInfo();
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//添加
		case R.id.add_image:
		case add_idd:
			Intent invate_intent = new Intent(this,InvatePlayerActivity.class);
			startActivity(invate_intent);
			break;
		default:
			break;
		}
	}
	//车队员显示界面
	private void showTeamView(MemberModel	mMemberModel) {
		if(mMemberModel.getLeaderType() == 1){
			mShowHeaderLayot.setVisibility(View.VISIBLE);
			mOneItem.setName("车队队长");
			mOneItem.setContent(mMemberModel.getName());
			mTwoItem.setName("手机号码");
			mTwoItem.setContent(mMemberModel.getCellphone());
			mContactHeaderText.setText("1, 联系车队长删除您的个人信息");
		}else if(mMemberModel.getLeaderType() == 2){
			mShowHeaderLayot.setVisibility(View.VISIBLE);
			mOneItem.setName("企业名称");
			mOneItem.setContent(mMemberModel.getEnterpriseName());
			mTwoItem.setName("企业法人");
			mTwoItem.setContent(mMemberModel.getName());
			
			mContactHeaderText.setText("1, 联系企业删除您的个人信息");
		}
		mThreeItem.setName("绑定车辆");
		if(mMemberModel.getTruckNumber() != null){
			mThreeItem.setContent(mMemberModel.getTruckNumber());
		}else{
			mThreeItem.setContent("未绑定");
		}
		mContactUsText.setText("2, 联系客服：400-000-1888");
	}
	//个体车队队员查看信息
	private void personalLookInfo(){
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(TruckTeamActivity.this, member_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				String body;
				try {
					JSONObject jsonObject = new JSONObject(t);
					body = jsonObject.getString("body");
					MemberModel	mMemberModel = JSON.parseObject(body,MemberModel.class);
					if(mMemberModel != null)
					{
						showTeamView(mMemberModel);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

		});
	}
	//队长查看车队列表
	private void getFleetList(){
		AjaxParams params = new AjaxParams();
		params.put("relation", "1");
		DidiApp.getHttpManager().sessionPost(TruckTeamActivity.this, leader_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
					try {
						JSONObject jsonObject = new JSONObject(t);
						String all = jsonObject.getString("body");
//						LogUtil.myLog("lihe", "all=="+all);
						mData = JSON.parseArray(all,TeamMemberModel.class);
						if(mData.size()>0){
							mTitleAdd.setVisibility(View.VISIBLE);
							mAdapter = new TruckTeamMemberAdapter(TruckTeamActivity.this, mData);
							mTeamMenbersListview.setAdapter(mAdapter);
						}else{
							//没有数据
							type(0);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
	
	private void setAnimation(Dialog dialog){
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.dialog);
		window.getDecorView().setPadding(0, 0, 0, 0);
		
		WindowManager.LayoutParams lp = window.getAttributes();
		        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		        window.setAttributes(lp);
		//此处可以设置dialog显示的位置
		        window.setGravity(Gravity.CENTER);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
