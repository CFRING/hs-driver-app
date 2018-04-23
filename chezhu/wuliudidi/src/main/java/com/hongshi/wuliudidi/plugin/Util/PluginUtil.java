package com.hongshi.wuliudidi.plugin.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.hongshi.share.center.plugindownloader.PluginDownloader.DownLoadCallbackForPlugin;
import com.hongshi.share.center.plugindownloader.PluginDownloader.HttpDownloaderForPlugin;
import com.hongshi.wuliudidi.plugin.bean.ApkItem;
import com.hongshi.wuliudidi.plugin.bean.PluginParams;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.compat.PackageManagerCompat;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by huiyuan on 2016/4/18.
 */
public class PluginUtil {
    private static int countOfPluginNeedUpdate = 0;
    private static int totalSizeOfUpdate = 0;
    private static String pluginNames = "";

    public static boolean isActionAvailable(Context context, String action) {
        Intent intent = new Intent(action);
        return context.getPackageManager().resolveActivity(intent, 0) != null;
    }

    /**
     * judge if onlineVersion is a new version
     *
     * @param localVersion
     *            local version
     * @param onlineVersion
     *            online version
     * @return
     */
    public static boolean isAppNewVersion(String localVersion, String onlineVersion)
    {
        if(onlineVersion == null){
            return false;
        }
        if(localVersion == null || localVersion.equals("")){
            localVersion = "0.0.0";
        }
        if (localVersion.equals(onlineVersion))
        {
            return false;
        }
        String[] localArray = localVersion.split("\\.");
        String[] onlineArray = onlineVersion.split("\\.");

        int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;

        for (int i = 0; i < length; i++)
        {
            try {
                if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i]))
                {
                    return true;
                }
                else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i]))
                {
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
//                Log.d("huiyuan","plugin download failed, error msg = " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public static void downloadPlugin(final Context context,String name, String url, String md5, final String version){
        HttpDownloaderForPlugin.getInstance(context)
                .startDownload(name, url, md5, new DownLoadCallbackForPlugin(url) {
                    @Override
                    public void onStart() {
//                        Log.d("huiyuan", "start Download!");
                    }

                    @Override
                    public void onProgress(long totalSize, long currentSize, long speed) {
//                        Log.d("huiyuan", "download in Progress, currentSize = " + currentSize);
                    }

                    @Override
                    public void onSuccess(final String rootPath, final String downloadedFileName) {
                        if(countOfPluginNeedUpdate > 0){
                            countOfPluginNeedUpdate = countOfPluginNeedUpdate -1;
                        }else {
                            countOfPluginNeedUpdate = 0;
                        }
                        Log.d("huiyuan", downloadedFileName + " " + version + " downloaded success!! ");
                        context.getSharedPreferences("plugin_config", Activity.MODE_PRIVATE)
                        .edit().putString(downloadedFileName + "_downloaded_version",version).commit();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                installPlugin(context,rootPath + File.separator + downloadedFileName + ".apk",downloadedFileName);
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(String strMsg) {
                        if(countOfPluginNeedUpdate > 0){
                            countOfPluginNeedUpdate = countOfPluginNeedUpdate -1;
                        }else {
                            countOfPluginNeedUpdate = 0;
                        }
//                        Log.d("huiyuan", "download failed erro msg = " + strMsg);
                    }

                    @Override
                    public void onFinish() {
//                        Log.d("huiyuan", "download finish");
                    }
                });
    }

    public static void loadPlugin(Context context, String action, HashMap<String,String> param) {
        try {
            if(isActionAvailable(context,action)){
                Intent intent = new Intent(action);
                intent.putExtra("params",param);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e){
            Toast.makeText(context, "plugin loaded failed, the error is :" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static String getPkgName(String pluginName){
        if(pluginName.equals(PluginParams.INCOME_BOOK)){
            return PluginParams.INCOME_BOOK_PKG_NAME;
        }else if(pluginName.equals(PluginParams.MY_CASH)){
            return PluginParams.MY_CASH_PKG_NAME;
        }else if(pluginName.equals(PluginParams.MY_ROUTE)){
            return PluginParams.MY_ROUTE_PKG_NAME;
        }
        return "";
    }

    public static String getApkActionStr(String pluginName){
        if(pluginName.equals(PluginParams.INCOME_BOOK)){
            return PluginParams.INCOME_BOOK_ACTION;
        }else if(pluginName.equals(PluginParams.MY_CASH)){
            return PluginParams.MY_CASH_ACTION;
        }else if(pluginName.equals(PluginParams.MY_ROUTE)){
            return PluginParams.MY_ROUTE_ACTION;
        }
        return "";
    }

    public static boolean isInstalledPkg(String pkgName){
        List<PackageInfo> installedPackages = null;
        try {
            installedPackages = PluginManager.getInstance().getInstalledPackages(0);
//            if(installedPackages != null && installedPackages.get(0) != null)
//            Log.d("huiyuan"," installed packages = " + installedPackages.get(0).toString());
            if(!pkgName.equals("") && installedPackages != null){
                for(PackageInfo info : installedPackages){
                    if(pkgName.equals(info.packageName)){
                        return true;
                    }else {
                        return false;
                    }
                }
            }
        }catch (Exception e){
            Log.d("huiyuan",e.getMessage());
        }finally {
            return false;
        }
    }

    /** you should start a Thread to install plugin, because this may be time-consuming.
     *  @param apkPath: the plugin apk file path to install
     *  @param pluginName: the name of the plugin, plugin names defined in PluginParams.java
     *  @return installed result
     */
    public static int installPlugin(Context context, String apkPath, String pluginName) {
        if (!PluginManager.getInstance().isConnected()) {
            Log.d("huiyuan", "plugin manager service connected failed!");
        }
        try {
            File apkFile = new File(apkPath);
            int result = 1000;
            if(apkFile.exists()){
                String apkActionStr = getApkActionStr(pluginName);
                if(isActionAvailable(context, apkActionStr)){//update install
                    result = PluginManager.getInstance().installPackage(apkPath, PackageManagerCompat.INSTALL_REPLACE_EXISTING);
                }else {//new install
                    result = PluginManager.getInstance().installPackage(apkPath,0);
                }
            }
//            Log.d("huiyuan", "install result = " + result);
            boolean isRequestPermission = (result == PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION);
            if (isRequestPermission) {
//                Log.d("huiyuan", "plugin install failed for no requested permission.");
                return PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION;//permission deny
            }
        } catch (RemoteException e) {
            e.printStackTrace();
//            Log.d("huiyuan","plugin installed failed!");
            return PackageManagerCompat.INSTALL_FAILED_INTERNAL_ERROR;//install fail
        }
//        Log.d("huiyuan", "plugin installed success!");
        return PackageManagerCompat.INSTALL_SUCCEEDED;//install success
    }

    public static void deletePlugin(String pkgName){
        try {
             PluginManager.getInstance().deletePackage(pkgName, 0);
//            Log.d("huiyuan"," delete plugin success!!!!!!!");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return ;
        }
    }

    public static void checkPluginUpdate(final Context context, String appName, String appType){
        FinalHttp mFinalHttp = new FinalHttp();
        AjaxParams params = new AjaxParams();

        params.put("appName", appName);
        params.put("appType", appType);

        mFinalHttp.configCharset("UTF-8");
        mFinalHttp.configTimeout(10000);
        totalSizeOfUpdate = 0;
//        Log.i("huiyuan", "plugin update url = " + PluginParams.CHECK_PLUGIN_UPDATE_URL + params);
        mFinalHttp.post(PluginParams.CHECK_PLUGIN_UPDATE_URL, params, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String t) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(t);
                    boolean isSuccess = jsonObject.optBoolean("success");
                    if (isSuccess) {
                        JSONObject body = jsonObject.optJSONObject("body");
                        if (body != null) {
//                            Log.i("huiyuan", "plugin info  = " + body.toString());
                            JSONArray pluginList = body.optJSONArray("pluginList");
                            int length = pluginList.length();
                            if (length > 0) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject pluginConfig = (JSONObject) pluginList.get(i);
                                    String pluginName = pluginConfig.optString("pluginName");
                                    if (pluginName == null || pluginName.equals("")) {
                                        continue;
                                    }
                                    String pluginVersionInServer = pluginConfig.optString("pluginVersion");
                                    String pluginMd5 = pluginConfig.optString("md5");
                                    String pluginUrl = pluginConfig.optString("updateUrl");
                                    String pluginSize = pluginConfig.optString("pluginSize");
                                    pluginNames = pluginNames + "," + pluginName;

                                    JSONObject pluginLocalConfig = new JSONObject();
                                    pluginLocalConfig.put("pluginName", pluginName);
                                    pluginLocalConfig.put("pluginVersion", pluginVersionInServer);
                                    pluginLocalConfig.put("md5", pluginMd5);
                                    pluginLocalConfig.put("updateUrl", pluginUrl);
                                    pluginLocalConfig.put("pluginSize",pluginSize + "k");

                                    SharedPreferences sharedPreferences = context.getSharedPreferences("plugin_config", Activity.MODE_PRIVATE);
                                    try {
                                        String localPluginConfig = sharedPreferences.getString("plugin_" + pluginName, "");
                                        String localPluginVersion;

                                        if (localPluginConfig.equals("")) {
                                            localPluginVersion = "0.0.0";
                                        } else {
                                            JSONObject pluginInfo = new JSONObject(localPluginConfig);
                                            localPluginVersion = pluginInfo.optString("pluginVersion");
                                            if (localPluginVersion == null || localPluginVersion.equals("")) {
                                                localPluginVersion = "0.0.0";
                                            }
                                        }

                                        String pluginDownloadedVersion = sharedPreferences
                                                .getString(pluginName + "_downloaded_version", "0.0.0");

                                        boolean hasUpdate = PluginUtil.isAppNewVersion(localPluginVersion, pluginVersionInServer);
                                        boolean isDownloadedPluginOldVersion = PluginUtil.isAppNewVersion(pluginDownloadedVersion, pluginVersionInServer);
//                                        Log.d("huiyuan", pluginName + " has update " + hasUpdate + " , Local plugin is old ? " + isDownloadedPluginOldVersion);

                                        if (hasUpdate || isDownloadedPluginOldVersion) {
                                            if (countOfPluginNeedUpdate <= 0) {
                                                countOfPluginNeedUpdate = 0;
                                            } else {
                                                countOfPluginNeedUpdate = countOfPluginNeedUpdate + 1;
                                            }
                                            if(!"".equals(pluginSize))
                                            {
                                                totalSizeOfUpdate = totalSizeOfUpdate + Integer.parseInt(pluginSize);
                                            }
                                            pluginLocalConfig.put("needUpdate", true);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    sharedPreferences.edit().putString("plugin_" + pluginName,
                                            pluginLocalConfig.toString()).commit();
                                }
                                downloadPlugins(context);
                            }
                        }
                    } else {
                        Log.d("huiyuan", String.valueOf(jsonObject.optInt("errCode")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (!(countOfPluginNeedUpdate > 0))
                {
                    installLocalPlugins(context);
                }
            }
        });
    }

    /**
     * install local plugins
     */
    public static void installLocalPlugins(Context context){
        ApkOperator.getApk(context,
                context.getApplicationContext().getFilesDir().getAbsolutePath(),
                new ApkOperator.GetApkCallBack() {
                    @Override
                    public void getApkCallBack(ArrayList<ApkItem> apkItems) {
                        ApkOperator.installApk(apkItems, null);
                    }
                });
    }

    //added by huiyuan for plugin load
    public static void startPlugin(Context context, String action, String pluginName,HashMap<String,String> params){
            boolean isActionAvailable = PluginUtil.isActionAvailable(context, action);
            if(!isActionAvailable){
                String apkPath = context.getApplicationContext().getFilesDir().getAbsolutePath()
                        + File.separator + pluginName + ".apk";
                File apkFile = new File(apkPath);
                if(apkFile.exists()){
                    PluginUtil.installPlugin(context, apkPath, pluginName);
                }
            }
            loadPlugin(context, action, params);
    }

    public static void downloadPlugins(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("plugin_config", Activity.MODE_PRIVATE);
        String names[] = pluginNames.split(",");
        String networkEnv = "";
        if(totalSizeOfUpdate > 0 && totalSizeOfUpdate < 1000){
            networkEnv = "all";
        }else if(totalSizeOfUpdate > 1000 && totalSizeOfUpdate < 10000){
            networkEnv = "highspeed";
        }else if(totalSizeOfUpdate > 10000){
            networkEnv = "wifi";
        }
        int length = names.length;
        for(int i = 0; i < length; i++ ){
//            Log.d("huiyuan",names[i]);
            String localPluginJson = sharedPreferences.getString("plugin_" + names[i], "");
            try {
                JSONObject pluginConfig = new JSONObject(localPluginJson);
                String pluginVersionInServer = pluginConfig.optString("pluginVersion");
                String pluginMd5 = pluginConfig.optString("md5");
                String pluginUrl = pluginConfig.optString("updateUrl");
                boolean needUpdate = pluginConfig.optBoolean("needUpdate");

//                Log.d("huiyuan","插件名 = " + names[i] + " 版本号 = " + pluginVersionInServer
//                + " md5 = " + pluginMd5 + " 资源链接 = " + pluginUrl);
                if(needUpdate){
                    if (networkEnv.equals("wifi") && NetworkUtils.isWifiEnvironment(context)) {
                        PluginUtil.downloadPlugin(context, names[i], pluginUrl, pluginMd5, pluginVersionInServer);
                    }else if(networkEnv.equals("highspeed") && NetworkUtils.isNetworkConnectedOnHighSpeed(context)){
                        PluginUtil.downloadPlugin(context, names[i], pluginUrl, pluginMd5, pluginVersionInServer);
                    }else if(networkEnv.equals("all") && NetworkUtils.isNetworkAvailable(context)){
                        PluginUtil.downloadPlugin(context, names[i], pluginUrl, pluginMd5, pluginVersionInServer);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (!(countOfPluginNeedUpdate > 0))
        {
            installLocalPlugins(context);
        }
    }
}
