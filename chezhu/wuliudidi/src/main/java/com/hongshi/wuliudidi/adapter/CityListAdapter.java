package com.hongshi.wuliudidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.CityListModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityListAdapter extends BaseAdapter implements SectionIndexer{
	private List<CityListModel> list = null;
	private Map<Integer, View> viewMap;
	private Context mContext;

	public CityListAdapter(Context mContext, List<CityListModel> list) {
		this.mContext = mContext;
		this.list = list;
		viewMap = new HashMap<>();
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	public View getItemView(int position) {
		return viewMap.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final CityListModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_city, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.line = view.findViewById(R.id.line);
			view.setTag(viewHolder);
			viewMap.put(position, view);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.line.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
            viewHolder.line.setVisibility(View.GONE);
		}

		viewHolder.tvTitle.setText(this.list.get(position).getName());
		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
        View line;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}