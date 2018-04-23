package com.hongshi.wuliudidi.activity;

import java.util.ArrayList;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.adapter.BasePagerAdapter;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author huiyuan
 */
public class GuideActivity extends Activity implements OnPageChangeListener,
		OnClickListener {
	private Context context;
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private ImageView  no_notify;
	private LinearLayout indicatorLayout;
	private ArrayList<View> views;
	private ImageView[] indicators = null;
	private int[] images;

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("GuideActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("GuideActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		context = this;
		// 设置引导图片
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 仅需在这设置图片 指示器和page自动添加
		images = new int[] { R.drawable.g_1, R.drawable.g_2, R.drawable.g_3};
		initView();
	}

	// 初始化视图
	private void initView() {
		// 实例化视图控件
		viewPager = (ViewPager) findViewById(R.id.viewpage);
//		startButton = (ImageView) findViewById(R.id.start_Button);
		no_notify = (ImageView) findViewById(R.id.no_notify);
//		startButton.setOnClickListener(this);
		no_notify.setOnClickListener(this);
		indicatorLayout = (LinearLayout) findViewById(R.id.indicator);
		views = new ArrayList<View>();
		// 定义指示器数组大小
		indicators = new ImageView[images.length];
		for (int i = 0; i < images.length; i++) {
			// 循环加入图片
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(images[i]);
			views.add(imageView);
			// 循环加入指示器
			indicators[i] = new ImageView(context);
			indicators[i].setBackgroundResource(R.drawable.indicators_default);
			if (i == 0) {
				indicators[i].setBackgroundResource(R.drawable.indicators_now);
			}
			LinearLayout.LayoutParams childLpPlay = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			childLpPlay.leftMargin = 40;
			// childLpPlay.rightMargin = Size.$().w(190);
			indicatorLayout.addView(indicators[i], childLpPlay);
		}
		pagerAdapter = new BasePagerAdapter(views);
		// 设置适配器
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	// 按钮的点击事件
	@Override
	public void onClick(View v) {
		SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		String userRole = preferences.getString("user_role","车主");
//		if (v.getId() == R.id.start_Button) {
//			if("司机".equals(userRole)){
//				startActivity(new Intent(GuideActivity.this, DriverMainActivity.class));
//			}else
//			startActivity(new Intent(GuideActivity.this, MainActivity.class));
//			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//			this.finish();
//		} else if (v.getId() == R.id.no_notify) {
			 Editor editor = preferences.edit();
			 // 存入数据
			 editor.putBoolean("isShowGuide", false);
			 // 提交修改
			 editor.commit();
			if("司机".equals(userRole)){
				startActivity(new Intent(GuideActivity.this, DriverMainActivity.class));
			}else{
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
			}
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			this.finish();
//		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 监听viewpage
	@Override
	public void onPageSelected(int arg0) {
		// 显示最后一个图片时显示按钮
		if (arg0 == indicators.length - 1) {
//			startButton.setVisibility(View.VISIBLE);
			no_notify.setVisibility(View.VISIBLE);
		} else {
//			startButton.setVisibility(View.INVISIBLE);
			no_notify.setVisibility(View.INVISIBLE);
		}
		// 更改指示器图片
		for (int i = 0; i < indicators.length; i++) {
			indicators[i].setVisibility(View.VISIBLE);
			indicators[arg0].setBackgroundResource(R.drawable.indicators_now);
			if (arg0 != i) {
				indicators[i].setBackgroundResource(R.drawable.indicators_default);
			}
			if (arg0 == indicators.length - 1) {
				indicators[i].setVisibility(View.GONE);
			}
		}
	}
}
