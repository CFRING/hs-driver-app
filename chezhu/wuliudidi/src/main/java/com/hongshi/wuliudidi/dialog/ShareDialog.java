package com.hongshi.wuliudidi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.share.Share;

public class ShareDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout mCancel;
    private LinearLayout mQQ, mWeixin, mWXTimeLine;
    private Context context;
//    private String shareUrl = GloableParams.WEB_URL + "index.html";
    private String shareUrl = "http://cz.redlion56.com/share/supply.html?id=";
    private String shareContent = "";

    public ShareDialog(Context context, int theme,String id,String sharedContent) {
        super(context,theme);
        this.context = context;
        shareUrl = shareUrl + id;
        shareContent = sharedContent;

        if(shareContent == null){
            shareContent = "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        mCancel = (RelativeLayout) findViewById(R.id.cancel);
        mQQ= (LinearLayout) findViewById(R.id.qq);
        mWeixin = (LinearLayout) findViewById(R.id.weixin);
        mWXTimeLine = (LinearLayout) findViewById(R.id.weixin_circle);

        mCancel.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mWeixin.setOnClickListener(this);
        mWXTimeLine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weixin:
               Share.shareWX(context, shareUrl, context.getResources().getString(R.string.share_title),
                       shareContent);
                cancel();
                break;
            case R.id.weixin_circle:
                Share.shareWXTimeLine(context, shareUrl, context.getResources().getString(R.string.share_title),
                        shareContent);
                cancel();
                break;
            case R.id.qq:
                Share.share(context, shareUrl, context.getResources().getString(R.string.share_title),
                        shareContent);
                cancel();
                break;
            case R.id.cancel:
                cancel();
                break;
            default:
                break;
        }
    }
}
