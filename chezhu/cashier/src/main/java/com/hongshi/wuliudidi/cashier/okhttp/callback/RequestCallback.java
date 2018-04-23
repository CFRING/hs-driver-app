package com.hongshi.wuliudidi.cashier.okhttp.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by he on 16/04/24.
 */
public abstract class RequestCallback {
    public void Success(String body) {}
    public void onError(Call call, Exception e) {}
    public void onError(String errMsg) {}
}
