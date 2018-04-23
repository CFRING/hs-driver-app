package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.TruckListModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.InputLimitTextWatcher;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.hongshi.wuliudidi.view.NullDataView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author huiyuan
 */
public class AssignTruckActivity extends Activity{
	private LinearLayout mTruckItemLayout;
	private DiDiTitleView mTitle;
	private TextView mWeightText;
	private NullDataView nullDataView;
	private RelativeLayout mBottomLayout;
	private HashMap<String, String> map = new HashMap<String, String>();
	private String planId;
	private Button mOk, reTryButton;
	private String truck_url = GloableParams.HOST + "carrier/transit/task/topc.do?";
	private String assign_truck_url = GloableParams.HOST + "carrier/transit/task/insert.do?";
	private List<TruckListModel> mTruckList = new ArrayList<TruckListModel>();
	private List<TruckListModel> mJsonTruckList = new ArrayList<TruckListModel>();
	private double total = 0.0;
	private TextView mSendWeightText;
	private String planAmount;
	private TextView tagNumberTextView;
	private TextView tagDriverIdTextView;
	//判断是否选中
	private int updateNumber = -1;
	//运输单位
	private String assignUnit = "";
	//运输单位
	private String assignUnitText = "";
	private int number_truck = 0;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("AssignTruckActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("AssignTruckActivity");
	}

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.assign_truck_weight);
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setBack(this);
		mTitle.setTitle("选择车辆");
		try {
			planId = getIntent().getExtras().getString("planId");
			planAmount = getIntent().getExtras().getString("planAmount");
			assignUnit = getIntent().getExtras().getString("assignUnit");
			assignUnitText = getIntent().getExtras().getString("assignUnitText");
		} catch (Exception e) {
			planId = "";
			planAmount = "0";
			assignUnit = "";
		}
		mWeightText = (TextView) findViewById(R.id.all_weight);
		mTruckItemLayout = (LinearLayout) findViewById(R.id.truck_info_layout);
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		nullDataView = (NullDataView) findViewById(R.id.no_data_view);
		reTryButton = nullDataView.getInfoImage();
		reTryButton.setBackground(getResources().getDrawable(R.drawable.solid_btn_style));
		reTryButton.setText("请点击重试");
		reTryButton.setTextColor(getResources().getColor(R.color.white));
		reTryButton.setVisibility(View.VISIBLE);
		reTryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getData();
			}
		});
		
		mSendWeightText = (TextView) findViewById(R.id.send_weight);
		mSendWeightText.setText("运量" + planAmount + assignUnitText);
		String str = Util.formatDoubleToString(0, assignUnitText) + assignUnitText;
		Util.setText(AssignTruckActivity.this, str, str, mWeightText, R.color.theme_color);
		mOk = (Button) findViewById(R.id.ok);
		mOk.setText(getResources().getString(R.string.confirm_assign_truck)+"("+number_truck+")");
		getData();

		mOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!CommonRes.TRUCK){
					if(!checkRepeat(mJsonTruckList)){
						ToastUtil.show(AssignTruckActivity.this, "司机电话号码有重复");
						return;
					}
				}
				if(mJsonTruckList.size()== 0){
					ToastUtil.show(AssignTruckActivity.this, "请选择车辆");
					return;
				}
				String json = JSON.toJSONString(mJsonTruckList);
				AjaxParams params = new AjaxParams();
				params.put("tasksJson", json);
				final PromptManager mPromptManager = new PromptManager();
				mPromptManager.showProgressDialog1(AssignTruckActivity.this, "正在上传");
				DidiApp.getHttpManager().sessionPost(AssignTruckActivity.this, assign_truck_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						mPromptManager.closeProgressDialog();
						Toast.makeText(AssignTruckActivity.this, "已派车", Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						intent.setAction(CommonRes.NewTask);
						sendBroadcast(intent);
						finish();
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						mPromptManager.closeProgressDialog();
					}
				});
			}
		});
	}
	private void getData(){
		AjaxParams params = new AjaxParams();
		params.put("planId", planId);
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(AssignTruckActivity.this, "加载中");
		DidiApp.getHttpManager().sessionPost(AssignTruckActivity.this, truck_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				mPromptManager.closeProgressDialog();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t);
					String all = jsonObject.getJSONObject("body").getString("trucks");
					String restAmount = jsonObject.getJSONObject("body").getString("restAmount");
					mSendWeightText.setText("运量  " + planAmount + "  " + assignUnitText +"(剩  " + restAmount +")");
					mTruckList = JSON.parseArray(all,TruckListModel.class);	
					addDate(mTruckList);
				} catch (JSONException e) {
					e.printStackTrace();
					showNoData(true);
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
				showNoData(true);
			}
		});
	}
	void addDate(List<TruckListModel> mTruckList){
		if(mTruckList == null || mTruckList.size() <= 0){
			showNoData(false);
			return;
		}
		mTruckItemLayout.setVisibility(View.VISIBLE);
		mBottomLayout.setVisibility(View.VISIBLE);
		nullDataView.setVisibility(View.GONE);
		for (int i = 0; i < mTruckList.size(); i++) {
			map.put("" + i, "数据" + i);
			LinearLayout truck_item_lyout = (LinearLayout) LayoutInflater.from(AssignTruckActivity.this).inflate(R.layout.assign_truck_item, null);
			final CheckBox box = (CheckBox) truck_item_lyout.findViewById(R.id.check_box);
			final EditText edit = (EditText) truck_item_lyout.findViewById(R.id.weight_edit);
			final TextView driver_number = (TextView) truck_item_lyout.findViewById(R.id.phone_number);
			//在途标记
			CheckBox transit_text = (CheckBox) truck_item_lyout.findViewById(R.id.transit_box);
			final TextView driver_id = (TextView) truck_item_lyout.findViewById(R.id.driver_id);
			final RelativeLayout select_number = (RelativeLayout) truck_item_lyout.findViewById(R.id.select_number);
			RelativeLayout weight_layour = (RelativeLayout) truck_item_lyout.findViewById(R.id.weight_layour);
			TextView text = (TextView) truck_item_lyout.findViewById(R.id.text);
			text.setText(mTruckList.get(i).getNumber()+"   "+mTruckList.get(i).getTypeText()+"   "+mTruckList.get(i).getLengthText()
					+"   "+mTruckList.get(i).getCapacity() + "吨");
			final TextView truckId = (TextView) truck_item_lyout.findViewById(R.id.truckId);
			truckId.setText(mTruckList.get(i).getTruckId());
			if(mTruckList.get(i).isInTransit()){
				box.setVisibility(View.GONE);
				weight_layour.setVisibility(View.GONE);
				select_number.setVisibility(View.GONE);
				transit_text.setVisibility(View.VISIBLE);
			}else{
				box.setVisibility(View.VISIBLE);
				weight_layour.setVisibility(View.VISIBLE);
				select_number.setVisibility(View.VISIBLE);
				transit_text.setVisibility(View.GONE);
			}
			if(assignUnit.equals("TRUCK")){
				weight_layour.setVisibility(View.GONE);
			}else{
				if(assignUnit.equals("T") || assignUnit.equals("M3")){
					//如果运输单位是吨或立方米
					InputLimitTextWatcher mInputLimitTextWatcher = Util.getDoubleInputLimitTextWatcher();
					mInputLimitTextWatcher.setEditText(edit);
				}else if(assignUnit.equals("PIECE")){
					//如果运输单位是件
					edit.setInputType(InputType.TYPE_CLASS_NUMBER);
				}
			}
			box.setId(i);
			select_number.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!box.isChecked()){
						updateNumber = -1;
					}else{
						updateNumber = box.getId();
					}
					Intent intent = new Intent(AssignTruckActivity.this,ChooseDriverActivity.class);
					startActivityForResult(intent, 11);
					tagNumberTextView = driver_number;	
					tagDriverIdTextView = driver_id;	
				}
			});
			box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
						if(assignUnit.equals("TRUCK")){
							//以车为单位派车
							if(isChecked){
								if(!driver_number.getText().toString().equals("请选择司机的手机号码")){
									TruckListModel mTruckListModel = new TruckListModel();
									mTruckListModel.setPlanId(planId);
									total = total + 1;
									String str = (int)total + "车";
									Util.setText(AssignTruckActivity.this, str, (String.valueOf((int)total) + "车"), mWeightText, R.color.theme_color);
									mTruckListModel.setTruckId(truckId.getText().toString());
									mTruckListModel.setId(buttonView.getId());
									mTruckListModel.setPlanAmount(1);
									mTruckListModel.setCellphone(driver_number.getText().toString());
									mTruckListModel.setDriverBookId(driver_id.getText().toString());
									mJsonTruckList.add(mTruckListModel);
									number_truck = number_truck + 1;
									mOk.setText(getResources().getString(R.string.confirm_assign_truck)+"("+number_truck+")");
								}else{
									if(driver_number.getText().toString().equals("请选择司机的手机号码")){
										box.setChecked(false);
										Toast.makeText(AssignTruckActivity.this, "请选择司机号码", Toast.LENGTH_SHORT).show();	
										return;
									}
								}
							}else{
								for(int n=0;n<mJsonTruckList.size();n++){
									if(buttonView.getId() == mJsonTruckList.get(n).getId()){
										total = total - 1;
										String str = (int)total + "车";
										Util.setText(AssignTruckActivity.this, str,(String.valueOf((int)total) + "车"), mWeightText, R.color.theme_color);
										mJsonTruckList.remove(n);
										number_truck = number_truck - 1;
										mOk.setText(getResources().getString(R.string.confirm_assign_truck)+"("+number_truck+")");
										break;
									}
								}
							}
						}else{
							if(isChecked){
								if(!edit.getText().toString().equals("") && !driver_number.getText().toString().equals("请选择司机的手机号码")){
									TruckListModel mTruckListModel = new TruckListModel();
									mTruckListModel.setPlanId(planId);
									String strPlanAmount = Util.inputToDoubleStr(edit.getText().toString());
									if(strPlanAmount.equals("")){
										ToastUtil.doubleParseError(AssignTruckActivity.this, "车运载量");
										box.setChecked(false);
										return;
									}else{
										mTruckListModel.setPlanAmount(Util.inputToDoubleValue(strPlanAmount));
										total = total + Util.inputToDoubleValue(strPlanAmount);
										total = Util.doubleRound(total, 2);
									}
									String str = Util.formatDoubleToString(total, assignUnitText) + assignUnitText;
									Util.setText(AssignTruckActivity.this, str, str, mWeightText, R.color.theme_color);
									mTruckListModel.setTruckId(truckId.getText().toString());
									mTruckListModel.setId(buttonView.getId());
									mTruckListModel.setCellphone(driver_number.getText().toString());
									mTruckListModel.setDriverBookId(driver_id.getText().toString());
									mJsonTruckList.add(mTruckListModel);
									number_truck = number_truck + 1;
									mOk.setText(getResources().getString(R.string.confirm_assign_truck)+"("+number_truck+")");
									edit.addTextChangedListener(new TextWatcher() {
										@Override
										public void onTextChanged(CharSequence s, int start, int before, int count) {
											if(edit.getText().toString().equals("")){
												for(int n=0;n<mJsonTruckList.size();n++){
													if(buttonView.getId() == mJsonTruckList.get(n).getId()){
														total = total - mJsonTruckList.get(n).getPlanAmount();
														total = Util.doubleRound(total, 2);
														String str = Util.formatDoubleToString(total, assignUnitText) + assignUnitText;
														Util.setText(AssignTruckActivity.this, str,str, mWeightText, R.color.theme_color);
														mJsonTruckList.remove(n);
														box.setChecked(false);
														number_truck = number_truck - 1;
														mOk.setText(getResources().getString(R.string.confirm_assign_truck)+"("+number_truck+")");
														break;
													}
												}
											}else{
												for(int n = 0;n < mJsonTruckList.size(); n++){
													if(buttonView.getId() == mJsonTruckList.get(n).getId()){
														//监听Edittext数据变化，先把之前存储的数据减掉，然后在累加 填写的数量，
														String strPlanAmount = Util.inputToDoubleStr(edit.getText().toString());
														if(strPlanAmount.equals("")){
															ToastUtil.doubleParseError(AssignTruckActivity.this, "车运载量");
															edit.setText(String.valueOf(mJsonTruckList.get(n).getPlanAmount()));
															edit.setSelection(edit.getText().toString().length());
														}else{
															total = total - mJsonTruckList.get(n).getPlanAmount();
															mJsonTruckList.get(n).setPlanAmount(Util.inputToDoubleValue(strPlanAmount));
															total = total + Util.inputToDoubleValue(strPlanAmount);
															total = Util.doubleRound(total, 2);
														}
														String str = Util.formatDoubleToString(total, assignUnitText) + assignUnitText;
														Util.setText(AssignTruckActivity.this, str, str, mWeightText, R.color.theme_color);
//
														break;
													}
												}
											}
										}
										@Override
										public void beforeTextChanged(CharSequence s, int start, int count,
												int after) {
										}
										@Override
										public void afterTextChanged(Editable s) {

										}
									});
								}else{
									if(edit.getText().toString().equals("")){
										box.setChecked(false);
										Toast.makeText(AssignTruckActivity.this, "请输入该车运量", Toast.LENGTH_SHORT).show();	
										return;
									}
									if(driver_number.getText().toString().equals("请选择司机的手机号码")){
										box.setChecked(false);
										Toast.makeText(AssignTruckActivity.this, "为该车选择司机", Toast.LENGTH_SHORT).show();	
										return;
									}
								}
							}else{
								for(int n=0;n<mJsonTruckList.size();n++){
									if(buttonView.getId() == mJsonTruckList.get(n).getId()){
										total = total - mJsonTruckList.get(n).getPlanAmount();
										total = Util.doubleRound(total, 2);
										String str = Util.formatDoubleToString(total, assignUnitText) + assignUnitText;
										Util.setText(AssignTruckActivity.this, str, str, mWeightText, R.color.theme_color);
										mJsonTruckList.remove(n);
										number_truck = number_truck - 1;
										mOk.setText(getResources().getString(R.string.confirm_assign_truck)+"("+number_truck+")");
										break;
									}
								}
							}
						}
					}
				});
			
			mTruckItemLayout.addView(truck_item_lyout);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 11 && resultCode == RESULT_OK) {
			if (data != null) {
				String phoneNumber = data.getStringExtra("phoneNumber");
				String driverBookId = data.getStringExtra("driverBookId");
				tagNumberTextView.setText(phoneNumber);
				tagDriverIdTextView.setText(driverBookId);
				if(updateNumber != -1){
					for(int n=0;n<mJsonTruckList.size();n++){
						if(mJsonTruckList.get(n).getId() == updateNumber){
							TruckListModel truckListModel = mJsonTruckList.get(n);
							mJsonTruckList.remove(n);
							truckListModel.setCellphone(phoneNumber);
							truckListModel.setDriverBookId(driverBookId);
							mJsonTruckList.add(n, truckListModel);
						}
					}
				}
				}
			}
	}
	/**
	 * 判断是否有重复的司机号码
	 * @param mJsonTruckList
	 * @return
	 */
	public boolean checkRepeat(List<TruckListModel> mJsonTruckList){
		  Set<String> set = new HashSet<String>();
		  for(int i = 0;i< mJsonTruckList.size(); i++){
		    set.add(mJsonTruckList.get(i).getCellphone());
		  }
		  if(set.size() != mJsonTruckList.size()){
			  //有重复
		    return false;
		  }else{
			  //不重复
		    return true;
		  }
		}

	private void showNoData(boolean isError){//true则为后台报错，false则为数据为空
		mTruckItemLayout.setVisibility(View.GONE);
		mBottomLayout.setVisibility(View.GONE);
		nullDataView.setVisibility(View.VISIBLE);
		if(isError){
			nullDataView.showErrorView();
			nullDataView.setInfoHint("读取信息失败");
		}else{
			nullDataView.showNoneView();
			nullDataView.setInfoHint("暂无车辆信息");
		}
	}
}
