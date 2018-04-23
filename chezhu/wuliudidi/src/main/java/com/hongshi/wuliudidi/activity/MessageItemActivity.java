package com.hongshi.wuliudidi.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.AuctionMessageAdapter;
import com.hongshi.wuliudidi.adapter.BussinessActivityAdapter;
import com.hongshi.wuliudidi.adapter.InviteListAdapter;
import com.hongshi.wuliudidi.adapter.SystemMessageAdapter;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.model.DbMsgBodyVO;
import com.hongshi.wuliudidi.model.InviteModel;
import com.hongshi.wuliudidi.model.MsgFindDBAndPageParamVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class MessageItemActivity extends Activity {
	private DiDiTitleView mTitle;
	private ListView mMessageListView,message_list_add_lv;
	private PullToRefreshListView message_list_add;
	private List<InviteModel> mInviteList = new ArrayList<>();
	private List<DbMsgBodyVO> dbMsgBodyVOs = new ArrayList<>();
	private InviteListAdapter mInviteListAdapter;
	private SystemMessageAdapter mMessageItemListAdapter;
	private int type = 0;
	//根据类型获取消息列表
	private String fetch_url = GloableParams.HOST + "mc/fetchDbMsg4Biz.do?";
	private String message_list_url = GloableParams.HOST + "mc/findDBAndPage.do";
	private String typeStr;
	private BussinessActivityAdapter activityAdapter;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("MessageItemActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("MessageItemActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.message_item_activity);
		initViews();
	}

	private void initViews(){
		mTitle = (DiDiTitleView) findViewById(R.id.system_title);
		mTitle.setBack(this);
		mMessageListView = (ListView) findViewById(R.id.message_listview);
		message_list_add = (PullToRefreshListView) findViewById(R.id.message_list_add);
		type = getIntent().getIntExtra("type", 0);
		setView(type);
		if(type <= 3){
			mMessageListView.setVisibility(View.VISIBLE);
			message_list_add.setVisibility(View.GONE);
			mMessageListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					String SUB_TYPE = mInviteList.get(position).getMsgSubBizType();
					List<String> params = mInviteList.get(position).getParams();
					if(SUB_TYPE == null){
						return;
					}
					if (type == 1) {
						//系统消息
						if(SUB_TYPE.equals("SYSTEM_IDENTIFY_OK") || SUB_TYPE.equals("SYSTEM_IDENTIFY_FAIL")){
							//个人认证成功或个人认证不成功
							Intent intent = new Intent(MessageItemActivity.this,AuthActivity.class);
							intent.putExtra("name", "name");
							startActivity(intent);
						}else if(SUB_TYPE.equals("SYSTEM_ENTERPRISE_OK") || SUB_TYPE.equals("SYSTEM_ENTERPRISE_FAIL")){
							//企业认证通过 或 企业认证不通过
							Intent intent = new Intent(MessageItemActivity.this,AuthActivity.class);
							intent.putExtra("name", "enterprise");
							startActivity(intent);
						}else if(SUB_TYPE.equals("SYSTEM_TRUCK_OK")){
							//车辆审核通过 或 车辆审核不通过
//						params.get(0);
							if(params.size()>2){
								String truckId = params.get(2);
								Intent intent = new Intent(MessageItemActivity.this,AddTruckNewActivity.class);
								intent.putExtra("truckId", truckId);
								startActivity(intent);
							}
						}else if(SUB_TYPE.equals("SYSTEM_TRUCK_FAIL")){
							if(params.size()>1){
								String truckId = params.get(1);
								Intent intent = new Intent(MessageItemActivity.this,AddTruckNewActivity.class);
								intent.putExtra("truckId", truckId);
								startActivity(intent);
							}
						}else if(SUB_TYPE.equals("SYSTEM_DRIVING_OK") || SUB_TYPE.equals("SYSTEM_DRIVING_FAIL")){
							//驾照认证通过 或 驾照认证不通过
							Intent intent = new Intent(MessageItemActivity.this,AuthActivity.class);
							intent.putExtra("name", "license");
							startActivity(intent);
						}
					} else if(type == 2){
						//竞价
						if(SUB_TYPE.equals("AUCTION_PASS_AUDIT")){
							//运力交易生效
							if(params.size()>1){
								String mAuctionId = params.get(1);
								Intent intent = new Intent(MessageItemActivity.this,WinBidActivity.class);
								intent.putExtra("AuctionId", mAuctionId);
								startActivity(intent);
							}
						}else if(SUB_TYPE.equals("AUCTION_BARGIN")){
							//协商
							if(params.size()>2){
								String auctionId = params.get(2);
								Intent intent = new Intent(MessageItemActivity.this,AuctionDetailsActivity.class);
								intent.putExtra("auctionId", auctionId);
								startActivity(intent);
							}
						}else if(SUB_TYPE.equals("AUCTION_FAIL_AUDIT")){
							//运力未成功
							if(params.size()>1){
								String auctionId = params.get(1);
								Intent intent = new Intent(MessageItemActivity.this,AuctionDetailsActivity.class);
								intent.putExtra("auctionId", auctionId);
								startActivity(intent);
							}
						}
					}else if (type == 3) {
						//邀请消息
						if(SUB_TYPE.equals("FRIENDSHIP_DRIVER_AGREE")){
							//司机{1}同意加入车队
							if(params.size()>3){
								String userId = params.get(3);
								Intent intent = new Intent(MessageItemActivity.this,PlayerInfoActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
							}
						}else if(SUB_TYPE.equals("FRIENDSHIP_DRIVER_DISAGREE")){
							//跳转到队员列表
							Intent intent = new Intent(MessageItemActivity.this,TruckTeamActivity.class);
							startActivity(intent);
						}
					}
				}
			});
		}else if(4 <= type || type <=6){
			mMessageListView.setVisibility(View.GONE);
			message_list_add.setVisibility(View.VISIBLE);
			message_list_add_lv = message_list_add.getRefreshableView();
			message_list_add.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (message_list_add.isRefreshing()) {
						if (message_list_add.isHeaderShown()){
							currentPage = 0;
							getMessageList(true);
						} else if (message_list_add.isFooterShown()) {
							// 加载更多
							if(isEnd){
								Toast.makeText(MessageItemActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
								CloseRefreshTask closeRefreshTask = new CloseRefreshTask(message_list_add);
								closeRefreshTask.execute();
								return;
							}
							currentPage = currentPage + 1;
							getMessageList(false);
						}
					}
				}
			});
			getMessageList(true);
			message_list_add_lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String url = dbMsgBodyVOs.get(position - 1).getUrl();
					Intent intent = new Intent(MessageItemActivity.this,WebViewWithTitleActivity.class);
					if(url.startsWith("http")){
						intent.putExtra("url",url);
					}else {
						Toast.makeText(MessageItemActivity.this,"无法打开不合规定的链接",Toast.LENGTH_LONG).show();
					}
					intent.putExtra("title",dbMsgBodyVOs.get(position - 1).getTitle());
					startActivity(intent);
				}
			});
		}
	}

	public void setData(int type) {
		if (type == 1) {
			getData("SYSTEM");
			typeStr = "SYSTEM";
		} else if(type == 2){
			getData("AUCTION");
			typeStr = "AUCTION";
		}else if (type == 3) {
			getData("FRIENDSHIP");
			typeStr = "FRIENDSHIP";
		}
	}

	private void getData(final String type){
		AjaxParams params = new AjaxParams();
		params.put("bizType", type);
		DidiApp.getHttpManager().sessionPost(MessageItemActivity.this, fetch_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Log.d("huiyuan","消息数据 = " + t);
				JSONObject jsonObject;
				if(mInviteList != null && mInviteList.size()>0){
					mInviteList.clear();
				}
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					mInviteList = JSON.parseArray(body, InviteModel.class);
					if(type.equals("FRIENDSHIP")){
						//邀请消息
						mInviteListAdapter = new InviteListAdapter(MessageItemActivity.this, mInviteList,new RefreshAdapterCallBack() {
							@Override
							public void isAccept(boolean args) {
								getData(typeStr);
								if(mInviteListAdapter != null){
									mInviteListAdapter.notifyDataSetChanged();
								}
							}
						});
						mMessageListView.setAdapter(mInviteListAdapter);
					}else if(type.equals("AUCTION")){
						AuctionMessageAdapter mAuctionMessageAdapter = new AuctionMessageAdapter(MessageItemActivity.this, mInviteList,new RefreshAdapterCallBack() {
							
							@Override
							public void isAccept(boolean args) {
								getData(typeStr);
								if(mInviteListAdapter != null){
									mInviteListAdapter.notifyDataSetChanged();
								}
							}
						});
						mMessageListView.setAdapter(mAuctionMessageAdapter);
					}else if(type.equals("SYSTEM")){
						mMessageItemListAdapter = new SystemMessageAdapter(MessageItemActivity.this, mInviteList);
						mMessageListView.setAdapter(mMessageItemListAdapter);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	public void setView(int type) {
		switch (type) {
		case 1:
			mTitle.setTitle("系统消息");
			setData(1);
			break;
		case 2:
			mTitle.setTitle("接单消息");
			setData(2);
			break;
		case 4:
			mTitle.setTitle("钱包消息");
			break;
			case 5:
				mTitle.setTitle("业务通知");
				break;
			case 6:
				mTitle.setTitle("营销活动");
				break;
		default:
			break;
		}
	}

	private int currentPage = 0;
	private boolean isEnd = false;
	private void getMessageList(final boolean isRefresh){
		AjaxParams params = new AjaxParams();
		params.put("bizType","CRM_ZDY");
		params.put("currentPage",""+currentPage);
		params.put("pageSize","10");

		if(type == 5){
			params.put("bizSubType","YE_WU_DB");
		}else if(type == 6){
			params.put("bizSubType","YE_YINGXIAO_DB");
		}

		DidiApp.getHttpManager().sessionPost(MessageItemActivity.this, message_list_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Log.d("huiyuan","消息list body = " + t);

				JSONObject jsonObject;
				try {
					if(isRefresh){
						isEnd = false;
						dbMsgBodyVOs.clear();
						message_list_add.onRefreshComplete();
					}
					jsonObject = new JSONObject(t);
					JSONObject body = jsonObject.getJSONObject("body");

					String all = body.getString("items");
					//判断是否还有下一页
					isEnd = body.getBoolean("end");
					currentPage = body.getInt("currentPage");
					List<DbMsgBodyVO> tempList = JSON.parseArray(all,DbMsgBodyVO.class);
					if(isRefresh){
						dbMsgBodyVOs.addAll(tempList);
						if(type == 4){
							activityAdapter = new BussinessActivityAdapter(
									MessageItemActivity.this,dbMsgBodyVOs,4);
							message_list_add_lv.setAdapter(activityAdapter);
						}else if(type == 5){
							activityAdapter = new BussinessActivityAdapter(
									MessageItemActivity.this,dbMsgBodyVOs,5);
							message_list_add_lv.setAdapter(activityAdapter);
						}else if(type == 6){
							activityAdapter = new BussinessActivityAdapter(
									MessageItemActivity.this,dbMsgBodyVOs,6);
							message_list_add_lv.setAdapter(activityAdapter);
						}
					}else{
							activityAdapter.addData(tempList);
							activityAdapter.notifyDataSetChanged();
					}
					if(dbMsgBodyVOs.size() > 0){
						message_list_add.setVisibility(View.VISIBLE);
						message_list_add.onRefreshComplete();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					message_list_add.onRefreshComplete();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				message_list_add.onRefreshComplete();
				Toast.makeText(MessageItemActivity.this,errMsg,Toast.LENGTH_LONG).show();
			}
		});
	}
}
