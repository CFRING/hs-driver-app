package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.LookAuctionTruckAdapter;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AuctionTrucksModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
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

public class AuctionTruckListActivity extends Activity {
	private ListView mTruckListView;
	private DiDiTitleView mTitle;
	private List<AuctionTrucksModel> mAuctionTruckList = new ArrayList<AuctionTrucksModel>();
	private LookAuctionTruckAdapter mLookAuctionTruckAdapter;
	private String truck_list_url = GloableParams.HOST + "carrier/transit/task/listtrucks.do?";
	private String mBidId;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AuctionTruckListActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AuctionTruckListActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.auction_truck_activity);
		mTitle = (DiDiTitleView) findViewById(R.id.auction_truck_list_title);
		mTitle.setBack(this);
		mTitle.setTitle("接单车辆");
		mTruckListView = (ListView) findViewById(R.id.auction_truck_listview);
		mBidId = getIntent().getExtras().getString("bidId");
		getData(mBidId);
		mTruckListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AuctionTruckListActivity.this,ReportActivity.class);
				intent.putExtra("bidItemId", mBidId);
				intent.putExtra("truckId", mAuctionTruckList.get(position).getTruckId());
				startActivity(intent);
			}
		});
	}
	private void getData(String id){
		AjaxParams params = new AjaxParams();
		params.put("bidItemId", id);
		DidiApp.getHttpManager().sessionPost(AuctionTruckListActivity.this, truck_list_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					mAuctionTruckList = JSON.parseArray(body,AuctionTrucksModel.class);
					mLookAuctionTruckAdapter = new LookAuctionTruckAdapter(AuctionTruckListActivity.this,mAuctionTruckList);
					mTruckListView.setAdapter(mLookAuctionTruckAdapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
