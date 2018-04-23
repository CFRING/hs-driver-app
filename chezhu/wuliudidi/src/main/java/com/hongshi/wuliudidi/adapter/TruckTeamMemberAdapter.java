package com.hongshi.wuliudidi.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TeamMemberModel;
import com.hongshi.wuliudidi.view.CircleImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TruckTeamMemberAdapter extends BaseAdapter {

	private Context mContext;
	private List<TeamMemberModel> mTeamMemberList;
	private FinalBitmap mFinalBitmap;
	
	public TruckTeamMemberAdapter(Context context, List<TeamMemberModel> mTeamMemberList) {
		this.mTeamMemberList = mTeamMemberList;
		this.mContext = context;
		mFinalBitmap = FinalBitmap.create(context);
	}

	@Override
	public int getCount() {
		return mTeamMemberList.size();
	}

	@Override
	public TeamMemberModel getItem(int position) {
		return mTeamMemberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.team_nember_item_view, null);
			viewHolder = new ViewHolder();
			//队员头像
			viewHolder.head_image = (CircleImageView) convertView.findViewById(R.id.head_image);
			//队员姓名
			viewHolder.member_name = (TextView) convertView.findViewById(R.id.member_name);
			//队员车辆车牌号
			viewHolder.plate_number = (TextView) convertView.findViewById(R.id.plate_number);
			//车高度
			viewHolder.truck_height = (TextView) convertView.findViewById(R.id.truck_height);
			//货车车型
			viewHolder.truck_type = (TextView) convertView.findViewById(R.id.truck_type);
			//货车载重量
			viewHolder.truck_load = (TextView) convertView.findViewById(R.id.truck_load);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		TeamMemberModel mTeamMember = mTeamMemberList.get(position);
		if(mTeamMember.getUserFace()==null || mTeamMember.getUserFace().equals("")){
			viewHolder.head_image.setImageResource(R.drawable.user);
		}else{
			mFinalBitmap.display(viewHolder.head_image, mTeamMember.getUserFace());
		}
		viewHolder.member_name.setText(mTeamMember.getName());
		if(mTeamMember.getTruckId() == null){
			viewHolder.plate_number.setText(mContext.getResources().getString(R.string.have_no_truck));
			viewHolder.truck_height.setText("");
			viewHolder.truck_load.setText("");
			viewHolder.truck_type.setText("");
		}else{
			viewHolder.plate_number.setText(mTeamMember.getTruckNumber());
			viewHolder.truck_height.setText(mTeamMember.getTruckLengthText());
			viewHolder.truck_load.setText(""+mTeamMember.getCarryCapacity()+"吨");
			viewHolder.truck_type.setText(mTeamMember.getTruckTypeText());
		}
		
		return convertView;
	}

	static class ViewHolder {
		CircleImageView head_image;
		TextView member_name, plate_number, truck_type, truck_height, truck_load;
		
	}
}
