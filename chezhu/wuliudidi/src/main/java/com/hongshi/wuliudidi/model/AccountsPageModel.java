package com.hongshi.wuliudidi.model;

import java.util.List;
import java.util.Map;

public class AccountsPageModel {
	private int currentPage;
	private int itemCount;
	private int pageSize;
	private List<String> month;
	private Map<String, List<AccountsItemModel>> map;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public Map<String, List<AccountsItemModel>> getMap() {
		return map;
	}
	public void setMap(Map<String, List<AccountsItemModel>> map) {
		this.map = map;
	}
	
	public List<String> getMonth() {
		return month;
	}
	public void setMonth(List<String> month) {
		this.month = month;
	}
	public void pasteDate(){
		for(int i = 0; i < month.size(); i++){
			String dateStr = month.get(i);
			List<AccountsItemModel> list = map.get(dateStr);
			for(int j = 0; j < list.size(); j++){
				list.get(j).setDateStr(dateStr);
			}
		}
	}
	
}
