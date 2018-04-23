package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.DriverAdapter;
import com.hongshi.wuliudidi.dialog.ListItemDeletingDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.DriverBookListVO;
import com.hongshi.wuliudidi.model.DriverModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.NullDataView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class ChooseDriverActivity extends Activity implements OnClickListener{
	private List<DriverBookListVO> mDriverList;
	private ListView mDriverListView;
	private DriverAdapter mAdapter;
	private DiDiTitleView mTitle;
	private TextView mAddDriver;
	private final String get_driver_list = GloableParams.HOST + "carrier/mydrivers/listall.do?";
	private final String driver_delete = GloableParams.HOST + "carrier/mydrivers/delete.do?";
	private LinearLayout driverListLayout;
	private NullDataView mNullDataView;
	private DriverListType mType;
	private ListItemDeletingDialog mDeletingDialog;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("ChooseDriverActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("ChooseDriverActivity");
	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.DriverModify)) {
				getDriverList();
			}
		}
	};
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.DELETE_DRIVER:
				String deletedDriverId;
				try {
					deletedDriverId = msg.getData().getString("itemId");
				} catch (Exception e) {
					ToastUtil.show(ChooseDriverActivity.this, "删除失败");
					return;
				}
				AjaxParams params = new AjaxParams();
				params.put("id", deletedDriverId);
				DidiApp.getHttpManager().sessionPost(ChooseDriverActivity.this, driver_delete, params, new ChildAfinalHttpCallBack() {
					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						ToastUtil.show(ChooseDriverActivity.this, "删除失败");
					}
					
					@Override
					public void data(String t) {
						getDriverList();
					}
				});
				
			default:
				break;
			}
		}
	};
	
	@SuppressLint("ResourceAsColor") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.choose_driver_activity);

		initViews();
		getDriverList();
	}

	private void initViews(){
		try {
			mType = DriverListType.valueOf(getIntent().getExtras().getString("driverListType"));
		} catch (Exception e) {
			mType = DriverListType.ChooseDriver;
		}

		driverListLayout = (LinearLayout) findViewById(R.id.driver_list_layout);
		mDriverListView = (ListView) findViewById(R.id.driver_list);

		mNullDataView = (NullDataView) findViewById(R.id.no_data_layout);
		mNullDataView.setInfoHint("您还没有司机");
		mNullDataView.setInfo("请添加新司机");
		Button button = mNullDataView.getInfoImage();
		button.setId(R.id.button_id);
		button.setOnClickListener(this);
		button.setVisibility(View.VISIBLE);
		button.setBackgroundResource(R.drawable.solid_btn_style);
		button.setText("添加司机");
		button.setTextColor(getResources().getColor(R.color.white));


		mTitle = (DiDiTitleView) findViewById(R.id.title);
		if(mType == DriverListType.ChooseDriver){
			mTitle.setTitle(getResources().getString(R.string.choose_driver));
		}else if(mType == DriverListType.MyDriver){
			mTitle.setTitle(getResources().getString(R.string.my_team_info));
		}
		mTitle.setBack(ChooseDriverActivity.this);

		mAddDriver = mTitle.getRightTextView();
		mAddDriver.setId(R.id.add_id);
		mAddDriver.setText(getResources().getString(R.string.add));
		mAddDriver.setOnClickListener(this);

		if(mType == DriverListType.ChooseDriver){
			mDriverListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					DriverBookListVO model = mDriverList.get(position);
					Intent it = new Intent();
					it.putExtra("phoneNumber", model.getCellphone());
					it.putExtra("driverBookId", model.getDriverBookId());
					setResult(Activity.RESULT_OK, it);
					finish();
				}

			});
		}else if(mType == DriverListType.MyDriver){
			mDriverListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					DriverBookListVO model = mDriverList.get(position);
					Intent it = new Intent(ChooseDriverActivity.this, DriverInfoActivity.class);
					it.putExtra("driverBookId", model.getDriverBookId());
					it.putExtra("driverId", model.getDriverId());
					it.putExtra("isOwner",model.isOwner());
					startActivity(it);
				}
			});
			mDriverListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
											   View view, int position, long id) {
					if(mDriverList.get(position).getDriverId().equals(CommonRes.UserId)){
						ToastUtil.show(ChooseDriverActivity.this, "不能删除车主本人");
						return true;
					}
					mDeletingDialog = new ListItemDeletingDialog(ChooseDriverActivity.this, R.style.data_filling_dialog, mHandler);
					mDeletingDialog.setCanceledOnTouchOutside(true);
					mDeletingDialog.setText("删除所选司机", "取消");
					mDeletingDialog.setItemId(mDriverList.get(position).getDriverBookId());
					mDeletingDialog.setMsgNum(CommonRes.DELETE_DRIVER);
					mDeletingDialog.getExampleImg().setVisibility(View.GONE);
					UploadUtil.setAnimation(mDeletingDialog, CommonRes.TYPE_BOTTOM, false);
					mDeletingDialog.show();
					return true;
				}
			});
		}

		mDriverList = new ArrayList<DriverBookListVO>();

		//注册刷新广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.DriverModify);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}

	private void getDriverList(){
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(ChooseDriverActivity.this, get_driver_list, params, new ChildAfinalHttpCallBack() {
			
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				driverListLayout.setVisibility(View.GONE);
				mNullDataView.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void data(String t) {
				try {
					Log.d("huiyuan","司机信息 = " + t);
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					mDriverList = JSON.parseArray(all,DriverBookListVO.class);
					if(mDriverList.size() > 0){
						mAdapter = new DriverAdapter(ChooseDriverActivity.this, mDriverList);
						mDriverListView.setAdapter(mAdapter);
						
						driverListLayout.setVisibility(View.VISIBLE);
						mNullDataView.setVisibility(View.GONE);
						mAddDriver.setVisibility(View.VISIBLE);
					}else{
						driverListLayout.setVisibility(View.GONE);
						mNullDataView.setVisibility(View.VISIBLE);
						mAddDriver.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add_id:
		case R.id.button_id:
			Intent addDriverIntent = new Intent(ChooseDriverActivity.this,InvatePlayerActivity.class);
			addDriverIntent.putExtra("inviteType", "addDriver");
			startActivity(addDriverIntent);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
	
	public enum DriverListType{
		ChooseDriver, MyDriver;
	}
}
