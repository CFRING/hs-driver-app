package com.hongshi.wuliudidi.view;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditView extends RelativeLayout {
	private View mView;
	private Context mContext;
	private TextView mTextName,mUnitText;
	private EditText mEdit;

	public EditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mView = View.inflate(mContext, R.layout.edit_item_view, null);
		mTextName = (TextView) mView.findViewById(R.id.edit_name_text);
		mUnitText = (TextView) mView.findViewById(R.id.unit_text);
		mEdit = (EditText) mView.findViewById(R.id.edit_content_edittext);
		addView(mView);
	}
	public void setEditName(String name){
		mTextName.setText(name);
	}
	public String getEditText(){
		return mEdit.getText().toString();
	}
	public void setEditHint(String content){
		mEdit.setHint(content);
	}
	public void setEditContent(String content){
		mEdit.setText(content);
	}
	public void setUnit(String unit){
		mUnitText.setText(unit);
	}
	
	public EditText getEditTextWidget(){
		return mEdit;
	}
}
