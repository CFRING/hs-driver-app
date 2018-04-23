package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMyLocationChangeListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.maps2d.selfdefineoverlay.DrivingRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.MapSearchListAdapter;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.model.GoodsModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.AMapUtil;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author huiyuan
 */
public class MapActivity extends Activity implements AMapLocationListener,
		OnPoiSearchListener, LocationSource, OnMarkerClickListener,
		OnInfoWindowClickListener, OnMyLocationChangeListener,
		InfoWindowAdapter, OnMapLoadedListener, OnCameraChangeListener,
		OnMapClickListener, TextWatcher, OnGeocodeSearchListener{

	private LocationManagerProxy aMapLocManager = null;
	private MapView mapView;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	// 判断是否是第一次定位
	private boolean isFirst = true;
	private Button mRightImage;
	// 当前页面，从0开始计数
	private int currentPage = 0;
	// Poi查询条件类
	private PoiSearch.Query query;
	// 输入搜索关键字
	private AutoCompleteTextView searchText;
	// POI搜索
	private PoiSearch poiSearch;
	// 搜索时进度条
	private ProgressDialog progDialog = null;
	// poi返回的结果
	private PoiResult poiResult;
	private Marker currentMarker;
	private RelativeLayout searchLayout;
	private DiDiTitleView mTitle;
	//地图需要实现何种功能
	private MapType mMapType;
	private String neighborhoodSearchingKeyWord;
	private static String query_url = GloableParams.HOST + "carrier/auction/listMap.do?";
	List<GoodsModel> goodsList = new ArrayList<GoodsModel>();
	private GeocodeSearch mSearch;
	private boolean isList = true;
	//实时定位
	private AMapLocation currentLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setTitle("地图信息");
		mTitle.setBack(MapActivity.this);

		mRightImage = (Button) findViewById(R.id.right_button);
		searchText = (AutoCompleteTextView) findViewById(R.id.search_edit);

		searchLayout = (RelativeLayout) findViewById(R.id.search_layout);
		
		mapView = (MapView) findViewById(R.id.map);
		// 此方法必须重写
		mapView.onCreate(savedInstanceState);
		
		try {
			Bundle mBundle = getIntent().getExtras();
			mMapType = MapType.valueOf(mBundle.getString("mapType"));
			if(mMapType == MapType.StartEnd){
				LLBegin = new LatLng(Double.parseDouble(mBundle.getString("startLat")),
						Double.parseDouble(mBundle.getString("startLng")));
				startProvinceAndCity = mBundle.getString("startProvinceAndCity");
				startAddr = mBundle.getString("startAddr");
				
				LLEnd = new LatLng(Double.parseDouble(mBundle.getString("endLat")),
						Double.parseDouble(mBundle.getString("endLng")));
				endProvinceAndCity = mBundle.getString("endProvinceAndCity");
				endAddr = mBundle.getString("endAddr");
			}

			if(mMapType == MapType.NeighborhoodSearching){
				try{
					neighborhoodSearchingKeyWord = mBundle.getString("keyWord");
				}catch (Exception e){
					neighborhoodSearchingKeyWord = "";
				}
			}
		} catch (Exception e) {
			mMapType = MapType.RequestLocation;
		}
		
		
		init();
		aMapLocManager = LocationManagerProxy.getInstance(this);
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		if(mMapType == MapType.RequestLocation){
			mTitle.setVisibility(View.GONE);
			searchLayout.setVisibility(View.VISIBLE);
			aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
		if(mMapType == MapType.NeighborhoodSearching){
			mTitle.setVisibility(View.VISIBLE);
			searchLayout.setVisibility(View.GONE);
			aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
		if(mMapType == MapType.StartEnd){
			mTitle.setVisibility(View.VISIBLE);
			searchLayout.setVisibility(View.GONE);
			display();
		}
	}

	private void initMapListener() {
		aMap.setOnMapLoadedListener(this);
		aMap.setOnCameraChangeListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		// 设置自定义InfoWindow样式
		aMap.setInfoWindowAdapter(this);
		aMap.setOnMyLocationChangeListener(this);
		aMap.setOnMapClickListener(this);
	}

	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// myLocationStyle.myLocationIcon(BitmapDescriptorFactory
		// .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		// 设置圆形的边框颜色
		myLocationStyle.strokeColor(Color.BLACK);
		// 设置圆形的填充颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		// 设置圆形的边框粗细
		myLocationStyle.strokeWidth(1.0f);
		aMap.setMyLocationStyle(myLocationStyle);
		// 设置定位监听
		aMap.setLocationSource(this);
		aMap.moveCamera(CameraUpdateFactory.zoomTo(11));
		// 设置默认定位按钮是否显示
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setMyLocationEnabled(true);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		// 添加文本输入框监听事件
		searchText.addTextChangedListener(this);
		// 搜索按钮
		mRightImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isList){
					finish();
				}else{
					// 要输入的poi搜索关键字
					String keyWord = AMapUtil.checkEditText(searchText);
					doSearchQuery(keyWord);
					//点击搜索按钮后通知系统收起输入法
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
					if(imm.isActive()){
						InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						inputMethodManager.hideSoftInputFromWindow(MapActivity.this.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
				
			}
		});
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		initMapListener();
		
		mSearch = new GeocodeSearch(this);
		mSearch.setOnGeocodeSearchListener(this);
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery(String keyWord) {
		// 显示进度框
		showProgressDialog(keyWord);
		currentPage = 0;
		String currentCity = "";
		if(currentLocation != null){
			currentCity = currentLocation.getCityCode();
		}
		// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query = new PoiSearch.Query(keyWord, "", currentCity);
		// 设置每页最多返回多少条poiitem
		query.setPageSize(10);
		// 设置查第一页
		query.setPageNum(currentPage);
		poiSearch = new PoiSearch(this, query);
		if(mMapType == MapType.NeighborhoodSearching && currentLocation != null){
			//如果是周边热点搜索，范围缩小到方圆3千米
			double lat = currentLocation.getLatitude();
			double lon = currentLocation.getLongitude();
			PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 3000);
			//设置周边搜索的中心点以及区域
			poiSearch.setBound(searchBound);
		}
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	/**
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			// this.aMapLocation = location;// 判断超时机制
			currentLocation = location;
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			if (isFirst) {
				setUpMap();
				if(mMapType == MapType.NeighborhoodSearching){
					doSearchQuery(neighborhoodSearchingKeyWord);
				}
//				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
//						30.331505, 120.129211)));
				
				MarkerOptions markerOption1 = new MarkerOptions();
				markerOption1.position(new LatLng(30.331505,120.129211));
				markerOption1.title("上海市").snippet("上海：34.341568, 108.940174");
				
				MarkerOptions markerOption = new MarkerOptions();
				markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
				markerOption.draggable(true);
				markerOption1.draggable(true);
				//aMap.addMarker(markerOption);
				//aMap.addMarker(markerOption1);
				aMap.setOnMarkerClickListener(this);
				isFirst = false;
			}
			// 显示系统小蓝点
			mListener.onLocationChanged(location);
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
					+ "\n精    度    :" + location.getAccuracy() + "米"
					+ "\n定位方式:" + location.getProvider() + "\n定位时间:"
					+ AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
					+ cityCode + "\n位置描述:" + desc + "\n省:"
					+ location.getProvince() + "\n市:" + location.getCity()
					+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
					.getAdCode());
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (currentMarker != null) {
			currentMarker.hideInfoWindow();
		}
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
	}

	@Override
	public void onCameraChangeFinish(CameraPosition position) {

		LatLng target = position.target;
		//获取中心点位置
		double latitude = target.latitude;
		double longitude = target.longitude;
		if(mMapType == MapType.RequestLocation){
			queryAuctions(latitude, longitude);
		}
	}

	private void queryAuctions(double latitude, double longitude){
		AjaxParams params = new AjaxParams();
		params.put("lat", String.valueOf(latitude));
		params.put("lng", String.valueOf(longitude));

		DidiApp.getHttpManager().sessionPost(MapActivity.this, query_url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				try {
					JSONObject jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					if(goodsList.size() > 0){
						goodsList.clear();
					}
					goodsList = JSON.parseArray(body, GoodsModel.class);
					MarkerOptions[] mOptions = new MarkerOptions[goodsList.size()];
					Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.goods_icon);
					Bitmap resiBitmap = ImageUtil.resizeBitmap(MapActivity.this, bitMap, bitMap.getWidth(), bitMap.getHeight());
					for(int i = 0; i < goodsList.size(); i++){
						GoodsModel model = goodsList.get(i);
						mOptions[i] = new MarkerOptions()
							.position(model.getLatLng())
							.title(model.getRecvProvince() + " " + model.getRecvCity())
							.snippet(model.getGoodsName() + "  "
									+ Util.formatDoubleToString(model.getGoodsAmount(), model.getUnitText())
									+ model.getUnitText())
							.icon(BitmapDescriptorFactory.fromBitmap(resiBitmap));
						
					}
					
					new AsyncTask<MarkerOptions[], Void, MarkerOptions[]>() {
						@Override
						protected MarkerOptions[] doInBackground(MarkerOptions[]... params) {
							return params[0];
						}
						
						@Override
						protected void onPostExecute(MarkerOptions[] options){
							MarkerData md = new MarkerData();
							for(int i = 0; i < options.length; i++){
								md.setType(MarkerType.goods_source);
								md.setContent(goodsList.get(i).getAuctionId());
								Marker marker = aMap.addMarker(options[i]);
								marker.setObject(md);
							}
						}
					}.execute(mOptions);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中
//		LatLngBounds bounds = new LatLngBounds.Builder()
//				.include(Constants.XIAN).include(Constants.CHENGDU)
//				.include(latlng).include(Constants.ZHENGZHOU)
//				.include(Constants.BEIJING).build();
//		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
	}


	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.marker_info, null);
		MarkerData md;
		try {
			md = (MarkerData) marker.getObject();
			MarkerType type = md.getType();
			if(type == MarkerType.goods_source){
				ImageView arrowImage = (ImageView) infoWindow.findViewById(R.id.my_item_right);
				arrowImage.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			
		}
		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(R.color.theme_color, 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(R.color.black, 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(15);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		//点击气泡跳转到响应竞价单详情页面
		MarkerData md;
		try {
			md = (MarkerData) arg0.getObject();
		} catch (Exception e) {
			md = null;
		}
//		if(md.getType() == MarkerType.goods_source){
//			String auctionID = md.getContent();
//			if(auctionID != null && auctionID.length() > 0){
//				Intent intent;
//				if(!Util.isLogin(MapActivity.this)){
//					intent = new Intent(MapActivity.this,LoginActivity.class);
//					startActivity(intent);
//				}
//				intent = new Intent(MapActivity.this, AuctionDetailsActivity.class);
//				intent.putExtra("auctionId", auctionID);
//				startActivity(intent);
//			}
//		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		currentMarker = marker;
		marker.showInfoWindow();
		return false;
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	/**
	 * POI信息查询回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// 隐藏对话框
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				// 搜索poi的结果
				if (result.getQuery().equals(query)) {
					// 是否是同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					// 取得第一页的poiitem数据，页数从数字0开始
					List<PoiItem> poiItems = poiResult.getPois();
					// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();

					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(MapActivity.this, R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(MapActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(MapActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(MapActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(MapActivity.this, getString(R.string.error_other)
					+ rCode);
		}

	}

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.show(MapActivity.this, infomation);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		if(newText.length() > 0){
			//按钮图片或文字改变
			mRightImage.setBackgroundResource(R.drawable.search_2x);
			isList = false;
		}
		else{
			//按钮图片或文字改变
			mRightImage.setBackgroundResource(R.drawable.list_style);
			isList = true;
		}
		
		Inputtips inputTips = new Inputtips(MapActivity.this,
				new InputtipsListener() {
					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0) {
							// 正确返回
							List<String> listString = new ArrayList<String>();
							for (int i = 0; i < tipList.size(); i++) {
								listString.add(tipList.get(i).getName());
							}
							MapSearchListAdapter aAdapter = new MapSearchListAdapter(MapActivity.this, listString);
							searchText.setAdapter(aAdapter);
							aAdapter.notifyDataSetChanged();
						}
					}
				});
		try {
			// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
			inputTips.requestInputtips(newText, "");

		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMyLocationChange(Location location) {
		LogUtil.i("lihe", "地图变化获取到的信息:" + "Latitude==" + location.getLatitude()
				+ "Longitude==" + location.getLongitude());
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog(String keyWord) {
		if (progDialog == null){
			progDialog = new ProgressDialog(this);
		}
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("MapActivity");
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("MapActivity");
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
	private LatLng LLBegin = null, LLEnd = null;
	private String startProvinceAndCity, startAddr;
	private String endProvinceAndCity, endAddr;
	
	
	private void getLatLng(String addr, String city){
		GeocodeQuery mQuery = new GeocodeQuery(addr, city);
		mSearch.getFromLocationNameAsyn(mQuery);
	}

	/**
	 * 根据地址查询经纬度的回调函数
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		GeocodeAddress address = result.getGeocodeAddressList().get(0);
		LatLonPoint  mLLPoint = address.getLatLonPoint();
		LatLng mLL = new LatLng(mLLPoint.getLatitude(), mLLPoint.getLongitude());
	}
	
	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		
	}
	
	public void display(){
		if(LLBegin != null && LLEnd != null){
			/*
			Bitmap bitMapBegin = BitmapFactory.decodeResource(getResources(), R.drawable.start_point);
			Bitmap resiBitmapBegin = ImageUtil.resizeBitmap(MapActivity.this,
					bitMapBegin, bitMapBegin.getWidth(), bitMapBegin.getHeight());
			Bitmap bitMapEnd = BitmapFactory.decodeResource(getResources(), R.drawable.end_point);
			Bitmap resiBitmapEnd = ImageUtil.resizeBitmap(MapActivity.this,
					bitMapEnd, bitMapEnd.getWidth(), bitMapEnd.getHeight());
			 MarkerOptions mOptions = new MarkerOptions()
			.position(LLBegin)
			.title(startProvinceAndCity)
			.snippet(startAddr)
			.icon(BitmapDescriptorFactory.fromBitmap(resiBitmapBegin));
			aMap.addMarker(mOptions);
			
			mOptions = new MarkerOptions()
			.position(LLEnd)
			.title(endProvinceAndCity)
			.snippet(endAddr)
			.icon(BitmapDescriptorFactory.fromBitmap(resiBitmapEnd));
			aMap.addMarker(mOptions);
			*/
			LatLngBounds mBounds = new LatLngBounds.Builder() 
			.include(LLBegin)
			.include(LLEnd).build();
			CameraUpdate mUpdate = CameraUpdateFactory.newLatLngBounds(mBounds, 0);
			aMap.moveCamera(mUpdate);
			
			//根据起终点查询驾驶路线
			RouteSearch routeSearch = new RouteSearch(this);
			routeSearch.setRouteSearchListener(new OnRouteSearchListener() {
				
				@Override
				public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
					return;
				}
				
				@Override
				public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
					if (rCode == 0) {  
			            if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {  
			            	DriveRouteResult driveRouteResult = result;  
			                DrivePath drivePath = driveRouteResult.getPaths().get(0);  
			                //aMap.clear();// 清理地图上的所有覆盖物  
			                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(MapActivity.this,
			                		aMap, drivePath, driveRouteResult.getStartPos(),  
			                        driveRouteResult.getTargetPos());
			                //drivingRouteOverlay.removeFromMap();
			                drivingRouteOverlay.addToMap();
			                
			            }
			        }
				}
				
				@Override
				public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
					return;
				}
			});
			LatLonPoint from = new LatLonPoint(LLBegin.latitude, LLBegin.longitude);
			LatLonPoint to = new LatLonPoint(LLEnd.latitude, LLEnd.longitude);
			RouteSearch.FromAndTo fromTo = new FromAndTo(from, to);
			// fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
			// 第三个参数表示途经点（最多支持16个），第四个参数表示避让区域（最多支持100个），第五个参数表示避让道路
			DriveRouteQuery query = new DriveRouteQuery(fromTo, RouteSearch.DrivingDefault, null, null, "");
			routeSearch.calculateDriveRouteAsyn(query);
		}
	}
	
	public enum MapType{
		StartEnd, RequestLocation, NeighborhoodSearching;
	}
	
	private class MarkerData{
		private MarkerType type;
		private String content;
		public MarkerType getType() {
			return type;
		}
		public void setType(MarkerType type) {
			this.type = type;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	};
	
	public enum MarkerType{
		//覆盖物种类：表示竞价单起点终点的marker、表示货源的marker、表示镜头中心所在地址的marker
		start_end, goods_source, locus;
	}
}
