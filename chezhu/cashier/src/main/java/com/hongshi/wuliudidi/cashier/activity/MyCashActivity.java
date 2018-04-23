package com.hongshi.wuliudidi.cashier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.hongshi.wuliudidi.cashier.R;
import com.hongshi.wuliudidi.cashier.okhttp.OkHttpUtils;
import com.hongshi.wuliudidi.cashier.okhttp.callback.StringCallback;
import com.hongshi.wuliudidi.cashier.view.SdkTitleView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


public class MyCashActivity extends Activity {

    public static String BASE_HOSE = "http://cz.redlion56.com";
    public static String HOST=BASE_HOSE + "/gwcz/";
    private String upload_url = HOST+ "commonservice/basic/featchInfo.do?";
    SdkTitleView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cash);
        mTitle = (SdkTitleView) findViewById(R.id.tile);
        mTitle.setTitle("我的收银台");
        Map<String, String> params = new HashMap<String, String>();
        OkHttpUtils.post().url(upload_url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {


            }
            @Override
            public void onResponse(String response) {
                Log.i("lihe","结果==="+response);
//                UpdateInfo mUpdateInfo = new Gson().fromJson(response,UpdateInfo.class);
            }
        });
    }
}
