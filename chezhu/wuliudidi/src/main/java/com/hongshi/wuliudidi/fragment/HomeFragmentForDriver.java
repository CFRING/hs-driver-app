package com.hongshi.wuliudidi.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AuctionDetailsActivity;
import com.hongshi.wuliudidi.activity.AuthActivity;
import com.hongshi.wuliudidi.activity.ChooseDriverActivity;
import com.hongshi.wuliudidi.activity.DriverMainActivity;
import com.hongshi.wuliudidi.activity.EmptyTruckCommitActivity;
import com.hongshi.wuliudidi.activity.IncomeBookActivity;
import com.hongshi.wuliudidi.activity.LoginActivity;
import com.hongshi.wuliudidi.activity.MainActivity;
import com.hongshi.wuliudidi.activity.MyIntegralActivity;
import com.hongshi.wuliudidi.activity.PersonInfoActivity;
import com.hongshi.wuliudidi.activity.SettingsActivity;
import com.hongshi.wuliudidi.activity.TruckInfoActivity;
import com.hongshi.wuliudidi.activity.TruckInfoForDriverActivity;
import com.hongshi.wuliudidi.activity.WebViewWithTitleActivity;
import com.hongshi.wuliudidi.adapter.BannerAdapter;
import com.hongshi.wuliudidi.adapter.HomeImageAdapter;
import com.hongshi.wuliudidi.adapter.RecgGoodsAdapter;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.AreaQueryVo;
import com.hongshi.wuliudidi.model.AuctionQueryModel;
import com.hongshi.wuliudidi.model.CityListModel;
import com.hongshi.wuliudidi.model.GoodsTypes;
import com.hongshi.wuliudidi.model.MyUserAppVO;
import com.hongshi.wuliudidi.model.RecgGoodsModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.plugin.Util.PluginUtil;
import com.hongshi.wuliudidi.plugin.bean.PluginParams;
import com.hongshi.wuliudidi.qr.ConfirmGoodsActivity;
import com.hongshi.wuliudidi.share.BannerModelList;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.CircleFlowIndicator;
import com.hongshi.wuliudidi.view.CircleImageView;
import com.hongshi.wuliudidi.view.MenuItemView;
import com.hongshi.wuliudidi.view.MyViewFlow;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Created by huiyuan on 2017/3/22.
 */

public class HomeFragmentForDriver extends Fragment implements View.OnClickListener{

    private CircleImageView driver_user_head,menu_user_head;
    private FinalBitmap mFinalBitmap;
    private FrameLayout frameLayout;
    private MyViewFlow view_flow;
    private CircleFlowIndicator indic;
    private BannerAdapter bannerAdapter;
    private TextView share_tip,address_text,goods_text,task_count;
//    private static final int[] ids = { R.drawable.home_one,
//            R.drawable.home_two, R.drawable.home_three};
    private ImageView driver_share_img,right_arrow_image,news_dot,home_page_icon,
            choose_address,choose_goods,guide_img,empty_truck_commit;
    private RelativeLayout driver_my_task_container,driver_take_task_container,driver_share_container,guide_container;
    private SlidingMenu menu;
    private RelativeLayout mLeftLayout;
    private String user_face_url = "";
    private String user_name,user_phone;
    private TextView mUserName,mUserPhone;
    private SharedPreferences sharedPreferences;
    private String user_url = GloableParams.HOST + "uic/user/getUserInfo.do?";
    /**
     * 上传用户头像
     */
    private String upload_url = GloableParams.HOST + "uic/user/uploadUserFace.do?";
    private DataFillingDialog mImageDialog;
//    private PullToRefreshListView mPullToRefreshListView;
    private ListView mGoddsListview;
    private RecgGoodsAdapter recgGoodsAdapter;
    private GridView gridView;
    private MyAdapter addrAdapter;
    private RelativeLayout gridView_container;
    private String latitude = "0";
    private String longitude = "0";
    private boolean isRecGoodsFileted = false;
    private Activity activity;
    private ScrollView scroll_view;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonRes.CAMERA:
                    UploadUtil.camera(getActivity(),mHandler);
                    break;
                case CommonRes.GALLERY:
                    UploadUtil.photo(getActivity(),mHandler);
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver driverHomeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(CommonRes.RefreshData.equals(action)){
                if(mGoodsTypesList.size() == 0){
                    getGoodsTypes();
                }

                if(mProvinceList.size() == 0){
                    getHotCity();
                }

                if(Util.isLogin(activity)){
                    getTaskCount();
                    if(mProvinceList != null && mProvinceList.size() > 0){
                        address_text.setText("提货地: " + mProvinceList.get(selectedPosition).getName());
                        getRecGoods();
                    }
                }else {task_count.setVisibility(View.GONE);}
            }else if(CommonRes.NewMessage.equals(action)){
                news_dot.setVisibility(View.VISIBLE);
            }else if(CommonRes.GetRecGoodsSourceAndFinishOrder.equals(action)){
                gridView_container.setVisibility(View.GONE);
                scroll_view.fullScroll(ScrollView.FOCUS_UP);
                if(mProvinceList.size() == 0){
                    getHotCity();
                }
                if(mGoodsTypesList.size() == 0){
                    getGoodsTypes();
                }
                    getRecGoods();
                if(Util.isLogin(activity)){
                    getTaskCount();
                }
            }else if(action.equals(CommonRes.UploadPhoto)){
                if(intent.getBooleanExtra("isCamera", false)){
                    //相机
                    if (UploadUtil.hasSdcard()) {
                        if(UploadUtil.tempFile == null){
                            UploadUtil.tempFile = new File(Environment.getExternalStorageDirectory(),UploadUtil.PHOTO_FILE_NAME);
                        }
                        uploadFile(upload_url, ImageUtil.getimage(ImageUtil.getImageAbsolutePath(getActivity(), Uri.fromFile(UploadUtil.tempFile))));
                        UploadUtil.tempFile = null;
                    }
                }else{
                    String path = intent.getStringExtra("path");
                    uploadFile(upload_url,ImageUtil.getimage(path));
                }
            }else if(CommonRes.RefreshUserInfo.equals(action)){

                menu.showContent();
                if(Util.isLogin(activity)){
                    empty_truck_commit.setVisibility(View.VISIBLE);
                    getDriverInfo();
                }else {
                    empty_truck_commit.setVisibility(View.GONE);
                    mUserName.setText("");
                    mUserPhone.setText("");
                    driver_user_head.setImageResource(R.drawable.default_photo);
                    menu_user_head.setImageResource(R.drawable.default_photo);
                }
            }else if("has_common_line".equals(action)){
                if(Util.isLogin(activity)){
                    getDriverInfo();
                    getRecGoods();
                }
            }else if("delete_route_success".equals(action)){
                if(Util.isLogin(activity)){
                    getDriverInfo();
                    getRecGoods();
                }
            }else if("get_banner_list_success".equals(action)){
                setBannerView();
            }else if("push_goods_from_server".equals(action)){
                getRecGoods();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.driver_home_page, null);

        activity = getActivity();
        sharedPreferences = activity.getSharedPreferences("config",Context.MODE_PRIVATE);
        mFinalBitmap = FinalBitmap.create(activity);

        //初始化得到用户信息
        user_face_url = activity.getSharedPreferences("config",Context.MODE_PRIVATE).getString("userFace","");
        user_name = activity.getSharedPreferences("config",Context.MODE_PRIVATE).getString("name","");
        user_phone = activity.getSharedPreferences("config",Context.MODE_PRIVATE).getString("cellphone","");

        scroll_view = (ScrollView) mView.findViewById(R.id.scroll_view);
        driver_user_head = (CircleImageView) mView.findViewById(R.id.driver_user_head);
        view_flow = (MyViewFlow) mView.findViewById(R.id.view_flow);

        task_count = (TextView) mView.findViewById(R.id.task_count);
        goods_text = (TextView) mView.findViewById(R.id.goods_text);
        address_text = (TextView) mView.findViewById(R.id.address_text);
        share_tip = (TextView) mView.findViewById(R.id.share_tip);
        choose_goods = (ImageView) mView.findViewById(R.id.choose_goods);
        choose_address = (ImageView) mView.findViewById(R.id.choose_address);
        home_page_icon = (ImageView) mView.findViewById(R.id.home_page_icon);
        news_dot = (ImageView) mView.findViewById(R.id.news_dot);
        driver_share_img = (ImageView) mView.findViewById(R.id.driver_share_img);
        guide_img = (ImageView) mView.findViewById(R.id.guide_img);
        guide_container = (RelativeLayout) mView.findViewById(R.id.guide_container);
        driver_my_task_container = (RelativeLayout) mView.findViewById(R.id.driver_my_task_container);
        driver_take_task_container = (RelativeLayout) mView.findViewById(R.id.driver_take_task_container);
        driver_share_container = (RelativeLayout) mView.findViewById(R.id.driver_share_container);
        gridView = (GridView) mView.findViewById(R.id.gridView);
        gridView_container = (RelativeLayout) mView.findViewById(R.id.gridView_container);
        frameLayout = (FrameLayout) mView.findViewById(R.id.framelayout);
        empty_truck_commit = (ImageView) mView.findViewById(R.id.empty_truck_commit);
        indic = (CircleFlowIndicator) mView.findViewById(R.id.viewflowindic);

        guide_container.setOnClickListener(this);
        goods_text.setOnClickListener(this);
        choose_goods.setOnClickListener(this);
        address_text.setOnClickListener(this);
        choose_address.setOnClickListener(this);
        home_page_icon.setOnClickListener(this);
        driver_user_head.setOnClickListener(this);
        driver_my_task_container.setOnClickListener(this);
        driver_take_task_container.setOnClickListener(this);
        driver_share_container.setOnClickListener(this);
        gridView_container.setOnClickListener(this);
        empty_truck_commit.setOnClickListener(this);

        setBannerView();

        if(!DidiApp.isUserAowner){
            driver_share_img.setImageResource(R.drawable.driver_contract_customer);
            share_tip.setText(R.string.my_contact);
        }

        // 拍照,0 表示拍照或者图库选取
        mImageDialog = new DataFillingDialog(activity, R.style.data_filling_dialog, mHandler, 0,-1);
        mImageDialog.setCanceledOnTouchOutside(true);
        mImageDialog.setText("拍照", "图库选取");
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonRes.GetRecGoodsSourceAndFinishOrder);
        filter.addAction(CommonRes.RefreshData);
        filter.addAction(CommonRes.UploadPhoto);
        filter.addAction(CommonRes.RefreshUserInfo);
        filter.addAction("has_common_line");
        filter.addAction("delete_route_success");
        filter.addAction("get_banner_list_success");
        filter.addAction("push_goods_from_server");
        activity.registerReceiver(driverHomeReceiver,filter);
        initSildingmenu();

        mGoddsListview = (ListView) mView.findViewById(R.id.goods_list);
//        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                if (mPullToRefreshListView.isRefreshing()) {
//                    if (mPullToRefreshListView.isHeaderShown()){
////                        currentPage = 1;
//                        getRecGoods();
//                    } else if (mPullToRefreshListView.isFooterShown()) {
//                        // 加载更多
////                        if(isEnd){
//                            Toast.makeText(activity, "已经是最后一页", Toast.LENGTH_SHORT).show();
//                            CloseRefreshTask closeRefreshTask = new CloseRefreshTask(mPullToRefreshListView);
//                            closeRefreshTask.execute();
//                            return;
////                        }
////                        currentPage = currentPage + 1;
////                        getRecGoods(false);
//                    }
//                }
//            }
//        });

        mGoddsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecgGoodsModel model = recommendAucVOList.get(position);
                if(Util.isLogin(activity)){
                        Intent intent = new Intent(activity, AuctionDetailsActivity.class);
                        intent.putExtra("auctionId", model.getAuctionId());
                        activity.startActivity(intent);
                    }else {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                    }
            }
        });

        latitude = sharedPreferences.getString("latitude", "");
        longitude = sharedPreferences.getString("longitude", "");

        if(Util.isLogin(activity)){
            getDriverInfo();
            getTaskCount();
            empty_truck_commit.setVisibility(View.VISIBLE);
        }else {
            empty_truck_commit.setVisibility(View.GONE);
        }

        getRecGoods();
        getHotCity();
        getGoodsTypes();

        scroll_view.smoothScrollTo(0, 0);
        return mView;
    }
    private void initSildingmenu() {
        menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.width_320);
        menu.setShadowDrawable(null);
        // SlidingMenu划出时主页面显示的剩余宽度
        // menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		menu.setBehindWidth(300);// 设置SlidingMenu菜单的宽度
        // 设置菜单占屏幕的比例
        menu.setBehindOffset(activity.getWindowManager().getDefaultDisplay().getWidth() / 3);
        // 设置渐入渐出效果的值
        menu.setFadeEnabled(true);
        menu.setFadeDegree(0.35f);
        // 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        // 为侧滑菜单设置布局
        menu.setMenu(R.layout.leftmenu);
        mLeftLayout = (RelativeLayout) activity.findViewById(R.id.left_layout);
        menu_user_head = (CircleImageView) activity.findViewById(R.id.photo);
        mUserName = (TextView) activity.findViewById(R.id.user_name);
        mUserPhone = (TextView) activity.findViewById(R.id.user_phone);
        if(Util.isLogin(activity)){
            mUserName.setText(user_name);
            mUserPhone.setText(user_phone);
            if(user_face_url != null && !user_face_url.equals("")){
                mFinalBitmap.display(menu_user_head, user_face_url);
                mFinalBitmap.display(driver_user_head, user_face_url);
            }else{
                driver_user_head.setImageResource(R.drawable.default_photo);
                menu_user_head.setImageResource(R.drawable.default_photo);
            }
        }

        MenuItemView name_auth = (MenuItemView) activity.findViewById(R.id.name_auth);
        MenuItemView licence_auth = (MenuItemView)activity.findViewById(R.id.licence_auth);
        MenuItemView my_route = (MenuItemView) activity.findViewById(R.id.my_route);
        MenuItemView my_trucks = (MenuItemView) activity.findViewById(R.id.my_trucks);
        MenuItemView my_drivers = (MenuItemView) activity.findViewById(R.id.my_drivers);
        MenuItemView my_integral = (MenuItemView) activity.findViewById(R.id.my_integral);
        MenuItemView income = (MenuItemView) activity.findViewById(R.id.income);
        MenuItemView scan_entry = (MenuItemView) activity.findViewById(R.id.scan_entry);
        MenuItemView settings = (MenuItemView)activity. findViewById(R.id.settings);
        MenuItemView contacts = (MenuItemView) activity.findViewById(R.id.contacts);
        MenuItemView truck_mall = (MenuItemView) activity.findViewById(R.id.truck_mall);
        right_arrow_image = (ImageView) activity.findViewById(R.id.right_arrow_image);
        name_auth.setItemIcon(R.drawable.real_name);
        name_auth.setItemName("实名认证");
        licence_auth.setItemIcon(R.drawable.driving_license_menu);
        licence_auth.setItemName("驾照认证");
        my_route.setItemIcon(R.drawable.line);
        my_route.setItemName("我的路线");
        my_trucks.setItemIcon(R.drawable.vehicle);
        my_trucks.setItemName("我的车辆");
        my_drivers.setItemIcon(R.drawable.driver);
        my_drivers.setItemName("我的司机");
        income.setItemIcon(R.drawable.income);
        my_integral.setItemName("我的积分");
        my_integral.setItemIcon(R.drawable.integral);
        income.setItemName("运费结算");
        scan_entry.setItemIcon(R.drawable.scanning_driver);
        scan_entry.setItemName("扫一扫");
        settings.setItemIcon(R.drawable.set_up);
        settings.setItemName("设置");
        contacts.setItemIcon(R.drawable.service);
        contacts.setItemName("联系客服");
        truck_mall.setItemIcon(R.drawable.shopping);
        truck_mall.setItemName("汽车商城");
        name_auth.setOnClickListener(this);
        licence_auth.setOnClickListener(this);
        my_route.setOnClickListener(this);
        my_trucks.setOnClickListener(this);
        my_drivers.setOnClickListener(this);
        my_integral.setOnClickListener(this);
        income.setOnClickListener(this);
        scan_entry.setOnClickListener(this);
        settings.setOnClickListener(this);
        contacts.setOnClickListener(this);
        truck_mall.setOnClickListener(this);
        mLeftLayout.setOnClickListener(this);
        menu_user_head.setOnClickListener(this);
        right_arrow_image.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        mUserPhone.setOnClickListener(this);
        if(!DidiApp.isUserAowner){
            right_arrow_image.setVisibility(View.VISIBLE);
            my_drivers.setVisibility(View.GONE);
            income.setVisibility(View.GONE);
            contacts.setVisibility(View.GONE);
            my_integral.setVisibility(View.GONE);
        }else {
            right_arrow_image.setVisibility(View.GONE);
            my_drivers.setVisibility(View.VISIBLE);
            income.setVisibility(View.VISIBLE);
            contacts.setVisibility(View.VISIBLE);
            my_integral.setVisibility(View.VISIBLE);
        }

    }

    private void setBannerView(){
        WindowManager windowManager = activity.getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width,2*width/5);
        layoutParams.topMargin = 14;
        layoutParams.addRule(RelativeLayout.BELOW,R.id.driver_user_head);
        frameLayout.setLayoutParams(layoutParams);
        // 轮播图
        bannerAdapter = new BannerAdapter(getActivity(),BannerModelList.homeBannerList,"home_page");
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(width,2*width/5);
        view_flow.setLayoutParams(layoutParams1);
        view_flow.setAdapter(bannerAdapter);
        // 实际图片张数
        view_flow.setmSideBuffer(BannerModelList.homeBannerList.size());
        view_flow.setViewPager(MainActivity.mPager);
        if(BannerModelList.homeBannerList.size() <= 1){
            indic.setVisibility(View.GONE);
        }else {
            view_flow.setFlowIndicator(indic);
            view_flow.setTimeSpan(4000);
            // 设置初始位置
            view_flow.setSelection(3 * 1000);
            // 启动自动播放
            view_flow.startAutoFlowTimer();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //空车找货入口
            case R.id.empty_truck_commit:
                if(Util.isLogin(getActivity())){
                    Intent commitIntent = new Intent(getActivity(), EmptyTruckCommitActivity.class);
                    startActivity(commitIntent);
                }else {
                    Intent login_intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            case R.id.guide_container:
                if(DidiApp.isDriverHasCommonLines){
                    if(DidiApp.isUserAowner){
                        if(MainActivity.mPager != null){
                            MainActivity.mPager.setCurrentItem(1,true);
                        }
                    }else {
                        if(DriverMainActivity.mPager != null){
                            DriverMainActivity.mPager.setCurrentItem(1,true);
                        }
                    }
                }else {
                    if(Util.isLogin(activity)){
                        String sessionID = sharedPreferences.getString("session_id", "");
                        String userRole = sharedPreferences.getString("user_role", "车主");
                        HashMap<String,String> params = new HashMap<>();
                        params.put("sessionId",sessionID);
                        if("司机".equals(userRole)){
                            params.put("userRole","sj");
                        }else {params.put("userRole","cz");}
                        PluginUtil.startPlugin(activity.getApplicationContext(),
                                PluginParams.MY_ROUTE_ACTION, PluginParams.MY_ROUTE, params);
                    }else {
                        Intent login_intent = new Intent(activity,LoginActivity.class);
                        startActivity(login_intent);
                    }
                }
                break;
            case R.id.gridView_container:
                gridView_container.setVisibility(View.GONE);
                break;
            case R.id.goods_text:
            case R.id.choose_goods:
                if(mGoodsTypesList.size() > 0){
                    isRecGoodsFileted = true;
                    goods_text.setVisibility(View.VISIBLE);
                    choose_goods.setVisibility(View.VISIBLE);
                    gridView_container.setVisibility(View.VISIBLE);
                    goodsTypeAdapter = new AdapterForGoodsType(activity,mGoodsTypesList);
                    gridView.setAdapter(goodsTypeAdapter);
                }else{
                    gridView_container.setVisibility(View.GONE);
                }
                break;
            case R.id.address_text:
            case R.id.choose_address:
                if(mProvinceList.size() > 0){
                    gridView_container.setVisibility(View.VISIBLE);
                    addrAdapter = new MyAdapter(activity,mProvinceList);
                    gridView.setAdapter(addrAdapter);
                }else{
                    gridView_container.setVisibility(View.GONE);
                }
                break;
            case R.id.home_page_icon:
                news_dot.setVisibility(View.GONE);
                if(DidiApp.isUserAowner){
                    if(MainActivity.mPager != null){
                        MainActivity.mPager.setCurrentItem(3,true);
                    }
                }else {
                    if(DriverMainActivity.mPager != null){
                        DriverMainActivity.mPager.setCurrentItem(3,true);
                    }
                }
                break;
            case R.id.name_auth:
                //实名认证
                if(Util.isLogin(activity)){
                    Intent name_auth_intent = new Intent(activity,AuthActivity.class);
                    name_auth_intent.putExtra("name", "name");
                    startActivity(name_auth_intent);
                }else{
                    Intent login_intent = new Intent(activity,LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            case R.id.licence_auth:
                //驾照认证
                if(Util.isLogin(activity)){
                    Intent name_auth_intent = new Intent(activity,AuthActivity.class);
                    name_auth_intent.putExtra("name", "license");
                    startActivity(name_auth_intent);
                }else{
                    Intent login_intent = new Intent(activity,LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            case R.id.my_route:
                //我的路线
                if(Util.isLogin(activity)){
                    String sessionID = sharedPreferences.getString("session_id", "");
                    String userRole = sharedPreferences.getString("user_role", "车主");
                    HashMap<String,String> params = new HashMap<>();
                    params.put("sessionId",sessionID);
                    if("司机".equals(userRole)){
                        params.put("userRole","sj");
                    }else {params.put("userRole","cz");}
                    PluginUtil.startPlugin(activity.getApplicationContext(),
                            PluginParams.MY_ROUTE_ACTION, PluginParams.MY_ROUTE, params);
                }else {
                    Intent login_intent = new Intent(activity,LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            case R.id.my_trucks:
                //我的车辆
                if(DidiApp.isUserAowner){
                    startLoginActivity(TruckInfoActivity.class);
                }else {
                    startLoginActivity(TruckInfoForDriverActivity.class);
                }
                break;
            case R.id.my_drivers:
                //我的司机
                Intent intent = new Intent(activity,ChooseDriverActivity.class);
                intent.putExtra("driverListType", "MyDriver");
                startActivity(intent);
                break;
            case R.id.my_integral:
                if(Util.isLogin(activity)){
                    Intent intent3 = new Intent(activity, MyIntegralActivity.class);
                    startActivity(intent3);
                }else {
                    Intent login_intent = new Intent(activity,LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            case R.id.income:
                //收入账本
                if(Util.isLogin(activity)){
//                    String sessionID = sharedPreferences.getString("session_id", "");
//                    String userId = sharedPreferences.getString("userId","");
//                    HashMap<String,String> params = new HashMap<>();
//                    params.put("sessionId",sessionID);
//                    params.put("userId",userId);
//                    PluginUtil.startPlugin(activity.getApplicationContext(),
//                            PluginParams.INCOME_BOOK_ACTION, PluginParams.INCOME_BOOK, params);
                    Intent intent2 = new Intent(activity, IncomeBookActivity.class);
                    startActivity(intent2);
                }else {
                    Intent login_intent = new Intent(activity,LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            case R.id.scan_entry:
                Intent scanIntent = new Intent(activity, ConfirmGoodsActivity.class);
                scanIntent.putExtra("from","my_wallet");
                startActivity(scanIntent);
                break;
            case R.id.settings:
                //设置
                Intent settings_intent = new Intent(activity, SettingsActivity.class);
                startActivity(settings_intent);
                break;
            case R.id.contacts:
                //联系客服
                Util.call(activity, getActivity().getResources().getString(R.string.contact_number));
//                Intent intent1 = new Intent(activity, PaymentOrdersActivity.class);
//                startActivity(intent1);
                break;
            case R.id.truck_mall:
                goToMall();
                break;
            case R.id.right_arrow_image:
            case R.id.user_name:
            case R.id.user_phone:
                //跳转个人信息界面
                if(!DidiApp.isUserAowner){
                    startLoginActivity(PersonInfoActivity.class);
                }
                break;
            case R.id.photo:
                if(Util.isLogin(activity)){
                    UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
                    mImageDialog.show();
                }else{
                    Intent login_intent = new Intent(activity,LoginActivity.class);
                    startActivity(login_intent);
                }
                break;
            //头像点击交互
            case R.id.driver_user_head:
                if(!Util.isLogin(activity)){
                    Intent login_intent = new Intent(activity,LoginActivity.class);
                    startActivity(login_intent);
                }else{
                menu.toggle(true);
                }
                break;
            case R.id.driver_my_task_container:
                if(DidiApp.isUserAowner){
                    if(MainActivity.mPager != null){
                    MainActivity.mPager.setCurrentItem(0,true);
                    }
                }else {
                    if(DriverMainActivity.mPager != null){
                    DriverMainActivity.mPager.setCurrentItem(2,true);
                    }
                }
                break;
            case R.id.driver_take_task_container:
                if(Util.isLogin(activity)){
                    Intent refreshGoodsIntent = new Intent();
                    refreshGoodsIntent.setAction(CommonRes.RefreshGoodsList);
                    activity.sendBroadcast(refreshGoodsIntent);
                    if(DidiApp.isUserAowner){
                        if(MainActivity.mPager != null){
                            MainActivity.mPager.setCurrentItem(1,true);
                        }
                    }else {
                        if(DriverMainActivity.mPager != null){
                            DriverMainActivity.mPager.setCurrentItem(1,true);
                        }
                    }
                }else {
                    Intent loginIntent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(loginIntent);
                }

                break;
            case R.id.driver_share_container:
                if(DidiApp.isUserAowner){
                    MainActivity.mPager.setCurrentItem(4,true);
                }else {
                    Util.call(activity, getActivity().getResources().getString(R.string.contact_number));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(driverHomeReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        menu.showContent();
    }

    private void startLoginActivity(Class<?> cls){
        if(Util.isLogin(activity)){
            Intent login_intent = new Intent(activity,cls);
            startActivity(login_intent);
        }else{
            Intent login_intent = new Intent(activity,LoginActivity.class);
            startActivity(login_intent);
        }
    }

    private String user_auth_url = GloableParams.HOST + "thirdpt/mall/authorizedLogin.do";
    private void goToMall(){

        AjaxParams params = new AjaxParams();
//      Log.d("huiyuan"," json params = " + object.toString());
        DidiApp.getHttpManager().sessionPost(activity, user_auth_url,
                params, new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(t);
                            String all = jsonObject.optString("body");
                            JSONObject object = new JSONObject(all);
                            String token = object.optString("token");
                            String uid = object.optString("uid");
                            String url = "http://redlion56.com/autoPartsShop/index.html?token=" + token + "&uid=" + uid;
                            Intent mIntent = new Intent(activity,
                                    WebViewWithTitleActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putString("title", "汽车商城");
                            mBundle.putString("url", url);
                            mIntent.putExtras(mBundle);
                            startActivity(mIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(activity, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                        ToastUtil.show(activity, errMsg);
                    }
                });

    }

    private void getDriverInfo(){
        AjaxParams params = new AjaxParams();
//      Log.d("huiyuan"," json params = " + object.toString());
        user_url = GloableParams.HOST + "uic/user/getUserInfo.do?";
        DidiApp.getHttpManager().sessionPost(activity, user_url,
                params, new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(t);
                            String all = jsonObject.optString("body");
                            MyUserAppVO mUserModel = JSON.parseObject(all,MyUserAppVO.class);
                            CommonRes.UserId = mUserModel.getUserId();
                            CommonRes.roleType = mUserModel.getRoleType();
                            CommonRes.organizationType = mUserModel.getOrganizationType();
                            //个人实名认证
                            CommonRes.hasIdentityAuth = mUserModel.isHasIdentityAuth();
                            //驾驶证
                            CommonRes.hasDrivingLicenceAuth = mUserModel.isHasDrivingLicenceAuth();
                            CommonRes.hasEnterpriseAuth = mUserModel.isHasEnterpriseAuth();
                            CommonRes.hasBusinessAuth = mUserModel.isHasBusinessAuth();
                            CommonRes.hasTransportationPermissionAuth = mUserModel.isHasTransportationPermissionAuth();
                            CommonRes.hasAcct = mUserModel.isHasAcct();
                            if(DidiApp.isUserAowner && CommonRes.roleType != 1){
                                activity.findViewById(R.id.my_drivers).setVisibility(View.VISIBLE);
                            }else {activity.findViewById(R.id.my_drivers).setVisibility(View.GONE);}

                            if(mUserModel.isHasCommonlines()){
                                DidiApp.isDriverHasCommonLines = true;
                            }else {DidiApp.isDriverHasCommonLines = false;}
                            mUserName.setText(mUserModel.getRealName());
                            mUserPhone.setText(mUserModel.getCellphone());
                            String face_url = mUserModel.getUserFace();
                            if(face_url != null && !face_url.equals("")){
                                if(face_url != null && !face_url.equals("")){
                                    activity.getSharedPreferences("config",Context.MODE_PRIVATE).edit().putString("userFace",face_url).commit();
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.default_photo);
                                    mFinalBitmap.display(driver_user_head, face_url,bitmap,bitmap);
                                    mFinalBitmap.display(menu_user_head, face_url,bitmap,bitmap);
                                }else{
                                    driver_user_head.setImageResource(R.drawable.default_photo);
                                    menu_user_head.setImageResource(R.drawable.default_photo);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(activity, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                        ToastUtil.show(activity, errMsg);
                    }
                });
    }

    //上传用户头像
    private void uploadFile(String url,InputStream in) {
        try {
            final PromptManager mPromptManager = new PromptManager();
            mPromptManager.showProgressDialog(activity, "正在上传图片");
            AjaxParams params = new AjaxParams();
            params.put("userFace", in,"img.png");
            DidiApp.getHttpManager().sessionPost(activity, url, params, new ChildAfinalHttpCallBack() {
                @Override
                public void data(String t) {
                    mPromptManager.closeProgressDialog();
                    Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show();
                    getDriverInfo();
                }

                @Override
                public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                    mPromptManager.closeProgressDialog();
                }
            });
        } catch (Exception e) {
        }
    }

    private String task_count_url = "";
    private void getTaskCount(){
        task_count_url = GloableParams.HOST + "carrier/transit/task/countIntransitTask.do";
//        task_count_url = "http://192.168.158.149:8080/gwcz/" + "carrier/auction/recommendAuction.do";
        AjaxParams params = new AjaxParams();

        DidiApp.getHttpManager().sessionPost(activity, task_count_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(t);
                    String count = jsonObject.getString("body");
                    if(count != null && !"".equals(count) && !"0".equals(count)){
                        task_count.setVisibility(View.VISIBLE);
                        task_count.setText(count);
                    }else {task_count.setVisibility(View.GONE);}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
            }

        });
    }


    private List<RecgGoodsModel> recommendAucVOList = new ArrayList<>();
    private String goods_rec_url = GloableParams.HOST + "carrier/auction/recommendAuction.do";
    private void getRecGoods(){
        goods_rec_url = GloableParams.HOST + "carrier/auction/recommendAuction.do";
//        goods_rec_url = "http://192.168.158.149:8080/gwcz/" + "carrier/auction/recommendAuction.do";
        AjaxParams params = new AjaxParams();
        AuctionQueryModel model = new AuctionQueryModel();
        model.setSenderAreaId(selectedProvinceId);
        if(isRecGoodsFileted){
            model.setGoodsName(selectedGoodsName);
        }
        params.put("auctionQueryJson", JSON.toJSONString(model));
        DidiApp.getHttpManager().sessionPost(activity, goods_rec_url, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
//                Log.i("http","推荐货源 = " + t);
//                mPullToRefreshListView.onRefreshComplete();
                JSONObject jsonObject;
                if(recommendAucVOList.size() > 0){
                    recommendAucVOList.clear();
                }
                try {
                    jsonObject = new JSONObject(t);
                    String all = jsonObject.getString("body");
                    JSONObject jsonObject1 = new JSONObject(all);
                    String itemsJson = jsonObject1.optString("items");
                    List<RecgGoodsModel> tmpList = JSON.parseArray(itemsJson,RecgGoodsModel.class);
                    recommendAucVOList.addAll(tmpList);
                    if(recommendAucVOList.size() == 0){
                        mGoddsListview.setVisibility(View.GONE);
                        guide_container.setVisibility(View.VISIBLE);
                        if(DidiApp.isDriverHasCommonLines){
                            guide_img.setImageResource(R.drawable.no_goods_icon);
                        }else {
                            guide_img.setImageResource(R.drawable.add_route_icon);
                        }
                        return;
                    }
//                    Log.d("huiyuan","推荐货源Json = " + JSON.toJSONString(recommendAucVOList));
                    recgGoodsAdapter = new RecgGoodsAdapter(activity,recommendAucVOList);
                    mGoddsListview.setAdapter(recgGoodsAdapter);
                    guide_container.setVisibility(View.GONE);
                    mGoddsListview.setVisibility(View.VISIBLE);
                    scroll_view.fullScroll(ScrollView.FOCUS_UP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
//                mPullToRefreshListView.onRefreshComplete();
            }

        });
    }

    /**
     * 获取热点城市
     */
    private List<CityListModel> mProvinceList = new ArrayList<>();
    private String GET_HOT_CITY_URL = GloableParams.HOST + "carrier/app/areaQuery/queryHotCity.do";
    private void getHotCity() {
        GET_HOT_CITY_URL = GloableParams.HOST + "carrier/app/areaQuery/queryHotCity.do";
        AjaxParams params = new AjaxParams();
        AreaQueryVo areaQueryVo = new AreaQueryVo();
//        Log.i("http","latitude = " + latitude + " longitude = " + longitude);
        if("".equals(latitude)){
            areaQueryVo.setLat("0");
        }else{
        areaQueryVo.setLat(latitude);
        }
        if("".equals(longitude)){
            areaQueryVo.setLng("0");
        }else{
        areaQueryVo.setLng(longitude);
        }
        params.put("queryJson",JSON.toJSONString(areaQueryVo));
        DidiApp.getHttpManager().sessionPost(activity, GET_HOT_CITY_URL, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String body = jsonObject.optString("body");
                    mProvinceList = JSON.parseArray(body, CityListModel.class);
                    initHotCity();
                    selectedPosition = 0;
                    addrAdapter = new MyAdapter(activity,mProvinceList);
                    gridView.setAdapter(addrAdapter);
                    if(mProvinceList.size() > 0){
                        address_text.setVisibility(View.VISIBLE);
                        choose_address.setVisibility(View.VISIBLE);
                    }else {
                        choose_address.setVisibility(View.GONE);
                        address_text.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                choose_address.setVisibility(View.GONE);
                address_text.setVisibility(View.GONE);
            }
        });
    }

    private void initHotCity(){
        if(mProvinceList != null){
            int size = mProvinceList.size();
            if(size > 0){
                address_text.setText("提货地: " + mProvinceList.get(0).getName());
                mProvinceList.remove(size-1);
                selectedProvinceId = mProvinceList.get(0).getId();
                getRecGoods();
                }
            }
    }

    private static String selectedProvinceId = "";
    private static int selectedPosition = 0;
    class MyAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<CityListModel> addressList;
        private Context context;
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,88);
        final RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,88);

        public MyAdapter(Context context,List<CityListModel> goodsText){
            this.addressList = goodsText;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
            layoutParams.leftMargin = 2;
            layoutParams.rightMargin = 2;
            layoutParams.topMargin = 2;
            layoutParams.bottomMargin = 2;
            layoutParams1.leftMargin = 2;
            layoutParams1.rightMargin = 2;
            layoutParams1.topMargin = 20;
            layoutParams1.bottomMargin = 2;
        }
        @Override
        public int getCount() {
            return addressList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = layoutInflater.inflate(R.layout.choose_province_item,null);
            final TextView tv = (TextView) v.findViewById(R.id.province_text);
            tv.setText(addressList.get(position).getName());
            tv.setLayoutParams(layoutParams);

            if(position == selectedPosition){
                tv.setBackgroundColor(context.getResources().getColor(R.color.home_text_press));
                tv.setTextColor(context.getResources().getColor(R.color.white));
                tv.setLayoutParams(layoutParams);
            }else {
                tv.setBackgroundColor(context.getResources().getColor(R.color.list_item_line));
                tv.setTextColor(context.getResources().getColor(R.color.black));
                tv.setLayoutParams(layoutParams);
            }
            if(position == 0 || position == 1 || position == 2){
                tv.setLayoutParams(layoutParams1);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        tv.setBackgroundColor(context.getResources().getColor(R.color.home_text_press));
                        tv.setTextColor(context.getResources().getColor(R.color.white));
                        tv.setLayoutParams(layoutParams);
                        refreshGridView(position);
                        return;
                    }
            });
            return v;
        }
    }

    private void refreshGridView(int position){
        selectedProvinceId = mProvinceList.get(position).getId();
        selectedPosition = position;
        address_text.setText("提货地: " + mProvinceList.get(position).getName());
        gridView_container.setVisibility(View.GONE);
        getRecGoods();
    }

    private List<String> mGoodsTypesList = new ArrayList<>();
    private AdapterForGoodsType goodsTypeAdapter;
    private String goodsListUrl = "";
    private void getGoodsTypes(){
        goodsListUrl = GloableParams.HOST + "carrier/enums/findGoodsType.do";
        AjaxParams params = new AjaxParams();
        DidiApp.getHttpManager().sessionPost(activity, goodsListUrl, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
//                Log.d("huiyuan","goods types json = " + t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    boolean isSuccess = jsonObject.optBoolean("success");
                    JSONArray goodsTypeList = jsonObject.optJSONArray("body");
                    int size = goodsTypeList.length();
                    if (isSuccess) {
                        if (goodsTypeList == null || size <= 0) {
                            Toast.makeText(activity, "data exception", Toast.LENGTH_LONG);
                        } else {
                            for (int i = 0; i < size; i++) {
                                GoodsTypes model = new GoodsTypes();
                                JSONObject obj = (JSONObject) goodsTypeList.get(i);
                                model.setKey(obj.optInt("key"));
                                model.setValue(obj.optString("value"));
                                mGoodsTypesList.add(model.getValue());
                                initGoodsType();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {

            }
        });
    }

    private void initGoodsType(){
        if(mGoodsTypesList != null){
            int size = mGoodsTypesList.size();
            if(size > 0){
                goods_text.setVisibility(View.VISIBLE);
                choose_goods.setVisibility(View.VISIBLE);
            }else {
                goods_text.setVisibility(View.GONE);
                choose_goods.setVisibility(View.GONE);
            }
        }
    }

    private static int selectedGoodsTypePosition = 0;
    private static String selectedGoodsName = "";
    class AdapterForGoodsType extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<String> goodsList;
        private Context context;
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,88);
        final RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,88);

        public AdapterForGoodsType(Context context,List<String> goodsText){
            this.goodsList = goodsText;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
            layoutParams.leftMargin = 2;
            layoutParams.rightMargin = 2;
            layoutParams.topMargin = 2;
            layoutParams.bottomMargin = 2;
            layoutParams1.leftMargin = 2;
            layoutParams1.rightMargin = 2;
            layoutParams1.topMargin = 20;
            layoutParams1.bottomMargin = 2;
        }
        @Override
        public int getCount() {
            return goodsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = layoutInflater.inflate(R.layout.choose_province_item,null);
            final TextView tv = (TextView) v.findViewById(R.id.province_text);
            tv.setText(goodsList.get(position));
            tv.setLayoutParams(layoutParams);

            if(position == selectedGoodsTypePosition){
                tv.setBackgroundColor(context.getResources().getColor(R.color.home_text_press));
                tv.setTextColor(context.getResources().getColor(R.color.white));
                tv.setLayoutParams(layoutParams);
            }else {
                tv.setBackgroundColor(context.getResources().getColor(R.color.list_item_line));
                tv.setTextColor(context.getResources().getColor(R.color.black));
                tv.setLayoutParams(layoutParams);
            }
            if(position == 0 || position == 1 || position == 2){
                tv.setLayoutParams(layoutParams1);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv.setBackgroundColor(context.getResources().getColor(R.color.home_text_press));
                    tv.setTextColor(context.getResources().getColor(R.color.white));
                    tv.setLayoutParams(layoutParams);
                    selectedGoodsName = goodsList.get(position);
                    refreshGoodsGridView(position);
                    return;
                }
            });
            return v;
        }
    }

    private void refreshGoodsGridView(int position){
        selectedGoodsTypePosition = position;
        goods_text.setText(selectedGoodsName);
        choose_goods.setVisibility(View.VISIBLE);
        gridView_container.setVisibility(View.GONE);
        getRecGoods();
    }
}
