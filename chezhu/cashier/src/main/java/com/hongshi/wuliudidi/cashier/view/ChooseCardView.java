package com.hongshi.wuliudidi.cashier.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.cashier.R;


/**
 * Created on 2016/4/25.
 * 选择银行卡view
 */
public class ChooseCardView extends RelativeLayout{

    private Context mContext;
    private RelativeLayout mChooseCardLayout;
    private ImageView mYinHangImg;
    private TextView mYinHangTxt;
    private TextView mYinHangWeiHaoTxt;

    public ChooseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        View view = View.inflate(mContext, R.layout.sdk_view_choose_card, null);
        mChooseCardLayout = (RelativeLayout) view.findViewById(R.id.choose_card_layout);
        mYinHangImg = (ImageView) view.findViewById(R.id.yin_hang_image);
        mYinHangTxt = (TextView) view.findViewById(R.id.yin_hang_text);
        mYinHangWeiHaoTxt = (TextView) view.findViewById(R.id.yin_hang_wei_hao_text);
        addView(view);
    }

    public void setmYinHangImg(int bankType){
        switch (bankType){
            case 1:
                mYinHangImg.setImageResource(R.drawable.zhong_guo_yin_hang);
                break;
            case 2:
                mYinHangImg.setImageResource(R.drawable.gong_shang_yin_hang);
                break;
            case 4:
                mYinHangImg.setImageResource(R.drawable.jian_she_yin_hang);
                break;
            case 5:
                mYinHangImg.setImageResource(R.drawable.nong_ye_yin_hang);
                break;
        }
    }

    public void setmYinHangTxt(String bankName, String bankNumber){
        mYinHangTxt.setText(bankName);
        mYinHangWeiHaoTxt.setText(bankNumber);
    }
}
