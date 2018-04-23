package com.hongshi.wuliudidi.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.ChooseAreaListViewAdapter;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChooseAreaCallBack;
import com.hongshi.wuliudidi.model.AreaBackstageModel;
import com.hongshi.wuliudidi.model.AreaModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.LocalPropertyUtil;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaPopupWindow extends PopupWindow{
	private ChooseAreaCallBack callBack;
	private Activity mContext;
	private ListView provinceListView, cityListView, countyListView;
	private ChooseAreaListViewAdapter provinceAdapter, cityAdapter, countyAdapter;
	private TextView currentAreaText;
	private Map<AreaModel, Map<AreaModel, List<AreaModel>>> map = new HashMap<AreaModel, Map<AreaModel,List<AreaModel>>>();
	private List<AreaModel> provinceList = new ArrayList<AreaModel>();
	private List<AreaModel> cityList = new ArrayList<AreaModel>();
	private List<AreaModel> countyList = new ArrayList<AreaModel>();
	private AreaModel currentProvince, currentCity, currentCounty;
	private String allArea;
	//遮罩
	private View maskArea;
	private AreaType areaTag;
	private final MyHandler mHandler = new MyHandler(this);
	int new_address_version = 0;
	
	public ChooseAreaPopupWindow(Activity context, AreaType areaTag, ChooseAreaCallBack callBack) {
		super(context);
		this.mContext = context;
		this.callBack = callBack;
		this.areaTag = areaTag;
		init();
	}
	
	 private void init(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View mView = inflater.inflate(R.layout.choose_area_dialog, null);
		setContentView(mView);
		setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new ColorDrawable());
		setFocusable(true);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setOutsideTouchable(true);
		
		currentAreaText = (TextView) mView.findViewById(R.id.current_area_text);
		
		provinceListView = (ListView) mView.findViewById(R.id.province_listview);
		
		cityListView = (ListView) mView.findViewById(R.id.city_listview);
		
		countyListView = (ListView) mView.findViewById(R.id.county_listview);
		
		maskArea = (View) mView.findViewById(R.id.mask_area);
		maskArea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		setOnItemClick();
		getData();
	}
	
	public void setArea(String str){
		currentAreaText.setText("当前区域： " + str);
	}
	
	private void initInterface(){
		//读取完区域的数据以后，把省份罗列给用户
		AreaModel allArea = new AreaModel();
		allArea.setAreaCode("");
		allArea.setAreaText(mContext.getResources().getString(R.string.all));
		provinceList.add(allArea);
		for (AreaModel theProvince : map.keySet()) {
			provinceList.add(theProvince);
		}
		Collections.sort(provinceList, new Comparator<AreaModel>() {
			@Override
			public int compare(AreaModel lhs, AreaModel rhs) {
				return lhs.getAreaCode().compareTo(rhs.getAreaCode());
			}
		});
		provinceAdapter = new ChooseAreaListViewAdapter(mContext, provinceList, R.layout.choose_area_item1,
				ChooseAreaListViewAdapter.AreaType.province);
		provinceListView.setAdapter(provinceAdapter);
	}
	
	/**
	 * 自查用，检查解析是否正确
	 */
	private void showMap(){
		for (AreaModel theProvince : map.keySet()) {
//			LogUtil.myLog("zhuoran", theProvince.getAreaText() + ":" + theProvince.getAreaCode());
			Map<AreaModel, List<AreaModel>> citymap = map.get(theProvince);
			for (AreaModel theCity : citymap.keySet()) {
//				LogUtil.myLog("zhuoran", theCity.getAreaText() + ":" + theCity.getAreaCode());
				List<AreaModel> countymap = citymap.get(theCity);
				for (AreaModel theCounty : countymap) {
//					LogUtil.myLog("zhuoran", theCounty.getAreaText() + ":" + theCounty.getAreaCode());
				}
			}
		}
	}
	
	private void getData(){
		Thread thread  = new Thread(new Runnable() {
			@Override
			public void run() {
				final String update_url = GloableParams.HOST + "commonservice/basic/featchInfo.do?";
				AjaxParams param = new AjaxParams();
				DidiApp.getHttpManager().sessionPost(mContext, update_url, param, new ChildAfinalHttpCallBack() {

					@Override
					public void data(String t) {
						Log.d("huiyuan", "返回的地址版本更新数据：" + t);
						int local_version = mContext.getSharedPreferences("config", Context.MODE_PRIVATE)
								.getInt("addressVersion",0);
						if(t != null && !t.equals("")){
							try {
								JSONObject object = new JSONObject(t);
								boolean success = object.optBoolean("success");
								if(success){
									String body = object.optString("body");
									if(body != null && !body.equals("")){
										JSONObject bodyJson = new JSONObject(body);
										new_address_version = bodyJson.optInt("addressVersion");
									}
								}else {
									Toast.makeText(mContext,"获取地址数据失败",Toast.LENGTH_LONG);
								}
							}catch (JSONException e){
								e.printStackTrace();
							}
						}

						if(new_address_version > local_version){
							//有更新
							Message msg = new Message();
							msg.what = CommonRes.GetAllAreaFailed;
							mHandler.sendMessage(msg);
						}else {
							//无更新
							allArea = LocalPropertyUtil.getProperty(mContext, LocalPropertyUtil.AllArea);
							Message msg = new Message();
							if(allArea != null && allArea.length() > 0){
								msg.what = CommonRes.GetAllAreaSuccess;
								mHandler.sendMessage(msg);
							}else{
								msg.what = CommonRes.GetAllAreaFailed;
								mHandler.sendMessage(msg);
							}
						}
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						ToastUtil.show(mContext, mContext.getResources().getString(R.string.please_check_your_network));
						dismiss();
					}
				});
			}
		});
		thread.start();
	}

	private void requestData(){
			final String url = GloableParams.HOST + "carrier/app/areaQuery/buildAreaSelector.do?";
			AjaxParams params = new AjaxParams();
			DidiApp.getHttpManager().sessionPost(mContext, url, params, new ChildAfinalHttpCallBack() {

				@Override
				public void data(String t) {
					allArea = t;
					LocalPropertyUtil.saveProperty(mContext, LocalPropertyUtil.AllArea, allArea);
					analysis();
					mContext.getSharedPreferences("config", Context.MODE_PRIVATE)
							.edit().putInt("addressVersion",new_address_version).commit();
				}

				@Override
				public void onFailure(String errCode, String errMsg, Boolean errSerious) {
					ToastUtil.show(mContext, mContext.getResources().getString(R.string.please_check_your_network));
					dismiss();
				}
			});
	}
	
	private void analysis(){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(allArea);
			String body = jsonObject.getString("body");
			
			AreaBackstageModel backstageModel = JSON.parseObject(body, AreaBackstageModel.class);
			for (String  provinceName : backstageModel.getProvinceIDCityMap().keySet()) {
				//后台的市-区map
				Map<String, Map<String, String>> cityAndCounty = backstageModel.getProvinceIDCityMap().get(provinceName);
				AreaModel theProvince = new AreaModel();
				theProvince.setAreaText(provinceName);
				String provinceCodeStr = "";
				
				Map<AreaModel, List<AreaModel>> cityAndCountyMap = new HashMap<AreaModel, List<AreaModel>>();
				
				for(String cityName : cityAndCounty.keySet()){
					//后台的区-代码map
					Map<String, String> county = cityAndCounty.get(cityName);
					AreaModel theCity = new AreaModel();
					theCity.setAreaText(cityName);
					String cityCodeStr = "";
					
					List<AreaModel> countyMap = new ArrayList<AreaModel>();
					
					for(String countyName : county.keySet()){
						AreaModel theCounty = new AreaModel();
						String longCodeStr = county.get(countyName);
						theCounty.setAreaText(countyName);
						theCounty.setAreaCode(longCodeStr);
						countyMap.add(theCounty);
						String[] codeStr = longCodeStr.split("_");
						//这个区所属的市的代码
						cityCodeStr = codeStr[0] + "_" + codeStr[1];
					}
					theCity.setAreaCode(cityCodeStr);
					cityAndCountyMap.put(theCity, countyMap);
					
					//这个市所属的省的代码
					String[] codeStr = theCity.getAreaCode().split("_");
					provinceCodeStr = codeStr[0];
				}
				theProvince.setAreaCode(provinceCodeStr);
				map.put(theProvince, cityAndCountyMap);
			}
			
			
			showMap();
			initInterface();
		} catch (JSONException e) {
			e.printStackTrace();
			
		}
	}
	
	public void setCurrentArea(String area){
		currentAreaText.setText(area);
	}


	private void setOnItemClick(){
		provinceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				provinceAdapter.setCurrentItem(position);
				provinceAdapter.notifyDataSetInvalidated();
				currentProvince = provinceList.get(position);
				if(position <= 0){
					//如果选了“全部”
					callBack.getResult(currentProvince, areaTag);
					dismiss();
				}else if(position > 0){
					//当某个省被选中,刷新市列表、清空区列表
					try {
						Map<AreaModel, List<AreaModel>> cityMap = map.get(currentProvince);
						cityList.clear();
						for (AreaModel city : cityMap.keySet()) {
							cityList.add(city);
						}
					} catch (Exception e) {
						cityList.clear();
					}
					Collections.sort(cityList, new Comparator<AreaModel>() {
						@Override
						public int compare(AreaModel lhs, AreaModel rhs) {
							return lhs.getAreaCode().compareTo(rhs.getAreaCode());
						}
					});
					cityAdapter = new ChooseAreaListViewAdapter(mContext, cityList, R.layout.choose_area_item1,
							ChooseAreaListViewAdapter.AreaType.city);
					cityListView.setAdapter(cityAdapter);
					
					countyList.clear();
					countyAdapter = new ChooseAreaListViewAdapter(mContext, countyList, R.layout.choose_area_item1,
							ChooseAreaListViewAdapter.AreaType.county);
					countyListView.setAdapter(countyAdapter);
				}
			}
		});
		
		//当某个城市被选中，刷新区列表
		cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cityAdapter.setCurrentItem(position);
				cityAdapter.notifyDataSetInvalidated();
				currentCity = cityList.get(position);
				countyList.clear();
				try {
					//刷新区列表
					List<AreaModel> tempList = map.get(currentProvince).get(currentCity);
					for(int i = 0; i <tempList.size(); i++){
						countyList.add(tempList.get(i));
					}
				} catch (Exception e) {
				}
				//第一个选项是全城
				AreaModel wholeCity = currentCity.Copy();
				wholeCity.setAreaText("全城");
				countyList.add(0, wholeCity);
				Collections.sort(countyList, new Comparator<AreaModel>() {
					@Override
					public int compare(AreaModel lhs, AreaModel rhs) {
						return lhs.getAreaCode().compareTo(rhs.getAreaCode());
					}
				});
				countyAdapter = new ChooseAreaListViewAdapter(mContext, countyList, R.layout.choose_area_item1,
						ChooseAreaListViewAdapter.AreaType.county);
				countyListView.setAdapter(countyAdapter);
			}
		});
		
		//当某个区被选中
		countyListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				countyAdapter.setCurrentItem(position);
				if(position == 0){
					callBack.getResult(currentCity, areaTag);
				}else{
					currentCounty = countyList.get(position);
					callBack.getResult(currentCounty, areaTag);
				}
				mHandler.removeCallbacksAndMessages(null);
				dismiss();
			}
		});
	}


	private static class MyHandler extends Handler {
		private ChooseAreaPopupWindow popupWindow;

		public MyHandler(ChooseAreaPopupWindow popupWindow) {
			this.popupWindow = popupWindow;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case CommonRes.GetAllAreaSuccess:
					popupWindow.analysis();
					break;
				case CommonRes.GetAllAreaFailed:
					popupWindow.requestData();
					break;
				default:
					break;
			}
		}
	}

	public enum AreaType{
		start, end;
	}
}
