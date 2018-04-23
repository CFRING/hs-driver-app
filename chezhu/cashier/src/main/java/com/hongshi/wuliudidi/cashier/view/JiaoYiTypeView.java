package com.hongshi.wuliudidi.cashier.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.cashier.R;


/**
 * Created on 2016/4/25.
 * 交易类型和提现账户View
 */
public class JiaoYiTypeView extends RelativeLayout {

    private Context mContext;
    private TextView mjiaoYiTypeTxt, mTiXianAccountTxt;
    private View mTopLongLine, mTopLine, mBottomLongLine, mBottomLine;

    public JiaoYiTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.sdk_view_jiao_yi_type, null);
        mjiaoYiTypeTxt = (TextView) view.findViewById(R.id.jiao_yi_type_text);
        mTiXianAccountTxt = (TextView) view.findViewById(R.id.ti_xian_account_text);
        mTopLine = view.findViewById(R.id.top_line);
        mTopLongLine = view.findViewById(R.id.top_long_line);
        mBottomLine = view.findViewById(R.id.bottom_line);
        mBottomLongLine = view.findViewById(R.id.bottom_long_line);
        addView(view);
    }

    public void showTopLongLine(){
        mTopLine.setVisibility(GONE);
        mTopLongLine.setVisibility(VISIBLE);
    }

    public void showTopLine(){
        mTopLine.setVisibility(VISIBLE);
        mTopLongLine.setVisibility(GONE);
    }

    public void showBottomLongLine(){
        mBottomLine.setVisibility(GONE);
        mBottomLongLine.setVisibility(VISIBLE);
    }

    public void showBottomLine(){
        mBottomLine.setVisibility(VISIBLE);
        mBottomLongLine.setVisibility(GONE);
    }

    public void setJiaoYiType(String jiaoYiType){
        mjiaoYiTypeTxt.setText(jiaoYiType);
    }

    public void setTiXianAccount(String tiXianAccount){
        mTiXianAccountTxt.setText(tiXianAccount);
    }
}
