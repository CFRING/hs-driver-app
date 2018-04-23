package com.hongshi.wuliudidi.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.SeeTruckdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TruckListModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * @author huiyuan
 */
public class SeeTruckActivity extends Activity {
	private DiDiTitleView mTitle;
	private ListView mListView;
	private String url = GloableParams.HOST + "carrier/transit/task/plantrucks.do?";

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("SeeTruckActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("SeeTruckActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.see_truck_activity);
		mListView = (ListView) findViewById(R.id.truck_listview);
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setTitle("已派车辆");
		mTitle.setBack(this);
		String planId = getIntent().getStringExtra("planId");
		getData(planId);
	}
	private void getData(String planId) {
	
		AjaxParams params = new AjaxParams();
		params.put("planId", planId);
		DidiApp.getHttpManager().sessionPost(SeeTruckActivity.this, url, params, new ChildAfinalHttpCallBack() {
			
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
			}
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					List<TruckListModel> mList = JSON.parseArray(all,TruckListModel.class);
					SeeTruckdapter adapter = new SeeTruckdapter(SeeTruckActivity.this, mList);
					mListView.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
