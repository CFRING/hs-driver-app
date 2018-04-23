package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hongshi.wuliudidi.AES;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.update.AppUtils;
import com.hongshi.wuliudidi.utils.MD5Util;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.SmsObserver;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.TimeCounterButton;

import net.tsz.afinal.http.AjaxParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Created by huiyuan on 2016/12/14.
 */
public class PhoneChangeActivity extends Activity {

    private DiDiTitleView titleView;
    private ImageView step1_img,step2_img,step_finish_flag;
    private TextView identify_check,change_phone,login_psw_input_tip;
    private EditText edit_psw,edit_yanzhengma;
    private Button login;
    private LinearLayout yanzhegnma_input_container;
    private View line_2,line_3;
    private TimeCounterButton get_captchas;

    //获取验证码
    private final String GET_CAPTCHAS = GloableParams.HOST + "/uic/user/sendChangeCellphoneCode.do";
    //验证密码
    private final String IDENTIFY_CHECK_URL = GloableParams.HOST + "/uic/user/verifyPasswd.do";
    //更换手机号码
    private final String PHONE_CHANGE_URL = GloableParams.HOST + "/uic/user/verifyChangeCellphoneCode.do";
    private String psw = "";

    private SmsObserver mObserver;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CommonRes.SMS_PASSWORD) {
                String code = (String) msg.obj;
                edit_yanzhengma.setText(code);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_change_activity);

        init();
    }

    private void init(){
        titleView = (DiDiTitleView) findViewById(R.id.phone_change_title);
        titleView.setTitle(getResources().getString(R.string.yan_zheng_shen_fen));
        titleView.setBack(this);

        step1_img = (ImageView) findViewById(R.id.step1_img);
        step2_img = (ImageView) findViewById(R.id.step2_img);
        step_finish_flag = (ImageView) findViewById(R.id.step_finish_flag);
        identify_check = (TextView) findViewById(R.id.identify_check);
        change_phone = (TextView) findViewById(R.id.change_phone);
        login_psw_input_tip = (TextView) findViewById(R.id.login_psw_input_tip);
        edit_psw = (EditText) findViewById(R.id.edit_psw);
        edit_yanzhengma = (EditText) findViewById(R.id.edit_yanzhengma);
        login = (Button) findViewById(R.id.login);
        yanzhegnma_input_container = (LinearLayout) findViewById(R.id.yanzhegnma_input_container);
        yanzhegnma_input_container.setVisibility(View.GONE);
        line_2 = findViewById(R.id.line_2);
        line_3 = findViewById(R.id.line_3);
        get_captchas = (TimeCounterButton) findViewById(R.id.get_captchas);
        get_captchas.setVisibility(View.GONE);
        line_3.setVisibility(View.GONE);

        step1_img.setBackgroundResource(R.drawable.phone_change_step1_on);
        step2_img.setBackgroundResource(R.drawable.phone_change_step2_off);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PhoneChangeActivity.this.getSharedPreferences("config", Context.MODE_PRIVATE);
//                String account = sp.getString("account","");
                psw = edit_psw.getText().toString();
                checkIdentify(psw);
            }
        });

        mObserver = new SmsObserver(this, mHandler);
        Uri uri = Uri.parse("content://sms");
        // 注册ConentObserver
        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

    private void checkIdentify(final String psw){
        AjaxParams params = new AjaxParams();
        params.put("password", MD5Util.getMD5String(psw));
//        params.put("roleRefer", "5");//5代表车主登陆i
        final PromptManager mPromptManager = new PromptManager();
        mPromptManager.showProgressDialog1(PhoneChangeActivity.this, "正在校验....");

        DidiApp.getHttpManager().sessionPost(PhoneChangeActivity.this, IDENTIFY_CHECK_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                Toast.makeText(PhoneChangeActivity.this,"校验成功!",Toast.LENGTH_LONG).show();

                step1_img.setBackgroundResource(R.drawable.phone_change_step1_off);
                step_finish_flag.setVisibility(View.VISIBLE);
                step2_img.setBackgroundResource(R.drawable.phone_change_step2_on);
                identify_check.setTextColor(getResources().getColor(R.color.light_grey));
                change_phone.setTextColor(getResources().getColor(R.color.black));
                yanzhegnma_input_container.setVisibility(View.VISIBLE);
                line_3.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
                layoutParams.addRule(RelativeLayout.BELOW,R.id.psw_input_container);
                layoutParams.leftMargin = 20;
                line_2.setLayoutParams(layoutParams);
                login_psw_input_tip.setText(getResources().getString(R.string.cellphone));
                edit_psw.setText("");
                edit_psw.setHint(getResources().getString(R.string.input_new_phone_number_tip));
                edit_psw.setInputType(InputType.TYPE_CLASS_PHONE);
                get_captchas.setVisibility(View.VISIBLE);
                get_captchas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_captchas.setEnabled(false);
//                        get_captchas.setBackgroundResource(R.drawable.yan_zheng_ma_selected_bg);
                        getCaptchas(edit_psw.getText().toString());
                    }
                });

                login.setText("完成");
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePhone(edit_psw.getText().toString(), edit_yanzhengma.getText().toString());
                    }
                });
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
                Toast.makeText(PhoneChangeActivity.this,"校验失败: " + errMsg,Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isMobileNO(String mobiles) {

        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(mobiles)){
            return false;
        }
        else {return mobiles.matches(telRegex);}
    }

    private void getCaptchas(String phone_number){
        if(phone_number == null || "".equals(phone_number) || !isMobileNO(phone_number)){
            Toast.makeText(PhoneChangeActivity.this,"请输入正确的新手机号码",Toast.LENGTH_LONG).show();
            get_captchas.setEnabled(true);
            get_captchas.setBackgroundResource(R.drawable.frame_btn_style);
            return;
        }
        final PromptManager promptManager = new PromptManager();
        promptManager.showProgressDialog1(PhoneChangeActivity.this, "正在获取验证码...");
        SharedPreferences sharedPreferences = this.getSharedPreferences("config",MODE_PRIVATE);
        String sessionId = sharedPreferences.getString("session_id", "");
        AES aes = new AES();
        AjaxParams params = new AjaxParams();
        params.put("cellphone",phone_number);
        //加密之前的crc,使用老的手机号码
//        String old_phone_number = sharedPreferences.getString("cellphone", "");
        String crc = sessionId + "_" + phone_number;
        try {
            //加密过后的crc
            crc = URLEncoder.encode(aes.encrypt(crc.getBytes()), "utf-8");
            params.put("crc",crc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        DidiApp.getHttpManager().sessionPost(PhoneChangeActivity.this, GET_CAPTCHAS, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                promptManager.closeProgressDialog();
                get_captchas.TimeCounting(60);
                Toast.makeText(PhoneChangeActivity.this,"成功获取验证码",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                promptManager.closeProgressDialog();
                Toast.makeText(PhoneChangeActivity.this,"获取验证码失败: " + errMsg,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changePhone(final String newAccount,String checkCode){
        AjaxParams params = new AjaxParams();
        params.put("cellphone",newAccount);
        params.put("code", checkCode);
//        params.put("roleRefer", "5");//5代表车主登陆i
        if(newAccount == null || "".equals(newAccount)){
            Toast.makeText(PhoneChangeActivity.this,"请输入完整信息",Toast.LENGTH_LONG);
            return;
        }

        final PromptManager mPromptManager = new PromptManager();
        mPromptManager.showProgressDialog1(PhoneChangeActivity.this, "正在更换....");
        DidiApp.getHttpManager().sessionPost(PhoneChangeActivity.this, PHONE_CHANGE_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                Toast.makeText(PhoneChangeActivity.this,"更换成功!" ,Toast.LENGTH_LONG).show();
                new AppUtils().doLogin(PhoneChangeActivity.this, psw, newAccount);
//                Intent userInfo_intent = new Intent();// 我的界面更新
//                userInfo_intent.setAction(CommonRes.RefreshUserInfo);
//                sendBroadcast(userInfo_intent);
//                PhoneChangeActivity.this.finish();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
//                Toast.makeText(PhoneChangeActivity.this,"更换失败: " + errMsg,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
