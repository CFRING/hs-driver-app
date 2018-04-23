package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AuctionOfferActivity;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.MapActivity;
import com.hongshi.wuliudidi.activity.ResultActivity;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AuctionDoBid;
import com.hongshi.wuliudidi.model.GoodsModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.GoodsBubbleMsg;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.Util;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

public class GoodsAdapter extends BaseAdapter {

	private Context mContext;
	private List<GoodsModel> mGoodsList;

	public GoodsAdapter(Context context, List<GoodsModel> mGoodsList,boolean isCementGoods) {
		this.mGoodsList = mGoodsList;
		this.mContext = context;
	}
	public void addGoodsList(List<GoodsModel> mGoodsList){
		this.mGoodsList.addAll(mGoodsList);
	}
	@Override
	public int getCount() {
		return mGoodsList.size();
	}

	@Override
	public GoodsModel getItem(int position) {
		return mGoodsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.goods_item, null);
			viewHolder = new ViewHolder();
			viewHolder.startCity = (TextView) convertView.findViewById(R .id.start_name);
			viewHolder.endCity = (TextView) convertView.findViewById(R .id.end_name);
			viewHolder.startCounty = (TextView) convertView.findViewById(R .id.start_county_text);
			viewHolder.endCounty = (TextView) convertView.findViewById(R .id.end_county_text);
			viewHolder.price_layout = (LinearLayout) convertView.findViewById(R .id.price_layout);
			viewHolder.mAuction = (TextView) convertView.findViewById(R.id.auction_image);
//			viewHolder.read_num = (TextView) convertView.findViewById(R.id.read_num);
			viewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
			viewHolder.goods_weight = (TextView) convertView.findViewById(R.id.goods_weight);
			viewHolder.price_type = (TextView) convertView.findViewById(R.id.price_type);
			viewHolder.price_text = (TextView) convertView.findViewById(R.id.price_text);
			viewHolder.auction_number_text = (TextView) convertView.findViewById(R.id.auction_number_text);
			viewHolder.startTimeText = (TextView) convertView.findViewById(R.id.start_time_text);
			viewHolder.handling_charges_text = (TextView)convertView.findViewById(R.id.handling_charges_text);
			viewHolder.price_tip = (TextView)convertView.findViewById(R.id.price_tip);
			viewHolder.map_entry = (RelativeLayout) convertView.findViewById(R.id.map_entry);
			viewHolder.tj_icon = (ImageView) convertView.findViewById(R.id.tj_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final GoodsModel goodsmodel = mGoodsList.get(position);

		if(goodsmodel.isPushFlag()){
			viewHolder.tj_icon.setVisibility(View.VISIBLE);
		}else {
			viewHolder.tj_icon.setVisibility(View.GONE);
		}
		viewHolder.price_layout.setVisibility(View.VISIBLE);
		viewHolder.mAuction.setVisibility(View.VISIBLE);
		viewHolder.startCity.setText("(" + goodsmodel.getSendAddr() + ")");
		viewHolder.endCity.setText("(" + goodsmodel.getRecvAddr() + ")");
		viewHolder.startCounty.setText(goodsmodel.getSendDis());
		viewHolder.endCounty.setText(goodsmodel.getRecvDis());
//		viewHolder.read_num.setText(String.valueOf(goodsmodel.getReadAmount()));
		viewHolder.goods_name.setText(goodsmodel.getGoodsName());
		viewHolder.auction_number_text.setText("承运期    "+ Util.formatDatePoint(goodsmodel.getGmtStartPeriod())
				+" - " + Util.formatDatePoint(goodsmodel.getGmtEndPeriod()));
		//判断计量单位
		String unitText = goodsmodel.getAssignUnitText();
		if (goodsmodel.isBidStart()){
			//已开始
			if ((goodsmodel.getAuctionType() == CommonRes.FIXED_PRICE || goodsmodel.getAuctionType() == CommonRes.LONG_TRANSPORT)
					&& goodsmodel.getInnerGoods() == 1){
				//一口价且是内部货源或者接单运输
				viewHolder.goods_weight.setVisibility(View.VISIBLE);
				if("车".equals(unitText)){
					viewHolder.goods_weight.setText("剩余" + goodsmodel.getGoodsAmountSp() + unitText);
				}else {
					viewHolder.goods_weight.setText("剩余" + Util.formatDoubleToString(Double.valueOf(goodsmodel.getGoodsAmountSp()),"吨") + unitText);
				}

				viewHolder.startTimeText.setVisibility(View.GONE);
				viewHolder.startTimeText.setText("剩余" + Util.formatDoubleToString(Double.valueOf(goodsmodel.getGoodsAmountSp()),"吨") + unitText);
				if (goodsmodel.getGoodsAmountSp() == null || Double.valueOf(goodsmodel.getGoodsAmountSp()) <= 0) {
					//已抢完
					viewHolder.mAuction.setBackgroundResource(R.drawable.already_over);
					viewHolder.mAuction.setOnClickListener(null);
				} else {
					//未抢完
					if (goodsmodel.isHasBidded()) {
						//已接单
						viewHolder.mAuction.setBackgroundResource(R.drawable.bid_finish);
						viewHolder.mAuction.setOnClickListener(null);
					} else {
						//未接单
							if(goodsmodel.getOneKeyRec() == 1){
            					viewHolder.mAuction.setBackgroundResource(R.drawable.one_key_rec_style);
            					viewHolder.mAuction.setVisibility(View.VISIBLE);
            					viewHolder.mAuction.setOnClickListener(new View.OnClickListener() {
               					 @Override
                				public void onClick(View v) {
                   				 	if(Util.isLogin(mContext)){
										toAuctionOffer(goodsmodel,true);
                   				 	}else {
                        			Intent intent = new Intent(mContext, LoginActivity.class);
                       			 	mContext.startActivity(intent);
                    				}
                				}
            					});
							}else {
            					viewHolder.mAuction.setBackgroundResource(R.drawable.to_bid_style);
           	 					viewHolder.mAuction.setOnClickListener(new View.OnClickListener() {
                						@Override
                					public void onClick(View v) {
                    						if(Util.isLogin(mContext)){
												toAuctionOffer(goodsmodel,false);
                    						}else {
                        						Intent intent = new Intent(mContext, LoginActivity.class);
                        						mContext.startActivity(intent);
                   		 					}
										}
            					});
        				}}}
			} else {
				//非一口价或不是内部货源
//				viewHolder.goods_weight.setVisibility(View.GONE);
				if("车".equals(unitText)){
					viewHolder.goods_weight.setText("剩余" + goodsmodel.getGoodsAmountSp() + unitText);
				}else {
					viewHolder.goods_weight.setText("剩余" + Util.formatDoubleToString(Double.valueOf(goodsmodel.getGoodsAmountSp()),"吨") + unitText);
				}
				viewHolder.startTimeText.setVisibility(View.GONE);
				if (goodsmodel.getGoodsAmountSp() == null || Double.valueOf(goodsmodel.getGoodsAmountSp()) <= 0) {
					//已抢完
					viewHolder.mAuction.setBackgroundResource(R.drawable.already_over);
					viewHolder.mAuction.setOnClickListener(null);
				}else {
					if (goodsmodel.isHasBidded()) {
						//已接单
						viewHolder.mAuction.setBackgroundResource(R.drawable.bid_finish);
						viewHolder.mAuction.setOnClickListener(null);
					} else {
						//未接单
//					viewHolder.mAuction.setBackgroundResource(R.drawable.bid_none);
						if(goodsmodel.getOneKeyRec() == 1){
							viewHolder.mAuction.setBackgroundResource(R.drawable.one_key_rec_style);
							viewHolder.mAuction.setVisibility(View.VISIBLE);
							viewHolder.mAuction.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Util.isLogin(mContext)){
										toAuctionOffer(goodsmodel,true);
									}else {
										Intent intent = new Intent(mContext, LoginActivity.class);
										mContext.startActivity(intent);
									}
								}
							});
						}else {
							viewHolder.mAuction.setBackgroundResource(R.drawable.to_bid_style);
							viewHolder.mAuction.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if(Util.isLogin(mContext)){
										toAuctionOffer(goodsmodel,false);
									}else {
										Intent intent = new Intent(mContext, LoginActivity.class);
										mContext.startActivity(intent);
									}
								}
							});
						}
					}
				}
			}
		}else {
			//即将开始
			if("车".equals(unitText)){
				viewHolder.goods_weight.setText("剩余" + goodsmodel.getGoodsAmountSp() + unitText);
			}else {
				viewHolder.goods_weight.setText("剩余" + Util.formatDoubleToString(Double.valueOf(goodsmodel.getGoodsAmountSp()),"吨") + unitText);
			}
			viewHolder.mAuction.setBackgroundResource(R.drawable.soon_start_btn);
			viewHolder.startTimeText.setVisibility(View.GONE);
			viewHolder.startTimeText.setText("接单开始时间" + (goodsmodel.getGmtBidStart().getMonth() + 1) + "月"
					+ goodsmodel.getGmtBidStart().getDate() + "日" + goodsmodel.getGmtBidStart().getHours() + ":");
			if (goodsmodel.getGmtBidStart().getMinutes() < 10){
				viewHolder.startTimeText.setText(viewHolder.startTimeText.getText() +
						"0" + goodsmodel.getGmtBidStart().getMinutes());
			} else {
				viewHolder.startTimeText.setText(viewHolder.startTimeText.getText() +
						"" + goodsmodel.getGmtBidStart().getMinutes());
			}
		}
		viewHolder.price_type.setText(goodsmodel.getAuctionTypeText());
		if(goodsmodel.getInnerGoods() == 2 && goodsmodel.isTerminalCharges()){
			viewHolder.handling_charges_text.setVisibility(View.VISIBLE);
			viewHolder.handling_charges_text.setBackgroundResource(goodsmodel.isTerminalCharges()
					? R.drawable.terminal_charge
					: R.drawable.not_terminal_charge);
		}else {
			viewHolder.handling_charges_text.setVisibility(View.GONE);
		}
		String price;
		if(goodsmodel.getAuctionType()== CommonRes.FIXED_PRICE || goodsmodel.getAuctionType() == CommonRes.LONG_TRANSPORT){
//			viewHolder.price_type.setText("一口价");
			price = Util.formatDoubleToString(goodsmodel.getAuctionPrice(), "元");
		}else {
//			viewHolder.price_type.setText("报价区间");
			price = Util.formatDoubleToString(goodsmodel.getLowPrice(), "元") + " ~ " +
					Util.formatDoubleToString(goodsmodel.getHighPrice(), "元");
		}
		String price_str = price + "元/" + goodsmodel.getSettleUnitText();
		Util.setText(mContext, price_str, price, viewHolder.price_text, R.color.theme_color, (int)mContext.getResources().getDimension(R.dimen.width_17));
		if(goodsmodel.getAuctionPriceTemplateStr() != null
				&& !"".equals(goodsmodel.getAuctionPriceTemplateStr())){
			viewHolder.price_tip.setText(goodsmodel.getAuctionPriceTemplateStr());
		}else {
			viewHolder.price_tip.setText("");
		}
		viewHolder.map_entry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						MapActivity.class);
				Bundle b = new Bundle();
				b.putString("mapType", "StartEnd");
				GoodsBubbleMsg startMsg = new GoodsBubbleMsg(),
						endMsg = new GoodsBubbleMsg();
				startMsg.setMessage("",
						"",
						goodsmodel.getSendAddr());

				endMsg.setMessage(goodsmodel.getRecvProvince(),
						goodsmodel.getRecvCity(),
						goodsmodel.getRecvAddr());

				b.putString("startProvinceAndCity", startMsg.getProvinceAndCity());
				b.putString("startLat", "" + goodsmodel.getSendLat());
				b.putString("startLng", "" + goodsmodel.getSendLng());
				b.putString("startAddr", startMsg.getAddr());

				b.putString("endProvinceAndCity", endMsg.getProvinceAndCity());
				b.putString("endLat", "" + goodsmodel.getRecvLat());
				b.putString("endLng", "" + goodsmodel.getRecvLng());
				b.putString("endAddr", endMsg.getAddr());
				intent.putExtras(b);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView startCity,endCity,startCounty, endCounty,goods_name,goods_weight,
			price_type, price_text, startTimeText,handling_charges_text,price_tip;
		LinearLayout price_layout;
		RelativeLayout map_entry;
		TextView mAuction,auction_number_text;
		ImageView tj_icon;
	}

	// 竞拍出价
	private String auction_url = GloableParams.HOST + "carrier/bid/dobid.do?";
	private void auction(GoodsModel model) {

		AuctionDoBid mAuctionDoBid;
		List<String> truckIds = new ArrayList<>();
		if(model.getAuctionType()== CommonRes.FIXED_PRICE){
			mAuctionDoBid = new AuctionDoBid(model.getAuctionId(),
					1, model.getAuctionPrice(), truckIds);
		}else {
			Toast.makeText(mContext,"非一口价单子!",Toast.LENGTH_LONG).show();
			return;
		}
		mAuctionDoBid.setBillTemplateId("");
		mAuctionDoBid.setFromFrontPage(true);
		String jsonString = JSON.toJSONString(mAuctionDoBid);
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog(mContext, "请稍等");
		AjaxParams params = new AjaxParams();
		params.put("bidJson", jsonString);
		DidiApp.getHttpManager().sessionPost(mContext,
				auction_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						mPromptManager.closeProgressDialog();
						if(!t.equals("")){
							Intent check_intent = new Intent(mContext, ResultActivity.class);
							check_intent.putExtra("result", 1);
							mContext.startActivity(check_intent);
							Intent refreshGoodsIntent = new Intent();
							refreshGoodsIntent.setAction(CommonRes.RefreshGoodsList);
							mContext.sendBroadcast(refreshGoodsIntent);
						}
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						mPromptManager.closeProgressDialog();
//						Log.d("huiyuan","确认接单错误信息:" + errMsg);
					}
				});
		}

	private final String check_privilege_url = GloableParams.HOST + "carrier/bid/checkpriv.do?";//查询竞价权限
	private void toAuctionOffer(final GoodsModel goodsmodel, final boolean isOneKey) {
		AjaxParams params = new AjaxParams();
		DidiApp.getHttpManager().sessionPost(mContext,
				check_privilege_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						if(isOneKey){
							auction(goodsmodel);
						}else {
							Intent offer_intent = new Intent(mContext, AuctionOfferActivity.class);
							offer_intent.putExtra("auctionId", goodsmodel.getAuctionId());
							if((goodsmodel.getInnerGoods() == 2) && "吨".equals(goodsmodel.getAssignUnitText())){
								offer_intent.putExtra("outer_goods_amount",goodsmodel.getGoodsAmountSp());
							}
							mContext.startActivity(offer_intent);
						}
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
					}
				});
	}
}
