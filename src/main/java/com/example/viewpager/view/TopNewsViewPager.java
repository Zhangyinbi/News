package com.example.viewpager.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <--重写ViewPager中的dispatchTouchEven方法-->为也签页请求不要拦截往右的事件
 * Created by Administrator on 2016/6/17 0017.
 */
public class TopNewsViewPager extends ViewPager {
    int startX;
    int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param ev 滑动事件
     * @return 请求父控件是否拦截1：右划，如果是第一个页，需要父控件拦截
     * 2：左划，若果是最后一个，需要父控件拦截
     * 3：上下滑动，需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//不要拦截
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX=(int) ev.getRawX();
                int endY=(int) ev.getRawY();
                if (Math.abs(endX-startX)>Math.abs(endY-startY)){//左右话
                    if (endX>startX){//右划
                        if (getCurrentItem()==0){//第一个界面
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {//左划
                        if(getCurrentItem()==getAdapter().getCount()-1){//左后一个界面
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {//上下划
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
//        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
