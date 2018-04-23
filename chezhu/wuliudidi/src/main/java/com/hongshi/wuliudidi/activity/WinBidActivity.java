package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.WaybillListAdapter;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AppListVO;
import com.hongshi.wuliudidi.model.ConsignDetailModel;
import com.hongshi.wuliudidi.model.TaskOrderModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.GoodsDetailsView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class WinBidActivity extends Activity{
	private DiDiTitleView mTitle;
	private PullToRefreshListView mPullToRefreshListView;
//	private GoodsDetailsView mGoodsDetailsView;
	private ListView mAssignOrderList;
	private WaybillListAdapter mWaybillListAdapter;
	//托运单ID
	private String mAuctionId = "";
//	private TextView mStatus;
	private String detail_url = GloableParams.HOST + "carrier/bid/consignDetail.do?";
	//查看更多信息
	private String more_detail_url = GloableParams.HOST + "carrier/transit/task/listmore.do?";
	//删除托运单
	private String del_url = GloableParams.HOST + "carrier/bid/delete.do?";
	private ConsignDetailModel mConsignDetailModel;
//	private TextView mAllCountText,mAllCount,mFinishedText,mFinished,mRestAmountText,mRestAmount,mAuctionPrice;
	private List<TaskOrderModel> transitTaskList;
	private boolean end = false;
	private String bidItemId = "";
	private String last = "";
	
	private TextView mOrderNumber,mOrderTime,fact_unit_text,fact_amount_text;
	private Button mDel;
	private boolean isRefreshing = false;
	private boolean tag = false;
	private View top_view;
	//以车为单位显示实际运量
	private RelativeLayout fact_layout;
	private List<TaskOrderModel> mTylist = new ArrayList<TaskOrderModel>();

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("WinBidActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("WinBidActivity");
	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.RefreshWinBid)) {
				//刷新
				if(bidItemId != null){
//					ToastUtil.show(WinBidActivity.this, "开始运输");
					requestData();
				}
			}else if(action.equals(CommonRes.NewTask)){
				requestData();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.win_bid_activity);
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setTitle("托运信息");
		mTitle.setBack(this);
		mTitle.setFocusable(true);
		mTitle.requestFocus();
		//获取托运单ID
		mAuctionId = getIntent().getExtras().getString("AuctionId");
		initView();
		//删除点击
		mDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != mConsignDetailModel && !mConsignDetailModel.getBidItemId().equals("")){
					delOrder(mConsignDetailModel.getBidItemId());
				}
			}
		});
		
		// 派车单list
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.assign_order);
		mAssignOrderList = mPullToRefreshListView.getRefreshableView();
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");  
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");  
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多"); 
		
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");  
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");  
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");  
		
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!isRefreshing) {
					isRefreshing = true;
					if (mPullToRefreshListView.isHeaderShown()){
						requestData();
						
					} else if (mPullToRefreshListView.isFooterShown()) {
						if(mTylist.size()>0){
							// 加载更多
							if(end){
								tag = true;
								ToastUtil.show(WinBidActivity.this, "已经是最后一页");
							}
							loadMore();
						}else{
							ToastUtil.show(WinBidActivity.this, "已经是最后一页");
							requestData();
						}
					}
				} else {
//					mPullToRefreshListView.onRefreshComplete();
					isRefreshing = false;
				}

			}
		});
		mAssignOrderList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(null == transitTaskList.get(position-1).getPlanId() && position != 1){
					Intent intent = new Intent(WinBidActivity.this,TruckingOrderDetailsActivity.class);
					intent.putExtra("transitId", transitTaskList.get(position-1).getTransitTaskId());
					startActivity(intent);
				}
			}
		});
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.RefreshWinBid);
		intentFilter.addAction(CommonRes.NewTask);
		registerReceiver(mRefreshBroadcastReceiver, intentFilter);
	}
	private void delOrder(String bidItemId) {
		AjaxParams params = new AjaxParams();
		params.put("bidItemId", bidItemId);
		DidiApp.getHttpManager().sessionPost(WinBidActivity.this, del_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Toast.makeText(WinBidActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				Intent refresh_intent = new Intent();
				refresh_intent.setAction(CommonRes.RefreshData);
				sendBroadcast(refresh_intent);	
				finish();
			}
		});
	}
	private void initView() {
		mOrderNumber = (TextView) findViewById(R.id.order_number);
		mOrderTime = (TextView) findViewById(R.id.order_time);
		fact_unit_text = (TextView) findViewById(R.id.fact_unit_text);
		fact_amount_text = (TextView) findViewById(R.id.fact_amount_text);
		top_view = findViewById(R.id.top_view);
		fact_layout = (RelativeLayout) findViewById(R.id.fact_layout);
		mDel = (Button) findViewById(R.id.del);
		requestData();
	}
	//请求详情数据
	private void requestData() {
		AjaxParams params = new AjaxParams();
		params.put("bidItemId", mAuctionId);
		DidiApp.getHttpManager().sessionPost(WinBidActivity.this, detail_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				isRefreshing = false;
				end = false;
				tag = false;
				mPullToRefreshListView.onRefreshComplete();
				try {
					jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");	
					mConsignDetailModel = JSON.parseObject(all,ConsignDetailModel.class);
					setViewData(mConsignDetailModel);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPullToRefreshListView.onRefreshComplete();
			}
		});
	}
	//获取到数据，更新
	private void setViewData(ConsignDetailModel mConsignDetailModel){
		if(mConsignDetailModel.getShipmentStatus() == 3 || mConsignDetailModel.getShipmentStatus() == 4 || mConsignDetailModel.getShipmentStatus() == 7){
//			mDel.setVisibility(View.VISIBLE);//暂时去掉删除的功能
		}else{
			mDel.setBackgroundResource(R.color.gray);
			mDel.setClickable(false);
		}
		if(mConsignDetailModel.getAssignUnit().equals("TRUCK")){
			fact_layout.setVisibility(View.VISIBLE);
			String unit = mConsignDetailModel.getSettleUnitText();
			fact_unit_text.setText("实际运量(" + unit + ")");
			fact_amount_text.setText(Util.formatDoubleToString(mConsignDetailModel.getSettleAmount(), unit));
		}else{
			fact_layout.setVisibility(View.GONE);
		}
		
		mOrderTime.setText("托运时间    "+Util.formatDate(mConsignDetailModel.getGmtAudit()));
		mOrderNumber.setText("托运单号    "+mConsignDetailModel.getBidItemId());
		
		if(transitTaskList != null && transitTaskList.size()>0){
			transitTaskList.clear();
		}
		//获取派车计划的列表
		List<TaskOrderModel> planList = mConsignDetailModel.getPlanList();
		if(null != planList){
			transitTaskList = planList;
		}
		AppListVO transitList = mConsignDetailModel.getTransitTaskList();
		mTylist = transitList.getItems();
		transitTaskList.addAll(mTylist);

		end = transitList.isEnd();
		if(transitTaskList!=null && transitTaskList.size()>0){
			bidItemId = transitTaskList.get(transitTaskList.size()-1).getBidItemId();
			last = transitTaskList.get(transitTaskList.size()-1).getTransitTaskId();
			mWaybillListAdapter = new WaybillListAdapter(WinBidActivity.this,transitTaskList,mConsignDetailModel);
			
			//如果已经显示完数据
			mWaybillListAdapter.setIsEnd(transitList.isEnd());
			if(mConsignDetailModel.getGmtFinish() != null){
				mWaybillListAdapter.setConsignMessage(mConsignDetailModel.getBidItemId(),
						Util.formatDateSecond(mConsignDetailModel.getGmtAudit()),
						Util.formatDateSecond(mConsignDetailModel.getGmtFinish()),
						"结算方式：" + mConsignDetailModel.getBillTempalte());
			}else{
				mWaybillListAdapter.setConsignMessage(mConsignDetailModel.getBidItemId(),
						Util.formatDateSecond(mConsignDetailModel.getGmtAudit()),
						"",
						"结算方式：" + mConsignDetailModel.getBillTempalte());
			}
			mAssignOrderList.setAdapter(mWaybillListAdapter);
		}else{
			//处理没有数据的时候显示竞价信息
			top_view.setVisibility(View.VISIBLE);
			initTop(mConsignDetailModel);
			mPullToRefreshListView.setVisibility(View.GONE);
		}
	}
	//当没有派车任务的时候显示头部信息
	private void initTop(final ConsignDetailModel mConsignDetailModel){
		TextView mAllCountText,mAllCount,mFinishedText,mFinished,mRestAmountText,mRestAmount,mAuctionPrice,mStatus;
		RelativeLayout mLookTruckLayout;
		GoodsDetailsView mGoodsDetailsView;
		mStatus = (TextView) findViewById(R.id.status);
		mAllCountText = (TextView) findViewById(R.id.all_count_text);
		mAllCount = (TextView) findViewById(R.id.all_count);
		mFinishedText = (TextView) findViewById(R.id.finished_text);
		mFinished = (TextView) findViewById(R.id.finished);
		mRestAmountText = (TextView) findViewById(R.id.rest_amount_text);
		mRestAmount = (TextView) findViewById(R.id.rest_amount);
		mAuctionPrice = (TextView) findViewById(R.id.price_);
		mGoodsDetailsView = (GoodsDetailsView)findViewById(R.id.auction_number_view);
		LinearLayout bidPriceAndTruckLayout = (LinearLayout) findViewById(R.id.bidprice_trucks_layout);
		if(CommonRes.roleType == 1){
			//如果用为普通司机
			bidPriceAndTruckLayout.setVisibility(View.GONE);
		}
		
		mGoodsDetailsView.setAuctionNumber(mConsignDetailModel.getAuctionId());
		TextView auctionNumberTextView = mGoodsDetailsView.getAuctionNumberTextView();
		//运输状态
		mStatus.setText(mConsignDetailModel.getShipmentStatusText());
		String assignUnitText = mConsignDetailModel.getAssignUnitText();
		mAllCountText.setText("运输总量("+ assignUnitText +")");
		mFinishedText.setText("已运量("+ assignUnitText +")");
		mRestAmountText.setText("剩余量("+ assignUnitText +")");
		mAllCount.setText(Util.formatDoubleToString(mConsignDetailModel.getTotalAmount(), assignUnitText));
		mFinished.setText(Util.formatDoubleToString(mConsignDetailModel.getFinishedAmount(), assignUnitText));
		mRestAmount.setText(Util.formatDoubleToString(mConsignDetailModel.getRestAmount(), assignUnitText));
		
		mGoodsDetailsView.setStartCity(mConsignDetailModel.getSendAddr());
		mGoodsDetailsView.setEndCity(mConsignDetailModel.getRecvAddr());
		mGoodsDetailsView.setGoodsName(mConsignDetailModel.getGoodsName());
		mGoodsDetailsView.setGoodsWeight(Util.formatDoubleToString(mConsignDetailModel.getGoodsAmount(),assignUnitText)
				+ mConsignDetailModel.getAssignUnitText());
		// 显示竞拍单号
		mGoodsDetailsView.showAuctionOrder();
		// 隐藏竞拍人数
		mGoodsDetailsView.hideJoinView();
		mGoodsDetailsView.setHideTime();
		auctionNumberTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WinBidActivity.this, AuctionDetailsActivity.class);
				intent.putExtra("auctionId", mConsignDetailModel.getAuctionId());
				startActivity(intent);
			}
		});
		//查看竞拍车辆
		mLookTruckLayout = (RelativeLayout) findViewById(R.id.look_truck_layout);
		
		mAuctionPrice.setText(Util.formatDoubleToString(mConsignDetailModel.getBidPrice(), "元")
				+ "元/" + mConsignDetailModel.getSettleUnitText());//中标价格
		
		mLookTruckLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WinBidActivity.this,AuctionTruckListActivity.class);
				intent.putExtra("bidId", mConsignDetailModel.getBidItemId());
				startActivity(intent);	
			}
		});
		
		
		LinearLayout bottonLayout = (LinearLayout) findViewById(R.id.consign_finish_time_layout);
		bottonLayout.setVisibility(View.VISIBLE);
		TextView tydNumber = (TextView) findViewById(R.id.tyd_number);
		TextView creatTime = (TextView) findViewById(R.id.creat_time);
		TextView finishTime = (TextView) findViewById(R.id.finish_time);
		TextView payWayText = (TextView) findViewById(R.id.pay_way_text);

		tydNumber.setText("托运单号： " + mConsignDetailModel.getBidItemId());
		creatTime.setText("创建时间： " + Util.formatDateSecond(mConsignDetailModel.getGmtAudit()));
		if(mConsignDetailModel.getGmtFinish() != null){
			finishTime.setText("完成时间：" + Util.formatDateSecond(mConsignDetailModel.getGmtFinish()));
		}else{
			finishTime.setVisibility(View.GONE);
		}
		
		if(mConsignDetailModel.getBillTempalte() != null && mConsignDetailModel.getBillTempalte().length() > 0){
			payWayText.setText("结算方式：" + mConsignDetailModel.getBillTempalte());
			payWayText.setVisibility(View.VISIBLE);
		}else{
			payWayText.setVisibility(View.GONE);
		}
	}
	private void loadMore(){
		AjaxParams params = new AjaxParams();
		params.put("bidItemId", bidItemId);
		params.put("last", last);
		DidiApp.getHttpManager().sessionPost(WinBidActivity.this, more_detail_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				isRefreshing = false;
				mPullToRefreshListView.onRefreshComplete();
				if(tag){
					return;
				}else{
					try {
						jsonObject = new JSONObject(t);
						JSONObject body = jsonObject.getJSONObject("body");
						String all = body.getString("items");
						List<TaskOrderModel> transitTaskList = JSON.parseArray(all,TaskOrderModel.class);
						if(transitTaskList != null && transitTaskList.size()>0){
							bidItemId = transitTaskList.get(transitTaskList.size()-1).getBidItemId();
							last = transitTaskList.get(transitTaskList.size()-1).getTransitTaskId();
							
							//如果已经显示完数据
							mWaybillListAdapter.setIsEnd(body.getBoolean("end"));
							mWaybillListAdapter.addList(transitTaskList);
						}
						end = body.getBoolean("end");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPullToRefreshListView.onRefreshComplete();
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mRefreshBroadcastReceiver);
	}
}
