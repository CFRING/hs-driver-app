package com.hongshi.wuliudidi.incomebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONArray;
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
public class TransitRecordStatisticLayout extends RelativeLayout {
    private Context mContext;
    private View mView;
    private final String record_data_url = "https://cz.redlion56.com" + "/gwcz/" + "carrier/app/bill/newBillList.do?";
    private String queryType = "1", queryTruckId, queryDateStart;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView transitRecordListView;
    private ErrorDialog mErrorDialog = null;
    private TransitRecordStatisticAdapter adapter;
    private List<CarrierBillDetailVO> list;
    private List<FeeVo> carribillTypeList = null;
    private final int currentPage = 0;
    private final int pageSize = 10;
    private TextView timeRangeText, totalIncomeText, transitTruckText, transitRecordNumText;
    private final String RefreshTransitAnnual = "action.refresh.transit_annual";//刷新运输记录年度
    private final String RefreshTransitMonthly = "action.refresh.transit_monthly";//刷新运输记录月度


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshTransitAnnual)) {
                if(!queryType.equals("2")){
                    return;
                }
                queryDateStart = intent.getStringExtra("queryDateStart");
                if(queryDateStart == null){
                    Date date = getLastMonthDate(queryType);
                    queryDateStart = String.valueOf(date.getTime());
                }

                queryTruckId = intent.getStringExtra("queryTruckId");
                if(queryTruckId == null){
                    queryTruckId = "";
                }
                list.clear();
                getData(true, queryType, queryDateStart, queryTruckId);
            }else if(action.equals(RefreshTransitMonthly)){
                if(!queryType.equals("1")){
                    return;
                }
                queryDateStart = intent.getStringExtra("queryDateStart");
                if(queryDateStart == null){
                    Date date = getLastMonthDate(queryType);
                    queryDateStart = String.valueOf(date.getTime());
                }

                queryTruckId = intent.getStringExtra("queryTruckId");
                if(queryTruckId == null){
                    queryTruckId = "";
                }
                list.clear();
                getData(true, queryType, queryDateStart, queryTruckId);
            }
        }
    };

    private Date getLastMonthDate(String queryType){
        Date date = new Date();
        if(queryType != null && queryType.equals("2")){
            return date;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, cal.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public TransitRecordStatisticLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inflateView();
        init();
    }

    private void inflateView(){
        mView = View.inflate(mContext, R.layout.transit_record_statistic_fragment, null);
        timeRangeText = (TextView) mView.findViewById(R.id.time_range_text);
        totalIncomeText = (TextView) mView.findViewById(R.id.total_income_text);
        transitTruckText = (TextView) mView.findViewById(R.id.transit_truck_text);
        transitRecordNumText = (TextView) mView.findViewById(R.id.transit_record_num_text);
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.transit_statistic_pull);
        addView(mView);
    }

    private void init(){
        queryTruckId = "";
        Date date = getLastMonthDate(queryType);
        queryDateStart = String.valueOf(date.getTime());
        list = new ArrayList<>();
        transitRecordListView = mPullToRefreshListView.getRefreshableView();

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(mContext.getString(R.string.pull_up_load_more));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(mContext.getString(R.string.loading));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(mContext.getString(R.string.loosen_to_load_more));

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(mContext.getString(R.string.loosen_to_refresh));
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(mContext.getString(R.string.refreshing));
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(mContext.getString(R.string.pull_down_refresh));

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.clear();
                getData(true, queryType, queryDateStart, queryTruckId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(mContext, mContext.getString(R.string.end_page), Toast.LENGTH_SHORT).show();
                CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                closeRefreshTask.execute();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshTransitMonthly);
        intentFilter.addAction(RefreshTransitAnnual);
        mContext.registerReceiver(mRefreshBroadcastReceiver, intentFilter);

    }

    private void getData(boolean isrefresh, String queryType, String queryDateStart, String queryTruckId){
        if(mContext == null){
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put("orderByTruck", "false");
        params.put("queryType", queryType);
        params.put("queryDateStart", queryDateStart);
        if(queryTruckId != null && !queryTruckId.equals("")){
            params.put("truckId", queryTruckId);
        }
        params.put("currentPage", String.valueOf(currentPage));
        params.put("pageSize", String.valueOf(pageSize));
        sessionPost(mContext, record_data_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(mContext,"请求失败: " + errMsg,Toast.LENGTH_LONG).show();
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void data(String t) {
                mPullToRefreshListView.onRefreshComplete();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String all = jsonObject.optString("body");
                    TransitRecordOverviewModel overviewModel = JSON.parseObject(all, TransitRecordOverviewModel.class);

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

                    dataFillIn(overviewModel);
                } catch (Exception e) {
                    e.printStackTrace();
                    transitRecordListView.setAdapter(null);
                    Log.d("huiyuan",e.getMessage());
                }
            }
        });
    }

    private void dataFillIn(TransitRecordOverviewModel overviewModel){
        if(overviewModel == null || mContext == null){
            return;
        }

        Date date = overviewModel.getQueryDateS();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(queryType.equals("1")){
            if(overviewModel.getTruckId() != null && overviewModel.getTruckId().length() > 0){
                timeRangeText.setText(String.valueOf(cal.get(Calendar.YEAR)) + mContext.getString(R.string.year));
            }else{
                String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
                String timeRange = String.valueOf(cal.get(Calendar.YEAR)) + mContext.getString(R.string.year) + month + mContext.getString(R.string.month);
                Util.setMonthText(mContext, timeRange, month, timeRangeText, R.color.theme_color);
            }
        }else if(queryType.equals("2")){
            String year = String.valueOf(cal.get(Calendar.YEAR)) + mContext.getString(R.string.year);
            Util.setText(mContext, year, String.valueOf(cal.get(Calendar.YEAR)), timeRangeText, R.color.theme_color);
        }

        String totalIncome = mContext.getString(R.string.total_income) + String.valueOf(overviewModel.getSumMoney()) + mContext.getString(R.string.yuan);
        Util.setText(mContext, totalIncome, String.valueOf(overviewModel.getSumMoney()), totalIncomeText, R.color.theme_color);

        if(overviewModel.getTruckId() != null && overviewModel.getTruckId().length() > 0){
            List<CarrierBillDetailVO> tempList = overviewModel.getBillDetailVoList();
            if(tempList != null && tempList.size() > 0){
                transitTruckText.setText(tempList.get(0).getTruckNO());
            }
        }else{
            String transitTruckNum = mContext.getString(R.string.total_transit_truck) + overviewModel.getSumTruck() + mContext.getString(R.string.liang);
            Util.setText(mContext, transitTruckNum, String.valueOf(overviewModel.getSumTruck()), transitTruckText, R.color.theme_color);
        }

        String transitRecordNum = mContext.getString(R.string.transit_bill_record) + overviewModel.getSumRecord() + mContext.getString(R.string.ci);
        Util.setText(mContext, transitRecordNum, String.valueOf(overviewModel.getSumRecord()), transitRecordNumText, R.color.theme_color);

//        list = overviewModel.getBillDetailVoList();
        if(list != null && list.size() > 0){
            adapter = new TransitRecordStatisticAdapter(mContext, list, String.valueOf(overviewModel.getQueryType()));
            String dateS, dateE;
            Date mDate = new Date();
            if(overviewModel.getQueryDateS() != null) {
                dateS = String.valueOf(overviewModel.getQueryDateS().getTime());
            }else{
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);

                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
                dateS = String.valueOf(calendar.getTime().getTime());
            }

            transitRecordListView.setAdapter(adapter);
        }else{
            transitRecordListView.setAdapter(null);
        }
    }

    /**
     * @param queryType
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    private void sessionPost(final Context mContext, String url,
                             AjaxParams params, final AfinalHttpCallBack mCallBack) {
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

    private Intent intent = null;
    public void setContextIntent(Intent intent){
        this.intent = intent;
    }
}

