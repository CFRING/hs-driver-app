package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.wheelviewlib.R;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.OnWheelChangedListener;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.WheelViewLib;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.adapter.DateArrayAdapter;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model.AreaModelLib;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model.CityModelLib;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model.DistrictModelLib;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model.ProvinceModel;
import com.hongshi.wuliudidi.wheelviewlib.wheelview.library.utils.LayoutParamsUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WheelAddressDialog extends Dialog implements OnWheelChangedListener, View.OnClickListener {

	public interface OnCancelBtnClickListener{
		void onClick();
	}

	public interface OnSureBtnClickListener{
		void onClick(String province, String city, String district);
	}

	public interface OnSureBtnClickListener1{
		void onClick(String province, String provinceId, String city, String cityId, String district, String districtId);
	}

	private OnCancelBtnClickListener mOnCancelBtnClickListener;
	private OnSureBtnClickListener mOnSureBtnClickListener;
	private OnSureBtnClickListener1 mOnSureBtnClickListener1;
	private static List<ProvinceModel> provinceList = null;
	private static List<com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model.CityModelLib> cityList = null;
	private static List<DistrictModelLib> districtList = null;
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
//	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName ="";

	/**
	 * 当前区的邮政编码
	 */
//	protected String mCurrentZipCode ="";
	private TextView mCancelTxt, mSureTxt;
	WheelViewLib mViewProvince;
	WheelViewLib mViewCity;
	WheelViewLib mViewDistrict;
	private String cityJson;
	private DateArrayAdapter mCityAdapter,mAreaAdapter;

	private Context mContext;
	public WheelAddressDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public WheelAddressDialog(Context context, String cityJson, int theme) {
		super(context, theme);
		this.mContext = context;
		this.cityJson = cityJson;
		init();
	}
	private void init() {
		setContentView(R.layout.wheel_address_dialog);
		mCancelTxt = (TextView) findViewById(R.id.cancel_btn);
		mSureTxt = (TextView) findViewById(R.id.sure_btn);
		mViewProvince = (WheelViewLib) findViewById(R.id.wheel_id_province);
		mViewCity = (WheelViewLib) findViewById(R.id.wheel_id_city);
		mViewDistrict = (WheelViewLib) findViewById(R.id.wheel_id_district);
		LayoutParamsUtil layoutParamsUtil = new LayoutParamsUtil(mContext);
		layoutParamsUtil.setWheelAddressDialog((RelativeLayout) findViewById(R.id.lib_title_layout), mCancelTxt, mSureTxt,
				mViewProvince, mViewCity, mViewDistrict);

		// 添加change事件
		mViewProvince.addChangingListener(this);
		mViewProvince.setVisibleItems(5);
		// 添加change事件
		mViewCity.addChangingListener(this);
		mViewCity.setVisibleItems(5);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		mViewDistrict.setVisibleItems(5);
		mViewProvince.setDrawShadows(false);
		mViewCity.setDrawShadows(false);
		mViewDistrict.setDrawShadows(false);
		mCancelTxt.setOnClickListener(this);
		mSureTxt.setOnClickListener(this);
		initProvinceDatas();
	}

	@Override
	public void onChanged(WheelViewLib wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			mViewProvince.setViewAdapter(new DateArrayAdapter(mContext, mProvinceDatas, newValue));
			updateCities();
		} else if (wheel == mViewCity) {
			mViewCity.setViewAdapter(new DateArrayAdapter(mContext, mCitisDatasMap.get(mCurrentProviceName),newValue));
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mAreaAdapter = new DateArrayAdapter(mContext, mDistrictDatasMap.get(mCurrentCityName), newValue);
			mViewDistrict.setViewAdapter(mAreaAdapter);
//            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
//		Toast.makeText(mContext,"当前的地址=="+mCurrentProviceName + mCurrentCityName
//				+ mCurrentDistrictName,Toast.LENGTH_SHORT).show();
//		address_text.setText(mCurrentProviceName + mCurrentCityName
//				+ mCurrentDistrictName);
//		address_text.setTextColor(getResources().getColor(R.color.gray));
	}

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas(){
		List<ProvinceModel> provinceList = null;
		provinceList = parseAreaToProvinceList(cityJson);
		initData(provinceList);
	}
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	protected void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
//		if (mAreaAdapter != null){
//			mAreaAdapter.setChangeData(areas);
//		}else{
			mAreaAdapter = new DateArrayAdapter(mContext, areas, 0);
			mViewDistrict.setViewAdapter(mAreaAdapter);
//		}
		mViewDistrict.setCurrentItem(0);
		if (mDistrictDatasMap.get(mCurrentCityName).length > 0) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
		} else {
			mCurrentDistrictName = "";
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	protected void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		// Log.i("lihe", "xedssa"+mCurrentProviceName);
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
//		if (mCityAdapter != null){
//			mCityAdapter.setChangeData(cities);
//		}else{
			mCityAdapter = new DateArrayAdapter(mContext, cities, 0);
			mViewCity.setViewAdapter(mCityAdapter);
//		}
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
	void initData(List<ProvinceModel> provinceList){

		//*/ 初始化默认选中的省、市、区
		if (provinceList!= null && !provinceList.isEmpty()) {
			mCurrentProviceName = provinceList.get(0).getName();
			List<CityModelLib> cityList = provinceList.get(0).getCityList();
			if (cityList!= null && !cityList.isEmpty()) {
				mCurrentCityName = cityList.get(0).getName();
				List<DistrictModelLib> districtList = cityList.get(0).getDistrictList();
				if (districtList != null && districtList.size() > 0) {
					mCurrentDistrictName = districtList.get(0).getName();
				} else {
					mCurrentDistrictName = "";
				}
//				mCurrentZipCode = districtList.get(0).getZipcode();
			}
		}
		//*/
		mProvinceDatas = new String[provinceList.size()];
		for (int i=0; i< provinceList.size(); i++) {
			// 遍历所有省的数据
			mProvinceDatas[i] = provinceList.get(i).getName();
			List<CityModelLib> cityList = provinceList.get(i).getCityList();
			String[] cityNames = new String[cityList.size()];
			for (int j=0; j< cityList.size(); j++) {
				// 遍历省下面的所有市的数据
				cityNames[j] = cityList.get(j).getName();
				List<DistrictModelLib> districtList = cityList.get(j).getDistrictList();
				String[] distrinctNameArray = new String[districtList.size()];
				DistrictModelLib[] distrinctArray = new DistrictModelLib[districtList.size()];
				for (int k=0; k<districtList.size(); k++) {
					// 遍历市下面所有区/县的数据
					DistrictModelLib districtModel = new DistrictModelLib(districtList.get(k).getName(), districtList.get(k).getId(), districtList.get(k).getZipcode());
					// 区/县对于的邮编，保存到mZipcodeDatasMap
//    				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
					distrinctArray[k] = districtModel;
					distrinctNameArray[k] = districtModel.getName();
				}
				// 市-区/县的数据，保存到mDistrictDatasMap
				mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
			}
			// 省-市的数据，保存到mCitisDatasMap
			mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
		}
		mViewProvince.setViewAdapter(new DateArrayAdapter(mContext, mProvinceDatas, 0));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
//		updateAreas();
	}
	//把Json地区信息转换成List<ProvinceModel>
	private  List<ProvinceModel> parseAreaToProvinceList(String t){
		JSONObject jsonObject;
		List<AreaModelLib> areaList = null;
		try {
			jsonObject = new JSONObject(t);
			String body = jsonObject.getString("body");

			areaList = JSON.parseArray(body, AreaModelLib.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (areaList != null && areaList.size() > 0) {
			//全部只有一个provinceList
			provinceList = new ArrayList<>();
			for (int i = 0; i < areaList.size(); i++) {
				AreaModelLib provinceModel = areaList.get(i);
				if (provinceModel.getLevel() == 1) {
					//一个ProvinceModel里只有一个cityList
					cityList = new ArrayList<>();
					for (int j = 0; j<areaList.size(); j++){
						AreaModelLib cityModel = areaList.get(j);
						if (cityModel.getLevel() == 2 &&
								cityModel.getParentId().equals(provinceModel.getId())){
							//一个CityModel里只有一个districtList
							districtList = new ArrayList<>();
							for (int k = 0; k<areaList.size(); k++){
								AreaModelLib districtModel = areaList.get(k);
								if (districtModel.getLevel() == 3 &&
										districtModel.getParentId().equals(cityModel.getId())){
									districtList.add(new DistrictModelLib(districtModel.getName(), districtModel.getId(), k + ""));
								}
							}
							cityList.add(new CityModelLib(cityModel.getName(), cityModel.getId(),  districtList));
						}
					}
					provinceList.add(new ProvinceModel(provinceModel.getName(), provinceModel.getId(), cityList));
				}
			}
		}
		return provinceList;
	}

	public void setOnCancelBtnClickListener(OnCancelBtnClickListener listener){
		mOnCancelBtnClickListener = listener;
	}

	public void setOnSureBtnClickListener(OnSureBtnClickListener listener){
		mOnSureBtnClickListener = listener;
	}

	public void setOnSureBtnClickListener1(OnSureBtnClickListener1 listener){
		mOnSureBtnClickListener1 = listener;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.cancel_btn) {
			if (mOnCancelBtnClickListener != null){
				mOnCancelBtnClickListener.onClick();
			}
			cancel();
		}else if (i == R.id.sure_btn){
			if (mOnSureBtnClickListener != null) {
				mOnSureBtnClickListener.onClick(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
			}
			if (mOnSureBtnClickListener1 != null) {
				String provinceId = "", cityId = "", districtId = "";
				for (int j = 0; j < provinceList.size(); j++){
					if (mCurrentProviceName.equals(provinceList.get(j).getName())) {
						provinceId = provinceList.get(j).getId();
						cityList = provinceList.get(j).getCityList();
					}
				}
				for (int j = 0; j < cityList.size();j++) {
					if (mCurrentCityName.equals(cityList.get(j).getName())){
						cityId = cityList.get(j).getId();
						districtList = cityList.get(j).getDistrictList();
					}
				}
				for (int j = 0; j < districtList.size();j++) {
					if (mCurrentDistrictName.equals(districtList.get(j).getName())){
						districtId = districtList.get(j).getId();
					}
				}
				mOnSureBtnClickListener1.onClick(mCurrentProviceName, provinceId, mCurrentCityName, cityId, mCurrentDistrictName, districtId);
			}
			cancel();
		}
	}
}
