package com.hongshi.wuliudidi.incomebook;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DateArrayAdapter extends ArrayWheelAdapter<String> {

	private static final String TAG = "DateArrayAdapter";

	// Index of current item
	int currentItem;
	// Index of item to be highlighted
	int currentValue;
	Context context;

	/**
	 * Constructor
	 */
	public DateArrayAdapter(Context context, String[] items, int current) {
		super(context, items);
		this.currentValue = current;
		this.context = context;
		setItemResource(context.getResources().getColor(R.color.white));
	}

	@Override
	protected void configureTextView(TextView view) {
		super.configureTextView(view);
		if (currentItem == currentValue) {
			view.setTextColor(context.getResources().getColor(R.color.black));
		} else {
			view.setTextColor(context.getResources().getColor(R.color.gray_light));
		}
		view.setTextSize(context.getResources().getDimensionPixelOffset(R.dimen.width_6));
		view.setPadding(0, 15, 0, 15);
		view.setTypeface(Typeface.SANS_SERIF);
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		currentItem = index;
		return super.getItem(index, cachedView, parent);
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}
}
