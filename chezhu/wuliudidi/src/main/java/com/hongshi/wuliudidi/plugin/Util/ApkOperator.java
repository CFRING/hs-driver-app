package com.hongshi.wuliudidi.plugin.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.RemoteException;

import com.hongshi.wuliudidi.plugin.bean.ApkItem;
import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;
import java.util.ArrayList;

public class ApkOperator {

    //处理安装apk返回结果的回调
    interface InstallApkCallBack{
        void installApkCallBack(String result);
    }
    //处理从下载文件夹获取Apk获取的 ApkItemList 的回调
    interface GetApkCallBack {
        void getApkCallBack(ArrayList<ApkItem> apkItems);
    }

    /**
     * 安装Apk, 耗时较长, 需要使用异步线程
     *
     * @param item Apk项
     * @return [0:成功, 1:已安装, -1:连接失败, -2:权限不足, -3:安装失败]
     */
    private static String myInstallApk(final ApkItem item) {
        if (!PluginManager.getInstance().isConnected()) {
            return "连接失败"; // 连接失败
        }

        if (isApkInstall(item)) {
            return "已安装"; // 已安装
        }

        try {
            int result = PluginManager.getInstance().installPackage(item.apkFile, 0);
            boolean isRequestPermission = (result == PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION);
            if (isRequestPermission) {
                return "权限不足";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return "安装失败";
        }

        return "成功";
    }

    /**
     * 用异步线程安装apk
     * @author bian
     * @param installApkCallBack:处理返回结果的回调; apkItems:要安装的包
     * @return void
     * created at 2016/4/22 11:41
     */
    public static void installApk(final ArrayList<ApkItem> apkItems, final InstallApkCallBack installApkCallBack){
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String result = "DOWNLOAD文件夹下没有插件";
                for(int i=0;i<apkItems.size();i++){
                    result = myInstallApk(apkItems.get(i));
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                if(installApkCallBack != null){
                    installApkCallBack.installApkCallBack(s);
                }
            }
        }.execute();
    }

    // Apk是否已安装
    private static boolean isApkInstall(ApkItem apkItem) {
        PackageInfo info = null;
        try {
            info = PluginManager.getInstance().getPackageInfo(apkItem.packageInfo.packageName, 0);
        } catch (NullPointerException e){
            return false;
        }catch (RemoteException e) {
            e.printStackTrace();
        }
        return info != null;
    }


    // 从下载文件夹获取Apk
    private static ArrayList<ApkItem> myGetApk(Context context, String path) {
//        File files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File files = new File(path);
        PackageManager pm = context.getPackageManager();
        ArrayList<ApkItem> apkItems = new ArrayList<>();
        for (File file : files.listFiles()) {
            if (file.exists() && file.getPath().toLowerCase().endsWith(".apk")) {
                final PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), 0);
                apkItems.add(new ApkItem(pm, info, file.getPath()));
            }
        }
        return apkItems;
    }

    /**
     * 异步从下载文件夹获取Apk
     * @author bian
     * @param context; callBack:处理从下载文件夹获取Apk获取的 ApkItemList 的回调
     * @return void
     * created at 2016/4/22 14:01
     */
    public static void getApk(final Context context, final String path, final GetApkCallBack callBack){
        new AsyncTask<Void, Void, ArrayList<ApkItem>>() {

            @Override
            protected ArrayList<ApkItem> doInBackground(Void... params) {
                return myGetApk(context, path);
            }

            @Override
            protected void onPostExecute(ArrayList<ApkItem> apkItems) {
                callBack.getApkCallBack(apkItems);
            }
        }.execute();
    }
}
