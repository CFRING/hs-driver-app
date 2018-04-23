package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.ReportListAdapter;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AppMapVO;
import com.hongshi.wuliudidi.model.TruckTransitTaskRecordVO;
import com.hongshi.wuliudidi.model.TruckTransitTaskSumVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class ReportActivity extends Activity {
	private ExpandableListView mReportListView;
	private DiDiTitleView mTitle;
	private String history_url = GloableParams.HOST + "carrier/transit/task/truck/history.do?";
	private List<List<TruckTransitTaskRecordVO>> childDataList1 = new ArrayList<List<TruckTransitTaskRecordVO>>();
	private TextView mTruckNumberText,mTruckAccontText,mTruckInfoText;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("ReportActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("ReportActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.report_activity);
		initViews();
	}

	private void initViews(){
		mTitle = (DiDiTitleView) findViewById(R.id.report_title);
		mTitle.setBack(this);
		mTitle.setTitle("运输报表");
		mReportListView = (ExpandableListView) findViewById(R.id.report_listview);
		mReportListView.setGroupIndicator(null);

		mTruckNumberText = (TextView) findViewById(R.id.truck_number);
		mTruckAccontText = (TextView) findViewById(R.id.goods_accont);
		mTruckInfoText = (TextView) findViewById(R.id.truck_info);

		String bidItemId = getIntent().getStringExtra("bidItemId");
		String truckId = getIntent().getStringExtra("truckId");
		loadData(bidItemId,truckId);
		mReportListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
										int groupPosition, int childPosition, long id) {
				Intent transit_intent = new Intent(ReportActivity.this,TransitDetailActivity.class);
				String taskId = childDataList1.get(groupPosition).get(childPosition).getTaskId();
				transit_intent.putExtra("taskId", taskId);
				startActivity(transit_intent);
				return true;
			}
		});
	}

	private void loadData(String bidItemId,String truckId){
		AjaxParams params = new AjaxParams();
		params.put("bidItemId", bidItemId);
		params.put("truckId", truckId);
		DidiApp.getHttpManager().sessionPost(ReportActivity.this, history_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				List<String> groups = new ArrayList<String>();
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					TruckTransitTaskSumVO mTruckTransitTaskSumVO = JSON.parseObject(body, TruckTransitTaskSumVO.class);
					mTruckNumberText.setText(mTruckTransitTaskSumVO.getNumber());
					mTruckAccontText.setText(Util.formatDoubleToString(mTruckTransitTaskSumVO.getTotalAmount(),
							mTruckTransitTaskSumVO.getUnitText()));
					String truckInfoStr = "";
					if(mTruckTransitTaskSumVO.getTypeText() != null && !mTruckTransitTaskSumVO.getTypeText().equals("")
							&& !mTruckTransitTaskSumVO.getTypeText().equals(getString(R.string.unlimited))){
						//车型
						truckInfoStr += mTruckTransitTaskSumVO.getTypeText() + " ";
					}
					if(mTruckTransitTaskSumVO.getCarriageText() != null && !mTruckTransitTaskSumVO.getCarriageText().equals("")
							&& !mTruckTransitTaskSumVO.getCarriageText().equals(getResources().getString(R.string.unlimited))){
						//车厢
						truckInfoStr += mTruckTransitTaskSumVO.getCarriageText() + " ";
					}
					if(mTruckTransitTaskSumVO.getLengthText() != null && !mTruckTransitTaskSumVO.getLengthText().equals("")
							&& !mTruckTransitTaskSumVO.getLengthText().equals(getResources().getString(R.string.unlimited))){
						//车长
						truckInfoStr += mTruckTransitTaskSumVO.getLengthText() + " ";
					}
					if(mTruckTransitTaskSumVO.getCapacity() > 0){
						//载重
						truckInfoStr += Util.formatDoubleToString(mTruckTransitTaskSumVO.getCapacity(), "吨") + "吨" + " ";
					}
					if(mTruckTransitTaskSumVO.getCarryVolume() > 0){
						truckInfoStr += Util.formatDoubleToString(mTruckTransitTaskSumVO.getCarryVolume(), "立方米") + "立方米";
					}
					mTruckInfoText.setText(truckInfoStr);
					
					AppMapVO recordList = mTruckTransitTaskSumVO.getRecordList();
					Map<Long ,List<TruckTransitTaskRecordVO>> items = recordList.getItems();
					Set<?> set = items.entrySet();
					for(Iterator<?> iter = set.iterator(); iter.hasNext();){
					   List<TruckTransitTaskRecordVO> childDataList = new ArrayList<TruckTransitTaskRecordVO>();
					   Map.Entry entry = (Map.Entry)iter.next();
					   String key = (String)entry.getKey();
					   groups.add(key);
					   List value = items.get(key);
					   for(int i = 0; i < value.size(); i++){
						   childDataList.add(getChildData(""+value.get(i)));
					   }
					   childDataList1.add(childDataList);
					  }
					if(groups.size() > 0 && childDataList1.size() > 0){
						ReportListAdapter adapter = new ReportListAdapter(ReportActivity.this, mReportListView,groups,childDataList1);
						mReportListView.setAdapter(adapter);
						for (int i = 0; i < groups.size(); i++) {
							mReportListView.expandGroup(i);
						}
					}
					recordList.isEnd();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private TruckTransitTaskRecordVO getChildData(String childData){
		TruckTransitTaskRecordVO mTruckTransitTaskRecordVO = JSON.parseObject(childData, TruckTransitTaskRecordVO.class);
		return mTruckTransitTaskRecordVO;
	}
}
