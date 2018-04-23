package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AllAuctionModel;
import com.hongshi.wuliudidi.model.AllAuctionModelForEavelate;
import com.hongshi.wuliudidi.model.BidJudgeVO;
import com.hongshi.wuliudidi.model.EvaluateModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @a Created by he on 2017/6/27.
 */

public class RatingActivity extends Activity implements View.OnClickListener{
    private AllAuctionModelForEavelate ordermodel;
    private boolean isJudged = false;
    private TextView startCity,endCity,truck_number,goodsName,goodsWeight,price,order_state,evaluate_text;
    private RelativeLayout state_bg;
    private ImageView call_image;
    private DiDiTitleView mTitle;
    private EditText input_content;
    private Button mCommit;
    private RadioGroup radio_group;
    private int level = 2;
    private String info_url = GloableParams.HOST + "carrier/bidJudge/judgeDetail.do?";
    private String commit_url = GloableParams.HOST + "carrier/bidJudge/judge.do?";
    private PromptManager mPromptManager;
    private RatingBar ratingbar_level;
    private TextView name,number;
    private String tel = "";
    private BidJudgeVO mBidJudgeVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_activity);
        initViews();
    }

    private void initViews(){
        mPromptManager = new PromptManager();
        ordermodel = (AllAuctionModelForEavelate) getIntent().getExtras().get("model");
        isJudged = getIntent().getBooleanExtra("isJudged",false);
        mTitle = (DiDiTitleView) findViewById(R.id.rating_title);
        mTitle.setBack(this);
        mTitle.setTitle("评价交易员");
        truck_number = (TextView) findViewById(R.id.truck_number);
        state_bg = (RelativeLayout)findViewById(R.id.state_bg);
        startCity = (TextView)findViewById(R.id.start_city);
        endCity = (TextView) findViewById(R.id.end_city);
        goodsName = (TextView)findViewById(R.id.task_item_goods_name);
        goodsWeight = (TextView) findViewById(R.id.task_item_goods_weight);
        price = (TextView) findViewById(R.id.buttom_type_name);
        order_state = (TextView) findViewById(R.id.task_order_state);
        call_image = (ImageView) findViewById(R.id.call_image);
        evaluate_text = (TextView) findViewById(R.id.evaluate_text);
        input_content = (EditText) findViewById(R.id.input_content);
        mCommit = (Button) findViewById(R.id.submint);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        ratingbar_level = (RatingBar) findViewById(R.id.ratingbar_level);
        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        mBidJudgeVO = new BidJudgeVO();
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.btn_0:
                        level = 2;
                        break;
                    case R.id.btn_1:
                        level = 1;
                        break;
                    case R.id.btn_2:
                        level = 0;
                        break;
                    default:
                        break;
                }
            }
        });
        if (isJudged){
            evaluate_text.setVisibility(View.VISIBLE);
            input_content.setVisibility(View.GONE);
            disableRadioGroup(radio_group);
            mCommit.setVisibility(View.GONE);
        }
        init();
        call_image.setOnClickListener(this);
        mCommit.setOnClickListener(this);
    }

    public void disableRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
    }
    private void init() {
        if (ordermodel != null){
            truck_number.setText(ordermodel.getTruckNo());
            startCity.setText(ordermodel.getSendAddr());
            endCity.setText(ordermodel.getRecvAddr());

            goodsName.setText(ordermodel.getGoodsName());
            goodsWeight.setText(
                    Util.formatDoubleToString(ordermodel.getGoodsAmount(), ordermodel.getAssignUnitText())
                            + ordermodel.getAssignUnitText());
            String priceStr1 = Util.formatDoubleToString(ordermodel.getBidPrice(), "元");
            price.setVisibility(View.VISIBLE);
            String price_str2 = "价格  "+priceStr1+" 元/"+ ordermodel.getSettleUnitText();
            Util.setText(RatingActivity.this, price_str2, priceStr1, price, R.color.theme_color);
            order_state.setText(ordermodel.getStatusText());
            switch (ordermodel.getStatus()) {
                //待审核
                case 1:
                    break;
                //审核中
                case 2:
                    break;
                //取消
                case 3:
                    break;
                //未通过
                case 4:
                    break;
                //运输未开始
                case 5:
                    state_bg.setBackgroundResource(R.drawable.do_bg);
                    break;
                //运输中
                case 6:
                    state_bg.setBackgroundResource(R.drawable.doing_bg);
                    break;
                //已完成
                case 7:
                    state_bg.setBackgroundResource(R.drawable.done_bg);
                    break;
                default:
                    break;
            }
            getData();
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.submint:
                if(Util.containsEmoji(input_content.getText().toString())){
                    ToastUtil.show(RatingActivity.this, "输入有非法字符，请重新输入");
                    return;
                }
                commit();
                break;
            case R.id.call_image:
                if (!tel.equals("")){
                    Util.call(RatingActivity.this, tel);
                }else{
                    ToastUtil.show(RatingActivity.this, "电话号码为空");
                }
                break;
            default:
                break;
        }
    }

    private String optId = "";
    private void getData(){
        AjaxParams params = new AjaxParams();
        params.put("bidItemID", ordermodel.getBidItemId());
        params.put("optId", ordermodel.getOptId());
        mPromptManager.showProgressDialog1(RatingActivity.this, "加载中");
        DidiApp.getHttpManager().sessionPost(RatingActivity.this, info_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(t);
                    String all = jsonObject.getString("body");
                    JSONObject body_obj = new JSONObject(all);
                    int judgeLevel = body_obj.optInt("judgeLevel");
                    if (judgeLevel == 2 || judgeLevel == 0){
                        judgeLevel = 2 - judgeLevel;
                    }
                    ((RadioButton)radio_group.getChildAt(judgeLevel)).setChecked(true);
                    String content = body_obj.optString("content");
                    String user_info = body_obj.optString("operator");
                    if (!content.equals("")){
                        evaluate_text.setText(content);
                    }else{
                        evaluate_text.setVisibility(View.GONE);
                    }
                    EvaluateModel mEvaluateModel = JSON.parseObject(user_info,EvaluateModel.class);
                    optId = mEvaluateModel.getId();
                    ratingbar_level.setRating(mEvaluateModel.getStar());
                    name.setText(mEvaluateModel.getName());
                    number.setText("工号："+optId);
                    tel = mEvaluateModel.getCellPhone();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
            }
        });
    }
    private void commit(){
        AjaxParams params = new AjaxParams();
        mBidJudgeVO.setBidItemID(ordermodel.getBidItemId());
        mBidJudgeVO.setOptID(optId);
        mBidJudgeVO.setContent(input_content.getText().toString());
        mBidJudgeVO.setJudgeLevel(level);
        params.put("judge", JSON.toJSONString(mBidJudgeVO));
        mPromptManager.showProgressDialog1(RatingActivity.this, "加载中");
        DidiApp.getHttpManager().sessionPost(RatingActivity.this, commit_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String integral = jsonObject.optString("body");
                    if (integral != null && !"".equals(integral) && Integer.parseInt(integral) > 0){
                        Toast.makeText(RatingActivity.this,"获得" + integral + "个积分",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("earned_integral");
                        sendBroadcast(intent);
                    }else {
                        Toast.makeText(RatingActivity.this,"评价成功",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }

                Intent ref_intent = new Intent();
                ref_intent.setAction(CommonRes.Evaluate_Refresh);
                sendBroadcast(ref_intent);
                finish();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
            }
        });
    }
}
