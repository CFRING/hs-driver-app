package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.ChooseOilCardDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.OilCardModel;
import com.hongshi.wuliudidi.model.OilModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.ChooseCardView;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.List;

/**
 * @author Created by huiyuan on 2017/8/12.
 */

public class OilCardWidtrawlActivity extends Activity implements View.OnClickListener{

    private final String oilWidthdrawlUrl = GloableParams.HOST + "carrier/app/acct/drawOil.do?";
    private final String oilWidthdrawlInfo = GloableParams.HOST + "carrier/app/acct/fetchOilCards.do";

    private DiDiTitleView mTitleView;
    //提现金额输入框
    private EditText mTiXianMoneyEdt;
    private TextView mTiXianAllTxt,oil_supplier_name,truck_number,
            can_ti_xian_money_tip1,can_ti_xian_money,can_ti_xian_money_tip2,
            over_flow_tip,settle_company_text;
    //提现按钮
    private Button mTiXianBtn;
    private ChooseCardView choose_card_view;

    private double all_count = 0;
    private List<OilCardModel> oilCardModelList;
    private String chosenOilCardId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oil_widthrawl_activity);

        initView();
    }

    private void initView(){
        mTitleView = (DiDiTitleView) findViewById(R.id.ti_xian_title);
        mTitleView.setBack(this);
        mTitleView.setTitle("油卡余额提现");
        mTiXianMoneyEdt = (EditText) findViewById(R.id.ti_xian_money_edit);
        mTiXianAllTxt = (TextView) findViewById(R.id.ti_xian_all);
        can_ti_xian_money = (TextView) findViewById(R.id.can_ti_xian_money);
        oil_supplier_name = (TextView) findViewById(R.id.oil_supplier_name);
        truck_number = (TextView) findViewById(R.id.truck_number);
        can_ti_xian_money_tip1 = (TextView) findViewById(R.id.can_ti_xian_money_tip1);
        can_ti_xian_money = (TextView) findViewById(R.id.can_ti_xian_money);
        can_ti_xian_money_tip2 = (TextView) findViewById(R.id.can_ti_xian_money_tip2);
        over_flow_tip = (TextView) findViewById(R.id.over_flow_tip);
        settle_company_text = (TextView) findViewById(R.id.settle_company_text);
        mTiXianBtn = (Button) findViewById(R.id.bottom);
        choose_card_view = (ChooseCardView) findViewById(R.id.choose_card_view);

        mTiXianBtn.setOnClickListener(this);
        mTiXianAllTxt.setOnClickListener(this);
        choose_card_view.setOnClickListener(this);

        mTiXianMoneyEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //如果第一位为“.”就在最前面加“0”
                if (string.length() > 0) {
                    if (string.trim().substring(0, 1).equals(".")) {
                        string = "0" + string;
                        mTiXianMoneyEdt.setText(string);
                        mTiXianMoneyEdt.setSelection(2);
                    }
                }
                if (!string.equals("") && Double.valueOf(string) != 0) {
                    mTiXianBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bottom));
                } else {
                    mTiXianBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray));
                }

                if(!string.equals("") && Double.valueOf(string) > all_count){
                    //大于可提现额度
                    mTiXianBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray));
                    mTiXianBtn.setClickable(false);
                    can_ti_xian_money_tip1.setVisibility(View.GONE);
                    can_ti_xian_money.setVisibility(View.GONE);
                    can_ti_xian_money_tip2.setVisibility(View.GONE);
                    mTiXianAllTxt.setVisibility(View.GONE);
                    over_flow_tip.setVisibility(View.VISIBLE);
                }else {
                    setOilWidthdrawlStatus();
                }

                //限制只能出入两位小数
                if (string.contains(".")) {
                    if (string.length() - 1 - string.indexOf(".") > 2) {
                        string = string.substring(0, string.indexOf(".") + 3);
                        mTiXianMoneyEdt.setText(string);
                        mTiXianMoneyEdt.setSelection(string.length());
                    }
                }
//                如果第一位为“0”且有第二位并且第二位不是“.”，则去掉第一位“0”
                if (string.startsWith("0")
                        && string.trim().length() > 1) {
                    if (!string.substring(1, 2).equals(".")) {
                        mTiXianMoneyEdt.setText(string.substring(1));
                        mTiXianMoneyEdt.setSelection(1);
                    }
                }
            }
        });

        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ti_xian_all:
                mTiXianMoneyEdt.setText(all_count + "");
                break;
            case R.id.bottom:
                oilWidthdrawl();
                break;
            case R.id.choose_card_view:
                ChooseOilCardDialog chooseOilCardDialog =
                        new ChooseOilCardDialog(OilCardWidtrawlActivity.this,R.style.data_filling_dialog,
                        oilCardModelList,new ChooseOilCardDialog.ChooseDialogCallBack(){
                            @Override
                            public void oilCardChosen(OilCardModel model) {
                                if(model != null){
                                    setOilSupplierInfo(model);
                                }
                                setOilWidthdrawlStatus();
                            }
                        });
                UploadUtil.setAnimation(chooseOilCardDialog, CommonRes.TYPE_BOTTOM,true);
                chooseOilCardDialog.show();
                break;
            default:
                break;
        }
    }

    private void setOilWidthdrawlStatus(){
        mTiXianBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bottom));
        mTiXianBtn.setClickable(true);
        can_ti_xian_money_tip1.setVisibility(View.VISIBLE);
        can_ti_xian_money.setVisibility(View.VISIBLE);
        can_ti_xian_money_tip2.setVisibility(View.VISIBLE);
        mTiXianAllTxt.setVisibility(View.VISIBLE);
        over_flow_tip.setVisibility(View.GONE);
    }

    private void oilWidthdrawl(){
        AjaxParams params = new AjaxParams();
        params.put("amount",mTiXianMoneyEdt.getText().toString());
        params.put("cardID",chosenOilCardId);
        DidiApp.getHttpManager().sessionPost(OilCardWidtrawlActivity.this, oilWidthdrawlUrl,
                params, new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        Toast.makeText(OilCardWidtrawlActivity.this,"提取成功!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("com.action.oil");
                        sendBroadcast(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                        ToastUtil.show(OilCardWidtrawlActivity.this, errMsg);
                    }
                });
    }

    private void getData(){
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(OilCardWidtrawlActivity.this, oilWidthdrawlInfo,
                params, new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(t);
                            String body = jsonObject.optString("body");
                            OilModel oilModel = JSON.parseObject(body,OilModel.class);

//                            if(oilModel != null && !"".equals(oilModel.getAmount())){
//                                can_ti_xian_money.setText(oilModel.getAmount());
//                                all_count = Double.valueOf(oilModel.getAmount());
//                            }
                            oilCardModelList = oilModel.getCardInfoList();
                            if(oilCardModelList != null && oilCardModelList.size() > 1){
                                OilCardModel model = oilCardModelList.get(1);
                                setOilSupplierInfo(model);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(OilCardWidtrawlActivity.this, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                        ToastUtil.show(OilCardWidtrawlActivity.this, errMsg);
                    }
                });
    }

    private void setOilSupplierInfo(OilCardModel model){
        String oilName = "";
        switch (model.getSupplierType()){
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
        choose_card_view.setOilImg(model.getSupplierType());
        choose_card_view.setOilNameAndCardTxt(oilName,model.getCardID());
        oil_supplier_name.setText(model.getGasolineStation());
        truck_number.setText(model.getTruckNum());
        settle_company_text.setText(model.getSupplier());
        can_ti_xian_money.setText(model.getAmount());
        all_count = Double.valueOf(model.getAmount());
        chosenOilCardId = model.getCardID();
    }
}
