package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.RevokeOrderModel;
import com.hongshi.wuliudidi.model.TradeRecord4AppVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.view.ChooseCardView;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

/**
 * @author Created by huiyuan on 2017/9/7.
 */

public class RevokeOilWidthdrawlActivity extends Activity {

    private DiDiTitleView title_view;
    private ChooseCardView card_view;
    private TextView oil_supplier_name,truck_number,settle_company_text,
            ti_xian_money_text,widthdrawl_time_text;
    private Button revoke_submit;
    private TradeRecord4AppVO tradeRecord4AppVO;
    private String revokeUrl = GloableParams.HOST + "carrier/app/acct/cancelDrawOil.do?";
    private String revokeOrderDetailUrl = GloableParams.HOST + "carrier/app/acct/getDrawOilInfo.do?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tradeRecord4AppVO = (TradeRecord4AppVO) getIntent().getSerializableExtra("detail");
        setContentView(R.layout.revoke_oil_widthdrawl_activity);
        initView();
    }

    private void initView(){
        title_view = (DiDiTitleView) findViewById(R.id.title_view);
        card_view = (ChooseCardView) findViewById(R.id.card_view);
        oil_supplier_name = (TextView) findViewById(R.id.oil_supplier_name);
        truck_number = (TextView) findViewById(R.id.truck_number);
        settle_company_text = (TextView) findViewById(R.id.settle_company_text);
        ti_xian_money_text = (TextView) findViewById(R.id.ti_xian_money_text);
        widthdrawl_time_text = (TextView) findViewById(R.id.widthdrawl_time_text);
        revoke_submit = (Button) findViewById(R.id.revoke_submit);

        title_view.setBack(this);
        title_view.setTitle("提油详情");
        card_view.getRight_arrow_image().setVisibility(View.GONE);

        revoke_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeOrder();
            }
        });
        getRevokeOrderDetail();
    }

    private void getRevokeOrderDetail(){
        AjaxParams params = new AjaxParams();
        params.put("tradeID",tradeRecord4AppVO.getTradeId());

        DidiApp.getHttpManager().sessionPost(RevokeOilWidthdrawlActivity.this, revokeOrderDetailUrl, params,
                new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            String body = jsonObject.optString("body");
                            RevokeOrderModel revokeOrderModel = JSON.parseObject(body,RevokeOrderModel.class);

                            oil_supplier_name.setText(revokeOrderModel.getGasolineStation());
                            truck_number.setText(revokeOrderModel.getTruckNum());
                            settle_company_text.setText(revokeOrderModel.getSupplier());
                            ti_xian_money_text.setText(revokeOrderModel.getAmount());
                            widthdrawl_time_text.setText(revokeOrderModel.getGmtCreate());
                            card_view.setOilImg(revokeOrderModel.getSupplierType());

                            String oilName = "";
                            switch (revokeOrderModel.getSupplierType()){
                                case 1:
                                    //中国石油
                                    oilName = "中国石油";
                                    break;
                                case 2:
                                    //中国石化
                                    oilName = "中国石化";
                                    break;
                                case 3:
                                    //中国海油
                                    oilName = "中国海油";
                                    break;
                                default:
                                    //其他
                                    oilName = "其他";
                                    break;
                            }
                            card_view.setOilNameAndCardTxt(oilName,revokeOrderModel.getCardID());
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {

                    }
                });
    }

    private void revokeOrder(){
        AjaxParams params = new AjaxParams();
        params.put("tradeID",tradeRecord4AppVO.getTradeId());

        DidiApp.getHttpManager().sessionPost(RevokeOilWidthdrawlActivity.this, revokeUrl, params,
                new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        Toast.makeText(RevokeOilWidthdrawlActivity.this,"撤销成功!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("com.action.revoke");
                        sendBroadcast(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {

                    }
                });
    }
}
