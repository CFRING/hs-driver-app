package com.hongshi.wuliudidi.cashier;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;


public class Util {

	
	/**
	 * 判断网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
	public static boolean isLogin(Context context){
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		String session_id = sp.getString("session_id", "");
//		String phone_umber = sp.getString("cellphone", "");
		String user_id = sp.getString("userId", "");
//		LogUtil.myLog("lihe", "session_id="+session_id+"--"+"phone_umber="+phone_umber+"__"+"user_id=="+user_id);
		if(!session_id.equals("") && !user_id.equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * 将传入的文本输入框与按钮绑定起来。
	 * 文本输入框监听按钮的click事件，按钮被点击时，文本框清空。
	 * 按钮监听文本框输入改变，当文本框内容为空时，按钮不可见。
	 * @param mEditText
	 * @param mButton
	 */
	
	static public void BindingEditTextAndButton(final EditText mEditText, final Button mButton){
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String mContent = mEditText.getText().toString();
				if(mContent.length() == 0)
				{
					mButton.setVisibility(View.INVISIBLE);
				}
				else
				{
					mButton.setVisibility(View.VISIBLE);
				}
			}
		});		
		
		
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditText.setText("");
			}
		});
	}

	/*
	 * 给入double值x，保留num位小数并返回。仅适用于x为正数、num在0——10之间的情况，其他一律返回-1.
	 */
	public static double doubleRound(double x, int num){
		if(num < 0 || num > 10 || x < 0){
			return -1;
		}else{
			return Math.round(x * Math.pow(10, num)) / Math.pow(10, num);
		}
	}
	
	/**
	 * 去掉给定字符串内的空格
	 * @param str
	 * @return
	 */
	public static String DeleteSpace(String str){
		if(str == null){
			return "";
		}
		String reStr = "";
		char ch;
		for(int i = 0; i < str.length(); i++){
			ch = str.charAt(i);
			if(ch != ' '){
				reStr += ch;
			}
		}
		return reStr;
	}
	/**
	 * 获取资源字符串
	 * @param mContext
	 * @param stringId
	 * @return
	 */
	public static  String getResString(Context mContext,int stringId){
		return  mContext.getResources().getString(stringId);
	}

	/**
	 * textview 字体变色，字体size可设置大小
	 * @param mContext Context
	 * @param str   整个textview的字符串
	 * @param str_color 改变颜色的字符串
	 * @param t  传人显示的控件
	 * @param id 颜色值color
	 */
	public static void setText(Context mContext, String str, String str_color, TextView t, int id){
		int fstart=str.indexOf(str_color);
		int fend=fstart+str_color.length();
		SpannableStringBuilder style=new SpannableStringBuilder(str);
		style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(id)),fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		t.setText(style);
	}

	/**
	 * 设置dialog从弹出,type可以为Gravity.BOTTOM或Gravity.CENTER
	 * @param dialog
	 */
	public static void setAnimation(Dialog dialog, int type, boolean isWidMatchParent){
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.dialog);
		window.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = window.getAttributes();
		if(isWidMatchParent){
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		}else{
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		}
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		if(type == CommonRes.TYPE_BOTTOM){
			window.setGravity(Gravity.BOTTOM);
			//此处可以设置dialog显示的位置
		}else if(type == CommonRes.TYPE_CENTER){
			window.setGravity(Gravity.CENTER);
		}
	}
}
