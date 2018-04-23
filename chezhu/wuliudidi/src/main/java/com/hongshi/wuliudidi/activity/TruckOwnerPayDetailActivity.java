package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.Base64Encoder;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.CancelDialog;
import com.hongshi.wuliudidi.dialog.HintDialog;
import com.hongshi.wuliudidi.dialog.PayTypeChooseDialog;
import com.hongshi.wuliudidi.dialog.PaymentVerifyCodeDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AccountInfo;
import com.hongshi.wuliudidi.model.OrderDetailModel;
import com.hongshi.wuliudidi.model.TradeRecord4AppVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.CircleImageView;
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
 * @author Created by huiyuan on 2017/6/6.
 */

public class TruckOwnerPayDetailActivity extends Activity implements View.OnClickListener {

    private DiDiTitleView title;
    private Button sure_to_pay,cancel_pay;
    private CircleImageView user_head;
    private FinalBitmap mFinalBitmap;
    private TextView user_name,money_num,trade_status,product_text,payment_text,create_time_text,
            trade_code_text,payment_desc_text,supplier_text,account_type,change_account_type,driver_name;
    private RelativeLayout driver_info_container;
    private int selectedPayType = 11;
    private TradeRecord4AppVO tradeRecord4AppVO;
    private PaymentVerifyCodeDialog mVerificationCodeDialog;
    private String mUserId = "";
    private String verifyCode = "";
    private String currentMoney = "0";
    private String account = "消费账户余额";
    private OrderDetailModel orderDetailModel = null;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1009){
                verifyCode = (String) msg.obj;
                toPay();
            }else if(msg.what == 4){
                choosePayType();
            }else {
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
                    account = "消费账户余额";
                    currentMoney = accountInfoList.get(3).getAmount();
                }
                account_type.setText("使用" + account + "付款");
            }
        }
    };

    private void choosePayType(){
        PayTypeChooseDialog dialog = new PayTypeChooseDialog(TruckOwnerPayDetailActivity.this,myHandler,
                selectedPayType,accountInfoList,R.style.data_filling_dialog);
        dialog.setCanceledOnTouchOutside(true);
        UploadUtil.setAnimation(dialog, CommonRes.TYPE_BOTTOM, true);
        dialog.show();
    }

    private void showVerifyDialog(){
        mVerificationCodeDialog = new PaymentVerifyCodeDialog(TruckOwnerPayDetailActivity.this,
                R.style.data_filling_dialog,mUserId,myHandler);
        mVerificationCodeDialog.setCanceledOnTouchOutside(false);
        UploadUtil.setAnimation(mVerificationCodeDialog,CommonRes.TYPE_CENTER,false);
        mVerificationCodeDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck_owner_pay_detail_activity);

        tradeRecord4AppVO = (TradeRecord4AppVO) getIntent().getSerializableExtra("detail");
        title = (DiDiTitleView) findViewById(R.id.title);
        title.setBack(this);
        title.setTitle("车主代支付详情");

        account_type = (TextView) findViewById(R.id.account_type);
        change_account_type = (TextView) findViewById(R.id.change_account_type);
        sure_to_pay = (Button) findViewById(R.id.sure_to_pay);
        cancel_pay = (Button) findViewById(R.id.cancel_pay);
        user_head = (CircleImageView) findViewById(R.id.user_head);
        user_name = (TextView) findViewById(R.id.user_name);
        money_num = (TextView) findViewById(R.id.money_num);
        trade_status = (TextView) findViewById(R.id.trade_status);
        product_text = (TextView) findViewById(R.id.product_text);
        payment_text = (TextView) findViewById(R.id.payment_text);
        create_time_text = (TextView) findViewById(R.id.create_time_text);
        trade_code_text = (TextView) findViewById(R.id.trade_code_text);
        payment_desc_text = (TextView) findViewById(R.id.payment_desc_text);
        supplier_text = (TextView) findViewById(R.id.supplier_text);
        driver_info_container = (RelativeLayout) findViewById(R.id.driver_info_container);
        driver_name = (TextView) findViewById(R.id.driver_name);

        driver_info_container.setVisibility(View.VISIBLE);

        change_account_type.setOnClickListener(this);
        sure_to_pay.setOnClickListener(this);
        cancel_pay.setOnClickListener(this);

        mFinalBitmap = FinalBitmap.create(this);

        mUserId = getSharedPreferences("config",MODE_PRIVATE).getString("userId","");
//        setData();
        getOrderDetail();
        getPayTypes();
    }

    private void setData(){
        try {
            account_type.setText("使用" + account + "付款");
            user_name.setText(orderDetailModel.getSupplierShowName());
            money_num.setText(orderDetailModel.getMoneyStr());
            product_text.setText(orderDetailModel.getProduct());
            payment_text.setText(orderDetailModel.getPayTypeName());
            create_time_text.setText(orderDetailModel.getCreateDate());
            trade_code_text.setText(orderDetailModel.getTradeId());
            payment_desc_text.setText(orderDetailModel.getRemark());
            supplier_text.setText(orderDetailModel.getSupplierShowNickName());
            driver_name.setText(orderDetailModel.getDriverName() + "(" + orderDetailModel.getDriverPhone() + ")");
        }catch (Exception e){

        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head_def_img);
        mFinalBitmap.display(user_head,tradeRecord4AppVO.getStorePhoto(),bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_account_type:
                choosePayType();
                break;
            case R.id.sure_to_pay:
                if (Math.abs(tradeRecord4AppVO.getMoney()) > 0) {
                    boolean isSupport = true;
                    if("消费账户余额".equals(account)){
                        if("1".equals(accountInfoList.get(3).getSupport())){
                            isSupport = true;
                        }else {
                            isSupport = false;
                        }
                    }
                    if(isSupport){
                        if(Double.valueOf(currentMoney) < Math.abs(tradeRecord4AppVO.getMoney())){
                            Toast.makeText(getApplicationContext(), account + "不足,请更换付款账户", Toast.LENGTH_SHORT).show();
                        }else {
                            showVerifyDialog();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "暂不支持" + account + "账户支付，请更换其他账户", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "待支付金额有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_pay:
                showConfirmDialog();
                break;
            default:
                break;
        }
    }


    private final String payment_url = GloableParams.HOST + "carrier/qrcodepay/pay.do";
//        private final String payment_url = "http://192.168.158.125:8081/gwcz/carrier/qrcodepay/pay.do";

    private void toPay(){

        AjaxParams params = new AjaxParams();

        params.put("mobileCode",verifyCode);
        params.put("payeeUid",tradeRecord4AppVO.getPayeeUid());
        params.put("payWay","" + selectedPayType);
        params.put("amount", "" + Math.abs(tradeRecord4AppVO.getMoney()));
        params.put("remark",tradeRecord4AppVO.getRemark());
        params.put("operateType","2");
        params.put("outerId",tradeRecord4AppVO.getId());

        DidiApp.getHttpManager().sessionPost(TruckOwnerPayDetailActivity.this, payment_url, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(TruckOwnerPayDetailActivity.this,errMsg,Toast.LENGTH_LONG).show();
                if("120309".equals(errCode)){
                    CancelDialog mCancelDialog = new CancelDialog(TruckOwnerPayDetailActivity.this, R.style.data_filling_dialog, myHandler);
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
                    Toast.makeText(TruckOwnerPayDetailActivity.this,"支付失败:"+errMsg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void data(String t) {

                try {
                    JSONObject jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");
                    String integral = body.optString("integral");
                    if (integral != null && !"".equals(integral) && Integer.parseInt(integral) > 0){
                        Toast.makeText(TruckOwnerPayDetailActivity.this,"获得" + integral + "个积分",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("earned_integral");
                        sendBroadcast(intent);
                    }else {
                        Toast.makeText(TruckOwnerPayDetailActivity.this,"支付成功!",Toast.LENGTH_LONG).show();
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
                    intent.setAction("com.action.wait_to_pay");
                }
                sendBroadcast(intent);
                finish();
            }
        });
    }

    private List<AccountInfo> accountInfoList = new ArrayList<>();
    private String payTypesUrl = GloableParams.HOST + "carrier/qrcodepay/getPayWay.do";
//        private String payTypesUrl = "http://192.168.158.125:8081/gwcz/carrier/qrcodepay/getPayWay.do";
    private void getPayTypes(){

        AjaxParams params = new AjaxParams();
        params.put("payeeUid",tradeRecord4AppVO.getPayeeUid());
        DidiApp.getHttpManager().sessionPost(TruckOwnerPayDetailActivity.this, payTypesUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(TruckOwnerPayDetailActivity.this,errMsg,Toast.LENGTH_LONG).show();
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
                    currentMoney = accountInfoList.get(3).getAmount();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String cancelPayOrderUrl =  GloableParams.HOST + "carrier/qrcodepay/carrierRefusePay4Driver.do?";
    private void cancelPayOrder(){
        AjaxParams params = new AjaxParams();
        params.put("outerId",tradeRecord4AppVO.getOuterId());
        DidiApp.getHttpManager().sessionPost(TruckOwnerPayDetailActivity.this, cancelPayOrderUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(TruckOwnerPayDetailActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void data(String t) {
                Intent intent = new Intent();
                if(selectedPayType == 1){
                    intent.setAction("com.action.cash");
                }else if(selectedPayType == 6){
                    intent.setAction("com.action.tyre");
                }else if(selectedPayType == 5){
                    intent.setAction("com.action.oil");
                }else if(selectedPayType == 11){
                    intent.setAction("com.action.wait_to_pay");
                }
                sendBroadcast(intent);
                Toast.makeText(TruckOwnerPayDetailActivity.this,"取消成功",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private final String orderDetailUrl = GloableParams.HOST + "carrier/qrcodepay/orderDetail.do?";
    //        private final String orderDetailUrl = "http://192.168.158.33:8080/gwcz/carrier/qrcodepay/orderDetail.do";
    private void getOrderDetail(){
        AjaxParams params = new AjaxParams();
        params.put("orderId",tradeRecord4AppVO.getOuterId());

        DidiApp.getHttpManager().sessionPost(TruckOwnerPayDetailActivity.this, orderDetailUrl, params,
                new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            String body = jsonObject.optString("body");
                            orderDetailModel = JSON.parseObject(body,OrderDetailModel.class);
                            setData();

                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {

                    }
                });
    }

    private void showConfirmDialog(){
        HintDialog cancelDialog = new HintDialog(TruckOwnerPayDetailActivity.this,R.style.data_filling_dialog,
                "您是否要取消支付","否","是",null);

        cancelDialog.getmRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPayOrder();
            }
        });
        cancelDialog.show();
    }
}
