package com.hongshi.wuliudidi.incomebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by zhuoran on 2016/4/21.
 *
 */
public class TransitRecordItemsLayout extends RelativeLayout{
    private Context mContext;
    private View mView;
    private final String record_data_url = "https://cz.redlion56.com" + "/gwcz/" + "carrier/app/bill/newBillList.do?";
    private final String queryType = "0";
    private PullToRefreshListView mPullToRefreshListView;
    private ListView transitRecordListView;
    private TransitRecordItemsAdapter adapter;
    private List<CarrierBillDetailVO> list;
//    private List<FeeVo> carribillTypeList = null;
    private int currentPage, itemCount, currentItemNum;
    private final int pageSize = 10;
    private String queryDateStart, queryDateEnd, queryTruckId;
    private ErrorDialog mErrorDialog = null;
    private Intent intent = null;

    private final String RefreshTransitDialy = "action.refresh.transit_daily";//刷新运输记录每日


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshTransitDialy)) {
                queryDateStart = intent.getStringExtra("queryDateStart");
                queryDateEnd = intent.getStringExtra("queryDateEnd");
                if(queryDateStart == null || queryDateEnd == null){
                    initQueryDateStr();
                }

                queryTruckId = intent.getStringExtra("queryTruckId");
                if(queryTruckId == null){
                    queryTruckId = "";
                }
                list.clear();
                getData(true, queryDateStart, queryDateEnd, queryTruckId);
            }
        }
    };

    public TransitRecordItemsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inflateView();
        init();
    }

    private void inflateView(){
        mView = View.inflate(mContext, R.layout.transit_record_items_fragment, null);
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.transit_record_pull);
        addView(mView);
    }

    private void init(){
        transitRecordListView = mPullToRefreshListView.getRefreshableView();
        list = new ArrayList<>();
        currentPage = -1;
        currentItemNum = 0;
        itemCount = 0;
        initQueryDateStr();
        queryTruckId = "";
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(mContext.getString(R.string.pull_up_load_more));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(mContext.getString(R.string.loading));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(mContext.getString(R.string.loosen_to_load_more));

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(mContext.getString(R.string.loosen_to_refresh));
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(mContext.getString(R.string.refreshing));
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(mContext.getString(R.string.pull_down_refresh));

        transitRecordListView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.clear();
                getData(true, queryDateStart, queryDateEnd, queryTruckId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (currentItemNum >= itemCount) {
                    if (mContext != null) {
                        Toast.makeText(mContext, mContext.getString(R.string.end_page), Toast.LENGTH_SHORT).show();
                    }
                    CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                    closeRefreshTask.execute();
                    return;
                } else {
                    getData(false, queryDateStart, queryDateEnd, queryTruckId);
                }
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshTransitDialy);
        if (mContext != null){
            mContext.registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        }
    }

    private void initQueryDateStr(){
        Date date1 = new Date(), date2 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
        queryDateStart = String.valueOf(cal.getTime().getTime());
        queryDateEnd = String.valueOf(date2.getTime());
    }

    private void getData(final boolean isrefresh, String queryDateStart, String queryDateEnd, String truckId){
        if(isrefresh){
            currentPage = -1;
            currentItemNum = 0;
        }

        AjaxParams params = new AjaxParams();
        params.put("orderByTruck", "false");
        params.put("queryType", queryType);
        params.put("queryDateStart", queryDateStart);
        params.put("queryDateEnd", queryDateEnd);
        if(truckId != null && !truckId.equals("")){
            params.put("truckId", truckId);
        }
        params.put("currentPage", String.valueOf(currentPage + 1));
        params.put("pageSize", String.valueOf(pageSize));

        sessionPost(mContext, record_data_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void data(String t) {
                mPullToRefreshListView.onRefreshComplete();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String all = jsonObject.getString("body");
                    TransitRecordOverviewModel overviewModel = JSON.parseObject(all, TransitRecordOverviewModel.class);
                    itemCount = overviewModel.getItemCount();
                    currentPage = overviewModel.getCurrentPage();

                    JSONObject  transitRecordInfo = new JSONObject(all);
                    JSONArray billDetailVoList = transitRecordInfo.optJSONArray("billDetailVoList");
                    int billDetailVoListLength = billDetailVoList.length();
                    for(int i = 0; i < billDetailVoListLength; i++){
                        JSONObject feeVoList = (JSONObject)billDetailVoList.get(i);
                        CarrierBillDetailVO detailVO = JSON.parseObject(billDetailVoList.get(i).toString(), CarrierBillDetailVO.class);
                        JSONArray feeVoArr = feeVoList.optJSONArray("carribillTypeList");
                        int size = feeVoArr.length();
                        List<FeeVo> carribillTypeList = new ArrayList<>();
                        for(int j = 0; j < size; j++){
                            FeeVo feeVo = JSON.parseObject(feeVoArr.get(j).toString(),FeeVo.class);
                            carribillTypeList.add(feeVo);
                        }
                        detailVO.setCarribillTypeList(carribillTypeList);
                        list.add(detailVO);

                    }
                    overviewModel.setBillDetailVoList(list);

                    List<CarrierBillDetailVO> newList = overviewModel.getBillDetailVoList();
                    if(newList != null){
                        if(isrefresh){
                            list = newList;
                            currentItemNum = newList.size();
                            if(mContext != null) {
                                adapter = new TransitRecordItemsAdapter(mContext, list);
                                transitRecordListView.setAdapter(adapter);
                            }
                        }else{
                            list.addAll(newList);
                            currentItemNum += newList.size();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    transitRecordListView.setAdapter(null);
                }
            }
        },intent);
    }

    private void sessionPost(final Context mContext, String url,
                             AjaxParams params, final AfinalHttpCallBack mCallBack,Intent intent) {
        FinalHttp mFinalHttp = new FinalHttp();
        String sessionID = null;
        if(intent != null) sessionID = intent.getStringExtra("sessionId");
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
//        Log.d("huiyuan", "url=" + url + params);
        mFinalHttp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    if (jsonObject != null) {
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            mCallBack.data(t);
                        } else {
                            String errMsg = jsonObject.getString("errMsg");
                            String errCode = jsonObject.getString("errCode");
                            boolean errSerious = jsonObject.getBoolean("errSerious");
                            if (errSerious) {
                                //要弹出错误框
                                if (mErrorDialog == null) {
                                    mErrorDialog = new ErrorDialog(mContext, R.style.data_filling_dialog, errMsg);
                                }
                                if (!mErrorDialog.isShowing()) {
                                    mErrorDialog.show();
                                }
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
    private SSLSocketFactory setCertificates(InputStream...certificates) {
        SSLSocketFactory sslSocketFactory = HttpsUtilsForFinalHttp.getSslSocketFactory(certificates, null, null);
        return sslSocketFactory;
    }

    public void setContextIntent(Intent intent){
        this.intent = intent;
    }
}

