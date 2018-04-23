package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.EmptyTruckModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.view.CircleImageView;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/10/19.
 */

public class EmptyTruckCommitActivity extends Activity {

    private DiDiTitleView empty_truck_title;
    private CircleImageView driver_user_head;
    private FinalBitmap mFinalBitmap;
    private TextView driver_name,latest_trade_time,trucks_number,traded_number,
            location_text,latest_commit_time;
    private EditText input_content;
    private Button submit;

    private String user_face_url = "";
    private String user_name = "";
    private String current_location = "";
    private String lat = "",lng = "";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        user_face_url = sharedPreferences.getString("userFace","");
        user_name = sharedPreferences.getString("name","");
        current_location = sharedPreferences.getString("current_location","");
        lat = sharedPreferences.getString("latitude","0");
        lng = sharedPreferences.getString("longitude","0");
        setContentView(R.layout.empty_truck_commit_activity);

        initView();
    }

    private void initView(){
        mFinalBitmap = FinalBitmap.create(this);
        empty_truck_title = (DiDiTitleView) findViewById(R.id.empty_truck_title);
        empty_truck_title.setBack(this);
        empty_truck_title.setTitle("空车找货");

        driver_user_head = (CircleImageView) findViewById(R.id.driver_user_head);
        driver_name = (TextView) findViewById(R.id.driver_name);
        latest_trade_time = (TextView) findViewById(R.id.latest_trade_time);
        trucks_number = (TextView) findViewById(R.id.trucks_number);
        traded_number = (TextView) findViewById(R.id.traded_number);
        location_text = (TextView) findViewById(R.id.location_text);
        latest_commit_time = (TextView) findViewById(R.id.latest_commit_time);
        input_content = (EditText) findViewById(R.id.input_content);
        submit = (Button) findViewById(R.id.submit);

        if(user_face_url != null && !user_face_url.equals("")){
            mFinalBitmap.display(driver_user_head, user_face_url);
        }else{
            driver_user_head.setImageResource(R.drawable.default_photo);
        }

        driver_name.setText(user_name + "  " +
                getSharedPreferences("config",Context.MODE_PRIVATE).getString("cellphone",""));
        if(current_location == null){
            location_text.setText("");
        }else {location_text.setText(current_location);}

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitStatus();
            }
        });

        getData();
    }

    private String url = GloableParams.HOST + "carrier/reportEmpty/doReport.do?";
    private void submitStatus(){
        AjaxParams params = new AjaxParams();
        params.put("lat",lat);
        params.put("lng",lng);
        params.put("goodsRequire",input_content.getText().toString());

        DidiApp.getHttpManager().sessionPost(this, url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String integral = jsonObject.optString("body");
                    if (integral != null && !"".equals(integral) && Integer.parseInt(integral) > 0){
                        Toast.makeText(EmptyTruckCommitActivity.this,"获得" + integral + "个积分",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("earned_integral");
                        sendBroadcast(intent);
                    }else {
                        Toast.makeText(EmptyTruckCommitActivity.this,"空车上报成功!",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){

                }
                getData();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(EmptyTruckCommitActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }
        });
    }

    private String data_url = GloableParams.HOST + "carrier/reportEmpty/toReport.do";
    private void getData(){
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(this, data_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.optString("body");
                    EmptyTruckModel emptyTruckModel = JSON.parseObject(body,EmptyTruckModel.class);
                    if(emptyTruckModel != null){
                        if(emptyTruckModel.getLastestGmtFinished() != null){
                            latest_trade_time.setText("最近一次完成交易 " + emptyTruckModel.getLastestGmtFinished());
                        }else {latest_trade_time.setText("最近一次完成交易 ");}
                        trucks_number.setText(emptyTruckModel.getTruckNum());
                        traded_number.setText(emptyTruckModel.getFinishedDealNum());
                        if(emptyTruckModel.getLastReportDate() != null){
                            latest_commit_time.setText("上次空车找货时间:" + emptyTruckModel.getLastReportDate());
                        }else {latest_commit_time.setText("");}
                    }

                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(EmptyTruckCommitActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }
        });
    }
}
