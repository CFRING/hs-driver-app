package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.MyBankcardListAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.BankcardModel;
import com.hongshi.wuliudidi.model.WalletModel;
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
 * @author huiyuan
 */
public class MyBankcardsActivity extends Activity implements View.OnClickListener{
    private DiDiTitleView title;
    private ListView bankcardListView;
    private List<BankcardModel> list = new ArrayList<>();
    private MyBankcardListAdapter adapter;
    private RelativeLayout addBankcardLayout;
    private final String walletDataUrl = GloableParams.HOST + "uic/user/myWallet.do";
    private WalletModel model;
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
        MobclickAgent.onPageEnd("MyBankcardsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("MyBankcardsActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.my_bankcards_activity);
        init();
    }

    private void init(){
        title = (DiDiTitleView) findViewById(R.id.my_bank_card_title);
        bankcardListView = (ListView) findViewById(R.id.bankcard_listview);
        addBankcardLayout = (RelativeLayout) findViewById(R.id.add_bankcard_layout);

        title.setBack(MyBankcardsActivity.this);
        title.setTitle("我的银行卡");

        bankcardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyBankcardsActivity.this, BankcardDetailActivity.class);
                intent.putExtra("bankcardModel", list.get(i));
                startActivity(intent);
            }
        });

        addBankcardLayout.setOnClickListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonRes.BindNewBankcard);
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_bankcard_layout:
                Intent intent = new Intent(MyBankcardsActivity.this, BindNewBankcardActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getData(){
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(MyBankcardsActivity.this, "加载中");
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(MyBankcardsActivity.this, walletDataUrl, params, new ChildAfinalHttpCallBack() {

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
        if(model == null){
            return;
        }
        list = model.getBankCardVOList();
//        for test
//        list = new ArrayList<>();
//
//        BankcardModel bankcardModel = new BankcardModel();
//        bankcardModel.setBankName("花旗银行");
//        bankcardModel.setBankNumber("00001234");
//        bankcardModel.setBankType(1);
//        list.add(bankcardModel);
//
//        bankcardModel = new BankcardModel();
//        bankcardModel.setBankName("花旗银行");
//        bankcardModel.setBankNumber("08651234");
//        bankcardModel.setBankType(2);
//        list.add(bankcardModel);
//
//        bankcardModel = new BankcardModel();
//        bankcardModel.setBankName("花旗银行");
//        bankcardModel.setBankNumber("67801234");
//        bankcardModel.setBankType(3);
//        list.add(bankcardModel);
//
//        bankcardModel = new BankcardModel();
//        bankcardModel.setBankName("花旗银行");
//        bankcardModel.setBankNumber("01253748");
//        bankcardModel.setBankType(4);
//        list.add(bankcardModel);
//
//        bankcardModel = new BankcardModel();
//        bankcardModel.setBankName("花旗银行");
//        bankcardModel.setBankNumber("01112349");
//        bankcardModel.setBankType(5);
//        list.add(bankcardModel);
        if(list == null){
            list = new ArrayList<>();
        }
        adapter = new MyBankcardListAdapter(MyBankcardsActivity.this, list);
        bankcardListView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
