package com.hongshi.wuliudidi.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.hongshi.wuliudidi.CommonRes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huiyuan on 2016/9/19.
 */
public class SmsObserver extends ContentObserver{
    private Context mContext;
    private Handler mHandler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        if (uri.toString().equals("content://sms/raw")) {
            return;
        }

        String code = "";

        Uri inboxUri = Uri.parse("content://sms/inbox");

        // 按短信ID倒序排序，避免修改手机时间数据不正确
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null,
                null, "_id desc");
        if (c != null) {
            if (c.moveToFirst()) {
                // 发送人号码
                String address = c.getString(c.getColumnIndex("address"));
                // 短信内容
                String body = c.getString(c.getColumnIndex("body"));

                // 读取红狮物流短信平台的短信内容
                if (!address.equals("1069029924629796")) {
                    return;
                }

                // 正则表达式
                Pattern pattern = Pattern.compile("(\\d{4})");
                // 匹配短信内容
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    code = matcher.group(0);
                    mHandler.obtainMessage(CommonRes.SMS_PASSWORD, code)
                            .sendToTarget();
                }
            }
        }
    }
}
