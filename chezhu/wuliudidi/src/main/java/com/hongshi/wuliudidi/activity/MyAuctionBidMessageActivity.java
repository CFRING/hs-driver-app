package com.hongshi.wuliudidi.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AuctionBidMessageModel;
import com.hongshi.wuliudidi.model.AuctionTrucksModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @author huiyuan
 */
public class MyAuctionBidMessageActivity extends Activity {
	private DiDiTitleView mTitle;
	private AuctionItem bidPrice, bidWeight;
	private LinearLayout selectedTrucksLayout;
	private final String get_data_url = GloableParams.HOST + "carrier/bid/bidinfo.do?";
	private String bidItemId;
	private AuctionBidMessageModel bidMessageModel;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("MyAuctionBidMessageActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("MyAuctionBidMessageActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.my_auctionbid_message_activity);
		try {
			bidItemId = getIntent().getStringExtra("bidItemId");
		} catch (Exception e) {
		}
		
		mTitle = (DiDiTitleView)findViewById(R.id.title);
		mTitle.setBack(MyAuctionBidMessageActivity.this);
		bidPrice = (AuctionItem)findViewById(R.id.bid_price);
		bidWeight = (AuctionItem)findViewById(R.id.bid_weight);
		selectedTrucksLayout = (LinearLayout)findViewById(R.id.selected_trucks_layout);
		
		getData();
	}
	
	private void initView(){
		if(bidMessageModel == null){
			return;
		}
		
		mTitle.setTitle("我的接单信息");
		
		bidPrice.setName("接单出价");
		bidPrice.setContent(Util.formatDoubleToString(bidMessageModel.getBidPrice(), "元")
				+ "元/" 
				+ bidMessageModel.getSettleUnitText());
		bidPrice.setContentColor(getResources().getColor(R.color.black));
		
		bidWeight.setName("最大运量");
		bidWeight.setContent(
				Util.formatDoubleToString(bidMessageModel.getBidAmount(), bidMessageModel.getAssignUnit())
				+ bidMessageModel.getAssignUnitText());
		bidWeight.setContentColor(getResources().getColor(R.color.black));
		
		AuctionTrucksModel truckModel;
		for(int i = 0; i < bidMessageModel.getTrucks().size(); i++){
			truckModel = bidMessageModel.getTrucks().get(i);
			View view = View.inflate(MyAuctionBidMessageActivity.this, R.layout.selected_truck_item, null);
			TextView reportTrucks = (TextView) view.findViewById(R.id.report_trucks);
			if(i == 0){
				reportTrucks.setVisibility(View.VISIBLE);
			}
			TextView trucknumberText = (TextView) view.findViewById(R.id.truck_number);
			trucknumberText.setText(truckModel.getNumber());
			TextView drivernameText = (TextView) view.findViewById(R.id.driver_name);
			String truckDetailStr = "";
			if(truckModel.getTypeText() != null){
				truckDetailStr += truckModel.getTypeText() + " ";
			}
			if(truckModel.getCarriageText() != null && 
					!truckModel.getCarriageText().equals(getResources().getString(R.string.unlimited))){
				truckDetailStr += truckModel.getCarriageText()  + " ";
			}
			if(truckModel.getLengthText() != null){
				truckDetailStr += truckModel.getLengthText()  + " ";
			}
			if(truckModel.getCapacity() > 0){
				truckDetailStr += truckModel.getCapacity() + "吨" + " ";
			}
			if(truckModel.getCarryVolume() > 0){
				truckDetailStr += String.valueOf(truckModel.getCarryVolume()) + "立方米";
			}
			drivernameText.setText(truckDetailStr);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.height_33));
			selectedTrucksLayout.addView(view, params);
			
		}
	}
	
	private void getData(){
		if(bidItemId == null){
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("bidItemId", bidItemId);
		DidiApp.getHttpManager().sessionPost(MyAuctionBidMessageActivity.this, get_data_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				try {
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					bidMessageModel = JSON.parseObject(all, AuctionBidMessageModel.class);
					if(bidMessageModel != null){
						initView();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {

			}
		});
	}
}
