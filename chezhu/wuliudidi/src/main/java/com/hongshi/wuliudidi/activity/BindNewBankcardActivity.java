package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

/**
 * @author huiyuan
 */
public class BindNewBankcardActivity extends Activity implements View.OnClickListener{
    private DiDiTitleView title;
    private EditText nameEdit, cardNumberEdit,bank_info_edittext;
    private Button bindButton;
    private String cardUserName = "", cardNumber = "",bankInfo = "";
    private RelativeLayout bindFailedLayout;
    private final String bindCardUrl = GloableParams.HOST + "uic/user/bankCard/create.do?";

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("BindNewBankcardActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("BindNewBankcardActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.bind_new_bankcard_activity);

        init();
    }

    private void init(){
        title = (DiDiTitleView) findViewById(R.id.add_bank_card_title);
        bindFailedLayout = (RelativeLayout) findViewById(R.id.bind_failed_layout);
        nameEdit = (EditText) findViewById(R.id.bankcard_username_edittext);
        cardNumberEdit = (EditText) findViewById(R.id.bankcard_number_edittext);
        bank_info_edittext = (EditText) findViewById(R.id.bank_info_edittext);
        bindButton = (Button) findViewById(R.id.bind_button);

        title.setTitle(getResources().getString(R.string.add_bankcard));
        nameEdit.setText(getSharedPreferences("config", Context.MODE_PRIVATE)
                .getString("name",""));
        title.setBack(BindNewBankcardActivity.this);

        bindButton.setOnClickListener(this);
    }

    private boolean inputJudge(){
        try{
            cardUserName = nameEdit.getText().toString();
            cardNumber = cardNumberEdit.getText().toString();
            bankInfo = bank_info_edittext.getText().toString();
        }catch (Exception e){
            return false;
        }
        if(cardNumber.equals("") || cardUserName.equals("")){
            ToastUtil.show(BindNewBankcardActivity.this, "请填写持卡人姓名及卡号");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bind_button:
                if(inputJudge()){
                    binkBankcard();
                }
                break;
            default:
                break;
        }
    }

    private void binkBankcard(){
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(BindNewBankcardActivity.this, "正在提交");
        AjaxParams params = new AjaxParams();
        params.put("userName", cardUserName);
        params.put("bankNumber", cardNumber);
        params.put("openBankName",bankInfo);
        DidiApp.getHttpManager().sessionPost(BindNewBankcardActivity.this, bindCardUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                //160002银行卡号格式不正确；160004目前仅支持XX、XX等银行；170101银行卡号错误
                mPromptManager.closeProgressDialog();
                if(errCode.equals("160002") || errCode.equals("160004")) {
                    bindFailedLayout.setVisibility(View.VISIBLE);
                }else{
                    bindFailedLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                //添卡成功发送广播
                Intent intent = new Intent();
                intent.setAction(CommonRes.BindNewBankcard);
                sendBroadcast(intent);
                finish();
            }
        });
    }
}
