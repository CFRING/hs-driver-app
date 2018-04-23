package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.EnvChooseDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.UploadUtil;

import net.tsz.afinal.http.AjaxParams;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/11/8.
 */

public class EnvChangeActivity extends Activity implements View.OnClickListener,
        EnvChooseDialog.EnvChoosenCallBack {

    private ImageView cancel_btn;
    private RelativeLayout env_container;
    private TextView env_text;
    private EditText password_editor;
    private Button ensure_btn;
    private String url = GloableParams.HOST + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_change);

        initView();
    }

    private void initView(){
        cancel_btn = (ImageView) findViewById(R.id.cancel_btn);
        env_container = (RelativeLayout) findViewById(R.id.env_container);
        env_text = (TextView) findViewById(R.id.env_text);
        password_editor = (EditText) findViewById(R.id.password_editor);
        ensure_btn = (Button) findViewById(R.id.ensure_btn);

        cancel_btn.setOnClickListener(this);
        env_container.setOnClickListener(this);
        ensure_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.env_container:
                EnvChooseDialog envChooseDialog =
                        new EnvChooseDialog(EnvChangeActivity.this,R.style.data_filling_dialog, this);
                UploadUtil.setAnimation(envChooseDialog, CommonRes.TYPE_BOTTOM, true);
                envChooseDialog.show();
                break;
            case R.id.ensure_btn:
                setEnv();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnEnvChoosen(String envStr) {
        if(envStr != null) env_text.setText(envStr);
    }

    private void setEnv(){
        AjaxParams params = new AjaxParams();
        params.put("password",password_editor.getText().toString());
        params.put("env",env_text.getText().toString());

        DidiApp.getHttpManager().sessionPost(this, url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {

                String envStr = env_text.getText().toString();
                Toast.makeText(EnvChangeActivity.this,"已切换至" + envStr,Toast.LENGTH_LONG).show();

                if("线上环境".equals(envStr)){
                    GloableParams.EVN_HOST = "redlion56.com";
                }else if("预发环境".equals(envStr)){
                    GloableParams.EVN_HOST = "redlion56.com:8097";
                }else {
                    GloableParams.EVN_HOST = "redlion56.com";
                }
                finish();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }
}
