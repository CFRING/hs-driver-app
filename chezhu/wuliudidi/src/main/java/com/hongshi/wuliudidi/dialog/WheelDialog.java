package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.DimensionsComputer;
import com.hongshi.wuliudidi.view.wheel.OnWheelChangedListener;
import com.hongshi.wuliudidi.view.wheel.WheelView;
import com.hongshi.wuliudidi.view.wheel.adapter.DateArrayAdapter;


/**
 * Created by bian on 2016/6/21 16:17.
 */
public class WheelDialog extends Dialog implements OnWheelChangedListener, View.OnClickListener {

    //回调接口
    public interface OnCancelBtnClickListener{
        void onClick();
    }
    public interface OnSureBtnClickListener{
        void onClick(String data);
    }
    private OnCancelBtnClickListener mOnCancelBtnClickListener;
    private OnSureBtnClickListener mOnSureBtnClickListener;
    //显示的数据
    private String[] mData;
    //确定取消按钮
    private TextView mCancelTxt, mSureTxt;
    //滚轮
    private WheelView mWheelView;
    private Context mContext;
    //当前选中项
    private String mCurrentItem;

    public WheelDialog(Context context, int theme, String[] data) {
        super(context, theme);
        mContext = context;
        mData = data;
        mCurrentItem = data[0];
        init();
    }

    private void init(){
        setContentView(R.layout.wheel_dialog);
        mCancelTxt = (TextView) findViewById(R.id.cancel_btn);
        mSureTxt = (TextView) findViewById(R.id.sure_btn);
        mWheelView = (WheelView) findViewById(R.id.wheel_view);
        setWheelDialog((RelativeLayout) findViewById(R.id.lib_title_layout), mCancelTxt,
                mSureTxt, mWheelView);

        mWheelView.setViewAdapter(new DateArrayAdapter(mContext, mData, 0));
        // 添加change事件
        mWheelView.addChangingListener(this);
        mWheelView.setVisibleItems(5);
        mWheelView.setDrawShadows(false);
        mCancelTxt.setOnClickListener(this);
        mSureTxt.setOnClickListener(this);
    }

    public void setOnCancelBtnClickListener(OnCancelBtnClickListener listener){
        mOnCancelBtnClickListener = listener;
    }

    public void setOnSureBtnClickListener(OnSureBtnClickListener listener){
        mOnSureBtnClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_btn) {
            if (mOnCancelBtnClickListener == null){
                cancel();
            }else {
                mOnCancelBtnClickListener.onClick();
            }
        }else if (i == R.id.sure_btn){
            if (mOnSureBtnClickListener != null) {
                mOnSureBtnClickListener.onClick(mCurrentItem);
            }else {
                cancel();
            }
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mWheelView) {
            mCurrentItem = mData[mWheelView.getCurrentItem()];
            mWheelView.setViewAdapter(new DateArrayAdapter(mContext, mData, newValue));
        }
    }

    public void setWheelDialog(RelativeLayout titleLayout, TextView cancelTxt, TextView sureTxt,
                               WheelView wheelViewLib){
        DimensionsComputer dimensionsComputer = DimensionsComputer.getInstance();
        dimensionsComputer.activate(mContext);
        if (!dimensionsComputer.IS_Dimensions_720P()) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) titleLayout.getLayoutParams();
            lp.width = dimensionsComputer.getWidth(lp.width);
            lp.height = dimensionsComputer.getHeight(lp.height);
            titleLayout.setLayoutParams(lp);
            //取消键
            RelativeLayout.LayoutParams lp1;
            lp1 = (RelativeLayout.LayoutParams) cancelTxt.getLayoutParams();
            lp1.width = dimensionsComputer.getWidth(lp1.width);
            lp1.height = dimensionsComputer.getHeight(lp1.height);
            cancelTxt.setTextSize(dimensionsComputer.getTextSize((int)cancelTxt.getTextSize()));
            cancelTxt.setPadding(dimensionsComputer.getWidth(cancelTxt.getPaddingLeft()), 0, 0, 0);
            cancelTxt.setLayoutParams(lp1);
            //确定键
            lp1 = (RelativeLayout.LayoutParams) sureTxt.getLayoutParams();
            lp1.width = dimensionsComputer.getWidth(lp1.width);
            lp1.height = dimensionsComputer.getHeight(lp1.height);
            sureTxt.setTextSize(dimensionsComputer.getTextSize((int)sureTxt.getTextSize()));
            sureTxt.setPadding(0, 0, dimensionsComputer.getWidth(sureTxt.getPaddingRight()), 0);
            sureTxt.setLayoutParams(lp1);
            //选择滚轮
            lp = (LinearLayout.LayoutParams) wheelViewLib.getLayoutParams();
            lp.width = dimensionsComputer.getWidth(lp.width);
            lp.height = dimensionsComputer.getHeight(lp.height);
            wheelViewLib.setLayoutParams(lp);
        }
    }
}