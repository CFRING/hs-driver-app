package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.MyBankcardListAdapter;
import com.hongshi.wuliudidi.adapter.OilCardsAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.OilCardModel;
import com.hongshi.wuliudidi.model.OilModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by huiyuan on 2017/8/12.
 */

public class OilCardsActivity extends Activity {
    private DiDiTitleView title;
    private ListView oil_card_listview;
    private List<OilCardModel> list;
    private OilCardsAdapter adapter;
    private final String oilCardsDataUrl = GloableParams.HOST + "carrier/app/acct/fetchOilCards.do";

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("OilCardsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("OilCardsActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.oil_cards_activity);
        init();
    }

    private void init(){
        title = (DiDiTitleView) findViewById(R.id.oil_card_title);
        oil_card_listview = (ListView) findViewById(R.id.oil_card_listview);

        title.setBack(this);
        title.setTitle("我的油卡");

        getData();
    }


    private void getData(){
        final PromptManager mPromptManager = new PromptManager();
        mPromptManager.showProgressDialog1(OilCardsActivity.this, "加载中");
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(OilCardsActivity.this, oilCardsDataUrl, params, new ChildAfinalHttpCallBack() {

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
                    OilModel oilModel = JSON.parseObject(all, OilModel.class);
                    dataFillIn(oilModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void dataFillIn(OilModel model){
        if(model == null){
            return;
        }
        list = model.getCardInfoList();
//        for test
//        list = new ArrayList<>();
//
//        OilCardModel bankcardModel = new OilCardModel();
//        bankcardModel.setCardID("29385098545");
//        bankcardModel.setSupplierType(1);
//        bankcardModel.setTruckNum("浙A 12345");
//        list.add(bankcardModel);
//
//        bankcardModel = new OilCardModel();
//        bankcardModel.setCardID("324859869");
//        bankcardModel.setSupplierType(2);
//        bankcardModel.setTruckNum("浙B 12345");
//        list.add(bankcardModel);
//
//        bankcardModel = new OilCardModel();
//        bankcardModel.setCardID("3485901845");
//        bankcardModel.setSupplierType(3);
//        bankcardModel.setTruckNum("浙C 12345");
//        list.add(bankcardModel);
//
//        bankcardModel = new OilCardModel();
//        bankcardModel.setCardID("348509845");
//        bankcardModel.setSupplierType(4);
//        bankcardModel.setTruckNum("浙D 12345");
//        list.add(bankcardModel);

        if(list == null){
            list = new ArrayList<>();
        }

        List<OilCardModel> tmpList = new ArrayList<>();
        int size = list.size();
        for(int i = 0; i < size; i++){
            OilCardModel oilCardModel = list.get(i);
            if(oilCardModel.getCardID() != null && !"".equals(oilCardModel.getCardID())){
                tmpList.add(oilCardModel);
            }
        }
        adapter = new OilCardsAdapter(OilCardsActivity.this,tmpList);
        oil_card_listview.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
