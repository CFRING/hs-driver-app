package com.hongshi.wuliudidi.adapter;

import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.model.InviteModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AuctionMessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<InviteModel> mMessageList;
	//同意协商
	private String accept_url = GloableParams.HOST + "carrier/bidbargain/accept.do?";
	//拒绝协商
	private String refuse_url = GloableParams.HOST + "carrier/bidbargain/refuse.do?";
	private ViewHolder viewHolder;
	private RefreshAdapterCallBack mRefreshAdapterCallBack;
	public AuctionMessageAdapter(Context context, List<InviteModel> mMessageList,RefreshAdapterCallBack mRefreshAdapterCallBack) {
		this.mMessageList = mMessageList;
		this.mContext = context;
		this.mRefreshAdapterCallBack = mRefreshAdapterCallBack;
	}

	@Override
	public int getCount() {
		return mMessageList.size();
	}

	@Override
	public InviteModel getItem(int position) {
		return mMessageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.message_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.message_type = (TextView) convertView.findViewById(R.id.message_type);
			viewHolder.no = (TextView) convertView.findViewById(R.id.no);
			viewHolder.yes = (TextView) convertView.findViewById(R.id.yes);
			viewHolder.message_image = (ImageView) convertView.findViewById(R.id.message_image);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			//消息内容
			viewHolder.message_content = (TextView) convertView.findViewById(R.id.message_content);
		//处理同意或者拒绝的布局
			viewHolder.accept_layout = (LinearLayout) convertView.findViewById(R.id.accept_layout);
			viewHolder.detail_layout = (RelativeLayout) convertView.findViewById(R.id.detail_layout);
			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		InviteModel messagemodel = mMessageList.get(position);
		viewHolder.message_type.setText("接单消息");
		viewHolder.message_image.setImageResource(R.drawable.auction_message);
		viewHolder.time.setText(Util.formatDateSecond(messagemodel.getGmtCreate()));
		int resultType = messagemodel.getDealResult();
		final String mesg_id = messagemodel.getMsgId();
		viewHolder.message_content.setText(messagemodel.getRealContent());
		List<String> params = messagemodel.getParams();
		if(messagemodel.getMsgSubBizType().equals("AUCTION_PASS_AUDIT")){

			//竞价审核通过消息
		}else if(messagemodel.getMsgSubBizType().equals("AUCTION_BARGIN")){
			viewHolder.accept_layout.setVisibility(View.VISIBLE);
			if(resultType == 0){
				if(params != null && params.size()>5){
					final String id = params.get(5);//ID
					//未处理
					viewHolder.no.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							askResult(false,id,mesg_id);
						}
					});
					viewHolder.yes.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							askResult(true,id,mesg_id);
						}
					});
				}
				
			}else if(resultType == 1){
				//同意
				viewHolder.yes.setBackgroundResource(R.color.white);
				viewHolder.yes.setTextColor(mContext.getResources().getColor(R.color.theme_color));
				viewHolder.yes.setText("已同意");
				viewHolder.no.setVisibility(View.GONE);
			}else if(resultType == 2){
				//拒绝
				viewHolder.yes.setBackgroundResource(R.color.white);
				viewHolder.yes.setText("已拒绝");
				viewHolder.no.setVisibility(View.GONE);
			}
			
		}else if(messagemodel.getMsgBizType().equals("FRIENDSHIP")){
			
		}
		return convertView;
	}
	/**
	 * 
	 * @param type 同意或者拒绝标记 
	 * @param id 协商ID
	 */
	private void askResult(final Boolean isAccept, String id,String msgId) {
		String url = "";
		if(isAccept){
			url = accept_url;
		}else{
			url = refuse_url;
		}
		AjaxParams params = new AjaxParams();
		params.put("bidBargainId", id);
		params.put("msgId", msgId);
		DidiApp.getHttpManager().sessionPost(mContext, url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				ToastUtil.show(mContext, "发送成功");
				if(isAccept){
					viewHolder.yes.setBackgroundResource(R.color.white);
					viewHolder.yes.setText("已同意");
					viewHolder.no.setVisibility(View.GONE);
					mRefreshAdapterCallBack.isAccept(true);
				}else{
					viewHolder.no.setBackgroundResource(R.color.white);
					viewHolder.yes.setText("已拒绝");
					viewHolder.yes.setVisibility(View.GONE);
					mRefreshAdapterCallBack.isAccept(false);
				}
			}
		});
	}
	static class ViewHolder {
		TextView message_type,time,message_content;
		TextView no,yes;
		ImageView message_image;
		LinearLayout accept_layout;
		RelativeLayout detail_layout;
	}
}
