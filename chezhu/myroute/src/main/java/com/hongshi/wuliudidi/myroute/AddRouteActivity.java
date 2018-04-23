package com.hongshi.wuliudidi.myroute;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.dialog.WheelAddressDialog;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.dialog.WheelDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2016/6/20.
 */
public class AddRouteActivity extends Activity {
    private final String addRouteUrl = CommonRes.HOST + "carrier/commonLine/create.do";
    private final String goodsListUrl = CommonRes.CHE_ZHU_HOST  + "carrier/commonLine/findGoodsType.do";
//    private final String cityListUrl = "http://192.168.158.204:8080/gwsj/carrier/app/areaQuery/queryAll.do";
    private final String cityListUrl = CommonRes.HOST + "carrier/app/areaQuery/queryAll.do";
    private String sessionId;
    private String cityListJson = "";
    private String goodsListArr[];
    private DiDiTitleView titleView;
    private RelativeLayout start_city_layout;
    private RelativeLayout end_city_layout;
    private RelativeLayout choose_goods_layout;
    private TextView choose_goods_txt;
    private TextView end_addr_text;
    private TextView start_addr_text;
    private Button sure_add_btn;
    private ErrorDialog mErrorDialog;
    private boolean isStartCityChoosen = false;
    private boolean isEndCityChoosen = false;

    private List<GoodsTypes> goodsTypesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        routeCnt = getIntent().getIntExtra("current_count",0);
        setContentView(R.layout.my_route_choose);
        sessionId = getIntent().getStringExtra("sessionId");
        initView();
        getCityInfo();
        getGoodsTypes();
    }

    private void initView(){
        titleView = (DiDiTitleView)findViewById(R.id.add_route_title);
        titleView.setTitle(getString(R.string.add_route_btn_txt));
        titleView.setBack(AddRouteActivity.this);
        start_city_layout = (RelativeLayout)findViewById(R.id.start_city_layout);
        end_city_layout = (RelativeLayout)findViewById(R.id.end_city_layout);
        choose_goods_layout = (RelativeLayout)findViewById(R.id.choose_goods_layout);
        choose_goods_txt = (TextView)findViewById(R.id.choose_goods_txt);
        end_addr_text = (TextView)findViewById(R.id.end_addr_text);
        start_addr_text = (TextView)findViewById(R.id.start_addr_text);
        sure_add_btn = (Button)findViewById(R.id.sure_add_btn);
        start_city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityListJson != null && !cityListJson.equals("") && cityListJson.length() > 0){
                    setBtnClickAble(false);
                    final WheelAddressDialog dialog = new WheelAddressDialog(AddRouteActivity.this,
                            cityListJson, R.style.data_filling_dialog);
                    dialog.setOnSureBtnClickListener(new WheelAddressDialog.OnSureBtnClickListener() {
                        @Override
                        public void onClick(String province, String city, String area) {
//                        Toast.makeText(AddRouteActivity.this, province, Toast.LENGTH_SHORT).show();
                            isStartCityChoosen = true;
                            start_addr_text.setText(province + "-" + city + "-" + area);
                            start_addr_text.setTextColor(getResources().getColor(R.color.my_route_blacd));
                            dialog.cancel();
                            setBtnClickAble(true);
                        }
                    });
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            setBtnClickAble(true);
                        }
                    });
                    dialog.setOnCancelBtnClickListener(new WheelAddressDialog.OnCancelBtnClickListener() {
                        @Override
                        public void onClick() {
                            dialog.cancel();
                            setBtnClickAble(true);
                        }
                    });

                    Util.setAnimation(dialog, CommonRes.TYPE_BOTTOM, true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }else {
                    CancelDialog dialog = new CancelDialog(AddRouteActivity.this, R.style.data_filling_dialog, false);
                    dialog.setHint(getString(R.string.my_route_no_data));
                    dialog.setLeftText(getString(R.string.sure));
                    dialog.getRightView().setVisibility(View.GONE);
                    dialog.show();
                }
            }
        });
        end_city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityListJson != null && !cityListJson.equals("") && cityListJson.length() > 0){
                    setBtnClickAble(false);
                    final WheelAddressDialog dialog = new WheelAddressDialog(AddRouteActivity.this,
                            cityListJson, R.style.data_filling_dialog);
                    dialog.setOnSureBtnClickListener(new WheelAddressDialog.OnSureBtnClickListener() {
                        @Override
                        public void onClick(String province, String city, String area) {
//                        Toast.makeText(AddRouteActivity.this, province, Toast.LENGTH_SHORT).show();
                            isEndCityChoosen = true;
                            end_addr_text.setText(province + "-" + city + "-" + area);
                            end_addr_text.setTextColor(getResources().getColor(R.color.my_route_blacd));
                            dialog.cancel();
                            setBtnClickAble(true);
                        }
                    });
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            setBtnClickAble(true);
                        }
                    });
                    dialog.setOnCancelBtnClickListener(new WheelAddressDialog.OnCancelBtnClickListener() {
                        @Override
                        public void onClick() {
                            dialog.cancel();
                            setBtnClickAble(true);
                        }
                    });

                    Util.setAnimation(dialog, CommonRes.TYPE_BOTTOM, true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }else {
                    CancelDialog dialog = new CancelDialog(AddRouteActivity.this, R.style.data_filling_dialog, false);
                    dialog.setHint(getString(R.string.my_route_no_data));
                    dialog.setLeftText(getString(R.string.sure));
                    dialog.getRightView().setVisibility(View.GONE);
                    dialog.show();
                }
            }
        });
        choose_goods_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsListArr != null && goodsListArr.length > 0){
                    final WheelDialog dialog = new WheelDialog(AddRouteActivity.this, R.style.data_filling_dialog, goodsListArr);
                    dialog.setOnSureBtnClickListener(new WheelDialog.OnSureBtnClickListener() {
                        @Override
                        public void onClick(String s) {
                            choose_goods_txt.setText(s);
                            choose_goods_txt.setTextColor(getResources().getColor(R.color.my_route_blacd));
                            dialog.cancel();
                        }

                    });
                    dialog.setOnCancelBtnClickListener(new WheelDialog.OnCancelBtnClickListener() {
                        @Override
                        public void onClick() {
                            dialog.cancel();
                        }
                    });
                    Util.setAnimation(dialog, CommonRes.TYPE_BOTTOM, true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }else {
                    CancelDialog dialog = new CancelDialog(AddRouteActivity.this, R.style.data_filling_dialog, false);
                    dialog.setHint(getString(R.string.my_route_no_data));
                    dialog.setLeftText(getString(R.string.sure));
                    dialog.getRightView().setVisibility(View.GONE);
                    dialog.show();
                }
            }
        });
        sure_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int goodsTypeId = 0;
                if(goodsTypesList.size() > 0){
                    for(GoodsTypes type : goodsTypesList){
                        if(type.getValue().equals(choose_goods_txt.getText().toString())){
                            goodsTypeId = type.getKey();
                        }
                    }
                }

                if(isStartCityChoosen && isEndCityChoosen){
//                if(isStartCityChoosen){
                    addRoute(start_addr_text.getText().toString()
                            , end_addr_text.getText().toString(), String.valueOf(goodsTypeId));
                    finish();

                }else {
                    CancelDialog dialog = new CancelDialog(AddRouteActivity.this, R.style.data_filling_dialog, false);
                    dialog.setHint(getString(R.string.my_route_fill_info_tips));
                    dialog.setLeftText(getString(R.string.sure));
                    dialog.getRightView().setVisibility(View.GONE);
                    dialog.show();
                }
            }
        });
    }

    private void addRoute(String start_city, String end_city, String goodsType){
        AjaxParams params = new AjaxParams();
        params.put("senderArea", start_city);
        params.put("recipientArea", end_city);
        params.put("goodsType", goodsType);
        sessionPost(AddRouteActivity.this, addRouteUrl, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan","添加路线成功");
                Intent intent = new Intent();
                intent.setAction("add_route_suc");
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Log.d("huiyuan","添加路线失败:" + errMsg);
            }
        });
    }

    private void getCityInfo(){
        AjaxParams params = new AjaxParams();
        sessionPost(AddRouteActivity.this, cityListUrl, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan", "city json = " + t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    boolean isSuccess = jsonObject.optBoolean("success");
                    JSONArray cityList = jsonObject.optJSONArray("body");
                    if (isSuccess) {
                        if (cityList == null || cityList.length() <= 0) {
                            Toast.makeText(AddRouteActivity.this, "data exception", Toast.LENGTH_LONG);
                            cityListJson = "";
                        } else {
                            cityListJson = t;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }

    private void getGoodsTypes(){
        AjaxParams params = new AjaxParams();
        sessionPost(AddRouteActivity.this, goodsListUrl, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan","goods types json = " + t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    boolean isSuccess = jsonObject.optBoolean("success");
                    JSONArray goodsTypeList = jsonObject.optJSONArray("body");
                    int size = goodsTypeList.length();
                    if (isSuccess) {
                        if (goodsTypeList == null || size <= 0) {
                            Toast.makeText(AddRouteActivity.this, "data exception", Toast.LENGTH_LONG);
                        } else {
                            goodsListArr = new String[size];
                            for (int i = 0; i < size; i++) {
                                GoodsTypes model = new GoodsTypes();
                                JSONObject obj = (JSONObject) goodsTypeList.get(i);
                                model.setKey(obj.optInt("key"));
                                model.setValue(obj.optString("value"));
                                goodsTypesList.add(model);
                                goodsListArr[i] = model.getValue();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }

    private void sessionPost(final Context mContext, String url,
                             AjaxParams params, final AfinalHttpCallBack mCallBack) {
        FinalHttp mFinalHttp = new FinalHttp();
        mFinalHttp.configCharset("UTF-8");
        mFinalHttp.configTimeout(10000);// ��ʱʱ��
        try {
            mFinalHttp.configSSLSocketFactory(setCertificates(mContext.getAssets().open("HSWLROOTCAforInternalTest.crt"), mContext.getAssets().open("HSWLROOTCA.crt")));//����https������ʱΪ������֤
        }catch(IOException e){
            e.printStackTrace();
        }

        if(sessionId == null){
            sessionId = "";
        }
        mFinalHttp.addHeader("sessionId", sessionId);
        mFinalHttp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    if (jsonObject != null) {
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            mCallBack.data(t);
                        } else {
                            String errMsg = jsonObject.getString("errMsg");
                            String errCode = jsonObject.getString("errCode");
                            boolean errSerious = jsonObject.getBoolean("errSerious");
                            if (errSerious) {
                                //Ҫ���������
                                if (mErrorDialog == null) {
                                    mErrorDialog = new ErrorDialog(mContext, R.style.data_filling_dialog, errMsg);
                                }
                                if (!mErrorDialog.isShowing()) {
                                    mErrorDialog.show();
                                }
                            } else {
                                //���⴦������
//                                ErrorCodeUtil.errorCode(errCode, mContext, errMsg);//�������������
//                                Log.d("huiyuan","error msg = " + errMsg);
                            }
                            try {
                                ((ChildAfinalHttpCallBack) mCallBack).onFailure("", errMsg, false);
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//					mCallBack.data("");
                }
            }

            // ����ʧ��
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                try {
                    ((ChildAfinalHttpCallBack) mCallBack).onFailure("", strMsg, false);
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * for https-way authentication
     *
     * @param certificates
     */
    private SSLSocketFactory setCertificates(InputStream...certificates) {
        SSLSocketFactory sslSocketFactory = HttpsUtilsForFinalHttp.getSslSocketFactory(certificates, null, null);
        return sslSocketFactory;
    }

    private void setBtnClickAble(boolean clickable){
        start_city_layout.setClickable(clickable);
        end_city_layout.setClickable(clickable);
        choose_goods_layout.setClickable(clickable);
    }
}
