package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.ChooseConsumeTypeDialog;
import com.hongshi.wuliudidi.dialog.DateDialog;
import com.hongshi.wuliudidi.impl.SetDateCallBack;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import java.util.Calendar;
import java.util.Date;

import static com.hongshi.wuliudidi.CommonRes.TYPE_BOTTOM;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/6/7.
 */

public class FilterActivity extends Activity implements View.OnClickListener{

    private DiDiTitleView titleView;
    private RelativeLayout start_time_layout;
    private RelativeLayout end_time_layout;
    private RelativeLayout choose_type_layout;
    private TextView start_time_text,end_time_text,type_text;
    private Button sure_btn;

    private int account_value = 0;
    private String consumeType = "";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    consumeType = "全部";
                    type_text.setText(consumeType);
                    break;
                case 2:
                    consumeType = "消费";
                    type_text.setText(consumeType);
                    break;
                case 3:
                    if(account_value == 2){
                        consumeType = "提油";
                    }else if(account_value == 1){
                        consumeType = "提现";
                    }else {
                        consumeType = "";
                    }
                    type_text.setText(consumeType);
                    break;
                case 4:
                    consumeType = "结算";
                    type_text.setText(consumeType);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account_value = getIntent().getIntExtra("account_value",0);
        setContentView(R.layout.filter_activity);
        initView();
    }

    private void initView(){
        titleView = (DiDiTitleView)findViewById(R.id.title);
        titleView.setTitle("筛选条件");
        titleView.setBack(FilterActivity.this);

        start_time_layout = (RelativeLayout)findViewById(R.id.start_time_layout);
        end_time_layout = (RelativeLayout)findViewById(R.id.end_time_layout);
        choose_type_layout = (RelativeLayout)findViewById(R.id.choose_type_layout);
        start_time_text = (TextView)findViewById(R.id.start_time_text);
        end_time_text = (TextView)findViewById(R.id.end_time_text);
        type_text = (TextView)findViewById(R.id.type_text);
        sure_btn = (Button)findViewById(R.id.sure_btn);

        start_time_layout.setOnClickListener(this);
        end_time_layout.setOnClickListener(this);
        choose_type_layout.setOnClickListener(this);
        sure_btn.setOnClickListener(this);

    }

//    private void setBtnClickAble(boolean clickable){
//        start_time_layout.setClickable(clickable);
//        end_time_layout.setClickable(clickable);
//        choose_type_layout.setClickable(clickable);
//    }

    private DateDialog mDateDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_time_layout:
                mDateDialog = new DateDialog(FilterActivity.this, R.style.data_filling_dialog,
                        new SetDateCallBack() {
                            @Override
                            public void date(long date) {
                                Date mDate = new Date(date);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(mDate);
                                start_time_text.setText(String.valueOf(cal.get(Calendar.YEAR)) + "-"
                                        + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
                                        + String.valueOf(cal.get(Calendar.DATE)));
                            }
                        }, DateDialog.YearMonthDay, "请选择开始/结束时间");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.end_time_layout:
                mDateDialog = new DateDialog(FilterActivity.this, R.style.data_filling_dialog,
                        new SetDateCallBack() {
                            @Override
                            public void date(long date) {
                                Date mDate = new Date(date);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(mDate);
                                end_time_text.setText(String.valueOf(cal.get(Calendar.YEAR)) + "-"
                                        + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
                                        + String.valueOf(cal.get(Calendar.DATE)));
                            }
                        }, DateDialog.YearMonthDay, "请选择开始/结束时间");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.choose_type_layout:
                ChooseConsumeTypeDialog chooseConsumeTypeDialog = new ChooseConsumeTypeDialog(this,R.style.choose_consume_type_dialog,handler,account_value);
                chooseConsumeTypeDialog.setCanceledOnTouchOutside(true);
                UploadUtil.setAnimation(chooseConsumeTypeDialog, TYPE_BOTTOM, true);
                chooseConsumeTypeDialog.show();
                break;
            case R.id.sure_btn:
                Intent intent = new Intent();
                if("选择开始时间".equals(start_time_text.getText().toString())){
                    intent.putExtra("start_time","");
                }else {
                    intent.putExtra("start_time",start_time_text.getText().toString());
                }

                if("选择结束时间".equals(end_time_text.getText().toString())){
                    intent.putExtra("end_time","");
                }else {intent.putExtra("end_time",end_time_text.getText().toString());}

                if("选择类型".equals(type_text.getText().toString())){
                    intent.putExtra("type","");
                }else
                {intent.putExtra("type",type_text.getText().toString());}
                setResult(account_value,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
