package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongshi.wuliudidi.Base64Encoder;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.CancelDialog;
import com.hongshi.wuliudidi.dialog.PayTypeChooseDialog;
import com.hongshi.wuliudidi.dialog.PaymentVerifyCodeDialog;
import com.hongshi.wuliudidi.dialog.VerificationCodeDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AccountInfo;
import com.hongshi.wuliudidi.model.SupplierUserDetailVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by huiyuan on 2017/6/5.
 */

public class PaymentDetailActivity extends Activity {

    private ImageView head_image,money_edit_delete;
    private TextView payee_name,phone_number,account_type;
    private EditText ti_xian_money_edit,tips_edit;
    private Button sure_to_pay;
    private RelativeLayout change_account_type_container;
    private FinalBitmap mFinalBitmap;
    private DiDiTitleView payment_detail_title;
    private int selectedPayType = 11;
    private PaymentVerifyCodeDialog mVerificationCodeDialog;
    private String mUserId = "";
    private String payeeUid = "";
    private String verifyCode = "";
    private String currentMoney = "0";

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1009){
                verifyCode = (String) msg.obj;
                toPay();
            }else if(msg.what == 4){
                choosePayType();
            }else {
                String account = "";
                selectedPayType = msg.what;
                if(selectedPayType == 1){
                    account = "现金余额";
                    currentMoney = accountInfoList.get(0).getAmount();
                }else if(selectedPayType == 6){
                    account = "轮胎余额";
                    currentMoney = accountInfoList.get(1).getAmount();
                }else if(selectedPayType == 5){
                    account = "油卡余额";
                    currentMoney = accountInfoList.get(2).getAmount();
                }else if(selectedPayType == 11){
                    account = "消费账户";
                    currentMoney = accountInfoList.get(3).getAmount();
                }
                account_type.setText(account);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mUserId = intent.getStringExtra("userId");
        setContentView(R.layout.payment_detail_activity);

        initViews();
        getUserInfo(payeeUid);
        getPayTypes();
    }

    private void initViews(){
        Intent intent = getIntent();
        mUserId = getSharedPreferences("config",MODE_PRIVATE).getString("userId","");
        payeeUid = intent.getStringExtra("payeeUid");
        String fromType = intent.getStringExtra("from");
        payment_detail_title = (DiDiTitleView) findViewById(R.id.payment_detail_title);
        head_image = (ImageView) findViewById(R.id.head_image);
        money_edit_delete = (ImageView) findViewById(R.id.money_edit_delete);
        payee_name = (TextView) findViewById(R.id.payee_name);
        phone_number = (TextView) findViewById(R.id.phone_number);
        account_type = (TextView) findViewById(R.id.account_type);
        change_account_type_container = (RelativeLayout) findViewById(R.id.change_account_type_container);
        ti_xian_money_edit = (EditText) findViewById(R.id.ti_xian_money_edit);
        tips_edit = (EditText) findViewById(R.id.tips_edit);
        sure_to_pay = (Button) findViewById(R.id.sure_to_pay);

        payment_detail_title.setBack(this);
        payment_detail_title.setTitle("支付详情");

        if(fromType != null && !"".equals(fromType)){
            if("oil_activity".equals(fromType)){
                selectedPayType = 5;
            }else if("tyre_activity".equals(fromType)){
                selectedPayType = 6;
            }else if("my_integral".equals(fromType) || "consume_account_activity".equals(fromType)){
                selectedPayType = 11;
            }else if("cash_activity".equals(fromType)){
                selectedPayType = 1;
            }
        }

        String account = "";
        if(selectedPayType == 1){
            account = "现金余额";
        }else if(selectedPayType == 6){
            account = "轮胎余额";
        }else if(selectedPayType == 5){
            account = "油卡余额";
        }else if(selectedPayType == 11){
            account = "消费账户";
        }
        account_type.setText(account);

        ti_xian_money_edit.addTextChangedListener(new TextWatcher() {
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
                        ti_xian_money_edit.setText(string);
                        ti_xian_money_edit.setSelection(2);
                    }
                }
                if (!string.equals("") && Double.valueOf(string) != 0) {
//                    sure_to_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bottom));
                    sure_to_pay.setEnabled(true);
                } else {
//                    sure_to_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray));
                    sure_to_pay.setEnabled(false);
                }
                //限制只能出入两位小数
                if (string.contains(".")) {
                    if (string.length() - 1 - string.indexOf(".") > 2) {
                        string = string.substring(0, string.indexOf(".") + 3);
                        ti_xian_money_edit.setText(string);
                        ti_xian_money_edit.setSelection(string.length());
                    }
                }
//                如果第一位为“0”且有第二位并且第二位不是“.”，则去掉第一位“0”
                if (string.startsWith("0")
                        && string.trim().length() > 1) {
                    if (!string.substring(1, 2).equals(".")) {
                        ti_xian_money_edit.setText(string.substring(1));
                        ti_xian_money_edit.setSelection(1);
                    }
                }
            }
        });
        change_account_type_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePayType();
            }
        });

        sure_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ti_xian_money_edit.getText().toString().equals("")) {
                    boolean isPayWaySupport = true;
                    switch (selectedPayType){
                        case 1:
                            if("1".equals(accountInfoList.get(0).getSupport())){
                                isPayWaySupport = true;
                            }else {
                                isPayWaySupport = false;
                            }
                            break;
                        case 6:
                            if("1".equals(accountInfoList.get(1).getSupport())){
                                isPayWaySupport = true;
                            }else {
                                isPayWaySupport = false;
                            }
                            break;
                        case 5:
                            if("1".equals(accountInfoList.get(2).getSupport())){
                                isPayWaySupport = true;
                            }else {
                                isPayWaySupport = false;
                            }
                            break;
                        case 11:
                            if("1".equals(accountInfoList.get(3).getSupport())){
                                isPayWaySupport = true;
                            }else {
                                isPayWaySupport = false;
                            }
                            break;
                        default:
                            break;
                    }
                    if(isPayWaySupport){
                        double value = Double.valueOf(ti_xian_money_edit.getText().toString());
                        if ( value > 0) {
                            if(value > Double.valueOf(currentMoney)){
                                Toast.makeText(getApplicationContext(), "填写的金额超出可用余额", Toast.LENGTH_SHORT).show();
                            }else {
                                showVerifyDialog();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "请输入正确金额", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "该付款方式不支持当前交易,请更换付款方式", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        money_edit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ti_xian_money_edit.setText("");
            }
        });
        mFinalBitmap = FinalBitmap.create(this);
    }

    private List<AccountInfo> accountInfoList = new ArrayList<>();
    private String payTypesUrl = GloableParams.HOST + "carrier/qrcodepay/getPayWay.do";
//    private String payTypesUrl = "http://192.168.158.96:8080/gwsj/carrier/qrcodepay/getPayWay.do";
    private void getPayTypes(){

        AjaxParams params = new AjaxParams();
        params.put("payeeUid",payeeUid);
        DidiApp.getHttpManager().sessionPost(PaymentDetailActivity.this, payTypesUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(PaymentDetailActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    org.json.JSONArray jsonArray = jsonObject.optJSONArray("body");
                    int size = jsonArray.length();
                    for(int i = 0; i < size; i++){
                        AccountInfo info = new AccountInfo();
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        info.setAmount(jsonObject1.optString("amount"));
                        info.setName(jsonObject1.optString("name"));
                        info.setSupport(jsonObject1.optString("support"));
                        accountInfoList.add(info);
                    }
                    switch (selectedPayType){
                        case 1:
                            currentMoney = accountInfoList.get(0).getAmount();
                            break;
                        case 6:
                            currentMoney = accountInfoList.get(1).getAmount();
                            break;
                        case 5:
                            currentMoney = accountInfoList.get(2).getAmount();
                            break;
                        case 11:
                            currentMoney = accountInfoList.get(3).getAmount();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

     String userInfoUrl = GloableParams.HOST + "thirdpt/supplier/fetchSupplierBaseInfo.do";
//    String userInfoUrl = "http://cz.redlion56.com/gwcz/thirdpt/supplier/fetchSupplierBaseInfo.do";
    private void getUserInfo(String cifUserId){
        AjaxParams params = new AjaxParams();
        params.put("cifUserId",payeeUid);

        DidiApp.getHttpManager().sessionPost(PaymentDetailActivity.this, userInfoUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(PaymentDetailActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String all = jsonObject.getString("body");
                    SupplierUserDetailVO supplierUserDetailVO = JSON.parseObject(all,SupplierUserDetailVO.class);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.default_photo);
                    mFinalBitmap.display(head_image,supplierUserDetailVO.getStorePhoto(),bitmap);
                    payee_name.setText(supplierUserDetailVO.getEnterpriseName());
                    phone_number.setText(supplierUserDetailVO.getCellphone());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showVerifyDialog(){
        mVerificationCodeDialog = new PaymentVerifyCodeDialog(PaymentDetailActivity.this,
                R.style.data_filling_dialog,mUserId,myHandler);
        mVerificationCodeDialog.setCanceledOnTouchOutside(false);
        UploadUtil.setAnimation(mVerificationCodeDialog,CommonRes.TYPE_CENTER,false);
        mVerificationCodeDialog.show();
    }

    private final String payment_url = GloableParams.HOST + "carrier/qrcodepay/pay.do";
//    private final String payment_url = "http://192.168.158.96:8080/gwsj/carrier/qrcodepay/pay.do";
    private void toPay(){
        AjaxParams params = new AjaxParams();

        params.put("mobileCode",verifyCode);
        params.put("payeeUid",payeeUid);
        params.put("payWay","" +selectedPayType);
        params.put("amount", ti_xian_money_edit.getText().toString());
        params.put("remark",tips_edit.getText().toString());

        DidiApp.getHttpManager().sessionPost(PaymentDetailActivity.this, payment_url, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
//                Toast.makeText(PaymentDetailActivity.this,errMsg,Toast.LENGTH_LONG).show();
                if("120309".equals(errCode)){
                    CancelDialog mCancelDialog = new CancelDialog(PaymentDetailActivity.this, R.style.data_filling_dialog, myHandler);
                    mCancelDialog.setCanceledOnTouchOutside(true);
                    if(selectedPayType == 1){
                        mCancelDialog.setHint("现金账户余额不足" + "(剩余" + errMsg + "元)");
                    }else if(selectedPayType == 6){
                        mCancelDialog.setHint("轮胎账户余额不足" + "(剩余" + errMsg + "元)");
                    }else if(selectedPayType == 5){
                        mCancelDialog.setHint("油卡账户余额不足" + "(剩余" + errMsg + "元)");
                    }else if(selectedPayType == 11){
                        mCancelDialog.setHint("消费账户余额不足" + "(剩余" + errMsg + "元)");
                    }
                    mCancelDialog.setRightText("其他付款方式");
                    mCancelDialog.setMsgCode(4);
                    mCancelDialog.show();
                }else {
                    Toast.makeText(PaymentDetailActivity.this,"支付失败:"+errMsg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");
                    String integral = body.optString("integral");
                    if (integral != null && !"".equals(integral) && Integer.parseInt(integral) > 0){
                        Toast.makeText(PaymentDetailActivity.this,"获得" + integral + "个积分",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("earned_integral");
                        sendBroadcast(intent);
                    }else {
                        Toast.makeText(PaymentDetailActivity.this,"支付成功!",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){

                }

                Intent intent = new Intent();
                if(selectedPayType == 1){
                    intent.setAction("com.action.cash");
                }else if(selectedPayType == 6){
                    intent.setAction("com.action.tyre");
                }else if(selectedPayType == 5){
                    intent.setAction("com.action.oil");
                }else if(selectedPayType == 11){
                    intent.setAction("consume_account_activity");
                }
                sendBroadcast(intent);
                finish();
            }
        });
    }

    private void choosePayType(){
        PayTypeChooseDialog dialog = new PayTypeChooseDialog(PaymentDetailActivity.this,myHandler,
                selectedPayType,accountInfoList,R.style.data_filling_dialog);
        dialog.setCanceledOnTouchOutside(true);
        UploadUtil.setAnimation(dialog, CommonRes.TYPE_BOTTOM, true);
        dialog.show();
    }


}
