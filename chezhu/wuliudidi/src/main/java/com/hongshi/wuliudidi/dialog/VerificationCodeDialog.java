package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.TimeCounterButton;

import net.tsz.afinal.http.AjaxParams;

/**
 * Created by Administrator on 2016/4/14.
 */
public class VerificationCodeDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private TextView hintText;
    private EditText verifyCodeEdit;
    private Button inputCancel, cancelButton, sureButton;
    private TimeCounterButton getCaptchas;
    private String getCaptchasUrl, verifyUrl;
    private VerificationCodeCallBack callBack;
    private AjaxParams getVerifyCodeParams, submitVerifyCodeParams;
    private String verifyCode = "";
    public VerificationCodeDialog(Context context, int theme, String getCaptchasUrl, String verifyUrl, VerificationCodeCallBack callBack) {
        super(context, theme);
        this.mContext = context;
        this.getCaptchasUrl = getCaptchasUrl;
        this.verifyUrl = verifyUrl;
        this.callBack = callBack;
        init();
    }

    public void setParams(AjaxParams paramsGet, AjaxParams paramsSub){
        this.getVerifyCodeParams = paramsGet;
        this.submitVerifyCodeParams = paramsSub;
    }

    private void init() {
        setContentView(R.layout.verification_code_dialog_layout);

        hintText = (TextView) findViewById(R.id.hint_text);
        verifyCodeEdit = (EditText) findViewById(R.id.code_input);
        inputCancel = (Button) findViewById(R.id.input_cancel);
        getCaptchas = (TimeCounterButton) findViewById(R.id.get_captchas);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        sureButton = (Button) findViewById(R.id.sure_button);

        SharedPreferences sp = mContext.getSharedPreferences("config", mContext.MODE_PRIVATE);
        String cellphone = "", cellphoneTail = "";
        try{
            cellphone = sp.getString("cellphone", "");
            if(cellphone != null && cellphone.length() >= 4){
                cellphoneTail = cellphone.substring(cellphone.length() - 4, cellphone.length());
            }
        }catch (Exception e){

        }
        hintText.setText("输入手机尾号" + cellphoneTail + "接收到的短信验证码");

        Util.BindingEditTextAndButton(verifyCodeEdit, inputCancel);
        getCaptchas.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        sureButton.setOnClickListener(this);
    }

    private void getCaptchas(){
        if(getVerifyCodeParams == null){
            getVerifyCodeParams = new AjaxParams();
        }
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(mContext, "正在获取验证码");
        DidiApp.getHttpManager().sessionPost(mContext, getCaptchasUrl, getVerifyCodeParams, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
                getCaptchas.setEnabled(true);
            }

            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                getCaptchas.TimeCounting(60);
            }
        });
    }

    private void verifyCode(){
        if(submitVerifyCodeParams == null){
            submitVerifyCodeParams = new AjaxParams();
        }
        submitVerifyCodeParams.put("checkCode", verifyCode);
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(mContext, "正在提交验证码");
        DidiApp.getHttpManager().sessionPost(mContext, verifyUrl, submitVerifyCodeParams, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
                hintText.setText("验证码输入错误，请重新输入");
                hintText.setTextColor(mContext.getResources().getColor(R.color.theme_color));
            }

            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                callBack.onSucceed();
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.get_captchas:
                getCaptchas.setEnabled(false);
                getCaptchas();
                break;
            case R.id.cancel_button:
                dismiss();
                break;
            case R.id.sure_button:
                if(inputJudge()) {
                    verifyCode();
                }
                break;
            default:
                break;
        }
    }

    private boolean inputJudge(){
        try{
            verifyCode = verifyCodeEdit.getText().toString();
        }catch (Exception e){
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.input_captchas));
            return false;
        }
        if(verifyCode == null || verifyCode.length() <= 0){
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.input_captchas));
            return false;
        }
        return true;
    }

    public  static interface  VerificationCodeCallBack{
        public void onSucceed();
    }
}
