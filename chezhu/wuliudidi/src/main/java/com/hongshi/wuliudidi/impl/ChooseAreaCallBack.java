package com.hongshi.wuliudidi.impl;

import com.hongshi.wuliudidi.dialog.ChooseAreaPopupWindow.AreaType;
import com.hongshi.wuliudidi.model.AreaModel;

public interface ChooseAreaCallBack {
	public void getResult(AreaModel result, AreaType areaTag);
}
