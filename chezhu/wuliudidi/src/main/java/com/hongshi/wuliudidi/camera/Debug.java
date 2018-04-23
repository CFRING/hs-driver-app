package com.hongshi.wuliudidi.camera;

import android.text.TextUtils;
import android.util.Log;

import com.hongshi.wuliudidi.utils.LogUtil;

/**
 * 日志信息打印工具类
 * @author huiyuan
 * @title:
 * @description:
 * @version 1.0
 * @created
 * @changeRecord
 */
public class Debug {

	private static final String TAG = "HG";
	public static boolean DEBUG = true;

	private static String checkTag(String tag) {
		if (TextUtils.isEmpty(tag)) {
			tag = TAG;
		}
		return tag;

	}

	public static void debug(String msg) {
		if (DEBUG) {
			LogUtil.d(TAG, "debug: " + msg);
		}
	}

	public static void trace(String msg) {
		if (DEBUG) {
			LogUtil.v(TAG, "trace: " + msg);
		}
	}

	public static void warn(String msg) {
		if (DEBUG) {
			LogUtil.w(TAG, "warning: " + msg);
		}
	}

	public static void error(String msg) {
		if (DEBUG) {
			LogUtil.e(TAG, "error: " + msg);
		}
	}

	public static void info(String msg) {
		if (DEBUG) {
			LogUtil.i(TAG, "info: " + msg);
		}
	}

	/**
	 * 
	 * 2011 -10 - 25 添加了bytag 后缀支持添加自己的定义的tag 。 by zxy
	 * 
	 */

	public static void info(String tag, String msg) {
		if (DEBUG) {
			LogUtil.i(checkTag(tag), "info: " + msg);
		}
	}

	public static void debug(String tag, String msg) {
		if (DEBUG) {
			LogUtil.d(checkTag(tag), "debug: " + msg);
		}
	}

	public static void trace(String tag, String msg) {
		if (DEBUG) {
			LogUtil.v(checkTag(tag), "trace: " + msg);
		}
	}

	public static void warn(String tag, String msg) {
		if (DEBUG) {
			LogUtil.w(checkTag(tag), "warning: " + msg);
		}
	}

	public static void error(String tag, String msg) {
		if (DEBUG) {
			LogUtil.e(checkTag(tag), "error" + msg);
		}
	}

	public static void info(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			LogUtil.i(checkTag(tag), "info: " + msg, throwable);
		}
	}

	public static void debug(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			LogUtil.d(checkTag(tag), "debug: " + msg, throwable);
		}
	}

	public static void trace(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			LogUtil.v(checkTag(tag), "trace: " + msg, throwable);
		}
	}

	public static void warn(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			LogUtil.w(checkTag(tag), "warn: " + msg, throwable);
		}
	}

	public static void error(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			LogUtil.e(checkTag(tag), "error: " + msg, throwable);
		}
	}

	/**
	 * 强制打印msg信息
	 * 
	 * @param msg
	 */
	public static void forcePrint(String msg) {
		LogUtil.i(TAG, "forcePrint: " + msg);
	}

	/**
	 * 强制打印msg信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void forcePrint(String tag, String msg) {
		LogUtil.i(checkTag(tag), "forcePrint: " + msg);
	}

	/**
	 * 强制打印msg信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void forcePrint(String tag, String msg, Throwable throwable) {
		LogUtil.i(checkTag(tag), "forcePrint: " + msg, throwable);
	}

}
