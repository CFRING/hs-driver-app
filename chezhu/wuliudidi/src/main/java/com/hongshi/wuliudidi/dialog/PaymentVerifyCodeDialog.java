package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.Base64Encoder;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.TimeCounterButton;

import net.tsz.afinal.http.AjaxParams;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huiyuan on 2017/6/8.
 */

public class PaymentVerifyCodeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView hintText;
    private EditText verifyCodeEdit;
    private Button inputCancel, cancelButton, sureButton;
    private TimeCounterButton getCaptchas;
    private String userId = "";
    private String verifyCode = "";

    private Handler handler;

    /**
     *
     * @param context       上下文对象
     * @param theme         主题样式
     * @param userId        资金用户ID
     */
    public PaymentVerifyCodeDialog(Context context, int theme,
                                   String userId,
                                   Handler handler) {
        super(context, theme);
        this.mContext = context;
        this.userId = userId;
        this.handler = handler;
        init();
    }


    private void init() {
        setContentView(R.layout.verifycation_code_dialog);
        hintText = (TextView) findViewById(R.id.sdk_hint_text);
        verifyCodeEdit = (EditText) findViewById(R.id.sdk_code_input);
        inputCancel = (Button) findViewById(R.id.input_cancel);
        getCaptchas = (TimeCounterButton) findViewById(R.id.get_captchas);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        sureButton = (Button) findViewById(R.id.sure_button);

        hintText.setText("请输入" + mContext.getString(R.string.receive_code));

        Util.BindingEditTextAndButton(verifyCodeEdit, inputCancel);
        getCaptchas.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        sureButton.setOnClickListener(this);
    }

    /**
     * 获取验证码
     * @param userId  资金用户ID
     */
    private void getCaptchas(String userId){
        final PromptManager promptManager = new PromptManager();
        promptManager.showProgressDialog1(mContext, mContext.getString(R.string.hint_get_captchas));
        String url = GloableParams.HOST + "carrier/qrcodepay/sendCode.do";
//        String url = "http://192.168.158.96:8080/gwsj/carrier/qrcodepay/sendCode.do";
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(mContext, url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                promptManager.closeProgressDialog();
//                getCaptchas.setBackgroundResource(R.drawable.yan_zheng_ma_selected_bg);
                getCaptchas.TimeCounting(60);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                promptManager.closeProgressDialog();
            }
        });
    }


    private boolean inputJudge(){
        try{
            verifyCode = verifyCodeEdit.getText().toString();
        }catch (Exception e){
            Toast.makeText(mContext,mContext.getResources().getString(R.string.input_captchas),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(verifyCode == null || verifyCode.length() <= 0){
            Toast.makeText(mContext,mContext.getResources().getString(R.string.input_captchas),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.get_captchas) {
            getCaptchas(userId);

        } else if (i == R.id.cancel_button) {
            dismiss();

        } else if (i == R.id.sure_button) {
            if (inputJudge()) {
                if(handler != null){
                    handler.obtainMessage(1009,verifyCode).sendToTarget();
                    dismiss();
                }
            }

        } else {
        }
    }

}
