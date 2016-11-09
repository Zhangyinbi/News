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
public class Setting extends BasePager {
    public Setting(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        btnMenu.setVisibility(View.GONE);
        tvTitle.setText("设置");
        TextView text=new TextView(mActivity);
        text.setText("设置");
        text.setTextColor(Color.RED);
        text.setTextSize(30);
        text.setGravity(Gravity.CENTER);

        flLayout.addView(text);

        setSlidingMenuEnable(false);
    }
}
