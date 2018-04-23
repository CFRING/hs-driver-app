package com.hongshi.wuliudidi.utils;

import android.util.Log;

/**
 * Created on 2016/4/5.
 */
public class LogUtil {

    //是否打印Log信息
    private static final boolean isShow = true;

    public static void v(String tag, String msg){
        if (isShow){
            Log.v(tag, msg);
        }
    }

    public static  void v(String tag, String msg, Throwable tr){
        if (isShow){
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg){
        if (isShow){
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr){
        if (isShow){
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg, Throwable tr){
        if (isShow){
            Log.i(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg){
        if (isShow){
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if (isShow){
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr){
        if (isShow){
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg){
        if (isShow){
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr){
        if (isShow){
            Log.e(tag, msg, tr);
        }
    }

}
