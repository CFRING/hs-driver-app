package com.hongshi.wuliudidi.plugin.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * * Created by huiyuan on 2016/4/18.
 */
public class NetworkUtils {

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("NetWorkState", "Unavailabel");
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.i("NetWorkState", "Availabel");
						return true;
					}
				}
			}
		}
		return false;
	}

	// true;连接wifi
	// false;没有网络连接.
	/**
	 * make true current network connect service is wifi
	 * @param mContext
	 * @return
	 */
	public static boolean isWifiEnvironment(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	// true; 连接gprs
	public static boolean isMobileConnected(Context context) {
		try {
			ConnectivityManager connectivityManager = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE));
			NetworkInfo networkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (networkInfo != null) {
				return networkInfo.isConnected() && networkInfo.isAvailable();
			}
		} catch (Throwable e) {
			Log.w("NetworkUtils", "getNetworkInfo error: " + e);
		}
		return false;
	}

	public static boolean isNetworkConnectedOnHighSpeed(Context context) {
		boolean ret = false;
		try {
			ConnectivityManager cm = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE));
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				int type = ni.getType();
				if (type == ConnectivityManager.TYPE_WIFI) {
					//Log.e("SmsInterceptor", "network type: " + ni.getTypeName());
					ret = true;
				} else if (type == ConnectivityManager.TYPE_MOBILE) {
					int subType = ni.getSubtype();
					//Here include all 3G and LTE network
					if (subType == TelephonyManager.NETWORK_TYPE_UMTS ||
							(subType >= TelephonyManager.NETWORK_TYPE_EVDO_0 &&
									subType <= TelephonyManager.NETWORK_TYPE_HSPAP)) {
						//Note: For convenience, here we don't remove iden 1xRTT are not used in China
						ret = true;
					}
					//Log.e("SmsInterceptor", "network subtype: " + ni.getSubtypeName());
				}
			}
		} catch (Throwable e) {
			Log.w("NetworkUtils", "getActiveNetworkInfo error: " + e);
		}
		return ret;
	}
}
