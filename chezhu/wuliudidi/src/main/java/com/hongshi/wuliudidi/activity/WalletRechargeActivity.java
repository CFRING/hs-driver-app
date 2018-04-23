package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

/**
 * @author huiyuan
 */
public class WalletRechargeActivity extends Activity implements OnClickListener {
    RelativeLayout bankCardLayout, unionpayLayout;
    DiDiTitleView mTitle;

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("WalletRechargeActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("WalletRechargeActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.wallet_recharge_activity);

        init();
    }

    private void init() {
        mTitle = (DiDiTitleView) findViewById(R.id.wallet_recharge_title);
        mTitle.setBack(WalletRechargeActivity.this);
        mTitle.setTitle(getResources().getString(R.string.recharge));

        bankCardLayout = (RelativeLayout) findViewById(R.id.by_bank_card_layout);
        unionpayLayout = (RelativeLayout) findViewById(R.id.by_unionpay_layout);

        bankCardLayout.setOnClickListener(this);
        unionpayLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.by_bank_card_layout:
                Intent mIntent =  new Intent(WalletRechargeActivity.this, BankCardRechargeActivity.class);
                mIntent.putExtra("pageType", 1);
                startActivity(mIntent);
                break;
            case R.id.by_unionpay_layout:
                /*DatePickerDialog dialog = new DatePickerDialog(WalletRechargeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    }
                },
                2000, 10, 10);
                dialog.show();*/
                break;
            default:
                break;
        }

    }
}