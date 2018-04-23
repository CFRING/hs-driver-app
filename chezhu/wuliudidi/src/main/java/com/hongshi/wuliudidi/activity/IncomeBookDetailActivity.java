package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.IncomeBookAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.ClassfyStatVO;
import com.hongshi.wuliudidi.model.CtBillCycleReconStatVO;
import com.hongshi.wuliudidi.model.TransitInfoStatVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.view.NoScrollListView;
import com.hongshi.wuliudidi.view.ObservableScrollView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/8/23.
 */

public class IncomeBookDetailActivity extends Activity implements View.OnClickListener,
        ObservableScrollView.ScrollViewListener{

    private ImageView back;
    private TextView date_text,total_money_text,cash_text,consume_text,oil_text,tyre_text,
            transit_count_text,transit_weight_text,transit_trucks_text,transit_materia_text,
            sort_by_truck_text, sort_by_goods_text,sort_by_truck_head_text,sort_by_goods_head_text,
            tong_ji_dan_wei;
    private View truck_sort_bottom_line,goods_sort_bottom_line,
            truck_sort_bottom_line_head,goods_sort_bottom_line_head;
    private LinearLayout middle_tab_bar,head_tab_bar;
    private RelativeLayout sort_by_truck_container,sort_by_goods_container,
            sort_by_truck_container_head,sort_by_goods_container_head,title_view;
    private ObservableScrollView scroll_view;
    private NoScrollListView noScrollListView;

    private IncomeBookAdapter incomeBookAdapter;
    private List<ClassfyStatVO> inComeBookModelList = new ArrayList<>();

    private final String income_book_detail_url = GloableParams.HOST + "carrier/app/bill/transitInfoStat.do";
    private int titleViewBottomLocation;
    private int classifyStatType = 1;
    private String userId = "";
    private String billCycleId = "";
    private String periodText = "";
    private String billCycleJsonStr = "";
    private CtBillCycleReconStatVO ctBillCycleReconStatVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctBillCycleReconStatVO = (CtBillCycleReconStatVO)getIntent().getSerializableExtra("ctBillCycleReconStatVO");
        userId = getSharedPreferences("config",MODE_PRIVATE).getString("userId","");
        billCycleId = ctBillCycleReconStatVO.getBillCycleId();
        periodText = ctBillCycleReconStatVO.getPeriod();
        setContentView(R.layout.income_book_detail_activity);

        initView();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        date_text = (TextView) findViewById(R.id.date_text);
        total_money_text = (TextView) findViewById(R.id.total_money_text);
        cash_text = (TextView) findViewById(R.id.cash_text);
        consume_text = (TextView) findViewById(R.id.consume_text);
        oil_text = (TextView) findViewById(R.id.oil_text);
        tyre_text = (TextView) findViewById(R.id.tyre_text);
        transit_count_text = (TextView) findViewById(R.id.transit_count_text);
        transit_weight_text = (TextView) findViewById(R.id.transit_weight_text);
        transit_trucks_text = (TextView) findViewById(R.id.transit_trucks_text);
        transit_materia_text = (TextView) findViewById(R.id.transit_materia_text);
        sort_by_truck_text = (TextView) findViewById(R.id.sort_by_truck_text);
        sort_by_goods_text = (TextView) findViewById(R.id.sort_by_goods_text);
        sort_by_truck_head_text = (TextView) findViewById(R.id.sort_by_truck_head_text);
        sort_by_goods_head_text = (TextView) findViewById(R.id.sort_by_goods_head_text);
        tong_ji_dan_wei = (TextView) findViewById(R.id.tong_ji_dan_wei);
        sort_by_truck_container = (RelativeLayout) findViewById(R.id.sort_by_truck_container);
        sort_by_goods_container = (RelativeLayout) findViewById(R.id.sort_by_goods_container);
        sort_by_truck_container_head = (RelativeLayout) findViewById(R.id.sort_by_truck_container_head);
        sort_by_goods_container_head = (RelativeLayout) findViewById(R.id.sort_by_goods_container_head);
        title_view = (RelativeLayout) findViewById(R.id.title_view);
        truck_sort_bottom_line_head = findViewById(R.id.truck_sort_bottom_line_head);
        goods_sort_bottom_line_head = findViewById(R.id.goods_sort_bottom_line_head);
        truck_sort_bottom_line = findViewById(R.id.truck_sort_bottom_line);
        goods_sort_bottom_line = findViewById(R.id.goods_sort_bottom_line);
        middle_tab_bar = (LinearLayout) findViewById(R.id.middle_tab_bar);
        head_tab_bar = (LinearLayout) findViewById(R.id.head_tab_bar);
        noScrollListView = (NoScrollListView) findViewById(R.id.no_scroll_listView);
        scroll_view = (ObservableScrollView) findViewById(R.id.scroll_view);

        back.setOnClickListener(this);
        sort_by_truck_container.setOnClickListener(this);
        sort_by_goods_container.setOnClickListener(this);
        sort_by_truck_container_head.setOnClickListener(this);
        sort_by_goods_container_head.setOnClickListener(this);

        //for test
//        InComeBookModel model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙A 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙B 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙C 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙D 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙E 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙F 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);
//
//        model = new InComeBookModel();
//        model.setCash_text("111111");
//        model.setOil_text("222222");
//        model.setTotal_money("123434.0");
//        model.setTruck_number_text("浙G 12345");
//        model.setTyre_text("333333");
//        inComeBookModelList.add(model);

        noScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassfyStatVO classfyStatVO = inComeBookModelList.get(position);
                Intent intent = new Intent(IncomeBookDetailActivity.this,TransitRecordDetailActivity.class);
                intent.putExtra("billCycleJsonStr",billCycleJsonStr);
                intent.putExtra("classifyStatType",classifyStatType);
                intent.putExtra("periodText",periodText);
                intent.putExtra("classfyStatVO",classfyStatVO);
                startActivity(intent);
            }
        });

        incomeBookAdapter = new IncomeBookAdapter(IncomeBookDetailActivity.this,inComeBookModelList);
        noScrollListView.setAdapter(incomeBookAdapter);
        noScrollListView.setFocusable(false);
        scroll_view.setScrollViewListener(this);

        if (periodText != null) {date_text.setText(periodText);}
        total_money_text.setText(ctBillCycleReconStatVO.getTotalMoney());
        cash_text.setText(ctBillCycleReconStatVO.getSalaryMoney());
        consume_text.setText(ctBillCycleReconStatVO.getConsumptionMoney());
        oil_text.setText(ctBillCycleReconStatVO.getOilMoney());
        tyre_text.setText(ctBillCycleReconStatVO.getTyreMoney());

        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.sort_by_truck_container:
            case R.id.sort_by_truck_container_head:
                sort_by_truck_text.setTextColor(getResources().getColor(R.color.theme_color));
                sort_by_goods_text.setTextColor(getResources().getColor(R.color.gray));
                truck_sort_bottom_line.setVisibility(View.VISIBLE);
                goods_sort_bottom_line.setVisibility(View.GONE);
                sort_by_truck_head_text.setTextColor(getResources().getColor(R.color.theme_color));
                sort_by_goods_head_text.setTextColor(getResources().getColor(R.color.gray));
                truck_sort_bottom_line_head.setVisibility(View.VISIBLE);
                goods_sort_bottom_line_head.setVisibility(View.GONE);
                classifyStatType = 1;
                loadData();
                break;
            case R.id.sort_by_goods_container:
            case R.id.sort_by_goods_container_head:
                sort_by_truck_text.setTextColor(getResources().getColor(R.color.gray));
                sort_by_goods_text.setTextColor(getResources().getColor(R.color.theme_color));
                truck_sort_bottom_line.setVisibility(View.GONE);
                goods_sort_bottom_line.setVisibility(View.VISIBLE);
                sort_by_truck_head_text.setTextColor(getResources().getColor(R.color.gray));
                sort_by_goods_head_text.setTextColor(getResources().getColor(R.color.theme_color));
                truck_sort_bottom_line_head.setVisibility(View.GONE);
                goods_sort_bottom_line_head.setVisibility(View.VISIBLE);
                classifyStatType = 2;
                loadData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int[] location = new int[2];
        title_view.getLocationOnScreen(location);
        int locationY = location[1];
        titleViewBottomLocation = locationY + title_view.getHeight();
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        middle_tab_bar.getLocationOnScreen(location);
        int locationY = location[1];
//        Log.i("locationY", locationY + "  " + "标题栏底部的Y坐标是：" + titleViewBottomLocation);

        if (locationY <= titleViewBottomLocation && (head_tab_bar.getVisibility() == View.GONE || head_tab_bar.getVisibility() == View.INVISIBLE)) {
            head_tab_bar.setVisibility(View.VISIBLE);
        }

        if (locationY > titleViewBottomLocation && head_tab_bar.getVisibility() == View.VISIBLE) {
            head_tab_bar.setVisibility(View.GONE);
        }
    }

    private void loadData(){
       AjaxParams params = new AjaxParams();
        params.put("userId",userId);
        params.put("billCycleId",billCycleId);
        params.put("classifyStatType","" + classifyStatType);
       DidiApp.getHttpManager().sessionPost(IncomeBookDetailActivity.this, income_book_detail_url, params, new ChildAfinalHttpCallBack() {

           @Override
           public void onFailure(String errCode, String errMsg, Boolean errSerious) {
           }

           @Override
           public void data(String t) {
               try {
                   if(inComeBookModelList != null && inComeBookModelList.size() > 0){
                       inComeBookModelList.clear();
                   }
                   JSONObject jsonObject = new JSONObject(t);
                   String all = jsonObject.getString("body");
                   if(all != null && !"".equals(all)){
                       TransitInfoStatVO transitInfoStatVO = JSON.parseObject(all,TransitInfoStatVO.class);
                       List<ClassfyStatVO> tmpList = transitInfoStatVO.getClassfyStatVOList();
                       inComeBookModelList.addAll(tmpList);
                       incomeBookAdapter.notifyDataSetChanged();
                       noScrollListView.setFocusable(true);

                       billCycleJsonStr = transitInfoStatVO.getBillCycleJsonStr();

                       tong_ji_dan_wei.setText(transitInfoStatVO.getGoodsStationName());
                       total_money_text.setText(transitInfoStatVO.getTotalMoney());
                       cash_text.setText(transitInfoStatVO.getTotalSalaryMoney());
                       consume_text.setText(transitInfoStatVO.getTotalConsumptionMoney());
                       oil_text.setText(transitInfoStatVO.getTotalOilMoney());
                       tyre_text.setText(transitInfoStatVO.getTotalTyreMoney());

                       transit_count_text.setText(transitInfoStatVO.getTransitTimes() + "车");
                       transit_weight_text.setText(transitInfoStatVO.getTotalWeight() + "吨");
                       transit_trucks_text.setText(transitInfoStatVO.getTruckCount() + "辆");
                       transit_materia_text.setText(transitInfoStatVO.getGoodsTypeCount() + "种");
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
   }
}
