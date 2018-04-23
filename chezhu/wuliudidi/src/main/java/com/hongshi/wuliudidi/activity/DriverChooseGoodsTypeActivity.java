package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.GoodsTypes;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.http.AjaxParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/3/24.
 */

public class DriverChooseGoodsTypeActivity extends Activity {

    private GridView gridView;
    private String[] goodsListArr;
    private String[] goodsIdList;
    private MyAdapter adapter;
    private final String goodsListUrl = GloableParams.HOST + "carrier/enums/findGoodsType.do";
    private String goodsName = "";
    private static List<String> selectedStrList = new ArrayList<>();
    private String selectedIds = "";

    private DiDiTitleView goods_page_title;
    private EditText input_goods_name;
    private TextView ensure_goods_choose,cancel_goods_choose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_choose_goods_type_activity);

        initViews();
        getGoodsTypes();
    }

    private void initViews(){
        goods_page_title = (DiDiTitleView) findViewById(R.id.goods_page_title);
        goods_page_title.setBack(this);
        goods_page_title.setTitle("货物类型");
        gridView = (GridView) findViewById(R.id.gridView);
        input_goods_name = (EditText) findViewById(R.id.input_goods_name);
        ensure_goods_choose = (TextView) findViewById(R.id.ensure_goods_choose);
        cancel_goods_choose = (TextView) findViewById(R.id.cancel_goods_choose);
        cancel_goods_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ensure_goods_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStrList.size() > 3){
                    Toast.makeText(DriverChooseGoodsTypeActivity.this,"最多只能选择3种货品类型",Toast.LENGTH_LONG).show();
                    return;
                }
                String selectedStr = "";
                for(int i= 0; i<selectedStrList.size(); i++){
                    selectedStr = selectedStrList.get(i);
                    if(goodsIdList != null && goodsIdList.length > 0){
                        for(int j= 0; j <goodsIdList.length; j++){
                            if(selectedStr.equals(goodsListArr[j])){
                                selectedIds = goodsIdList[j] + "_" + selectedIds;
                            }
                        }
                    }
                }
                goodsName = input_goods_name.getText().toString();
                Intent intent = new Intent();
                intent.setAction(CommonRes.RefreshGoodsListWithGoodsType);
                intent.putExtra("goodsTypeId",selectedIds);
                if(selectedStrList.size() == 1){
                    intent.putExtra("goodsTypeName",selectedStrList.get(0));
                }else{
                    intent.putExtra("goodsTypeName","筛选");
                }
                intent.putExtra("goodsName",goodsName);
                sendBroadcast(intent);
                finish();
            }
        });
    }

    private void getGoodsTypes(){
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(DriverChooseGoodsTypeActivity.this, goodsListUrl, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Log.d("huiyuan","goods types json = " + t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    boolean isSuccess = jsonObject.optBoolean("success");
                    JSONArray goodsTypeList = jsonObject.optJSONArray("body");
                    int size = goodsTypeList.length();
                    if (isSuccess) {
                        if (goodsTypeList == null || size <= 0) {
                            Toast.makeText(DriverChooseGoodsTypeActivity.this, "data exception", Toast.LENGTH_LONG);
                        } else {
                            goodsListArr = new String[size];
                            goodsIdList = new String[size];
                            for (int i = 0; i < size; i++) {
                                GoodsTypes model = new GoodsTypes();
                                JSONObject obj = (JSONObject) goodsTypeList.get(i);
                                model.setKey(obj.optInt("key"));
                                model.setValue(obj.optString("value"));
                                goodsListArr[i] = model.getValue();
                                goodsIdList[i] = String.valueOf(model.getKey());
                            }
                            adapter = new MyAdapter(DriverChooseGoodsTypeActivity.this,goodsListArr);
                            gridView.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }

    private static class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private String[] goodsList;
        private boolean [] selectedList;
        private Context context;
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,88);

        public MyAdapter(Context context,String[] goodsText){
            this.goodsList = goodsText;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
            selectedList = new boolean[this.goodsList.length];
            layoutParams.leftMargin = 2;
            layoutParams.rightMargin = 2;
            layoutParams.topMargin = 2;
            layoutParams.bottomMargin = 2;
            selectedStrList.clear();
        }
        @Override
        public int getCount() {
            return goodsList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = layoutInflater.inflate(R.layout.driver_choose_goods_type_item,null);
            final TextView tv = (TextView) v.findViewById(R.id.goods_type);
            tv.setText(goodsList[position]);
            tv.setLayoutParams(layoutParams);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedList[position]){
                        tv.setBackgroundColor(context.getResources().getColor(R.color.white));
                        tv.setTextColor(context.getResources().getColor(R.color.home_text_none));
                        tv.setLayoutParams(layoutParams);
                        selectedList[position] = false;
                        String t = tv.getText().toString();
                        if(selectedStrList.contains(t)){
                            selectedStrList.remove(t);
                        }
//                        for(int i= 0; i < selectedStrList.size(); i++){
//                            Log.d("huiyuan",selectedStrList.get(i));
//                        }
                        return;
                    }else {
                        tv.setBackgroundColor(context.getResources().getColor(R.color.home_text_press));
                        tv.setTextColor(context.getResources().getColor(R.color.white));
                        tv.setLayoutParams(layoutParams);
                        selectedList[position] = true;
                        String t = tv.getText().toString();
                        if(!selectedStrList.contains(t)){
                            selectedStrList.add(t);
                        }
//                        for(int i= 0; i < selectedStrList.size(); i++){
//                            Log.d("huiyuan",selectedStrList.get(i));
//                        }
                        return;
                    }
                }
            });
            return v;
        }
    }
}
