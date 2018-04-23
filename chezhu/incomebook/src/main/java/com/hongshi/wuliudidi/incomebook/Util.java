package com.hongshi.wuliudidi.incomebook;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huiyuan on 2016/5/26.
 */
public class Util {
    /**
     * 时间精确到分钟
     * @param date
     * @return
     */
    public static String formatDateMinute(Date date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr =format.format(date);
        return dateStr;
    }

    public static String formatDoubleToString(double amount, String unit){
        if(unit == null){
            return String.valueOf(amount);
        }
        if(unit.equals("元") || unit.equals("吨") || unit.equals("立方米") || unit.equals("T") || unit.equals("M3")){
            DecimalFormat d = new DecimalFormat("#0.00");
            String format = d.format(amount);
            return format;
        }else{
            int i = (int) amount;
            return String.valueOf(i);
        }
    }

    public static void setText(Context mContext,String str,String str_color,TextView t,int id){
        int fstart=str.indexOf(str_color);
        int fend=fstart+str_color.length();
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        if(fstart >= 0 && fend <= str.length()){
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(id)),fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            t.setText(style);
        }else{
            t.setText(str);
        }
    }

    /**
     * 设置月份字体颜色改变
     * @param context 上下文
     * @param str 总文本
     * @param mouthStr 月文本
     * @param tv TextView
     * @param colorId 颜色Id
     */
    public static void setMonthText(Context context,String str,String mouthStr,TextView tv,int colorId){
        int start = str.indexOf("年") + 1;
        int end = start + mouthStr.length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        if(start >= 0 && end <= str.length()){
            style.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorId)),start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv.setText(style);
        }else{
            tv.setText(str);
        }
    }

    public static void sessionPost(final Context mContext, String url,
                             AjaxParams params, final AfinalHttpCallBack mCallBack,String sessionID) {
        FinalHttp mFinalHttp = new FinalHttp();
//        Log.d("huiyuan", "session id in income book = " + sessionID);
        mFinalHttp.configCharset("UTF-8");
        mFinalHttp.configTimeout(10000);// 超时时间
        try {
            mFinalHttp.configSSLSocketFactory(setCertificates(mContext.getAssets().open("HSWLROOTCAforInternalTest.crt"), mContext.getAssets().open("HSWLROOTCA.crt")));//设置https请求，暂时为单向验证
        }catch(IOException e){
            e.printStackTrace();
        }
        if(sessionID == null){
            sessionID = "";
        }
        mFinalHttp.addHeader("sessionId", sessionID);
        Log.d("huiyuan", "url=" + url + params);
        mFinalHttp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    if (jsonObject != null) {
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
//                            Log.d("huiyuan","income book data = " + t);
                            mCallBack.data(t);
                        } else {
                            String errMsg = jsonObject.getString("errMsg");
                            String errCode = jsonObject.getString("errCode");
                            boolean errSerious = jsonObject.getBoolean("errSerious");
                            if (errSerious) {
                                //要弹出错误框
                                Toast.makeText(mContext,errMsg,Toast.LENGTH_LONG);
//                                if (mErrorDialog == null) {
//                                    mErrorDialog = new ErrorDialog(mContext, R.style.data_filling_dialog, errMsg);
//                                }
//                                if (!mErrorDialog.isShowing()) {
//                                    mErrorDialog.show();
//                                }
                            } else {
                                //特殊处理错误框
//                                ErrorCodeUtil.errorCode(errCode, mContext, errMsg);//处理请求错误码
//                                Log.d("huiyuan","error msg = " + errMsg);
                                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
                            }
                            try {
                                ((ChildAfinalHttpCallBack) mCallBack).onFailure("", errMsg, false);
                            } catch (Exception e) {
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//					mCallBack.data("");
                }
            }

            // 加载失败
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(mContext, mContext.getString(R.string.request_err_tips) + strMsg, Toast.LENGTH_SHORT).show();
                try {
                    ((ChildAfinalHttpCallBack) mCallBack).onFailure("", strMsg, false);
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * for https-way authentication
     *
     * @param certificates
     */
    public static SSLSocketFactory setCertificates(InputStream...certificates) {
        SSLSocketFactory sslSocketFactory = HttpsUtilsForFinalHttp.getSslSocketFactory(certificates, null, null);
        return sslSocketFactory;
    }
}
