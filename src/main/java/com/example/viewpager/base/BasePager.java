package com.example.viewpager.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.viewpager.MainActivity;
import com.example.viewpager.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 五个子页面的基类
 * Created by Administrator on 2016/6/14 0014.
 */
public class BasePager {
    public int count1=0;
    public ImageButton btnMenu;
    public Activity mActivity;
    public View mRootView;//布局界面
    public TextView tvTitle;
    public FrameLayout flLayout;

    public BasePager(Activity activity) {
        mActivity = activity;
        initViews();
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.basepager, null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        flLayout = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     */
    private void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
    }
    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * @param enable  设置侧边框能不能滑动
     */
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi= (MainActivity) mActivity;
        SlidingMenu slidingMenu=mainUi.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
