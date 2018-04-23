package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;

/**
 * Created by huiyuan on 2017/11/8.
 */

public class EnvChooseDialog extends Dialog implements View.OnClickListener{

    private EnvChoosenCallBack callBack;
    private TextView env_online_text,env_preview_text,cancel;
    private RelativeLayout env_choose_dialog;

    public interface EnvChoosenCallBack{
        public void OnEnvChoosen(String envStr);
    }

    public EnvChooseDialog(Context context,int theme,EnvChoosenCallBack choosenCallBack){
        super(context,theme);
        this.callBack = choosenCallBack;
        initView();
    }

    private void initView(){
        setContentView(R.layout.env_choose_dialog);
        env_online_text = (TextView) findViewById(R.id.env_online_text);
        env_preview_text = (TextView) findViewById(R.id.env_preview_text);
        cancel = (TextView) findViewById(R.id.cancel);
        env_choose_dialog = (RelativeLayout) findViewById(R.id.env_choose_dialog);

        env_online_text.setOnClickListener(this);
        env_preview_text.setOnClickListener(this);
        cancel.setOnClickListener(this);
        env_choose_dialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.env_online_text:
                if(callBack != null){
                    callBack.OnEnvChoosen(env_online_text.getText().toString());
                }
                dismiss();
                break;
            case R.id.env_preview_text:
                if(callBack != null){
                    callBack.OnEnvChoosen(env_preview_text.getText().toString());
                }
                dismiss();
                break;
            case R.id.cancel:
            case R.id.env_choose_dialog:
                dismiss();
                break;
            default:
                break;
        }
    }
}
