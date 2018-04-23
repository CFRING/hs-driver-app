package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.IntegralDetailsAdapter;
import com.hongshi.wuliudidi.dialog.DateDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.SetDateCallBack;
import com.hongshi.wuliudidi.model.IntegralDetailModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.CloseRefreshTask;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.hongshi.wuliudidi.CommonRes.TYPE_BOTTOM;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/11/20.
 * 积分明细
 */

public class IntegralDetailActivity extends Activity implements View.OnClickListener{

    private DiDiTitleView integral_detail_title;
    private TextView integral_text,date_text,integral_rules;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView listView;
    private ImageView time_shift,question_img;
    private DateDialog mDateDialog;
    private IntegralDetailsAdapter integralDetailsAdapter;
    private String integrals = "";
    //积分规则链接
    private String integral_detail_url = "http://cz.redlion56.com/app/integralRule.html";
    private boolean isFirstRequest = true;
    private boolean isEnd = false;
    private int currentPage = 1;
    private List<IntegralDetailModel> integralDetailModelList = new ArrayList<>();
    //积分明细链接
    private String detail_url = GloableParams.HOST + "credit/integral/userIntegral/integralRecord.do";
    //筛选时间
    private String dateText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        integrals = getIntent().getStringExtra("integrals");
        setContentView(R.layout.activity_integral_detail);

        initView();
    }

    private void initView(){
        integral_detail_title = (DiDiTitleView) findViewById(R.id.integral_detail_title);
        integral_detail_title.setTitle("积分明细");
        integral_detail_title.setBack(this);

        date_text = (TextView) findViewById(R.id.date_text);
        integral_text = (TextView) findViewById(R.id.integral_text);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.details_list);
        time_shift = (ImageView) findViewById(R.id.time_shift);
        question_img = (ImageView) findViewById(R.id.question_img);
        integral_rules = (TextView) findViewById(R.id.integral_rules);

        question_img.setOnClickListener(this);
        integral_rules.setOnClickListener(this);
        time_shift.setOnClickListener(this);

        if(integrals != null) {integral_text.setText(integrals);}

        listView = mPullToRefreshListView.getRefreshableView();
        integralDetailsAdapter = new IntegralDetailsAdapter(IntegralDetailActivity.this,integralDetailModelList);
        listView.setAdapter(integralDetailsAdapter);

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多");

        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("松开刷新");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("刷新中");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新");

        listView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPullToRefreshListView.isRefreshing()) {
                    if (mPullToRefreshListView.isHeaderShown()){
                        getIntegralDetails(true);
                    } else if (mPullToRefreshListView.isFooterShown()) {
                        // 加载更多
                        if(isEnd){
                            Toast.makeText(IntegralDetailActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
                            closeRefreshTask.execute();
                            return;
                        }
                        currentPage = currentPage + 1;
                        getIntegralDetails(false);
                    }
                }
            }
        });

        String integrals = getIntent().getStringExtra("integrals");
        if(integrals != null){
            integral_text.setText(integrals);
        }
        getIntegralDetails(true);
    }

    private void getIntegralDetails(final boolean isRefresh){

        if(isRefresh){
            currentPage = 1;
        }

        AjaxParams params = new AjaxParams();
        params.put("currentPage",""+ currentPage);
        params.put("pageSize","10");
        params.put("queryMonth",dateText);

        DidiApp.getHttpManager().sessionPost(IntegralDetailActivity.this, detail_url, params, new ChildAfinalHttpCallBack() {

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(IntegralDetailActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }

            @Override
            public void data(String t) {
                try {
                    if(isRefresh){
                        integralDetailModelList.clear();
                        isEnd = false;
                        mPullToRefreshListView.onRefreshComplete();
                    }
                    JSONObject jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");

                    if(isFirstRequest){
                        isFirstRequest = false;
                        String systemTime = body.optString("systemTime");
                        String date[] = systemTime.split("-");
                        if(date != null && date.length > 1){
                            date_text.setText(date[0] + "年" + date[1] + "月");
                        }
                    }
                    isEnd = body.getBoolean("end");
                    currentPage = body.getInt("currentPage");
                    String items = body.optString("items");
                    List<IntegralDetailModel> integralDetailModels = JSON.parseArray(items,IntegralDetailModel.class);

                    integralDetailModelList.addAll(integralDetailModels);
                    integralDetailsAdapter.notifyDataSetChanged();
                    mPullToRefreshListView.onRefreshComplete();
            }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.question_img:
            case R.id.integral_rules:
                Intent mIntent = new Intent(IntegralDetailActivity.this,
                        WebViewWithTitleActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("title", "积分规则");
                mBundle.putString("url", integral_detail_url);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                break;
            case R.id.time_shift:
                mDateDialog = new DateDialog(IntegralDetailActivity.this, R.style.data_filling_dialog,
                        new SetDateCallBack() {
                            @Override
                            public void date(long date) {
                                Date mDate = new Date(date);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(mDate);
                                date_text.setText(String.valueOf(cal.get(Calendar.YEAR)) + "年"
                                        + String.valueOf(cal.get(Calendar.MONTH) + 1) + "月");
                                dateText = String.valueOf(cal.get(Calendar.YEAR)) + "-"
                                        + String.valueOf(cal.get(Calendar.MONTH) + 1);
                                getIntegralDetails(true);
                            }
                        }, DateDialog.YearMonth, "请选择时间");
                UploadUtil.setAnimation(mDateDialog, TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            default:
                break;
        }
    }
}
