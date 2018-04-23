package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.utils;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.WheelViewLib;


/**
 * Created by he on 2016/5/26.
 */
public class LayoutParamsUtil {
    public Context mContext;
    public LayoutParamsUtil(Context context) {
        this.mContext = context;
    }
    /**
     *
     */

    public void setCaptrueParams(ImageView imageView,ImageView imageView1,ImageView imageView2){
        if (!DimensionsComputer.getInstance().IS_Dimensions_720P()) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView
                    .getLayoutParams();
            lp.width = DimensionsComputer.getInstance().getWidth(66);
            lp.height = DimensionsComputer.getInstance().getHeight(50);
            imageView.setLayoutParams(lp);
            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) imageView1
                    .getLayoutParams();
            lp1.width = DimensionsComputer.getInstance().getWidth(30);
            lp1.height = DimensionsComputer.getInstance().getHeight(60);
            imageView1.setLayoutParams(lp1);
            FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) imageView2
                    .getLayoutParams();
            lp2.width = DimensionsComputer.getInstance().getWidth(180);
            lp2.height = DimensionsComputer.getInstance().getHeight(180);
            imageView2.setLayoutParams(lp2);
        }
    }
    /**
     * 拍照查看界面
     * @param imageview
     * @param mBottomLayout
     * @param t1
     * @param t2
     */
    public void setPhotoPreview(ImageView imageview, RelativeLayout mBottomLayout,TextView t1,TextView t2){
        if (!DimensionsComputer.getInstance().IS_Dimensions_720P()) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageview
                    .getLayoutParams();
            lp.width = DimensionsComputer.getInstance().getScreenWidth();
            lp.height = lp.width;
//            lp.topMargin = getHeight(R.dimen.image_hight);
            imageview.setLayoutParams(lp);

            LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) mBottomLayout
                    .getLayoutParams();
            mBottomLayout.setLayoutParams(lp1);
            RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) t1
                    .getLayoutParams();
//            lp2.leftMargin = getWidth(R.dimen.textview_margin);
            t1.setLayoutParams(lp2);
            RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) t2
                    .getLayoutParams();
//            lp3.rightMargin = getWidth(R.dimen.textview_margin);
            t2.setLayoutParams(lp3);
        }
    }

    /**
     * 选择地址弹窗
     * @param titleLayout
     * @param cancelTxt
     * @param sureTxt
     * @param province
     * @param city
     * @param district
     */
    public void setWheelAddressDialog(RelativeLayout titleLayout, TextView cancelTxt, TextView sureTxt,
                                      WheelViewLib province, WheelViewLib city, WheelViewLib district){
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
            cancelTxt.setTextSize(dimensionsComputer.getTextSize((int) cancelTxt.getTextSize()));
            cancelTxt.setPadding(dimensionsComputer.getWidth(cancelTxt.getPaddingLeft()), 0, 0, 0);
            cancelTxt.setLayoutParams(lp1);
            //确定键
            lp1 = (RelativeLayout.LayoutParams) sureTxt.getLayoutParams();
            lp1.width = dimensionsComputer.getWidth(lp1.width);
            lp1.height = dimensionsComputer.getHeight(lp1.height);
            sureTxt.setTextSize(dimensionsComputer.getTextSize((int)sureTxt.getTextSize()));
            sureTxt.setPadding(0, 0, dimensionsComputer.getWidth(sureTxt.getPaddingRight()), 0);
            sureTxt.setLayoutParams(lp1);
            //选省滚轮
            lp = (LinearLayout.LayoutParams) province.getLayoutParams();
            lp.width = dimensionsComputer.getWidth(lp.width);
            lp.height = dimensionsComputer.getHeight(lp.height);
            province.setLayoutParams(lp);
            //选市滚轮
            lp = (LinearLayout.LayoutParams) city.getLayoutParams();
            lp.width = dimensionsComputer.getWidth(lp.width);
            lp.height = dimensionsComputer.getHeight(lp.height);
            city.setLayoutParams(lp);
            //选区滚轮
            lp = (LinearLayout.LayoutParams) district.getLayoutParams();
            lp.width = dimensionsComputer.getWidth(lp.width);
            lp.height = dimensionsComputer.getHeight(lp.height);
            district.setLayoutParams(lp);
        }
    }

    /**
     * 滚轮选择弹窗
     * @param titleLayout
     * @param cancelTxt
     * @param sureTxt
     * @param wheelViewLib
     */
    public void setWheelDialog(RelativeLayout titleLayout, TextView cancelTxt, TextView sureTxt,
                                      WheelViewLib wheelViewLib){
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

    public int getWidth(int dimen) {

        int width = DimensionsComputer.getInstance().getWidth(
                dimen);
        return width;
    }

    public int getHeight(int dimen) {
        int height = DimensionsComputer.getInstance().getHeight(
                dimen);
        return height;
    }

    public int getTextSize(int dimen) {
        int size = DimensionsComputer.getInstance().getTextSize(
                dimen);
        return size;
    }

}
