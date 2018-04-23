package com.hongshi.wuliudidi.activity;

import net.tsz.afinal.http.AjaxParams;


import com.hongshi.wuliudidi.DidiApp;
import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.impl.AfinalHttpCallBack;
import com.hongshi.wuliudidi.params.GloableParams;
import com.hongshi.wuliudidi.utils.ToastUtil;
import com.hongshi.wuliudidi.utils.Util;
import com.hongshi.wuliudidi.view.DiDiTitleView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author huiyuan
 */
public class SubmitSuggestionActivity extends Activity implements OnClickListener{
	private DiDiTitleView mTitleView;
	private RadioGroup mRadioGroup;
	private EditText mEditText;
	private Button mSubmit;
	private String submit_url = GloableParams.HOST + "carrier/feedback/insert.do?";

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("SubmitSuggestionActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("SubmitSuggestionActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.submit_suggestion_activity);
		  
		 mTitleView = (DiDiTitleView) findViewById(R.id.suggestion_title);
		 mTitleView.setTitle(getResources().getString(R.string.feedback));
		 mTitleView.setBack(this); 
		 
		 mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
		 setRadioButtonTextColor();
		 
		 mEditText = (EditText) findViewById(R.id.input_feedback);
		 
		 mSubmit = (Button) findViewById(R.id.submint);
		 mSubmit.setOnClickListener(this);
	}
	private void setRadioButtonTextColor()
	{
		 ColorStateList mColorList;
		//定义一个colorlist声明一个配色方案，这个方案声明了两种状况下（checked和-checked）分别使用两种字体颜色（white和black），
		 int[] mRadioBtnTextColors = new int[]{getResources().getColor(R.color.white), getResources().getColor(R.color.theme_color)};
		 int[][] mRadioBtnStates = {{android.R.attr.state_checked},{-android.R.attr.state_checked}};
		 //把每个radiobutton都读取过来，用mColorList一一为它们设置字体配色
		 
		 mColorList = new ColorStateList(mRadioBtnStates, mRadioBtnTextColors);
		 for(int i = 0; i < mRadioGroup.getChildCount(); i++)
		 {
			 RadioButton mRadioBtn = (RadioButton) mRadioGroup.getChildAt(i);
			 mRadioBtn.setTextColor(mColorList);
		 }
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.submint:
			AjaxParams params = new AjaxParams();
			int type_int = getSelectedType();
			if(type_int < 0){
				//用户没有选中任何一个选项
				ToastUtil.show(SubmitSuggestionActivity.this, "请选择反馈类型");
				return;
			}
			if(Util.containsEmoji(mEditText.getText().toString())){
				ToastUtil.show(SubmitSuggestionActivity.this, "输入有非法字符，请重新输入");
				return;
			}
			params.put("type", String.valueOf(type_int));
			params.put("content", mEditText.getText().toString());
			DidiApp.getHttpManager().sessionPost(SubmitSuggestionActivity.this, submit_url, params, new AfinalHttpCallBack() {
				@Override
				public void data(String t) {
					ToastUtil.show(SubmitSuggestionActivity.this, "意见反馈成功");
					finish();
				}
			});
			break;
		default:
			break;
		}
		
	}
	private int getSelectedType(){
		int i;
		for(i = 0; i < mRadioGroup.getChildCount(); i++){
			if(mRadioGroup.getChildAt(i).getId() == mRadioGroup.getCheckedRadioButtonId()){
				break;
			}
		}
		switch(i){
		case 0:
			return 1;
		case 1:
			return 3;
		case 2:
			return 99;
		default:
			return -1;
		}
	}
}
