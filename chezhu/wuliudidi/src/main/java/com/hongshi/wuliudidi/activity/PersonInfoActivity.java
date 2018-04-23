package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.camera.ActivityCapture;
import com.hongshi.wuliudidi.camera.PhotoPreviewActivity;
import com.hongshi.wuliudidi.dialog.DataFillingDialog;
import com.hongshi.wuliudidi.dialog.UploadReceiptDialog;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.model.MyUserAppVO;
import com.hongshi.wuliudidi.model.UserModel;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.photo.GetPhotoUtil;
import com.hongshi.wuliudidi.utils.ImageUtil;
import com.hongshi.wuliudidi.utils.PromptManager;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.CircleImageView;
import com.hongshi.wuliudidi.view.DiDiTitleView;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

/**
 * @author Created by huiyuan on 2017/3/21.
 */

public class PersonInfoActivity extends Activity {

    private CircleImageView user_head_img;
    private ImageView call_host,driver_info_guide;
    private RelativeLayout user_head_img_container;
    private TextView driver_name_text,driver_phone_text,driver_email_text,driver_host_text,
            driver_host_phone_text;
    private String user_url = GloableParams.HOST + "uic/user/getUserInfo.do?";
    //上传用户头像
    private String upload_url = GloableParams.HOST + "uic/user/uploadUserFace.do?";
    private DataFillingDialog mImageDialog;
    private FinalBitmap mFinalBitmap;
    private DiDiTitleView personal_info_title;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonRes.CAMERA:
                    UploadUtil.camera(PersonInfoActivity.this,mHandler);
                    break;
                case CommonRes.GALLERY:
                    UploadUtil.photo(PersonInfoActivity.this,mHandler);
                    break;
                default:
                    break;
            }
        }
    };

    //刷新页面的广播接收
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CommonRes.RefreshUserInfo)) {
                getDriverInfo();
            }else if(action.equals(CommonRes.UploadPhoto)){
                if(intent.getBooleanExtra("isCamera", false)){
                    //相机
                    if (UploadUtil.hasSdcard()) {
                        if(UploadUtil.tempFile == null){
                            UploadUtil.tempFile = new File(Environment.getExternalStorageDirectory(),UploadUtil.PHOTO_FILE_NAME);
                        }
                        uploadFile(upload_url, ImageUtil.getimage(ImageUtil.getImageAbsolutePath(PersonInfoActivity.this, Uri.fromFile(UploadUtil.tempFile))));
                        UploadUtil.tempFile = null;
                    }
                }else{
                    String path = intent.getStringExtra("path");
                    uploadFile(upload_url,ImageUtil.getimage(path));
                }
            }
        }
    };

    //上传用户头像
    private void uploadFile(String url,InputStream in) {
        try {
            final PromptManager mPromptManager = new PromptManager();
            mPromptManager.showProgressDialog(PersonInfoActivity.this, "正在上传图片");
            AjaxParams params = new AjaxParams();
            params.put("userFace", in,"img.png");
            DidiApp.getHttpManager().sessionPost(PersonInfoActivity.this, url, params, new AfinalHttpCallBack() {
                @Override
                public void data(String t) {
                    mPromptManager.closeProgressDialog();
                    Toast.makeText(PersonInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    getDriverInfo();
                }

            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_person_info_activity);

        initViews();
        getDriverInfo();
    }

    private void initViews(){
        mFinalBitmap = FinalBitmap.create(PersonInfoActivity.this);
        user_head_img = (CircleImageView) findViewById(R.id.user_head_img);
        call_host = (ImageView) findViewById(R.id.call_host);
        driver_info_guide = (ImageView) findViewById(R.id.driver_info_guide);
        user_head_img_container = (RelativeLayout) findViewById(R.id.user_head_img_container);
        driver_name_text = (TextView) findViewById(R.id.driver_name_text);
        driver_phone_text = (TextView) findViewById(R.id.driver_phone_text);
        driver_email_text = (TextView) findViewById(R.id.driver_email_text);
        driver_host_text = (TextView) findViewById(R.id.driver_host_text);
        driver_host_phone_text = (TextView) findViewById(R.id.driver_host_phone_text);
        personal_info_title = (DiDiTitleView) findViewById(R.id.personal_info_title);
        personal_info_title.setTitle("个人资料");
        personal_info_title.setBack(this);
        call_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.call(PersonInfoActivity.this,driver_host_phone_text.getText().toString());
            }
        });
        user_head_img_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isLogin(PersonInfoActivity.this)){
                    UploadUtil.setAnimation(mImageDialog,CommonRes.TYPE_BOTTOM, false);
                    mImageDialog.show();
                }else{
                    Intent login_intent = new Intent(PersonInfoActivity.this,LoginActivity.class);
                    startActivity(login_intent);
                }
            }
        });

        // 拍照,0 表示拍照或者图库选取
        mImageDialog = new DataFillingDialog(PersonInfoActivity.this, R.style.data_filling_dialog, mHandler, 0,-1);
        mImageDialog.setCanceledOnTouchOutside(true);
        mImageDialog.setText("拍照", "图库选取");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonRes.RefreshUserInfo);
        intentFilter.addAction(CommonRes.UploadPhoto);
        intentFilter.addAction("has_common_line");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    private void getDriverInfo(){
        AjaxParams params = new AjaxParams();
//      Log.d("huiyuan"," json params = " + object.toString());
        DidiApp.getHttpManager().sessionPost(PersonInfoActivity.this, user_url,
                params, new ChildAfinalHttpCallBack() {
                    @Override
                    public void data(String t) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(t);
                            String all = jsonObject.optString("body");
                            MyUserAppVO mUserModel = JSON.parseObject(all,MyUserAppVO.class);
                            if(mUserModel.isHasCommonlines()){
                                DidiApp.isDriverHasCommonLines = true;
                            }else {DidiApp.isDriverHasCommonLines = false;}
                            String realName = mUserModel.getRealName();
                            if(realName != null && !"".equals(realName)){
                                driver_name_text.setText(mUserModel.getRealName());
                            }else {
                                driver_name_text.setText(getSharedPreferences("config", Context.MODE_PRIVATE)
                                        .getString("name",""));
                            }
                            driver_phone_text.setText(mUserModel.getCellphone());
                            driver_email_text.setText(mUserModel.getEmail());
                            driver_host_text.setText(mUserModel.getTruckOwnerName());
                            driver_host_phone_text.setText(mUserModel.getTruckOwnerCellphone());
                            String face_url = mUserModel.getUserFace();
                            if(face_url != null && !face_url.equals("")){
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.default_photo);
                                mFinalBitmap.display(user_head_img, face_url,bitmap,bitmap);
                                Intent intent = new Intent();
                                intent.setAction(CommonRes.RefreshUserPhoto);
                                sendBroadcast(intent);
                            }else{
                                user_head_img.setImageResource(R.drawable.default_photo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(PersonInfoActivity.this, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                        ToastUtil.show(PersonInfoActivity.this, errMsg);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == UploadUtil.PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                String path = ImageUtil.getImageAbsolutePath(PersonInfoActivity.this, uri);
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
                GetPhotoUtil.crop(PersonInfoActivity.this, uri,CommonRes.tempFile);
            }
        }else if (requestCode == GetPhotoUtil.PHOTO_REQUEST_CUT && resultCode == RESULT_OK){
            String path = ImageUtil.getImageAbsolutePath(PersonInfoActivity.this, Uri.fromFile(CommonRes.tempFile));
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }
}
