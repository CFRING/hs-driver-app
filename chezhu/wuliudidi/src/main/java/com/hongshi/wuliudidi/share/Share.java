package com.hongshi.wuliudidi.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;

/**
 * Created on 2016/3/28.
 */
public class Share {

    private static Tencent mTencent;
    private static final String wxAppId = "wx73a4f1869122a4ad";

    public static void share(Context mContext, String webUrl,String title,String content) {
        mTencent = Tencent.createInstance("100371282", mContext);

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://www.redlion56.com/images/logo.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getString(R.string.app_name));

        mTencent.shareToQQ((Activity) mContext, params, new BaseUiListener());

    }

    public static void shareWX(Context mContext, String webUrl, String title, String content) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webUrl;
        WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
        mediaMessage.title = title;
        mediaMessage.description = content;
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_icon);
        mediaMessage.thumbData = bmpToByteArray(thumb,false);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";

        req.message = mediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        IWXAPI api = WXAPIFactory.createWXAPI(mContext, wxAppId, true);
        api.registerApp(wxAppId);

        api.sendReq(req);
    }

    public static void shareWXTimeLine(Context mContext, String webUrl, String title, String content){
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webUrl;
        WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
        mediaMessage.title = title;
        mediaMessage.description = content;
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_icon);
        mediaMessage.thumbData = bmpToByteArray(thumb,false);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";

        req.message = mediaMessage;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        IWXAPI api = WXAPIFactory.createWXAPI(mContext, wxAppId, true);
        api.registerApp(wxAppId);
        if(api.getWXAppSupportAPI() > 0x21020001){
            api.sendReq(req);
        }else {
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.weixin_timeline_share_not_support));
        }
    }

    public static void onActivityResultData(int requestCode, int resultCode, Intent data, BaseUiListener baseUiListener){
        if (null != mTencent) {
            Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListener);
        }
    }

    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
