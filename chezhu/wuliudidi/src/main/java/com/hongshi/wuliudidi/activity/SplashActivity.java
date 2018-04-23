package com.hongshi.wuliudidi.activity;

/**
 * @author huiyuan
 * 欢迎界面
 */

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.MapView;
import com.example.administrator.myapplication.DialogTools;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.http.HttpManager;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.BannerModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.share.BannerModelList;
import com.hongshi.wuliudidi.update.AppUtils;
import com.hongshi.wuliudidi.update.DownloadManager;
import com.hongshi.wuliudidi.update.UpdateDialog;
import com.hongshi.wuliudidi.update.UpdateInfo;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends Activity implements AMapLocationListener {

    private UpdateInfo update;
    private LocationManagerProxy aMapLocManager = null;
    private MapView mapView;
    private SharedPreferences sharedPreferences;
    private Editor edit;
    private boolean isFirst = true;
    //	int flag = 0;
    private boolean androidUpdateMadatory = false;
    private String upload_url = GloableParams.HOST + "common/lbs/upload.do?";
    private Dialog dlg = null;
    private DialogTools.Paras paras;
    int currentSize;
    int totalSize;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                    currentSize = msg.arg1;
                    totalSize = msg.arg2;
                    paras.getSeekBar().setProgress(100*currentSize/totalSize);
                    String curSize = decimalFormat.format((float) currentSize/1000/1000);
                    String allSize = decimalFormat.format((float) totalSize/1000f/1000f);
                    paras.getTxtProgressInfo().setText(curSize + "M" + allSize + "/M");
                    paras.getTxtProgressPercent().setText("" + 100*currentSize/totalSize + "%");
                    if(currentSize >= totalSize){
                        dlg.dismiss();
                    }
                    break;
                case 0:
                    boolean isShowGuide = sharedPreferences.getBoolean("isShowGuide", true);
                    Intent intent;
                    if (isShowGuide) {
                        intent = new Intent(SplashActivity.this, GuideActivity.class);
                    } else {
//                        Log.d("huiyuan","local user is " + sharedPreferences.getString("user_role","车主"));
                        if("司机".equals(sharedPreferences.getString("user_role","车主"))){
                            DidiApp.setUserOwnerRole(false);
                            intent = new Intent(SplashActivity.this, DriverMainActivity.class);
                        }else {
                            DidiApp.setUserOwnerRole(true);
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }
                    }
                    startActivity(intent);
                    finish();
                    break;
                // 1升级
                case 1:
//                    final ProgressDialog pd = new ProgressDialog(
//                            SplashActivity.this);
//                    pd.setCancelable(false);
//                    pd.setTitle("更新提醒:");
//                    pd.setMessage("正在下载更新apk");
//                    // 显示指定水平方向的进度条
//                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    pd.show();
                    dlg.show();
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        new Thread() {
                            @Override
                            public void run() {
                                File f = new File(Environment.getExternalStorageDirectory(), "temp.apk");
                                File file = DownloadManager.download(
                                        update.getAndroidPackageUrl(),
                                        f.getAbsolutePath(),mHandler);
                                if (file == null) {
                                } else {
                                    AppUtils.installApplication(getApplicationContext(), file);
                                }
                            }
                        }.start();
                    } else {
                        ToastUtil.show(getApplicationContext(), "sd卡不可用");
                        start();
                    }
                    break;
                // 2不升级
                case 2:
                    if (!androidUpdateMadatory) {
                        start();
                    } else {
                        System.exit(0);
                    }
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splach);
        dlg = new Dialog(this,R.style.appdialog);
        paras = DialogTools.updateProgressDialog(SplashActivity.this, dlg);
        dlg.dismiss();
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String userRoleText = sharedPreferences.getString("user_role","车主");
        if("车主".equals(userRoleText)){
            DidiApp.setUserOwnerRole(true);
        }else {
            DidiApp.setUserOwnerRole(false);
        }

        //上次退出或者上传失败保存到本地存储
        String addressInfo = sharedPreferences.getString("addressInfo", "");
        if (!addressInfo.equals("") && Util.isLogin(SplashActivity.this)) {
            //上传上次未上传的本地数据
        }
        edit = sharedPreferences.edit();
        //初始化地图
        mapView = (MapView) findViewById(R.id.main_map);
        // 此方法必须重写
        mapView.onCreate(savedInstanceState);
        aMapLocManager = LocationManagerProxy.getInstance(this);
        aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
        String userSystemInfo = "";
        if(Util.isLogin(SplashActivity.this)){
            String userName = sharedPreferences.getString("cellphone", "");
            userSystemInfo = AppUtils.appInfo(SplashActivity.this,userName,"2");
        }else{
            userSystemInfo = AppUtils.appInfo(SplashActivity.this, "", "1");
        }
        AppUtils.upLoadInfo(userSystemInfo,getApplicationContext());

        init();
    }

    private void init() {
        if(Util.isLogin(this)){
            BannerModelList.getAdvertList(getApplicationContext());
        }
        filldata();
    }


    /**
     * 从服务器获取版本号信息
     *
     * @return
     */
    private void filldata() {
        checkUpdate(this);
    }

    // http://www.bjtime.cn
    private void checkUpdate(final Context context) {
        String url = GloableParams.HOST + "commonservice/basic/featchInfo.do?";
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(SplashActivity.this, url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(t);
                    String body = jsonObject.getString("body");
//					LogUtil.d("huiyuan", "升级信息==" + t + " 本地版本号 = " + AppUtils.getAppVersionCode(context));
                    if (jsonObject != null) {
                        update = JSON.parseObject(body, UpdateInfo.class);
                        androidUpdateMadatory = update.isAndroidUpdateMadatory();
                        CommonRes.TRUCK = update.isAllowDriverDuplicate();
                        String content = update.getAndroidComment();
                        if (AppUtils.getAppVersionCode(context) != (update.getAndroidVcode())) {
                            UpdateDialog mUpdateDialog = new UpdateDialog(context, R.style.dialog, mHandler,
                                    content);
                            mUpdateDialog.setCanceledOnTouchOutside(false);
                            Window window = mUpdateDialog.getWindow();
                            WindowManager.LayoutParams lp = window.getAttributes();
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.gravity = Gravity.CENTER;
                            window.setAttributes(lp);
                            window.setBackgroundDrawableResource(R.color.half_transparent);
                            mUpdateDialog.show();
                        } else {
//							Toast.makeText(context, "当前是最新版本", 0).show();
                            start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    start();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                start();
            }
        });
    }

    /**
     * 显示更新对话框
     */
//    protected void showUpdateDialog(final UpdateInfo update) {
//        Builder builder = new Builder(this);
//        builder.setTitle("更新提醒");
//        AlertDialog dialog = builder.create();
//        builder.setCancelable(false);
//        // dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.app_icon);
//        builder.setIcon(R.drawable.icon);
////		String desc = update.getVersionDesc();
//        String desc = "djskfjdskfnk";
//        builder.setMessage(desc != null ? desc : "");
//        builder.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                start();
//            }
//        });
//        builder.setPositiveButton("立刻升级", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                final ProgressDialog pd = new ProgressDialog(
//                        SplashActivity.this);
//                pd.setCancelable(false);
//                pd.setTitle("更新提醒:");
//                pd.setMessage("正在下载更新apk");
//                // 显示指定水平方向的进度条
//                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pd.show();
//                if (Environment.getExternalStorageState().equals(
//                        Environment.MEDIA_MOUNTED)) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            File f = new File(Environment
//                                    .getExternalStorageDirectory(), "temp.apk");
//                            File file = DownloadManager.download(update.getAndroidPackageUrl(),
//                                    f.getAbsolutePath(), pd,mHandler);
//                            if (file == null) {
//                            } else {
//                                AppUtils.installApplication(getApplicationContext(), file);
//                            }
//                            pd.dismiss();
//                        }
//
//                        ;
//                    }.start();
//                } else {
//                    ToastUtil.show(getApplicationContext(), "sd卡不可用");
//                    start();
//                }
//            }
//        });
//        builder.setNegativeButton("下次再说", new OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                System.exit(0);
//            }
//        });
//        builder.show();
//    }

    private void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    go();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    private void go() {
        Message msg = Message.obtain();
        msg.what = 0;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null && location.getAMapException().getErrorCode() == 0) {
            double geoLat = location.getLatitude();
            double geoLng = location.getLongitude();
            String currentAddr = location.getProvince() + location.getCity() + location.getDistrict();

            if (CommonRes.time < 10) {
                CommonRes.time = CommonRes.time + 1;
            } else {
                if (CommonRes.time == 10) {
                    CommonRes.time = 0;
                }
//				CommonRes.time = 0;
//                if (Util.isLogin(SplashActivity.this)) {
                    Date dt = new Date();
                //这就是距离1970年1月1日0点0分0秒的毫秒数
                    long time = dt.getTime();
                    String jsonString = "" + geoLng + ";" + geoLat + ";" + location.getProvince() + "|" + location.getCity()
                            + "|" + location.getDistrict() + "|" + location.getStreet() + ";" + time;
//					Log.i("http", "转化后的数据===" + currentAddr);
                    DidiApp.list.add(jsonString);
                    if (DidiApp.list.size() == 20) {
                        //上传位置信息
                        upload(JSON.toJSONString(DidiApp.list));
//						LogUtil.myLog("http", "转化后的数据==="+JSON.toJSONString(DidiApp.list));
                        DidiApp.list.clear();
                    }
//                }
            }
//			LogUtil.myLog("http", "CommonRes.time==="+CommonRes.time);
            if (isFirst) {
                isFirst = false;
//                Log.i("http", "转化后的数据===" + currentAddr);
                edit.putString("latitude", "" + geoLat);
                edit.putString("longitude", "" + geoLng);
                edit.putString("current_location",currentAddr);
                edit.commit();
            }
        }
    }

    private void upload(String json) {
        AjaxParams params = new AjaxParams();
        params.put("param", json);
        DidiApp.getHttpManager().sessionPost(SplashActivity.this, upload_url, params, new ChildAfinalHttpCallBack() {

            @Override
            public void data(String t) {

            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        aMapLocManager = null;
        mHandler.removeCallbacks(null);
    }
}
