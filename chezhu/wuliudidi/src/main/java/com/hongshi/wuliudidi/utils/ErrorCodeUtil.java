package com.hongshi.wuliudidi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Toast;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.RegisterActivity;
import com.hongshi.wuliudidi.dialog.AuthHintDialog;
import com.hongshi.wuliudidi.dialog.CancelDialog;
import com.hongshi.wuliudidi.dialog.HintDialog;

public class ErrorCodeUtil {
	/**
	 * 
	 * @param errCode
	 * @param mContext
	 */
	public static void errorCode(String errCode,Context mContext,String errMsg){
    	if(errCode != null){
    		if(errCode.equals("060017")){
    			//重新登陆
    			SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
//    			Intent login_intent = new Intent(mContext,LoginActivity.class);
//    			mContext.startActivity(login_intent);
//				Toast.makeText(mContext,errMsg, Toast.LENGTH_SHORT).show();
    			Editor edit = sp.edit();
//    			edit.putString("cellphone", "");
    			edit.putString("userId", "");
    			edit.putString("session_id", "");
				edit.putString("user_role","车主");
    			edit.commit();
//    			Intent ref_intent = new Intent();
//    			ref_intent.setAction(CommonRes.RefreshData);
//    			mContext.sendBroadcast(ref_intent);
//    			Intent userInfo_intent = new Intent();//我的界面更新
//    			userInfo_intent.setAction(CommonRes.RefreshUserInfo);
//    			mContext.sendBroadcast(userInfo_intent);
    		}else if(errCode.equals("042719")){
    			//您暂无任何车辆信息，请先添加车辆
    			HintDialog mDialog = new HintDialog(mContext, R.style.data_filling_dialog,
				HintDialog.showType.add_truck_type);
				if(!DidiApp.isUserAowner){
					mDialog.getmRight().setVisibility(View.GONE);
				}
    			mDialog.show();
    		}else if(errCode.equals("042720")){
    			//您还未通过实名认证，请先认证
    			HintDialog mDialog = new HintDialog(mContext, R.style.data_filling_dialog,
    			HintDialog.showType.type_auth);
    	        mDialog.show();
    		}else if(errCode.equals("042806")){
				//如果邀请的司机不是平台用户
    			AuthHintDialog mDialog = new AuthHintDialog(mContext, R.style.hint_dialog);
    			mDialog.setCanceledOnTouchOutside(true);
    			mDialog.setTitle(mContext.getResources().getString(R.string.add_driver_invalid_user_errmsg));
    			mDialog.show();
    		}else if(errCode.equals("042807")){
				//如果邀请的司机为车主
    			AuthHintDialog mDialog = new AuthHintDialog(mContext, R.style.hint_dialog);
    			mDialog.setCanceledOnTouchOutside(true);
    			mDialog.setTitle(mContext.getResources().getString(R.string.add_driver_unsuit_errmsg));
    			mDialog.show();
    		}else if(errCode.equals("042733")){
				//如果邀请的司机已经在司机列表中存在
    			AuthHintDialog mDialog = new AuthHintDialog(mContext, R.style.hint_dialog);
    			mDialog.setCanceledOnTouchOutside(true);
    			mDialog.setTitle(mContext.getResources().getString(R.string.add_driver_duplicate_errmsg));
    			mDialog.show();
    		}else if(errCode.equals("043405")){
    			//无权查看运输计划，不做处理，不弹toast
    		}else if(errCode.equals("060302")){
    			//内部推广人员的id查询失败，另行弹窗提示
    		}else if(errCode.equals("060049")){
				//账户在别的终端登陆，被挤掉了
    			CancelDialog mCancelDialog = new CancelDialog(mContext, R.style.data_filling_dialog, true);
    			mCancelDialog.setCanceledOnTouchOutside(true);
    			mCancelDialog.setHint(mContext.getString(R.string.login_on_another_terminal));
    			mCancelDialog.setRightText("重新登陆");
    			mCancelDialog.show();
    			
    			SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
    			Editor edit = sp.edit();
//    			edit.putString("cellphone", "");
    			edit.putString("userId", "");
    			edit.putString("session_id", "");
				edit.putString("user_role","车主");
    			edit.commit();
    		}else if(errCode.equals("061026")){
				//基金账户可用资金不够
				HintDialog mDialog = new HintDialog(mContext, R.style.data_filling_dialog,
				HintDialog.showType.currency_lack_type);
				mDialog.show();
			}else if("043213".equals(errCode)){
				Toast.makeText(mContext,"已接过此单!",Toast.LENGTH_LONG).show();
			}else if("120309".equals(errCode)) {
				return;
			}else {
    			Toast.makeText(mContext,errMsg, Toast.LENGTH_SHORT).show();
    		}
		}
	}
}
