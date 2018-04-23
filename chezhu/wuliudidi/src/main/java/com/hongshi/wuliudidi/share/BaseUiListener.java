package com.hongshi.wuliudidi.share;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created on 2016/3/23.
 */
public class BaseUiListener implements IUiListener {
//    @Override
//    public void onComplete(JSONObject response) {
//        mBaseMessageText.setText("onComplete:");
//        mMessageText.setText(response.toString());
////        doComplete(response);
//    }

    protected void doComplete(JSONObject values) {
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError e) {
    }

    @Override
    public void onCancel() {
    }
}

