package com.hongshi.wuliudidi.cashier.okhttp.callback;

import okhttp3.Call;

/**
 * Created by he on 16/04/24.
 */
public abstract class RequestCallback {
    public void Success(String body) {}
    public void onError(Call call, Exception e) {}
    public void onError(String errMsg) {}
}
