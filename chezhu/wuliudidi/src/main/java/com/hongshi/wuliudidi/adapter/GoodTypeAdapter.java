package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.GoodsTypeModel;

import java.util.List;

/**
 * Created by bian on 2016/7/18 10:20.
 */
public class GoodTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsTypeModel> mList;
    //表示选中的是第几个：-1表示没有选中的
    private int mPosition;

    public GoodTypeAdapter(Context mContext, List<GoodsTypeModel> mList, int mPosition) {
        this.mContext = mContext;
        this.mList = mList;
        this.mPosition = mPosition;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_good_type, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.goodTypeText.setText(mList.get(i).getValue());
        if (i == mPosition){
            viewHolder.goodTypeText.setBackgroundResource(R.drawable.good_type_choosed_bj);
        } else {
            viewHolder.goodTypeText.setBackgroundResource(R.drawable.good_type_choose_bj);
        }
        return view;
    }

    public void setChanged(int position) {
        mPosition = position;
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView goodTypeText;

        ViewHolder(View view) {
            goodTypeText = (TextView) view.findViewById(R.id.good_type_text);
        }
    }
}
