package com.example.viewpager.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <--重写ViewPager中的dispatchTouchEven方法-->为也签页请求不要拦截往右的事件
 * Created by Administrator on 2016/6/17 0017.
 *
 * 过度用的  后来没有用到
 */
public class HorizonTalScrollViewPager extends ViewPager{

    public HorizonTalScrollViewPager(Context context) {
        super(context);
    }

    public HorizonTalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentItem()!=0){
            getParent().requestDisallowInterceptTouchEvent(true);
        }else {//如果是第一个Viewpager的界面，请求父控件拦截事件，自己消耗，不让往下面传递
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }

}
