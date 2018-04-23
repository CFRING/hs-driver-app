package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.AssignTaskAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TransitPlanModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author huiyuan
 */
public class PlanActivity extends Activity{
	private String assign_all_url = GloableParams.HOST + "carrier/transit/plan/listall.do";
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mTaskListview;
	private List<TransitPlanModel> mAllPlanList = new ArrayList<TransitPlanModel>();
	//加载更多，是否最后一页，在请求里去掉加载的状态
	private boolean assign_tag = false;
	private boolean isAssignEnd = false;
	private AssignTaskAdapter mAssignTaskAdapter;
	private List<TransitPlanModel> mTransitPlanList = new ArrayList<TransitPlanModel>();
	private List<TransitPlanModel> mMoreTransitPlanList = new ArrayList<TransitPlanModel>();
	private DiDiTitleView  mTitle;
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.NewTask)) {
				//刷新
				assignloadData(assign_all_url);
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("PlanActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("PlanActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.plan_activity);
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setBack(this);
		mTitle.setTitle("派车历史");
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.task_listview);
		
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");  
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");  
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多"); 
		
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");  
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");  
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新"); 
		
		mTaskListview = mPullToRefreshListView.getRefreshableView();
		initPlan();
		assignloadData(assign_all_url);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.NewTask);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}
	private void initPlan(){
			mTaskListview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(PlanActivity.this,WinBidActivity.class);
					intent.putExtra("AuctionId", mAllPlanList.get(position-1).getBidItemId());
					startActivity(intent);
				}
			});
			
			mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (mPullToRefreshListView.isRefreshing()) {
						if (mPullToRefreshListView.isHeaderShown()) {
							//刷新
							assignloadData(assign_all_url);
						} else if (mPullToRefreshListView.isFooterShown()) {
							// 加载更多
							if(isAssignEnd){
								assign_tag = true;
								Toast.makeText(PlanActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
								CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
								closeRefreshTask.execute();
								return;
							}
							assignloadMoreData(assign_all_url);
						}
					}
				}
			});
	}
	private void assignloadData(String assign_url) {
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(PlanActivity.this, assign_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				isAssignEnd = false;
				assign_tag = false;
				mPullToRefreshListView.onRefreshComplete();
				JSONObject jsonObject;
				if (mTransitPlanList.size() > 0){
					mTransitPlanList.clear();	
				}
				if (mAllPlanList.size() > 0){
					mAllPlanList.clear();	
				}
				try {
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.getString("items");
					mTransitPlanList = JSON.parseArray(all,TransitPlanModel.class);
					mAllPlanList.addAll(mTransitPlanList);
					mAssignTaskAdapter = new AssignTaskAdapter(PlanActivity.this, mTransitPlanList);
					mTaskListview.setAdapter(mAssignTaskAdapter);
					isAssignEnd = body.getBoolean("end");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPullToRefreshListView.onRefreshComplete();
			}
		});
	}
	private void assignloadMoreData(String url) {
		AjaxParams params = new AjaxParams();
		if(mMoreTransitPlanList.size()>0){
			params.put("last", mAllPlanList.get(mAllPlanList.size()-1).getPlanId());
		}
		DidiApp.getHttpManager().sessionPost(PlanActivity.this, url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				if(assign_tag){
					mPullToRefreshListView.onRefreshComplete();
				}else{
					try {
						mPullToRefreshListView.onRefreshComplete();
						jsonObject = new JSONObject(t);
						JSONObject body = jsonObject.getJSONObject("body");
						String all = body.getString("items");
						mMoreTransitPlanList = JSON.parseArray(all,TransitPlanModel.class);
						mAssignTaskAdapter.addList(mMoreTransitPlanList);
						mAllPlanList.addAll(mMoreTransitPlanList);
						mAssignTaskAdapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
						isAssignEnd = body.getBoolean("end");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPullToRefreshListView.onRefreshComplete();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
