package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.DriverTruckListAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.TruckIdCallBack;
import com.hongshi.wuliudidi.model.PageInfo4App;
import com.hongshi.wuliudidi.model.TruckAuthAppVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

public class ChooseTruckForDriverActivity extends Activity {

    private DiDiTitleView mDiDiTitleView;
    private ListView mTruckListview;
    private List<TruckAuthAppVO> mTruckList = new ArrayList<>();
    private PageInfo4App pageInfo4App;
    private DriverTruckListAdapter mTruckListAdapter;
    //多个报备车辆的ID数组
    private List<String> mTruckIdsList = new ArrayList<String>();
    //多个报备车辆的ID数组
    private List<String> mTruckIdsList1 = new ArrayList<String>();
    //多个报备车辆的ID数组
    private List<String> mTruckIdsNumber = new ArrayList<String>();
    //多个报备车辆的司机姓名
    private List<String> mDriversName = new ArrayList<String>();
    private Button mSure;
    private CheckBox mSelectAll;
    private int truckNum = 0;
    private TextView choose_all_tip;
    private final String get_truck_list_url = GloableParams.HOST + "uic/authentication/getAuditedTruckList.do";

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("ChooseTruckActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("ChooseTruckActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setContentView(R.layout.choose_truck_activity);

        initViews();
        getTruckList();
    }

    private void initViews(){
        mDiDiTitleView = (DiDiTitleView) findViewById(R.id.choose_truck_title);
        mDiDiTitleView.setBack(this);
        mDiDiTitleView.setTitle("车辆分配");
        mTruckListview = (ListView) findViewById(R.id.choose_truck_listview);
        mSure = (Button) findViewById(R.id.auction_sure);
        mSelectAll = (CheckBox) findViewById(R.id.check);
        choose_all_tip = (TextView) findViewById(R.id.choose_all_tip);
//        AuctionOfferModel mAuctionOfferModel = (AuctionOfferModel) getIntent().getSerializableExtra("mode");
//        mTruckList = mAuctionOfferModel.getTrucks();

//        mTruckIdsList1 = getIntent().getStringArrayListExtra("truckId");
//        truckNum = mTruckIdsList1.size();
        mSure.setText("确认车辆("+ truckNum +")");
//        if(mTruckIdsList1.size() == mTruckList.size()){
//            mSelectAll.setChecked(true);
//            mSure.setText("确认车辆("+ truckNum +")");
//        }
//        if(mTruckIdsList1.size()>0){
//            for(int i=0;i<mTruckIdsList1.size();i++){
//                for(int j=0;j<mTruckList.size();j++){
//                    if(mTruckIdsList1.get(i).equals(mTruckList.get(j).getTruckId())){
//                        mTruckIdsList.add(mTruckList.get(j).getTruckId());
//                        mTruckIdsNumber.add(mTruckList.get(j).getTruckNumber());
//                        mDriversName.add(mTruckList.get(j).getDriverName());
//                    }
//                }
//            }
//        }
        //添加全选功能
//        mSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    if(mTruckListAdapter != null){
//                        mTruckIdsList.clear();
//                        mTruckIdsNumber.clear();
//                        mDriversName.clear();
//                        for(int i=0;i<mTruckList.size();i++){
//                            mTruckIdsList.add(mTruckList.get(i).getTruckId());
//                            mTruckIdsNumber.add(mTruckList.get(i).getTruckNumber());
//                            mDriversName.add(mTruckList.get(i).getDriverName());
//                        }
//                        truckNum = mTruckList.size();
//                        mSure.setText("确认车辆("+ truckNum +")");
//                        mTruckListAdapter.setSelect(true);
//                        mTruckListAdapter.setNoneSelect(false);
//                        mTruckListAdapter.notifyDataSetChanged();
//                    }
//                }else{
//                    if(truckNum == mTruckList.size()){
//                        mTruckIdsList.clear();
//                        mTruckIdsNumber.clear();
//                        mDriversName.clear();
//                        mTruckListAdapter.setNoneSelect(true);
//                        mTruckListAdapter.setSelect(false);
//                        mTruckListAdapter.notifyDataSetChanged();
//                        mSure.setText("确认车辆("+ 0 +")");
//                        truckNum = 0;
//                    }
//                }
//            }
//        })

        mSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTruckIdsNumber.size() > 5){
                    Toast.makeText(ChooseTruckForDriverActivity.this,"最多只能给司机分配5辆车!",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Intent intent = new Intent();
                    intent.setAction("driver_truck_list");
                    intent.putStringArrayListExtra("truckNumber", (ArrayList<String>) mTruckIdsNumber);
                    intent.putStringArrayListExtra("truckIdList",(ArrayList<String>) mTruckIdsList);
                    sendBroadcast(intent);
                    finish();
                }
            }
        });
        mSelectAll.setVisibility(View.GONE);
        choose_all_tip.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 120);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mSure.setLayoutParams(layoutParams);
    }

    private void getTruckList(){
        AjaxParams params = new AjaxParams();
        params.put("currentPage","1");
        params.put("pageSize","200");
        DidiApp.getHttpManager().sessionPost(ChooseTruckForDriverActivity.this, get_truck_list_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan","车辆分配界面数据 = " + t);
                try{
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.optString("body");
                    pageInfo4App = JSON.parseObject(body,PageInfo4App.class);
                    mTruckList = pageInfo4App.getItems();
                    mTruckListAdapter = new DriverTruckListAdapter(ChooseTruckForDriverActivity.this,
                            mTruckIdsList,mTruckList,new TruckIdCallBack() {
                        @Override
                        public void addId(String id,int position,String truckNumber) {
//                if(!mSelectAll.isChecked()){
                            mTruckIdsList.add(id);
                            mTruckIdsNumber.add(truckNumber);
//                    mDriversName.add(mTruckList.get(position).getDriverName());
                            truckNum = truckNum + 1;
                            mSure.setText("确认车辆("+ truckNum +")");
//                    if(truckNum == mTruckList.size()){
//                        mSelectAll.setChecked(true);
//                    }
//                }
                            mTruckListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void minusId(String id,int position,String truckNumber) {
                            for(int i=0;i<mTruckIdsList.size();i++){
                                if(mTruckIdsList.get(i).equals(id)){
                                    mTruckIdsList.remove(i);
                                    mTruckIdsNumber.remove(i);
//                        mDriversName.remove(i);
                                }
                            }
                            truckNum = truckNum - 1;
                            if(truckNum>0){
                                mSure.setText("确认车辆("+ truckNum +")");
                            }else{
                                mSure.setText("确认车辆("+ 0 +")");
                                truckNum = 0;
                            }
//                mSelectAll.setChecked(false);
                            mTruckListAdapter.notifyDataSetChanged();
                        }
                    });
                    mTruckListview.setAdapter(mTruckListAdapter);
                }catch (Exception e){
                    Toast.makeText(ChooseTruckForDriverActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(ChooseTruckForDriverActivity.this,errMsg,Toast.LENGTH_LONG).show();
            }
        });
    }
}
