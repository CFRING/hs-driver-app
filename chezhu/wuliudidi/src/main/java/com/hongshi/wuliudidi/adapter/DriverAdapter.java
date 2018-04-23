package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.DriverBookListVO;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.CircleImageView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

public class DriverAdapter extends BaseAdapter {
    private Context mContext;
    private List<DriverBookListVO> mDriverList;
    private FinalBitmap mFinalBitmap;

    public DriverAdapter(Context context, List<DriverBookListVO> mTeamMemberList) {
        this.mDriverList = mTeamMemberList;
        this.mContext = context;
        mFinalBitmap = FinalBitmap.create(context);
    }

    @Override
    public int getCount() {
        return mDriverList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDriverList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_my_driver, null);
            viewHolder = new ViewHolder();
            //队员头像
            viewHolder.head_image = (CircleImageView) convertView.findViewById(R.id.head_image);
            //队员姓名
            viewHolder.driver_name = (TextView) convertView.findViewById(R.id.member_name);
            //队员车辆车牌号
            viewHolder.driver_phone_number = (TextView) convertView.findViewById(R.id.plate_number);
            viewHolder.flagText = (TextView) convertView.findViewById(R.id.flag_text);
            viewHolder.callPhoneImg = (ImageView) convertView.findViewById(R.id.call_phone_image);
            viewHolder.trucksLay = (LinearLayout) convertView.findViewById(R.id.trucks_layout);
            viewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DriverBookListVO mDriverModel = mDriverList.get(position);
        if (mDriverModel.getUserFace() == null || mDriverModel.getUserFace().equals("")) {
            viewHolder.head_image.setImageResource(R.drawable.user);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.default_photo);
            mFinalBitmap.display(viewHolder.head_image, mDriverModel.getUserFace(),bitmap,bitmap);
        }

        if (mDriverModel.isOwner()) {
            viewHolder.flagText.setVisibility(View.VISIBLE);
        } else {
            viewHolder.flagText.setVisibility(View.GONE);
        }

        if (mDriverModel.getNickName() != null && mDriverModel.getNickName().length() > 0) {
            viewHolder.driver_name.setText(mDriverModel.getNickName());
        } else if (mDriverModel.getRealName() != null && mDriverModel.getRealName().length() > 0) {
            viewHolder.driver_name.setText(mDriverModel.getRealName());
        } else {
            viewHolder.driver_name.setText(R.string.unknown);
        }
        viewHolder.driver_phone_number.setText(mDriverModel.getCellphone());
        if (TextUtils.isEmpty(mDriverModel.getCellphone())) {
            viewHolder.callPhoneImg.setEnabled(false);
        } else {
            viewHolder.callPhoneImg.setEnabled(true);
        }
        viewHolder.callPhoneImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.call(mContext, mDriverModel.getCellphone());
            }
        });

        List<String> mStringList = mDriverModel.getTruckNumberList();

        if (mStringList != null && !mStringList.isEmpty()) {
            viewHolder.trucksLay.setVisibility(View.VISIBLE);
            viewHolder.line.setVisibility(View.VISIBLE);
        } else {
            viewHolder.trucksLay.setVisibility(View.GONE);
            viewHolder.line.setVisibility(View.GONE);
        }
        if (mStringList != null && !mStringList.isEmpty() &&
                viewHolder.trucksLay.getVisibility() == View.VISIBLE) {
            viewHolder.trucksLay.removeAllViews();
            LinearLayout linearLayout = null;
            for (int i = 0; i < mStringList.size(); i++) {
                if (i % 3 == 0) {
                    linearLayout = (LinearLayout) View.inflate(mContext, R.layout.item_truck, null);
                    viewHolder.trucksLay.addView(linearLayout);
                }
                TextView textView;
                if (i % 3 == 0) {
                    textView = (TextView) linearLayout.findViewById(R.id.truck_text1);
                    textView.setText(mStringList.get(i));
                    textView.setVisibility(View.VISIBLE);
                }
                if (i % 3 == 1) {
                    textView = (TextView) linearLayout.findViewById(R.id.truck_text2);
                    textView.setText(mStringList.get(i));
                    textView.setVisibility(View.VISIBLE);
                }
                if (i % 3 == 2) {
                    textView = (TextView) linearLayout.findViewById(R.id.truck_text3);
                    textView.setText(mStringList.get(i));
                    textView.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        CircleImageView head_image;
        TextView driver_name, driver_phone_number;
        TextView flagText;
        ImageView callPhoneImg;
        LinearLayout trucksLay;
        View line;
    }
}
