package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;

/**
 * @author Created by huiyuan on 2016/8/19.
 */
public class TruckTransitDetailActivity extends Activity {

    private TextView truck_number,goods_name,net_weight_text,full_weight_text,pi_weight_text,
            date_text,bill_code_text,pound_number_text,kuang_dian_text,company_text,gong_ying_shang_text;
    private DiDiTitleView titleView;

    /**
     * 单据号
     */
    private String id = "";
    /**
     * 磅单号
     */
    private String billCode = "";
    /**
     * 重车重量//毛重
     */
    private String fullWeight = "";
    /**
     * 平台货物类型名称
     */
    private String categoryText = "";

    /**
     * 净重
     */
    private String  netWeight = "";

    /**
     * 公司名称
     */
    private String   pkCorpName = "";
    /**
     * 供货方
     */
    private String  supplier = "";
    /**
     * 车牌号
     */
    private String  truckNumber = "";
    /**
     * 重量单位
     */
    private String  weightText = "";
    /**
     * 统计日期
     */
    private String  sumDate = "";
    /**
     * 发货地(矿点)
     */
    private String fromAddr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck_transit_detail_activity);
        Intent intent = getIntent();
        truckNumber = intent.getStringExtra("truckNumber");
        billCode = intent.getStringExtra("billCode");
        fullWeight = intent.getStringExtra("fullWeight");
        categoryText = intent.getStringExtra("categoryText");
        netWeight = intent.getStringExtra("netWeight");
        pkCorpName = intent.getStringExtra("pkCorpName");
        supplier = intent.getStringExtra("supplier");
        weightText = intent.getStringExtra("weightText");
        sumDate = intent.getStringExtra("sumDate");
        fromAddr = intent.getStringExtra("fromAddr");
        id = intent.getStringExtra("id");

        initView();
    }

    private void initView(){
        titleView = (DiDiTitleView) findViewById(R.id.truck_transit_detail_title);
        titleView.setBack(this);
        titleView.setTitle("运输详情");
        truck_number = (TextView) findViewById(R.id.truck_number);
        goods_name = (TextView) findViewById(R.id.goods_name);
        net_weight_text = (TextView) findViewById(R.id.net_weight_text);
        full_weight_text = (TextView) findViewById(R.id.full_weight_text);
        pi_weight_text = (TextView) findViewById(R.id.pi_weight_text);
        date_text = (TextView) findViewById(R.id.date_text);
        bill_code_text = (TextView) findViewById(R.id.bill_code_text);
        pound_number_text = (TextView) findViewById(R.id.pound_number_text);
        kuang_dian_text = (TextView) findViewById(R.id.kuang_dian_text);
        company_text = (TextView) findViewById(R.id.company_text);
        gong_ying_shang_text = (TextView) findViewById(R.id.gong_ying_shang_text);

        setData();
    }

    private void setData(){
        truck_number.setText(truckNumber);
        goods_name.setText(categoryText);
        net_weight_text.setText(Util.formatDoubleToString(Double.valueOf(netWeight),weightText) + weightText);
        full_weight_text.setText( Util.formatDoubleToString(Double.valueOf(fullWeight),weightText) + weightText);
        pi_weight_text.setText(Util.formatDoubleToString((Double.valueOf(fullWeight) - Double.valueOf(netWeight)),"吨") + weightText);
        date_text.setText(sumDate);
        bill_code_text.setText(id);
        pound_number_text.setText(billCode);
        kuang_dian_text.setText(fromAddr);
        company_text.setText(pkCorpName);
        gong_ying_shang_text.setText(supplier);
    }
}
