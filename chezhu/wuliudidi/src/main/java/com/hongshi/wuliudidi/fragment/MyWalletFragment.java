package com.hongshi.wuliudidi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.BindNewBankcardActivity;
import com.hongshi.wuliudidi.activity.CashAccountActivity;
import com.hongshi.wuliudidi.activity.ConsumeAccountActivity;
import com.hongshi.wuliudidi.activity.IncomeBookDetailActivity;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.MainActivity;
import com.hongshi.wuliudidi.activity.OilAccountActivity;
import com.hongshi.wuliudidi.activity.PaymentOrdersActivity;
import com.hongshi.wuliudidi.activity.TyreAccountActivity;
import com.hongshi.wuliudidi.activity.WebViewWithTitleActivity;
import com.hongshi.wuliudidi.adapter.BannerAdapter;
import com.hongshi.wuliudidi.adapter.MyWalletBillListAdapter;
import com.hongshi.wuliudidi.cashier.TiXianActivity;
import com.hongshi.wuliudidi.dialog.HintDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.CtBillCycleReconStatVO;
import com.hongshi.wuliudidi.model.WalletModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.qr.ConfirmGoodsActivity;
import com.hongshi.wuliudidi.share.BannerModelList;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.CircleFlowIndicator;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.MyViewFlow;
import com.hongshi.wuliudidi.view.NoLoginView;
import com.hongshi.wuliudidi.view.TextViewWithDrawable;
import com.umeng.analytics.MobclickAgent;

/**
 * @author huiyuan
 */
public class MyWalletFragment extends Fragment implements OnClickListener{
	private DiDiTitleView mTitle;
	private TextView accountRestMoneyText,oil_rest_money_text,tyre_rest_money_text,
			consume_account_rest_money_text,detail_tip;
	private TextViewWithDrawable withdrawal,orders_list,truck_mall,scan_entry0;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListview;
	private WalletModel mWalletModel;
	private final String wallet_data_url = GloableParams.HOST + "carrier/app/bill/carrierTotalFee.do";
	private final String wallet_detail_url = GloableParams.HOST + "carrier/app/bill/billCycleReconStat.do";
	private MyWalletBillListAdapter myWalletBillListAdapter;
	private List<CtBillCycleReconStatVO> list = new ArrayList<>();
	private int currentPage = 1;
	private boolean isEnd = false;
	private Activity myWalletActivity;
	private NoLoginView no_login_layout;
	private RelativeLayout cash_account_container,cash_consume_container,tyre_account_container,oil_card_container;
	private LinearLayout total_money_container;
	private FrameLayout frameLayout;
	private MyViewFlow view_flow;
	private CircleFlowIndicator indic;
	private BannerAdapter bannerAdapter;

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CommonRes.BindNewBankcard) || action.equals("get_cash_data_success")) {
				getData(true);
			}else if(CommonRes.RefreshData.equals(action) || CommonRes.RefreshMyWalletData.equals(action)){
				if(DidiApp.isUserAowner){
					getWalletData();
				}
			}else if("com.refresh.my_wallet_items".equals(action)){
//				BannerModelList.getAdvertList(getActivity());
				getWalletData();
			}else if("com.action.cash".equals(action) ||
					"com.action.tyre".equals(action) ||
					"com.action.oil".equals(action) ||
					"com.action.revoke".equals(action) ||
					"consume_account_activity".equals(action)){
				getWalletData();
			}else if("get_banner_list_success".equals(action)){
					setBannerView();
			}else if("add_bank_card".equals(action)){
				showBankCardDialog();
			}
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyWalletFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyWalletFragment");
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myWalletActivity = getActivity();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.my_wallet_activity, null);
		mTitle = (DiDiTitleView) view.findViewById(R.id.my_wallet_title);
		mTitle.setTitle(getResources().getString(R.string.my_wallet));
		mTitle.hideBack();

		mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.my_account_list);
		mListview = mPullToRefreshListView.getRefreshableView();
		View listHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.my_wallet_list_header_view,null);
		mListview.addHeaderView(listHeaderView);
		cash_account_container = (RelativeLayout) listHeaderView.findViewById(R.id.cash_account_container);
		cash_consume_container = (RelativeLayout) listHeaderView.findViewById(R.id.cash_consume_container);
		tyre_account_container = (RelativeLayout) listHeaderView.findViewById(R.id.tyre_account_container);
		oil_card_container = (RelativeLayout) listHeaderView.findViewById(R.id.oil_card_container);
		no_login_layout = (NoLoginView) view.findViewById(R.id.no_login_layout);
		accountRestMoneyText = (TextView) view.findViewById(R.id.account_rest_money_text);
		oil_rest_money_text = (TextView) view.findViewById(R.id.oil_rest_money_text);
		tyre_rest_money_text = (TextView) view.findViewById(R.id.tyre_rest_money_text);
		consume_account_rest_money_text = (TextView) view.findViewById(R.id.consume_account_rest_money_text);
		detail_tip = (TextView) view.findViewById(R.id.detail_tip);
//		scanner = (ImageView) view.findViewById(R.id.scanner);
		total_money_container = (LinearLayout) view.findViewById(R.id.total_money_container);

		scan_entry0 = (TextViewWithDrawable) view.findViewById(R.id.scan_entry0);
		withdrawal = (TextViewWithDrawable) view.findViewById(R.id.withdrawal);
		orders_list = (TextViewWithDrawable) view.findViewById(R.id.orders_list);
		truck_mall = (TextViewWithDrawable) view.findViewById(R.id.truck_mall);
		frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
		view_flow = (MyViewFlow) view.findViewById(R.id.view_flow);
		indic = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);


		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");
		mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (mPullToRefreshListView.isRefreshing()) {
					if (mPullToRefreshListView.isHeaderShown()){
						getData(true);
					} else if (mPullToRefreshListView.isFooterShown()) {
						// 加载更多
						if(isEnd){
							Toast.makeText(getActivity(), "已经是最后一页", Toast.LENGTH_SHORT).show();
							CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
							closeRefreshTask.execute();
							return;
						}
						currentPage = currentPage + 1;
						getData(false);
					}
				}
			}
		});


		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(Util.isLogin(getActivity())){
					//已登录
					if(list.size() <= 0){
						return;
					}
					Intent intent = new Intent(myWalletActivity,IncomeBookDetailActivity.class);

					CtBillCycleReconStatVO ctBillCycleReconStatVO = list.get(position - 2);
					intent.putExtra("ctBillCycleReconStatVO",ctBillCycleReconStatVO);
					startActivity(intent);
				}else{
					ToastUtil.show(getActivity(), "请登录");
					Intent login_intent = new Intent(getActivity(),LoginActivity.class);
					getActivity().startActivity(login_intent);
				}
			}
		});


		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.BindNewBankcard);
		intentFilter.addAction("get_cash_data_success");
		intentFilter.addAction("com.action.cash");
		intentFilter.addAction("com.action.tyre");
		intentFilter.addAction("com.action.oil");
		intentFilter.addAction("com.action.revoke");
		intentFilter.addAction("consume_account_activity");
		intentFilter.addAction("com.refresh.my_wallet_items");
		intentFilter.addAction("get_banner_list_success");
		intentFilter.addAction("add_bank_card");
		intentFilter.addAction(CommonRes.RefreshData);
		intentFilter.addAction(CommonRes.RefreshMyWalletData);
		myWalletActivity.registerReceiver(mRefreshBroadcastReceiver, intentFilter);

		scan_entry0.setOnClickListener(this);
		withdrawal.setOnClickListener(this);
		orders_list.setOnClickListener(this);
		truck_mall.setOnClickListener(this);
//		scanner.setOnClickListener(this);
		cash_account_container.setOnClickListener(this);
		cash_consume_container.setOnClickListener(this);
		tyre_account_container.setOnClickListener(this);
		oil_card_container.setOnClickListener(this);

		setBannerView();

		myWalletBillListAdapter = new MyWalletBillListAdapter(getActivity(),list);
		mListview.setAdapter(myWalletBillListAdapter);
		getWalletData();
		return view;
	}

	private void setBannerView(){
		WindowManager windowManager = getActivity().getWindowManager();
		int width = windowManager.getDefaultDisplay().getWidth();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,1 * width/5);
		frameLayout.setLayoutParams(layoutParams);
		// 轮播图
		if(BannerModelList.myWalletBannerList.size() > 0){
			frameLayout.setVisibility(View.VISIBLE);
			bannerAdapter = new BannerAdapter(getActivity(),BannerModelList.myWalletBannerList,"my_wallet_page");
			FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(width,1 * width/5);
			view_flow.setLayoutParams(layoutParams1);
			view_flow.setAdapter(bannerAdapter);
			/**
			 * 实际图片张数
			 */
			view_flow.setmSideBuffer(BannerModelList.myWalletBannerList.size());
			view_flow.setViewPager(MainActivity.mPager);
			if(BannerModelList.myWalletBannerList.size() <= 1){
				indic.setVisibility(View.GONE);
			}else {
				view_flow.setFlowIndicator(indic);
				view_flow.setTimeSpan(4000);
				/**
				 * 设置初始位置
				 */
				view_flow.setSelection(3 * 1000);
				// 启动自动播放
				view_flow.startAutoFlowTimer();
			}

		}else {
			frameLayout.setVisibility(View.GONE);
		}
	}

	private void getWalletData(){
		if(Util.isLogin(getActivity())){
			no_login_layout.setVisibility(View.GONE);
			total_money_container.setVisibility(View.VISIBLE);
			mPullToRefreshListView.setVisibility(View.VISIBLE);
			getSummaryData();
			getData(true);
		}else {
			no_login_layout.setVisibility(View.VISIBLE);
			total_money_container.setVisibility(View.GONE);
			mPullToRefreshListView.setVisibility(View.GONE);
		}
	}

	private void getSummaryData(){
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(myWalletActivity, wallet_data_url, params, new ChildAfinalHttpCallBack() {

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				Toast.makeText(myWalletActivity,errMsg,Toast.LENGTH_LONG).show();
			}

			@Override
			public void data(String t) {
				try {
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					mWalletModel = JSON.parseObject(all, WalletModel.class);
					myWalletActivity.getSharedPreferences("config",Context.MODE_PRIVATE).edit()
							.putString("cifUserId",mWalletModel.getCifUserId()).commit();
					dataFillIn();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void getData(final boolean isRefresh){
		if(isRefresh){
			currentPage = 1;
		}
		AjaxParams params = new AjaxParams();
		params.put("currentPage",""+ currentPage);
		params.put("pageSize","10");
		DidiApp.getHttpManager().sessionPost(myWalletActivity, wallet_detail_url, params, new ChildAfinalHttpCallBack() {

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPullToRefreshListView.onRefreshComplete();
			}

			@Override
			public void data(String t) {
				no_login_layout.setVisibility(View.GONE);
				isLoginData(t,isRefresh);
			}
		});
	}

	private void isLoginData(String t, boolean isRefresh){
		try {
			if(isRefresh){
				list.clear();
				isEnd = false;
				mPullToRefreshListView.onRefreshComplete();
			}
			JSONObject jsonObject = new JSONObject(t);
			String all = jsonObject.getString("body");
			JSONObject body = new JSONObject(all);
			//判断是否还有下一页
			isEnd = body.getBoolean("end");
			currentPage = body.getInt("currentPage");
			String itemsStr = body.getString("items");
			List<CtBillCycleReconStatVO> tempList = JSON.parseArray(itemsStr,CtBillCycleReconStatVO.class);
			if(isRefresh){
				list = tempList;
				myWalletBillListAdapter = new MyWalletBillListAdapter(myWalletActivity,list);
				mListview.setAdapter(myWalletBillListAdapter);
			}else{
				list.addAll(tempList);
				myWalletBillListAdapter.notifyDataSetChanged();
			}

			mPullToRefreshListView.setVisibility(View.VISIBLE);
			mPullToRefreshListView.onRefreshComplete();
		} catch (JSONException e) {
			e.printStackTrace();
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	private void dataFillIn(){
		if(mWalletModel == null){
			return;
		}
		no_login_layout.setVisibility(View.GONE);

		/**
		 * key 1基本账户，2保证金，3一卡通，4 运费，5油费，6收益，7信用度， 8水泥预付运费
		 */
		double baseMoney = mWalletModel.getSum("1"), ensureMoney = mWalletModel.getSum("2");
		accountRestMoneyText.setText(Util.formatDoubleToString(baseMoney, "元"));
		oil_rest_money_text.setText(mWalletModel.getOilAmount());
		tyre_rest_money_text.setText(mWalletModel.getTyreAmount());
		consume_account_rest_money_text.setText(Util.formatDoubleToString(mWalletModel.getSum("11"),"元"));
	}

	private void doWithdrawl(){
		if(mWalletModel == null){
			return;
		}
		String cashCode = mWalletModel.getCifUserId();
		if(cashCode == null){
			return;
		}
		if(cashCode != null && cashCode.length() > 0){
			HashMap<String,String> params = new HashMap<>();
			params.put("userId", cashCode);
//			PluginUtil.startPlugin(myWalletActivity,
//					PluginParams.MY_CASH_ACTION, PluginParams.MY_CASH, params);
			Intent intent = new Intent(myWalletActivity, TiXianActivity.class);
			intent.putExtra("params",params);
			startActivity(intent);
		}else{
			ToastUtil.show(myWalletActivity, "资金信息获取失败");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		myWalletActivity.unregisterReceiver(mRefreshBroadcastReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.scan_entry0:
//			case R.id.scanner:
				Intent scannerIntent = new Intent(myWalletActivity, ConfirmGoodsActivity.class);
				scannerIntent.putExtra("from","my_wallet");
				myWalletActivity.startActivity(scannerIntent);
				break;
			case R.id.cash_account_container:
				Intent intent0 = new Intent(myWalletActivity, CashAccountActivity.class);
				myWalletActivity.startActivity(intent0);
				break;
			case R.id.cash_consume_container:
				Intent intent1 = new Intent(myWalletActivity, ConsumeAccountActivity.class);
				myWalletActivity.startActivity(intent1);
				break;
			case R.id.oil_card_container:
				Intent intent2 = new Intent(myWalletActivity, OilAccountActivity.class);
				myWalletActivity.startActivity(intent2);
				break;
			case R.id.tyre_account_container:
				Intent intent3 = new Intent(myWalletActivity, TyreAccountActivity.class);
				myWalletActivity.startActivity(intent3);
				break;
			case R.id.withdrawal:
				doWithdrawl();
				break;
			case R.id.orders_list:
				Intent ordersIntent = new Intent(myWalletActivity, PaymentOrdersActivity.class);
				myWalletActivity.startActivity(ordersIntent);
				break;
			case R.id.truck_mall:
				goToMall();
				break;
			default:
				break;
		}
	}

	private String user_auth_url = GloableParams.HOST + "thirdpt/mall/authorizedLogin.do";
	private void goToMall(){

		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(myWalletActivity, user_auth_url,
				params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(t);
							String all = jsonObject.optString("body");
							JSONObject object = new JSONObject(all);
							String token = object.optString("token");
							String uid = object.optString("uid");
							String url = "http://redlion56.com/autoPartsShop/index.html?token=" + token + "&uid=" + uid;
							Intent mIntent = new Intent(myWalletActivity,
									WebViewWithTitleActivity.class);
							Bundle mBundle = new Bundle();
							mBundle.putString("title", "汽车商城");
							mBundle.putString("url", url);
							mIntent.putExtras(mBundle);
							startActivity(mIntent);
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtil.show(myWalletActivity, e.getMessage());
						}
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						ToastUtil.show(myWalletActivity, errMsg);
					}
				});

	}

	private void showBankCardDialog(){
		HintDialog cancelDialog = new HintDialog(getActivity(),R.style.data_filling_dialog,
				"您还未绑定银行卡，请先绑定银行卡","稍后再说","添加银行卡",null);

		cancelDialog.getmRight().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), BindNewBankcardActivity.class);
				startActivity(intent);
			}
		});
		cancelDialog.show();
	}
}
