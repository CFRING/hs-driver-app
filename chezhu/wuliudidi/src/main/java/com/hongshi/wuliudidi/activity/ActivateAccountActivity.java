package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.UserLoginModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.update.AppUtils;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

/**
 * @author huiyuan
 * Created by huiyuan on 2016/7/8.
 */
public class ActivateAccountActivity extends Activity{

    private Button sure_active_btn;
    private DiDiTitleView titleView;
    private TextView transit_protocol,register_protocol;
    private String account;
    private String psw;
    private String userId;

    private final String activeUrl = "https://cz.redlion56.com/gwcz/uic/user/activeTransAccount.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = getIntent().getStringExtra("account");
        psw = getIntent().getStringExtra("psw");
        userId = getIntent().getStringExtra("userId");
        setContentView(R.layout.active_account_activity);
        initView();

    }

    private void initView(){
        titleView = (DiDiTitleView)findViewById(R.id.active_account_title);
        titleView.setBack(this);
        transit_protocol = (TextView)findViewById(R.id.transit_protocol);
        register_protocol = (TextView)findViewById(R.id.register_protocol);
        transit_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mTransitAgreementIntent = new Intent(ActivateAccountActivity.this,WebViewWithTitleActivity.class);
                mTransitAgreementIntent.putExtra("title", "运输协议");
                mTransitAgreementIntent.putExtra("url", GloableParams.WEB_URL + "transportationProtocol.html");
                startActivity(mTransitAgreementIntent);
            }
        });
        register_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivateAccountActivity.this, WebViewWithTitleActivity.class);
                intent.putExtra("url", GloableParams.WEB_URL + "registrationProtocol.html");
                intent.putExtra("title", "注册协议");
                startActivity(intent);
            }
        });

        sure_active_btn = (Button)findViewById(R.id.sure_active_btn);
        sure_active_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AjaxParams params = new AjaxParams();
                JSONObject object = new JSONObject();
                try {
                    object.put("cellphone",account);
                    object.put("passWord",psw);
                    object.put("userId",userId);
                }catch (Exception e){
                    e.printStackTrace();
                }
                params.put("userJson",object.toString());
                DidiApp.getHttpManager().sessionPost(ActivateAccountActivity.this, activeUrl,
                        params, new ChildAfinalHttpCallBack() {
                            @Override
                            public void data(String t) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(t);
                                    String body = jsonObject.optString("body");
                                    Log.d("huiyuan","active account json body = " + body);
                                    UserLoginModel userModel = JSON.parseObject(
                                            body, UserLoginModel.class);
                                    new AppUtils().saveLoginData(ActivateAccountActivity.this, userModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtil.show(ActivateAccountActivity.this, e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                                ToastUtil.show(ActivateAccountActivity.this, errMsg);
                            }
                        });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("ActivateAccountActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("ActivateAccountActivity");
    }
}
