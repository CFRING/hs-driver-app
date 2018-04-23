package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.wheelviewlib.R;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.OnWheelChangedListener;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.WheelViewLib;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.adapter.DateArrayAdapter;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.utils.LayoutParamsUtil;

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
    private WheelViewLib mWheelView;
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
        mWheelView = (WheelViewLib) findViewById(R.id.wheel_view);
        LayoutParamsUtil layoutParamsUtil = new LayoutParamsUtil(mContext);
        layoutParamsUtil.setWheelDialog((RelativeLayout) findViewById(R.id.lib_title_layout), mCancelTxt,
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
    public void onChanged(WheelViewLib wheel, int oldValue, int newValue) {
        if (wheel == mWheelView) {
            mCurrentItem = mData[mWheelView.getCurrentItem()];
            mWheelView.setViewAdapter(new DateArrayAdapter(mContext, mData, newValue));
        }
    }
}