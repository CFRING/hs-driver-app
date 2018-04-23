package com.hongshi.wuliudidi.myroute;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huiyuan on 2016/6/17.
 */
public class MyRouteActivity extends Activity {

    private ErrorDialog mErrorDialog;
    private DiDiTitleView titleView;
    private RelativeLayout my_route_empty_layout;
    private ListView my_route_listView;
    private Button  addBtn;
    private ListItemDeletingDialog mDeletingDialog;
    private MyRouteListAdapter adapter;
    private List<MyRouteModel> myRouteDataList;
    private String myRouteUrl = CommonRes.HOST +"carrier/commonLine/list.do";
    private String deleteMyRouteUrl = CommonRes.HOST + "carrier/commonLine/delete.do";
    private String addRouteAction = "add_route_suc";
    private String driverHasCommonLines = "has_common_line";
    private String sessionID;
    private String userRole;

    private BroadcastReceiver addRouteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(addRouteAction)){
                getMyRouteData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_route);

        HashMap<String,String> params = (HashMap)getIntent().getExtras().get("params");
        sessionID = params.get("sessionId");
        userRole = params.get("userRole");
        Log.d("huiyuan","sessionID = " + sessionID
                + " userRole = " + userRole);
        if("sj".equals(userRole)){
            CommonRes.HOST = CommonRes.DRIVER_HOST;
        }else {
            CommonRes.HOST = CommonRes.CHE_ZHU_HOST;
        }
        myRouteUrl = CommonRes.HOST +"carrier/commonLine/list.do";
        deleteMyRouteUrl = CommonRes.HOST + "carrier/commonLine/delete.do";
        initView();
        getMyRouteData();
        IntentFilter filter = new IntentFilter();
        filter.addAction(addRouteAction);
        registerReceiver(addRouteReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(addRouteReceiver);
    }

    private void initView(){
        titleView = (DiDiTitleView)findViewById(R.id.my_route_title);
        my_route_empty_layout = (RelativeLayout)findViewById(R.id.my_route_empty_layout);
        my_route_listView = (ListView)findViewById(R.id.my_route_list);
        addBtn = (Button)findViewById(R.id.add_btn);

        myRouteDataList = new ArrayList<>();
        adapter = new MyRouteListAdapter(MyRouteActivity.this,myRouteDataList);
        my_route_listView.setAdapter(adapter);

        my_route_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteRoute(position);
                return true;
            }
        });
        titleView.setTitle(getString(R.string.my_route_title));
        titleView.setBack(MyRouteActivity.this);
        titleView.getRightTextView().setText(getString(R.string.add_route_title_btn_txt));
        titleView.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myRouteDataList.size() >= 5) {
                    CancelDialog dialog = new CancelDialog(MyRouteActivity.this, R.style.data_filling_dialog, false);
                    dialog.setHint(getString(R.string.my_route_count_exceeded_tip));
                    dialog.setLeftText(getString(R.string.sure));
                    dialog.getRightView().setVisibility(View.GONE);
                    dialog.show();
                } else gotoAddRouteActivity();
            }
        });
        titleView.getRightTextView().setVisibility(View.GONE);
    }

    private void getMyRouteData(){
        final PromptManager promptManager = new PromptManager();
        promptManager.showProgressDialog1(MyRouteActivity.this,getString(R.string.my_route_loading));
        my_route_empty_layout.setVisibility(View.GONE);
        myRouteDataList.clear();
        AjaxParams params = new AjaxParams();
        sessionPost(MyRouteActivity.this, myRouteUrl, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan", "route list json = " + t);
                promptManager.closeProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    boolean isSuccess = jsonObject.optBoolean("success");
                    JSONArray routeList = jsonObject.optJSONArray("body");

                    if (isSuccess) {
                        if (routeList == null || routeList.length() <= 0) {
                            Intent intent = new Intent();
                            intent.setAction(driverHasCommonLines);
                            sendBroadcast(intent);
                            setEmptyView();
                        } else {
                            for (int i = 0; i < routeList.length(); i++) {
                                MyRouteModel model = JSON.parseObject(routeList.get(i).toString(), MyRouteModel.class);
                                myRouteDataList.add(model);
                            }
                            my_route_listView.setVisibility(View.VISIBLE);
                            titleView.getRightTextView().setVisibility(View.VISIBLE);
                            my_route_empty_layout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction(driverHasCommonLines);
                            sendBroadcast(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                setEmptyView();
//                Log.d("huiyuan","err msg = " + errMsg);
                promptManager.closeProgressDialog();
            }
        });
    }

    private void gotoAddRouteActivity(){
        Intent addRouteIntent = new Intent(MyRouteActivity.this,AddRouteActivity.class);
        addRouteIntent.putExtra("sessionId", sessionID);
        startActivity(addRouteIntent);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 30001:
                    String deletedRouteId;
                    try {
                        deletedRouteId = msg.getData().getString("itemId");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    AjaxParams params = new AjaxParams();
                    params.put("id", deletedRouteId);
                    Log.d("huiyuan","delete route id = " + deletedRouteId);
                    sessionPost(MyRouteActivity.this, deleteMyRouteUrl, params, new ChildAfinalHttpCallBack() {
                        @Override
                        public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                            Toast.makeText(MyRouteActivity.this, errMsg, Toast.LENGTH_LONG);
                        }

                        @Override
                        public void data(String t) {
//                            adapter.deleteItem(deleteItemPosition);
                            getMyRouteData();
                        }
                    });

                default:
                    break;
            }
        }
    };

    public void deleteRoute(int position){
        mDeletingDialog = new ListItemDeletingDialog(MyRouteActivity.this, R.style.data_filling_dialog, mHandler);
        mDeletingDialog.setCanceledOnTouchOutside(true);
        mDeletingDialog.setText(getString(R.string.my_route_delete), getString(R.string.my_route_cancel));
        mDeletingDialog.setItemId(myRouteDataList.get(position).getId());
        mDeletingDialog.setMsgNum(30001);
        Util.setAnimation(mDeletingDialog, CommonRes.TYPE_BOTTOM, false);
        mDeletingDialog.show();
    }

    private void setEmptyView(){
        my_route_empty_layout.setVisibility(View.VISIBLE);
        my_route_listView.setVisibility(View.GONE);
        titleView.getRightTextView().setVisibility(View.GONE);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddRouteActivity();
            }
        });
    }

    private void sessionPost(final Context mContext, String url,
                             AjaxParams params, final AfinalHttpCallBack mCallBack) {
        FinalHttp mFinalHttp = new FinalHttp();
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
        Log.d("huiyuan"," url = " + url);
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
}
