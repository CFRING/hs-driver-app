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
import android.widget.TextView;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.qr.ConfirmGoodsActivity;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/11/20.
 * 我的积分界面
 */

public class MyIntegralActivity extends Activity implements View.OnClickListener{

    private DiDiTitleView my_integral_title;
    private Button go_to_finish1,go_to_finish2,go_to_finish3,go_to_finish4,go_to_finish5;
    private TextView total_integrals,earned_integral_count,integral_details,empty_truck_summary,
            receipt_commit_summary,trade_evaluate_summary,scanning_summary,has_sent_summary;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String integralUrl = GloableParams.HOST + "credit/integral/userIntegral/myIntegral.do";

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("earned_integral".equals(action)){
                getIntegral();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.activity_my_integral);

        initViews();
    }

    private void initViews(){
        my_integral_title = (DiDiTitleView) findViewById(R.id.my_integral_title);
        my_integral_title.setBack(this);
        my_integral_title.setTitle("我的积分");

        go_to_finish1 = (Button) findViewById(R.id.go_to_finish1);
        go_to_finish2 = (Button) findViewById(R.id.go_to_finish2);
        go_to_finish3 = (Button) findViewById(R.id.go_to_finish3);
        go_to_finish4 = (Button) findViewById(R.id.go_to_finish4);
        go_to_finish5 = (Button) findViewById(R.id.go_to_finish5);
        integral_details = (TextView) findViewById(R.id.integral_details);
        total_integrals = (TextView) findViewById(R.id.total_integrals);
        earned_integral_count = (TextView) findViewById(R.id.earned_integral_count);
        empty_truck_summary = (TextView) findViewById(R.id.empty_truck_summary);
        receipt_commit_summary = (TextView) findViewById(R.id.receipt_commit_summary);
        trade_evaluate_summary = (TextView) findViewById(R.id.trade_evaluate_summary);
        scanning_summary = (TextView) findViewById(R.id.scanning_summary);
        has_sent_summary = (TextView) findViewById(R.id.has_sent_summary);

        go_to_finish1.setOnClickListener(this);
        go_to_finish2.setOnClickListener(this);
        go_to_finish3.setOnClickListener(this);
        go_to_finish4.setOnClickListener(this);
        go_to_finish5.setOnClickListener(this);
        integral_details.setOnClickListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("earned_integral");
        registerReceiver(broadcastReceiver,intentFilter);

        getIntegral();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_to_finish1:
                //进入空车上报
                Intent intent1 = new Intent(this,EmptyTruckCommitActivity.class);
                startActivity(intent1);
                break;
            case R.id.go_to_finish2:
                //上传回单
            case R.id.go_to_finish5:
                editor.putString("from_integral","commit_receipt").commit();
                MainActivity.mPager.setCurrentItem(0);
                Intent intent0 = new Intent();
                intent0.setAction("from_task_fragment");
                sendBroadcast(intent0);
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("from","commit_receipt");
                startActivity(intent);
                break;
            case R.id.go_to_finish3:
                //交易评价
                editor.putString("from_integral","evaluation").commit();
                MainActivity.mPager.setCurrentItem(0);
                Intent intentEva = new Intent();
                intentEva.setAction("from_task_fragment");
                sendBroadcast(intentEva);
                Intent intent3 = new Intent(this,MainActivity.class);
                intent3.putExtra("from","evaluation");
                startActivity(intent3);
                break;
            case R.id.go_to_finish4:
                //扫码
                Intent scannerIntent = new Intent(this, ConfirmGoodsActivity.class);
                scannerIntent.putExtra("from","my_integral");
                startActivity(scannerIntent);
                break;
            case R.id.integral_details:
                Intent detailIntent = new Intent(MyIntegralActivity.this,IntegralDetailActivity.class);
                detailIntent.putExtra("integrals",total_integrals.getText().toString());
                startActivity(detailIntent);
                break;
            default:
                break;
        }
    }

    private void getIntegral(){
        AjaxParams params = new AjaxParams();

        DidiApp.getHttpManager().sessionPost(MyIntegralActivity.this, integralUrl,
                params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.optString("body");
                    JSONObject bodyJson = new JSONObject(body);
                    earned_integral_count.setText(bodyJson.optString("todayIntegral"));
                    total_integrals.setText(bodyJson.optString("myIntegral"));
                    empty_truck_summary.setText(bodyJson.optString("REPORT_EMPTY"));
                    receipt_commit_summary.setText(bodyJson.optString("UPLOAD_VOUCHER"));
                    trade_evaluate_summary.setText(bodyJson.optString("EVALUATION"));
                    scanning_summary.setText(bodyJson.optString("QR_CODE_PAY"));
                    has_sent_summary.setText(bodyJson.optString("DELIVERY_PROOF"));
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }
}
