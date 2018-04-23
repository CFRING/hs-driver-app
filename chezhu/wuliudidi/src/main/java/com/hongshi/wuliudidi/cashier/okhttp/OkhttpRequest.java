package com.hongshi.wuliudidi.cashier.okhttp;

import android.content.Context;
import android.widget.Toast;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.cashier.CommonRes;
import com.hongshi.wuliudidi.cashier.okhttp.callback.Callback;
import com.hongshi.wuliudidi.cashier.okhttp.callback.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by he on 2016/4/25.
 */
public class OkhttpRequest {
    /**
     *
     * @param context          上下文对象
     * @param url              请求的URL
     * @param params           请求参数map
     * @param mRequestCallback 回调callback
     */
    public static void postString(final Context context, String url, Map<String, String> params, final RequestCallback mRequestCallback){
        try {
            OkHttpUtils.getInstance().setCertificates(context.getAssets().open("HSWLROOTCAforInternalTest.crt"),context.getAssets().open("HSWLROOTCA.crt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.post().url(url).params(params).build().execute(new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response) throws Exception {
                String body = response.body().string();
                return body;
            }

            @Override
            public void onError(Call call, Exception e) {
                mRequestCallback.onError(call,e);
                mRequestCallback.onError("request_timeout"+e.toString());
                Toast.makeText(context,context.getString(R.string.error_msg),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if (response != null){
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        boolean isSuccess = obj.getBoolean("success");
                        String errorMsg = obj.getString("errMsg");
                        String body = obj.getString("body");
                        String errCode = obj.getString("errCode");
                        if (isSuccess){
                            mRequestCallback.Success(body);
                        }else{
                            if(errCode.equals(CommonRes.Y_ERRORCODE)){
                                mRequestCallback.onError(CommonRes.Y_ERRORCODE+errorMsg);
                            }else{
                                mRequestCallback.onError(errorMsg);
                            }
                           if(!"140001".equals(errCode)){
                               Toast.makeText(context,errorMsg,Toast.LENGTH_SHORT).show();
                           }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mRequestCallback.onError(context.getString(R.string.error_msg));
                        mRequestCallback.onError(context.getString(R.string.error_msg));
                    }
                }else{
                    mRequestCallback.onError(context.getString(R.string.error_msg));
                }
            }
        });
    }
}
