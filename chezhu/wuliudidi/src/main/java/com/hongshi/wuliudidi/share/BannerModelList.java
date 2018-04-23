package com.hongshi.wuliudidi.share;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.BannerModel;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 * @version 1.0
 * @created 2017/11/29 14:31
 * @title BannerModelList
 * @description 保存广告数据
 * @changeRecord：2017/11/29 14:31 modify by
 */
public class BannerModelList {
    private static String advertListUrl = "http://gwapp.redlion56.com/gwapp/app/advert/findAdvertList.do?";
    //private String advertListUrl = "http://192.168.158.205:8080/gwapp/app/advert/findAdvertList.do?";

    public static List<BannerModel> homeBannerList = new ArrayList<>();
    public static List<BannerModel> myWalletBannerList = new ArrayList<>();
    public static List<BannerModel> goodsListBannerList = new ArrayList<>();

    public static void getAdvertList(final Context context){
        AjaxParams params = new AjaxParams();
        params.put("appType","2");
        DidiApp.getHttpManager().sessionPost(context, advertListUrl, params, new ChildAfinalHttpCallBack() {

            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    JSONObject body = jsonObject.getJSONObject("body");

                    String homeBannerArray = body.optString("AD_POSITION");
                    String myWalletBannerArray = body.optString("AD_WALLET");
                    String goodsListBannerArray = body.optString("AD_GOODSLIST");

                    List<BannerModel> homeBannerList = JSON.parseArray(homeBannerArray,BannerModel.class);
                    List<BannerModel> myWalletBannerList = JSON.parseArray(myWalletBannerArray,BannerModel.class);
                    List<BannerModel> goodsListBannerList = JSON.parseArray(goodsListBannerArray,BannerModel.class);

                    BannerModelList.homeBannerList = homeBannerList;
                    BannerModelList.myWalletBannerList = myWalletBannerList;
                    BannerModelList.goodsListBannerList = goodsListBannerList;

                    Intent intent = new Intent();
                    intent.setAction("get_banner_list_success");
                    context.sendBroadcast(intent);

                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }

}
