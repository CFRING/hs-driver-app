package com.hongshi.wuliudidi.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.CancelDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.DriverBookDetailVO;
import com.hongshi.wuliudidi.model.DriverBookInputFormVO;
import com.hongshi.wuliudidi.model.DriverModel;
import com.hongshi.wuliudidi.model.TruckAuthAppVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.AuctionItem;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 */
public class DriverInfoActivity extends Activity implements OnClickListener{
	private DriverBookDetailVO mDriverModel;
	private DiDiTitleView mTitle;
	private ImageView photoImage;
	private RelativeLayout band_truck_container;
	private TextView mDriverName,truck1,truck2,truck3,truck4,truck5;
	private AuctionItem mNickName, mPhoneNumber, mMarkPhoneNumber;
	private Button mSaveBtn,mDelete;
	private ToggleButton voice_switch;
	private String driverBookId = "";
	private String driverId = "";
	private String cellPhone = "";
	//获取司机信息
	private String info_url = GloableParams.HOST + "carrier/mydrivers/detail.do?";
	//获取尚未添加的司机信息
	private String info_url_new = GloableParams.HOST + "carrier/mydrivers/driverdetail.do?";
	//编辑完成
	private String update_url = GloableParams.HOST + "carrier/mydrivers/update.do?";
	//新增司机
	private String add_url = GloableParams.HOST + "carrier/mydrivers/insert.do?";
	private String add_update_driver_info_url = GloableParams.HOST + "/carrier/mydrivers/createDriver.do";
	//删除司机
	private String delete_url = GloableParams.HOST + "carrier/mydrivers/delete.do?";
	private boolean isNew = false;
	private boolean isOwner = false;
	private SharedPreferences sp;
	private List<String> truckNumberList = new ArrayList<>();
	private List<String> truckIdList = new ArrayList<>();
	//车辆列表.
	private List<TruckAuthAppVO> truckVoList = new ArrayList<>();
	private DriverBookInputFormVO driverBookInputFormVO = new DriverBookInputFormVO();

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("DriverInfoActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("DriverInfoActivity");
	}

	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
			case CommonRes.DELETE_DRIVER:
				AjaxParams params = new AjaxParams();
				params.put("id", driverBookId);
				DidiApp.getHttpManager().sessionPost(DriverInfoActivity.this, delete_url, params, new ChildAfinalHttpCallBack() {
					@Override
					public void data(String t) {
						ToastUtil.show(DriverInfoActivity.this, "成功删除司机");
						Intent intent = new Intent();
						intent.setAction(CommonRes.DriverModify);
						sendBroadcast(intent);
						finish();
					}

					@Override
					public void onFailure(String errCode, String errMsg, Boolean errSerious) {
						ToastUtil.show(DriverInfoActivity.this, "删除司机失败");
					}
				});
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.driver_info_activity);

		initViews();
		getData();
	}

	private void initViews(){
		try {
			isNew = getIntent().getBooleanExtra("isNew", false);
			isOwner = getIntent().getBooleanExtra("isOwner",false);
		} catch (Exception e) {
		}

		if(!isNew){
			try {
				driverBookId = getIntent().getStringExtra("driverBookId");
				driverId = getIntent().getStringExtra("driverId");
			} catch (Exception e) {
			}
		}else{
			try {
				cellPhone = getIntent().getStringExtra("cellPhone");
			} catch (Exception e) {
			}
		}
		mTitle = (DiDiTitleView) findViewById(R.id.title);
		mTitle.setTitle("司机信息");
		mTitle.setBack(DriverInfoActivity.this);
		mTitle.getRightTextView().setText("车辆分配");
		mTitle.getRightTextView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DriverInfoActivity.this,ChooseTruckForDriverActivity.class);
				startActivity(intent);
			}
		});
		band_truck_container = (RelativeLayout) findViewById(R.id.band_truck_container);
		truck1 = (TextView) findViewById(R.id.truck1);
		truck2 = (TextView) findViewById(R.id.truck2);
		truck3 = (TextView) findViewById(R.id.truck3);
		truck4 = (TextView) findViewById(R.id.truck4);
		truck5 = (TextView) findViewById(R.id.truck5);

		photoImage = (ImageView) findViewById(R.id.driver_photo_image);
		mDriverName = (TextView) findViewById(R.id.driver_name);

		mNickName = (AuctionItem) findViewById(R.id.nick_item);
		mNickName.setName("备注昵称");
		//备注姓名禁止回车

		Util.getNoEnterLimitTextWatcher().setEditText(mNickName.getContentEdit());

		mPhoneNumber = (AuctionItem) findViewById(R.id.phone_number_item);
		mPhoneNumber.setName("手机号码");
		mPhoneNumber.getRightImage().setImageResource(R.drawable.call_blue);
		mPhoneNumber.setOnClickListener(this);

		mMarkPhoneNumber = (AuctionItem) findViewById(R.id.mark_number_item);
		mMarkPhoneNumber.setName("备注电话");
		mMarkPhoneNumber.getRightImage().setImageResource(R.drawable.call_blue);
		mMarkPhoneNumber.getContentEdit().setInputType(InputType.TYPE_CLASS_NUMBER);
		mMarkPhoneNumber.setOnClickListener(this);

		driverBookInputFormVO.setAllowDriverAcceptOrderFlag(0);
		voice_switch = (ToggleButton)findViewById(R.id.voice_switch);
		if(!isOwner){
			voice_switch.setChecked(sp.getBoolean("driver_take_order_switch",true));
			voice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					sp.edit().putBoolean("driver_take_order_switch",isChecked).commit();
					voice_switch.setChecked(isChecked);
					if(isChecked){
						driverBookInputFormVO.setAllowDriverAcceptOrderFlag(0);
					}else {
						driverBookInputFormVO.setAllowDriverAcceptOrderFlag(1);
					}
				}
			});
		}else {
			voice_switch.setVisibility(View.GONE);
		}

		mDelete = (Button) findViewById(R.id.delete_btn);
		if(isNew){
			mDelete.setVisibility(View.GONE);
		}
		mDelete.setOnClickListener(this);

		mSaveBtn = (Button) findViewById(R.id.save_btn);
		mSaveBtn.setOnClickListener(this);

		IntentFilter filter = new IntentFilter();
		filter.addAction("driver_truck_list");
		registerReceiver(receiver,filter);
	}

	private void getData(){
		String url;
		AjaxParams params = new AjaxParams();
		if(isNew){
			params.put("cellphone", cellPhone);
			url = info_url_new;
		}else{
			params.put("id", driverBookId);
			url = info_url;
		}
		DidiApp.getHttpManager().sessionPost(DriverInfoActivity.this, url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Log.d("huiyuan","司机信息 = " + t);
				try {
					JSONObject jsonObject = new JSONObject(t);
					String all = jsonObject.getString("body");
					JSONObject jsonObject1 = new JSONObject(all);
					if(mDriverModel == null){
						mDriverModel = new DriverBookDetailVO();
					}
					mDriverModel = JSON.parseObject(all,DriverBookDetailVO.class);
					int isAllow = mDriverModel.getAllowDriverAcceptOrderFlag();
					if(isAllow == 0){
						voice_switch.setChecked(true);
					}else {
						voice_switch.setChecked(false);
					}
					if(mDriverModel.getTruckVoList() != null){
						truckVoList = mDriverModel.getTruckVoList();
						if(truckVoList != null){
							int size = truckVoList.size();
							for(int i= 0; i < size; i++){
								truckNumberList.add(truckVoList.get(i).getTruckNumber());
								truckIdList.add(truckVoList.get(i).getTruckId());
							}
						}
					}
					if(isNew){
						mDriverModel.setDriverId(jsonObject1.optString("driverId"));
					}
					initView();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				Toast.makeText(DriverInfoActivity.this,"错误信息:" + errMsg,Toast.LENGTH_LONG).show();
//				finish();//产品没有设计当手机号校验失败时应作何反应，暂时直接粗暴地关闭页面 TODO
			}
		});
	}
	
	private void initView(){
		if(mDriverModel == null){
			return;
		}
		if(mDriverModel.getUserFace() != null){
			FinalBitmap mFinalBitmap = FinalBitmap.create(DriverInfoActivity.this);
			mFinalBitmap.display(photoImage, mDriverModel.getUserFace());//头像
		}
		//司机姓名
		if(mDriverModel.getRealName() != null && mDriverModel.getRealName().length() > 0){
			mDriverName.setText(mDriverModel.getRealName());
		}else{
			mDriverName.setText(R.string.unknown);
			mDriverName.setTextColor(getResources().getColor(R.color.light_grey));
		}
		//备注姓名
		if(mDriverModel.getNickName() != null && mDriverModel.getNickName().length() > 0){
			mNickName.setEditContent(mDriverModel.getNickName());
		}else{
			mNickName.setHint(getResources().getString(R.string.input_nickname_hint));
		}
		//电话号码
		if(mDriverModel.getCellphone() != null){
			mPhoneNumber.setContent(mDriverModel.getCellphone());
		}
		//备注号码
		if(mDriverModel.getBackupCellphone() != null && mDriverModel.getBackupCellphone().length() > 0){
			mMarkPhoneNumber.setEditContent(mDriverModel.getBackupCellphone());
		}else{
			mMarkPhoneNumber.setHint(getResources().getString(R.string.input_backup_phone_hint));
		}
		initTruckListContainer();
	}

	private void initTruckListContainer(){
		int size = truckNumberList.size();
		if(size > 0){
			band_truck_container.setVisibility(View.VISIBLE);
			switch (size){
				case 1:
					truck1.setText(truckNumberList.get(0));
					truck1.setVisibility(View.VISIBLE);
					truck2.setVisibility(View.GONE);
					truck3.setVisibility(View.GONE);
					truck4.setVisibility(View.GONE);
					truck5.setVisibility(View.GONE);
					break;
				case 2:
					truck1.setText(truckNumberList.get(0));
					truck1.setVisibility(View.VISIBLE);
					truck2.setText(truckNumberList.get(1));
					truck2.setVisibility(View.VISIBLE);
					truck3.setVisibility(View.GONE);
					truck4.setVisibility(View.GONE);
					truck5.setVisibility(View.GONE);
					break;
				case 3:
					truck1.setText(truckNumberList.get(0));
					truck1.setVisibility(View.VISIBLE);
					truck2.setText(truckNumberList.get(1));
					truck2.setVisibility(View.VISIBLE);
					truck3.setText(truckNumberList.get(2));
					truck3.setVisibility(View.VISIBLE);
					truck4.setVisibility(View.GONE);
					truck5.setVisibility(View.GONE);
					break;
				case 4:
					truck1.setText(truckNumberList.get(0));
					truck1.setVisibility(View.VISIBLE);
					truck2.setText(truckNumberList.get(1));
					truck2.setVisibility(View.VISIBLE);
					truck3.setText(truckNumberList.get(2));
					truck3.setVisibility(View.VISIBLE);
					truck4.setText(truckNumberList.get(3));
					truck4.setVisibility(View.VISIBLE);
					truck5.setVisibility(View.GONE);
					break;
				case 5:
					truck1.setText(truckNumberList.get(0));
					truck1.setVisibility(View.VISIBLE);
					truck2.setText(truckNumberList.get(1));
					truck2.setVisibility(View.VISIBLE);
					truck3.setText(truckNumberList.get(2));
					truck3.setVisibility(View.VISIBLE);
					truck4.setText(truckNumberList.get(3));
					truck4.setVisibility(View.VISIBLE);
					truck5.setText(truckNumberList.get(4));
					truck5.setVisibility(View.VISIBLE);
					break;
				default:
					truck1.setText(truckNumberList.get(0));
					truck1.setVisibility(View.VISIBLE);
					truck2.setText(truckNumberList.get(1));
					truck2.setVisibility(View.VISIBLE);
					truck3.setText(truckNumberList.get(2));
					truck3.setVisibility(View.VISIBLE);
					truck4.setText(truckNumberList.get(3));
					truck4.setVisibility(View.VISIBLE);
					truck5.setText(truckNumberList.get(4));
					truck5.setVisibility(View.VISIBLE);
					break;
			}
		}else {
			band_truck_container.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		if(mDriverModel == null){
			return;//model为空时没有响应交互的必要
		}
		switch(v.getId()){
		case R.id.save_btn:
			save();
			break;
		case R.id.phone_number_item:
			Util.call(DriverInfoActivity.this, mDriverModel.getCellphone());
			break;
		case R.id.mark_number_item:
			Util.call(DriverInfoActivity.this, mMarkPhoneNumber.getEditContent());
			break;
		case R.id.delete_btn:
			if(mDriverModel.isOwner()){
				ToastUtil.show(DriverInfoActivity.this, "不能删除车主本人");
				return;
			}
			CancelDialog mCancelDialog = new CancelDialog(DriverInfoActivity.this, R.style.data_filling_dialog, mHandler);
			mCancelDialog.setCanceledOnTouchOutside(true);
			mCancelDialog.setHint("确定要删除司机？");
			mCancelDialog.setMsgCode(CommonRes.DELETE_DRIVER);
			mCancelDialog.show();
			break;
		default:
			break;
		}
		
	}
	
	private void save(){
		String nickName = mNickName.getEditContent();
		String markPhoneNumber = mMarkPhoneNumber.getEditContent();
		AjaxParams params = new AjaxParams();
		String url = add_update_driver_info_url;
		if(isNew){
//			if(mDriverModel.getDriverBookId() == null){
////				params.put("driverId", "");
//				driverBookInputFormVO.setDriverId("");
//			}else {
//				params.put("driverId", mDriverModel.getDriverId());
				driverBookInputFormVO.setDriverId(mDriverModel.getDriverId());
//			}
			driverBookInputFormVO.setNickName(nickName);
			driverBookInputFormVO.setBackupCellphone(markPhoneNumber);
			driverBookInputFormVO.setCellphone(mDriverModel.getCellphone());
//			params.put("nickName", nickName);
//			params.put("backupPhone", markPhoneNumber);
//			params.put("cellphone", mDriverModel.getCellphone());
//			url = add_url;
		}else{
			driverBookInputFormVO.setId(driverBookId);
			driverBookInputFormVO.setDriverId(driverId);
			driverBookInputFormVO.setNickName(nickName);
			driverBookInputFormVO.setBackupCellphone(markPhoneNumber);
			driverBookInputFormVO.setCellphone(mDriverModel.getCellphone());
//			params.put("id", driverBookId);
//			params.put("nickName", nickName);
//			params.put("backupPhone", markPhoneNumber);
//			url = update_url;
		}
		int idListSize = truckIdList.size();
//		Log.d("huiyuan","保存接口车辆id list size = " + truckIdList.size());
		truckVoList.clear();
		for(int i= 0; i < idListSize; i++){
			TruckAuthAppVO vo = new TruckAuthAppVO();
			vo.setTruckId(truckIdList.get(i));
			truckVoList.add(vo);
		}
		driverBookInputFormVO.setTruckVoList(truckVoList);
		params.put("driverBookInputFormVOJson",JSON.toJSONString(driverBookInputFormVO));
//		Log.d("huiyuan","司机保存接口数据 =  " + JSON.toJSONString(driverBookInputFormVO));
		DidiApp.getHttpManager().sessionPost(DriverInfoActivity.this, url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				Intent intent = new Intent();
				intent.setAction(CommonRes.DriverModify);
				sendBroadcast(intent);
				finish();
			}
			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
//				Toast.makeText(DriverInfoActivity.this,"保存司机信息错误:" + errMsg,Toast.LENGTH_LONG).show();
			}
		});
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			truckNumberList = intent.getStringArrayListExtra("truckNumber");
			truckIdList = intent.getStringArrayListExtra("truckIdList");
			Log.d("huiyuan","车辆id list size = " + truckIdList.size());
			int size = truckNumberList.size();
			if(size > 0){
				band_truck_container.setVisibility(View.VISIBLE);
				initTruckListContainer();
			}else {
				band_truck_container.setVisibility(View.GONE);
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
