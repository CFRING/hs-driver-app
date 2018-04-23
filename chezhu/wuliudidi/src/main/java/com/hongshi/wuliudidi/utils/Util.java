package com.hongshi.wuliudidi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.view.TaskOrderTitleView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {
	public static void selectStyle(Context context,int num,TaskOrderTitleView t1,TaskOrderTitleView t2,TaskOrderTitleView t3){
		switch (num) {
		case 1:
			t1.setTextColor(context.getResources().getColor(R.color.line_press_color), true);
			t2.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			t3.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			break;
		case 2:
			t1.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			t2.setTextColor(context.getResources().getColor(R.color.line_press_color), true);
			t3.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			break;
		case 3:
			t1.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			t2.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			t3.setTextColor(context.getResources().getColor(R.color.line_press_color), true);
			break;

		default:
			break;
		}
	}
	public static void selectTaskStyle(Context context,int num,TaskOrderTitleView t1,TaskOrderTitleView t2){
		switch (num) {
		case 1:
			t1.setTextColor(context.getResources().getColor(R.color.line_press_color), true);
			t2.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			break;
		case 2:
			t1.setTextColor(context.getResources().getColor(R.color.home_text_none), false);
			t2.setTextColor(context.getResources().getColor(R.color.line_press_color), true);
			break;
		default:
			break;
		}
	}
	/**
	 * 打电话
	 * @param context
	 * @param phoneNumber
	 */
	@SuppressLint("NewApi") public static void call(Context context,String phoneNumber){
//		if(android.os.Build.VERSION.SDK_INT > 11) {
//			ClipboardManager c = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//			ClipData clipdata = ClipData.newPlainText("phoneNumber", phoneNumber);
//			c.setPrimaryClip(clipdata);
//
//		}else{
//			android.text.ClipboardManager c = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//			c.setText(phoneNumber);
//		}
//		ToastUtil.show(context, "电话号码已复制到剪切板");
//		Intent intent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + phoneNumber));
//		context.startActivity(intent);
		if(phoneNumber == null || phoneNumber.length() <= 0){
			ToastUtil.show(context, "电话号码不能为空");
			return;
		}
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    context.startActivity(intent);
	}

	//Date或者String转化为时间戳
	public static long getTimeStamp(String time){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(time);
			return date.getTime();
		}catch (Exception e){
			return 0;
		}

	}


	/**
	 * 毫秒值转换成日期
	 * @param dateTime
	 * @return
	 */
	public static String getFormatedDateTime(long dateTime) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }
	public static String getFormateDateTimeShort(long dateTime) {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
		return sDateFormat.format(new Date(dateTime + 0));
	}
	/**
	 * 毫秒值转换成天数
	 * @param ms
	 * @return
	 */
	public static String formatDate(long ms){
		//初始化Formatter的转换格式。
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String hms = formatter.format(ms); 
		return hms;
	}
	/**
	 * 毫秒值转换成天数
	 * @param ms
	 * @return
	 */
	public static String formatDateMs(long ms){
		//初始化Formatter的转换格式。
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String hms = formatter.format(ms); 
		return hms;

	}
	public static String formatDate(Date date){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = "";
		if(date != null) {
			dateStr = format.format(date);
		}
		return dateStr;
	}
	public static String formatDatePoint(Date date){
		SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd");
		String dateStr = "";
		if(date != null) {
			dateStr = format.format(date);
		}
		return dateStr;
	}
	public static String formatDateSecond(Date date){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = "";
		if(date != null) {
			dateStr = format.format(date);
		}
		return dateStr;
	}
	/**
	 * 时间精确到分钟
	 * @param date
	 * @return
	 */
	public static String formatDateMinute(Date date){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = "";
		if(date != null) {
			dateStr = format.format(date);
		}
		return dateStr;
	}
	/**
	 * 毫秒数转换成 x天x小时x分x秒
	 * @param ms
	 * @return
	 */
	public static String millisecondToDaysShort(long ms){
		final long sec = 1000;
		final long min = sec * 60;
		final long hour = min * 60;
		final long day = hour *24;
		long seconds, minutes, hours, days;
		days = ms / day;
		if(days > 0){
			return "" + days + "天";
		}
		
		hours = ms / hour;
		if(hours > 0){
			return "" + hours + "小时";
		}
		
		minutes = ms / min;
		if(minutes > 0){
			return "" + minutes + "分钟";
		}
		
		seconds = ms / sec;
		return "" + seconds + "秒";
	}
	
	public static String millisecondToDays(long ms){
		final long sec = 1000;
		final long min = sec * 60;
		final long hour = min * 60;
		final long day = hour *24;
		long seconds, minutes, hours, days;
		days = ms / day;
		ms %= day;
		
		hours = ms / hour;
		ms %= hour;
		
		minutes = ms / min;
		ms %= min;
		
		seconds = ms / sec;
		return "" + days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
	}
	
	/*
	 * 将用户输入的数字转成double，保留两位小数。如果不能转成double，返回空字符串；如果所得double值非正数，返回字符串"0"
	 */
	public static String inputToDoubleStr(String input){
		try{
		      Double.parseDouble(input);
		      double parseDouble = Double.parseDouble(input);
		      DecimalFormat df=new DecimalFormat(".##");
			  String st=df.format(parseDouble);
		      return st;
		}
		catch(NumberFormatException ex){
			return "";
		}
	}
	
	/**
	 * 将用户输入的数字转成double，保留两位小数。如果不能转成double，返回-1；如果所得double值非正数，返回值0
	 */
	public static double inputToDoubleValue(String input){
		try{
		      double parseDouble = Double.parseDouble(input);
		      DecimalFormat df=new DecimalFormat(".##");
			  String st=df.format(parseDouble);
			  double mjudge = parseDouble = Double.parseDouble(st);
		      if(mjudge <= 0){
		    	  return 0.0;
		      }
		      return mjudge;
		}
		catch(NumberFormatException ex){
			return -1;
		}
	}
	
	/*
	 * 将用户输入的数字转成integer。如果不能转成integer，返回空字符串；如果所得integer值非正数，返回字符串"0"
	 */
	public static String inputToIntegerStr(String input){
		try{
		      int mjudge = Integer.parseInt(input);
		      if(mjudge <= 0){
		    	  return "0";
		      }
		      return String.valueOf(mjudge);
		}
		catch(NumberFormatException ex){
			return "";
		}
	}

	/**
	 * textview 字体变色，字体size可设置大小
	 * @param mContext
	 * @param str   整个textview的字符串
	 * @param str_color 改变颜色的字符串
	 * @param t  传人显示的控件
	 * @param id 颜色值color
	 */
 	public static void setText(Context mContext,String str,String str_color,TextView t,int id){
		int fstart=str.indexOf(str_color);
        int fend=fstart+str_color.length();
        SpannableStringBuilder style=new SpannableStringBuilder(str); 
        if(fstart >= 0 && fend <= str.length()){
        	style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(id)),fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        	t.setText(style);
        }else{
        	t.setText(str);
        }
        
	}
	/**
	 * textview 字体变色，字体size可设置大小
	 * @param mContext
	 * @param str   整个textview的字符串
	 * @param str_color 改变颜色的字符串
	 * @param t  传人显示的控件
	 * @param id 颜色值color 
	 * @param size 字体大小
	 */
	
	public static void setText(Context mContext,String str,String str_color,TextView t,int id,int size){
		int fstart=str.indexOf(str_color);
		int fend=fstart+str_color.length();
		SpannableStringBuilder style=new SpannableStringBuilder(str); 
		style.setSpan(new AbsoluteSizeSpan(size), fstart, fend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(id)),fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		t.setText(style);
	}
	public static String getVersionNmae(Context context ){
		PackageManager packageManager = context.getPackageManager();
		String version = "";
        PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			 version = packInfo.versionName;
			 return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        return version;
	}
	/**
	 * @param listview 传的view
	 * 解决listview与ScrollView的冲突
	 * 
	 */
	public static void setListView(ListView listview){
		ListAdapter listAdapter = listview.getAdapter();
	    if (listAdapter == null) {
	        return;
	    }
	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        View listItem = listAdapter.getView(i, null, listview);
	        if(listItem != null){
	        	listItem.measure(0, 0);
		        totalHeight += listItem.getMeasuredHeight();
	        }
	    }
	    ViewGroup.LayoutParams params = listview.getLayoutParams();
	    params.height = totalHeight + (listview.getDividerHeight() * (listAdapter.getCount() - 1));
	    listview.setLayoutParams(params);
	}
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
	
	
	public static String formatDoubleToString(double amount, String unit){
		if(unit == null){
			return String.valueOf(amount);
		}
		if(unit.equals("元") || unit.equals("吨") || unit.equals("立方米") || unit.equals("T") || unit.equals("M3")){
			DecimalFormat d = new DecimalFormat("#0.00");
			String format = d.format(amount);
			return format;
		}else{
			int i = (int) amount;
			return String.valueOf(i);
		}
	}
	
	/*
	 * 给入double值x，保留num位小数并返回。仅适用于x为正数、num在0——10之间的情况，其他一律返回-1.
	 */
	public static double doubleRound(double x, int num){
		if(num < 0 || num > 10 || x < 0){
			return -1;
		}else{
			return Math.round(x * Math.pow(10, num)) / Math.pow(10, num) ;
		}
	}
	
	/**
	 * edittext的文本变化监听器,小数点后面只允许有两位字符
	 */
	public static InputLimitTextWatcher getDoubleInputLimitTextWatcher(){
		return new InputLimitTextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				selectionStart = editText.getSelectionStart();
				return;
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(s.toString().contains(".")){
					if (s.length() - 1 - s.toString().indexOf(".") > 2){
						editText.removeTextChangedListener(this);
						
						editText.setText(validStr);
						if(selectionStart <= validStr.length()){
							editText.setSelection(selectionStart);
						}else{
							editText.setSelection(validStr.length());
						}
						
						editText.addTextChangedListener(this);
						return;
					}
				}
				validStr = s.toString();
			}
		};
	}
	
	/**
	 * edittext的文本变化监听器,不允许输入回车
	 */
	public static InputLimitTextWatcher getNoEnterLimitTextWatcher(){
		return new InputLimitTextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				selectionStart = editText.getSelectionStart();
				return;
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().contains("\n")){
					editText.removeTextChangedListener(this);
					
					editText.setText(validStr);
					if(selectionStart <= validStr.length()){
						editText.setSelection(selectionStart);
					}else{
						editText.setSelection(validStr.length());
					}
					
					editText.addTextChangedListener(this);
					return;
				}
				validStr = s.toString();
			}
		};
	}
	
	/**
	 * edittext的文本变化监听器,数字不允许超过范围,注意设定的范围应当为正数
	 */
	public static InputLimitTextWatcher getNumericLimitTextWatcher(final double least, final double most){
		return new InputLimitTextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				selectionStart = editText.getSelectionStart();
				return;
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				double re = Util.inputToDoubleValue(s.toString());
				//既能转成合法浮点数，又不在范围内
				if(re > 0 && !(re < most && re > least)){
					editText.removeTextChangedListener(this);
					
					editText.setText(validStr);
					if(selectionStart <= validStr.length()){
						editText.setSelection(selectionStart);
					}else{
						editText.setSelection(validStr.length());
					}
					
					editText.addTextChangedListener(this);
					return;
				}
				validStr = s.toString();
			}
		};
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
	 * 字符串是否包含表情字符
	 *
	 * @param source
	 * @return
	 */
	public static boolean containsEmoji(String source) {
		if (source.length()==0) {
			return false;
		}
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isNormalCharacter(codePoint)) {
				return true;
			}
		}

		return false;
	}
	/**
	 * 是否一般文字，包含asc码0-127的所有字符，加上中文
	 *
	 * @param codePoint
	 * @return
	 */
	public static boolean isNormalCharacter(char codePoint) {
		return (codePoint < 0x80) || ((codePoint >= 0x4E00) && (codePoint <= 0x9FA5))
				|| ((codePoint >= 0x9FA6) && (codePoint <= 0x9FCB))
				|| ((codePoint >= 0x3400) && (codePoint <= 0x4DB5))
				|| ((codePoint >= 0x20000) && (codePoint <= 0x2A6D6))
				|| ((codePoint >= 0x2A700) && (codePoint <= 0x2B734))
				|| ((codePoint >= 0x2B740) && (codePoint <= 0x2B81D));
	}

	/**
	 * EnCode方法UTF-8格式
	 * @param s 要EnEode的字符串
	 * @return EnCode好的字符串 出现异常返回null
	 */
	public static String enCode(String s){
		String enCoded = null;
		try {
			enCoded = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return enCoded;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    */
		String num = "[1][35789]\\d{9}";
		if (TextUtils.isEmpty(number)) {
			return false;
		} else {
			//matches():字符串是否在给定的正则表达式匹配
			return number.matches(num);
		}
	}

}
