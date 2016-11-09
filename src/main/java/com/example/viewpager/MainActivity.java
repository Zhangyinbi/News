package com.example.viewpager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.example.viewpager.fragment.ContentFragment;
import com.example.viewpager.fragment.LeftMenuFragment;

/**
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_MAIN_CONTENT = "fragment_main_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);//设置侧边栏
        SlidingMenu slidingMenu = getSlidingMenu();//获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
        slidingMenu.setBehindOffset(260);//设置预留屏幕的宽度
        initFragment();

    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
      /*  FragmentManager f = getSupportFragmentManager();
        Log.e("哈哈哈", fm.toString());
//        android.app.FragmentManager f=getFragmentManager();
        Log.e("哈哈哈", f.toString());*/


        //开启事务
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //替换帧布局
        ft.replace(R.id.left_menu, new LeftMenuFragment(), FRAGMENT_LEFT_MENU);
        ft.replace(R.id.main_content, new ContentFragment(), FRAGMENT_MAIN_CONTENT);
        //提交事务
        ft.commit();

    }

    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return fragment;
    }
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_MAIN_CONTENT);
        return fragment;
    }

}