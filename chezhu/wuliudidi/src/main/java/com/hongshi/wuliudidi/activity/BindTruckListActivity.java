package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.BindTruckListAdapter;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.SearchTruckListModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author huiyuan
 */
public class BindTruckListActivity extends Activity {
	private String userId;
	//获取可以绑定的车辆列表
	private String bind_truck_list_url = GloableParams.HOST + "uic/fleet/fetchTeamTruckList.do?";
	//队长给队员分配车辆
	private String bind_url = GloableParams.HOST + "uic/fleet/distributeTeamMemberTruck.do?";
	private DiDiTitleView mTitle;
	private ListView mTruckListView;
	private List<SearchTruckListModel> mTruckList = new ArrayList<SearchTruckListModel>();

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("BindTruckListActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("BindTruckListActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.bind_truck_list_activity);
		userId = getIntent().getStringExtra("userId");
		mTitle = (DiDiTitleView) findViewById(R.id.bind_title);
		mTitle.setTitle("车辆信息");
		mTitle.setBack(this);
		mTruckListView = (ListView) findViewById(R.id.truck_listview);
		getTruckList();
		mTruckListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mTruckList.size()>0){
					bindTruck(userId,mTruckList.get(position).getTruckId());
				}
			}
			
		});
	}

	/**
	 * 给队员分配车辆
	 */
	private void bindTruck(final String userId,final String truckId){
		AjaxParams params = new AjaxParams();
		params.put("invitedUserId", userId);
		params.put("truckId", truckId);
		DidiApp.getHttpManager().sessionPost(BindTruckListActivity.this, bind_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				ToastUtil.show(BindTruckListActivity.this, "绑定成功");
				
				String truckNumber = "";
				for(int i = 0; i < mTruckList.size(); i++){
					if(mTruckList.get(i).getTruckId() == truckId){
						truckNumber = mTruckList.get(i).getTruckNumber();
						break;
					}
				}
				//绑定成功发送广播刷新
				Intent intent = new Intent();
				intent.setAction(CommonRes.TeamMemberModify);
				intent.putExtra("teamMemberUserId", userId);
				intent.putExtra("truckNumber", truckNumber);
				sendBroadcast(intent);
				finish();
			}
		});
	}
	//获取车辆列表
	private void getTruckList(){
		AjaxParams params = new AjaxParams();
		params.put("invitedUserId", userId);
		DidiApp.getHttpManager().sessionPost(BindTruckListActivity.this, bind_truck_list_url,params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					mTruckList = JSON.parseArray(body,SearchTruckListModel.class);
					BindTruckListAdapter adapter = new BindTruckListAdapter(BindTruckListActivity.this, mTruckList);
					mTruckListView.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
		});
	}
}
