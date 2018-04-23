package com.hongshi.wuliudidi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;


/**
 * Created by huiyuan on 2017/8/15.
 * 选择油卡view
 */
public class ChooseCardView extends RelativeLayout{

    private Context mContext;
    private ImageView mOilImg,right_arrow_image;
    private TextView mOilTxt;
    private TextView mOilWeiHaoTxt;

    public ChooseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        View view = View.inflate(mContext, R.layout.sdk_view_choose_card1, null);
        mOilImg = (ImageView) view.findViewById(R.id.oil_image);
        right_arrow_image = (ImageView) view.findViewById(R.id.right_arrow_image);
        mOilTxt = (TextView) view.findViewById(R.id.oil_text);
        mOilWeiHaoTxt = (TextView) view.findViewById(R.id.oil_card_wei_hao_text);
        addView(view);
    }

    public void setOilImg(int type){
        switch (type){
            case 1:
                //中国石油
                mOilImg.setImageResource(R.drawable.shiyou_logo);
                break;
            case 2:
                //中国石化
                mOilImg.setImageResource(R.drawable.shihua_logo);
                break;
            case 3:
                //中国海油
                mOilImg.setImageResource(R.drawable.haiyou_logo);
                break;
            default:
                //其他
                mOilImg.setImageResource(R.drawable.qita_logo);
                break;
        }
    }

    public void setOilNameAndCardTxt(String OilName, String oilNumber){
        mOilTxt.setText(OilName);
        mOilWeiHaoTxt.setText(oilNumber);
    }

    public ImageView getRight_arrow_image(){
        if(right_arrow_image != null){
            return right_arrow_image;
        }else {
            return null;
        }
    }
}
