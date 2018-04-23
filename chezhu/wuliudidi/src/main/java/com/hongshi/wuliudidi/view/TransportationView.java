package com.hongshi.wuliudidi.view;

import java.util.ArrayList;
import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.WinBidActivity;
import com.hongshi.wuliudidi.adapter.TransportationTaskAdapter;
import com.hongshi.wuliudidi.impl.RefreshAdapterCallBack;
import com.hongshi.wuliudidi.model.AllAuctionModel;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TransportationView extends LinearLayout {

	private TaskOrderTitleView mUnfinishView, mFinishView, mAllView;
	private View mView;
	private Context mContext;
	private ListView mOrderListView;
	private TransportationTaskAdapter mTransportationTaskAdapter;
	// private LinearLayout
	public TransportationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mView = View.inflate(mContext, R.layout.transportation_view, null);
		mUnfinishView = (TaskOrderTitleView) mView
				.findViewById(R.id.unfinish_view);
		mFinishView = (TaskOrderTitleView) mView.findViewById(R.id.finish_view);
		mAllView = (TaskOrderTitleView) mView.findViewById(R.id.all_view);
		mUnfinishView.setTitleText("未完成");
		mFinishView.setTitleText("已完成");
		mAllView.setTitleText("全部");
		mUnfinishView.setTextColor(getResources().getColor(R.color.line_press_color), true);
		mFinishView.setTextColor(getResources().getColor(R.color.home_text_none), false);
		mOrderListView.setAdapter(mTransportationTaskAdapter);
		clickListener();
		addView(mView);
	}

	private void clickListener() {
		mUnfinishView.setOnClickListener(new OnClick());
		mFinishView.setOnClickListener(new OnClick());
		mAllView.setOnClickListener(new OnClick());
		mOrderListView.setOnItemClickListener(new onItemClick());
	}

	class onItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(mContext,WinBidActivity.class);
			mContext.startActivity(intent);
		}
		
	}
	class OnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.unfinish_view:
				Util.selectStyle(mContext, 1, mUnfinishView, mFinishView,
						mAllView);
				break;
			case R.id.finish_view:
				Util.selectStyle(mContext, 2, mUnfinishView, mFinishView,
						mAllView);
				break;
			case R.id.all_view:
				Util.selectStyle(mContext, 3, mUnfinishView, mFinishView,
						mAllView);
				break;

			default:
				break;
			}
		}

	}
}
