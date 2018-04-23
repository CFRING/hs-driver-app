package com.hongshi.wuliudidi.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AuctionDetailsActivity;
import com.hongshi.wuliudidi.activity.MainActivity;
import com.hongshi.wuliudidi.activity.TruckingOrderDetailsActivity;
import com.hongshi.wuliudidi.activity.WinBidActivity;
import com.hongshi.wuliudidi.adapter.TaskPlanAdapter;
import com.hongshi.wuliudidi.adapter.TransportationTaskAdapter;
import com.hongshi.wuliudidi.adapter.TransportationTaskAdapterNew;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.dialog.UploadReceiptDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.model.AllAuctionModel;
import com.hongshi.wuliudidi.model.AllAuctionModelForEavelate;
import com.hongshi.wuliudidi.model.BidItemQueryVO;
import com.hongshi.wuliudidi.model.TaskPlanModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.photo.GetPhotoUtil;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.NoLoginView;
import com.hongshi.wuliudidi.view.NullDataView;
import com.umeng.analytics.MobclickAgent;

public class TaskModifyFragment extends Fragment implements View.OnClickListener{
	private ImageButton back_top;
	private View mView;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mTaskListview;
	private TaskPlanAdapter mAdapter;
	private List<TaskPlanModel> taskPlanList = new ArrayList<TaskPlanModel>();
	private NullDataView nullDataView;
	private NoLoginView noLoginView;
	private int currPage = 0;
	private int pageTotal = -1;
	private int pageSize = 10;
	//派车单ID，计价单位
	private String taskId, unitText;
	private DataFillingDialog mImageDialog;
	//任务url
	private String task_url = GloableParams.HOST + "carrier/transit/plan/findTaskForApp.do";
	private TextView upload_receipt_text,order_record_text,transit_record_text,search_tip;
	private EditText search_input;
	private RelativeLayout search_container;
	private boolean isSearchFirstLoadMore = false;
	private UploadReceiptDialog uploadDialog;

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {  
 	    @Override  
 	     public void onReceive(Context context, Intent intent) {  
 	        String action = intent.getAction();  
			if (action.equals(CommonRes.RefreshData)) {
				init();
			}else if(action.equals(CommonRes.NewTask)){

			}else if(action.equals(CommonRes.MessageFromTransit)){
				init();
			}else if (action.equals(CommonRes.ACTIONPHOTOPATH)) {
				String imagePath = intent.getStringExtra("imagePath");
				int tag = intent.getExtras().getInt("tag", -1);
				int amount = intent.getIntExtra("amount",0);
				String picUri = intent.getStringExtra("pic");
				if(tag == CommonRes.TYPE_UPLOAD_HUIDAN){
//					Log.i("http","上传回单");
					if(picUri != null && !"".equals(picUri)){
						try {
							uploadDialog = new UploadReceiptDialog(getActivity(),
									R.style.data_filling_dialog, mHandler, taskId, picUri, unitText);
							UploadUtil.setAnimation(uploadDialog, CommonRes.TYPE_BOTTOM, true);
							uploadDialog.setCanceledOnTouchOutside(false);
							uploadDialog.show();
						}catch (Exception e){
						}
					}else {
						//得到图片路径
						uploadDialog = new UploadReceiptDialog(getActivity(),
								R.style.data_filling_dialog, mHandler, taskId, imagePath, unitText);
						UploadUtil.setAnimation(uploadDialog, CommonRes.TYPE_BOTTOM, true);
						uploadDialog.setCanceledOnTouchOutside(false);
						uploadDialog.show();
					}
				}
			}else if(action.equals(CommonRes.UploadReceiptFinished)){
				//派车单签收单据上传完毕
				currPage = 0;
				taskPlanList.clear();
				search_input.setText("");
				//刷新列表
				taskData(currPage,false);
			}else if((CommonRes.DriverTask).equals(action)){
				init();
			}else if(CommonRes.UploadTakeOrdersCaFinished.equals(action)){
//				currPage = 1;
//				taskPlanList.clear();
//				taskData(currPage,false);//刷新列表
			}else if(CommonRes.Evaluate_Refresh.equals(action)){
				loadTransitData();
			}
 	      }  
 	};
 	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonRes.CAMERA:
				GetPhotoUtil.callCamera(getActivity(),msg.arg1);
				break;
			case CommonRes.GALLERY:
				GetPhotoUtil.callGallery(getActivity());
				break;
			case CommonRes.TYPE_CALLBACK_PCD_ID:
				Bundle data = msg.getData();
				//得到传递的派车单ID和计价单位，赋值
				try {
					taskId = data.getString("taskId");
					unitText = data.getString("unitText");
				} catch (Exception e) {
				}
				break;
			case CommonRes.RENEW_UPLOAD_PHOTO:
				//重新选图:0 表示拍照或者图库选取
				mImageDialog = new DataFillingDialog(getActivity(), R.style.data_filling_dialog, mHandler, 0);
				mImageDialog.setCanceledOnTouchOutside(true);
				mImageDialog.setText("拍照", "图库选取");
				mImageDialog.show();
				UploadUtil.setAnimation(mImageDialog, CommonRes.TYPE_BOTTOM, false);
				break;
			default:
				break;
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.task_modify_fragment, null);

		activity = getActivity();
		back_top = (ImageButton) mView.findViewById(R.id.back_top);
		mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.task_plan_listview);
		nullDataView = (NullDataView) mView.findViewById(R.id.no_data_view);
		nullDataView.setInfoHint("您没有相关任务信息");
		noLoginView = (NoLoginView) mView.findViewById(R.id.no_login_layout);
		upload_receipt_text = (TextView) mView.findViewById(R.id.upload_receipt_text);
		order_record_text = (TextView) mView.findViewById(R.id.order_record_text);
		transit_record_text = (TextView) mView.findViewById(R.id.transit_record_text);
		search_tip = (TextView) mView.findViewById(R.id.search_tip);
		search_input = (EditText) mView.findViewById(R.id.search_input);
		search_container = (RelativeLayout) mView.findViewById(R.id.search_container);
		search_tip.setOnClickListener(this);
		upload_receipt_text.setOnClickListener(this);
		order_record_text.setOnClickListener(this);
		transit_record_text.setOnClickListener(this);

		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");  
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");  
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多"); 
		
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");  
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");  
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新"); 
		
		mTaskListview = mPullToRefreshListView.getRefreshableView();
		mTaskListview.setDivider(null);
		
		// 注册刷新订单广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.RefreshData);
		intentFilter.addAction(CommonRes.MessageFromTransit);
		intentFilter.addAction(CommonRes.NewTask);
		//得到图片的action
		intentFilter.addAction(CommonRes.ACTIONPHOTOPATH);
		//派车单签收单据上传完毕后刷新任务列表
		intentFilter.addAction(CommonRes.UploadReceiptFinished);
		//上传提货凭证成功后刷新任务列表
		intentFilter.addAction(CommonRes.UploadTakeOrdersCaFinished);
		//评价完刷新
		intentFilter.addAction(CommonRes.Evaluate_Refresh);
		activity.registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		init();
		return mView;
	}
	
	private void init() {
		if(uploadDialog != null){
			uploadDialog.dismiss();
			uploadDialog = null;
		}
		String tagTabStr = activity.getSharedPreferences("config",Context.MODE_PRIVATE).
				getString("from_integral","");
		if(tagTabStr != null && !"".equals(tagTabStr)){
			//任务页面
			MainActivity.mPager.setCurrentItem(0);
			Intent intent = new Intent();
			intent.setAction("from_task_fragment");
			activity.sendBroadcast(intent);
		}
		if("evaluation".equals(tagTabStr)){
			activity.getSharedPreferences("config",Context.MODE_PRIVATE).
					edit().putString("from_integral","").commit();
			search_container.setVisibility(View.GONE);
			setTextViewUnSelectedStyle(transit_record_text,upload_receipt_text,order_record_text);
			initListView(3);
			loadTransitData();
		}else {
			activity.getSharedPreferences("config",Context.MODE_PRIVATE).
					edit().putString("from_integral","").commit();
			setTextViewUnSelectedStyle(upload_receipt_text,order_record_text,transit_record_text);
			if(Util.isLogin(activity)){
				mPullToRefreshListView.setVisibility(View.VISIBLE);
				noLoginView.setVisibility(View.GONE);
				nullDataView.setVisibility(View.GONE);
//			loadData();
				search_input.setText("");
				taskPlanList.clear();
				taskData(0,false);
			}else{
				//未登录
				mPullToRefreshListView.setVisibility(View.GONE);
				noLoginView.setVisibility(View.VISIBLE);
				nullDataView.setVisibility(View.GONE);
			}

			initListView(1);
		}

	}

	private void initListView(int type){
		if(type == 1){
			//上传回单
			mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if(mPullToRefreshListView.isRefreshing()){
						if (mPullToRefreshListView.isHeaderShown()) {
							//下拉刷新
							currPage = 0;
							taskPlanList.clear();
							search_input.setText("");
							isSearchFirstLoadMore = false;
							taskData(currPage,false);
						}else if (mPullToRefreshListView.isFooterShown()) {
							if(isSearchFirstLoadMore){
								CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
								closeRefreshTask.execute();
								return;
							}
							search_input.setText("");
							// 加载更多
							if(currPage + 1 == pageTotal){
								ToastUtil.show(activity, "已经是最后一页");
								CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
								closeRefreshTask.execute();
							}else{
								taskData(currPage+1,true);
							}
						}
					}
				}
			});
			mTaskListview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {

					if(taskPlanList.size() > 0 && (taskPlanList.get(position-1).getOutBizType()==2)){
						//判断是否属于派车单
						TaskPlanModel taskPlanModel = taskPlanList.get(position-1);
						Intent intent = new Intent(activity,TruckingOrderDetailsActivity.class);
						intent.putExtra("transitId", taskPlanModel.getOutBizId());
						boolean isShowBtn =  true;
						if(taskPlanModel.getSalerBizID()!= null){
							if(taskPlanModel.isOutLib()){
								isShowBtn = true;
							}else {
								isShowBtn = false;
							}
						}
						intent.putExtra("isShowBtn",isShowBtn);
						activity.startActivity(intent);
					}
				}
			});
		}else if(type == 2){
			//接单记录
			mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (mPullToRefreshListView.isRefreshing()) {
						if (mPullToRefreshListView.isHeaderShown()) {
							//刷新
							currPage = 1;
							loadData();
						} else if (mPullToRefreshListView.isFooterShown()) {
							// 加载更多
							if(isEnd){
//                            tag = true;
								ToastUtil.show(activity, "已经是最后一页");
								CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
								closeRefreshTask.execute();
								return;
							}
							currPage = currPage + 1;
							loadMoreData();

						}
					}
				}


			});
			mTaskListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					AllAuctionModel mModel = mList.get(position - 1);
					//1待审核, 2审核中, 3已取消, 4审核未通过, 5运输未开始, 6运输中, 7已完成
					ArrayList<Integer> jumpToJJD = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
					ArrayList<Integer> jumpToTYD = new ArrayList<Integer>(Arrays.asList(5, 6, 7));

					//1待审核, 2审核中, 3已取消, 4审核未通过  四种状态的竞价项目点击跳转到竞价单详情页面
					if(jumpToJJD.contains(mModel.getStatus())){
						Intent intent = new Intent(activity,AuctionDetailsActivity.class);
						intent.putExtra("auctionId", mModel.getAuctionId());
						startActivity(intent);
					}else if(jumpToTYD.contains(mModel.getStatus())){
						//5运输未开始, 6运输中, 7已完成 三种状态的竞价项目点击跳转到托运详情页面
						Intent intent = new Intent(activity,WinBidActivity.class);
						intent.putExtra("AuctionId", mModel.getBidItemId());
						startActivity(intent);
					}
				}

			});
			currPage = 1;
//			loadData();
		}else {
			//运输任务
			mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (mPullToRefreshListView.isRefreshing()) {
						if (mPullToRefreshListView.isHeaderShown()) {
							loadTransitData();
						} else if (mPullToRefreshListView.isFooterShown()) {
							// 加载更多
							if (isEnd) {
								ToastUtil.show(activity, getString(R.string.end_page));
								CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
								closeRefreshTask.execute();
								return;
							}
							currPage = currPage + 1;
							loadMoreTransitData();
						}
					}
				}
			});
			mTaskListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					Intent intent = new Intent(activity, WinBidActivity.class);
					intent.putExtra("AuctionId", mAllAuctionList.get(position - 1).getBidItemId());
					startActivity(intent);
				}

			});
		}
	}

	private boolean isSearch = false;
	private void taskData(final int currentPage,final boolean isLoadMore) {
		task_url = GloableParams.HOST + "carrier/transit/plan/findTaskForApp.do";
		AjaxParams params = new AjaxParams();
		//每页显示的条目
		params.put("pageSize", "10");
		params.put("currentPage",""+ currentPage);
		if(isSearch){
			params.put("truckNumber",search_input.getText().toString());
		}
		DidiApp.getHttpManager().sessionPost(activity, task_url, params, new ChildAfinalHttpCallBack() {
			
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPullToRefreshListView.onRefreshComplete();
//				mPullToRefreshListView.setVisibility(View.GONE);
				noLoginView.setVisibility(View.GONE);
//				nullDataView.setVisibility(View.VISIBLE);
				isSearch = false;
			}
			
			@Override
			public void data(String t) {
				mPullToRefreshListView.onRefreshComplete();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.optString("list");
//					isEnd = body.optBoolean("end");
					//获取当前页
					currPage = body.optInt("currentPage");
					//获取总页数
					pageTotal = body.optInt("pageTotal");
//					taskPlanList = JSON.parseArray(all, TaskPlanModel.class);

					if(!isLoadMore && taskPlanList.size() > 0){
						taskPlanList.clear();
					}
					taskPlanList.addAll(JSON.parseArray(all, TaskPlanModel.class));
					if(taskPlanList.size() <= 0){
//						mPullToRefreshListView.setVisibility(View.GONE);
						noLoginView.setVisibility(View.GONE);
//						nullDataView.setVisibility(View.VISIBLE);
						Toast.makeText(activity,"无相关数据",Toast.LENGTH_LONG).show();
					}
					if(isLoadMore){
						mAdapter.notifyDataSetChanged();
					}else{
						mAdapter = new TaskPlanAdapter(activity, taskPlanList,mHandler);
						mTaskListview.setAdapter(mAdapter);
					}
					if(currentPage > 1){
						back_top.setVisibility(View.VISIBLE);
						back_top.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								mTaskListview.setSelection(0);
							}
						});
					}else {
						back_top.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mPullToRefreshListView.onRefreshComplete();
					if(taskPlanList != null && taskPlanList.size() > 0){
						taskPlanList.clear();
					}
					mAdapter = new TaskPlanAdapter(activity, taskPlanList,mHandler);
					mTaskListview.setAdapter(mAdapter);
					if (isSearch)
					{
						Toast.makeText(activity,"无相关数据",Toast.LENGTH_LONG).show();
					}
					mPullToRefreshListView.setVisibility(View.VISIBLE);
//					noLoginView.setVisibility(View.GONE);
//					nullDataView.setVisibility(View.VISIBLE);
				}finally {
					isSearch = false;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		activity.getSharedPreferences("config",Context.MODE_PRIVATE).
				edit().putString("from_integral","").commit();
		switch (v.getId()){
			case R.id.search_tip:
				isSearch = true;
				isSearchFirstLoadMore = true;
				taskData(0,false);
				break;
			case R.id.upload_receipt_text:
				search_container.setVisibility(View.VISIBLE);
				setTextViewUnSelectedStyle(upload_receipt_text,order_record_text,transit_record_text);
				initListView(1);
				search_input.setText("");
				taskData(0,false);
				break;
			case R.id.order_record_text:
				search_container.setVisibility(View.GONE);
				setTextViewUnSelectedStyle(order_record_text,upload_receipt_text,transit_record_text);
				initListView(2);
				loadData();
				break;
			case R.id.transit_record_text:
				search_container.setVisibility(View.GONE);
				setTextViewUnSelectedStyle(transit_record_text,upload_receipt_text,order_record_text);
				initListView(3);
				loadTransitData();
				break;
			default:
				break;
		}
	}

	private void setTextViewUnSelectedStyle(TextView selectedView,TextView unselectedView1,TextView unselectedView2){
		selectedView.setTextColor(getResources().getColor(R.color.title_background));
		selectedView.setBackgroundColor(getResources().getColor(R.color.white));
		unselectedView1.setTextColor(getResources().getColor(R.color.white));
		unselectedView1.setBackgroundColor(getResources().getColor(R.color.title_background));
		unselectedView2.setTextColor(getResources().getColor(R.color.white));
		unselectedView2.setBackgroundColor(getResources().getColor(R.color.title_background));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("TaskModifyFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("TaskModifyFragment");
	}

	/**
	* 接单记录
	* */
	private String all_url = GloableParams.HOST + "carrier/bid/allconsign.do";
	private List<AllAuctionModel> mAuctionList = new ArrayList<AllAuctionModel>();
	private List<AllAuctionModel> mMoreAuctionList = new ArrayList<AllAuctionModel>();
	private List<AllAuctionModel> mList = new ArrayList<AllAuctionModel>();
	private TransportationTaskAdapter mTransportationTaskAdapter;
	private TransportationTaskAdapterNew mTransportationTaskAdapterNew;
	private boolean isEnd = false;
	private BidItemQueryVO bidItemQueryVO = new BidItemQueryVO();

	private void loadMoreData() {
		all_url = GloableParams.HOST + "carrier/bid/allconsign.do";
		AjaxParams params = new AjaxParams();
		params.put("currentPage",""+currPage);
		params.put("pageSize",""+pageSize);
		bidItemQueryVO.setQueryStatus(0);
		params.put("queryJson",JSON.toJSONString(bidItemQueryVO));
		DidiApp.getHttpManager().sessionPost(activity, all_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					mPullToRefreshListView.onRefreshComplete();
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.getString("items");
					mMoreAuctionList = JSON.parseArray(all,AllAuctionModel.class);
					mList.addAll(mMoreAuctionList);
//						mAuctionList.addAll(mMoreAuctionList);
					mTransportationTaskAdapter.addList(mMoreAuctionList);
					mTransportationTaskAdapter.notifyDataSetChanged();
					isEnd = body.getBoolean("end");
					currPage = body.optInt("currentPage");
					if(currPage > 1){
						back_top.setVisibility(View.VISIBLE);
						back_top.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								mTaskListview.setSelection(0);
							}
						});
					}else {
						back_top.setVisibility(View.GONE);
					}
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
	private void loadData() {
		all_url = GloableParams.HOST + "carrier/bid/allconsign.do";
		AjaxParams params = new AjaxParams();
		params.put("currentPage",""+currPage);
		params.put("pageSize",""+pageSize);
		bidItemQueryVO.setQueryStatus(0);
		params.put("queryJson",JSON.toJSONString(bidItemQueryVO));
		DidiApp.getHttpManager().sessionPost(activity, all_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				isEnd = false;
//                tag = false;
				mPullToRefreshListView.onRefreshComplete();
				JSONObject jsonObject;
				if (mAuctionList.size() > 0){
					mAuctionList.clear();
				}
				if (mList.size() > 0){
					mList.clear();
				}
				try {
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.getString("items");
					mAuctionList = JSON.parseArray(all,AllAuctionModel.class);
					mList.addAll(mAuctionList);
					mTransportationTaskAdapter = new TransportationTaskAdapter(activity, mAuctionList,new RefreshAdapterCallBack() {
						@Override
						public void isAccept(boolean args) {
							currPage = 1;
							loadData();//操作成功重新加载数据
						}
					});
					mTransportationTaskAdapter.setShowType(TransportationTaskAdapter.ShowType.ShowJJD);
					mTaskListview.setAdapter(mTransportationTaskAdapter);
					mTransportationTaskAdapter.notifyDataSetChanged();
					isEnd = body.getBoolean("end");
					currPage = body.optInt("currentPage");
					if(mAuctionList.size() > 0){
						nullDataView.setVisibility(View.GONE);
						mPullToRefreshListView.setVisibility(View.VISIBLE);
					}else {
						nullDataView.setVisibility(View.VISIBLE);
						mPullToRefreshListView.setVisibility(View.GONE);
					}
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

	private Activity activity;

	/**
	* 运输记录
	* */
	private List<AllAuctionModelForEavelate> mAllAuctionList = new ArrayList<>();
	private void loadTransitData() {
		all_url = GloableParams.HOST + "carrier/bid/allconsign.do";
		AjaxParams params = new AjaxParams();
		currPage = 1;
		params.put("currentPage",""+currPage);
		params.put("pageSize",""+pageSize);
		bidItemQueryVO.setQueryStatus(4);
		params.put("queryJson",JSON.toJSONString(bidItemQueryVO));
		DidiApp.getHttpManager().sessionPost(activity,all_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				if(t == null || "".equals(t)){
					nullDataView.setVisibility(View.VISIBLE);
					mPullToRefreshListView.setVisibility(View.GONE);
					return;
				}else {
					nullDataView.setVisibility(View.GONE);
					mPullToRefreshListView.setVisibility(View.VISIBLE);
				}
				isEnd = false;
				mPullToRefreshListView.onRefreshComplete();
				JSONObject jsonObject;
//                if (mAuctionList.size() > 0){
//                    mAuctionList.clear();
//                }
				if (mAllAuctionList.size() > 0){
					mAllAuctionList.clear();
				}
				try {
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.getString("items");
					mAllAuctionList = JSON.parseArray(all,AllAuctionModelForEavelate.class);
//                    mAllAuctionList.addAll(mAuctionList);
					mTransportationTaskAdapterNew = new TransportationTaskAdapterNew(activity, mAllAuctionList,new RefreshAdapterCallBack() {
						@Override
						public void isAccept(boolean args) {
							loadTransitData();
						}
					});
					mTaskListview.setAdapter(mTransportationTaskAdapterNew);
//                    mTransportationTaskAdapter.notifyDataSetChanged();
					currPage = body.getInt("currentPage");
					isEnd = body.getBoolean("end");
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

	private void loadMoreTransitData() {
		all_url = GloableParams.HOST + "carrier/bid/allconsign.do";
		AjaxParams params = new AjaxParams();
		params.put("currentPage",""+currPage);
		params.put("pageSize",""+pageSize);
		bidItemQueryVO.setQueryStatus(4);
		params.put("queryJson",JSON.toJSONString(bidItemQueryVO));
		DidiApp.getHttpManager().sessionPost(activity, all_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				JSONObject jsonObject;
				try {
					mPullToRefreshListView.onRefreshComplete();
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");
					String all = body.getString("items");
					List<AllAuctionModelForEavelate> allAuctionModels = JSON.parseArray(all,AllAuctionModelForEavelate.class);
                        mAllAuctionList.addAll(allAuctionModels);
//                        mTransportationTaskAdapter.addList(mMoreAuctionList);
					mTransportationTaskAdapterNew.notifyDataSetChanged();
					currPage = body.getInt("currentPage");
					isEnd = body.getBoolean("end");
					if(currPage > 1){
						back_top.setVisibility(View.VISIBLE);
						back_top.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								mTaskListview.setSelection(0);
							}
						});
					}else {
						back_top.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPullToRefreshListView.onRefreshComplete();
				Toast.makeText(activity,errMsg,Toast.LENGTH_LONG).show();
			}
		});
	}
}
