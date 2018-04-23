package com.hongshi.wuliudidi.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.AccountsItemModel;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountsItemAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<AccountsItemModel> mAccountsList;
	private FinalBitmap mFinalBitmap;
	
	public AccountsItemAdapter(Context context, List<AccountsItemModel> mAccountsList) {
		this.mAccountsList = mAccountsList;
		this.mContext = context;
		mFinalBitmap = FinalBitmap.create(context);
	}
	
	@Override
	public int getCount() {
		return mAccountsList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAccountsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addList(List<AccountsItemModel> mAdd){
		mAccountsList.addAll(mAdd);
	}
	
	public void clearList(){
		if(mAccountsList != null){
			mAccountsList.clear();
			notifyDataSetChanged();
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.accounts_item, null);
			
			viewHolder.dateText = (TextView) convertView.findViewById(R.id.date_text);
			viewHolder.goodsOnwerHead = (ImageView) convertView.findViewById(R.id.goodsowner_headview);
			viewHolder.goodsOnwerName = (TextView) convertView.findViewById(R.id.goodsowner_name);
			viewHolder.flagImage = (ImageView) convertView.findViewById(R.id.accounts_flag_image);
			viewHolder.startCityText = (TextView) convertView.findViewById(R.id.start_city_text);
			viewHolder.endCityText = (TextView) convertView.findViewById(R.id.end_city_text);
			viewHolder.startCountyText = (TextView) convertView.findViewById(R.id.start_county_text);
			viewHolder.endCountyText = (TextView) convertView.findViewById(R.id.end_county_text);
			viewHolder.goodDetailText = (TextView) convertView.findViewById(R.id.goods_name_weight_text);
			viewHolder.money = (TextView) convertView.findViewById(R.id.turnover);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		AccountsItemModel model = mAccountsList.get(position);
		
		//所属日期
		LinearLayout dateLayout = (LinearLayout) convertView.findViewById(R.id.date_layout);
		String dateStr = model.getDateStr();
		if(position > 0 && mAccountsList.get(position - 1).getDateStr().equals(dateStr)){
			dateLayout.setVisibility(View.GONE);
		}else{
			dateLayout.setVisibility(View.VISIBLE);
		}
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date date = fmt.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			viewHolder.dateText.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + "日"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//头像
		if(model.getUserFace() == null || model.getUserFace().equals("")){
			viewHolder.goodsOnwerHead.setImageResource(R.drawable.goodsowner_tacit_head);
		}else{
			mFinalBitmap.display(viewHolder.goodsOnwerHead, model.getUserFace());
		}
		
		//货主名称
		if(model.getSenderName() != null && !model.getSenderName().equals("")){
			viewHolder.goodsOnwerName.setText(model.getSenderName());
		}
		
		if(model.getSettleType() == 1){
			viewHolder.flagImage.setImageResource(R.drawable.accounts_finished);
		}else{
			viewHolder.flagImage.setImageResource(R.drawable.accounts_unfinish);
		}
		
		//发货地城市
		if(model.getSenderCity() != null && !model.getSenderCity().equals("")){
			viewHolder.startCityText.setText(model.getSenderCity());
		}
		
		//收货地城市
		if(model.getRecipientCity() != null && !model.getRecipientCity().equals("")){
			viewHolder.endCityText.setText(model.getRecipientCity());
		}
		
		//发货地区/县
		if(model.getSenderDistrict() != null && !model.getSenderDistrict().equals("")){
			viewHolder.startCountyText.setText(model.getSenderDistrict());
		}
		
		//收货地区/县
		if(model.getRecipientDistrict() != null && !model.getRecipientDistrict().equals("")){
			viewHolder.endCountyText.setText(model.getRecipientDistrict());
		}
		
		//货名、重量、体积
		String goodDetailStr = "";
		if(model.getGoodsName() != null && !model.getGoodsName().equals("")){
			goodDetailStr = model.getGoodsName();
		}
		goodDetailStr += "，" + model.getGoodsAmount() + model.getUnit();
		viewHolder.goodDetailText.setText(goodDetailStr);
		
		//金额
		if(model.getMoneyUnit() != null){
			viewHolder.money.setText("+" + Util.formatDoubleToString(model.getMoney(), "元"));
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView dateText, startCityText, endCityText, startCountyText, endCountyText, goodDetailText, money,
			goodsOnwerName;
		ImageView goodsOnwerHead, flagImage;
	}
}
