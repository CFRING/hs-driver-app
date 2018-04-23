package com.hongshi.wuliudidi.incomebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class IncomeActivity extends Activity{

    private DiDiTitleView mTitle;
    private TextView mZshouru,mYjiesuan,mWjiesuan,mRecordNumber,
            mGongzi,mYoufei,mLuntaifei,mZulinfei,mTongxingfei,mYunfei,laowufei,
            electronic_bill,qita;
    private RelativeLayout mRecordLayout;
    private String yunfei_url = "https://cz.redlion56.com" + "/gwcz/" + "carrier/app/bill/incomeBooks.do";
    private String sessionID = "";

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd("IncomeActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart("IncomeActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String,String> params = (HashMap)getIntent().getExtras().get("params");
        sessionID = params.get("sessionId");
        //for test 暂时写死的sessionId
//        sessionID = "20170303fOS3degYpvQeYHh";
        setContentView(R.layout.income_activity);
        mTitle = (DiDiTitleView) findViewById(R.id.income_title);
        mTitle.setTitle(getString(R.string.app_name));
        mTitle.setBack(this);

        electronic_bill = (TextView) findViewById(R.id.electronic_bill);
        mZshouru = (TextView) findViewById(R.id.zongshouru);
        mYjiesuan = (TextView) findViewById(R.id.yijiesuan);
        mWjiesuan = (TextView) findViewById(R.id.weijiesuan);
        mRecordNumber = (TextView) findViewById(R.id.record_number);
        mGongzi = (TextView) findViewById(R.id.gongzi);
        mYoufei = (TextView) findViewById(R.id.youfei);
        mLuntaifei = (TextView) findViewById(R.id.luntaifei);
        mZulinfei = (TextView) findViewById(R.id.zulin_weixiu);
        mTongxingfei = (TextView) findViewById(R.id.tongxingfei);
        mYunfei = (TextView) findViewById(R.id.yunfei);
        laowufei = (TextView) findViewById(R.id.laowufei);
        qita = (TextView) findViewById(R.id.qita);
        mRecordLayout = (RelativeLayout) findViewById(R.id.record_layout);
        mRecordLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeActivity.this,TransitRecordOverviewActivity.class);
                intent.putExtra("sessionId",sessionID);
                startActivity(intent);
            }
        });
        electronic_bill.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        electronic_bill.getPaint().setAntiAlias(true);
        electronic_bill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeActivity.this,ElectronicBillActivity.class);
                intent.putExtra("sessionId",sessionID);
                startActivity(intent);
            }
        });
        getData();
    }
    private void getData() {
        final PromptManager mPromptManager = new PromptManager();
        mPromptManager.showProgressDialog1(IncomeActivity.this, "加载中");
        AjaxParams params = new AjaxParams();
        Util.sessionPost(IncomeActivity.this, yunfei_url, params, new ChildAfinalHttpCallBack() {

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
                    IncomeModel mInconModel = JSON.parseObject(all, IncomeModel.class);
                    initViewData(mInconModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },sessionID);

    }
    private void initViewData(IncomeModel mInconModel) {
		mTitle.setTitle(mInconModel.getThisMonth());
        mZshouru.setText(mInconModel.getSumMoney());
        mYjiesuan.setText(mInconModel.getSellterMoney());
        mWjiesuan.setText(mInconModel.getUnSellterMoney());
        mGongzi.setText(mInconModel.getSalary());
        mYoufei.setText(mInconModel.getOilFee());
        mLuntaifei.setText(mInconModel.getTyreFee());
        mZulinfei.setText(""+mInconModel.getLeaseAndRepair());
        mTongxingfei.setText(mInconModel.getRoadFee());
        mYunfei.setText(mInconModel.getTransitFee());
        mRecordNumber.setText(mInconModel.getTaskCount());
        laowufei.setText(mInconModel.getLaowufei());
        qita.setText("0.00");
    }

}
