package com.hongshi.wuliudidi.utils;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.AsyncTask;

public class CloseRefreshTask extends AsyncTask<Void, Void, Void> {
	private PullToRefreshListView refreshView;
	
	public CloseRefreshTask(PullToRefreshListView refreshView){
		this.refreshView = refreshView;
	}
	
    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
    
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.refreshView.onRefreshComplete();
    }
}
