package com.hongshi.wuliudidi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hongshi.wuliudidi.CommonRes;
import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.dialog.DateDialog;
import com.hongshi.wuliudidi.impl.ChildAfinalHttpCallBack;
import com.hongshi.wuliudidi.impl.SetDateCallBack;
import com.hongshi.wuliudidi.model.TaskStatusTrackVO;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ActivityManager;
import com.hongshi.wuliudidi.utils.LogUtil;
import com.hongshi.wuliudidi.utils.UploadUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;

/**
 * @author Created by huiyuan on 2016/3/30.
 */
public class TransportStatusActivity extends Activity implements View.OnClickListener{
    private DiDiTitleView pageTitle;
    private TextView arrive_time_btn;
    private TextView has_arrived_source_place_btn;
    private TextView wait_for_dispatch_btn;
    private TextView estimated_arrived_target_btn;
    private TextView encounter_tough_btn;
    private TextView finish_transport_btn;

    private ArrayList<TaskStatusTrackVO> statusList;
    private final String UPDATE_TRANSPORT_STATUS = GloableParams.HOST + "/carrier/transit/task/TST/updateTaskStatus.do";

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ActivityManager.getInstance().addActivity(this);
        
        setContentView(R.layout.transportation_status_commit);
        
        Bundle bundle = getIntent().getExtras();
        statusList = (ArrayList<TaskStatusTrackVO>)bundle.getSerializable("transport");
//        if(statusList != null)
//        LogUtil.d("huiyuan", "the Task Status Track List = " + statusList.get(0).toString());
        initView();
    }

    private void initView(){
        pageTitle = (DiDiTitleView)findViewById(R.id.transport_status_page_tittle);
        arrive_time_btn =(TextView)findViewById(R.id.arrive_time_btn);
        has_arrived_source_place_btn =(TextView)findViewById(R.id.has_arrived_source_place_btn);
        wait_for_dispatch_btn = (TextView) findViewById(R.id.wait_for_dispatch_btn);
        estimated_arrived_target_btn = (TextView) findViewById(R.id.estimated_arrived_target_btn);
        encounter_tough_btn = (TextView) findViewById(R.id.encounter_tough_btn);
        finish_transport_btn = (TextView) findViewById(R.id.finish_transport_btn);

        pageTitle.setBack(this);
        pageTitle.setTitle(getString(R.string.transport_status_page_tittle));
        
        setBtnTextStatus();
    }

    private void setBtnTextStatus(){
    	if(statusList != null && statusList.size() >= 6){
	        if(statusList.get(0).getStatus() == 2){
	            arrive_time_btn.setClickable(false);
	            arrive_time_btn.setTextColor(getResources().getColor(R.color.line_color));
	        }else {
	            arrive_time_btn.setOnClickListener(this);
	            arrive_time_btn.setTextColor(getResources().getColor(R.color.gray));
	        }
	        if(statusList.get(1).getStatus() ==2){
	            has_arrived_source_place_btn.setClickable(false);
	            has_arrived_source_place_btn.setTextColor(getResources().getColor(R.color.line_color));
	        }else {
	            has_arrived_source_place_btn.setOnClickListener(this);
	            has_arrived_source_place_btn.setTextColor(getResources().getColor(R.color.gray));
	        }
	        if(statusList.get(2).getStatus() == 2){
	            wait_for_dispatch_btn.setClickable(false);
	            wait_for_dispatch_btn.setTextColor(getResources().getColor(R.color.line_color));
	        }else {
	            wait_for_dispatch_btn.setOnClickListener(this);
	            wait_for_dispatch_btn.setTextColor(getResources().getColor(R.color.gray));
	        }
	
	        if(statusList.get(3).getStatus() == 2){
	            estimated_arrived_target_btn.setClickable(false);
	            estimated_arrived_target_btn.setTextColor(getResources().getColor(R.color.line_color));
	        }else {
	            estimated_arrived_target_btn.setOnClickListener(this);
	            estimated_arrived_target_btn.setTextColor(getResources().getColor(R.color.gray));
	        }
	        if(statusList.get(4).getStatus() ==2){
	            encounter_tough_btn.setClickable(false);
	            encounter_tough_btn.setTextColor(getResources().getColor(R.color.line_color));
	        }else {
	            encounter_tough_btn.setOnClickListener(this);
	            encounter_tough_btn.setTextColor(getResources().getColor(R.color.gray));
	        }
	        if(statusList.get(5).getStatus() == 2){
	            finish_transport_btn.setClickable(false);
	            finish_transport_btn.setTextColor(getResources().getColor(R.color.line_color));
	        }else {
	            finish_transport_btn.setOnClickListener(this);
	            finish_transport_btn.setTextColor(getResources().getColor(R.color.gray));
	        }
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.arrive_time_btn:
                //预计到达发货地时间
                DateDialog mDateDialog = new DateDialog(this, R.style.data_filling_dialog,new SetDateCallBack() {
                    @Override
                    public void date(long date) {
//                        arrive_time_txt.setText(Util.getFormatedDateTime(date));
//                        arrive_time_txt.setVisibility(View.VISIBLE);
                        arrive_time_btn.setTextColor(getResources().getColor(R.color.line_color));
                        TaskStatusTrackVO statusTrackVO = null;
                        if(statusList != null){
                         statusTrackVO = statusList.get(0);
                        }
                        if(statusTrackVO != null){
                        updateTransitStatus(statusTrackVO.getTaskId(),statusTrackVO.getId(),
                                statusTrackVO.getSortOrder(),Util.getFormatedDateTime(date));
                        }
                        arrive_time_btn.setClickable(false);
                    }
                }, DateDialog.Entire,"预计到达发货地时间");

                UploadUtil.setAnimation(mDateDialog, CommonRes.TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.has_arrived_source_place_btn:
                //已到达等待装货
            	arrive_time_btn.setTextColor(getResources().getColor(R.color.line_color));
            	arrive_time_btn.setClickable(false);
                has_arrived_source_place_btn.setTextColor(getResources().getColor(R.color.line_color));
                TaskStatusTrackVO statusTrackVO1 = null;
                if(statusList != null){
                    statusTrackVO1 = statusList.get(1);
                }
                if(statusTrackVO1 != null){
                updateTransitStatus(statusTrackVO1.getTaskId(), statusTrackVO1.getId(),
                        statusTrackVO1.getSortOrder(), "");
                }
                has_arrived_source_place_btn.setClickable(false);
                break;
            case R.id.wait_for_dispatch_btn:
                //装完发车
            	arrive_time_btn.setTextColor(getResources().getColor(R.color.line_color));
            	arrive_time_btn.setClickable(false);
                has_arrived_source_place_btn.setTextColor(getResources().getColor(R.color.line_color));
                has_arrived_source_place_btn.setClickable(false);
                wait_for_dispatch_btn.setTextColor(getResources().getColor(R.color.line_color));
                TaskStatusTrackVO statusTrackVO2 = null;
                if(statusList != null){
                    statusTrackVO2 = statusList.get(2);
                }
                if(statusTrackVO2 != null){
                updateTransitStatus(statusTrackVO2.getTaskId(), statusTrackVO2.getId(),
                        statusTrackVO2.getSortOrder(), "");
                }
                wait_for_dispatch_btn.setClickable(false);
                break;
            case R.id.estimated_arrived_target_btn:
                //预计到达收货地时间
                mDateDialog = new DateDialog(this, R.style.data_filling_dialog,new SetDateCallBack() {
                    @Override
                    public void date(long date) {
//                        arrive_time_txt2.setText(Util.getFormatedDateTime(date));
//                        arrive_time_txt2.setVisibility(View.VISIBLE);
                        estimated_arrived_target_btn.setTextColor(getResources().getColor(R.color.line_color));
                        TaskStatusTrackVO statusTrackVO3 = null;
                        if(statusList != null){
                            statusTrackVO3 = statusList.get(3);
                        }
                        if(statusTrackVO3 != null){
                        updateTransitStatus(statusTrackVO3.getTaskId(), statusTrackVO3.getId(),
                                statusTrackVO3.getSortOrder(), Util.getFormatedDateTime(date));
                        }
                        arrive_time_btn.setTextColor(getResources().getColor(R.color.line_color));
                    	arrive_time_btn.setClickable(false);
                        has_arrived_source_place_btn.setTextColor(getResources().getColor(R.color.line_color));
                        has_arrived_source_place_btn.setClickable(false);
                        wait_for_dispatch_btn.setTextColor(getResources().getColor(R.color.line_color));
                        wait_for_dispatch_btn.setClickable(false);
                        estimated_arrived_target_btn.setClickable(false);
                    }
                }, DateDialog.Entire, "预计到达收货地时间");
                UploadUtil.setAnimation(mDateDialog, CommonRes.TYPE_BOTTOM, true);
                mDateDialog.show();
                break;
            case R.id.encounter_tough_btn:
                //遇到困难
                encounter_tough_btn.setTextColor(getResources().getColor(R.color.line_color));
                TaskStatusTrackVO statusTrackVO4 = null;
                if(statusList != null){
                    statusTrackVO4 = statusList.get(4);
                }
                if(statusTrackVO4 != null){
                updateTransitStatus(statusTrackVO4.getTaskId(), statusTrackVO4.getId(),
                        statusTrackVO4.getSortOrder(), "");
                }
                arrive_time_btn.setTextColor(getResources().getColor(R.color.line_color));
            	arrive_time_btn.setClickable(false);
                has_arrived_source_place_btn.setTextColor(getResources().getColor(R.color.line_color));
                has_arrived_source_place_btn.setClickable(false);
                wait_for_dispatch_btn.setTextColor(getResources().getColor(R.color.line_color));
                wait_for_dispatch_btn.setClickable(false);
                estimated_arrived_target_btn.setTextColor(getResources().getColor(R.color.line_color));
                estimated_arrived_target_btn.setClickable(false);
                encounter_tough_btn.setClickable(false);
                break;
            case R.id.finish_transport_btn:
                //货物送达
                arrive_time_btn.setTextColor(getResources().getColor(R.color.line_color));
                has_arrived_source_place_btn.setTextColor(getResources().getColor(R.color.line_color));
                wait_for_dispatch_btn.setTextColor(getResources().getColor(R.color.line_color));
                estimated_arrived_target_btn.setTextColor(getResources().getColor(R.color.line_color));
                encounter_tough_btn.setTextColor(getResources().getColor(R.color.line_color));
                finish_transport_btn.setTextColor(getResources().getColor(R.color.line_color));
                arrive_time_btn.setClickable(false);
                has_arrived_source_place_btn.setClickable(false);
                wait_for_dispatch_btn.setClickable(false);
                estimated_arrived_target_btn.setClickable(false);
                encounter_tough_btn.setClickable(false);
                finish_transport_btn.setClickable(false);

                TaskStatusTrackVO statusTrackVO5 = null;
                if(statusList != null){
                    statusTrackVO5 = statusList.get(5);
                }
                if(statusTrackVO5 != null){
                updateTransitStatus(statusTrackVO5.getTaskId(), statusTrackVO5.getId(),
                        statusTrackVO5.getSortOrder(), "");
                }
                break;
            default:
                break;
        }
    }

    private void updateTransitStatus(String taskId, String tstid, int sortOrder, String statusDesc ){
        AjaxParams params = new AjaxParams();
        params.put("transitTaskId", taskId);
        params.put("statusDesc",statusDesc);
        params.put("tstid",tstid);
        params.put("sortOrder","" + sortOrder);
        DidiApp.getHttpManager().sessionPost(TransportStatusActivity.this, UPDATE_TRANSPORT_STATUS, params, new ChildAfinalHttpCallBack() {
            @Override
            public void data(String t) {
                Toast.makeText(TransportStatusActivity.this,"运输状态设置成功!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String errCode, String errMsg, Boolean errSerious) {
                Toast.makeText(TransportStatusActivity.this,"运输状态设置失败!",Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(0);
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("TransportStatusActivity");
    }

    @Override
    protected void onResume(){
    	super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("TransportStatusActivity");
    }

}
