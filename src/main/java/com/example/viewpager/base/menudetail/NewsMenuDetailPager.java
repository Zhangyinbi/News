package com.example.viewpager.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.viewpager.MainActivity;
import com.example.viewpager.R;
import com.example.viewpager.base.BaseMenuDetailPager;
import com.example.viewpager.base.TabDetailPager;
import com.example.viewpager.demain.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {
    private ArrayList<TabDetailPager> mPagerList;
    private ViewPager mViewPager;
    private ArrayList<NewsData.NewsMenuData.NewsTabData> mNewsTabData;//也签的网络数据
    private TabPageIndicator indicator;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsMenuData.NewsTabData> list) {
        super(activity);
        mNewsTabData = list;
    }

    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
        //初始化自定义控件TabPageIndicator
        ViewUtils.inject(this,view);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setOverScrollMode(mViewPager.OVER_SCROLL_NEVER);//去掉ViewPager两边的光晕效果

        //当ViewPager和TabPageIndicator合用的时候  要设置TabPageIndicator的滑动事件
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                MainActivity mainUi= (MainActivity) mActivity;
                SlidingMenu slidingMenu=  mainUi.getSlidingMenu();
                if (position==0){
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else{
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }


    /**
     * @param view    采用xutils的事件注册相应方法来实现按钮的监听
     *                最后ViewPagerIndicator有个事件拦截  所以要重写源代码中的TabPagerIndicator中的一个
     *                方法，名称是dispatchTouchEvent方法
     */
    @OnClick(R.id.btn_next)
    public void nextPage(View view){
        int currentItem=mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }
    @Override
    public void initData() {

        mPagerList = new ArrayList<TabDetailPager>();
        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabData);
            mPagerList.add(pager);
        }
        mViewPager.setAdapter(new MenuDetailAdapter());
        indicator.setViewPager(mViewPager);//讲ViewPager和indicator关联起来必须在mViewpager设置玩数据之后才能调用

    }

    class MenuDetailAdapter extends PagerAdapter {

        /**
         * @param position  重写此方法，用于ViewPagerIndicator的页签显示
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).name;
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
//            Log.e("初始化主界面---", "点击"+position );
            pager.setPosition(position);
            pager.initData();//数据初始化，可以优化，放置于不同的地方
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
