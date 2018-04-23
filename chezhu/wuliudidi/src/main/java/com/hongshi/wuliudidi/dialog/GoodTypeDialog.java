package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.GoodTypeAdapter;
import com.hongshi.wuliudidi.model.GoodsTypeModel;

import java.util.List;


/**
 * Created by bian on 2016/7/15 17:22.
 */
public class GoodTypeDialog extends Dialog implements View.OnClickListener {

    private TextView mTitleText;
    private GridView mGoodType;
    private Button mResetBtn;
    private Button mSureBtn;
    private Context mContext;
    private List<GoodsTypeModel> mList;
    //表示选中的是第几个：-1表示没有选中的
    private int mPosition;
    private GoodTypeAdapter mAdapter;
    private OnSureBtnClickListener mListener;

    //确定按钮的回调
    public interface OnSureBtnClickListener{
        //表示选中的是第几个：-1表示没有选中的
        void onClick(int position);
    }

    public GoodTypeDialog(Context context, int theme, List<GoodsTypeModel> list, int position) {
        super(context, theme);
        mContext = context;
        mList = list;
        mPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_good_type);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mGoodType = (GridView) findViewById(R.id.good_type);
        mResetBtn = (Button) findViewById(R.id.reset);
        mSureBtn = (Button) findViewById(R.id.sure);
        init();
    }

    private void init() {
        Window window = getWindow();
        window.setGravity(Gravity.END);
        setCanceledOnTouchOutside(true);
        window.setWindowAnimations(R.style.animation_in_from_right);
        mAdapter = new GoodTypeAdapter(mContext, mList, mPosition);
        mGoodType.setAdapter(mAdapter);
        mGoodType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPosition = i;
                mAdapter.setChanged(mPosition);
            }
        });
        mSureBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);
    }

    public void setOnSureBtnClickListener(OnSureBtnClickListener listener){
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                mPosition = -1;
                mAdapter.setChanged(mPosition);
                break;
            case R.id.sure:
                mListener.onClick(mPosition);
                cancel();
                break;
            default:
                break;
        }
    }
}
