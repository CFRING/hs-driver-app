package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.view.FlowIndicator;

/**
 * Created by huiyuan on 2017/6/7.
 */

public class ChooseConsumeTypeDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView all,consume,widthdrawl,settle_account;
    private ImageView cancel;

    private Handler handler;
    private int accountType;

    public ChooseConsumeTypeDialog(Context context){
        super(context);
        this.mContext = context;
        initView();
    }

    public ChooseConsumeTypeDialog(Context context, int theme, Handler handler,int accountType){
        super(context,theme);
        this.mContext = context;
        this.handler = handler;
        this.accountType = accountType;
        initView();
    }

    private void initView(){
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.choose_consume_type_dialog);

        cancel = (ImageView) findViewById(R.id.cancel);
        all = (TextView) findViewById(R.id.all);
        consume = (TextView) findViewById(R.id.consume);
        widthdrawl = (TextView) findViewById(R.id.widthdrawl);
        settle_account = (TextView) findViewById(R.id.settle_account);

        if(accountType == 2){
            widthdrawl.setText("提油");
            widthdrawl.setVisibility(View.VISIBLE);
        }else if(accountType == 1){
            widthdrawl.setText("提现");
            widthdrawl.setVisibility(View.VISIBLE);
        }else {
            widthdrawl.setVisibility(View.GONE);
        }

        cancel.setOnClickListener(this);
        all.setOnClickListener(this);
        consume.setOnClickListener(this);
        widthdrawl.setOnClickListener(this);
        settle_account.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Message msg;
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
//                handler.sendEmptyMessage(0);
                break;
            case R.id.all:
                dismiss();
                handler.sendEmptyMessage(1);
                break;
            case R.id.consume:
                dismiss();
                handler.sendEmptyMessage(2);
                break;
            case R.id.widthdrawl:
                dismiss();
                handler.sendEmptyMessage(3);
                break;
            case R.id.settle_account:
                dismiss();
                handler.sendEmptyMessage(4);
                break;
            default:
                break;
        }
    }
}
