package com.hongshi.wuliudidi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.Util;

public class GoodsDetailsView extends LinearLayout{

	private View mView;
	private Context mContext;
	private TextView mAuctionImage,mPriceType,mPriceText,mDetailsNumber;
	private LinearLayout mAuctionOrderLayout,mPriceLayout,mDistLayout;
	private TextView mJoinNumber,mStartName,mEndName,mGoodsName,mGoodsWeight, mAuctionNumber,mDist,mTime;
	private RelativeLayout mRightLayout;
	public GoodsDetailsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.goods_list_item, null);
		mAuctionImage = (TextView) mView.findViewById(R.id.auction_image);
		mAuctionOrderLayout = (LinearLayout) mView.findViewById(R.id.auction_ordder_layout);
		mPriceLayout= (LinearLayout) mView.findViewById(R.id.price_layout);
		mRightLayout= (RelativeLayout) mView.findViewById(R.id.right_layout);
		mDistLayout= (LinearLayout) mView.findViewById(R.id.dist_layout);
		mPriceType = (TextView) mView.findViewById(R.id.price_type);
		mPriceText = (TextView) mView.findViewById(R.id.price_text);
		mJoinNumber = (TextView) mView.findViewById(R.id.join_num);
		mStartName = (TextView) mView.findViewById(R.id.start_name);
		mEndName = (TextView) mView.findViewById(R.id.end_name);
		mDist = (TextView) mView.findViewById(R.id.distance);
		mTime = (TextView) mView.findViewById(R.id.minute);
		mGoodsName = (TextView) mView.findViewById(R.id.goods_name);
		mGoodsWeight = (TextView) mView.findViewById(R.id.goods_weight);
		mAuctionNumber = (TextView) mView.findViewById(R.id.auction_number_text);
		mDetailsNumber = (TextView)mView. findViewById(R.id.details_number);
		addView(mView);
	}
	public void setPrice(int bg,double auctionPrice,String unitTxt, String shengYu){
//		mPriceLayout.setVisibility(View.VISIBLE);
		mPriceType.setBackgroundResource(bg);
		Util.setText(mContext,Util.formatDoubleToString((auctionPrice), "元")+" 元/"+unitTxt + "  " + shengYu,
				" 元/"+unitTxt + "  " + shengYu, mPriceText, R.color.gray);
	}
	//设置价格区间
	public void setPrice(int bg, String auctionPrice, String unitTxt){
		ViewGroup.LayoutParams layoutParams = mPriceType.getLayoutParams();
		layoutParams.width = (int) getResources().getDimension(R.dimen.width_48);
		if(bg == R.drawable.price_type3){
			mPriceLayout.setVisibility(View.VISIBLE);
		}else {
			mPriceLayout.setVisibility(View.GONE);
		}
		mPriceType.setBackgroundResource(bg);
		Util.setText(mContext, auctionPrice +" 元/"+unitTxt,
				" 元/"+unitTxt, mPriceText, R.color.gray);
	}
	public void setNumber(String num){
		mDetailsNumber.setText(num);
	}
	public void hideJoinView(){
		mJoinNumber.setVisibility(View.INVISIBLE);
	}
	public void joinNumber(int number){
		mJoinNumber.setVisibility(View.VISIBLE);
		mJoinNumber.setText("参与"+number+"人");
	}
	//设置距离和发布时间
	public void setDist(int distance,long diffPublish){
		mDistLayout.setVisibility(View.VISIBLE);
		String str = "";
		if(distance > 0){
			if((distance/1000)<1){
				str = "距离您"+ distance + "米";
			}else{
				str = "距离您"+(distance/1000) + "公里";
			}
			str += "   ";
		}
		str += Util.millisecondToDaysShort(diffPublish) + "前发布";
		mTime.setText(str);
	}
	public void setGoodsName(String goodsName){
		mGoodsName.setText(goodsName);
	}
	public void setGoodsWeight(String goodsWeight){
		mGoodsWeight.setText(goodsWeight);
	}
	public void showAuctionOrder(){
		mAuctionOrderLayout.setVisibility(View.VISIBLE);
	}
	public void setHideAuction(){
		mAuctionImage.setVisibility(View.GONE);
	}
	public void setHideTime(){
		mTime.setVisibility(View.GONE);
	}
	public void showArrow(){
		mRightLayout.setVisibility(View.VISIBLE);
	}
	public void setStartCity(String start_name){
		mStartName.setText(start_name);
	}
	public void setEndCity(String end_name){
		mEndName.setText(end_name);
	}
	public void setAuctionNumber(String auctionNumber){
		mAuctionNumber.setText(getResources().getString(R.string.auction_number) + "     " + auctionNumber);
	}
	public TextView getAuctionNumberTextView(){
		return mAuctionNumber;
	}
}
