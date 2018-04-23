package com.hongshi.wuliudidi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AuctionDoBid;
import com.hongshi.wuliudidi.model.AuctionOfferModel;
import com.hongshi.wuliudidi.model.AuctionOfferModel.Statistic;
import com.hongshi.wuliudidi.model.AuctionOfferModel.TruckMode;
import com.hongshi.wuliudidi.model.BillTmpltVo;
import com.hongshi.wuliudidi.model.SettleModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.InputLimitTextWatcher;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.GoodsDetailsView;
import com.hongshi.wuliudidi.view.MagnificentChart;
import com.hongshi.wuliudidi.view.MagnificentChartItem;
import com.hongshi.wuliudidi.view.NoScrollGridView;
import com.hongshi.wuliudidi.view.PriceItemView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AuctionOfferActivity extends Activity implements OnClickListener {

	private DiDiTitleView mOfferTitle;
//	private GoodsDetailsView mGoodsView;
	private TextView mSureAuction;
	private RelativeLayout price_type1_container,price_type2_container,
			price_type3_container,price_type4_container,price_type5_container;
	private MagnificentChart chart = null;
	// 查看竞拍详情
	private String auction_offer_url = GloableParams.HOST + "carrier/auction/tobid.do?";
	private String mAuctionId = "",outer_goods_amount = "";
//	private List<AuctionOfferModel> AllAccount = new ArrayList<AuctionOfferModel>();
	private List<BillTmpltVo> billTmpltVoList = new ArrayList<>();
	private AuctionOfferModel mAuctionOfferModel = null;
	// 竞拍出价
	private String auction_url = GloableParams.HOST + "carrier/bid/dobid.do?";
	private List<TruckMode> mTruckList = new ArrayList<AuctionOfferModel.TruckMode>();
	// 多个报备车辆的ID数组
	private volatile List<String> mTruckIdsList = new ArrayList<String>();
	// 多个报备车辆的车牌号数组
	private List<String> mTruckNumber = new ArrayList<String>();
	// 竞拍id
	private String auctionId;
	private EditText mAuctionPriceEdit;
	private EditText mMaxTrafficEdit;
	private String mAuctionPrice, mMaxTraffic;
	private TextView mAuctionUnit, mTransitUnit;
	private TextView one_truck_drivername;
//	private LinearLayout mAddTruckLayout,contract_container;
	private PriceItemView mFirstItem,mSecondItem,mThirdItem;
	private String unitext="";
	private RelativeLayout mAuctionOfferLayout;
	private LinearLayout mChatLayout;
//	private boolean isFirst = true;
//	private ImageView mRightImage;
//隐藏价格标记
	private boolean isHidePrice = true;
	private CheckBox check_box1,check_box2,check_box3,check_box4,check_box5,check_box_protocol;
	private TextView price_text1,price_text2,price_text3,price_text4,price_text5,price_text1_tip,
			price_text2_tip,price_text3_tip,price_text4_tip,price_text5_tip,price_type1,price_type2,
			price_type3,price_type4,price_type5,protocol1,protocol2;
	//运价模板ID
	private String billTemplateId = "";
	private double auctionPrice;
	private boolean isSelected = true;
	private NoScrollGridView trucks_gridview;
	private MyAdapter adapter;
	private ImageView settle_details;
	private LinearLayout settle_way_1,settle_way_2,settle_way_3;
	private TextView settle_way_1_title,settle_way_1_detail,settle_way_2_title,
			settle_way_2_detail,settle_way_3_title,settle_way_3_detail;
	private String settleWayId = "";
	private RelativeLayout settle_way_container;
	private List<SettleModel> billPaymentWayList = new ArrayList<>();

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AuctionOfferActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AuctionOfferActivity");
	}

	@SuppressLint("HandlerLeak") Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			chart.setAnimationState(false);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		Window win = this.getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager windowManager = getWindowManager();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = 2 * screenHeight / 3;
		lp.gravity = Gravity.BOTTOM;
		win.setAttributes(lp);

		setContentView(R.layout.auction_offer_activity);

		initViews();
		getData();
	}

	private void initViews(){
		mAuctionId = getIntent().getExtras().getString("auctionId");
		outer_goods_amount = getIntent().getExtras().getString("outer_goods_amount");
		mOfferTitle = (DiDiTitleView) findViewById(R.id.offer_title);
//		mOfferTitle.setBack(this);
		mOfferTitle.getBackImageView().setVisibility(View.GONE);
		mOfferTitle.setTitle("接单确认");
		mOfferTitle.getRightImage().setImageResource(R.drawable.bid_cancle_icon);
		mOfferTitle.getRightImage().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

//		mGoodsView = (GoodsDetailsView) findViewById(R.id.goods_details);
//		mGoodsView.setHideAuction();
//		mGoodsView.hideJoinView();
		mSureAuction = (TextView) findViewById(R.id.auction_sure);
		mAuctionPriceEdit = (EditText) findViewById(R.id.auction_parice_text);
		mMaxTrafficEdit = (EditText) findViewById(R.id.max_traffic_edit);
		mAuctionUnit = (TextView) findViewById(R.id.auction_unit);
		mTransitUnit = (TextView) findViewById(R.id.transit_unit);
//		mRightImage = (ImageView) findViewById(R.id.my_right_icon);
		//竞拍出价layout
		mAuctionOfferLayout = (RelativeLayout) findViewById(R.id.auction_offer);
		mChatLayout = (LinearLayout) findViewById(R.id.chart_layout);
		//竞价人item
		mFirstItem = (PriceItemView) findViewById(R.id.first_item);
		mSecondItem = (PriceItemView) findViewById(R.id.second_item);
		mThirdItem = (PriceItemView) findViewById(R.id.third_item);
		// 选择车辆布局
//		mChooseTruckLayout = (RelativeLayout) findViewById(R.id.choose_truck_layout);
		price_type1_container = (RelativeLayout) findViewById(R.id.price_type1_container);
		price_type2_container = (RelativeLayout) findViewById(R.id.price_type2_container);
		price_type3_container = (RelativeLayout) findViewById(R.id.price_type3_container);
		price_type4_container = (RelativeLayout) findViewById(R.id.price_type4_container);
		price_type5_container = (RelativeLayout) findViewById(R.id.price_type5_container);
//		mOneTruck = (TextView) findViewById(R.id.one_truck);
		check_box1 = (CheckBox) findViewById(R.id.check_box1);
		check_box2 = (CheckBox) findViewById(R.id.check_box2);
		check_box3 = (CheckBox) findViewById(R.id.check_box3);
		check_box4 = (CheckBox) findViewById(R.id.check_box4);
		check_box5 = (CheckBox) findViewById(R.id.check_box5);
		check_box_protocol = (CheckBox) findViewById(R.id.check_box_protocol);
		price_text1 = (TextView) findViewById(R.id.price_text1);
		price_text2 = (TextView) findViewById(R.id.price_text2);
		price_text3 = (TextView) findViewById(R.id.price_text3);
		price_text4 = (TextView) findViewById(R.id.price_text4);
		price_text5 = (TextView) findViewById(R.id.price_text5);
		price_text1_tip = (TextView) findViewById(R.id.price_text1_tip);
		price_text2_tip = (TextView) findViewById(R.id.price_text2_tip);
		price_text3_tip = (TextView) findViewById(R.id.price_text3_tip);
		price_text4_tip = (TextView) findViewById(R.id.price_text4_tip);
		price_text5_tip = (TextView) findViewById(R.id.price_text5_tip);
		price_type1 = (TextView) findViewById(R.id.price_type1);
		price_type2 = (TextView) findViewById(R.id.price_type2);
		price_type3 = (TextView) findViewById(R.id.price_type3);
		price_type4 = (TextView) findViewById(R.id.price_type4);
		price_type5 = (TextView) findViewById(R.id.price_type5);
		protocol1 = (TextView) findViewById(R.id.protocol1);
		protocol2 = (TextView) findViewById(R.id.protocol2);
		one_truck_drivername = (TextView) findViewById(R.id.one_truck_drivername);
		trucks_gridview = (NoScrollGridView) findViewById(R.id.trucks_gridview);
		settle_details = (ImageView) findViewById(R.id.settle_details);
		settle_way_1 = (LinearLayout) findViewById(R.id.settle_way_1);
		settle_way_2 = (LinearLayout) findViewById(R.id.settle_way_2);
		settle_way_3 = (LinearLayout) findViewById(R.id.settle_way_3);
		settle_way_1_title = (TextView) findViewById(R.id.settle_way_1_title);
		settle_way_1_detail = (TextView) findViewById(R.id.settle_way_1_detail);
		settle_way_2_title = (TextView) findViewById(R.id.settle_way_2_title);
		settle_way_2_detail = (TextView) findViewById(R.id.settle_way_2_detail);
		settle_way_3_title = (TextView) findViewById(R.id.settle_way_3_title);
		settle_way_3_detail = (TextView) findViewById(R.id.settle_way_3_detail);
		settle_way_container = (RelativeLayout) findViewById(R.id.settle_way_container);

		settle_details.setOnClickListener(this);
		settle_way_1.setOnClickListener(this);
		settle_way_2.setOnClickListener(this);
		settle_way_3.setOnClickListener(this);
		one_truck_drivername.setOnClickListener(this);
		check_box1.setOnClickListener(this);
		check_box2.setOnClickListener(this);
		check_box3.setOnClickListener(this);
		check_box4.setOnClickListener(this);
		check_box5.setOnClickListener(this);
		mSureAuction.setOnClickListener(this);
//		mChooseTruckLayout.setOnClickListener(this);
		check_box_protocol.setOnClickListener(this);
		protocol1.setOnClickListener(this);
		protocol2.setOnClickListener(this);
		//添加车辆布局
//		mAddTruckLayout = (LinearLayout) findViewById(R.id.add_truck_layout);
//		contract_container = (LinearLayout) findViewById(R.id.contract_container);

		//注册刷新广播
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(CommonRes.GetAuctionTruckList);
//		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}

	public void initViewData(AuctionOfferModel mAuctionOfferModel) {

		if(billPaymentWayList == null || billPaymentWayList.size() <= 0){
			settle_way_container.setVisibility(View.GONE);
		}else {
			SettleModel settleModel_1 = null,settleModel_2 = null,settleModel_3 = null;
			if(billPaymentWayList.size() == 1){
				settleModel_1 = billPaymentWayList.get(0);
			}else if(billPaymentWayList.size() == 2){
				settleModel_1 = billPaymentWayList.get(0);
				settleModel_2 = billPaymentWayList.get(1);
			}else {
				settleModel_1 = billPaymentWayList.get(0);
				settleModel_2 = billPaymentWayList.get(1);
				settleModel_3 = billPaymentWayList.get(2);
			}

			try {
				if(settleModel_1 != null && settleModel_1.getId() != null &&
						settleModel_1.getName() != null && settleModel_1.getFee() != null){
					settle_way_1.setVisibility(View.VISIBLE);
					settle_way_1_title.setText(settleModel_1.getName());
					settle_way_1_detail.setText("手续费" + settleModel_1.getFee() + "元");
				}else {
					settle_way_1.setVisibility(View.INVISIBLE);
				}

				if(settleModel_2 != null && settleModel_2.getId() != null &&
						settleModel_2.getName() != null && settleModel_2.getFee() != null){
					settle_way_2.setVisibility(View.VISIBLE);
					settle_way_2_title.setText(settleModel_2.getName());
					settle_way_2_detail.setText("手续费" + settleModel_2.getFee() + "元");
				}else {
					settle_way_2.setVisibility(View.INVISIBLE);
				}

				if(settleModel_3 != null && settleModel_3.getId() != null &&
						settleModel_3.getName() != null && settleModel_3.getFee() != null){
					settle_way_3.setVisibility(View.VISIBLE);
					settle_way_3_title.setText(settleModel_3.getName());
					settle_way_3_detail.setText("手续费" + settleModel_3.getFee() + "元");
				}else {
					settle_way_3.setVisibility(View.INVISIBLE);
				}

				setSettleViewStatus(settle_way_1);
				settle_way_container.setVisibility(View.VISIBLE);
			}catch (Exception e){

			}
		}

		//内部货源隐藏价格
		isHidePrice = !mAuctionOfferModel.isGoodsSourceTag();
//		mGoodsView.setPrice(R.drawable.price_type, mAuctionOfferModel.getAuctionPrice(), mAuctionOfferModel.getSettleUnitText(), "");
//		mGoodsView.setStartCity(mAuctionOfferModel.getSendAddr());
//		mGoodsView.setEndCity(mAuctionOfferModel.getRecvAddr());
//		mGoodsView.setGoodsName(mAuctionOfferModel.getGoodsName());
//		mGoodsView.setGoodsWeight(
//				Util.formatDoubleToString(mAuctionOfferModel.getGoodsAmount(), mAuctionOfferModel.getAssignUnitText())
//						+ mAuctionOfferModel.getAssignUnitText());
//		mGoodsView.setDist(mAuctionOfferModel.getDistance(), mAuctionOfferModel.getDiffPublish());
		mTruckList = mAuctionOfferModel.getTrucks();

		//最大运量单位
		mTransitUnit.setText(mAuctionOfferModel.getAssignUnitText());

		if(mTruckList != null && mTruckList.size() > 0){
			int validTruckNum = 0;
			int size = mTruckList.size();
			if(mTruckNumber.size() > 0) {
				mTruckNumber.clear();
			}
			for(int i = 0; i < size; i++){
				mTruckNumber.add(mTruckList.get(i).getTruckNumber());
				if(mTruckList.get(i).isValid){
					validTruckNum ++;
				}
			}

			if(size == 1){
				adapter = new MyAdapter(this,mTruckNumber,true);
				trucks_gridview.setAdapter(adapter);
				mTruckIdsList.add(mTruckList.get(0).getTruckId());
				one_truck_drivername.setVisibility(View.GONE);
			}else {
				adapter = new MyAdapter(this,mTruckNumber,false);
				trucks_gridview.setAdapter(adapter);
				one_truck_drivername.setText("全部车辆");
				one_truck_drivername.setVisibility(View.VISIBLE);
			}

			if("车".equals(mAuctionOfferModel.getAssignUnitText())){
				//车为单位
				if(mTruckList != null){
					mMaxTrafficEdit.setText("" + validTruckNum);
				}
			}else if((outer_goods_amount != null) && !"".equals(outer_goods_amount)){
				//吨为单位的外部货源
				mMaxTrafficEdit.setText(outer_goods_amount);
			}else{
				mMaxTrafficEdit.setText("");
			}


		}

		auctionId = mAuctionOfferModel.getAuctionId();
		if(mAuctionOfferModel.getAuctionType() == 2 || mAuctionOfferModel.getAuctionType() == 3){
			//一口价
			mAuctionOfferLayout.setVisibility(View.GONE);
			mChatLayout.setVisibility(View.GONE);
			price_type1_container.setVisibility(View.VISIBLE);
			check_box1.setChecked(true);
			price_text1.setText(Util.formatDoubleToString((mAuctionOfferModel.getAuctionPrice()),
					"元")+" 元/"+mAuctionOfferModel.getSettleUnitText());
			String recPriceStr = mAuctionOfferModel.getAuctionPriceTemplateStr();
			if(recPriceStr == null || "".equals(recPriceStr)){
				price_type1.setBackgroundResource(R.drawable.price_type);
			}else {
				price_type1.setBackgroundResource(R.drawable.driver_divide_price);
				price_text1_tip.setText(mAuctionOfferModel.getAuctionPriceTemplateStr());
			}

//			mGoodsView.joinNumber(mAuctionOfferModel.getBidderNum());
			auctionPrice = mAuctionOfferModel.getAuctionPrice();
			billTmpltVoList = mAuctionOfferModel.getBillTmpltVoList();
			int size = billTmpltVoList.size();
			if(size > 0){
				initPriceTmp(size);
			}

//			if ((mAuctionOfferModel.getAuctionType()==CommonRes.FIXED_PRICE ||
//					mAuctionOfferModel.getAuctionType()==CommonRes.LONG_TRANSPORT) && mAuctionOfferModel.getInnerGoods() == 1) {
//				//内部货源
//				mGoodsView.setPrice(R.drawable.price_type, mAuctionOfferModel.getAuctionPrice(),
//						mAuctionOfferModel.getSettleUnitText(), "剩余量 " +
//								Util.formatDoubleToString(Double.valueOf(mAuctionOfferModel.getGoodsAmountSp()), mAuctionOfferModel.getAssignUnit()) + " " + mAuctionOfferModel.getAssignUnitText());
//			} else {
//				//外部货源
//				mGoodsView.setPrice(R.drawable.price_type, mAuctionOfferModel.getAuctionPrice(),
//						mAuctionOfferModel.getSettleUnitText(), "");
//			}
		}else {
			//价格区间
			init(mAuctionOfferModel);
//			mGoodsView.setPrice(R.drawable.price_type3, Util.formatDoubleToString((mAuctionOfferModel.getLowPrice()), "元") +
//					" ~ " + Util.formatDoubleToString((mAuctionOfferModel.getHighPrice()), "元"),
//					mAuctionOfferModel.getSettleUnitText());
			mAuctionPriceEdit.setHint("本次报价幅度为" + mAuctionOfferModel.getBidIncrement() + "元");
			mAuctionPriceEdit.setVisibility(View.VISIBLE);
			mAuctionOfferLayout.setVisibility(View.VISIBLE);
		}
		//竞拍出价输入限制，小数点后不能超过两位字符，出价范围在0——10^7元
		Util.getDoubleInputLimitTextWatcher().setEditText(mAuctionPriceEdit);
		Util.getNumericLimitTextWatcher(0, 10000000).setEditText(mAuctionPriceEdit);
		//竞拍出价单位
		mAuctionUnit.setText("元/" + mAuctionOfferModel.getSettleUnitText());
		
		//最大运量输入限制，小数点后不能超过两位字符
		if(mAuctionOfferModel.getAssignUnit().equals("T") || mAuctionOfferModel.getAssignUnit().equals("M3")){
			//竞拍出价输入限制，小数点后不能超过两位字符
			InputLimitTextWatcher watcher = Util.getDoubleInputLimitTextWatcher();
			watcher.setEditText(mMaxTrafficEdit);
		}else if(mAuctionOfferModel.getAssignUnit().equals("TRUCK") || mAuctionOfferModel.getAssignUnit().equals("PIECE")){
			mMaxTrafficEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			if(mTruckList != null){
				mMaxTrafficEdit.setText(mTruckList.size());
			}
		}

	}

	private void initPriceTmp(int size){
		switch (size){
			case 1:
				price_type2_container.setVisibility(View.VISIBLE);
				price_text2.setText(billTmpltVoList.get(0).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());

				String recPriceStr = billTmpltVoList.get(0).getTemplateStr();
				if(recPriceStr == null || "".equals(recPriceStr)){
					price_type2.setBackgroundResource(R.drawable.price_type);
				}else {
					price_type2.setBackgroundResource(R.drawable.driver_divide_price);
					price_text2_tip.setText(recPriceStr);
				}
				break;
			case 2:
				price_type2_container.setVisibility(View.VISIBLE);
				price_text2.setText(billTmpltVoList.get(0).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				String recPriceStr2 = billTmpltVoList.get(0).getTemplateStr();
				if(recPriceStr2 == null || "".equals(recPriceStr2)){
					price_type2.setBackgroundResource(R.drawable.price_type);
				}else {
					price_type2.setBackgroundResource(R.drawable.driver_divide_price);
					price_text2_tip.setText(recPriceStr2);
				}
				price_type3_container.setVisibility(View.VISIBLE);
				price_text3.setText(billTmpltVoList.get(1).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text3_tip.setText(billTmpltVoList.get(1).getTemplateStr());
				break;
			case 3:
				price_type2_container.setVisibility(View.VISIBLE);
				price_text2.setText(billTmpltVoList.get(0).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				String recPriceStr3 = billTmpltVoList.get(0).getTemplateStr();
				if(recPriceStr3 == null || "".equals(recPriceStr3)){
					price_type2.setBackgroundResource(R.drawable.price_type);
				}else {
					price_type2.setBackgroundResource(R.drawable.driver_divide_price);
					price_text2_tip.setText(recPriceStr3);
				}
				price_type3_container.setVisibility(View.VISIBLE);
				price_text3.setText(billTmpltVoList.get(1).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text3_tip.setText(billTmpltVoList.get(1).getTemplateStr());
				price_type4_container.setVisibility(View.VISIBLE);
				price_text4.setText(billTmpltVoList.get(2).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text4_tip.setText(billTmpltVoList.get(2).getTemplateStr());
				break;
			case 4:
				price_type2_container.setVisibility(View.VISIBLE);
				price_text2.setText(billTmpltVoList.get(0).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				String recPriceStr4 = billTmpltVoList.get(0).getTemplateStr();
				if(recPriceStr4 == null || "".equals(recPriceStr4)){
					price_type2.setBackgroundResource(R.drawable.price_type);
				}else {
					price_type2.setBackgroundResource(R.drawable.driver_divide_price);
					price_text2_tip.setText(recPriceStr4);
				}
				price_type3_container.setVisibility(View.VISIBLE);
				price_text3.setText(billTmpltVoList.get(1).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text3_tip.setText(billTmpltVoList.get(1).getTemplateStr());
				price_type4_container.setVisibility(View.VISIBLE);
				price_text4.setText(billTmpltVoList.get(2).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text4_tip.setText(billTmpltVoList.get(2).getTemplateStr());
				price_type5_container.setVisibility(View.VISIBLE);
				price_text5.setText(billTmpltVoList.get(3).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text5_tip.setText(billTmpltVoList.get(3).getTemplateStr());
				break;
			default:
				price_type2_container.setVisibility(View.VISIBLE);
				price_text2.setText(billTmpltVoList.get(0).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				String recPriceStr5 = billTmpltVoList.get(0).getTemplateStr();
				if(recPriceStr5 == null || "".equals(recPriceStr5)){
					price_type2.setBackgroundResource(R.drawable.price_type);
				}else {
					price_type2.setBackgroundResource(R.drawable.driver_divide_price);
					price_text2_tip.setText(recPriceStr5);
				}
				price_type3_container.setVisibility(View.VISIBLE);
				price_text3.setText(billTmpltVoList.get(1).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text3_tip.setText(billTmpltVoList.get(1).getTemplateStr());
				price_type4_container.setVisibility(View.VISIBLE);
				price_text4.setText(billTmpltVoList.get(2).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text4_tip.setText(billTmpltVoList.get(2).getTemplateStr());
				price_type5_container.setVisibility(View.VISIBLE);
				price_text5.setText(billTmpltVoList.get(3).getTotalFee() + "元/" + mAuctionOfferModel.getSettleUnitText());
				price_text5_tip.setText(billTmpltVoList.get(3).getTemplateStr());
				break;
		}
	}


	public void getData() {
		SharedPreferences sp = getSharedPreferences("config",Context.MODE_PRIVATE);
		String lati = sp.getString("latitude", "");
		String longi = sp.getString("longitude", "");
		
		AjaxParams params = new AjaxParams();
		params.put("auctionId", mAuctionId);
		params.put("lat", lati);
		params.put("lng", longi);
		DidiApp.getHttpManager().sessionPost(AuctionOfferActivity.this,
				auction_offer_url, params, new AfinalHttpCallBack() {
					@Override
					public void data(String t) {
						mSureAuction.setVisibility(View.VISIBLE);
						try {
							JSONObject jsonObject = new JSONObject(t);
							String all = jsonObject.getString("body");
							mAuctionOfferModel = JSON.parseObject(all,AuctionOfferModel.class);
							unitext = mAuctionOfferModel.getSettleUnitText();
//							Log.i("http","mAuctionOfferModel = " + JSON.toJSONString(mAuctionOfferModel));
							billPaymentWayList = mAuctionOfferModel.getBillPaymentWayList();
							initViewData(mAuctionOfferModel);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	private void init(AuctionOfferModel mAuctionOfferModel) {
		chart = (MagnificentChart) this.findViewById(R.id.order_chart_id);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scrWidth = (int) (dm.widthPixels * 0.6);
		int scrHeight = (int) (dm.widthPixels * 0.6);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				scrWidth, scrHeight);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

		chart.setLayoutParams(layoutParams);
		chart.setMaxValue(100);
		chart.setChartBackgroundColor(this.getResources().getColor(
				R.color.white));
		List<Statistic> statistic = null;
		if(mAuctionOfferModel != null){
			statistic = mAuctionOfferModel.getStatistic();
		}
		List<MagnificentChartItem> chartItemsList = new ArrayList<MagnificentChartItem>();
		int max = 0;
		if(statistic != null && statistic.size()>0){
			for(int n=0;n<statistic.size();n++){
				int person = statistic.get(n).getBidderNum();
				max = max+person;
			}
			chart.setMaxValue(max);
			if(statistic.size()==6){
				int color = getResources().getColor(R.color.chart_6);
				MagnificentChartItem item1 = new MagnificentChartItem("", statistic.get(5).getBidderNum(), color, "其他");
				chartItemsList.add(item1);
				mThirdItem.setVisibility(View.VISIBLE);
				mThirdItem.showSecondLayout();
				mThirdItem.setSecondColor(color);
				if(isHidePrice){
					mThirdItem.setSecondPrice("");
				}else{
					mThirdItem.setSecondPrice("其他");
				}
				mThirdItem.setSecondBidNumber(statistic.get(5).getBidderNum());
			}
			for(int i=0;i<statistic.size();i++){
				int person = statistic.get(i).getBidderNum();  
				
				if(i==0){
					int color = getResources().getColor(R.color.chart_0);
					MagnificentChartItem item2 = new MagnificentChartItem("", person,color , "49 元 / 吨");
					chartItemsList.add(item2);
					mFirstItem.setVisibility(View.VISIBLE);
					mFirstItem.setColor(color);
					mFirstItem.setPrice(statistic.get(i).getBidPrice(), unitext,isHidePrice);
					mFirstItem.setBidNumber(statistic.get(i).getBidderNum());
				}
				if(i==1){
					int color = getResources().getColor(R.color.chart_1);
					MagnificentChartItem item3 = new MagnificentChartItem("", person, color, "48 元 / 吨");
					chartItemsList.add(item3);
					mFirstItem.showSecondLayout();
					mFirstItem.setSecondColor(color);
					mFirstItem.setSecondPrice(statistic.get(i).getBidPrice(), unitext,isHidePrice);
					mFirstItem.setSecondBidNumber(statistic.get(i).getBidderNum());
				}
				if(i==2){
					int color = getResources().getColor(R.color.chart_2);
					MagnificentChartItem item4 = new MagnificentChartItem("", person, color, "47 元 / 吨");
					chartItemsList.add(item4);
					
					mSecondItem.setVisibility(View.VISIBLE);
					mSecondItem.setColor(color);
					mSecondItem.setPrice(statistic.get(i).getBidPrice(), unitext,isHidePrice);
					mSecondItem.setBidNumber(statistic.get(i).getBidderNum());
				}
				if(i==3){
					int color = getResources().getColor(R.color.chart_3);
					MagnificentChartItem item5 = new MagnificentChartItem("", person, color, "46 元 / 吨");
					chartItemsList.add(item5);
					mSecondItem.showSecondLayout();
					mSecondItem.setSecondColor(color);
					mSecondItem.setSecondPrice(statistic.get(i).getBidPrice(), unitext,isHidePrice);
					mSecondItem.setSecondBidNumber(statistic.get(i).getBidderNum());
				}
				if(i==4){
					int color = getResources().getColor(R.color.chart_4);
					MagnificentChartItem item6 = new MagnificentChartItem("", person, color, "45 元 / 吨");
					chartItemsList.add(item6);
					
					mThirdItem.setVisibility(View.VISIBLE);
					mThirdItem.setColor(color);
					mThirdItem.setPrice(statistic.get(i).getBidPrice(), unitext,isHidePrice);
					mThirdItem.setBidNumber(statistic.get(i).getBidderNum());
				}
			}
			if (mAuctionOfferModel != null) {
				chart.setChartItemsList(chartItemsList,mAuctionOfferModel.getBidderNum());
			} 
		}else{
			MagnificentChartItem item = new MagnificentChartItem("", 100, this.getResources().getColor(R.color.chart_6), "45 元 / 吨");
			chartItemsList.add(item);
			chart.setChartItemsList(chartItemsList,0);
		}
		handler.sendEmptyMessageDelayed(0, 2000);
	}

	private void auction() {

		if(!isSelected){
			Toast.makeText(AuctionOfferActivity.this,"须选择运价模板才能参与竞价!",Toast.LENGTH_LONG).show();
			return;
		}
		AuctionDoBid mAuctionDoBid;
		if(mAuctionOfferModel.getAuctionType()==CommonRes.BID_PRICE){
			mAuctionDoBid = new AuctionDoBid(auctionId,
					Double.valueOf(mMaxTraffic),
					Double.valueOf(mAuctionPrice), mTruckIdsList);
		}else{
			mAuctionDoBid = new AuctionDoBid(auctionId,
					Double.valueOf(mMaxTraffic),
					auctionPrice, mTruckIdsList);
		}
		mAuctionDoBid.setBillTemplateId(billTemplateId);
		mAuctionDoBid.setPayTypeId(settleWayId);
		String jsonString = JSON.toJSONString(mAuctionDoBid);
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog(AuctionOfferActivity.this, "请稍等");
		AjaxParams params = new AjaxParams();
		params.put("bidJson", jsonString);
		DidiApp.getHttpManager().sessionPost(AuctionOfferActivity.this,
				auction_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						mPromptManager.closeProgressDialog();
						if(!t.equals("")){
							Intent check_intent = new Intent(AuctionOfferActivity.this, ResultActivity.class);
							check_intent.putExtra("result", 1);
							startActivity(check_intent);

							Intent homeRefresh = new Intent();
							//启动消息线程广播
							homeRefresh.setAction(CommonRes.RefreshGoodsList);
							sendBroadcast(homeRefresh);
							finish();
						}
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						mPromptManager.closeProgressDialog();
//						Log.d("huiyuan","确认接单错误信息:" + errMsg);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.settle_details:
				Intent mIntent = new Intent(AuctionOfferActivity.this,WebViewWithTitleActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putString("title", "结算方式说明");
				mBundle.putString("url", "http://cz.redlion56.com/app/settlementStatement.html");
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
				break;
			case R.id.settle_way_1:
				setSettleViewStatus(settle_way_1);
				break;
			case R.id.settle_way_2:
				setSettleViewStatus(settle_way_2);
				break;
			case R.id.settle_way_3:
				setSettleViewStatus(settle_way_3);
				break;
			case R.id.one_truck_drivername:
				if(mTruckList == null || (mTruckList != null && mTruckList.size() <= 0)){
					Toast.makeText(AuctionOfferActivity.this,"无车辆信息!",Toast.LENGTH_LONG).show();
					return;
				}
				if("全部车辆".equals(one_truck_drivername.getText().toString())){
					if(mTruckIdsList.size() > 0){
						mTruckIdsList.clear();
					}

					int validTruksNumber = 0;
					if(mTruckList != null){
						for(int i = 0; i < mTruckList.size(); i++){
							if(mTruckList.get(i).isValid){
								mTruckIdsList.add(mTruckList.get(i).getTruckId());
								validTruksNumber ++;
							}
//						Log.i("http",mTruckIdsList.get(i));
						}
					}

					if("车".equals(mAuctionOfferModel.getAssignUnitText())){
						if(mTruckList != null){
							mMaxTrafficEdit.setText("" + validTruksNumber);
						}
					}

					adapter = new MyAdapter(AuctionOfferActivity.this,mTruckNumber,true);
					trucks_gridview.setAdapter(adapter);
					one_truck_drivername.setText("重新选择");
				}else {
					if(mTruckIdsList.size() > 0){
						mTruckIdsList.clear();
					}
					adapter = new MyAdapter(AuctionOfferActivity.this,mTruckNumber,false);
					trucks_gridview.setAdapter(adapter);
					one_truck_drivername.setText("全部车辆");
					if("车".equals(mAuctionOfferModel.getAssignUnitText())){
						mMaxTrafficEdit.setText("0");
					}
				}
				break;
		case R.id.auction_sure:
			StringBuffer mbuffer = new StringBuffer();
			
			//如果不是一口价，需要检查是否填写竞拍出价，并检查是否合法
			mAuctionPrice = mAuctionPriceEdit.getText().toString();
			if(mAuctionOfferModel.getAuctionType() == CommonRes.BID_PRICE){
				if(!inputJudge(mAuctionPriceEdit.getText().toString(), "接单出价", mbuffer)){
					return;
				}
				mAuctionPrice = mbuffer.toString();
				if (Double.valueOf(mAuctionPrice) < mAuctionOfferModel.getLowPrice() ||
						Double.valueOf(mAuctionPrice) > mAuctionOfferModel.getHighPrice()){
					ToastUtil.show(AuctionOfferActivity.this, "接单出价应在报价区间内");
					return;
				}
			}
			
			//检查是否填写最大运量，并检查是否合法
			String assignUnit = mAuctionOfferModel.getAssignUnit();
			if((assignUnit.equals("T") || (assignUnit.equals("M3")))
					&& !inputJudge(mMaxTrafficEdit.getText().toString(), "最大运量", mbuffer)){
				return;
			}else if((assignUnit.equals("TRUCK") || (assignUnit.equals("PIECE")))
					&& !NaturalNumberInputJudge(mMaxTrafficEdit.getText().toString(), "最大运量", mbuffer)){
				return;
			}
			mMaxTraffic = mMaxTrafficEdit.getText().toString();
			if("".equals(mMaxTraffic)){
				mMaxTraffic = "0";
			}
			//检查是否已选报备车辆
			if(mTruckIdsList.size() <= 0){
				ToastUtil.show(AuctionOfferActivity.this, "请选择报备车辆");
				return;
			}
			if(mAuctionOfferModel.getAuctionType() == CommonRes.LONG_TRANSPORT){
				if(Double.valueOf(mMaxTraffic) <= Double.valueOf(mAuctionOfferModel.getGoodsAmountSp())){
					auction();
					return;
				}else {
					Toast.makeText(AuctionOfferActivity.this,"本次货源数量为" + "【" + mAuctionOfferModel.getGoodsAmount()
							+ "】" + mAuctionOfferModel.getAssignUnitText() + "还剩" + "【" + mAuctionOfferModel.getGoodsAmountSp() + "】" + mAuctionOfferModel.getAssignUnitText(),Toast.LENGTH_LONG).show();
					return;
				}
			}
			auction();
			break;

			case R.id.check_box1:
				if(check_box1.isChecked()){
					auctionPrice = mAuctionOfferModel.getAuctionPrice();
					isSelected = true;
				}else {
					isSelected = false;
				}
				check_box2.setChecked(false);
				check_box3.setChecked(false);
				check_box4.setChecked(false);
				check_box5.setChecked(false);
				break;
			case R.id.check_box2:
				if(check_box2.isChecked()){
					billTemplateId = mAuctionOfferModel.getBillTmpltVoList().get(0).getBillTmpltId();
					auctionPrice = mAuctionOfferModel.getBillTmpltVoList().get(0).getTotalFee();
					isSelected = true;
				}else {
					isSelected = false;
				}
				check_box1.setChecked(false);
				check_box3.setChecked(false);
				check_box4.setChecked(false);
				check_box5.setChecked(false);
				break;
			case R.id.check_box3:
				if(check_box3.isChecked()){
					billTemplateId = mAuctionOfferModel.getBillTmpltVoList().get(1).getBillTmpltId();
					auctionPrice = mAuctionOfferModel.getBillTmpltVoList().get(1).getTotalFee();
					isSelected = true;
				}else {
					isSelected = false;
				}
				check_box1.setChecked(false);
				check_box2.setChecked(false);
				check_box4.setChecked(false);
				check_box5.setChecked(false);
				break;
			case R.id.check_box4:
				if(check_box4.isChecked()){
					billTemplateId = mAuctionOfferModel.getBillTmpltVoList().get(2).getBillTmpltId();
					auctionPrice = mAuctionOfferModel.getBillTmpltVoList().get(2).getTotalFee();
					isSelected = true;
				}else {
					isSelected = false;
				}
				check_box1.setChecked(false);
				check_box2.setChecked(false);
				check_box3.setChecked(false);
				check_box5.setChecked(false);
				break;
			case R.id.check_box5:
				if(check_box5.isChecked()){
					billTemplateId = mAuctionOfferModel.getBillTmpltVoList().get(3).getBillTmpltId();
					auctionPrice = mAuctionOfferModel.getBillTmpltVoList().get(3).getTotalFee();
					isSelected = true;
				}else {
					isSelected = false;
				}
				check_box1.setChecked(false);
				check_box2.setChecked(false);
				check_box3.setChecked(false);
				check_box4.setChecked(false);
				break;
			case R.id.protocol1:
				Intent mIntent1 = new Intent(AuctionOfferActivity.this,WebViewWithTitleActivity.class);
				Bundle mBundle1 = new Bundle();
				mBundle1.putString("title", getResources().getString(R.string.zu_lin_he_tong_tip));
				mBundle1.putString("url", GloableParams.WEB_URL+"vehicleRentProtocol.html");
				mIntent1.putExtras(mBundle1);
				startActivity(mIntent1);
				break;
			case R.id.protocol2:
				Intent mIntent12 = new Intent(AuctionOfferActivity.this,WebViewWithTitleActivity.class);
				Bundle mBundle12 = new Bundle();
				mBundle12.putString("title", getResources().getString(R.string.lao_wu_cheng_bao_he_tong_tip));
				mBundle12.putString("url", GloableParams.WEB_URL+"personalServiceProtocol.html");
				mIntent12.putExtras(mBundle12);
				startActivity(mIntent12);
				break;
			case R.id.check_box_protocol:
				break;
		default:
			break;
		}
	}

	//判断字符串是否为合法的正浮点数，如果不是，按错误情况给出相应的toast提示
	private boolean inputJudge(String input, String name, StringBuffer result){
		if (input.equals("") ) {
			ToastUtil.show(AuctionOfferActivity.this, "请填写" + name);
			return false;
		}
		result.delete(0, result.length());
		result.append(Util.inputToDoubleStr(input));
		if(result.toString().equals("")){
			ToastUtil.doubleParseError(AuctionOfferActivity.this, name);
			return false;
		}else if(result.toString().equals("0")){
			ToastUtil.doubleNonpositiveError(AuctionOfferActivity.this, name);
			return false;
		}
		return true;
	}
	
	//判断字符串是否为合法的正整数，如果不是，按错误情况给出相应的toast提示
	private boolean NaturalNumberInputJudge(String input, String name, StringBuffer result){
		if (input.equals("") ) {
			ToastUtil.show(AuctionOfferActivity.this, "请填写" + name);
			return false;
		}
		result.delete(0, result.length());
		result.append(Util.inputToIntegerStr(input));
		if(result.toString().equals("")){
			ToastUtil.integerParseError(AuctionOfferActivity.this, name);
			return false;
		}else if(result.toString().equals("0")){
			ToastUtil.integerNonpositiveError(AuctionOfferActivity.this, name);
			return false;
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		unregisterReceiver(mRefreshBroadcastReceiver);
	}

	private boolean isItemClicked = false;
	private class MyAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;
		private List<String> truckList;
		private boolean [] selectedList;
		private Context context;
		private boolean isSelectedAll;
		final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,88);

		public MyAdapter(Context context,List<String> truckList,boolean isSelectedAll){
			this.truckList = truckList;
			this.context = context;
			this.layoutInflater = LayoutInflater.from(context);
			selectedList = new boolean[this.truckList.size()];
			this.isSelectedAll = isSelectedAll;
			layoutParams.leftMargin = 2;
			layoutParams.rightMargin = 2;
			layoutParams.topMargin = 2;
			layoutParams.bottomMargin = 2;
//			if(mTruckIdsList.size() > 0){
//				mTruckIdsList.clear();
//			}
		}
		@Override
		public int getCount() {
			return truckList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View v = layoutInflater.inflate(R.layout.driver_choose_goods_type_item,null);
			final TextView tv = (TextView) v.findViewById(R.id.goods_type);
			final ImageView imageView = (ImageView) v.findViewById(R.id.status_logo);
			if(!mTruckList.get(position).isValid){
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(AuctionOfferActivity.this,"禁止接单车辆，请联系客服",Toast.LENGTH_LONG).show();
						return;
					}
				});
			}else {
				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						isItemClicked = true;
						String truckId = mTruckList.get(position).getTruckId();
						int amount = 0;
						if(!"".equals(mMaxTrafficEdit.getText().toString())
								&& "车".equals(mAuctionOfferModel.getAssignUnitText())){
							amount = Integer.parseInt(mMaxTrafficEdit.getText().toString());
						}
						if(selectedList[position]){
//						selectedList[position] = false;
							if(mTruckIdsList.contains(truckId)){
								mTruckIdsList.remove(truckId);
							}
							setViewStatus(tv,false,position,imageView);
							if("车".equals(mAuctionOfferModel.getAssignUnitText())){
								if(amount > 1){
									mMaxTrafficEdit.setText("" + (amount - 1));
								}else {
									mMaxTrafficEdit.setText("0");
								}
							}
							if((!"".equals(mMaxTrafficEdit.getText().toString()) && "车".equals(mAuctionOfferModel.getAssignUnitText())) &&
									(Integer.parseInt(mMaxTrafficEdit.getText().toString()) == mTruckList.size())){
								one_truck_drivername.setText("重新选择");
							}else {
								one_truck_drivername.setText("全部车辆");
							}
							return;
						}else {
//						selectedList[position] = true;
							if(!mTruckIdsList.contains(truckId)){
								mTruckIdsList.add(truckId);
							}
							setViewStatus(tv,true,position,imageView);
							if("车".equals(mAuctionOfferModel.getAssignUnitText())){
								if(amount >= mTruckList.size()){
									mMaxTrafficEdit.setText("" + mTruckIdsList.size());
								}else {
									mMaxTrafficEdit.setText("" + (amount + 1));
								}
							}
							if((!"".equals(mMaxTrafficEdit.getText().toString()) && "车".equals(mAuctionOfferModel.getAssignUnitText())) &&
									(Integer.valueOf(mMaxTrafficEdit.getText().toString()) == mTruckList.size())){
								one_truck_drivername.setText("重新选择");
							}else {
								one_truck_drivername.setText("全部车辆");
							}
							return;
						}
					}
				});
			}
			tv.setText(truckList.get(position));
			tv.setLayoutParams(layoutParams);

			if(isSelectedAll){
//				Log.i("http","全选");
				setViewStatus(tv,true,position,imageView);
			}else if(!isItemClicked){
//				Log.i("http","取消全选");
				setViewStatus(tv,false,position,imageView);
			}else {
				setViewStatus(tv,false,position,imageView);
			}

			return v;
		}

		private void setViewStatus(TextView tv,boolean isSelected,int position,ImageView imageView){
			if(isSelected){
				selectedList[position] = true;
				Log.i("http",position + " = " + selectedList[position]);
				tv.setBackgroundColor(context.getResources().getColor(R.color.home_text_press));
				tv.setTextColor(context.getResources().getColor(R.color.white));

			}else {
				selectedList[position] = false;
				Log.i("http",position + " = " + selectedList[position]);
				tv.setBackgroundColor(context.getResources().getColor(R.color.list_item_line));
				tv.setTextColor(context.getResources().getColor(R.color.black));
			}
			if(!mTruckList.get(position).isValid){
				tv.setBackgroundColor(context.getResources().getColor(R.color.list_item_line));
				tv.setTextColor(getResources().getColor(R.color.line_color));
				imageView.setVisibility(View.VISIBLE);
				selectedList[position] = false;
			}
		}
	}

	private void setSettleViewStatus(LinearLayout selectedSettleWay){
		selectedSettleWay.setBackgroundColor(getResources().getColor(R.color.home_text_press));
		switch (selectedSettleWay.getId()){
			case R.id.settle_way_1:
				settleWayId = billPaymentWayList.get(0).getId();
				settle_way_2.setBackgroundColor(getResources().getColor(R.color.list_item_line));
				settle_way_3.setBackgroundColor(getResources().getColor(R.color.list_item_line));
				settle_way_1_title.setTextColor(getResources().getColor(R.color.white));
				settle_way_1_detail.setTextColor(getResources().getColor(R.color.white));
				settle_way_2_title.setTextColor(getResources().getColor(R.color.black));
				settle_way_2_detail.setTextColor(getResources().getColor(R.color.black));
				settle_way_3_title.setTextColor(getResources().getColor(R.color.black));
				settle_way_3_detail.setTextColor(getResources().getColor(R.color.black));
				break;
			case R.id.settle_way_2:
				settleWayId = billPaymentWayList.get(1).getId();
				settle_way_1.setBackgroundColor(getResources().getColor(R.color.list_item_line));
				settle_way_3.setBackgroundColor(getResources().getColor(R.color.list_item_line));
				settle_way_1_title.setTextColor(getResources().getColor(R.color.black));
				settle_way_1_detail.setTextColor(getResources().getColor(R.color.black));
				settle_way_2_title.setTextColor(getResources().getColor(R.color.white));
				settle_way_2_detail.setTextColor(getResources().getColor(R.color.white));
				settle_way_3_title.setTextColor(getResources().getColor(R.color.black));
				settle_way_3_detail.setTextColor(getResources().getColor(R.color.black));
				break;
			case R.id.settle_way_3:
				settleWayId = billPaymentWayList.get(2).getId();
				settle_way_2.setBackgroundColor(getResources().getColor(R.color.list_item_line));
				settle_way_1.setBackgroundColor(getResources().getColor(R.color.list_item_line));
				settle_way_1_title.setTextColor(getResources().getColor(R.color.black));
				settle_way_1_detail.setTextColor(getResources().getColor(R.color.black));
				settle_way_2_title.setTextColor(getResources().getColor(R.color.black));
				settle_way_2_detail.setTextColor(getResources().getColor(R.color.black));
				settle_way_3_title.setTextColor(getResources().getColor(R.color.white));
				settle_way_3_detail.setTextColor(getResources().getColor(R.color.white));
				break;
			default:
				break;
		}
	}
}
