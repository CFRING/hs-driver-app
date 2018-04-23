package com.hongshi.wuliudidi.dialog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.AddTypeGridViewAdapter;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.CallBackString;
import com.hongshi.wuliudidi.model.AuctionOfferModel;
import com.hongshi.wuliudidi.model.TruckTypeModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.Util;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddTruckPop {
	// 省份
	String[] pro_data = { "川", "鄂", "苏", "琼", "皖", "豫", "浙", "甘", "赣", "辽",
			"津", "沪", "闽", "青", "鲁", "渝", "冀", "京", "云", "臧", "粤", "蒙", "新",
			"黑", "桂", "湘", "陕", "宁", "贵", "晋", "吉" };
	// 字母
	String[] char_data = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };
	// 字母
	String[] license_data = {"A1", "A2", "A3", "B1", "B2", "C1", "C2", "C3", "C4", "D"};
	//车型
//	String[] truck_type = {"厢式密封车","厢式雨棚车","厢式尾板车","开篷车","高栏车","低栏车","平板车",
//			"高低板车","集装箱","开顶厢车","冷藏车","面包车","保温车","挂车","飞翼车","栏板车","畜牧车"};
	String[] truck_type ;
	//车长
//	String[] truck_length = { "3", "3.6", "4", "4.2", "4.8", "5", "5.2", "5.8", "6.2", "6.5",
//			"6.8", "7.2", "7.6", "7.8", "8", "8.6", "9.6", "10", "11.5", "12", "13", "13.5", "15",
//			"16", "16.5", "17" , "17.5", "18.5", "20", "21", "22", "23"};
	String[] truck_length;
	private List<String> truck_id = new ArrayList<String>();
	private Activity mContext;
	PopupWindow pop;
	private TextView mFinish, mContent,mBack;
	private GridView mTypeGridView;
	private int type = -1;
	private AddTypeGridViewAdapter mAddTypeGridViewAdapter;
	private boolean isSelectChar = false;
	private boolean isSelectLength = false;
	private boolean isSelectLicense = false;
	private String mLicenceSte = "";
	private String mLastStr = "";
	private CallBackString mCallBackString;
	private String truckTypeId;
	private String truckLengthId;
	private String licenseName = "";
	private int licensetype;
	private List<TruckTypeModel> truckTypeList = new ArrayList<TruckTypeModel>();
	private List<TruckTypeModel> truckLengthList = new ArrayList<TruckTypeModel>();
	public AddTruckPop(Activity context,int type,CallBackString mCallBackString) {
		this.mContext = context;
		this.type = type;
		this.mCallBackString = mCallBackString;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.add_truck_dialog, null);
		mTypeGridView = (GridView) view.findViewById(R.id.type_gridview);
		mContent = (TextView) view.findViewById(R.id.content);
		mFinish = (TextView) view.findViewById(R.id.finish);
		mBack = (TextView) view.findViewById(R.id.back);
		setAdapter(type);
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		pop.setFocusable(true);
		pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		mTypeGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectData(position,type);
			}
		});
		mFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mContent.getText().toString().equals("")) {
					resetData(type);
				}

			}
		});
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mLicenceSte = "";
				switch (type) {
				case 2:
					mBack.setVisibility(View.GONE);
					mAddTypeGridViewAdapter.setData(pro_data);
					mAddTypeGridViewAdapter.notifyDataSetChanged();
					type = 1;
					break;
				case 4:
					mBack.setVisibility(View.GONE);
					mAddTypeGridViewAdapter.setData(truck_type);
					mAddTypeGridViewAdapter.notifyDataSetChanged();
					type = 3;
					mTypeGridView.setNumColumns(4);
					break;

				default:
					break;
				}
				
			}
		});
	}

	//点击完成时的不同状态
	private void resetData(int type_f){
		switch (type_f) {
		case 1:
			mAddTypeGridViewAdapter.setData(char_data);
			mAddTypeGridViewAdapter.notifyDataSetChanged();
			mBack.setVisibility(View.VISIBLE);
			type = 2;
			break;
		case 2:
			//此时选择代表地区的字母车牌号选择完成，向activity中传递，更新ui
			if(isSelectChar){
				mCallBackString.getStr(mLastStr,"","");
				pop.dismiss();
			}else{
				Toast.makeText(mContext, "请选择", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case 3:
			//根据车型请求车长数据
//			mAddTypeGridViewAdapter.setData(truck_length);
//			mAddTypeGridViewAdapter.notifyDataSetChanged();
//			mBack.setVisibility(View.VISIBLE);
//			type = 4;
//			mTypeGridView.setNumColumns(5);
			getTruckLength();
			break;
		case 4:
			//此时选择车长选择完成，向activity中传递，更新ui
			if(isSelectLength){
				mCallBackString.getStr(mLastStr+"m",truckLengthId,truckTypeId);
				pop.dismiss();
			}else{
				Toast.makeText(mContext, "请选择", Toast.LENGTH_LONG).show();
			}
			
			break;
		case 5:
			//选择驾照
			if(isSelectLicense){
				mCallBackString.getStr(licenseName,"",""+licensetype);
				pop.dismiss();
			}else{
				Toast.makeText(mContext, "请选择", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case CommonRes.TYPE_TRUCK_NONGYONG:
			if(!mContent.getText().toString().equals("")){
				mCallBackString.getStr(mContent.getText().toString(),"","");
				pop.dismiss();	
			}
			break;
		default:
			break;
		}
	}
	//第一次设置adapter的数据
	private void setAdapter(int type_t){
		switch (type) {
		case CommonRes.TYPE_TRUCK_NORMAL:
			//选择车牌
			mAddTypeGridViewAdapter = new AddTypeGridViewAdapter(mContext, pro_data);
			mTypeGridView.setAdapter(mAddTypeGridViewAdapter);
//			mTypeGridView.setNumColumns(3);
			break;
		case CommonRes.TYPE_DRIVER_LICENCE:
			//选择驾照类型
			mAddTypeGridViewAdapter = new AddTypeGridViewAdapter(mContext, license_data);
			mTypeGridView.setAdapter(mAddTypeGridViewAdapter);
			break;
		case CommonRes.TYPE_ALL_TRUCK_TYPE:
			//获取所有的车型
			getTruckType();
			break;
		case CommonRes.TYPE_TRUCK_NONGYONG:
			mAddTypeGridViewAdapter = new AddTypeGridViewAdapter(mContext, pro_data);
			mTypeGridView.setAdapter(mAddTypeGridViewAdapter);
			break;
		default:
			break;
		}
	} 
	//gridview点击item选择内容
	public void selectData(int pos, int type) {
		switch (type) {
		// 选择地区的车辆
		case 1:
			mContent.setText(pro_data[pos]);
			isSelectChar = false;
			mLicenceSte = pro_data[pos];
			break;
		// 选择车辆的字符
		case 2:
			//增加标记判断字符是否选择了，完成按钮时使用
			mContent.setText(char_data[pos]);
			isSelectChar = true;
			mLastStr = mLicenceSte + "  "+char_data[pos];
			break;
		case 3:
			//增加标记判断车长是否选择了，完成按钮时使用
			mContent.setText(truck_type[pos]);
			truckTypeId = truckTypeList.get(pos).getTruckModelId();
			isSelectLength = false;
			mLicenceSte = truck_type[pos];
			break;
		case 4:
			//增加标记判断是否选择了，完成按钮时使用
			mContent.setText(truck_length[pos]);
			truckLengthId = truckLengthList.get(pos).getTruckLengthId();
			isSelectLength = true;
			mLastStr = mLicenceSte + "  "+truck_length[pos];
			break;
		case 5:
			isSelectLicense = true;
			mContent.setText(license_data[pos]);
			licenseName = license_data[pos];
			licensetype = pos+1;
			break;
		case CommonRes.TYPE_TRUCK_NONGYONG:
			mContent.setText(pro_data[pos]);
			break;
		default:	
			break;
		}
	}

	public void setShow(View v) {
		pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
	}
	private void getTruckLength(){
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog(mContext, "正在加载");
		//根据所有车长
		String url = GloableParams.HOST + "commonservice/app/truckLength/queryTruckLengthAll.do?";
		AjaxParams params = new AjaxParams();	
		DidiApp.getHttpManager().sessionPost(mContext, url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					truckLengthList = JSON.parseArray(all,TruckTypeModel.class);
					List<String> truck_length_list = new ArrayList<String>();
					for(int i=0;i<truckLengthList.size();i++){
						String length = truckLengthList.get(i).getLength();
						truck_length_list.add(length);
					}
					final int size = truckLengthList.size(); 
					truck_length = (String[]) truck_length_list.toArray(new String[size]);
					
					mAddTypeGridViewAdapter.setData(truck_length);
					mAddTypeGridViewAdapter.notifyDataSetChanged();
					mBack.setVisibility(View.VISIBLE);
					type = 4;
					mTypeGridView.setNumColumns(5);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
		});
	}
	/**
	 * 获取车型
	 */
	private void getTruckType(){
		//获取所有车型
		String url = GloableParams.HOST + "commonservice/app/truckModel/queryTruckModelAll.do?";
		AjaxParams params = new AjaxParams();
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog(mContext, "正在加载");
		DidiApp.getHttpManager().sessionPost(mContext, url, params, new AfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					truckTypeList = JSON.parseArray(all,TruckTypeModel.class);
					List<String> truck_type_list = new ArrayList<String>();
					
					for(int i=0;i<truckTypeList.size();i++){
						String name = truckTypeList.get(i).getName();
						String truckId = truckTypeList.get(i).getTruckModelId();
						truck_type_list.add(name);
						truck_id.add(truckId);
					}
					final int size = truckTypeList.size(); 
					truck_type = (String[]) truck_type_list.toArray(new String[size]);
					mAddTypeGridViewAdapter = new AddTypeGridViewAdapter(mContext, truck_type);
					mTypeGridView.setAdapter(mAddTypeGridViewAdapter);
					mTypeGridView.setNumColumns(4);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
