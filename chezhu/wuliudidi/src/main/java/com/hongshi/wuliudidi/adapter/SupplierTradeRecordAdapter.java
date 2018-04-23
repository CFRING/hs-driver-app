package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.SupplierTradeRecordItemModel;
import com.hongshi.wuliudidi.model.SupplierTradeRecordModel;
import com.hongshi.wuliudidi.view.CircleImageView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by huiyuan on 2017/9/15.
 */

public class SupplierTradeRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<SupplierTradeRecordItemModel> list;
    private FinalBitmap mFinalBitmap;
    private String currentDate = "";

    public SupplierTradeRecordAdapter(Context mContext, List<SupplierTradeRecordItemModel> list){
        this.mContext = mContext;
        this.list = list;
        mFinalBitmap = FinalBitmap.create(mContext);
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(list != null && i < list.size() && i >= 0){
            return list.get(i);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.supplier_trade_record_list_item, null);

            viewHolder.image = (CircleImageView) convertView.findViewById(R.id.image);
            viewHolder.date_text = (TextView) convertView.findViewById(R.id.date_text);
            viewHolder.supplier = (TextView) convertView.findViewById(R.id.supplier);
            viewHolder.consume_date = (TextView) convertView.findViewById(R.id.consume_date);
            viewHolder.money_text = (TextView) convertView.findViewById(R.id.money_text);
            viewHolder.date_container = (RelativeLayout) convertView.findViewById(R.id.date_container);
            viewHolder.consume_info_container = (RelativeLayout) convertView.findViewById(R.id.consume_info_container);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SupplierTradeRecordItemModel model = list.get(position);
        viewHolder.date_text.setText(model.getCreateDate());
        viewHolder.supplier.setText(model.getSupplierShowNickName());
        viewHolder.consume_date.setText(model.getPayDateTime());
        viewHolder.money_text.setText(model.getMoneyStr());

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.head_def_img);
        mFinalBitmap.display(viewHolder.image,model.getStorePhoto(),bitmap);

        if(!currentDate.equals(model.getCreateDate())){
            viewHolder.date_container.setVisibility(View.VISIBLE);
            currentDate = model.getCreateDate();
        }else {
            viewHolder.date_container.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class ViewHolder{
        CircleImageView image;
        TextView date_text,supplier,consume_date,money_text;
        RelativeLayout date_container,consume_info_container;
    }
}
