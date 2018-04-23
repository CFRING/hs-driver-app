package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.TruckInfoAdapter;
import com.hongshi.wuliudidi.dialog.ListItemDeletingDialog;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.SearchTruckListModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class TruckInfoActivity extends Activity {
	private DiDiTitleView mTitle;

	private Button mAddTruckImage;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mTruckListview;
	private String truck_list_url = GloableParams.HOST + "uic/authentication/getTruckList.do?";
	private List<SearchTruckListModel> mTruckList = new ArrayList<SearchTruckListModel>();
	private long gmtModified;
	private boolean isEnd = false;
	private TruckInfoAdapter mTruckInfoAdapter;
	private LinearLayout mNoTruckLayout;
	/**
	 * 删除车辆接口
	 */
	private String truckDeletingUrl = GloableParams.HOST + "uic/authentication/deleteTruck.do?";
	private ListItemDeletingDialog mDeletingDialog;
	private TextView forbid_tip;

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
					ToastUtil.show(TruckInfoActivity.this, "删除失败");
					return;
				}
				AjaxParams params = new AjaxParams();
				params.put("truckId", deletedTruckId);
				DidiApp.getHttpManager().sessionPost(TruckInfoActivity.this, truckDeletingUrl, params, new ChildAfinalHttpCallBack() {
					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						ToastUtil.show(TruckInfoActivity.this, "删除失败");
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
		
		setContentView(R.layout.truck_info_activity);
		initViews();
	}

	private void initViews(){
		mTitle = (DiDiTitleView) findViewById(R.id.truck_info_item);
		mTitle.setTitle("我的车辆");
		mTitle.setBack(this);
		mAddTruckImage = (Button) findViewById(R.id.add_image);
		mNoTruckLayout = (LinearLayout) findViewById(R.id.no_truck_layout);
		forbid_tip = (TextView) findViewById(R.id.forbid_tip);
		TextView mTextView = mTitle.getRightTextView();
		mTextView.setText("添加");
		mTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TruckInfoActivity.this, AddTruckNewActivity.class);
				intent.putExtra("add_new_truck",true);
				startActivity(intent);
			}
		});

		mDeletingDialog = new ListItemDeletingDialog(TruckInfoActivity.this, R.style.data_filling_dialog, mHandler);
		mDeletingDialog.setCanceledOnTouchOutside(true);
		mDeletingDialog.setText("删除所选车辆", "取消");
		mDeletingDialog.getExampleImg().setVisibility(View.GONE);
		UploadUtil.setAnimation(mDeletingDialog, CommonRes.TYPE_BOTTOM, false);


		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.truck_listview);
		mTruckListview = mPullToRefreshListView.getRefreshableView();

		//注册刷新广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.RefreshTruck);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);

		loadData();
		mAddTruckImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TruckInfoActivity.this,AddTruckNewActivity.class);
				startActivity(intent);
			}
		});
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (mPullToRefreshListView.isHeaderShown()) {
					//刷新
					loadData();
				} else if (mPullToRefreshListView.isFooterShown()) {
					// 加载更多
					loadMore();
				}
			}
		});
		mTruckListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent intent = new Intent(TruckInfoActivity.this, AddTruckNewActivity.class);
				intent.putExtra("truckId", mTruckList.get(position-1).getTruckId());
				startActivity(intent);
			}
		});
		mTruckListview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				mDeletingDialog.setText("删除所选车辆", "取消");
				mDeletingDialog.setMsgNum(CommonRes.DELETE_TRUCK);
				mDeletingDialog.setItemId(mTruckList.get(position-1).getTruckId());
				mDeletingDialog.getExampleImg().setVisibility(View.GONE);
				UploadUtil.setAnimation(mDeletingDialog,CommonRes.TYPE_BOTTOM,false);
				mDeletingDialog.show();
				return true;
			}
		});
	}

	private void loadMore(){
		AjaxParams params = new AjaxParams();
		params.put("queryTime", ""+gmtModified);
		DidiApp.getHttpManager().sessionPost(TruckInfoActivity.this, truck_list_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPullToRefreshListView.onRefreshComplete();
				if(isEnd){
					Toast.makeText(TruckInfoActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.getString("items");
					List<SearchTruckListModel> mTruckList = JSON.parseArray(all,SearchTruckListModel.class);
					for(SearchTruckListModel model : mTruckList){
						if(!model.isAcceptOrder()){
							forbid_tip.setVisibility(View.VISIBLE);
							break;
						}else {
							forbid_tip.setVisibility(View.GONE);
						}
					}
					if(mTruckInfoAdapter!=null){
						mTruckInfoAdapter.addList(mTruckList);
					}
					gmtModified = mTruckList.get(mTruckList.size()-1).getGmtModified();
					isEnd = body.getBoolean("end");
					mTruckInfoAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
					mPullToRefreshListView.onRefreshComplete();
				}
			}
		});
	}
	private void loadData(){
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(TruckInfoActivity.this, truck_list_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPullToRefreshListView.onRefreshComplete();
				JSONObject jsonObject;
				if (mTruckList != null && mTruckList.size() > 0) {
					mTruckList.clear();
				}
				try {
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.getString("items");
					mTruckList = JSON.parseArray(all, SearchTruckListModel.class);
					//当没数据时显示添加车辆的布局
					if (mTruckList.size() == 0) {
						mPullToRefreshListView.setVisibility(View.GONE);
						mNoTruckLayout.setVisibility(View.VISIBLE);
					} else {
						mPullToRefreshListView.setVisibility(View.VISIBLE);
						mNoTruckLayout.setVisibility(View.GONE);
					}
					mTruckInfoAdapter = new TruckInfoAdapter(TruckInfoActivity.this, mTruckList);
					mTruckListview.setAdapter(mTruckInfoAdapter);
					if (mTruckList.size() > 0) {
						gmtModified = mTruckList.get(mTruckList.size() - 1).getGmtModified();
					}
					isEnd = body.getBoolean("end");
					for(SearchTruckListModel model : mTruckList){
						if(!model.isAcceptOrder()){
							forbid_tip.setVisibility(View.VISIBLE);
							return;
						}else {
							forbid_tip.setVisibility(View.GONE);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mPullToRefreshListView.onRefreshComplete();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}

}
