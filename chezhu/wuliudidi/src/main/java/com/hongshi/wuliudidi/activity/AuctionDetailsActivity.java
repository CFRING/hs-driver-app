package com.hongshi.wuliudidi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.CancelDialog;
import com.hongshi.wuliudidi.dialog.ShareDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AuctionDetailsModel;
import com.hongshi.wuliudidi.model.SettleModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.GoodsBubbleMsg;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.MyItemView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author huiyuan
 */
public class AuctionDetailsActivity extends Activity implements OnClickListener {
	private DiDiTitleView mTitleView;
	private AuctionItem mSendTimeItem, mSuggestTruckTypeItem, mSuggestTruckCarriageItem, mSuggestTruckLengthItem;
	private AuctionItem mGoodsName, mGoodsWeight, mGoodsVolume, mGoodsNumber,settle_company,
			mTransitMeasure, mGoodsModelNumber, mGoodsPackageType, mGoodsMileage, mPayType,mSettleType, mBill, mShengYu;
	private RelativeLayout myAuctionMessageLayout, remarkLayout;
	private ImageView mShareImage, mCallImage;
	private MyItemView mContactItem, mAuctionIntroduceItem;
	private TextView mAuctionBtn;
	private TextView mAuctionText, mAuctionNumber, mAuctionTimeText, mAuctionTime, mBidderNum;
	private LinearLayout mTwoLayout, mOrderAddressLayout;
	private String mAuctionId = "";
	//货源详情
	private final String auction_url = GloableParams.HOST + "carrier/auction/find.do?";
	//查询竞价权限
	private final String check_privilege_url = GloableParams.HOST + "carrier/bid/checkpriv.do?";
	public AuctionDetailsModel mAuctionDetailsModel;
	private TextView mSendAddressText, mRecvAddressText, mAuctionTypeText;
	private TextView mPriceText, mAuctionTimeType, mRemarkText;
	private long mBidTime = -1;
	private Timer timer;
	private LinearLayout order_layout;
	private String cancel_url = GloableParams.HOST + "carrier/bid/cancelbid.do?";
	private Boolean isFirst = true;
	private List<SettleModel> billPaymentWayList = new ArrayList<>();

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case 1:
					mBidTime = mBidTime - 1000;
					// 时间倒计时
					if (!mAuctionDetailsModel.isBidStart()) {
						//此时竞拍还未开始
						if (mBidTime < 0) {
							mBidTime = mAuctionDetailsModel.getDiffBidEnd();
							mAuctionTimeType.setText("距离接单结束：" + Util.millisecondToDays(mBidTime));
							mAuctionDetailsModel.setBidStart(true);
							mAuctionDetailsModel.setStatus(3);
							mAuctionBtn.setText(R.string.auction);
							mAuctionBtn.setTextColor(getResources().getColor(R.color.white));
							mAuctionBtn.setBackgroundResource(R.color.theme_color);
							mAuctionBtn.setClickable(true);
						}else {
							mAuctionTimeType.setText("距离接单开始：" + Util.millisecondToDays(mBidTime));
						}
					}else if (mAuctionDetailsModel.getDiffBidEnd() >= 0 && mAuctionDetailsModel.getStatus() == 3) {
						// 此时是竞拍中
						if (mBidTime < 0) {
							mAuctionTimeType.setText("接单已截止");
							mAuctionBtn.setText("接单已结束");
							mAuctionBtn.setClickable(false);
							mAuctionBtn.setBackgroundResource(R.color.gray);
							if (timer != null)
							{
								timer.cancel();
							}
						} else {
							mAuctionTimeType.setText("距离接单结束：" + Util.millisecondToDays(mBidTime));
						}
					}
					break;
				case CommonRes.CANCELAUCTION:
					//取消竞价
					AjaxParams params = new AjaxParams();
					params.put("bidItemId", mAuctionDetailsModel.getBidItemId());
					DidiApp.getHttpManager().sessionPost(AuctionDetailsActivity.this, cancel_url, params, new ChildAfinalHttpCallBack() {
						@Override
						public void data(String t) {
							ToastUtil.show(AuctionDetailsActivity.this, "成功取消接单");
							getAuctionInfo();

							Intent homeRefresh = new Intent();
							//启动消息线程广播
							homeRefresh.setAction(CommonRes.RefreshGoodsList);
							sendBroadcast(homeRefresh);
						}

						@Override
						public void onFailure(String errCode, String errMsg, Boolean errSerious) {
							ToastUtil.show(AuctionDetailsActivity.this, "接单取消失败");
						}
					});
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AuctionDetailsActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AuctionDetailsActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.auction_details);
		mTitleView = (DiDiTitleView) findViewById(R.id.auction_title);
		// 获取竞价单ID
		mAuctionId = getIntent().getExtras().getString("auctionId");
		mShareImage = mTitleView.getRightImage();
		mShareImage.setVisibility(View.VISIBLE);
		mShareImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Util.showShare(AuctionDetailsActivity.this);
				ShareDialog mShareDialog = new ShareDialog(AuctionDetailsActivity.this, R.style.data_filling_dialog,
						mAuctionId,mAuctionDetailsModel.getDynamicShareStr());
				UploadUtil.setAnimation(mShareDialog, CommonRes.TYPE_BOTTOM, true);
				mShareDialog.show();
			}
		});
		mTitleView.setBack(this);
		mTitleView.setTitle(getResources().getString(R.string.auction_details));

		//我的竞价信息
		myAuctionMessageLayout = (RelativeLayout) findViewById(R.id.my_auction_message_layout);

		// 地址详情
		mOrderAddressLayout = (LinearLayout) findViewById(R.id.address_order_layout);
		mOrderAddressLayout.setOnClickListener(this);
		mSendAddressText = (TextView) findViewById(R.id.send_addr_text);
		mRecvAddressText = (TextView) findViewById(R.id.recv_addr_text);
		// 底部单子详情
		mAuctionText = (TextView) findViewById(R.id.one_text);
		mAuctionText.setText("接单号");
		mAuctionNumber = (TextView) findViewById(R.id.one_content);
		mAuctionNumber.setText("hs33243244");
		mTwoLayout = (LinearLayout) findViewById(R.id.two_layout);
		mTwoLayout.setVisibility(View.VISIBLE);
		mAuctionTimeText = (TextView) findViewById(R.id.two_text);
		mAuctionTimeText.setText("接单时间");
		mAuctionTime = (TextView) findViewById(R.id.two_content);
		mAuctionTime.setText("2014-2-5");
		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.four_layout);
		mLinearLayout.setVisibility(View.GONE);
		order_layout = (LinearLayout) findViewById(R.id.order_info);
		order_layout.setVisibility(View.GONE);
		// 发货时间
		mSendTimeItem = (AuctionItem) findViewById(R.id.send_time);
		mSendTimeItem.setBackground(R.color.white);
		mSendTimeItem.setName("承运期");
		// 建议车型
		mSuggestTruckTypeItem = (AuctionItem) findViewById(R.id.suggest_truck_type);
		mSuggestTruckTypeItem.setBackground(R.color.white);
		mSuggestTruckTypeItem.setName("车辆类型");
		// mSuggestItem.setContent("不限车型");
		// 所需车长
		mSuggestTruckCarriageItem = (AuctionItem) findViewById(R.id.suggest_truck_carrige);
		mSuggestTruckCarriageItem.setBackground(R.color.white);
		mSuggestTruckCarriageItem.setName("车厢类型");
		// mNeedItem.setContent("2.5m");
		//所需车长
		mSuggestTruckLengthItem = (AuctionItem) findViewById(R.id.suggest_truck_length);
		mSuggestTruckLengthItem.setBackground(R.color.white);
		mSuggestTruckLengthItem.setName("所需车长");
		// 货物名称
		mGoodsName = (AuctionItem) findViewById(R.id.goods_name);
		mGoodsName.setBackground(R.color.list_item_background);
		mGoodsName.setName("货物名称");
		// mGoodsName.setContent("水泥");

		//结算单位
		settle_company = (AuctionItem) findViewById(R.id.settle_company);
		settle_company.setBackground(R.color.list_item_background);
		settle_company.setName("结算单位");
		// 付款方式
		mPayType = (AuctionItem) findViewById(R.id.pay_type);
		mPayType.setBackground(R.color.list_item_background);
		mPayType.setName("结算比例");
		// mPayType.setContent("到付");
		//结算方式
		mSettleType = (AuctionItem) findViewById(R.id.settle_type);
		mSettleType.setBackground(R.color.list_item_background);
		mSettleType.setName("结算方式");
		// 开发票
		mBill = (AuctionItem) findViewById(R.id.bill);
		mBill.setBackground(R.color.list_item_background);
		mBill.setName("运输发票");
		// mBill.setContent("11%");
		//剩余量
		mShengYu = (AuctionItem) findViewById(R.id.sheng_yu);
		mShengYu.setBackground(R.color.list_item_background);
		mShengYu.setName("剩余量");
		// 提示价
		//价格区间标记图
		mAuctionTypeText = (TextView) findViewById(R.id.auction_type);
		//价格TextView
		mPriceText = (TextView) findViewById(R.id.price);
		mAuctionTimeType = (TextView) findViewById(R.id.auction_time_type);
		mBidderNum = (TextView) findViewById(R.id.bidder_number);
		// 货物重量
		mGoodsWeight = (AuctionItem) findViewById(R.id.goods_weight);
		mGoodsWeight.setBackground(R.color.list_item_background);
		mGoodsWeight.setName("重量");
		// 体积
		mGoodsVolume = (AuctionItem) findViewById(R.id.goods_volume);
		mGoodsVolume.setBackground(R.color.list_item_background);
		mGoodsVolume.setName("体积");
		// 数量
		mGoodsNumber = (AuctionItem) findViewById(R.id.goods_number);
		mGoodsNumber.setBackground(R.color.list_item_background);
		mGoodsNumber.setName("数量");
		//运量
		mTransitMeasure = (AuctionItem) findViewById(R.id.transit_measure);
		mTransitMeasure.setBackground(R.color.list_item_background);
		mTransitMeasure.setName("运量");
		//型号
		mGoodsModelNumber = (AuctionItem) findViewById(R.id.goods_model_number);
		mGoodsModelNumber.setBackground(R.color.list_item_background);
		mGoodsModelNumber.setName("型号");
		//包装
		mGoodsPackageType = (AuctionItem) findViewById(R.id.goods_package_type);
		mGoodsPackageType.setBackground(R.color.list_item_background);
		mGoodsPackageType.setName("包装");
		// 运输里程
		mGoodsMileage = (AuctionItem) findViewById(R.id.goods_mileage);
		mGoodsMileage.setBackground(R.color.white);
		mGoodsMileage.setName("运输里程");
		mGoodsMileage.setVisibility(View.GONE);

		ImageView myItemViewIcon;
		// 联系货主
		mContactItem = (MyItemView) findViewById(R.id.contact_vector);
		mCallImage = mContactItem.getAuthImage(4);
		mCallImage.setImageResource(R.drawable.call);
		mContactItem.setItemName("联系货主");
		mContactItem.setOnClickListener(this);
		myItemViewIcon = mContactItem.getItemIconImage();
		myItemViewIcon.setVisibility(View.GONE);
		mContactItem.setVisibility(View.GONE);
		// 竞拍介绍
		mAuctionIntroduceItem = (MyItemView) findViewById(R.id.auction_introduce);
		mAuctionIntroduceItem.setItemName("接单介绍");
		mAuctionIntroduceItem.setOnClickListener(this);
		myItemViewIcon = mAuctionIntroduceItem.getItemIconImage();
		myItemViewIcon.setVisibility(View.GONE);
		// 备注
		remarkLayout = (RelativeLayout) findViewById(R.id.remark_layout);
		mRemarkText = (TextView) findViewById(R.id.remark_content);
		mAuctionBtn = (TextView) findViewById(R.id.auction_btn);
		mAuctionBtn.setOnClickListener(this);

		getAuctionInfo();
	}

	/**
	 *  初始化view的数据
	 */
	private void initView() {
		mSendAddressText.setText(mAuctionDetailsModel.getSendAddrWhole());
		mRecvAddressText.setText(mAuctionDetailsModel.getRecvAddrWhole());
		mGoodsName.setContent(mAuctionDetailsModel.getGoodsName());

		// 重量
		if (mAuctionDetailsModel.getGoodsWeight() > 0) {
			mGoodsWeight.setContent(Util.formatDoubleToString(mAuctionDetailsModel.getGoodsWeight(),
					mAuctionDetailsModel.getGoodsWeightUnitText())
					+ mAuctionDetailsModel.getGoodsWeightUnitText());
			mGoodsWeight.setVisibility(View.VISIBLE);
		}
		//体积
		if (mAuctionDetailsModel.getGoodsVolume() > 0) {
			mGoodsVolume.setContent(Util.formatDoubleToString(mAuctionDetailsModel.getGoodsVolume(),
					mAuctionDetailsModel.getGoodsVolumeUnitText())
					+ mAuctionDetailsModel.getGoodsVolumeUnitText());
			mGoodsVolume.setVisibility(View.VISIBLE);
		}
		//数量
		if (mAuctionDetailsModel.getGoodsCount() > 0) {
			mGoodsNumber.setContent(Util.formatDoubleToString(mAuctionDetailsModel.getGoodsCount(),
					mAuctionDetailsModel.getGoodsCountUnitText())
					+ mAuctionDetailsModel.getGoodsCountUnitText());
			mGoodsNumber.setVisibility(View.VISIBLE);
		}
		//运量
		if (mAuctionDetailsModel.getGoodsTrucks() > 0) {
			mTransitMeasure.setContent("" + mAuctionDetailsModel.getGoodsTrucks() + "车");
			mTransitMeasure.setVisibility(View.VISIBLE);
		}
		//型号
		if (mAuctionDetailsModel.getModelNumber() != null && mAuctionDetailsModel.getModelNumber().length() > 0) {
			mGoodsModelNumber.setContent(mAuctionDetailsModel.getModelNumber());
			mGoodsModelNumber.setVisibility(View.VISIBLE);
		}
		//包装
		if (mAuctionDetailsModel.getPackageType() != null && mAuctionDetailsModel.getPackageType().length() > 0) {
			mGoodsPackageType.setContent(mAuctionDetailsModel.getPackageType());
			mGoodsPackageType.setVisibility(View.VISIBLE);
		}

		if(mAuctionDetailsModel.getSenderCorporation() != null){
			settle_company.setContent(mAuctionDetailsModel.getSenderCorporation());
		}else{
			settle_company.setContent("");
		}

		if (mAuctionDetailsModel.getAuctionBillTemplate() != null && !mAuctionDetailsModel.getAuctionBillTemplate().equals("")) {
			mPayType.setContent(mAuctionDetailsModel.getAuctionBillTemplate());
		} else {
			mPayType.setContent("100%运费");
		}

		if(billPaymentWayList != null && billPaymentWayList.size() > 0){
			mSettleType.setVisibility(View.VISIBLE);
			mSettleType.setContent(billPaymentWayList.get(0).getName() + "(手续费" + billPaymentWayList.get(0).getFee() + "元)");
		}else {
			mSettleType.setVisibility(View.GONE);
		}
		//mBill.setContent(mAuctionDetailsModel.getInvoiceTypeText());
		//隐藏运输发票
		mBill.setVisibility(View.GONE);
		String price;
		if (mAuctionDetailsModel.getAuctionType() == CommonRes.FIXED_PRICE ||
				mAuctionDetailsModel.getAuctionType() == CommonRes.LONG_TRANSPORT) {
			//一口价与长期运输
//			mAuctionTypeText.setBackgroundResource(R.drawable.price_type);
			if ((mAuctionDetailsModel.getAuctionType() == CommonRes.FIXED_PRICE || mAuctionDetailsModel.getAuctionType() == CommonRes.LONG_TRANSPORT)
					&& mAuctionDetailsModel.getInnerGoods() == 1) {
				//内部货源 + 一口价或者接单运输
				mShengYu.setVisibility(View.VISIBLE);
				mShengYu.setContent(Util.formatDoubleToString(Double.valueOf(mAuctionDetailsModel.getGoodsAmountSp()), mAuctionDetailsModel.getAssignUnit()) + mAuctionDetailsModel.getAssignUnit());
			}
			price = Util.formatDoubleToString(mAuctionDetailsModel.getAuctionPrice(), "元");
		} else {
			//非一口价与长期运输
			ViewGroup.LayoutParams layoutParams = mAuctionTypeText.getLayoutParams();
			layoutParams.width = (int) getResources().getDimension(R.dimen.width_48);
			mAuctionTypeText.setBackgroundResource(R.drawable.price_type3);
			price = Util.formatDoubleToString(mAuctionDetailsModel.getLowPrice(), "元") + " ~ " +
					Util.formatDoubleToString(mAuctionDetailsModel.getHighPrice(), "元");
		}
		String price_str = price + "  元/" + mAuctionDetailsModel.getSettleUnitText();
		Util.setText(AuctionDetailsActivity.this, price_str, price,
				mPriceText, R.color.theme_color, (int) getResources().getDimension(R.dimen.width_20));
		mBidderNum.setText(mAuctionDetailsModel.getBidderAmount() + "人接单");
		// 时间倒计时
		if (!mAuctionDetailsModel.isBidStart()){
			//此时竞拍还未开始
			mBidTime = mAuctionDetailsModel.getDiffStartBid();
			mAuctionTimeType.setText("距离接单开始："
					+ Util.millisecondToDays(mBidTime));
		}else if (mAuctionDetailsModel.getDiffBidEnd() >= 0
				&& mAuctionDetailsModel.getStatus() == 3) {
			// 此时是竞拍中
			mBidTime = mAuctionDetailsModel.getDiffBidEnd();
			mAuctionTimeType.setText("距离接单结束："
					+ Util.millisecondToDays(mBidTime));
		} else if (mAuctionDetailsModel.getStatus() == 99) {
			// 此时是竞价已结束
			mAuctionTimeType.setText("接单已截止");
		}
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		};
		if (isFirst) {
			isFirst = false;
			timer = new Timer();
			timer.schedule(timerTask, 1000, 1000);
		}

		mSendTimeItem.setContent(Util.getFormateDateTimeShort(mAuctionDetailsModel.getGmtStartPeriod())
				+ "~"
				+ Util.getFormateDateTimeShort(mAuctionDetailsModel.getGmtEndPeriod()));

		List<String> mTruckModelList = mAuctionDetailsModel.getTruckModel();
		List<String> truckCarriageList = mAuctionDetailsModel.getTruckCarriage();
		List<String> truckLengthList = mAuctionDetailsModel.getTruckLength();
		if (mTruckModelList != null && mTruckModelList.size() > 0) {
			if (mTruckModelList.size() == 1 && mTruckModelList.get(0).equals("不限")) {
				mSuggestTruckTypeItem.setVisibility(View.GONE);
			} else {
				// 车辆类型
				setTruckModel(mTruckModelList, mSuggestTruckTypeItem);
			}
		} else {
			mSuggestTruckTypeItem.setVisibility(View.GONE);
		}
		if (truckCarriageList != null && truckCarriageList.size() > 0) {
			if (truckCarriageList.size() == 1 && truckCarriageList.get(0).equals("不限")) {
				mSuggestTruckCarriageItem.setVisibility(View.GONE);
			} else {
				// 车厢类型
				setTruckModel(truckCarriageList, mSuggestTruckCarriageItem);
			}
		} else {
			mSuggestTruckCarriageItem.setVisibility(View.GONE);
		}
		if (truckLengthList != null && truckLengthList.size() > 0) {
			//所需车长
			if (truckLengthList.size() == 1 && truckLengthList.get(0).equals("不限")) {
				mSuggestTruckLengthItem.setVisibility(View.GONE);
			} else {
				setTruckModel(truckLengthList, mSuggestTruckLengthItem);
			}
		} else {
			mSuggestTruckLengthItem.setVisibility(View.GONE);
		}
		mGoodsMileage.setContent(""
				+ Math.round(mAuctionDetailsModel.getDistance() / 1000.0)
				+ "公里");
		if (mAuctionDetailsModel.getRemark() != null && !mAuctionDetailsModel.getRemark().equals("")) {
			mRemarkText.setText(mAuctionDetailsModel.getRemark());
			remarkLayout.setVisibility(View.VISIBLE);
		} else {
			remarkLayout.setVisibility(View.GONE);
		}
		// 竞价单号
		mAuctionNumber.setText(mAuctionDetailsModel.getAuctionId());
		if (mAuctionDetailsModel.getGmtBid() > 0) {
			Date mDate = new Date();
			mDate.setTime(mAuctionDetailsModel.getGmtBid());
			// 竞价时间
			mAuctionTime.setText(Util.formatDateSecond(mDate));
		} else {
			mAuctionTime.setText("您还没有接单");
		}

		if (mAuctionDetailsModel.isBidStart()) {
			//已开始
			//如果竞价人的竞价时间有值，说明已经竞价
			if (mAuctionDetailsModel.getGmtBid() != 0) {
				if (mAuctionDetailsModel.getStatus() == 99) {
					mAuctionBtn.setText("已接单");
					mAuctionBtn.setBackgroundResource(R.color.gray);
					mAuctionBtn.setClickable(false);
				} else {
					mAuctionBtn.setText("取消接单");
					mAuctionBtn.setTextColor(getResources().getColor(R.color.theme_color));
					mAuctionBtn.setBackgroundResource(R.color.list_item_background);
				}

				//显示“我的竞价详情入口”
				order_layout.setVisibility(View.VISIBLE);
				myAuctionMessageLayout.setVisibility(View.VISIBLE);
				TextView myAuctionMessageText = (TextView) findViewById(R.id.my_auction_message_text);
				myAuctionMessageText.setText(getResources().getString(R.string.my) +
						getResources().getString(R.string.auction_message));
				myAuctionMessageLayout.setOnClickListener(this);
			} else {
				mAuctionBtn.setText(R.string.auction);
				mAuctionBtn.setTextColor(getResources().getColor(R.color.white));
				mAuctionBtn.setBackgroundResource(R.color.theme_color);
				mAuctionBtn.setClickable(true);
				myAuctionMessageLayout.setVisibility(View.GONE);
				order_layout.setVisibility(View.GONE);
			}

			//如果竞价单被货主取消发布 或者竞价单已截止
			if (mAuctionDetailsModel.getStatus() == 4 || mAuctionDetailsModel.getStatus() == 99) {
				mAuctionBtn.setBackgroundResource(R.color.gray);
				mAuctionBtn.setText("接单已截止");
				mAuctionBtn.setClickable(false);
			}
		} else {
			//未开始
			mAuctionBtn.setBackgroundResource(R.color.gray);
			mAuctionBtn.setText(R.string.auction);
			mAuctionBtn.setClickable(false);
		}
	}

	/**
	 * 遍历所有的车型
 	 */
	private void setTruckModel(List<String> list, AuctionItem mAuctionItem) {
		String s = "";
		if (list == null) {
			mAuctionItem.setContent(s);
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			s = s + list.get(i) + "  ";
		}
		mAuctionItem.setContent(s);
	}

	private void getAuctionInfo() {
		AjaxParams params = new AjaxParams();
		params.put("id", mAuctionId);
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog(AuctionDetailsActivity.this, "加载中");

		DidiApp.getHttpManager().sessionPost(AuctionDetailsActivity.this,
				auction_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						mPromptManager.closeProgressDialog();
						try {
							JSONObject jsonObject = new JSONObject(t);
							String all = jsonObject.getString("body");
							mAuctionDetailsModel = JSON.parseObject(all,
									AuctionDetailsModel.class);

							billPaymentWayList = mAuctionDetailsModel.getBillPaymentWayList();
							if (mAuctionDetailsModel != null){
								initView();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						mPromptManager.closeProgressDialog();
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.auction_btn:
				if (mAuctionDetailsModel != null && mAuctionDetailsModel.getGmtBid() != 0 && mAuctionDetailsModel.getStatus() != 99) {
					//取消竞价单dialog
					CancelDialog mCancelDialog = new CancelDialog(AuctionDetailsActivity.this, R.style.data_filling_dialog, mHandler);
					mCancelDialog.setCanceledOnTouchOutside(true);
					mCancelDialog.setHint("确定要取消此次接单？");
					mCancelDialog.setMsgCode(CommonRes.CANCELAUCTION);
					mCancelDialog.show();
					break;
				}
				if (mAuctionDetailsModel != null) {
					toAuctionOffer();
				} else {
					ToastUtil.show(AuctionDetailsActivity.this, "请求参数出错");
				}
				break;
			case R.id.address_order_layout:
				if (mAuctionDetailsModel == null) {
					ToastUtil.show(AuctionDetailsActivity.this, "请求参数出错");
					break;
				}
				Intent intent = new Intent(AuctionDetailsActivity.this,
						MapActivity.class);
				Bundle b = new Bundle();
				b.putString("mapType", "StartEnd");
				GoodsBubbleMsg startMsg = new GoodsBubbleMsg(),
						endMsg = new GoodsBubbleMsg();
				startMsg.setMessage(mAuctionDetailsModel.getSendProvince(),
						mAuctionDetailsModel.getSendCity(),
						mAuctionDetailsModel.getSendAddr());

				endMsg.setMessage(mAuctionDetailsModel.getRecvProvince(),
						mAuctionDetailsModel.getRecvCity(),
						mAuctionDetailsModel.getRecvAddr());

				b.putString("startProvinceAndCity", startMsg.getProvinceAndCity());
				b.putString("startLat", "" + mAuctionDetailsModel.getSendLat());
				b.putString("startLng", "" + mAuctionDetailsModel.getSendLng());
				b.putString("startAddr", startMsg.getAddr());

				b.putString("endProvinceAndCity", endMsg.getProvinceAndCity());
				b.putString("endLat", "" + mAuctionDetailsModel.getRecvLat());
				b.putString("endLng", "" + mAuctionDetailsModel.getRecvLng());
				b.putString("endAddr", endMsg.getAddr());
				intent.putExtras(b);
				startActivity(intent);
				break;
			case R.id.contact_vector:
				if (mAuctionDetailsModel != null
						&& !"".equals(mAuctionDetailsModel.getSenderPhone())){
					Util.call(AuctionDetailsActivity.this,
							mAuctionDetailsModel.getSenderPhone());
				}
				break;
			case R.id.auction_introduce:
				Intent mIntent = new Intent(AuctionDetailsActivity.this,
						WebViewWithTitleActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putString("title",
						getResources().getString(R.string.auction_introduce));
				mBundle.putString("url", GloableParams.WEB_URL + "description.html");
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
				break;
			case R.id.my_auction_message_layout:
				Intent info_intent = new Intent(AuctionDetailsActivity.this, MyAuctionBidMessageActivity.class);
				info_intent.putExtra("bidItemId", mAuctionDetailsModel.getBidItemId());
				startActivity(info_intent);
			default:
				break;
		}

	}

	private void toAuctionOffer() {
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(AuctionDetailsActivity.this,
				check_privilege_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						Intent offer_intent = new Intent(AuctionDetailsActivity.this, AuctionOfferActivity.class);
						offer_intent.putExtra("auctionId", mAuctionId);
						if((mAuctionDetailsModel.getInnerGoods() == 2) && "吨".equals(mAuctionDetailsModel.getAssignUnit())){
							offer_intent.putExtra("outer_goods_amount",mAuctionDetailsModel.getGoodsAmountSp());
						}
						startActivity(offer_intent);
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
					}
				});
	}
}