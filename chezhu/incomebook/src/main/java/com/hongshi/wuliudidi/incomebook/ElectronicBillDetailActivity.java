package com.hongshi.wuliudidi.incomebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyuan on 2016/8/11.
 */
public class ElectronicBillDetailActivity extends Activity implements View.OnClickListener{

    private String sessionId = "";
    private boolean isHSJCBill = false;
    private int billStatus;
    private String billStatusText;
    private String startTimeStr;
    private String endTimeStr;
    private String totalMoney;
    private String realMoney;
    private String weight;
    private String transitTimes;
    private String truckCount;
    private String checkMan;
    private String billCycleId;
    private String goodsStationUid;
//    private ArrayList<ReconciliationTruckVO> truckInfoList = new ArrayList<>();

    private DiDiTitleView titleView;
    private TextView zong_jin_er_text,actual_money_text,yun_shu_che_ci,yun_shu_che_liang,shu_liang,he_dui_ren
            ,first_truck_text,second_truck_text,third_truck_text,gong_zi,lun_tai_fei,tong_xing_fei,jin_er,you_fei
            ,zu_lin_fei,yun_fei,refuse,agree,lao_wu_fei;
    private PullToRefreshListView electronic_bill_detail_listview;
    private ListView listView;
    private View electronic_bill_summary_sheet_layout;
    private ImageView left_arrow,right_arrow,right_arrow_red;
    private BillSheetDetailAdapter billSheetDetailAdapter;
    private HSJCBillSheetAdapter hsjcBillSheetAdapter;
    private List<BillSheetDetailModel> billSheetDetailModelList = new ArrayList<>();
    private List<HSJCBillDetailItemModel> hsjcBillDetailItemModelList = new ArrayList<>();
    private final String HE_TONG_YUN_FEI_URL = "https://cz.redlion56.com" + "/gwcz/" + "/carrier/reconciliation/findBizStub.do";
    private final String HE_TONG_BILL_URL = "https://cz.redlion56.com" + "/gwcz/" + "/carrier/reconciliation/findAndPage4TransitItemBySupplier.do";
    private final String HSJC_BILL_URL = "https://cz.redlion56.com" + "/gwcz/" +  "/carrier/reconciliation/findAndPage4TransitItemByHsjc.do";
    private final String AGREE_COMMIT_URL = "https://cz.redlion56.com" + "/gwcz/" + "carrier/reconciliation/agreeRec.do";
    private final String REFUSE_COMMIT_URL = "https://cz.redlion56.com" + "/gwcz/" + "carrier/reconciliation/refuseRec.do";
    private int currentPage = 0;
    private int pageSize = 10;
    private boolean isEnd;
    private String truckId[];
    private String truckArr[];
    private String bankCards[];
    private double moneyArray[];
    private String gong_zi_for_dialog = "4000";
    private String selected_truck = "";
    private int first_truck_in_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        sessionId = intent.getStringExtra("sessionId");
        isHSJCBill = intent.getBooleanExtra("isHSJCBill", false);
        startTimeStr = intent.getStringExtra("startTimeStr");
        endTimeStr = intent.getStringExtra("endTimeStr");
        totalMoney = intent.getStringExtra("totalMoney");
        realMoney = intent.getStringExtra("realMoney");
        weight = intent.getStringExtra("weight");
        transitTimes = intent.getStringExtra("transitTimes");
        truckCount = intent.getStringExtra("truckCount");
        checkMan = intent.getStringExtra("checkMan");
        billCycleId = intent.getStringExtra("billCycleId");
        goodsStationUid = intent.getStringExtra("goodsStationUid");
//        truckInfoList = (ArrayList<ReconciliationTruckVO>)intent.getSerializableExtra("truckInfoList");
        truckId = intent.getStringArrayExtra("truckId");
        truckArr = intent.getStringArrayExtra("truckArr");
        billStatus = intent.getIntExtra("receiptStatus", 0);
        billStatusText = intent.getStringExtra("billStatusText");

        setContentView(R.layout.electronic_bill_detail);
        initView();
    }

    private void initView(){
        titleView = (DiDiTitleView) findViewById(R.id.title_view);
        titleView.setTitle(startTimeStr + "—" + endTimeStr);
        titleView.setBack(this);
        zong_jin_er_text = (TextView) findViewById(R.id.zong_jin_er_text);
        actual_money_text = (TextView) findViewById(R.id.actual_money_text);
        yun_shu_che_ci = (TextView) findViewById(R.id.yun_shu_che_ci);
        yun_shu_che_liang = (TextView) findViewById(R.id.yun_shu_che_liang);
        shu_liang = (TextView) findViewById(R.id.shu_liang);
        he_dui_ren = (TextView) findViewById(R.id.he_dui_ren);
        electronic_bill_detail_listview = (PullToRefreshListView) findViewById(R.id.electronic_bill_detail_listview);
        electronic_bill_summary_sheet_layout = findViewById(R.id.electronic_bill_summary_sheet_layout);
        first_truck_text = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.first_truck_text);
        second_truck_text = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.second_truck_text);
        third_truck_text = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.third_truck_text);
        gong_zi = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.gong_zi);
        lun_tai_fei = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.lun_tai_fei);
        tong_xing_fei = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.tong_xing_fei);
        jin_er = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.jin_er);
        you_fei = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.you_fei);
        zu_lin_fei = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.zu_lin_fei);
        yun_fei = (TextView) electronic_bill_summary_sheet_layout.findViewById(R.id.yun_fei);
        refuse = (TextView)findViewById(R.id.refuse);
        agree = (TextView) findViewById(R.id.agree);
        lao_wu_fei = (TextView) findViewById(R.id.lao_wu_fei);
        left_arrow = (ImageView) electronic_bill_summary_sheet_layout.findViewById(R.id.left_arrow);
        right_arrow = (ImageView) electronic_bill_summary_sheet_layout.findViewById(R.id.right_arrow);
        right_arrow_red = (ImageView) electronic_bill_summary_sheet_layout.findViewById(R.id.right_arrow_red);

        zong_jin_er_text.setText(totalMoney);
        actual_money_text.setText(realMoney);
        yun_shu_che_ci.setText("运输车次 " + transitTimes + "车");
        yun_shu_che_liang.setText("运输车辆 " + truckCount + "辆");
        shu_liang.setText("数量 " + weight + "吨");
        he_dui_ren.setText("核对人：" + checkMan);

        initTruckTextView();

        gong_zi.setOnClickListener(this);
        right_arrow_red.setOnClickListener(this);
        left_arrow.setOnClickListener(this);
        right_arrow.setOnClickListener(this);

        first_truck_in_show = 0;
        first_truck_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_truck = first_truck_text.getText().toString();
                setTextViewStatusOnSelected(first_truck_text);
                setTextViewOnUnSelected(second_truck_text);
                setTextViewOnUnSelected(third_truck_text);

                getYunFeiData(getSelectedTruckId(selected_truck));
                currentPage = 0;
                sessionLoadBillItemsData(getSelectedTruckId(selected_truck), true);
            }
        });
        second_truck_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_truck = second_truck_text.getText().toString();
                setTextViewStatusOnSelected(second_truck_text);
                setTextViewOnUnSelected(first_truck_text);
                setTextViewOnUnSelected(third_truck_text);

                getYunFeiData(getSelectedTruckId(selected_truck));
                currentPage = 0;
                sessionLoadBillItemsData(getSelectedTruckId(selected_truck), true);
            }
        });
        third_truck_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_truck = third_truck_text.getText().toString();
                setTextViewStatusOnSelected(third_truck_text);
                setTextViewOnUnSelected(second_truck_text);
                setTextViewOnUnSelected(first_truck_text);

                getYunFeiData(getSelectedTruckId(selected_truck));
                currentPage = 0;
                sessionLoadBillItemsData(getSelectedTruckId(selected_truck), true);
            }
        });

        electronic_bill_detail_listview.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_up_load_more));
        electronic_bill_detail_listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        electronic_bill_detail_listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.loosen_to_load_more));

        electronic_bill_detail_listview.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.loosen_to_refresh));
        electronic_bill_detail_listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshing));
        electronic_bill_detail_listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.pull_down_refresh));
        listView = electronic_bill_detail_listview.getRefreshableView();
        electronic_bill_detail_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (electronic_bill_detail_listview.isRefreshing()) {
                    if (electronic_bill_detail_listview.isHeaderShown()) {
                        currentPage = 0;
                        String truckId = getSelectedTruckId(selected_truck);
                        sessionLoadBillItemsData(truckId, true);
                    } else if (electronic_bill_detail_listview.isFooterShown()) {
                        // 加载更多
                        if (isEnd) {
                            Toast.makeText(ElectronicBillDetailActivity.this, "已经是最后一页", Toast.LENGTH_SHORT).show();
                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(electronic_bill_detail_listview);
                            closeRefreshTask.execute();
                            return;
                        }
                        String truckId = getSelectedTruckId(selected_truck);
                        sessionLoadBillItemsData(truckId, false);
                    }
                }
            }
        });

        if(isHSJCBill){
            electronic_bill_summary_sheet_layout.setVisibility(View.GONE);
            sessionLoadBillItemsData(selected_truck, true);
        }else if(truckId != null && truckId.length > 0){
            electronic_bill_summary_sheet_layout.setVisibility(View.VISIBLE);
            int length = truckId.length;
            if (length <= 0){
                first_truck_text.setVisibility(View.INVISIBLE);
                second_truck_text.setVisibility(View.INVISIBLE);
                third_truck_text.setVisibility(View.INVISIBLE);
            }else if(length == 1){
                first_truck_text.setVisibility(View.VISIBLE);
            }else if(length == 2){
                first_truck_text.setVisibility(View.VISIBLE);
                second_truck_text.setVisibility(View.VISIBLE);
            }else{
                first_truck_text.setVisibility(View.VISIBLE);
                second_truck_text.setVisibility(View.VISIBLE);
                third_truck_text.setVisibility(View.VISIBLE);
            }
            getYunFeiData(truckId[0]);
            currentPage = 0;
            sessionLoadBillItemsData(truckId[0],true);
        }else electronic_bill_summary_sheet_layout.setVisibility(View.GONE);

        if(billStatus == 2 || "已同意".equals(billStatusText)){
            refuse.setVisibility(View.GONE);
            agree.setText(getResources().getString(R.string.agreed));
            setViewStatus(agree);
        }else if(billStatus == 3 || "已拒绝".equals(billStatusText)){
            agree.setVisibility(View.GONE);
            refuse.setText(getResources().getString(R.string.refused));
            setViewStatus(refuse);
        }else if(billStatus == 1 || "待回执".equals(billStatusText)){
            refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuseOrAgree("1");
                }
            });

            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuseOrAgree("0");
                }
            });
        }
    }

    private void setViewStatus(TextView statusView){
        statusView.setTextColor(getResources().getColor(R.color.white));
        statusView.setBackgroundColor(getResources().getColor(R.color.gray_light));
        statusView.setClickable(false);
    }

    private void refuseOrAgree(final String status){
        String url = "";
        AjaxParams params = new AjaxParams();
        params.put("billCycleId",billCycleId);
        if(status.equals("0")){
           url = AGREE_COMMIT_URL;
        }else if(status.equals("1")){
            url = REFUSE_COMMIT_URL;
        }

        Util.sessionPost(ElectronicBillDetailActivity.this, url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan", "状态提交返回结果 = " + t);
                if(status.equals("0")){
                    refuse.setVisibility(View.GONE);
                    agree.setText(getResources().getString(R.string.agreed));
                    setViewStatus(agree);
                    Toast.makeText(ElectronicBillDetailActivity.this,"您已同意账单明细!",Toast.LENGTH_LONG);
                }else if(status.equals("1")){
                    agree.setVisibility(View.GONE);
                    refuse.setText(getResources().getString(R.string.refused));
                    setViewStatus(refuse);
                    Toast.makeText(ElectronicBillDetailActivity.this,"您已拒绝此账单!",Toast.LENGTH_LONG);
                }
                Intent intent = new Intent("refreshBillData");
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(ElectronicBillDetailActivity.this,errMsg,Toast.LENGTH_LONG);
            }
        }, sessionId);
    }

    private String getSelectedTruckId(String selected_truck){
        String selectedTruckId = "";
        if(truckArr != null && truckArr.length > 0){
            for(int i = 0; i < truckArr.length; i++){
                if(selected_truck.equals(truckArr[i])){
                    selectedTruckId = truckId[i];
                }
            }
        }
        return selectedTruckId;
    }

    private void getYunFeiData(String selectedTruckId){

        if(!isHSJCBill){
            AjaxParams params = new AjaxParams();
            params.put("billCycleId",billCycleId);
            params.put("truckId",selectedTruckId);
            params.put("truckIdsJson",JSON.toJSONString(truckId));
            params.put("goodsStationUid",goodsStationUid);

            Util.sessionPost(ElectronicBillDetailActivity.this, HE_TONG_YUN_FEI_URL, params, new ChildAfinalHttpCallBack() {
                @Override
                public void data(String t) {
                    Log.d("huiyuan","工资、通行费等数据 = " + t);
                    if(t != null && !t.equals("")){
                        try{
                            JSONObject object = new JSONObject(t);
                            String allData = object.optString("body");
                            ReconciliationBizStubVO vo = JSON.parseObject(allData,ReconciliationBizStubVO.class);
                            String moneyUnitText = vo.getMoneyUnitText();
                            gong_zi_for_dialog = vo.getSalaryAmount() + moneyUnitText;
                            gong_zi.setText("工资 " + gong_zi_for_dialog);
                            lun_tai_fei.setText("轮胎费 " + vo.getTyreAmount() + moneyUnitText);
                            tong_xing_fei.setText("通行费 " + vo.getRoadAmount() + moneyUnitText);
                            jin_er.setText("金额 " + vo.getTotalAmount() + moneyUnitText);
                            you_fei.setText("油费 " + vo.getOilAmount() + moneyUnitText);
                            zu_lin_fei.setText("租赁费 " + vo.getLeaseAndRepairAmount() + moneyUnitText);
                            yun_fei.setText("运费 " + vo.getTransitAmount() + moneyUnitText);
                            lao_wu_fei.setText("劳务费 " + vo.getLaowufei());

                            int size = vo.getCardVoList().size();
                            if(size > 0){
                                bankCards = new String[size];
                                moneyArray = new double[size];
                                for(int i = 0; i < size; i++){
                                    bankCards[i] = vo.getCardVoList().get(i).getCardEndWith();
                                    moneyArray[i] = vo.getCardVoList().get(i).getAmount();
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else Toast.makeText(ElectronicBillDetailActivity.this,"get data failure",Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                    Toast.makeText(ElectronicBillDetailActivity.this,"获取工资、运费等数据异常:" + errMsg,Toast.LENGTH_LONG);
                }
            }, sessionId);
        }
    }

    private PromptManager mPromptManager = new PromptManager();
    private void sessionLoadBillItemsData(String truckID,final boolean isRefresh ) {
        mPromptManager.showProgressDialog1(ElectronicBillDetailActivity.this, "请稍等");
        AjaxParams params = new AjaxParams();
        params.put("truckIdsJson", JSON.toJSONString(truckId));
        params.put("billCycleId", billCycleId);
        params.put("currentPage", String.valueOf(currentPage + 1));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("goodsStationUid",goodsStationUid);

        if(!isHSJCBill){
            params.put("truckId",truckID);
        }
        String url = "";
        if(isHSJCBill){
            url = HSJC_BILL_URL;
        }else {
            url = HE_TONG_BILL_URL;
        }
        Util.sessionPost(ElectronicBillDetailActivity.this, url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                mPromptManager.closeProgressDialog();
                Log.d("huiyuan","bill detail = " + t);
                getBillListData(t, isRefresh);
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                mPromptManager.closeProgressDialog();
                electronic_bill_detail_listview.onRefreshComplete();
                Toast.makeText(ElectronicBillDetailActivity.this, "详单列表数据异常:" + errMsg, Toast.LENGTH_LONG);
            }
        }, sessionId);

    }

    private void getBillListData(String t, boolean isRefresh) {
        JSONObject jsonObject;
        try {
            if (isRefresh) {
                if(isHSJCBill){
                    hsjcBillDetailItemModelList.clear();
                    isEnd = false;
//                    lastId = "";
                    electronic_bill_detail_listview.onRefreshComplete();
                }else {
                    billSheetDetailModelList.clear();
                    isEnd = false;
//                    lastId = "";
                    electronic_bill_detail_listview.onRefreshComplete();
                }
            }
            jsonObject = new JSONObject(t);
            JSONObject body = jsonObject.getJSONObject("body");
            String all = body.optString("items");
            //判断是否还有下一页
            isEnd = body.optBoolean("end");
            currentPage = body.optInt("currentPage");
            List<HSJCBillDetailItemModel> hsjcBillTempList = new ArrayList<>();
            List<BillSheetDetailModel> billTempList = new ArrayList<>();

            if(isHSJCBill){
                hsjcBillTempList = JSON.parseArray(all,HSJCBillDetailItemModel.class);
            }else {
                billTempList = JSON.parseArray(all, BillSheetDetailModel.class);
            }

            if(isRefresh){
                if(isHSJCBill){
                    hsjcBillDetailItemModelList = hsjcBillTempList;
                    hsjcBillSheetAdapter = new HSJCBillSheetAdapter(this, hsjcBillDetailItemModelList);
                    listView.setAdapter(hsjcBillSheetAdapter);
                }else {
                    billSheetDetailModelList = billTempList;
                    billSheetDetailAdapter = new BillSheetDetailAdapter(this,billSheetDetailModelList);
                    listView.setAdapter(billSheetDetailAdapter);
                }
            }else{
                if(isHSJCBill){
                    hsjcBillSheetAdapter.addGoodsList(hsjcBillTempList);
                }else {
                    billSheetDetailAdapter.addGoodsList(billTempList);
                }
            }

            if(isHSJCBill){
                if(hsjcBillDetailItemModelList.size() <= 0){
                    Toast.makeText(this, "无账单数据", Toast.LENGTH_LONG);
                }else{
                    electronic_bill_detail_listview.setVisibility(View.VISIBLE);
                    if(!isRefresh)
                        hsjcBillSheetAdapter.notifyDataSetChanged();
                }
            }else {
                if(billSheetDetailModelList.size() <= 0){
                    Toast.makeText(this, "无账单数据", Toast.LENGTH_LONG);
                }else{
                    electronic_bill_detail_listview.setVisibility(View.VISIBLE);
                    if(!isRefresh)
                        billSheetDetailAdapter.notifyDataSetChanged();
                }
            }

            electronic_bill_detail_listview.onRefreshComplete();
        } catch (JSONException e) {
            e.printStackTrace();
            electronic_bill_detail_listview.onRefreshComplete();
            Toast.makeText(this,"无账单数据",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gong_zi:
            case R.id.right_arrow_red:
                MoneyTipsDialog dialog = new MoneyTipsDialog(ElectronicBillDetailActivity.this,
                        bankCards,moneyArray,gong_zi_for_dialog);
                UploadUtil.setAnimation(dialog,1,false);
                dialog.show();
                break;
            case R.id.left_arrow:

                if(truckArr.length >3 && first_truck_in_show >= 0 && first_truck_in_show < (truckArr.length - 3)){
                    first_truck_in_show = first_truck_in_show + 1;
                    setTruckTextViewOnSelecting();
                    if(first_truck_in_show == truckArr.length - 3){
//                        left_arrow.setVisibility(View.GONE);
                    }else left_arrow.setVisibility(View.VISIBLE);
                }else {
//                    left_arrow.setVisibility(View.GONE);
                }
                break;
            case R.id.right_arrow:

                if (truckArr.length >3 && first_truck_in_show > 0 && first_truck_in_show <= (truckArr.length - 3)){
                    first_truck_in_show = first_truck_in_show - 1;
                    setTruckTextViewOnSelecting();
                    if(first_truck_in_show == 0){
//                        right_arrow.setVisibility(View.GONE);
                    }else {
                        right_arrow.setVisibility(View.VISIBLE);
                    }
                }else {
//                    right_arrow.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void setTextViewStatusOnSelected(TextView view){
        view.setTextColor(getResources().getColor(R.color.white));
        view.setBackgroundResource(R.drawable.truck_status_selected);
    }
    private void setTextViewOnUnSelected(TextView view){
        view.setTextColor(getResources().getColor(R.color.gray));
        view.setBackgroundResource(R.drawable.truck_status_unselected);
    }

    private void initTruckTextView(){
        if(!isHSJCBill && truckArr != null && truckArr.length > 0){
            electronic_bill_summary_sheet_layout.setVisibility(View.VISIBLE);
            electronic_bill_detail_listview.setVisibility(View.VISIBLE);
            setTextViewStatusOnSelected(first_truck_text);
            if(truckArr.length == 1){
                first_truck_text.setVisibility(View.VISIBLE);
                first_truck_text.setText(truckArr[0]);
            }else if(truckArr.length == 2){
                first_truck_text.setVisibility(View.VISIBLE);
                second_truck_text.setVisibility(View.VISIBLE);
                first_truck_text.setText(truckArr[0]);
                second_truck_text.setText(truckArr[1]);
            }else if(truckArr.length >= 3){
                first_truck_text.setVisibility(View.VISIBLE);
                second_truck_text.setVisibility(View.VISIBLE);
                third_truck_text.setVisibility(View.VISIBLE);
                first_truck_text.setText(truckArr[0]);
                second_truck_text.setText(truckArr[1]);
                third_truck_text.setText(truckArr[2]);
            }
            selected_truck = first_truck_text.getText().toString();
        }else {
            electronic_bill_summary_sheet_layout.setVisibility(View.GONE);
        }
    }

    private void setTruckTextViewOnSelecting(){
        first_truck_text.setText(truckArr[first_truck_in_show]);
        second_truck_text.setText(truckArr[first_truck_in_show + 1]);
        third_truck_text.setText(truckArr[first_truck_in_show + 2]);
        if(selected_truck.equals(first_truck_text.getText().toString())){
            setTextViewStatusOnSelected(first_truck_text);
            setTextViewOnUnSelected(second_truck_text);
            setTextViewOnUnSelected(third_truck_text);
        }else if(selected_truck.equals(second_truck_text.getText().toString())){
            setTextViewStatusOnSelected(second_truck_text);
            setTextViewOnUnSelected(first_truck_text);
            setTextViewOnUnSelected(third_truck_text);
        }else if(selected_truck.equals(third_truck_text.getText().toString())){
            setTextViewStatusOnSelected(third_truck_text);
            setTextViewOnUnSelected(first_truck_text);
            setTextViewOnUnSelected(second_truck_text);
        }else {
            setTextViewOnUnSelected(first_truck_text);
            setTextViewOnUnSelected(second_truck_text);
            setTextViewOnUnSelected(third_truck_text);
        }
    }
}
