package com.hongshi.wuliudidi.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.model.InviteModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InviteListAdapter extends BaseAdapter {

	private Context mContext;
	private List<InviteModel> mInviteList;
	private FinalBitmap bitmap ;
	private String ask_url= GloableParams.HOST +"uic/fleet/operateInvite.do?";
	private ViewHolder viewHolder;
	private RefreshAdapterCallBack mRefreshAdapterCallBack;
	public InviteListAdapter(Context context, List<InviteModel> mInviteList,RefreshAdapterCallBack mRefreshAdapterCallBack) {
		this.mInviteList = mInviteList;
		this.mContext = context;
		bitmap = FinalBitmap.create(mContext);
		this.mRefreshAdapterCallBack = mRefreshAdapterCallBack;
	}

	@Override
	public int getCount() {
		return mInviteList.size();
	}

	@Override
	public InviteModel getItem(int position) {
		return mInviteList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
//		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.invite_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.realContent = (TextView) convertView.findViewById(R.id.realContent);
			viewHolder.captain_name = (TextView) convertView.findViewById(R.id.captain_name);
			viewHolder.invite_time = (TextView) convertView.findViewById(R.id.invite_time);
			viewHolder.photo_image = (ImageView) convertView.findViewById(R.id.photo_image);
			viewHolder.buttom_layout = (RelativeLayout) convertView.findViewById(R.id.buttom_layout);
			viewHolder.no = (TextView) convertView.findViewById(R.id.no);
			viewHolder.yes = (TextView) convertView.findViewById(R.id.yes);
			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		InviteModel inviteModel = mInviteList.get(position);
		final String id = inviteModel.getMsgId();
		int resultType = inviteModel.getDealResult(); 
		List<String> params = inviteModel.getParams();
		if(inviteModel.getMsgSubBizType().equals("FRIENDSHIP_LEADER_INVITE")){
			viewHolder.buttom_layout.setVisibility(View.VISIBLE);
			if(resultType == 0){
				if(params != null && params.size()>1){
					//邀请人ID
					final String number = params.get(2);
					//未处理
					viewHolder.no.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							ToastUtil.show(mContext, "拒绝点击了");
							askResult(2,number,id);
						}
					});
					viewHolder.yes.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
//							ToastUtil.show(mContext, "同意点击了");
							askResult(1,number,id);
						}
					});
				}
				
			}else if(resultType == 1){
				//同意
				viewHolder.yes.setBackgroundResource(R.color.white);
				viewHolder.yes.setText("已同意");
				viewHolder.no.setVisibility(View.GONE);
			}else if(resultType == 2){
				//拒绝
				viewHolder.no.setBackgroundResource(R.color.white);
				viewHolder.no.setText("已拒绝");
				viewHolder.yes.setVisibility(View.GONE);
			}
		}else if(inviteModel.getMsgSubBizType().equals("FRIENDSHIP_DRIVER_AGREE")){
			//司机同意
			viewHolder.buttom_layout.setVisibility(View.VISIBLE);
			viewHolder.no.setVisibility(View.GONE);
			viewHolder.yes.setVisibility(View.GONE);
		}else if(inviteModel.getMsgSubBizType().equals("FRIENDSHIP_DRIVER_DISAGREE")){
			//司机拒绝
			viewHolder.buttom_layout.setVisibility(View.VISIBLE);
			viewHolder.no.setVisibility(View.GONE);
			viewHolder.yes.setVisibility(View.GONE);
		}else{
			//以后新添类型
			viewHolder.buttom_layout.setVisibility(View.GONE);
		}
		if(mInviteList.get(position).getParams().size()>4){
			viewHolder.captain_name.setText(mInviteList.get(position).getParams().get(4));	
		}else if(mInviteList.get(position).getParams().size()>3){
			bitmap.display(viewHolder.photo_image, mInviteList.get(position).getParams().get(3));
		}
		viewHolder.realContent.setText(mInviteList.get(position).getRealContent());
		viewHolder.invite_time.setText(Util.formatDateSecond(inviteModel.getGmtCreate()));
		return convertView;
	}

	static class ViewHolder {
		TextView no,yes,invite_time;
		TextView realContent,captain_name;

		ImageView photo_image;
		RelativeLayout buttom_layout;
	}
	/**
	 * 
	 * @param type 1,同意，2拒绝
	 * @param number 邀请人id
	 */
	private void askResult(final int type,String number,String msgId){
		AjaxParams params = new AjaxParams();
		params.put("type", ""+type);
		//邀请人ID
		params.put("sposorUserId", number);
		//消息ID
		params.put("msgId", msgId);
		DidiApp.getHttpManager().sessionPost(mContext, ask_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				if(type == 1){
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
}
