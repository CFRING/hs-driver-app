package com.hongshi.wuliudidi.cashier;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.cashier.okhttp.OkhttpRequest;
import com.hongshi.wuliudidi.cashier.okhttp.callback.RequestCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/4/14.
 */
public class VerificationCodeDialog extends Dialog implements View.OnClickListener{
    private final  String TAG = "VerificationCodeDialog";
    private Context mContext;
    private TextView hintText;
    private EditText verifyCodeEdit;
    private Button inputCancel, cancelButton, sureButton;
    private SdkTimeCounterButton getCaptchas;
    private VerificationCodeCallBack callBack;
    private String verifyCode = "";
    private String phoneNum = "";
    private String amount = "";
    private String bindingCardId = "" ;
    private String moneyUnitCode = "";
    private String userId = "";
    private String tradeCode = "";

    /**
     *
     * @param context       上下文对象
     * @param theme         主题样式
     * @param phoneNum      用户接收短信的手机号码
     * @param amount        金额
     * @param bindingCardId 绑定卡ID
     * @param moneyUnitCode 金钱单位
     * @param callBack      回调到页面的callback
     * @param userId        资金用户ID
     * @param tradeCode      提现类型
     */
    public VerificationCodeDialog(Context context, int theme,String tradeCode,String userId,String phoneNum,String amount,String bindingCardId ,String moneyUnitCode, VerificationCodeCallBack callBack) {
        super(context, theme);
        this.mContext = context;
        this.phoneNum = phoneNum;
        this.callBack = callBack;
        this.amount = amount;
        this.bindingCardId = bindingCardId;
        this.moneyUnitCode = moneyUnitCode;
        this.userId = userId;
        this.tradeCode = tradeCode;
        init();
    }


    private void init() {
        setContentView(R.layout.sdk_verification_code_dialog_layout);
        hintText = (TextView) findViewById(R.id.sdk_hint_text);
        verifyCodeEdit = (EditText) findViewById(R.id.sdk_code_input);
        inputCancel = (Button) findViewById(R.id.input_cancel);
        getCaptchas = (SdkTimeCounterButton) findViewById(R.id.get_captchas);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        sureButton = (Button) findViewById(R.id.sure_button);
        String cellphoneTail = "";
        try{
            if(phoneNum != null && phoneNum.length() >= 4){
                cellphoneTail = phoneNum.substring(phoneNum.length() - 4, phoneNum.length());
            }
        }catch (Exception e){

        }
        hintText.setText(mContext.getString(R.string.input_phone_number) + cellphoneTail + mContext.getString(R.string.receive_code));

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
        String url = GloableParams.BASE_URL+"/cashier/msg/sendCode.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId",userId);
        OkhttpRequest.postString(mContext, url, params, new RequestCallback() {
            @Override
            public void Success(String body) {
                promptManager.closeProgressDialog();
                getCaptchas.setBackgroundResource(R.drawable.yan_zheng_ma_selected_bg);
                getCaptchas.timeCounting(60);
            }

            @Override
            public void onError(String errMsg) {
                super.onError(errMsg);
                promptManager.closeProgressDialog();
            }
        });
    }

    /**
     * 验证验证码并生成订单
     * @param userId        资金用户ID
     * @param tradeCode     提现产品的code
     * @param amount        体现金额
     * @param bindingCardId 绑定银行卡ID
     * @param moneyUnitCode 金钱单位
     */
    private void verifyCode(String userId , String mobileCode,String tradeCode ,String amount,String bindingCardId,String moneyUnitCode){
        String url = GloableParams.BASE_URL + "/cashier/app/trade/withdraw.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId",userId);
        params.put("tradeCode",tradeCode);
        params.put("amount",amount);
        params.put("bindingCardId",bindingCardId);
        params.put("moneyUnitCode",moneyUnitCode);
        params.put("mobileCode", mobileCode);
        final PromptManager promptManager = new PromptManager();
        promptManager.showProgressDialog1(mContext, Util.getResString(mContext, R.string.waiting));
        OkhttpRequest.postString(mContext, url, params, new RequestCallback() {
            @Override
            public void Success(String body) {
                super.Success(body);
                promptManager.closeProgressDialog();
                callBack.onSucceed(body);
            }

            @Override
            public void onError(String errMsg) {
                super.onError(errMsg);
                callBack.onFail(errMsg);
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
                verifyCode(userId,verifyCode,tradeCode,amount,bindingCardId,moneyUnitCode);
            }

        } else {
        }
    }

    public  interface  VerificationCodeCallBack{
        void onSucceed(String body);
        void onFail(String errMsg);
    }
}
