package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.hongshi.wuliudidi.R;

/**
 * Created by huiyuan on 2017/2/16.
 */

public class AdvertisementActivity extends Activity {

    private ImageView imageView;
    private Button cancel,enter;
    private String url = "";
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisement_view);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        imageView = (ImageView) findViewById(R.id.image_src);
        cancel = (Button) findViewById(R.id.cancel_btn);
        enter = (Button) findViewById(R.id.enter);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        imageView.setImageResource(R.drawable.g_1);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putString("url",url);
                mBundle.putString("title",title);
                Intent intent = new Intent(AdvertisementActivity.this, WebViewWithTitleActivity.class);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
}
