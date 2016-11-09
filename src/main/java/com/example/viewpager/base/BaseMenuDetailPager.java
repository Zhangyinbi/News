package com.example.viewpager.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public abstract  class BaseMenuDetailPager {

    public Activity mActivity;
    public View mRootView;
    public BaseMenuDetailPager(Activity activity){
        mActivity=activity;
        mRootView=initViews();
    }

    protected abstract View initViews();
    public void initData(){
    }
}
