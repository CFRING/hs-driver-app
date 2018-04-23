package com.hongshi.wuliudidi.incomebook;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by abc on 2016/5/23.
 */
public class MyLayoutController {
    private List<ViewGroup> viewList;
    private int currentNum;
    private onViewChangeListener listener;

    public MyLayoutController(){
        viewList = new ArrayList<>();
        currentNum = -1;
    }

    public void setViewList(List<ViewGroup> viewList){
        if(viewList != null){
            this.viewList = viewList;
        }
        currentNum = -1;
    }

    public void setOnViewChangeListener(onViewChangeListener listener){
        this.listener = listener;
    }

    public int getCurrentNum(){
        return currentNum;
    }

    public void setCurrentNum(int i){
        if(viewList == null|| i < 0 || i >= viewList.size()){
            return;
        }

        for(int j = 0; j < viewList.size(); j++){
            viewList.get(j).setVisibility(View.GONE);
        }
        viewList.get(i).setVisibility(View.VISIBLE);
        currentNum = i;
        if(listener != null){
            listener.onViewSelected(currentNum);
        }
    }


    public interface onViewChangeListener{
        void onViewSelected(int i);
    }
}
