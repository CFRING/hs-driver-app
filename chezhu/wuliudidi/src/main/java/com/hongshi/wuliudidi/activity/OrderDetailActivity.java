package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.OrderDetailModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.CircleImageView;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

/**
 * @author Created by huiyuan on 2017/6/6.
 */

public class OrderDetailActivity extends Activity implements View.OnClickListener{

    private DiDiTitleView order_detail_title;
    private CircleImageView user_head;
    private FinalBitmap mFinalBitmap;
    private TextView user_name,money_num,trade_status,product_text,payment_text,create_time_text,
            trade_code_text,payment_desc_text,supplier_text,stars_count,driver_name;
    private RelativeLayout evaluate_entry_container,contact_supplier_container,supplier_container,
            driver_info_container;
    private OrderDetailModel orderDetailModel = null;
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderId = getIntent().getStringExtra("orderId");

        setContentView(R.layout.order_detail_activity);
        initViews();
//        setData(tradeRecord4AppVO);
        getOrderDetail();
    }

    private void initViews(){
        order_detail_title = (DiDiTitleView) findViewById(R.id.order_detail_title);
        order_detail_title.setBack(this);
        order_detail_title.setTitle("订单详情");

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
        stars_count = (TextView) findViewById(R.id.stars_count);
        driver_name = (TextView) findViewById(R.id.driver_name);
        evaluate_entry_container = (RelativeLayout) findViewById(R.id.evaluate_entry_container);
        contact_supplier_container = (RelativeLayout) findViewById(R.id.contact_supplier_container);
        supplier_container = (RelativeLayout) findViewById(R.id.supplier_container);
        driver_info_container = (RelativeLayout) findViewById(R.id.driver_info_container);

        mFinalBitmap = FinalBitmap.create(this);
        evaluate_entry_container.setOnClickListener(this);
        contact_supplier_container.setOnClickListener(this);
        supplier_container.setOnClickListener(this);
    }

    private void setData(OrderDetailModel tradeRecord4AppVO){
        user_name.setText(tradeRecord4AppVO.getSupplierShowName());
        money_num.setText(tradeRecord4AppVO.getMoneyStr());
        trade_status.setText(tradeRecord4AppVO.getStatusName());
        product_text.setText(tradeRecord4AppVO.getProduct());
        payment_text.setText(tradeRecord4AppVO.getPayTypeName());
        create_time_text.setText(tradeRecord4AppVO.getCreateDate());
        trade_code_text.setText(tradeRecord4AppVO.getTradeId());
        payment_desc_text.setText(tradeRecord4AppVO.getRemark());
        supplier_text.setText(tradeRecord4AppVO.getSupplierShowNickName());

        if(tradeRecord4AppVO.getDriverName() != null && !"".equals(tradeRecord4AppVO.getDriverName())){
            driver_info_container.setVisibility(View.VISIBLE);
            driver_name.setText(orderDetailModel.getDriverName() + "(" + orderDetailModel.getDriverPhone() + ")");
        }else {
            driver_info_container.setVisibility(View.GONE);
        }

        if(tradeRecord4AppVO.getEvaluationType() == 2){
            stars_count.setText(tradeRecord4AppVO.getStar() + "星");
            stars_count.setTextColor(getResources().getColor(R.color.black));
        }else {
            stars_count.setText("未评价");
            stars_count.setTextColor(getResources().getColor(R.color.light_grey));
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head_def_img);
        mFinalBitmap.display(user_head,tradeRecord4AppVO.getStorePhoto(),bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.evaluate_entry_container:
                if(orderDetailModel != null){
                    Intent intent = new Intent(OrderDetailActivity.this,SupplierEvaluateActivity.class);
                    intent.putExtra("payeeUid",orderDetailModel.getPayeeUid());
                    intent.putExtra("orderId",orderDetailModel.getId());
                    intent.putExtra("stars",orderDetailModel.getStar());
                    intent.putExtra("content",orderDetailModel.getContent());
                    startActivityForResult(intent,8001);
                }else {
                    Intent intent = new Intent(OrderDetailActivity.this,SupplierEvaluateActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.contact_supplier_container:
                if(orderDetailModel != null && orderDetailModel.getCellPhone() != null &&
                        !"".equals(orderDetailModel.getCellPhone())){
                    Util.call(OrderDetailActivity.this, orderDetailModel.getCellPhone());
                }else {
                    Toast.makeText(OrderDetailActivity.this,"供应商未留联系方式!",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.supplier_container:
                Intent intent1 = new Intent(OrderDetailActivity.this,SupplierTradeRecordActivity.class);
                if(orderDetailModel != null)
                {
                    intent1.putExtra("payeeUid",orderDetailModel.getPayeeUid());
                }
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    private final String orderDetailUrl = GloableParams.HOST + "carrier/qrcodepay/orderDetail.do?";
//        private final String orderDetailUrl = "http://192.168.158.33:8080/gwcz/carrier/qrcodepay/orderDetail.do";
    private void getOrderDetail(){
        AjaxParams params = new AjaxParams();
        params.put("orderId",orderId);

        DidiApp.getHttpManager().sessionPost(OrderDetailActivity.this, orderDetailUrl, params,
                new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            String body = jsonObject.optString("body");
                            orderDetailModel = JSON.parseObject(body,OrderDetailModel.class);
                            setData(orderDetailModel);

                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data != null){
            String stars = data.getStringExtra("stars");
            if(requestCode == 8001){
                stars_count.setText(stars + "星");
//                Intent intent = new Intent();
//                intent.setAction("com.action.evaluation");
//                sendBroadcast(intent);
                getOrderDetail();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
