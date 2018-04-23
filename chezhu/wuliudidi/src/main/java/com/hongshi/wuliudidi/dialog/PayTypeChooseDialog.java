package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.Base64Encoder;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.PaymentDetailActivity;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AccountInfo;
import com.hongshi.wuliudidi.params.GloableParams;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huiyuan on 2017/6/6.
 */

public class PayTypeChooseDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ImageView back,cash_icon,tyre_icon,oil_icon,consume_icon,selected0,selected1,selected2,selected3;
    private TextView cash_can_use,tyre_can_use,oil_can_use,consume_can_use,cash_tip,tyre_tip,oil_tip,consume_tip;
    private RelativeLayout cash_account,tyre_account,oil_account,consume_account;

    private Handler handler;
    private int selectedPayType = 1;
    private List<AccountInfo> accountInfoList = new ArrayList<>();

    public PayTypeChooseDialog(Context context, Handler handler,
                               int selectedPayType,List<AccountInfo> list,
                               int theme){
        super(context,theme);
        this.mContext = context;
        this.handler = handler;
        this.selectedPayType = selectedPayType;
        this.accountInfoList = list;
        initView();
    }

    private void initView(){
        setContentView(R.layout.pay_type_choose_dialog);

        back = (ImageView) findViewById(R.id.back);
        cash_icon = (ImageView) findViewById(R.id.cash_icon);
        tyre_icon = (ImageView) findViewById(R.id.tyre_icon);
        oil_icon = (ImageView) findViewById(R.id.oil_icon);
        consume_icon = (ImageView) findViewById(R.id.consume_icon);
        selected0 = (ImageView) findViewById(R.id.selected0);
        selected1 = (ImageView) findViewById(R.id.selected1);
        selected2 = (ImageView) findViewById(R.id.selected2);
        selected3 = (ImageView) findViewById(R.id.selected3);
        cash_can_use = (TextView) findViewById(R.id.cash_can_use);
        tyre_can_use = (TextView) findViewById(R.id.tyre_can_use);
        oil_can_use = (TextView) findViewById(R.id.oil_can_use);
        consume_can_use = (TextView) findViewById(R.id.consume_can_use);
        cash_tip = (TextView) findViewById(R.id.cash_tip);
        tyre_tip = (TextView) findViewById(R.id.tyre_tip);
        oil_tip = (TextView) findViewById(R.id.oil_tip);
        consume_tip = (TextView) findViewById(R.id.consume_tip);

        cash_account = (RelativeLayout) findViewById(R.id.cash_account);
        tyre_account = (RelativeLayout) findViewById(R.id.tyre_account);
        oil_account = (RelativeLayout) findViewById(R.id.oil_account);
        consume_account = (RelativeLayout) findViewById(R.id.consume_account);

        back.setOnClickListener(this);
        cash_account.setOnClickListener(this);
        tyre_account.setOnClickListener(this);
        oil_account.setOnClickListener(this);
        consume_account.setOnClickListener(this);


//        for(AccountInfo accountInfo : accountInfoList){
//            Log.d("huiyuan",accountInfo.getName() + " " + accountInfo.getAmount() + " " + accountInfo.getSupport());
//        }

        if(accountInfoList.size() <= 0) {
            getPayTypes();
        }else {
            initStatus();
        }

    }

    private void initStatus(){
        if(selectedPayType == 1){
            selected0.setVisibility(View.VISIBLE);
            selected1.setVisibility(View.GONE);
            selected2.setVisibility(View.GONE);
            selected3.setVisibility(View.GONE);
        }
        if(selectedPayType == 6){
            selected0.setVisibility(View.GONE);
            selected1.setVisibility(View.VISIBLE);
            selected2.setVisibility(View.GONE);
            selected3.setVisibility(View.GONE);
        }
        if(selectedPayType == 5){
            selected0.setVisibility(View.GONE);
            selected1.setVisibility(View.GONE);
            selected2.setVisibility(View.VISIBLE);
            selected3.setVisibility(View.GONE);
        }

        if(selectedPayType == 11){
            selected0.setVisibility(View.GONE);
            selected1.setVisibility(View.GONE);
            selected2.setVisibility(View.GONE);
            selected3.setVisibility(View.VISIBLE);
        }

        cash_can_use.setText("可用余额" + accountInfoList.get(0).getAmount()+"元");
        tyre_can_use.setText("可用余额" + accountInfoList.get(1).getAmount()+"元");
        oil_can_use.setText("可用余额" + accountInfoList.get(2).getAmount()+"元");
        consume_can_use.setText("可用余额" + accountInfoList.get(3).getAmount()+"元");

        if(selectedPayType != 1 && selectedPayType != 6 && selectedPayType != 5){
            selected0.setVisibility(View.GONE);
            selected1.setVisibility(View.GONE);
            selected2.setVisibility(View.GONE);
        }

        if("0".equals(accountInfoList.get(0).getSupport())){
            cash_icon.setImageResource(R.drawable.cash_icon_1);
            cash_can_use.setText("该付款方式不支持当前交易");
            cash_tip.setTextColor(mContext.getResources().getColor(R.color.light_grey));
            cash_account.setClickable(false);
            selected0.setVisibility(View.GONE);
        }
        if("0".equals(accountInfoList.get(1).getSupport())){
            tyre_icon.setImageResource(R.drawable.tyre_icon_1);
            tyre_can_use.setText("该付款方式不支持当前交易");
            tyre_tip.setTextColor(mContext.getResources().getColor(R.color.light_grey));
            tyre_account.setClickable(false);
            selected1.setVisibility(View.GONE);
        }
        if("0".equals(accountInfoList.get(2).getSupport())){
            oil_icon.setImageResource(R.drawable.oil_icon_1);
            oil_can_use.setText("该付款方式不支持当前交易");
            oil_tip.setTextColor(mContext.getResources().getColor(R.color.light_grey));
            oil_account.setClickable(false);
            selected2.setVisibility(View.GONE);
        }

        if("0".equals(accountInfoList.get(3).getSupport())){
            consume_icon.setImageResource(R.drawable.consumption_1);
            consume_can_use.setText("该付款方式不支持当前交易");
            consume_tip.setTextColor(mContext.getResources().getColor(R.color.light_grey));
            consume_account.setClickable(false);
            selected3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                dismiss();
                break;
            case R.id.cash_account:
                selected0.setVisibility(View.VISIBLE);
                selected1.setVisibility(View.GONE);
                selected2.setVisibility(View.GONE);
                dismiss();
                handler.sendEmptyMessage(1);
                break;
            case R.id.tyre_account:
                selected0.setVisibility(View.GONE);
                selected1.setVisibility(View.VISIBLE);
                selected2.setVisibility(View.GONE);
                dismiss();
                handler.sendEmptyMessage(6);
                break;
            case R.id.oil_account:
                selected0.setVisibility(View.GONE);
                selected1.setVisibility(View.GONE);
                selected2.setVisibility(View.VISIBLE);
                dismiss();
                handler.sendEmptyMessage(5);
                break;
            case R.id.consume_account:
                selected0.setVisibility(View.GONE);
                selected1.setVisibility(View.GONE);
                selected2.setVisibility(View.GONE);
                selected3.setVisibility(View.GONE);
                dismiss();
                handler.sendEmptyMessage(11);
                break;
            default:
                break;
        }
    }

        private String payTypesUrl = GloableParams.HOST + "openApi/qrCodePay/getPayWay.do?";
//    private String payTypesUrl = "http://192.168.158.125:8081/gwcz/openApi/qrCodePay/getPayWay.do?";
    private void getPayTypes(){

        AjaxParams params = new AjaxParams();
        Map map = new HashMap();
        String mUserId = mContext.getSharedPreferences("config",Context.MODE_PRIVATE).getString("userId","");
        map.put("userId",mUserId);

        try {
            params.put("param_str", Base64Encoder.encode(URLEncoder.encode(JSON.toJSONString(map),"UTF8")));
        }catch (Exception e){
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }
        DidiApp.getHttpManager().sessionPost(mContext, payTypesUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(mContext,errMsg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    org.json.JSONArray jsonArray = jsonObject.optJSONArray("body");
                    int size = jsonArray.length();
                    if(size <= 0){
                        Toast.makeText(mContext,"获取账户信息失败!",Toast.LENGTH_LONG).show();
                    }else {
                        for(int i = 0; i < size; i++){
                            AccountInfo info = new AccountInfo();
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            info.setAmount(jsonObject1.optString("amount"));
                            info.setName(jsonObject1.optString("name"));
                            info.setSupport(jsonObject1.optString("support"));
                            accountInfoList.add(info);
                        }
                        initStatus();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
