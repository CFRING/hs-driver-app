package com.hongshi.wuliudidi.cashier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.BindNewBankcardActivity;
import com.hongshi.wuliudidi.cashier.okhttp.OkhttpRequest;
import com.hongshi.wuliudidi.cashier.okhttp.callback.RequestCallback;
import com.hongshi.wuliudidi.dialog.CancelDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TiXianActivity extends Activity implements View.OnClickListener {

    private final String TAG = "TiXianActivity";
    private final String TRADE_CODE = "WITHDRAW_FROM_BASE_ACCT_XIAOER";

    private final String url = GloableParams.BASE_URL + "/cashier/app/info/withdraw.do";

    private SdkTitleView mTitleView;
    private EditText mTiXianMoneyEdt;//提现金额输入框
    private TextView mCanTiXianMoney;//可提现余额
    private TextView mTiXianAllTxt;//全部提现
//    private JiaoYiTypeView mJiaoYiTypeView;//交易类型
    private ChooseCardView mChooseCardView;//到账银行卡
    private Button mTiXianBtn;//提现按钮

    private TiXianModel mTiXianModel;
    private String mBindingCardId;//绑卡ID
    private String mUserId = "";
    private VerificationCodeDialog mVerificationCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_xian);
        HashMap<String,String> params = (HashMap)getIntent().getExtras().get("params");
        mUserId = params.get("userId");
        mTitleView = (SdkTitleView) findViewById(R.id.ti_xian_title);
        mTitleView.setBack(this);
        mTitleView.setTitle(getString(R.string.my_bank));
        mTiXianMoneyEdt = (EditText) findViewById(R.id.ti_xian_money_edit);
        mTiXianAllTxt = (TextView) findViewById(R.id.ti_xian_all);
        mTiXianAllTxt.setOnClickListener(this);
        mCanTiXianMoney = (TextView) findViewById(R.id.can_ti_xian_money);
        mChooseCardView = (ChooseCardView) findViewById(R.id.choose_card_view);
        mChooseCardView.setOnClickListener(this);
        mTiXianBtn = (Button) findViewById(R.id.bottom);
        mTiXianBtn.setOnClickListener(this);

        mTiXianMoneyEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //如果第一位为“.”就在最前面加“0”
                if (string.length() > 0) {
                    if (string.trim().substring(0, 1).equals(".")) {
                        string = "0" + string;
                        mTiXianMoneyEdt.setText(string);
                        mTiXianMoneyEdt.setSelection(2);
                    }
                }
                if (!string.equals("") && Double.valueOf(string) != 0) {
                    mTiXianBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bottom));
                } else {
                    mTiXianBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray));
                }
                //限制只能出入两位小数
                if (string.contains(".")) {
                    if (string.length() - 1 - string.indexOf(".") > 2) {
                        string = string.substring(0, string.indexOf(".") + 3);
                        mTiXianMoneyEdt.setText(string);
                        mTiXianMoneyEdt.setSelection(string.length());
                    }
                }
//                如果第一位为“0”且有第二位并且第二位不是“.”，则去掉第一位“0”
                if (string.startsWith("0")
                        && string.trim().length() > 1) {
                    if (!string.substring(1, 2).equals(".")) {
                        mTiXianMoneyEdt.setText(string.substring(1));
                        mTiXianMoneyEdt.setSelection(1);
                    }
                }
            }
        });

        sendHttpRequest();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ti_xian_all) {
            //全部提现
            mTiXianMoneyEdt.setText(mTiXianModel.getAmount());
            mTiXianMoneyEdt.setSelection(mTiXianMoneyEdt.getText().length());
        } else if (i == R.id.choose_card_view) {
            //选择银行卡
            if (mTiXianModel != null) {
                if (mTiXianModel.getBankCardList() != null && mTiXianModel.getBankCardList().size() > 0) {
                    ChooseBankDialog chooseBankDialog = new ChooseBankDialog(TiXianActivity.this, R.style.data_filling_dialog, mTiXianModel.getBankCardList(),
                            new ChooseBankDialog.ChooseDialogCallBack() {
                                @Override
                                public void chooseBank(TiXianModel.BankCard bankCard) {
                                    mChooseCardView.setmYinHangImg(bankCard.getBankType());
                                    mChooseCardView.setmYinHangTxt(bankCard.getBankName(),
                                            "尾号 " + bankCard.getBankNumber());
                                    TiXianActivity.this.mBindingCardId = bankCard.getId();
                                }
                            });
                    chooseBankDialog.setCanceledOnTouchOutside(true);
                    Util.setAnimation(chooseBankDialog, CommonRes.TYPE_BOTTOM, true);
                    chooseBankDialog.show();
                }
            }
        } else if (i == R.id.bottom) {
            //提现按钮
            if (!mTiXianMoneyEdt.getText().toString().equals("")) {
                if (Double.valueOf(mTiXianMoneyEdt.getText().toString()) != 0) {
                    if (Double.valueOf(mTiXianMoneyEdt.getText().toString()) <= Double.valueOf(mTiXianModel.getAmount())) {
                        if (!mUserId.equals("")){
                            mVerificationCodeDialog = new VerificationCodeDialog(TiXianActivity.this, R.style.data_filling_dialog, TRADE_CODE,
                                    mUserId, mTiXianModel.getCellPhone(), mTiXianMoneyEdt.getText().toString(), mBindingCardId, mTiXianModel.getMoneyUnitCode(),
                                    new VerificationCodeDialog.VerificationCodeCallBack() {
                                        @Override
                                        public void onSucceed(String body) {
                                            Intent sucIntent = new Intent("get_cash_data_success");
                                            sendBroadcast(sucIntent);
                                            Intent intent = new Intent(TiXianActivity.this, ResultActivity.class);
                                            intent.putExtra("body", body);
                                            intent.putExtra("TradeType", mTiXianModel.getTradeType());
                                            intent.putExtra("TiXianAccount", mTiXianModel.getWithdrawAcct());
                                            startActivity(intent);
                                            mVerificationCodeDialog.dismiss();
                                            finish();
                                        }
                                        @Override
                                        public void onFail(String errMsg) {
                                            if (!errMsg.contains("request_timeout")){
                                                if (!errMsg.contains(CommonRes.Y_ERRORCODE)) {
                                                    Intent intent = new Intent(TiXianActivity.this, ResultActivity.class);
                                                    intent.putExtra("errMsg", errMsg);
                                                    intent.putExtra("TradeType", mTiXianModel.getTradeType());
                                                    intent.putExtra("TiXianAccount", mTiXianModel.getWithdrawAcct());
                                                    startActivity(intent);
                                                    mVerificationCodeDialog.dismiss();
                                                    finish();
                                                }
                                            }
                                        }
                                    });
                            mVerificationCodeDialog.show();
                        }else{
                            Toast.makeText(getApplicationContext(), getString(R.string.cash_error), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_over), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_zero), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_empty), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendHttpRequest() {
        final PromptManager promptManager = new PromptManager();
        promptManager.showProgressDialog1(TiXianActivity.this, getString(R.string.waiting));
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("tradeCode", TRADE_CODE);
//        Log.e(TAG, url);
        OkhttpRequest.postString(this, url, params, new RequestCallback() {
            @Override
            public void Success(String body) {
                promptManager.closeProgressDialog();
//                Log.e(TAG, "body==" + body);
                mTiXianModel = new TiXianModel();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    mTiXianModel.setAmount(jsonObject.getString("amount"));
                    mTiXianModel.setTradeType(jsonObject.getString("tradeType"));
                    mTiXianModel.setTitle(jsonObject.getString("title"));
                    mTiXianModel.setWithdrawAcct(jsonObject.getString("withdrawAllTitle"));
                    mTiXianModel.setMoneyUnitTxt(jsonObject.getString("moneyUnitTxt"));
                    mTiXianModel.setTotalTitle(jsonObject.getString("totalTitle"));
                    mTiXianModel.setWithdrawAcct(jsonObject.getString("withdrawAcct"));
                    mTiXianModel.setWithdrawAcctTitle(jsonObject.getString("withdrawAcctTitle"));
                    mTiXianModel.setBankTitle(jsonObject.getString("bankTitle"));
                    mTiXianModel.setTradeTypeTitle(jsonObject.getString("tradeTypeTitle"));
                    mTiXianModel.setMoneyUnitCode(jsonObject.getString("moneyUnitCode"));
                    mTiXianModel.setCurrencySymbol(jsonObject.getString("currencySymbol"));
                    mTiXianModel.setCellPhone(jsonObject.getString("cellPhone"));
                    JSONArray jsonArray = jsonObject.getJSONArray("bankCardList");
                    List<TiXianModel.BankCard> bankCardList = new ArrayList<TiXianModel.BankCard>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TiXianModel.BankCard mBankCard = new TiXianModel.BankCard();
                        mBankCard.setId(jsonArray.getJSONObject(i).getString("id"));
                        mBankCard.setBankName(jsonArray.getJSONObject(i).getString("bankName"));
                        mBankCard.setBankCardType(jsonArray.getJSONObject(i).getString("bankCardType"));
                        mBankCard.setBankType(jsonArray.getJSONObject(i).getInt("bankType"));
                        mBankCard.setBankNumber(jsonArray.getJSONObject(i).getString("bankNumber"));
                        bankCardList.add(mBankCard);
                        mTiXianModel.setBankCardList(bankCardList);
                    }
                    mCanTiXianMoney.setText(mTiXianModel.getAmount());
//                    mJiaoYiTypeView.setJiaoYiType(mTiXianModel.getTradeType());
//                    mJiaoYiTypeView.setTiXianAccount(mTiXianModel.getWithdrawAcct());
                    if (mTiXianModel.getBankCardList() != null && mTiXianModel.getBankCardList().size() > 0) {
                        TiXianModel.BankCard bankCard = mTiXianModel.getBankCardList().get(0);
                        mChooseCardView.setmYinHangImg(bankCard.getBankType());
                        mChooseCardView.setmYinHangTxt(bankCard.getBankName(),
                                "尾号 " + bankCard.getBankNumber());
                        mBindingCardId = mTiXianModel.getBankCardList().get(0).getId();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errMsg) {
//                super.onError(errMsg);
                Intent intent = new Intent();
                intent.setAction("add_bank_card");
                sendBroadcast(intent);
                promptManager.closeProgressDialog();
                finish();
            }
        });
    }
}
