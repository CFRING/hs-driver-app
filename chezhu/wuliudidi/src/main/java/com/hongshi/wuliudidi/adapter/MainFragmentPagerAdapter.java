package com.hongshi.wuliudidi.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;
 
    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }
 
    @Override
    public int getCount() {
        return fragmentsList.size();
    }
 
    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }
 
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
 
}