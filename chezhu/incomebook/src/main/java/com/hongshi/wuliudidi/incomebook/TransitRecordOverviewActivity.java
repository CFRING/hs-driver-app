package com.hongshi.wuliudidi.incomebook;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TransitRecordOverviewActivity extends Activity implements View.OnClickListener{
    private final String RefreshTransitAnnual = "action.refresh.transit_annual";//刷新运输记录年度
    private final String RefreshTransitMonthly = "action.refresh.transit_monthly";//刷新运输记录月度
    private final String RefreshTransitDialy = "action.refresh.transit_daily";//刷新运输记录每日
    private DiDiTitleView title;
    private MyLayoutController controller;
    private ArrayList<ViewGroup> layoutList;
    private TransitRecordStatisticLayout annualLayout, monthlyLayout;
    private TransitRecordItemsLayout dailyLayout;
    private TextView[] choices;
    private final int PageNum = 3;
    private LinearLayout siftingLayout;
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshTransitAnnual)) {
                turnTo(2);
            }else if(action.equals(RefreshTransitMonthly)){
                turnTo(1);
            }else if(action.equals(RefreshTransitDialy)){
                turnTo(0);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd("TransitRecordOverviewActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart("TransitRecordOverviewActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transit_record_overview_activity);

        title = (DiDiTitleView) findViewById(R.id.transit_record_title);
        title.setBack(TransitRecordOverviewActivity.this);
        title.setTitle("运输记录");

        siftingLayout = (LinearLayout) findViewById(R.id.sifting_layout);
        siftingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransitRecordOverviewActivity.this, TimeSiftingActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

        choices = new TextView[PageNum];
        choices[0] = (TextView) findViewById(R.id.daily_statistic_text);
        choices[0].setOnClickListener(this);
        choices[1] = (TextView) findViewById(R.id.monthly_statistic_text);
        choices[1].setOnClickListener(this);
        choices[2] = (TextView) findViewById(R.id.annual_statistic_text);
        choices[2].setOnClickListener(this);

        annualLayout = (TransitRecordStatisticLayout) findViewById(R.id.annual_layout);
        monthlyLayout = (TransitRecordStatisticLayout) findViewById(R.id.monthly_layout);
        dailyLayout = (TransitRecordItemsLayout) findViewById(R.id.daily_layout);
        Intent intent = getIntent();
        annualLayout.setContextIntent(intent);
        monthlyLayout.setContextIntent(intent);
        dailyLayout.setContextIntent(intent);

        controller = new MyLayoutController();
        layoutList = new ArrayList<>();
        layoutList.add(dailyLayout);

        monthlyLayout.setQueryType("1");
        layoutList.add(monthlyLayout);

        annualLayout.setQueryType("2");
        layoutList.add(annualLayout);

        controller.setViewList(layoutList);
        Intent intent1 = new Intent();
        intent1.setAction(RefreshTransitDialy);
        sendBroadcast(intent1);

        Intent intent2 = new Intent();
        intent2.setAction(RefreshTransitMonthly);
        sendBroadcast(intent2);

        Intent intent3 = new Intent();
        intent3.setAction(RefreshTransitAnnual);
        sendBroadcast(intent3);

        controller.setOnViewChangeListener(new MyLayoutController.onViewChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onViewSelected(int i) {
                for (int j = 0; j < choices.length; j++) {
                    choices[j].setTextColor(getResources().getColor(R.color.black));
                    choices[j].setBackground(null);
                }
                choices[i].setTextColor(getResources().getColor(R.color.white));
                choices[i].setBackground(getResources().getDrawable(R.drawable.transit_overview_switchbtn_style));
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshTransitMonthly);
        intentFilter.addAction(RefreshTransitAnnual);
        intentFilter.addAction(RefreshTransitDialy);
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        turnTo(1);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.daily_statistic_text:
                turnTo(0);
                break;
            case R.id.monthly_statistic_text:
                turnTo(1);
                break;
            case R.id.annual_statistic_text:
                turnTo(2);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void turnTo(int index){
        if(index < 0 || index >= PageNum){
            return;
        }


        if(controller != null && controller.getCurrentNum() != index){
            controller.setCurrentNum(index);
            for(int i = 0; i < PageNum; i++){
                choices[i].setTextColor(getResources().getColor(R.color.black));
                choices[i].setBackground(null);
            }
            choices[index].setTextColor(getResources().getColor(R.color.white));
            choices[index].setBackground(getResources().getDrawable(R.drawable.transit_overview_switchbtn_style));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1002) {
            if (data != null) {
                String queryType, queryDateStart, queryDateEnd;
                try{
                    queryType = data.getStringExtra("queryType");
                }catch (Exception e){
                    queryType = "";
                }

                try{
                    queryDateStart = data.getStringExtra("queryDateStart");
                }catch (Exception e){
                    queryDateStart = "";
                }

                try{
                    queryDateEnd = data.getStringExtra("queryDateEnd");
                }catch (Exception e){
                    queryDateEnd = "";
                }
                Intent intent = new Intent();
                if(queryType.equals("0")){
                    queryDateStart = data.getStringExtra("queryDateStart");
                    queryDateEnd = data.getStringExtra("queryDateEnd");

                    intent.setAction(RefreshTransitDialy);
                    intent.putExtra("queryDateStart", queryDateStart);
                    intent.putExtra("queryDateEnd", queryDateEnd);
                }else if(queryType.equals("1")){
                    queryDateStart = data.getStringExtra("queryDateStart");

                    intent.setAction(RefreshTransitMonthly);
                    intent.putExtra("queryDateStart", queryDateStart);
                }else if(queryType.equals("2")){
                    queryDateStart = data.getStringExtra("queryDateStart");

                    intent.setAction(RefreshTransitAnnual);
                    intent.putExtra("queryDateStart", queryDateStart);
                }
                sendBroadcast(intent);
            }
        }
    }
}
