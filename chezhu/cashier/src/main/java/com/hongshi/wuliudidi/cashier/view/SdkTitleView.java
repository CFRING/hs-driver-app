package com.hongshi.wuliudidi.cashier.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.cashier.R;

/**
 * Created by he on 2016/4/23.
 */
public class SdkTitleView extends RelativeLayout {
    private View mView;
    private ImageView mBackImage;
    private Context mContext;
    private TextView mTitle,mRightText;
    public SdkTitleView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SdkTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public SdkTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }
    private void init() {
        mView = View.inflate(mContext, R.layout.sdk_title_view, null);
        mBackImage = (ImageView) mView.findViewById(R.id.back);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mRightText = (TextView) mView.findViewById(R.id.right_text);
        addView(mView);
    }
    public void setBack(final Activity mActivity){
        mBackImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }
    public void setBack(final Activity mActivity, final Class<?> activityCls){
        mBackImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mActivity,activityCls);
                mActivity.startActivity(mIntent);
                mActivity.finish();
            }
        });
    }
    public void hideBack(){
        mBackImage.setVisibility(View.GONE);
    }
    public void setTitle(String name){
        mTitle.setText(name);
    }
    //得到右边textview并显示
    public TextView getRightTextView(){
        mRightText.setVisibility(View.VISIBLE);
        return mRightText;
    }

    public ImageView getBackImageView(){
        return mBackImage;
    }
}
