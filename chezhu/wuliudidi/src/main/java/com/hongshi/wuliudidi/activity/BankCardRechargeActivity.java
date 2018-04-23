package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.AES;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.ChooseBankcardDialog;
import com.hongshi.wuliudidi.dialog.VerificationCodeDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.BankcardModel;
import com.hongshi.wuliudidi.model.MoneyAccountModel;
import com.hongshi.wuliudidi.model.MoneyChildAccountModel;
import com.hongshi.wuliudidi.model.WalletModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * @author huiyuan
 */
public class BankCardRechargeActivity extends Activity implements View.OnClickListener{
    private DiDiTitleView title;
    private ImageView bankcardImage;
    private TextView bankcardText, inputNameText, restMoneyText, withdrawAllText;
    private View useAnotherCard;
    private EditText rechargeNumberEditText;
    private LinearLayout withdrawHindLayout;
    private Button rechargeButton;
    private ChooseBankcardDialog chooseBankcardDialog;
    //1.银行卡充值 2.余额转出
    private int pageType;
    private final String walletDataUrl = GloableParams.HOST + "uic/user/myWallet.do?";
    private final String withdralUrl = GloableParams.HOST + "uic/user/sendDoWithdraw.do?";
    private final String doWithdralUrl = GloableParams.HOST + "uic/user/myWallet/doWithdraw.do?";
    private WalletModel model;
    private BankcardModel choosedBankcard;
    //账户可提现的余额
    private double restMoney;
    //可提现的余额（字符串）
    private String restMoneyStr;
    //余额的单位
    private String moneyUnit = "";
    private VerificationCodeDialog verificationCodeDialog;

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CommonRes.BindNewBankcard)) {
                getData();
            }
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("BankCardRechargeActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("BankCardRechargeActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.bank_card_recharge_activity);
        try{
            pageType = getIntent().getExtras().getInt("pageType");
        } catch (Exception e) {
            pageType = 1;
        }
        init();
    }

    private void init(){
        title = (DiDiTitleView) findViewById(R.id.bank_card_recharge_title);
        bankcardImage = (ImageView) findViewById(R.id.bank_card_image);
        bankcardText = (TextView) findViewById(R.id.bank_card_text);
        useAnotherCard = (View) findViewById(R.id.use_another_card);
        inputNameText = (TextView) findViewById(R.id.input_name_text);
        rechargeNumberEditText = (EditText) findViewById(R.id.recharge_number_edittext);
        withdrawHindLayout = (LinearLayout) findViewById(R.id.withdraw_layout);
        restMoneyText = (TextView) findViewById(R.id.account_rest_money_text);
        withdrawAllText = (TextView) findViewById(R.id.withdraw_all_text);
        rechargeButton = (Button) findViewById(R.id.recharge_button);

        title.setBack(BankCardRechargeActivity.this);
        if(pageType == 1) {
            title.setTitle("账户充值");
        }else if (pageType == 2){
            title.setTitle("余额转出");
            inputNameText.setText("转出金额");
            rechargeNumberEditText.setHint("请输入提现金额");
            withdrawHindLayout.setVisibility(View.VISIBLE);
            withdrawAllText.setOnClickListener(this);
            rechargeButton.setText(getResources().getString(R.string.confirm_withdraw));
        }

        useAnotherCard.setOnClickListener(this);

        //钱包充值输入限制，小数点后不能超过两位字符，范围在0——10^6元
        Util.getDoubleInputLimitTextWatcher().setEditText(rechargeNumberEditText);
        Util.getNumericLimitTextWatcher(0, 1000000).setEditText(rechargeNumberEditText);

        rechargeButton.setOnClickListener(this);

        verificationCodeDialog = new VerificationCodeDialog(BankCardRechargeActivity.this, R.style.data_filling_dialog,
                withdralUrl, doWithdralUrl,
                new VerificationCodeDialog.VerificationCodeCallBack() {
                    @Override
                    public void onSucceed() {
                        finish();
                    }
                });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonRes.BindNewBankcard);
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        getData();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.use_another_card:
                if(chooseBankcardDialog != null){
                    chooseBankcardDialog.show();
                }
                break;
            case R.id.recharge_button:
                if(pageType == 1){

                }else if(pageType == 2){
                    if(inputJudge()){
                        if(verificationCodeDialog != null) {
                            AjaxParams paramsGet = new AjaxParams();
                            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                            String sessionID = sp.getString("session_id", "");
                            //加密之前的crc
                            String crc = sessionID;
                            try {
                                //加密过后的crc
                                AES aes = new AES();
                                crc = URLEncoder.encode(aes.encrypt(crc.getBytes()), "utf-8");
                                paramsGet.put("crc", crc);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            AjaxParams paramsSub = new AjaxParams();
                            paramsSub.put("amount", rechargeNumberEditText.getText().toString());
                            paramsSub.put("bindingCardId", choosedBankcard.getId());
                            paramsSub.put("moneyUnitCode", moneyUnit);

                            verificationCodeDialog.setParams(paramsGet, paramsSub);
                            verificationCodeDialog.show();
                        }
                    }
                }
                break;
            case R.id.withdraw_all_text:
                //转出全部
                if(pageType == 2){
                    rechargeNumberEditText.setText(restMoneyStr);
                }
                break;
            default:
                break;
        }
    }

    private void getData(){
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(BankCardRechargeActivity.this, "加载中");
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(BankCardRechargeActivity.this, walletDataUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
            }

            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String all = jsonObject.getString("body");
                    model = JSON.parseObject(all, WalletModel.class);
                    dataFillIn();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void dataFillIn(){
        if(pageType == 2){
            //提现
            restMoney = 0;
            try{
                //key 1基本账户，2保证金，3一卡通，4 运费，5油费，6收益，7信用度， 8水泥预付运费。余额属于基本账户
                MoneyAccountModel moneyAccount = model.getLiabilityAcctMap().get("1");
                //基本账户下面的多个子账户
                List<MoneyChildAccountModel> amountVOList = moneyAccount.getAmountVOList();
                for (int i = 0; i < amountVOList.size(); i++) {
                    if(amountVOList.get(i).getAmountType() == 1){
                        //如果该子账户属于可用金额
                        restMoney += amountVOList.get(i).getAmount();
                    }
                }

                moneyUnit = moneyAccount.getCurrencyEnumCode();
            }catch (Exception e){

            }
//            restMoney = 100;//TODO
            restMoneyStr = Util.formatDoubleToString(restMoney, "元");
            restMoneyText.setText("账户可提现余额¥ " + restMoneyStr + "，");

            if(restMoney <= 0){
                rechargeButton.setEnabled(false);
            }else{
                rechargeButton.setEnabled(true);
            }
        }

        choosedBankcard = model.getBankCardVOList().get(0);
        setBankcardMessage();

        if(model.getBankCardVOList() != null && model.getBankCardVOList().size() > 0) {
            chooseBankcardDialog = new ChooseBankcardDialog(BankCardRechargeActivity.this, R.style.data_filling_dialog,
                    model.getBankCardVOList(),
                    new ChooseBankcardDialog.ChooseBankcardCallBack() {
                        @Override
                        public void getResult(BankcardModel bankcard) {
                            choosedBankcard = bankcard;
                            setBankcardMessage();
                        }
                    });
            chooseBankcardDialog.setCanceledOnTouchOutside(true);
            UploadUtil.setAnimation(chooseBankcardDialog, CommonRes.TYPE_BOTTOM, true);
        }
    }

    private void setBankcardMessage(){//根据选中的银行卡更新银行卡layout
        if(choosedBankcard == null){
            return;
        }
        String cardNumberTail = "";
        try{
            String bankNumber = choosedBankcard.getBankNumber();
            cardNumberTail = bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
        }catch (Exception e){

        }
        switch (choosedBankcard.getBankType()){
            case 1:
                bankcardImage.setImageResource(R.drawable.boc_bank_icon);
                break;
            case 2:
                bankcardImage.setImageResource(R.drawable.icbc_bank_icon);
                break;
            case 4:
                bankcardImage.setImageResource(R.drawable.ccb_bank_icon);
                break;
            case 5:
                bankcardImage.setImageResource(R.drawable.abc_bank_icon);
                break;
            default:
                bankcardImage.setImageResource(R.drawable.other_bank_icon);
                break;
        }
        bankcardText.setText(choosedBankcard.getBankName() + "（" + cardNumberTail + "）");
    }

    private boolean inputJudge(){
        String input;
        try{
           input  = rechargeNumberEditText.getText().toString();
        }catch (Exception e){
            return false;
        }

        double trans = Util.inputToDoubleValue(input);
        if(trans < 0){
            ToastUtil.show(BankCardRechargeActivity.this, "请正确输入提现金额");
            return false;
        }

        if(choosedBankcard == null){
            ToastUtil.show(BankCardRechargeActivity.this, "请选择有效银行卡");
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
