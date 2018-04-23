package com.hongshi.wuliudidi.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class InputLimitTextWatcher implements TextWatcher{
	protected EditText editText;
	protected String validStr = "";
	protected int selectionStart;
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		return;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		return;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		return;
	}

	public void setEditText(EditText editText) {
		this.editText = editText;
		editText.addTextChangedListener(this);
	}
	
	public void removeWatcher(){
		editText.removeTextChangedListener(this);
	}
}
