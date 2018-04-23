package com.hongshi.wuliudidi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.TruckInfoForDriverAdapter;
import com.hongshi.wuliudidi.dialog.ListItemDeletingDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.SearchTruckListModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 * 司机端我的车辆
 */
public class TruckInfoForDriverActivity extends Activity {
	private DiDiTitleView mTitle;

	private ListView mTruckListview;
	private String truck_list_url = GloableParams.HOST + "/carrier/truck/list.do";
	private List<SearchTruckListModel> mTruckList = new ArrayList<SearchTruckListModel>();
	private TruckInfoForDriverAdapter mTruckInfoForDriverAdapter;
	private LinearLayout mNoTruckLayout;
	//删除车辆接口
	private String truckDeletingUrl = GloableParams.HOST + "uic/authentication/deleteTruck.do?";
	private ListItemDeletingDialog mDeletingDialog;
	private PromptManager promptManager = new PromptManager();

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("TruckInfoActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("TruckInfoActivity");
	}

	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.DELETE_TRUCK:
				String deletedTruckId;
				try {
					deletedTruckId = msg.getData().getString("itemId");
				} catch (Exception e) {
					ToastUtil.show(TruckInfoForDriverActivity.this, "删除失败");
					return;
				}
				AjaxParams params = new AjaxParams();
				params.put("truckId", deletedTruckId);
				DidiApp.getHttpManager().sessionPost(TruckInfoForDriverActivity.this, truckDeletingUrl, params, new ChildAfinalHttpCallBack() {
					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						ToastUtil.show(TruckInfoForDriverActivity.this, "删除失败");
					}
					
					@Override
					public void data(String t) {
						//删除车辆可能使车主变为无车用户，所以需要刷新myfragment
						Intent broadcastIntent = new Intent();
						broadcastIntent.setAction(CommonRes.RefreshUserInfo);
						sendBroadcast(broadcastIntent);
						
						loadData();
					}
				});
				
				break;
			default:
				break;
			}
		}
	};
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.RefreshTruck)) {
				loadData();
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		
		setContentView(R.layout.truck_info_activity_for_driver);
		initViews();
		loadData();
	}

	private void initViews(){
		mTitle = (DiDiTitleView) findViewById(R.id.truck_info_item);
		mTitle.setTitle("我的车辆");
		mTitle.setBack(this);
		mNoTruckLayout = (LinearLayout) findViewById(R.id.no_truck_layout);
//		TextView mTextView = mTitle.getRightTextView();
//		mTextView.setText("添加");
//		mTextView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(TruckInfoForDriverActivity.this, AddTruckNewActivity.class);
//				startActivity(intent);
//			}
//		});

		mDeletingDialog = new ListItemDeletingDialog(TruckInfoForDriverActivity.this, R.style.data_filling_dialog, mHandler);
		mDeletingDialog.setCanceledOnTouchOutside(true);
		mDeletingDialog.setText("删除所选车辆", "取消");
		mDeletingDialog.getExampleImg().setVisibility(View.GONE);
		UploadUtil.setAnimation(mDeletingDialog, CommonRes.TYPE_BOTTOM, false);


		mTruckListview = (ListView) findViewById(R.id.list_view);

		//注册刷新广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.RefreshTruck);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);

//		mTruckListview.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent = new Intent(TruckInfoForDriverActivity.this, AddTruckNewActivity.class);
//				intent.putExtra("truckId", mTruckList.get(position).getTruckId());
//				startActivity(intent);
//			}
//		});
//		mTruckListview.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				mDeletingDialog.setText("删除所选车辆", "取消");
//				mDeletingDialog.setMsgNum(CommonRes.DELETE_TRUCK);
//				mDeletingDialog.setItemId(mTruckList.get(position-1).getTruckId());
//				mDeletingDialog.show();
//				return true;
//			}
//		});

		promptManager.showProgressDialog1(this, "加载中");
	}

	private void loadData(){
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(TruckInfoForDriverActivity.this, truck_list_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				promptManager.closeProgressDialog();
				JSONObject jsonObject;
				if (mTruckList != null && mTruckList.size() > 0) {
					mTruckList.clear();
				}
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					mTruckList = JSON.parseArray(body, SearchTruckListModel.class);
					//当没数据时显示添加车辆的布局
					if (mTruckList.size() == 0) {
						mTruckListview.setVisibility(View.GONE);
						mNoTruckLayout.setVisibility(View.VISIBLE);
					} else {
						mTruckListview.setVisibility(View.VISIBLE);
						mNoTruckLayout.setVisibility(View.GONE);
					}
					mTruckInfoForDriverAdapter = new TruckInfoForDriverAdapter(TruckInfoForDriverActivity.this, mTruckList);
					mTruckListview.setAdapter(mTruckInfoForDriverAdapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				promptManager.closeProgressDialog();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
