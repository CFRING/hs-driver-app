package com.hongshi.wuliudidi.cashier.okhttp.builder;


import com.hongshi.wuliudidi.cashier.okhttp.OkHttpUtils;
import com.hongshi.wuliudidi.cashier.okhttp.request.OtherRequest;
import com.hongshi.wuliudidi.cashier.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
