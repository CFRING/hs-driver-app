package com.hongshi.wuliudidi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by huiyuan on 2017/4/15.
 */

public class TextViewWithDrawable extends TextView {

    public TextViewWithDrawable(Context context){
        super(context);
    }

    public TextViewWithDrawable(Context context, AttributeSet set){
        super(context,set);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (null != drawables) {
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            //文字宽度
            float textWidth = getPaint().measureText(getText().toString());
            if (null != drawableLeft) {
                setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                //内容区域
                float contentWidth = textWidth + getCompoundDrawablePadding() + drawableLeft.getIntrinsicWidth();
                //向X轴的正方向平移
                canvas.translate((getWidth() - contentWidth) / 2, 0);
            }
            if (null != drawableRight) {
                //xml中不必设置Gravity，右边图片必须要设置Graviey为End，否则translate后文字看不到
                setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                float contentWidth = textWidth + getCompoundDrawablePadding() + drawableRight.getIntrinsicWidth();
                //向X轴的负方向平移
                canvas.translate(-(getWidth() - contentWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}
