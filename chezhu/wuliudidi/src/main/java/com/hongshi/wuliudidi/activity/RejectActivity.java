package com.hongshi.wuliudidi.activity;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.RejectGridViewAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.RejectModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.MyItemView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class RejectActivity extends Activity{
	private GridView mRejectGridView;
	private RejectGridViewAdapter mAdapter;
	private MyItemView mContactItem;
	private DiDiTitleView mTitle;
	//派车单ID
	private String taskId;
	private final String url = GloableParams.HOST + "carrier/transit/task/voucherreview/find.do?";
	private TextView mRejectInfo;
	private TextView mRejectTime;
	private RejectModel mRejectModel;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("RejectActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("RejectActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		
		setContentView(R.layout.reject_activity);
		try {
			taskId = getIntent().getStringExtra("taskId");
		} catch (Exception e) {
			taskId = "";
		}
		mTitle = (DiDiTitleView) findViewById(R.id.reject_title);
		mTitle.setTitle("驳回原因");
		mTitle.setBack(this);
		
		mRejectInfo = (TextView) findViewById(R.id.reject_info);
		mRejectTime = (TextView) findViewById(R.id.reject_time);
		mRejectTime.setText("驳回时间： " + getResources().getString(R.string.unknown));
		
		mContactItem = (MyItemView)findViewById(R.id.my_contact_item);
		mContactItem.setItemName(getName(R.string.my_contact));
		mContactItem.setItemIcon(R.drawable.phone_icon);
		mContactItem.getAuthImage(4).setImageResource(R.drawable.call);
		
		mRejectGridView = (GridView) findViewById(R.id.reject_gridview);
		mContactItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.call(RejectActivity.this,getResources().getString(R.string.contact_number));
			}
		});
		
		getData();
	}
	
	private void getData(){
		if(taskId == null || taskId.length() <= 0 ){
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("taskId", taskId);
		DidiApp.getHttpManager().sessionPost(RejectActivity.this, url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");	
					mRejectModel = JSON.parseObject(all,RejectModel.class);
					setViewData();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
			}

		});
	}
	
	private void setViewData(){
		mAdapter = new RejectGridViewAdapter(RejectActivity.this, mRejectModel.getRefuseType());
		mRejectGridView.setAdapter(mAdapter);
		
		if(mRejectModel.getReviewRemark() != null){
			mRejectInfo.setText(mRejectModel.getReviewRemark());
		}
		if(mRejectModel.getGmtReview() != null){
			mRejectTime.setText("驳回时间： " + Util.formatDateSecond(mRejectModel.getGmtReview()));
		}
	}
	
	private String getName(int id){
		return getResources().getString(id);
	}
}
