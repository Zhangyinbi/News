package com.example.viewpager.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.viewpager.base.BaseMenuDetailPager;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class PhotoMenuDstailPager extends BaseMenuDetailPager {
    public PhotoMenuDstailPager(Activity activity) {
        super(activity);
    }

    @Override
    protected View initViews() {
        TextView text = new TextView(mActivity);
        text.setText("菜单详情页——组图");
        text.setTextColor(Color.RED);
        text.setTextSize(30);
        text.setGravity(Gravity.CENTER);
        return text;
    }
}
