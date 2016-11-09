package com.example.viewpager.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.viewpager.base.BasePager;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        btnMenu.setVisibility(View.GONE);
        tvTitle.setText("大视野");
        TextView text=new TextView(mActivity);
        text.setText("首页");
        text.setTextColor(Color.RED);
        text.setTextSize(30);
        text.setGravity(Gravity.CENTER);

        flLayout.addView(text);
        setSlidingMenuEnable(false);

    }
}
