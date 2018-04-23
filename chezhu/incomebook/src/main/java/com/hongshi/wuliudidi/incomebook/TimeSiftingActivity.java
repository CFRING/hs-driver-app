package com.hongshi.wuliudidi.incomebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.Calendar;
import java.util.Date;

/**
 * 运输记录时间筛选页面
 */
public class TimeSiftingActivity extends Activity implements View.OnClickListener{
    private DiDiTitleView titleView;
    private TextView annualText, monthlyText, dailyStartText, dailyEndText, sureText;
    private View shortLine;
    private RadioGroup radioGroup;
    private Button cleanDataButton;
    private String annualSift, monthlySift, dailyStartSift, dailyEndSift;
    private int radioGroupCheckedId = -1;
    public static final int TYPE_BOTTOM  = 0;//dialog底部弹出标记
    public static final int TYPE_CENTER  = 1;//居中显示

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd("TimeSiftingActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart("TimeSiftingActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_sifting_activity);

        titleView = (DiDiTitleView) findViewById(R.id.title);
        titleView.setTitle(getResources().getString(R.string.sifting));
        titleView.setBack(this);

        annualText = (TextView) findViewById(R.id.annual_text);
        annualText.setOnClickListener(this);
        monthlyText = (TextView) findViewById(R.id.monthly_text);
        monthlyText.setOnClickListener(this);
        dailyStartText = (TextView) findViewById(R.id.daily_start_text);
        dailyStartText.setOnClickListener(this);
        dailyEndText = (TextView) findViewById(R.id.daily_end_text);
        dailyEndText.setOnClickListener(this);

        shortLine = (View) findViewById(R.id.short_line);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                clearAllDateText();
                DateDialog mDateDialog;
                if(radioGroupCheckedId == i){
                    return;
                }else{
                    radioGroupCheckedId = i;
                }
                switch (i){
                    case R.id.annual_radiobtn://年
                        annualText.setVisibility(View.VISIBLE);
                        mDateDialog = new DateDialog(TimeSiftingActivity.this, R.style.data_filling_dialog,
                                getDialogCallBack(0), DateDialog.Year,"请选择年份");
                        UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                        mDateDialog.show();
                        break;
                    case R.id.monthly_radiobtn://月
                        monthlyText.setVisibility(View.VISIBLE);
                        mDateDialog = new DateDialog(TimeSiftingActivity.this, R.style.data_filling_dialog,
                                getDialogCallBack(1), DateDialog.YearMonth,"请选择月份");
                        UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                        mDateDialog.show();
                        break;
                    case R.id.daily_radiobtn://日
                        dailyStartText.setVisibility(View.VISIBLE);
                        shortLine.setVisibility(View.VISIBLE);
                        dailyEndText.setVisibility(View.VISIBLE);
                        mDateDialog = new DateDialog(TimeSiftingActivity.this, R.style.data_filling_dialog,
                                new SetDateCallBack() {
                                    @Override
                                    public void date(long date) {
                                        Date mDate = new Date(date);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(mDate);
                                        dailyStartText.setText(String.valueOf(cal.get(Calendar.YEAR)) + "-"
                                                + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
                                                + String.valueOf(cal.get(Calendar.DATE)));
                                        dailyStartSift = String.valueOf(date);
                                        if(dailyEndSift != null && dailyEndSift.length() > 0){
                                            sureText.setEnabled(true);
                                        }
                                        //如果是点击radiobutton弹出来的日期对话框，在选择完开始时间 后 立即弹出 选择截止时间的对话框
                                        DateDialog newDateDialog = new DateDialog(TimeSiftingActivity.this,
                                                R.style.data_filling_dialog,
                                                new SetDateCallBack() {
                                                    @Override
                                                    public void date(long date) {
                                                        Date mDate = new Date(date);
                                                        Calendar cal = Calendar.getInstance();
                                                        cal.setTime(mDate);
                                                        dailyEndText.setText(String.valueOf(cal.get(Calendar.YEAR)) + "-"
                                                                + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
                                                                + String.valueOf(cal.get(Calendar.DATE)));
                                                        dailyEndSift = String.valueOf(date);
                                                        if(dailyStartSift != null && dailyStartSift.length() > 0){
                                                            sureText.setEnabled(true);
                                                        }
                                                    }
                                                }, DateDialog.YearMonthDay, "请选择截止时间");
                                        UploadUtil.setAnimation(newDateDialog, TYPE_BOTTOM, true);
                                        newDateDialog.show();
                                    }
                                }, DateDialog.YearMonthDay, "请选择起始时间");
                        UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                        mDateDialog.show();
                        break;
                }
            }
        });

        cleanDataButton = (Button) findViewById(R.id.clean_data_button);
        cleanDataButton.setOnClickListener(this);

        sureText = (TextView) findViewById(R.id.sure);
        sureText.setOnClickListener(this);

        radioGroupCheckedId = -1;
        clearAll();
    }

    @Override
    public void onClick(View view) {
        DateDialog mDateDialog;
        switch (view.getId()){
            case R.id.annual_text:
                mDateDialog = new DateDialog(this, R.style.data_filling_dialog, getDialogCallBack(0), DateDialog.Year, "请选择年份");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.monthly_text:
                mDateDialog = new DateDialog(this, R.style.data_filling_dialog, getDialogCallBack(1), DateDialog.YearMonth, "请选择月份");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.daily_start_text:
                mDateDialog = new DateDialog(this, R.style.data_filling_dialog, getDialogCallBack(2), DateDialog.YearMonthDay, "请选择起始时间");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.daily_end_text:
                mDateDialog = new DateDialog(this, R.style.data_filling_dialog, getDialogCallBack(3), DateDialog.YearMonthDay, "请选择截止时间");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.clean_data_button:
                clearAll();
                break;
            case R.id.sure:
                Intent it = new Intent();
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.daily_radiobtn:
                        it.putExtra("queryType", "0");
                        it.putExtra("queryDateStart", dailyStartSift);
                        it.putExtra("queryDateEnd", dailyEndSift);
                        break;
                    case R.id.monthly_radiobtn:
                        it.putExtra("queryType", "1");
                        it.putExtra("queryDateStart", monthlySift);
                        break;
                    case R.id.annual_radiobtn:
                        it.putExtra("queryType", "2");
                        it.putExtra("queryDateStart", annualSift);
                        break;
                }
                setResult(1002, it);
                finish();
                break;
        }
    }

    private void clearAll(){
        radioGroup.clearCheck();
        clearAllDateText();
    }

    private void clearAllDateText(){
        annualText.setText("");
        annualText.setVisibility(View.INVISIBLE);

        monthlyText.setText("");
        monthlyText.setVisibility(View.INVISIBLE);

        dailyStartText.setText("");
        dailyStartText.setVisibility(View.INVISIBLE);

        shortLine.setVisibility(View.INVISIBLE);

        dailyEndText.setText("");
        dailyEndText.setVisibility(View.INVISIBLE);

        annualSift = "";
        monthlySift = "";
        dailyStartSift = "";
        dailyEndSift = "";

        sureText.setEnabled(false);
    }

    private SetDateCallBack getDialogCallBack(int type){//0:年度 1:月度 2:每日统计起始时间 3:每日统计截止时间
        SetDateCallBack callBack;
        switch (type){
            case 0://年度
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {
                        Date mDate = new Date(date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDate);
                        annualText.setText(String.valueOf(cal.get(Calendar.YEAR)));
                        annualSift = String.valueOf(date);
                        sureText.setEnabled(true);
                    }
                };
                break;
            case 1://月度
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {
                        Date mDate = new Date(date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDate);
                        monthlyText.setText(String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.valueOf(cal.get(Calendar.MONTH) + 1));
                        monthlySift = String.valueOf(date);
                        sureText.setEnabled(true);
                    }
                };
                break;
            case 2://每日统计起始时间
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {
                        Date mDate = new Date(date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDate);
                        dailyStartText.setText(String.valueOf(cal.get(Calendar.YEAR)) + "-"
                                + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
                                + String.valueOf(cal.get(Calendar.DATE)));
                        dailyStartSift = String.valueOf(date);
                        if(dailyEndSift != null && dailyEndSift.length() > 0){
                            sureText.setEnabled(true);
                        }
                    }
                };
                break;
            case 3://每日统计截止时间
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {
                        Date mDate = new Date(date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDate);
                        dailyEndText.setText(String.valueOf(cal.get(Calendar.YEAR)) + "-"
                                + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
                                + String.valueOf(cal.get(Calendar.DATE)));
                        dailyEndSift = String.valueOf(date);
                        if(dailyStartSift != null && dailyStartSift.length() > 0){
                            sureText.setEnabled(true);
                        }
                    }
                };
                break;
            default:
                callBack = new SetDateCallBack() {
                    @Override
                    public void date(long date) {

                    }
                };
                break;
        }
        return callBack;
    }

}
