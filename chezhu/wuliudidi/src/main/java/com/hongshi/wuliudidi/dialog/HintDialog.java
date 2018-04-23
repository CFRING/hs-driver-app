package com.hongshi.wuliudidi.dialog;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.activity.AddTruckNewActivity;
import com.hongshi.wuliudidi.activity.AuthActivity;
import com.hongshi.wuliudidi.activity.WalletRechargeActivity;
import com.hongshi.wuliudidi.utils.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HintDialog extends Dialog implements OnClickListener{
	private Context mContext;
	private showType type = showType.other;
	private TextView mHintContent,mLeft,mRight;
	private String contentText, leftText, rightText;
	private HintDialogCallBack callBack;
	
	
	public HintDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	public HintDialog(Context context, int theme, showType type) {
		super(context, theme);
		this.mContext = context;
		this.type = type;
		init();
	}
	
	public HintDialog(Context context, int theme, String contentText, String leftText, String rightText,
			HintDialogCallBack callBack){
		super(context, theme);
		this.mContext = context;
		this.contentText = contentText;
		this.leftText = leftText;
		this.rightText = rightText;
		this.callBack = callBack;
		init();
	}
	
	private void init(){
		setContentView(R.layout.hint_dialog);
		mHintContent = (TextView) findViewById(R.id.hint_content);
		mLeft = (TextView) findViewById(R.id.left);
		mRight = (TextView) findViewById(R.id.right);
		mLeft.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mLeft.setText("稍后再说");
		switch (type) {
		case type_auth:
			mHintContent.setText("很遗憾！您还不是实名认证用户，只有通过实名认证才能参与接单哦。");
			mRight.setText("前往认证");
			break;
		case add_truck_type:
			mHintContent.setText("很抱歉！您暂无任何车辆信息，请先添加车辆后再来参与接单吧！");
			mRight.setText("添加车辆");
			break;
		case tel_type:
			mHintContent.setText("很遗憾！您的保证金不足，不能参与接单，请联系客服。");
			mRight.setText("联系客服");
			break;
		case currency_lack_type:
			mHintContent.setText("接单需要账户余额保障，余额不足不能参与接单。");
			mRight.setText("账户充值");
		default:
			mHintContent.setText(contentText);
			mLeft.setText(leftText);
			mRight.setText(rightText);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left:
				if(type == showType.other && callBack !=  null){
					callBack.onLeftClick();
				}
				dismiss();
				break;
			case R.id.right:
				switch (type) {
				case type_auth:
					Intent auth_intent = new Intent(mContext, AuthActivity.class);
					auth_intent.putExtra("name", "name");
					mContext.startActivity(auth_intent);
					break;
				case add_truck_type:
					Intent add_intent = new Intent(mContext, AddTruckNewActivity.class);
					mContext.startActivity(add_intent);
					break;
				case tel_type:
					Util.call(mContext, mContext.getResources().getString(R.string.contact_number));
					break;
				case currency_lack_type:
//					if(Util.isLogin(mContext)){
//						Intent rechargeIntent = new Intent(mContext, WalletRechargeActivity.class);
//						mContext.startActivity(rechargeIntent);
//					}
					break;
				default:
					if(callBack != null){
						callBack.onRightClick();
					}
					break;
				}
				dismiss();
				break;
			default:
				break;
		}
	}
	public static enum showType{
		//没实名认证不能接单
		type_auth,
		//没车不能接单
		add_truck_type,
		//保证金不足不能接单
		tel_type,
		//钱不够交押金不能接单
		currency_lack_type,
		other;
	}
	
	public static interface HintDialogCallBack{
		public void onLeftClick();
		public void onRightClick();
	}

	public TextView getmRight(){
		return this.mRight;
	}
}
