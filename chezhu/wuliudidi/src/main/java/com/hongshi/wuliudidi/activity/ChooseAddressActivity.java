package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.ChooseAddressAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.CityListModel;
import com.hongshi.wuliudidi.model.KVModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.PromptManager1;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.NoScrollGridView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bian
 * 目的地出发地选择地址界面
 */
public class ChooseAddressActivity extends Activity implements View.OnClickListener {
    //保存子公司搜索历史
    private final String SAVE_COMPANY_URL = GloableParams.HOST + "uic/app/company/saveSubCompanyHistory.do";
    //获取子公司搜索历史
    private final String GET_HIS_COMPANY_URL = GloableParams.HOST + "uic/app/company/queryHistorySubCompany.do";
    //获取子公司的枚举
    private final String GET_COMPANY_URL = GloableParams.HOST + "uic/app/company/queryHSSubCompany.do";
    //获取当前城市和历史城市
    private final String GET_HIS_CITY_URL = GloableParams.HOST + "carrier/app/areaQuery/queryHistoryCity.do";
    //获取热点城市
    private final String GET_HOT_CITY_URL = GloableParams.HOST + "carrier/app/areaQuery/queryAllProvince.do";
    //获取省下的所有市区
    private final String GET_CITY_BY_PROVINCE_URL = GloableParams.HOST + "carrier/app/areaQuery/queryAllByProvinceForAnd.do";

    private DiDiTitleView mTitleView;
    private RelativeLayout mAddressLay, mCompanyAddLay;
    private TextView mAddressTV, mCompanyAddTV, mHisProvinceTag, mProvinceTag;
    private View mAddressLine, mCompanyAddLine;
    private ScrollView mScrollView;
    private LinearLayout mHisProvinceLay, mProvinceLay;
    private LinearLayout mTopLay;
    private RelativeLayout mChooseAddressLay;
    private TextView mHintTV, mAddressTV_;
    private ImageView mAddressBackImg;
    private NoScrollGridView mAddressGV;

    private List<CityListModel> mHisProvinceList, mAddressList, mProvinceList, mCityList, mDistrictList;
    private List<KVModel> mHisCompanyList, mCompanyList;
    private int mLevel = 1;
    private String mProvinceId, mProvince, mCityId, mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);

        mAddressList = new ArrayList<>();
        mCityList = new ArrayList<>();
        mDistrictList = new ArrayList<>();

        initView();
        getHisCity();
        getHotCity();
    }

    private void initView() {
        mTitleView = (DiDiTitleView) findViewById(R.id.title_view);
        mAddressLay = (RelativeLayout) findViewById(R.id.address_layout);
        mCompanyAddLay = (RelativeLayout) findViewById(R.id.company_address_layout);
        mAddressTV = (TextView) findViewById(R.id.address_text);
        mCompanyAddTV = (TextView) findViewById(R.id.company_address_text);
        mAddressLine = findViewById(R.id.address_line);
        mCompanyAddLine = findViewById(R.id.company_address_line);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mHisProvinceTag = (TextView) findViewById(R.id.history_province_tag);
        mProvinceTag = (TextView) findViewById(R.id.province_tag);
        mHisProvinceLay = (LinearLayout) findViewById(R.id.history_province_layout);
        mProvinceLay = (LinearLayout) findViewById(R.id.province_layout);
        mTopLay = (LinearLayout) findViewById(R.id.top_layout);
        mChooseAddressLay = (RelativeLayout) findViewById(R.id.choose_address_lay);
        mHintTV = (TextView) findViewById(R.id.hint_text);
        mAddressTV_ = (TextView) findViewById(R.id.address_text_);
        mAddressBackImg = (ImageView) findViewById(R.id.back_image);
        mAddressGV = (NoScrollGridView) findViewById(R.id.address_gv);

        mTitleView.setBack(this);
        if (getIntent().getStringExtra("addressType").equals("start")) {
            mTitleView.setTitle("出发地");
        } else {
            mTitleView.setTitle("目的地");
        }
        ImageView mMap = mTitleView.getRightImage();

        mMap.setImageResource(R.drawable.map_style);
        //跳转到地图
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_intent = new Intent(ChooseAddressActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putString("mapType", "RequestLocation");
                map_intent.putExtras(b);
                startActivity(map_intent);
            }
        });

        mScrollView.smoothScrollTo(0, 0);

        mAddressLay.setOnClickListener(this);
        mCompanyAddLay.setOnClickListener(this);
    }

    /**
     * 获取历史城市
     */
    private void getHisCity() {
        PromptManager1.showProgressDialog1(this, "加载中");
        AjaxParams params = new AjaxParams();
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if (!TextUtils.isEmpty(sharedPreferences.getString("latitude", "")) && !TextUtils.isEmpty(sharedPreferences.getString("longitude", ""))) {
            String lat = "\"lat\":\"" + sharedPreferences.getString("latitude", "") + "\"";
            String lng = "\"lng\":\"" + sharedPreferences.getString("longitude", "") + "\"";
            params.put("queryJson", "{" + lat + "," + lng + "}");
        } else {
            params.put("queryJson", "{}");
        }
        DidiApp.getHttpManager().sessionPost(ChooseAddressActivity.this, GET_HIS_CITY_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    mHisProvinceList = JSON.parseArray(body, CityListModel.class);

                    initHisCity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                PromptManager1.closeProgressDialog();
            }
        });
    }

    private void initHisCity() {
        if (mHisProvinceList != null && !mHisProvinceList.isEmpty()) {
            mHisProvinceLay.removeAllViews();
            LinearLayout linearLayout = null;
            for (int i = 0; i < mHisProvinceList.size(); i++) {
                if (i % 3 == 0) {
                    linearLayout = (LinearLayout) View.inflate(ChooseAddressActivity.this, R.layout.item_province, null);
                    mHisProvinceLay.addView(linearLayout);
                }
                TextView textView = null;
                if (i % 3 == 0) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text1);
                    if (i == 0) {
                        ImageView locationImg = (ImageView) linearLayout.findViewById(R.id.location_image);
                        locationImg.setVisibility(View.VISIBLE);
                    }
                }
                if (i % 3 == 1) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text2);
                }
                if (i % 3 == 2) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text3);
                }
                final CityListModel model = mHisProvinceList.get(i);
                textView.setText(mHisProvinceList.get(i).getName());
                textView.setVisibility(View.VISIBLE);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        if (getIntent().getStringExtra("addressType").equals("start")) {
                            intent.setAction(CommonRes.RefreshGoodsListWithStartAddr);
                            if (model.getLevel() == 2) {
                                intent.putExtra("startAddressId", "_" + model.getId());
                            } else if (model.getLevel() == 1) {
                                intent.putExtra("startAddressId", model.getId());
                            } else if (model.getLevel() == 3) {
                                intent.putExtra("startAddressId", "__" + model.getId());
                            }
                            intent.putExtra("startAddressName", model.getName());
                        } else {
                            intent.setAction(CommonRes.RefreshGoodsListWithEndAddr);
                            if (model.getLevel() == 2) {
                                intent.putExtra("endAddressId", "_" + model.getId());
                            } else if (model.getLevel() == 1) {
                                intent.putExtra("endAddressId", model.getId());
                            } else if (model.getLevel() == 3) {
                                intent.putExtra("endAddressId", "__" + model.getId());
                            }
                            intent.putExtra("endAddressName", model.getName());
                        }
                        sendBroadcast(intent);
                        finish();
                    }
                });
            }
        }
    }

    /**
     * 获取热点城市
     */
    private void getHotCity() {
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(ChooseAddressActivity.this, GET_HOT_CITY_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                PromptManager1.closeProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    mProvinceList = JSON.parseArray(body, CityListModel.class);

                    initAddressGV();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                PromptManager1.closeProgressDialog();
            }
        });
    }

    private void initAddressGV() {
        ChooseAddressAdapter adapter = new ChooseAddressAdapter(ChooseAddressActivity.this, mProvinceList);
        mAddressGV.setAdapter(adapter);

        mAddressGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLevel == 1) {
                    mProvince = mProvinceList.get(position).getName();
                    mProvinceId = mProvinceList.get(position).getId();
                    if (mProvince.equals("全部")) {
                        Intent intent = new Intent();
                        if (getIntent().getStringExtra("addressType").equals("start")) {
                            intent.setAction(CommonRes.RefreshGoodsListWithStartAddr);
                            intent.putExtra("startAddressId", "__");
                            intent.putExtra("startAddressName", "全部");
                        } else {
                            intent.setAction(CommonRes.RefreshGoodsListWithEndAddr);
                            intent.putExtra("endAddressId", "__");
                            intent.putExtra("endAddressName", "全部");
                        }
                        sendBroadcast(intent);
                        finish();
                    } else {
                        mLevel = 2;
                        mCityList.clear();
                        getCityByProvince(mProvinceId);
                        mAddressTV_.setText(mProvince);
                        mHintTV.setText("当前区域");
                        mAddressBackImg.setVisibility(View.VISIBLE);
                    }
                } else if (mLevel == 2) {
                    mCity = mCityList.get(position).getName();
                    mCityId = mCityList.get(position).getId();
                    if (mCity.equals("全部")) {
                        Intent intent = new Intent();
                        if (getIntent().getStringExtra("addressType").equals("start")) {
                            intent.setAction(CommonRes.RefreshGoodsListWithStartAddr);
                            intent.putExtra("startAddressId", mProvinceId + "_" + mCityId + "_");
                            intent.putExtra("startAddressName", mProvince);
                        } else {
                            intent.setAction(CommonRes.RefreshGoodsListWithEndAddr);
                            intent.putExtra("endAddressId", mProvinceId + "_" + mCityId + "_");
                            intent.putExtra("endAddressName", mProvince);
                        }
                        sendBroadcast(intent);
                        finish();
                    } else {
                        mLevel = 3;
                        mDistrictList.clear();
                        mAddressTV_.setText(mCity);
                        mAddressBackImg.setVisibility(View.VISIBLE);
                        for (CityListModel model : mAddressList) {
                            if (model.getParentId().equals(mCityId)) {
                                mDistrictList.add(model);
                            }
                        }
                        ChooseAddressAdapter adapter = new ChooseAddressAdapter(ChooseAddressActivity.this, mDistrictList);
                        mAddressGV.setAdapter(adapter);
                    }
                } else if (mLevel == 3) {
                    String district = mDistrictList.get(position).getName();
                    String districtId = mDistrictList.get(position).getId();
                    Intent intent = new Intent();
                    if (getIntent().getStringExtra("addressType").equals("start")) {
                        intent.setAction(CommonRes.RefreshGoodsListWithStartAddr);
                        intent.putExtra("startAddressId", mProvinceId + "_" + mCityId + "_" + districtId);
                        if (district.equals("全部")) {
                            intent.putExtra("startAddressName", mCity);
                        } else {
                            intent.putExtra("startAddressName", district);
                        }
                    } else {
                        intent.setAction(CommonRes.RefreshGoodsListWithEndAddr);
                        intent.putExtra("endAddressId", mProvinceId + "_" + mCityId + "_" + districtId);
                        if (district.equals("全部")) {
                            intent.putExtra("endAddressName", mCity);
                        } else {
                            intent.putExtra("endAddressName", district);
                        }
                    }
                    sendBroadcast(intent);
                    finish();
                }
            }
        });

        //返回上一级
        mAddressBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLevel == 2) {
                    mLevel = 1;
                    mAddressBackImg.setVisibility(View.GONE);
                    mAddressTV_.setText("");
                    mHintTV.setText("请选择您所在的区域");
                    mProvince = "";
                    mProvinceId = "";
                    ChooseAddressAdapter adapter = new ChooseAddressAdapter(ChooseAddressActivity.this, mProvinceList);
                    mAddressGV.setAdapter(adapter);
                } else if (mLevel == 3) {
                    mLevel = 2;
                    mAddressTV_.setText(mProvince);
                    mCity = "";
                    mCityId = "";
                    ChooseAddressAdapter adapter = new ChooseAddressAdapter(ChooseAddressActivity.this, mCityList);
                    mAddressGV.setAdapter(adapter);
                }
            }
        });
    }

    private void getCityByProvince(final String provinceId) {
        PromptManager1.showProgressDialog1(this, "加载中");
        AjaxParams params = new AjaxParams();
        params.put("provinceId", provinceId);
        DidiApp.getHttpManager().sessionPost(ChooseAddressActivity.this, GET_CITY_BY_PROVINCE_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                PromptManager1.closeProgressDialog();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    mAddressList = JSON.parseArray(body, CityListModel.class);
                    for (CityListModel model: mAddressList) {
                        if (model.getParentId().equals(provinceId)) {
                            mCityList.add(model);
                        }
                    }
                    ChooseAddressAdapter adapter = new ChooseAddressAdapter(ChooseAddressActivity.this, mCityList);
                    mAddressGV.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                PromptManager1.closeProgressDialog();
                //防止因为省还在界面上指针越界
                ChooseAddressAdapter adapter = new ChooseAddressAdapter(ChooseAddressActivity.this, mCityList);
                mAddressGV.setAdapter(adapter);
            }
        });
    }

    /**
     * 获取历史工司
     */
    private void getHisCompany() {
        PromptManager1.showProgressDialog1(ChooseAddressActivity.this, "加载中");
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(ChooseAddressActivity.this, GET_HIS_COMPANY_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    mHisCompanyList = JSON.parseArray(body, KVModel.class);
                    initHisCompany();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                PromptManager1.closeProgressDialog();
            }
        });
    }

    private void initHisCompany() {
        if (mHisCompanyList != null && !mHisCompanyList.isEmpty()) {
            mHisProvinceLay.removeAllViews();
            LinearLayout linearLayout = null;
            for (int i = 0; i < mHisCompanyList.size(); i++) {
                if (i % 3 == 0) {
                    linearLayout = (LinearLayout) View.inflate(ChooseAddressActivity.this, R.layout.item_province, null);
                    mHisProvinceLay.addView(linearLayout);
                }
                TextView textView = null;
                if (i % 3 == 0) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text1);
                }
                if (i % 3 == 1) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text2);
                }
                if (i % 3 == 2) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text3);
                }
                textView.setText(mHisCompanyList.get(i).getKey());
                textView.setVisibility(View.VISIBLE);
                final KVModel model = mHisCompanyList.get(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveCompany(model);
                        Intent intent = new Intent();
                        if (getIntent().getStringExtra("addressType").equals("start")) {
                            intent.setAction(CommonRes.RefreshGoodsListWithStartCorp);
                            intent.putExtra("senderCopId", model.getValue());
                            intent.putExtra("senderCopName", model.getKey());
                        } else {
                            intent.setAction(CommonRes.RefreshGoodsListWithEndCorp);
                            intent.putExtra("recipCopId", model.getValue());
                            intent.putExtra("recipCopName", model.getKey());
                        }
                        sendBroadcast(intent);
                        finish();
                    }
                });
            }
        }
    }

    /**
     * 获取历史工司
     */
    private void getCompany() {
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(ChooseAddressActivity.this, GET_COMPANY_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                PromptManager1.closeProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
                    mCompanyList = JSON.parseArray(body, KVModel.class);
                    initCompany();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                PromptManager1.closeProgressDialog();
            }
        });
    }

    private void initCompany() {
        if (mCompanyList != null && !mCompanyList.isEmpty()) {
            mProvinceLay.removeAllViews();
            LinearLayout linearLayout = null;
            for (int i = 0; i < mCompanyList.size(); i++) {
                if (i % 3 == 0) {
                    linearLayout = (LinearLayout) View.inflate(ChooseAddressActivity.this, R.layout.item_province, null);
                    mProvinceLay.addView(linearLayout);
                }
                TextView textView = null;
                if (i % 3 == 0) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text1);
                }
                if (i % 3 == 1) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text2);
                }
                if (i % 3 == 2) {
                    textView = (TextView) linearLayout.findViewById(R.id.province_text3);
                }
                textView.setText(mCompanyList.get(i).getKey());
                textView.setVisibility(View.VISIBLE);
                final KVModel model = mCompanyList.get(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveCompany(model);
                        Intent intent = new Intent();
                        if (getIntent().getStringExtra("addressType").equals("start")) {
                            intent.setAction(CommonRes.RefreshGoodsListWithStartCorp);
                            intent.putExtra("senderCopId", model.getValue());
                            intent.putExtra("senderCopName", model.getKey());
                        } else {
                            intent.setAction(CommonRes.RefreshGoodsListWithEndCorp);
                            intent.putExtra("recipCopId", model.getValue());
                            intent.putExtra("recipCopName", model.getKey());
                        }
                        sendBroadcast(intent);
                        finish();
                    }
                });
            }
        }
    }

    /**
     * 保存历史公司
     */
    private void saveCompany(KVModel model) {
        AjaxParams params = new AjaxParams();
        params.put("subCompanyName", model.getKey());
        params.put("subCompanyId", model.getValue());
        DidiApp.getHttpManager().sessionPost(ChooseAddressActivity.this, SAVE_COMPANY_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {

            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_layout:
                mScrollView.setBackgroundResource(R.color.white);
                mHisProvinceTag.setText("- 当前/历史 -");
                mAddressTV.setTextColor(getResources().getColor(R.color.title_background));
                mAddressLine.setVisibility(View.VISIBLE);
                mCompanyAddLine.setVisibility(View.GONE);
                mProvinceTag.setVisibility(View.GONE);
                mProvinceLay.setVisibility(View.GONE);
                mCompanyAddTV.setTextColor(getResources().getColor(R.color.gray));
                mHisProvinceLay.removeAllViews();
                mChooseAddressLay.setVisibility(View.VISIBLE);
                if (mHisProvinceList == null) {
                    getHisCity();
                } else {
                    initHisCity();
                }
                break;
            case R.id.company_address_layout:
                mScrollView.setBackgroundResource(R.color.home_background);
                mHisProvinceTag.setText("- 历史 -");
                mProvinceTag.setText("- 红狮子公司 -");
                mAddressTV.setTextColor(getResources().getColor(R.color.gray));
                mAddressLine.setVisibility(View.GONE);
                mCompanyAddLine.setVisibility(View.VISIBLE);
                mProvinceTag.setVisibility(View.VISIBLE);
                mProvinceLay.setVisibility(View.VISIBLE);
                mCompanyAddTV.setTextColor(getResources().getColor(R.color.title_background));
                mProvinceLay.removeAllViews();
                mHisProvinceLay.removeAllViews();
                mChooseAddressLay.setVisibility(View.GONE);
                if (mHisCompanyList == null || mCompanyList == null) {
                    getHisCompany();
                    getCompany();
                } else {
                    initHisCompany();
                    initCompany();
                }
                break;
            default:
                break;
        }
    }
}
