package com.hongshi.wuliudidi.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.MainFragmentPagerAdapter;
import com.hongshi.wuliudidi.camera.ActivityCapture;
import com.hongshi.wuliudidi.camera.PhotoPreviewActivity;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.fragment.GoodsFragmentForDriver;
import com.hongshi.wuliudidi.fragment.HomeFragmentForDriver;
import com.hongshi.wuliudidi.fragment.MessageFragment;
import com.hongshi.wuliudidi.fragment.TaskModifyFragment;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.InviteModel;
import com.hongshi.wuliudidi.model.MyUserAppVO;
import com.hongshi.wuliudidi.model.RefreshMessage;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.photo.GetPhotoUtil;
import com.hongshi.wuliudidi.plugin.Util.NetworkUtils;
import com.hongshi.wuliudidi.plugin.Util.PluginUtil;
import com.hongshi.wuliudidi.plugin.bean.PluginParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.JpushUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huiyuan
 * Created by huiyuan on 2017/3/14.
 */

public class DriverMainActivity extends FragmentActivity {

    public static LinearLayout driver_bottom_container;
    private ImageView mGoodsImage, mTaskImage,mMessageImage,mHomeImage;
    private LinearLayout mGoodsLayout, mTaskLayout,mMessageLayout;
    private TextView mGoodsText, mTaskText,mMessageText,mHomeText;
    public static ViewPager mPager;
    private ArrayList<Fragment> mFragmentList;
    private long mkeyTime;
    private String message_url = GloableParams.COMET_HOST + "comet.do?";
    private boolean isComet = true;
    private RelativeLayout mTaskNewsLayout,mMessageNewsLayout,driver_main_home_page;
    private TextView mTaskNews,mMessageNews;
    private SharedPreferences sharedPreferences;
    private String sessionID;
    private int auction_message = 0;
    private int friendShip_message =0;
    private int system_message = 0;
    private int transit_message = 0;
    //任务总数
    private int task_message = 0;
    //消息总数
    private int message = 0;
    private RefreshMessage mList;

    //监控线程的广播
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CommonRes.CometStopForDriver)) {
                mTaskNewsLayout.setVisibility(View.GONE);
                task_message = 0;
                mMessageNewsLayout.setVisibility(View.GONE);
                DidiApp.sessionId = "";
                message = 0;
                isComet = false;
                if(DidiApp.mHttpURLConnection!=null){
//					DidiApp.mHttpURLConnection.disconnect();
                    try {
                        DidiApp.mHttpURLConnection.disconnect();
                        DidiApp.mHttpURLConnection.getInputStream().close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(action.equals(CommonRes.CometStartForDriver)){
                sessionID = sharedPreferences.getString("session_id", "");
                DidiApp.sessionId = sessionID;
                isComet = true;
//				updateMessage();
                if(Util.isLogin(DriverMainActivity.this)){
                    startThread();
                }
            }else if(action.equals(CommonRes.ClickSystemMessage)){
                int minus_system_number = intent.getIntExtra("system_number", 0);
                message = message - minus_system_number;
                if(message!=0&& message >0){
                    mMessageNews.setText(""+message);
                }else{
                    mMessageNewsLayout.setVisibility(View.GONE);
                    message = 0;
                }
            }else if(action.equals(CommonRes.ClickAuctionMessage)){
                //消息fragment点击了竞价消息
                int minus_auction_number = intent.getIntExtra("auction_number", 0);
                message = message - minus_auction_number;
                if(message!=0&& message >0){
                    mMessageNews.setText(""+message);
                }else{
                    mMessageNewsLayout.setVisibility(View.GONE);
                    message = 0;
                }
            }else if(action.equals(CommonRes.ClickInviteMessage)){
                //消息fragment点击了车队邀请消息
                int minus_invite_number = intent.getIntExtra("invite_number", 0);
                message = message - minus_invite_number;
                if(message!=0 && message >0){
                    mMessageNews.setText(""+message);
                }else{
                    mMessageNewsLayout.setVisibility(View.GONE);
                    message = 0;
                }
            }else if(action.equals(CommonRes.ClickTransitMessage)){
                int transit_number = intent.getIntExtra("transit_number", 0);
                task_message = task_message - transit_number;
                if(task_message>0){
                    mTaskNews.setText(""+task_message);
                }else{
                    mTaskNewsLayout.setVisibility(View.GONE);
                    task_message = 0;
                }
            }else if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                if(NetworkUtils.isNetworkConnectedOnHighSpeed(DriverMainActivity.this)){
                    PluginUtil.checkPluginUpdate(getApplicationContext(), PluginParams.CHENGYUNTONG, PluginParams.APPTYPE);
                }
            }else if(CommonRes.CZ_INDEX.equals(action)){
                // 设置当前显示标签页为第一页
                mPager.setCurrentItem(0);
                setText(0);
                driver_bottom_container.setVisibility(View.GONE);
            }
        }
    };

    @SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CommonRes.NEWMESSAGE:
                    mMessageNewsLayout.setVisibility(View.VISIBLE);
                    mMessageNews.setText(""+message);
                    break;
                case CommonRes.TASKMESSAGE:
                    mTaskNewsLayout.setVisibility(View.VISIBLE);
                    mTaskNews.setText(""+task_message);
                    break;
                case CommonRes.SYSTEMMESSAGE:
                    Intent system_intent = new Intent();
                    system_intent.putExtra("system_number", system_message);
                    system_intent.setAction(CommonRes.MessageFromSystem);
                    system_intent.putExtra("system", mList);
                    sendBroadcast(system_intent);
                    break;
                case CommonRes.AUCTIONMESSAGE:
                    Intent auction_intent = new Intent();
                    auction_intent.putExtra("auction_number", auction_message);
                    auction_intent.setAction(CommonRes.MessageFromAuction);
                    auction_intent.putExtra("auction", mList);
                    sendBroadcast(auction_intent);
                    break;
                case CommonRes.INVITEMESSAGE:
                    Intent invite_intent = new Intent();
                    invite_intent.putExtra("friend_number", friendShip_message);
                    invite_intent.putExtra("invite", mList);
                    invite_intent.setAction(CommonRes.MessageFromInvite);
                    sendBroadcast(invite_intent);
                    break;
                case CommonRes.TRANSITMESSAGE:
                    Intent transit_intent = new Intent();
                    transit_intent.putExtra("transit_number", transit_message);
                    transit_intent.setAction(CommonRes.MessageFromTransit);
                    sendBroadcast(transit_intent);
                    break;
                case CommonRes.STOPTHREAD:
                    isComet = false;
                    if(DidiApp.mHttpURLConnection!=null ){
                        try {
                            DidiApp.mHttpURLConnection.getInputStream().close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CommonRes.CAMERA:
                    UploadUtil.camera(DriverMainActivity.this,mHandler);
                    break;
                case CommonRes.GALLERY:
                    UploadUtil.photo(DriverMainActivity.this,mHandler);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_for_driver);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        driver_bottom_container = (LinearLayout) findViewById(R.id.driver_bottom_container);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        sessionID = sharedPreferences.getString("session_id", "");
        DidiApp.sessionId = sessionID;
        initView();
        //初始化Jpush
        JpushUtil mJpushUtil = new JpushUtil();
        mJpushUtil.initJpush(getApplicationContext(), "", CommonRes.UserId);
        if(Util.isLogin(DriverMainActivity.this)){
            startThread();
        }

        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonRes.CometStopForDriver);
        intentFilter.addAction(CommonRes.CometStartForDriver);
        intentFilter.addAction(CommonRes.ClickSystemMessage);
        intentFilter.addAction(CommonRes.ClickAuctionMessage);
        intentFilter.addAction(CommonRes.ClickInviteMessage);
        intentFilter.addAction(CommonRes.ClickTransitMessage);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(CommonRes.CZ_INDEX);
//		intentFilter.addAction("action_from_plan");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        if(NetworkUtils.isNetworkConnectedOnHighSpeed(DriverMainActivity.this)){
            PluginUtil.checkPluginUpdate(getApplicationContext(), PluginParams.CHENGYUNTONG, PluginParams.APPTYPE);
        }
        ActivityManager.getInstance().addActivity(this);
    }

    private void startThread(){
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isComet) {
                    try {
                        if(!Util.isLogin(DriverMainActivity.this)){
                            if(DidiApp.mHttpURLConnection!=null){
                                try {
                                    DidiApp.mHttpURLConnection.getInputStream().close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                        String string = DidiApp.getHttpManager().post(message_url);
                        if(null != string){
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(string);
                                boolean isSuccess = jsonObject.getBoolean("success");
                                if(isSuccess){
                                    String body = jsonObject.getString("body");
                                    mList = JSON.parseObject(body,RefreshMessage.class);
                                    updateMessage();
                                }else{
                                    String errCode = jsonObject.getString("errCode");
                                    if(errCode != null && errCode.equals("080003")){
                                        //未登录
                                        SharedPreferences.Editor edit = sharedPreferences.edit();
//                                        edit.putString("cellphone", "");
                                        edit.putString("userId", "");
                                        edit.putString("session_id", "");
                                        edit.commit();
                                        mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
                                    }
                                }
                            } catch (JSONException e) {
                                mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
                                e.printStackTrace();
                            }
                        }else{
                            mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
                        }
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(CommonRes.STOPTHREAD);
                        e.printStackTrace();
                    }
                }
            }
        });
        if(isComet){
            thread.start();
        }else{
            if(thread.isInterrupted()){
                thread.interrupt();
            }
        }
    }
    private void updateMessage(){
        List<InviteModel> auctionList = mList.getREFRESH_MSG_AUCTION();
        List<InviteModel> friendShipList = mList.getREFRESH_MSG_FRIENDSHIP();
        List<InviteModel> systemList = mList.getREFRESH_MSG_SYSTEM();
        List<InviteModel> transitList = mList.getREFRESH_TASK_TRANSIT();
        if (auctionList != null) {
            auction_message = auctionList.size();
            if(message != 0){
                message = message + auction_message;
            }else{
                message = auction_message;
            }
            mHandler.sendEmptyMessage(CommonRes.NEWMESSAGE);
            mHandler.sendEmptyMessage(CommonRes.AUCTIONMESSAGE);

        } else if (friendShipList != null) {
            friendShip_message = friendShipList.size();
            if(message != 0){
                message = message + friendShip_message;
            }else{
                message = friendShip_message;
            }
            mHandler.sendEmptyMessage(CommonRes.NEWMESSAGE);
            mHandler.sendEmptyMessage(CommonRes.INVITEMESSAGE);
        } else if (systemList != null) {
            system_message = systemList.size();
            if(message != 0){
                message = message + system_message;
            }else{
                message = system_message;
            }
            mHandler.sendEmptyMessage(CommonRes.NEWMESSAGE);
            mHandler.sendEmptyMessage(CommonRes.SYSTEMMESSAGE);
        } else if (transitList != null) {
            transit_message = transitList.size();
            if(task_message != 0){
                task_message = task_message + transit_message;
            }else{
                task_message = transit_message;
            }
            //添加消息到
            for(int i=0;i<transit_message;i++){
                CommonRes.taskIdList.add(transitList.get(i).getParams().get(0));
            }
            //更新主界面的消息
            mHandler.sendEmptyMessage(CommonRes.TASKMESSAGE);
            //更新子界面的消息
            mHandler.sendEmptyMessage(CommonRes.TRANSITMESSAGE);
        }
    }
    private void initView() {
        initTextView();
    }
    private void initTextView() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mGoodsImage = (ImageView) findViewById(R.id.goods_image);
        mTaskImage = (ImageView) findViewById(R.id.task_image);
        mMessageImage = (ImageView) findViewById(R.id.message_image);
        mHomeImage = (ImageView) findViewById(R.id.home_image);

        driver_main_home_page = (RelativeLayout) findViewById(R.id.driver_main_home_page);
        mGoodsLayout = (LinearLayout) findViewById(R.id.goods_layout);
        mTaskLayout = (LinearLayout) findViewById(R.id.task_layout);
        mMessageLayout = (LinearLayout) findViewById(R.id.message_layout);

        mGoodsText = (TextView) findViewById(R.id.goods_text);
        mTaskText = (TextView) findViewById(R.id.task_text);
        mMessageText = (TextView) findViewById(R.id.message_text);
        mHomeText = (TextView) findViewById(R.id.home_text);
        //显示消息
        mTaskNewsLayout = (RelativeLayout) findViewById(R.id.task_news_layout);
        mMessageNewsLayout = (RelativeLayout) findViewById(R.id.message_news_layout);
        mTaskNews = (TextView) findViewById(R.id.task_news);
        mMessageNews = (TextView) findViewById(R.id.message_news);

        mFragmentList = new ArrayList<>();
        Fragment mGoodsFragment = new GoodsFragmentForDriver();
        Fragment mTaskFragment = new TaskModifyFragment();
        Fragment mMessageFragment = new MessageFragment();
        Fragment mHomeFragment = new HomeFragmentForDriver();

        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mGoodsFragment);
        mFragmentList.add(mTaskFragment);
        mFragmentList.add(mMessageFragment);

        mPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));

        String push = null;
        Bundle bundle = null;
        bundle = getIntent().getExtras();
        if (bundle != null) {
            push = bundle.getString("from");
        }
        if(push != null && push.equals("jpush")){
            //任务页面
            mPager.setCurrentItem(2);
            setText(2);
        }else {
            // 设置当前显示标签页为第一页
            mPager.setCurrentItem(0);
            setText(0);
            driver_bottom_container.setVisibility(View.GONE);
        }

        mPager.setOffscreenPageLimit(4);
        // 页面变化时的监听器
        mPager.setOnPageChangeListener(new DriverMainActivity.MyOnPageChangeListener());

        mGoodsLayout.setOnClickListener(new DriverMainActivity.TextListener(1));
        mTaskLayout.setOnClickListener(new DriverMainActivity.TextListener(2));
        driver_main_home_page.setOnClickListener(new DriverMainActivity.TextListener(0));
        mMessageLayout.setOnClickListener(new DriverMainActivity.TextListener(3));
    }
    public void setText(int pos) {
        switch (pos) {
            case 1:
                mGoodsImage.setImageResource(R.drawable.goods_press);
                mGoodsText.setTextColor(getResources().getColor(R.color.home_text_press));

                mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));

                mHomeImage.setImageResource(R.drawable.driver_home_img);
                mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));

                mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
                mMessageImage.setImageResource(R.drawable.news_none);

                mTaskImage.setImageResource(R.drawable.task_none);
                break;
            case 0:
                mGoodsImage.setImageResource(R.drawable.goods_none);
                mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));

                mHomeImage.setImageResource(R.drawable.driver_home_page_press_icon);
                mHomeText.setTextColor(getResources().getColor(R.color.home_text_press));

                mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));

                mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
                mMessageImage.setImageResource(R.drawable.news_none);

                mTaskImage.setImageResource(R.drawable.task_none);
                break;
            case 2:
                mGoodsImage.setImageResource(R.drawable.goods_none);
                mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));

                mTaskText.setTextColor(getResources().getColor(R.color.home_text_press));

                mHomeImage.setImageResource(R.drawable.driver_home_img);
                mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));

                mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
                mMessageImage.setImageResource(R.drawable.news_none);

                mTaskImage.setImageResource(R.drawable.task_press);
                break;
            case 3:
                mGoodsImage.setImageResource(R.drawable.goods_none);
                mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));

                mTaskImage.setImageResource(R.drawable.task_none);
                mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));

                mHomeImage.setImageResource(R.drawable.driver_home_img);
                mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));

                mMessageText.setTextColor(getResources().getColor(R.color.home_text_press));
                mMessageImage.setImageResource(R.drawable.news_press);

                break;
            case 4:
                mGoodsImage.setImageResource(R.drawable.goods_none);
                mGoodsText.setTextColor(getResources().getColor(R.color.home_text_none));

                mTaskText.setTextColor(getResources().getColor(R.color.home_text_none));

                mHomeImage.setImageResource(R.drawable.driver_home_img);
                mHomeText.setTextColor(getResources().getColor(R.color.home_text_none));

                mMessageText.setTextColor(getResources().getColor(R.color.home_text_none));
                mMessageImage.setImageResource(R.drawable.news_none);

                mTaskImage.setImageResource(R.drawable.task_none);
                break;

            default:
                break;
        }
    }
    public class TextListener implements View.OnClickListener {
        private int index = 0;

        public TextListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
            driver_bottom_container.setVisibility(View.VISIBLE);
            if (mPager != null){
                mPager.setCurrentItem(index);
                if(index == 3){
                    Intent intent = new Intent();
                    intent.setAction(CommonRes.GetALlMessage);
                    sendBroadcast(intent);
                }else if(index == 4){
//					Intent intent = new Intent();
//					intent.setAction(CommonRes.RefreshUserInfo);
//					sendBroadcast(intent);
                }else if(index == 1){
                    Intent intent = new Intent();
                    intent.setAction(CommonRes.RefreshGoodsList);
                    sendBroadcast(intent);
                }else if(index == 2){
                    Intent intent = new Intent();
                    intent.setAction(CommonRes.MessageFromTransit);
                    sendBroadcast(intent);
                    mTaskNewsLayout.setVisibility(View.GONE);
                }
                setText(index);
            }
        }
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageSelected(int arg0) {
            if(arg0 == 0){
                Intent intent = new Intent();
                intent.setAction(CommonRes.GetRecGoodsSourceAndFinishOrder);
                sendBroadcast(intent);
                driver_bottom_container.setVisibility(View.GONE);
            }else {driver_bottom_container.setVisibility(View.VISIBLE);}
            if(arg0 == 2){
                Intent intent2 = new Intent();
                intent2.setAction(CommonRes.DriverTask);
                sendBroadcast(intent2);
                mTaskNewsLayout.setVisibility(View.GONE);
            }
            setText(arg0);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == UploadUtil.PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                String path = ImageUtil.getImageAbsolutePath(DriverMainActivity.this, uri);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                intent.putExtra("isCamera", false);
                intent.setAction(CommonRes.UploadPhoto);
                sendBroadcast(intent);
            }
        }else if (requestCode == UploadUtil.PHOTO_REQUEST_CAMERA &&  resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra("isCamera", true);
            intent.setAction(CommonRes.UploadPhoto);
            sendBroadcast(intent);
        }else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (data != null) {
                // 得到图片的全路径
                String path = data.getStringExtra(ActivityCapture.kPhotoPath);
                int amount = data.getIntExtra("amount",0);
                PhotoPreviewActivity.open(this, path, CommonRes.TYPE_UPLOAD_HUIDAN,amount);
            }
        }else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_GALLERY &&  resultCode == RESULT_OK) {
            if (UploadUtil.hasSdcard()) {
                Uri uri = data.getData();
                GetPhotoUtil.crop(DriverMainActivity.this, uri,CommonRes.tempFile);
            }
        }else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CUT && resultCode == RESULT_OK){
            String path = ImageUtil.getImageAbsolutePath(DriverMainActivity.this, Uri.fromFile(CommonRes.tempFile));
            Intent intent = new Intent();
            intent.putExtra("imagePath", path);
            intent.putExtra("tag", CommonRes.TYPE_UPLOAD_HUIDAN);
            int amount = data.getIntExtra("amount",0);
            intent.putExtra("amount",amount);
            intent.setAction(CommonRes.ACTIONPHOTOPATH);
            sendBroadcast(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                mkeyTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            } else {
                if(DidiApp.list.size()>0){
                    String json = JSON.toJSONString(DidiApp.list);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("addressInfo", json);
                    edit.commit();
                }
                ActivityManager.getInstance().exit();
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//		if(false){
//			sharedPreferences.edit().putBoolean("start_time",true);
//			Intent intent = new Intent(MainActivity.this,AdvertisementActivity.class);
//			intent.putExtra("url","www.baidu.com");
//			intent.putExtra("title","xxx活动详情");
//			startActivity(intent);
//		}
    }
}
