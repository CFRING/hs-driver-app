package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageItemView extends RelativeLayout {

	private View mView;
	private Context mContext;
	private TextView mMessageType;
	private ImageView mMessageImage,un_read_point;
//	private RelativeLayout mMessageLayout;
//	private TextView mNews;
	public MessageItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	private void init() {
		mView = View.inflate(mContext, R.layout.message_item, null);
		mMessageType = (TextView) mView.findViewById(R.id.message_type);
//		mMessageContent = (TextView) mView.findViewById(R.id.message_content);
//		mMessageTime = (TextView) mView.findViewById(R.id.time);
		mMessageImage = (ImageView) mView.findViewById(R.id.message_image);
		un_read_point = (ImageView) mView.findViewById(R.id.un_read_point);
//		mMessageLayout = (RelativeLayout) mView.findViewById(R.id.message_layout);
//		mNews = (TextView) mView.findViewById(R.id.message_news);
		addView(mView);		
	}
//	public void showNews(int number){
//		mMessageLayout.setVisibility(View.VISIBLE);
//		mNews.setText(""+number);
//	}
//	public void hideNews(){
//		mMessageLayout.setVisibility(View.GONE);
//	}
	public ImageView getMessageImage(){
		return mMessageImage;
	}
	public void setMessageType(String type){
		mMessageType.setText(type);
	}
	public void showRedPoint(boolean isShow){
		if(isShow){
			un_read_point.setVisibility(VISIBLE);
		}else {
			un_read_point.setVisibility(GONE);
		}
	}
//	public void setMessageContent(String content){
//		mMessageContent.setText(content);
//	}
//	public void setMessageTime(String time){
//		mMessageTime.setText(time);
//	}
}
