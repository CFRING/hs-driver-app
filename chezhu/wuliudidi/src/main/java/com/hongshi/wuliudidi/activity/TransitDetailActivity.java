package com.hongshi.wuliudidi.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TransitTaskWeighDetailModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.FormItem;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class TransitDetailActivity extends Activity {
	private DiDiTitleView mTitleView;
	private TextView mRealLoad;
	private FormItem mTransitNumber, mWeighingFormNumber, mGoodsReceiver, mGoodsSender,mWeightLoss,
		mPlateNumber, mDriver, mWeightEmptycar, mWeightLoadedcar, mGoodsChecker, mGoodsWeigher;
	private TextView mPaymentTerms, mCreateTime;
	private String taskId = "";
	private String detail_url = GloableParams.HOST + "carrier/transit/task/weighdetail.do?";

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("TransitDetailActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("TransitDetailActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.transit_detail_activity);

		initViews();
		loadData(taskId);
	}

	private void initViews(){
		mTitleView = (DiDiTitleView) findViewById(R.id.transit_title);

		mTitleView.setBack(TransitDetailActivity.this);

		taskId = getIntent().getExtras().getString("taskId");
//		if(getIntent().getBooleanExtra("isTruckingOrderDetailsActivity", false)){
//			mTitleView.setTitle(getResources().getString(R.string.detail));
//		}else{
//			mTitleView.setTitle(getResources().getString(R.string.transit_detail));
//		}
		mTitleView.setTitle(getResources().getString(R.string.detail));
		mTransitNumber = (FormItem) findViewById(R.id.transit_number);
		mWeightLoss = (FormItem) findViewById(R.id.weight_loss);
		mTransitNumber.setItemName(getResources().getString(R.string.transit_number));
//		mTransitNumber.getButton(this);
		mTransitNumber.setItemValue(taskId);
//		mTransitNumber.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(!taskId.equals("")){
//					Intent intent = new Intent(TransitDetailActivity.this,TruckingOrderDetailsActivity.class);
//					intent.putExtra("transitId", taskId);
//					startActivity(intent);
//				}
//			}
//		});

		mRealLoad = (TextView) findViewById(R.id.real_load);

		mWeighingFormNumber = (FormItem) findViewById(R.id.weighing_form_number);
		mWeighingFormNumber.setItemName(getResources().getString(R.string.weighing_form_number));

		mGoodsReceiver = (FormItem) findViewById(R.id.goods_receiver);
		mGoodsReceiver.setItemName(getResources().getString(R.string.goods_receiver));


		mGoodsSender = (FormItem) findViewById(R.id.goods_sender);
		mGoodsSender.setItemName(getResources().getString(R.string.goods_sender));


		mPlateNumber = (FormItem) findViewById(R.id.plate_number);
		mPlateNumber.setItemName(getResources().getString(R.string.plate_number));

		mWeightLoss.setItemName("货损货差");

		mDriver = (FormItem) findViewById(R.id.driver);
		mDriver.setItemName(getResources().getString(R.string.driver));


		mWeightEmptycar = (FormItem) findViewById(R.id.weight_emptycar);
		mWeightEmptycar.setItemName(getResources().getString(R.string.weight_emptycar));


		mWeightLoadedcar = (FormItem) findViewById(R.id.weight_loadedcar);
		mWeightLoadedcar.setItemName(getResources().getString(R.string.weight_loadedcar));

		mGoodsChecker = (FormItem) findViewById(R.id.goods_checker);
		mGoodsChecker.setItemName(getResources().getString(R.string.goods_checker));
		mGoodsChecker.setItemValue("李检查");
		mGoodsChecker.setVisibility(View.GONE);

		mGoodsWeigher = (FormItem) findViewById(R.id.goods_weigher);
		mGoodsWeigher.setItemName(getResources().getString(R.string.goods_weigher));
		mGoodsWeigher.hideLine();
		mGoodsWeigher.setItemValue("张称重");
		mGoodsWeigher.setVisibility(View.GONE);

		mPaymentTerms = (TextView) findViewById(R.id.payment_terms);

		mCreateTime = (TextView) findViewById(R.id.create_time);
	}

	private void dataView(TransitTaskWeighDetailModel detailModel) {
		//运量
		mRealLoad.setText(Util.formatDoubleToString(detailModel.getRealAmount() ,detailModel.getUnitText()) + detailModel.getUnitText());
		//过磅单号
		mWeighingFormNumber.setItemValue(detailModel.getWeighCode());
		//收货单位
		mGoodsReceiver.setItemValue(detailModel.getRecvName());
		mGoodsSender.setItemValue(detailModel.getSendName());
		//车牌号码
		mPlateNumber.setItemValue(detailModel.getTruckNum());
		//司机
		mDriver.setItemValue(detailModel.getDriverName());
		//空车过磅
		mWeightEmptycar.setItemValue(Util.formatDoubleToString(detailModel.getEmptyWeight(), detailModel.getUnitText()) + detailModel.getUnitText());
		mWeightLoadedcar.setItemValue(Util.formatDoubleToString(detailModel.getFullWeight(), detailModel.getUnitText()) + detailModel.getUnitText());
		//付款方式
		mPaymentTerms.setText(detailModel.getPayTypeText());
		mCreateTime.setText(Util.formatDateSecond(detailModel.getGmtCreate()));
		//货损货差
		mWeightLoss.setItemValue(Util.formatDoubleToString(detailModel.getLossWeight(), detailModel.getUnitText()) + detailModel.getUnitText());
	}
	private void loadData(String taskId) {
		AjaxParams params = new AjaxParams();
		params.put("taskId", taskId);
		DidiApp.getHttpManager().sessionPost(TransitDetailActivity.this, detail_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					TransitTaskWeighDetailModel detailModel = JSON.parseObject(body, TransitTaskWeighDetailModel.class);
					if(detailModel != null){
						dataView(detailModel);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}
	
}
