package com.hongshi.wuliudidi.incomebook;

/**
 * Created by huiyuan on 2016/8/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoneyTipsDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView gong_zi_zong_er,ka_hao_1,ka_hao_2,ka_hao_3,jin_er_text1,jin_er_text2,jin_er_text3;
    private LinearLayout yin_hang_ka_container,yin_hang_ka_jin_er_container;
    private ImageView cancel;
    private String bank_cards[];
    private double money_in_card[];
    private String gong_zi;

    public MoneyTipsDialog(Context context,String [] cards, double[] money, String gong_zi) {
        super(context);
        this.mContext = context;
        this.bank_cards = cards;
        this.money_in_card = money;
        this.gong_zi = gong_zi;
        init();
    }
    public MoneyTipsDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        init();
    }

    private void init(){
        setContentView(R.layout.money_tips_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        gong_zi_zong_er = (TextView)findViewById(R.id.gong_zi_zong_er);
        ka_hao_1 = (TextView)findViewById(R.id.ka_hao_1);
        ka_hao_2 = (TextView)findViewById(R.id.ka_hao_2);
        ka_hao_3 = (TextView)findViewById(R.id.ka_hao_3);
        jin_er_text1 = (TextView)findViewById(R.id.jin_er_text1);
        jin_er_text2 = (TextView)findViewById(R.id.jin_er_text2);
        jin_er_text3 = (TextView)findViewById(R.id.jin_er_text3);

        yin_hang_ka_container = (LinearLayout)findViewById(R.id.yin_hang_ka_container);
        yin_hang_ka_jin_er_container = (LinearLayout)findViewById(R.id.yin_hang_ka_jin_er_container);
        cancel = (ImageView) findViewById(R.id.cancel);

        gong_zi_zong_er.setText("工资总额" + gong_zi);
        if(bank_cards != null && bank_cards.length > 0){
            if(bank_cards.length == 1){
                ka_hao_1.setText(bank_cards[0]);
                jin_er_text1.setText(Util.formatDoubleToString(money_in_card[0],"元"));
            }else if(bank_cards.length == 2){
                ka_hao_1.setText(bank_cards[0]);
                ka_hao_2.setText(bank_cards[1]);
                jin_er_text1.setText(Util.formatDoubleToString(money_in_card[0],"元"));
                jin_er_text2.setText(Util.formatDoubleToString(money_in_card[1],"元"));
            }else if(bank_cards.length == 3){
                ka_hao_1.setText(bank_cards[0]);
                ka_hao_2.setText(bank_cards[1]);
                ka_hao_3.setText(bank_cards[2]);
                jin_er_text1.setText(Util.formatDoubleToString(money_in_card[0],"元"));
                jin_er_text2.setText(Util.formatDoubleToString(money_in_card[1],"元"));
                jin_er_text3.setText(Util.formatDoubleToString(money_in_card[2],"元"));
            }
        }else {
            ka_hao_1.setVisibility(View.INVISIBLE);
            ka_hao_2.setVisibility(View.INVISIBLE);
            ka_hao_3.setVisibility(View.INVISIBLE);
            jin_er_text1.setVisibility(View.INVISIBLE);
            jin_er_text2.setVisibility(View.INVISIBLE);
            jin_er_text3.setVisibility(View.INVISIBLE);
        }
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

}
