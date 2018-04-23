package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.TruckListAdapter;
import com.hongshi.wuliudidi.impl.TruckIdCallBack;
import com.hongshi.wuliudidi.model.AuctionOfferModel;
import com.hongshi.wuliudidi.model.AuctionOfferModel.TruckMode;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

/**
 * @author huiyuan
 */
public class ChooseTruckActivity extends Activity {

	private DiDiTitleView mDiDiTitleView;
	private ListView mTruckListview;
	private List<TruckMode> mTruckList = new ArrayList<TruckMode>();
	private TruckListAdapter mTruckListAdapter;
	//多个报备车辆的ID数组
	private List<String> mTruckIdsList = new ArrayList<String>();
	//多个报备车辆的ID数组
	private List<String> mTruckIdsList1 = new ArrayList<String>();
	//多个报备车辆的ID数组
	private List<String> mTruckIdsNumber = new ArrayList<String>();
	//多个报备车辆的司机姓名
	private List<String> mDriversName = new ArrayList<String>();
	private Button mSure;
	private CheckBox mSelectAll;
	private int truckNum = 0;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("ChooseTruckActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("ChooseTruckActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.choose_truck_activity);
		initViews();
	}

	private void initViews(){
		mDiDiTitleView = (DiDiTitleView) findViewById(R.id.choose_truck_title);
		mDiDiTitleView.setBack(this);
		mDiDiTitleView.setTitle("选择车辆");
		mTruckListview = (ListView) findViewById(R.id.choose_truck_listview);
		mSure = (Button) findViewById(R.id.auction_sure);
		mSelectAll = (CheckBox) findViewById(R.id.check);
		AuctionOfferModel mAuctionOfferModel = (AuctionOfferModel) getIntent().getSerializableExtra("mode");
		mTruckList = mAuctionOfferModel.getTrucks();

		mTruckIdsList1 = getIntent().getStringArrayListExtra("truckId");
		truckNum = mTruckIdsList1.size();
		mSure.setText("确认车辆("+ truckNum +")");
		if(mTruckIdsList1.size() == mTruckList.size()){
			mSelectAll.setChecked(true);
			mSure.setText("确认车辆("+ truckNum +")");
		}
		if(mTruckIdsList1.size()>0){
			for(int i=0;i<mTruckIdsList1.size();i++){
				for(int j=0;j<mTruckList.size();j++){
					if(mTruckIdsList1.get(i).equals(mTruckList.get(j).getTruckId())){
						mTruckIdsList.add(mTruckList.get(j).getTruckId());
						mTruckIdsNumber.add(mTruckList.get(j).getTruckNumber());
						mDriversName.add(mTruckList.get(j).getDriverName());
					}
				}
			}
		}
		//添加全选功能
		mSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(mTruckListAdapter != null){
						mTruckIdsList.clear();
						mTruckIdsNumber.clear();
						mDriversName.clear();
						for(int i=0;i<mTruckList.size();i++){
							mTruckIdsList.add(mTruckList.get(i).getTruckId());
							mTruckIdsNumber.add(mTruckList.get(i).getTruckNumber());
							mDriversName.add(mTruckList.get(i).getDriverName());
						}
						truckNum = mTruckList.size();
						mSure.setText("确认车辆("+ truckNum +")");
						mTruckListAdapter.setSelect(true);
						mTruckListAdapter.setNoneSelect(false);
						mTruckListAdapter.notifyDataSetChanged();
					}
				}else{
					if(truckNum == mTruckList.size()){
						mTruckIdsList.clear();
						mTruckIdsNumber.clear();
						mDriversName.clear();
						mTruckListAdapter.setNoneSelect(true);
						mTruckListAdapter.setSelect(false);
						mTruckListAdapter.notifyDataSetChanged();
						mSure.setText("确认车辆("+ 0 +")");
						truckNum = 0;
					}
				}
			}
		});
		mTruckListAdapter = new TruckListAdapter(ChooseTruckActivity.this,mTruckIdsList,mTruckList,new TruckIdCallBack() {
			@Override
			public void addId(String id,int position,String truckNumber) {
				if(!mSelectAll.isChecked()){
					mTruckIdsList.add(id);
					mTruckIdsNumber.add(truckNumber);
					mDriversName.add(mTruckList.get(position).getDriverName());
					truckNum = truckNum + 1;
					mSure.setText("确认车辆("+ truckNum +")");
					if(truckNum == mTruckList.size()){
						mSelectAll.setChecked(true);
					}
				}
			}

			@Override
			public void minusId(String id,int position,String truckNumber) {
				for(int i=0;i<mTruckIdsList.size();i++){
					if(mTruckIdsList.get(i).equals(id)){
						mTruckIdsList.remove(i);
						mTruckIdsNumber.remove(i);
						mDriversName.remove(i);
					}
				}
				truckNum = truckNum - 1;
				if(truckNum>0){
					mSure.setText("确认车辆("+ truckNum +")");
				}else{
					mSure.setText("确认车辆("+ 0 +")");
					truckNum = 0;
				}
				mSelectAll.setChecked(false);
			}
		});
		mTruckListview.setAdapter(mTruckListAdapter);

		mSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(CommonRes.GetAuctionTruckList);
				intent.putStringArrayListExtra("truckId", (ArrayList<String>) mTruckIdsList);
				intent.putStringArrayListExtra("truckNumber", (ArrayList<String>) mTruckIdsNumber);
				intent.putStringArrayListExtra("driverName", (ArrayList<String>) mDriversName);
				sendBroadcast(intent);
				finish();
			}
		});
	}
}
