package com.hongshi.wuliudidi.fragment;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.MessageItemActivity;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.InviteModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.MessageItemView;
import com.hongshi.wuliudidi.view.NoLoginView;
import com.umeng.analytics.MobclickAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MessageFragment extends Fragment implements OnClickListener{

	private View mView;
	private MessageItemView mSystemItem,mAuctionItem,mWalletItem,mBussinessItem,mMarketItem;
	private DiDiTitleView mMessageTitle;
	private NoLoginView mNoLoginView;
	private LinearLayout mMessageLayout;
	/**
	 * 车主最新DB消息
	 */

	private String fetch_url = GloableParams.HOST + "mc/fetchTopDbMsg4AllBiz.do?";
//	private String driver_fetch_url = GloableParams.HOST + "msg/fetchTopDbMsg4AllBiz.do";
	private int system_number = 0;
	private int auction_number = 0;
	private int friend_umber = 0;
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {  
 	    @Override  
 	     public void onReceive(Context context, Intent intent) {  
 	         String action = intent.getAction();  
			if (action.equals(CommonRes.RefreshData)) {
//				init();
				if(Util.isLogin(getActivity())){
					mNoLoginView.setVisibility(View.GONE);
					mMessageLayout.setVisibility(View.VISIBLE);
				}else{
					mNoLoginView.setVisibility(View.VISIBLE);
					mMessageLayout.setVisibility(View.GONE);
				}
			}else if(action.equals(CommonRes.MessageFromSystem)){
				if(system_number != 0){
					system_number = system_number + intent.getIntExtra("system_number", 0);	
				}else{
					system_number = intent.getIntExtra("system_number", 0);	
				}
				mSystemItem.showRedPoint(true);
//				RefreshMessage mRefreshMessage = (RefreshMessage) intent.getSerializableExtra("system");
//				mSystemItem.showNews(system_number);
//				mSystemItem.setMessageContent(mRefreshMessage.getREFRESH_MSG_SYSTEM().get(0).getRealContent());
				getMessage();
			}else if(action.equals(CommonRes.MessageFromAuction)){
				if(auction_number != 0){
					auction_number =auction_number + intent.getIntExtra("auction_number", 0);	
				}else{
					auction_number = intent.getIntExtra("auction_number", 0); 
				}
				mAuctionItem.showRedPoint(true);
//				RefreshMessage mRefreshMessage = (RefreshMessage) intent.getSerializableExtra("auction");
//				mAuctionItem.showNews(auction_number);
//				mSystemItem.setMessageContent(mRefreshMessage.getREFRESH_MSG_AUCTION().get(0).getRealContent());
				getMessage();
			}else if(action.equals(CommonRes.MessageFromInvite)){
				if(friend_umber != 0){
					friend_umber = intent.getIntExtra("friend_number", 0) + friend_umber;
				}else{
					friend_umber = intent.getIntExtra("friend_number", 0);
				}
//				RefreshMessage mRefreshMessage = (RefreshMessage) intent.getSerializableExtra("invite");
//				mInviteItem.showNews(friend_umber);
//				mInviteItem.setMessageContent(mRefreshMessage.getREFRESH_MSG_FRIENDSHIP().get(0).getRealContent());
				getMessage();
			}else if(action.equals(CommonRes.GetALlMessage)){
				if(Util.isLogin(getActivity())){
					getMessage();
				}
			}
 	      }  
 	  };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.message_activity, null);
		mMessageTitle = (DiDiTitleView) mView.findViewById(R.id.message_title);
		mMessageTitle.setTitle("消息");
//		mMessageTitle.setBack(getActivity());
		mMessageTitle.hideBack();
		mSystemItem = (MessageItemView) mView.findViewById(R.id.message_system_item);
		mAuctionItem = (MessageItemView) mView.findViewById(R.id.message_auction_item);
		mWalletItem = (MessageItemView) mView.findViewById(R.id.message_wallet_item);
		mBussinessItem = (MessageItemView) mView.findViewById(R.id.message_bussiness_item);
		mMarketItem = (MessageItemView) mView.findViewById(R.id.message_market_item);
//		mInviteItem = (MessageItemView) mView.findViewById(R.id.message_invite_item);
		mSystemItem.getMessageImage().setImageResource(R.drawable.system_message);
		mSystemItem.setMessageType("系统消息");
		mSystemItem.showRedPoint(false);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
		layoutParams.leftMargin = 20;
		mAuctionItem.getMessageImage().setImageResource(R.drawable.auction_message);
		mAuctionItem.setMessageType("接单消息");
		mAuctionItem.showRedPoint(false);
		mAuctionItem.findViewById(R.id.top_line).setLayoutParams(layoutParams);

		mWalletItem.getMessageImage().setImageResource(R.drawable.wallet_message);
		mWalletItem.setMessageType("钱包消息");
		mWalletItem.findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
		mWalletItem.findViewById(R.id.top_line).setLayoutParams(layoutParams);
		mWalletItem.showRedPoint(false);

		mBussinessItem.getMessageImage().setImageResource(R.drawable.bussiness_message);
		mBussinessItem.setMessageType("业务消息");
		mBussinessItem.showRedPoint(false);

		mMarketItem.getMessageImage().setImageResource(R.drawable.market_message);
		mMarketItem.setMessageType("营销消息");
		mMarketItem.findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
		mMarketItem.findViewById(R.id.top_line).setLayoutParams(layoutParams);
		mMarketItem.showRedPoint(false);

		if(!DidiApp.isUserAowner){
			mWalletItem.setVisibility(View.GONE);
			mBussinessItem.setVisibility(View.GONE);
			mMarketItem.setVisibility(View.GONE);
		}
//		mInviteItem.getMessageImage().setImageResource(R.drawable.invite_message);
//		mInviteItem.setMessageType("邀请消息");
		mMessageLayout = (LinearLayout) mView.findViewById(R.id.message_layout);
		mNoLoginView = (NoLoginView) mView.findViewById(R.id.no_login_view);
		// 注册刷新订单广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CommonRes.RefreshData);
		intentFilter.addAction(CommonRes.MessageFromSystem);
		intentFilter.addAction(CommonRes.MessageFromAuction);
		intentFilter.addAction(CommonRes.MessageFromInvite);
		intentFilter.addAction(CommonRes.GetALlMessage);
		getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
		if(Util.isLogin(getActivity())){
			getMessage();
		}else{
			mNoLoginView.setVisibility(View.VISIBLE);
			mMessageLayout.setVisibility(View.GONE);
		}
		mSystemItem.setOnClickListener(this);
//		mInviteItem.setOnClickListener(this);
		mAuctionItem.setOnClickListener(this);
		mWalletItem.setOnClickListener(this);
		mBussinessItem.setOnClickListener(this);
		mMarketItem.setOnClickListener(this);
		return mView;
	}
	
//	private void init(){
//		system_number = 0;
//		mSystemItem.hideNews();
//		auction_number = 0;
//		mAuctionItem.hideNews();
//		friend_umber = 0;
//		mInviteItem.hideNews();
//	}

	private void getMessage() {
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(getActivity(), fetch_url, params,
				new AfinalHttpCallBack() {
					@Override
					public void data(String t) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(t);
							String body = jsonObject.getString("body");
							List<InviteModel> mInviteModel = JSON.parseArray(
									body, InviteModel.class);
							if (mInviteModel != null && mInviteModel.size() > 0) {
								String type;
								for (int i = 0; i < mInviteModel.size(); i++) {
									type = mInviteModel.get(i).getMsgBizType();
									if (type.equals("FRIENDSHIP")) {
										// 邀请消息
//										mInviteItem.setMessageContent(mInviteModel.get(i).getRealContent());
//										mInviteItem.setMessageTime(Util.formatDateMinute(mInviteModel.get(i).getGmtCreate()));
									} else if (type.equals("AUCTION")) {
//										mAuctionItem.setMessageContent(mInviteModel.get(i).getRealContent());
//										mAuctionItem.setMessageTime(Util.formatDateMinute(mInviteModel.get(i).getGmtCreate()));
									} else if (type.equals("SYSTEM")) {
//										mSystemItem.setMessageContent(mInviteModel.get(i).getRealContent());
//										mSystemItem.setMessageTime(Util.formatDateMinute(mInviteModel.get(i).getGmtCreate()));
									}
								}

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_system_item:
			mSystemItem.showRedPoint(false);
			startActivity(1);
			if(system_number!=0){
//				mSystemItem.hideNews();
				Intent system_intent = new Intent();
				system_intent.setAction(CommonRes.ClickSystemMessage);
				system_intent.putExtra("system_number", system_number);
				getActivity().sendBroadcast(system_intent);	
				system_number=0;
			}
			break;
		case R.id.message_auction_item:
			mAuctionItem.showRedPoint(false);
			startActivity(2);
			if(auction_number!=0){
//				mAuctionItem.hideNews();
				Intent auction_intent = new Intent();
				auction_intent.setAction(CommonRes.ClickAuctionMessage);
				auction_intent.putExtra("auction_number", auction_number);
				getActivity().sendBroadcast(auction_intent);
				auction_number = 0;
			}
			break;
			case R.id.message_wallet_item:
				mWalletItem.showRedPoint(false);
				startActivity(4);
				break;
			case R.id.message_bussiness_item:
				mBussinessItem.showRedPoint(false);
				startActivity(5);
				break;
			case R.id.message_market_item:
				mMarketItem.showRedPoint(false);
				startActivity(6);
				break;
//		case R.id.message_invite_item:
//			startActivity(3);
//			if(friend_umber!=0){
////				mInviteItem.hideNews();
//				Intent invite_intent = new Intent();
//				invite_intent.setAction(CommonRes.ClickInviteMessage);
//				invite_intent.putExtra("invite_number", friend_umber);
//				getActivity().sendBroadcast(invite_intent);
//				friend_umber = 0;
//			}
//			break;
		default:
			break;
		}
	}
	public void startActivity(int type){
		Intent message_intent = new Intent(getActivity(),MessageItemActivity.class);
		message_intent.putExtra("type", type);
		startActivity(message_intent);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MessageFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MessageFragment");
	}
}
