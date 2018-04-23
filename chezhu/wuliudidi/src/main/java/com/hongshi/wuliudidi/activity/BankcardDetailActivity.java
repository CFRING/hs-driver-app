package com.hongshi.wuliudidi.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.ListItemDeletingDialog;
import com.hongshi.wuliudidi.dialog.VerificationCodeDialog;
import com.hongshi.wuliudidi.dialog.VerificationCodeDialog.VerificationCodeCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.BankcardModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

/**
 * @author huiyuan
 */
public class BankcardDetailActivity extends Activity implements View.OnClickListener{
    private String deletecardUrl = GloableParams.HOST + "uic/user/bankCard/bankCardDelete.do?";
    private BankcardModel bankcardModel;
    private DiDiTitleView title;
    private RelativeLayout bankCardLayout, surroundingBanksLayout;
    private ImageView bankIconImage;
    private TextView bankNameText, cardNumberText;
    private ListItemDeletingDialog mDeletingDialog;

    @SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonRes.DELETE_BANKCARD:
                    deleteBankcard();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("BankcardDetailActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("BankcardDetailActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.bankcard_detail_activity);
        try{
            bankcardModel = (BankcardModel) getIntent().getSerializableExtra("bankcardModel");
        }catch (Exception e){
            bankcardModel = new BankcardModel();
        }
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        title = (DiDiTitleView) findViewById(R.id.bank_card_detail_title);
        bankCardLayout = (RelativeLayout) findViewById(R.id.bank_card_layout);
        bankIconImage = (ImageView) findViewById(R.id.bank_icon_image);
        bankNameText = (TextView) findViewById(R.id.bank_name_text);
        cardNumberText = (TextView) findViewById(R.id.card_number_text);
        surroundingBanksLayout = (RelativeLayout) findViewById(R.id.surrounding_banks_layout);
        if (bankcardModel == null) {
            return;
        }
        title.setBack(BankcardDetailActivity.this);
        title.setTitle(getResources().getString(R.string.my_bankcard));
        title.getRightTextView().setText(getResources().getString(R.string.manager));
        title.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mDeletingDialog != null) {
//                    mDeletingDialog.show();
//                }
                //初始化删除银行卡对话框
                mDeletingDialog = new ListItemDeletingDialog(BankcardDetailActivity.this, R.style.data_filling_dialog, mHandler);
                mDeletingDialog.setCanceledOnTouchOutside(true);
                mDeletingDialog.setText("解绑银行卡", "取消");
                mDeletingDialog.setMsgNum(CommonRes.DELETE_BANKCARD);
                mDeletingDialog.setItemId("");
                mDeletingDialog.getExampleImg().setVisibility(View.GONE);
                UploadUtil.setAnimation(mDeletingDialog, CommonRes.TYPE_BOTTOM, false);
                mDeletingDialog.show();
            }
        });
        switch (bankcardModel.getBankType()) {
            case 1:
                //中国银行
                bankCardLayout.setBackgroundResource(R.drawable.boc_background_style);
                bankIconImage.setImageResource(R.drawable.boc_bank_icon_big);
                break;
            case 2:
                //工商银行
                bankCardLayout.setBackgroundResource(R.drawable.icbc_background_style);
                bankIconImage.setImageResource(R.drawable.icbc_bank_icon_big);
                break;
            case 4:
                //建设银行
                bankCardLayout.setBackgroundResource(R.drawable.ccb_background_style);
                bankIconImage.setImageResource(R.drawable.ccb_bank_icon_big);
                break;
            case 5:
                //农业银行
                bankCardLayout.setBackgroundResource(R.drawable.abc_background_style);
                bankIconImage.setImageResource(R.drawable.abc_bank_icon_big);
                break;
            default:
                bankCardLayout.setBackgroundResource(R.drawable.other_background_style);
                bankIconImage.setImageResource(R.drawable.other_bank_icon_big);
                break;
        }
        bankNameText.setText(bankcardModel.getBankName());
        String cardNumber = bankcardModel.getBankNumber(), hidedNumber = "";
        if (cardNumber != null) {
            for (int i = 0; i < cardNumber.length(); i++) {
                if (i < cardNumber.length() - 4) {
                    hidedNumber += '*';
                } else {
                    hidedNumber += cardNumber.charAt(i);
                }
            }
        }
        cardNumberText.setText(hidedNumber);

        surroundingBanksLayout.setOnClickListener(this);

    }

    private void deleteBankcard(){
        if(bankcardModel == null || bankcardModel.getId() == null){
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put("id", bankcardModel.getId());
        final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(BankcardDetailActivity.this, "正在提交申请");
        DidiApp.getHttpManager().sessionPost(BankcardDetailActivity.this, deletecardUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
            }

            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                //删卡成功发送广播
                Intent intent = new Intent();
                intent.setAction(CommonRes.BindNewBankcard);
                sendBroadcast(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.surrounding_banks_layout:
                intent = new Intent(BankcardDetailActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                if(bankcardModel.getBankName() != null) {
                    b.putString("mapType", "NeighborhoodSearching");
                    b.putString("keyWord", bankcardModel.getBankName());
                    intent.putExtras(b);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
