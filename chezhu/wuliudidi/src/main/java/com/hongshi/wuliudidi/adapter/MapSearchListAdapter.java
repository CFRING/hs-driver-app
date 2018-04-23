package com.hongshi.wuliudidi.adapter;

import java.util.List;

import com.hongshi.wuliudidi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class MapSearchListAdapter extends BaseAdapter implements Filterable {

	private Context mContext;
	private List<String> listString;
	private MyFilter mFilter;

	public MapSearchListAdapter(Context context, List<String> listString) {
		this.listString = listString;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return listString.size();
	}

	@Override
	public String getItem(int position) {
		return listString.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.route_inputs, null);
			viewHolder = new ViewHolder();
			viewHolder.online_user_list_item_textview = (TextView) convertView.findViewById(R.id.online_user_list_item_textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.online_user_list_item_textview.setText(listString.get(position));
		return convertView;
	}

	static class ViewHolder {
		TextView online_user_list_item_textview;

	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new MyFilter();
		}
		return mFilter;

	}

	private class MyFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			results.values = listString;
			results.count = listString.size();
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

}
