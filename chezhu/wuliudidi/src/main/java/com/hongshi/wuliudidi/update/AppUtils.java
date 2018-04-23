package com.hongshi.wuliudidi.update;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.activity.ActivateAccountActivity;
import com.hongshi.wuliudidi.activity.DriverMainActivity;
import com.hongshi.wuliudidi.activity.MainActivity;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.DeviceInfoModel;
import com.hongshi.wuliudidi.model.UserLoginModel;
import com.hongshi.wuliudidi.model.UserOrganizationVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.share.BannerModelList;
import com.hongshi.wuliudidi.utils.JpushUtil;
import com.hongshi.wuliudidi.utils.MD5Util;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;

/**
 * apk安装的工具类
 */
public class AppUtils {

	public static void installApplication(Context context, File apkfile) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkfile),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static int getAppVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(),0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static String get(String key){
		try {
			Class clazz=Class.forName("android.os.SystemProperties");
			Method method = clazz.getMethod("get", String.class);
			return (String) method.invoke(clazz.newInstance(), key);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getIMEI(Activity activity) {
		TelephonyManager telephonyManager = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		if (imei == null) {
			return "";
		}
		return imei;
	}
	public static String getMac() {
		String macSerial = null;
		String str = "";

		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}
	/**
	 * @Description: 屏幕分辨率
	 * @param activity
	 * @return String
	 */
	public static String getScreen(Activity activity) {
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = screenWidth = display.getWidth();
		int screenHeight = screenHeight = display.getHeight();
		return screenWidth + "*" + screenHeight;
	}

	/**
	 * @Description: 网络类型
	 * @param activity
	 * @return String
	 */
	public static String getNetType(Activity activity) {
		ConnectivityManager connectMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();

		if (info == null) {
			return "无网络";
		}

		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return "WIFI";
		}

		if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			return "CELL";
		}
		return "未知";
	}

	public static String getNetTypeAOperators(Activity activity) {
		ConnectivityManager connectMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();

		String strNetworkType = "";
		String operators = getOperators(activity);

		if (info == null) {
			return operators;
		}
		if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			String _strSubTypeName = info.getSubtypeName();

			// TD-SCDMA networkType is 17
			switch (info.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					// api<8 : replace by 11
				case TelephonyManager.NETWORK_TYPE_IDEN:
					strNetworkType = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
					// api<9 : replace by 14
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
					// api<11 : replace by 12
				case TelephonyManager.NETWORK_TYPE_EHRPD:
					// api<13 : replace by 15
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					strNetworkType = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_LTE:
					// api<11 : replace by 13
					strNetworkType = "4G";
					break;
				default:
					// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
					if (_strSubTypeName.equalsIgnoreCase("TD_SCDMA")
							|| _strSubTypeName.equalsIgnoreCase("TDS_HSDPA")
							|| _strSubTypeName.equalsIgnoreCase("WCDMA")
							|| _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
						strNetworkType = "3G";
					} else {
						strNetworkType = _strSubTypeName;
					}

					break;
			}
			return operators + strNetworkType;
		}
		return operators;
	}

	/**
	 * 返回运营商 需要加入权限 <uses-permission
	 * android:name="android.permission.READ_PHONE_STATE">
	 *
	 * @return 1,代表中国移动，2，代表中国联通，3，代表中国电信，0，代表未知
	 * @author yaojian
	 */
	public static String getOperators(Activity activity) {
		// 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country
		// Code，MCC）（也称为“MCC /
		// MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
		ConnectivityManager connectMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();

		String OperatorsName = "未知";
		String IMSI = ((TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
		// IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
		if (IMSI == null) {
			return "无运营商";
		}
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
				|| IMSI.startsWith("46007")) {
			OperatorsName = "中国移动";
		} else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
			OperatorsName = "中国联通";
		} else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {
			OperatorsName = "中国电信";
		}
		return OperatorsName;
	}

	public static String getAppIP(Activity activity) {
		ConnectivityManager connectMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();

		String IP = "";
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				IP = getLocalIpAddress(activity);
			} else {
				IP = getIpAddress();
			}
		}

		return IP;
	}

	/**
	 * @Description: wifi获取局域网地址
	 * @param activity
	 * @return String
	 */
	public static String getLocalIpAddress(Activity activity) {
		WifiManager wifiManager = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAddress = wifiInfo.getIpAddress();

		// 返回整型地址转换成“*.*.*.*”地址
		return String.format("%d.%d.%d.%d", (ipAddress & 0xff),
				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
	}

	/**
	 * @Description: 手机网络 获取公网IP地址 wifi获取局域网地址
	 * @return String
	 */
	public static String getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 *
	 * @param activity
	 * @param type     是否登陆 1.登录前调用，2，登录后调用
	 * @return
	 */
	public static String appInfo(Activity activity,String userName,String type){
		// 方法1 Android获得屏幕的宽和高
		String screen = getScreen(activity);
		String appVer = Util.getVersionNmae(activity);
		String appSpm = "init";
		String imei = getIMEI(activity);

//		ConnectivityManager connectMgr = (ConnectivityManager) activity
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		String localIp ="";
		String remoteIp = "";
//		if (info != null) {
//			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
//				localIp = getLocalIpAddress(activity);
//			} else {
//				remoteIp = getIpAddress();
//			}
//		}
		remoteIp = getIpAddress();
		String mac = getMac();
		String net = getNetType(activity);
		String platform = "android";
		//运营商
		String provider = getNetTypeAOperators(activity);
		//客户端发布类型
		String publisher = "Store";
		//手机型号);
		String ua =  android.os.Build.MODEL;
		String version =  android.os.Build.VERSION.RELEASE;

		DeviceInfoModel deviceInfoModel = new DeviceInfoModel();
		deviceInfoModel.setAppSpm(appSpm);
		deviceInfoModel.setAppVer(appVer);
		deviceInfoModel.setImei(imei);
//		deviceInfoModel.setLocalIp(localIp);
		deviceInfoModel.setRemoteIp(remoteIp);
		deviceInfoModel.setMac(mac);
		deviceInfoModel.setNet(net);
		deviceInfoModel.setPlatform(platform);
		deviceInfoModel.setProvider(provider);
		deviceInfoModel.setPublisher(publisher);
		deviceInfoModel.setScreen(screen);
		deviceInfoModel.setType(type);
		deviceInfoModel.setUa(ua);
		deviceInfoModel.setVersion(version);
		deviceInfoModel.setUserName(userName);
		return JSON.toJSONString(deviceInfoModel);
	}
	public static void upLoadInfo(String userSystemInfo,Context mContext){
		String url_info = GloableParams.HOST + "commonservice/usersysteminfo/addusersysteminfo.do?";
		AjaxParams params = new AjaxParams();
		params.put("userSystemInfoVo",userSystemInfo);

		DidiApp.getHttpManager().sessionPost(mContext, url_info, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {

			}
		});
	}
	public void doLogin(final Activity context,String password, final String account){
		//MD5加密后的字符串
		//登陆接口
		String login_url = GloableParams.LOGIN + "user/login.do?";
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("account", account);;
		edit.commit();

		final String passwd = MD5Util.getMD5String(password);
		AjaxParams params = new AjaxParams();
		params.put("account",account);
		params.put("passwd", passwd);
		//5代表车主登陆i
		params.put("roleRefer", "5");
		final PromptManager mPromptManager = new PromptManager();
		mPromptManager.showProgressDialog1(context, "正在登陆");
		DidiApp.getHttpManager().post(context, login_url, params, new ChildAfinalHttpCallBack() {
			@Override
			public void data(String t) {
				if(!context.isFinishing()){
					mPromptManager.closeProgressDialog();
				}
				JSONObject jsonObject;
//				Log.d("huiyuan","登录返回信息 = " + t);
				try {
					jsonObject = new JSONObject(t);
					String body = jsonObject.getString("body");
					UserLoginModel userModel = JSON.parseObject(
							body, UserLoginModel.class);
					if (userModel.getCurrentRole()) {
						saveLoginData(context,userModel);
					} else {
						Intent intent = new Intent(context, ActivateAccountActivity.class);
						intent.putExtra("account",account);
						intent.putExtra("psw",passwd);
						intent.putExtra("userId",userModel.getUserId());
						context.startActivity(intent);
						context.finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("huiyuan","登录报错信息是:" + e.getMessage());
					ToastUtil.show(context, e.getMessage());
				}
			}

			@Override
			public void onFailure(String errCode, String errMsg, Boolean errSerious) {
				mPromptManager.closeProgressDialog();
			}
		});
	}

	public void saveLoginData(Activity context,UserLoginModel userModel){
		BannerModelList.getAdvertList(context);
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		String name = "";
		String userRoleText = "";
		Intent intent = new Intent();
		if (null != userModel.getRealName()
				&& !userModel.getRealName().equals("")) {
			name = userModel.getRealName();
		} else {
			name = userModel.getEnterpriseName();
		}
		List<UserOrganizationVO> list = userModel.getUserOrganizationList();
		if(list != null){
			int size = list.size();
			UserOrganizationVO userOrganizationVO;
			for(int i=0; i < size; i++){
				userOrganizationVO = list.get(i);
				if(userOrganizationVO == null){
					break;
				}
				userRoleText = userOrganizationVO.getRoleTypeText();
				if(userOrganizationVO.getRoleType() == 6 || "车主".equals(userRoleText)){
					DidiApp.setUserOwnerRole(true);
					saveUserRole(edit,userRoleText);
					intent = new Intent(context, MainActivity.class);
					break;
				}else if(userOrganizationVO.getRoleType() == 11 || "司机".equals(userRoleText)){
					DidiApp.setUserOwnerRole(false);
					saveUserRole(edit,userRoleText);
					intent = new Intent(context, DriverMainActivity.class);
					break;
				}else {
					DidiApp.setUserOwnerRole(true);
					saveUserRole(edit,userRoleText);
					intent = new Intent(context, MainActivity.class);
				}
			}
		}

		String userSystemInfo = AppUtils.appInfo(context,userModel.getCellphone(),"2");
		upLoadInfo(userSystemInfo,context);
		edit.putString("cellphone", userModel.getCellphone());
		edit.putString("name", name);
		edit.putString("userId", userModel.getUserId());
		// 用户头像
		edit.putString("userFace",
				userModel.getUserFace());

		edit.commit();
		CommonRes.UserId = userModel.getUserId();
		Intent ref_intent = new Intent();
		ref_intent.setAction(CommonRes.RefreshData);
		context.sendBroadcast(ref_intent);
		// 我的界面更新
		Intent userInfo_intent = new Intent();
		userInfo_intent.setAction(CommonRes.RefreshUserInfo);
		context.sendBroadcast(userInfo_intent);
		// 启动消息线程广播
		Intent start_intent = new Intent();
		start_intent.setAction(CommonRes.CometStart);
		start_intent.setAction(CommonRes.CometStartForDriver);
		context.sendBroadcast(start_intent);
		// 登录成功初始化推送
		JpushUtil mJpushUtil = new JpushUtil();
		mJpushUtil.initJpush(context,"", userModel.getUserId());
		if(null != intent){
			context.startActivity(intent);
		}
		context.finish();
	}

	private void saveUserRole(SharedPreferences.Editor editor,String roleText){
		editor.putString("user_role",roleText);
	}
}
