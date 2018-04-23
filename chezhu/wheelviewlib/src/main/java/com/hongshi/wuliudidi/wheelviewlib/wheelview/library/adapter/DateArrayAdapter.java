package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongshi.wuliudidi.wheelviewlib.R;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.utils.LayoutParamsUtil;


public class DateArrayAdapter extends ArrayWheelAdapter<String> {

    private static final String TAG = "DateArrayAdapter";

    // Index of current item
    int currentItem;
    // Index of item to be highlighted
    int currentValue;
    Context context;
    private TextView mTextView;
    private LayoutParamsUtil util;

    private int mTextSize ;
    private int mCurrentTextColor;
    private int mTextColor;
    private int mTextLeft;
    private int mTextTop;
    private int mTextRight;
    private int mTextBottom;

    /**
     * Constructor
     */
    public DateArrayAdapter(Context context, String[] items, int current) {
        super(context, items);
        this.currentValue = current;
        this.context = context;
        util = new LayoutParamsUtil(context);
        Resources res = context.getResources();
        mTextSize = (int)res.getDimension(R.dimen.wheel_text_size);
        mCurrentTextColor = R.color.wheel_black;
        mTextColor = R.color.wheel_gray_light;
        mTextLeft = (int)res.getDimension(R.dimen.wheel_text_padding_left);
        mTextTop = (int)res.getDimension(R.dimen.wheel_text_padding_top);
        mTextRight = (int)res.getDimension(R.dimen.wheel_text_padding_right);
        mTextBottom = (int)res.getDimension(R.dimen.wheel_text_padding_bottom);
        setItemResource(context.getResources().getColor(R.color.wheel_white));
    }

    @Override
    protected void configureTextView(TextView view) {
        super.configureTextView(view);
        mTextView = view;
        if (currentItem == currentValue) {
            view.setTextColor(context.getResources().getColor(mCurrentTextColor));
        } else {
            view.setTextColor(context.getResources().getColor(mTextColor));
        }
        view.setTextSize(util.getTextSize(mTextSize));
        mTextView.setPadding(util.getWidth(mTextLeft), util.getHeight(mTextTop),
                util.getWidth(mTextRight), util.getHeight(mTextBottom));
        view.setTypeface(Typeface.SANS_SERIF);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        currentItem = index;
        return super.getItem(index, cachedView, parent);
    }

    public void setItemTextSize(int size){
        mTextSize = size;
    }

    public void setItemPadding(int left, int top, int right, int bottom){
        mTextLeft = left;
        mTextTop = top;
        mTextRight = right;
        mTextBottom = bottom;
    }

    public void setItemTextColor(int color){
        mTextColor = color;
    }

    public void setCurrentItemTextColor(int color){
        mCurrentTextColor = color;
    }
}
