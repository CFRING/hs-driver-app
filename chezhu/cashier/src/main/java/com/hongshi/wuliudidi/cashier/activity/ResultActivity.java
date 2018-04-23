package com.hongshi.wuliudidi.cashier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongshi.wuliudidi.cashier.R;
import com.hongshi.wuliudidi.cashier.model.TradeResultModel;
import com.hongshi.wuliudidi.cashier.view.JiaoYiTypeView;
import com.hongshi.wuliudidi.cashier.view.SdkTitleView;

import org.json.JSONException;
import org.json.JSONObject;


public class ResultActivity extends Activity {

    private final String TAG = "ResultActivity";

    private SdkTitleView mTitleView;
    private ImageView mResultImg;
    private TextView mResultTxt;
    private JiaoYiTypeView mJiaoYiTypeView;
    private Button mBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdkresult);

        init();
    }

    private void init(){
        mTitleView = (SdkTitleView) findViewById(R.id.result_title);
        mTitleView.setBack(this);
        mTitleView.setTitle(getResources().getString(R.string.my_bank));
        mResultImg = (ImageView) findViewById(R.id.result_image);
        mResultTxt = (TextView) findViewById(R.id.result_text);
        mJiaoYiTypeView = (JiaoYiTypeView) findViewById(R.id.jiao_yi_type_view);
        mBottom = (Button) findViewById(R.id.bottom);
        mBottom.setText(getResources().getString(R.string.ok));
        mBottom.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bottom));
        mBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        TradeResultModel model = new TradeResultModel();
        if (intent.hasExtra("body")) {
            String body = intent.getStringExtra("body");
            try {
                JSONObject jsonObject = new JSONObject(body);
                model.setGmtModified(jsonObject.getLong("gmtModified"));
                model.setGmtCreate(jsonObject.getLong("gmtCreate"));
                model.setStatusFinish(jsonObject.getBoolean("statusFinish"));
                model.setId(jsonObject.getString("id"));
                model.setDesc(jsonObject.getString("desc"));
                model.setUserId(jsonObject.getString("userId"));
                model.setMoney(jsonObject.getString("money"));
                model.setMoneyUnitText(jsonObject.getString("moneyUnitText"));
                model.setTradeName(jsonObject.getString("tradeName"));
                model.setTradeProductCode(jsonObject.getString("tradeProductCode"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mResultTxt.setText(model.getDesc());
        }else if (intent.hasExtra("errMsg")){
            mResultImg.setImageResource(R.drawable.error);
            mResultTxt.setText(intent.getStringExtra("errMsg"));
        }
        mJiaoYiTypeView.showTopLongLine();
        mJiaoYiTypeView.showBottomLongLine();
        mJiaoYiTypeView.setJiaoYiType(intent.getStringExtra("TradeType"));
        mJiaoYiTypeView.setTiXianAccount(intent.getStringExtra("TiXianAccount"));
    }
}
