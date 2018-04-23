package com.hongshi.wuliudidi.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hongshi.wuliudidi.R;
import com.hongshi.wuliudidi.model.TruckTransitTaskRecordVO;
import com.hongshi.wuliudidi.utils.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class ReportListAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
    List<List<TruckTransitTaskRecordVO>> childDataList = new ArrayList<List<TruckTransitTaskRecordVO>>();
	List<String> groups = new ArrayList<String>();
	
    public ReportListAdapter(Context context,ExpandableListView mReportListView,List<String> groups,List<List<TruckTransitTaskRecordVO>> childDataList) {
        inflater = LayoutInflater.from(context);
        this.groups = groups;
        this.childDataList = childDataList;
	}
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return groups.size();
    }
 
    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return childDataList.get(i).size();
    }
 
    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
    	 return groups.get(i);
    }
 
    @Override
    //gets the name of each item
    public Object getChild(int groupPosition, int childPosition) {
    	return childDataList.get(groupPosition).get(childPosition);
    }
 
    @Override
    public long getGroupId(int i) {
        return i;
    }
 
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
 
    @Override
    public boolean hasStableIds() {
        return true;
    }
 
    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.report_list_parent_item, viewGroup,false);
            TextView report_date = (TextView) view.findViewById(R.id.report_date);
            report_date.setText(groups.get(i));
        }
        view.setClickable(true);   
        //return the entire view
        return view;
    }
    
 
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.report_list_child_item, viewGroup,false);
            TextView date_name_text = (TextView) view.findViewById(R.id.date_name_text);
            TruckTransitTaskRecordVO truckTransitTaskRecordVO = childDataList.get(groupPosition).get(childPosition);
            date_name_text.setText(""+truckTransitTaskRecordVO.getDayOfMonth()+" 日"+"  "+truckTransitTaskRecordVO.getTruckNum()
            		+ "  "+truckTransitTaskRecordVO.getDriverName());
            TextView number = (TextView) view.findViewById(R.id.number);//运量
            number.setText(Util.formatDoubleToString(truckTransitTaskRecordVO.getRealAmount(), truckTransitTaskRecordVO.getUnitText()));
        }
        //return the entire view
        return view;
    }
 
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
