package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.DbMsgBodyVO;
import com.hongshi.wuliudidi.utils.Util;

import java.util.List;


/**
 * Created by huiyuan on 2017/2/20.
 */


public class BussinessActivityAdapter extends BaseAdapter {

    private Context mContext;
    private List<DbMsgBodyVO> mMessageList;
    private int messageType;
    public BussinessActivityAdapter(Context context, List<DbMsgBodyVO> mMessageList,int message_type) {
        this.mMessageList = mMessageList;
        this.mContext = context;
        this.messageType = message_type;
    }

    public void addData(List<DbMsgBodyVO> list){
        this.mMessageList.addAll(list);
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public DbMsgBodyVO getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
            if (messageType == 6){
                convertView = View.inflate(mContext,R.layout.message_item_new,null);
            }else{
                convertView = View.inflate(mContext, R.layout.message_list_item, null);
                viewHolder.check_detail = (TextView) convertView.findViewById(R.id.check_detail);
                viewHolder.my_right_icon = (ImageView) convertView.findViewById(R.id.my_right_icon);
            }

        viewHolder.time = (TextView) convertView.findViewById(R.id.time);
        viewHolder.message_content = (TextView) convertView.findViewById(R.id.message_content);
            viewHolder.message_type = (TextView) convertView.findViewById(R.id.message_type);
            viewHolder.message_image = (ImageView) convertView.findViewById(R.id.message_image);
        convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        if(messageType == 4){
            viewHolder.message_type.setText("钱包消息");
            viewHolder.message_image.setImageResource(R.drawable.wallet_message);
            viewHolder.check_detail.setVisibility(View.VISIBLE);
            viewHolder.my_right_icon.setVisibility(View.VISIBLE);
        }else if(messageType == 5){
            viewHolder.message_type.setText("业务通知");
            viewHolder.message_image.setImageResource(R.drawable.bussiness_message);
            viewHolder.check_detail.setVisibility(View.VISIBLE);
            viewHolder.my_right_icon.setVisibility(View.VISIBLE);
        }

        DbMsgBodyVO model = mMessageList.get(position);
        viewHolder.time.setText(Util.formatDateSecond(model.getGmtCreate()));
        viewHolder.message_content.setText(model.getRealContent());
        if(messageType == 6){
            viewHolder.message_type.setText(model.getTitle());
            viewHolder.message_image.setImageResource(R.drawable.home_one);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView message_type,time,message_content,check_detail;
        ImageView message_image,my_right_icon;
    }
}
