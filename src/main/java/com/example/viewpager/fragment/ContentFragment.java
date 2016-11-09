package com.example.viewpager.fragment;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.viewpager.R;
import com.example.viewpager.base.BasePager;
import com.example.viewpager.base.impl.Government;
import com.example.viewpager.base.impl.HomePager;
import com.example.viewpager.base.impl.NewsPager;
import com.example.viewpager.base.impl.Setting;
import com.example.viewpager.base.impl.SmartService;

import java.util.ArrayList;

/**
 * 主页面Fragment
 * Created by Administrator on 2016/2/4 0004.
 */
public class ContentFragment extends BaseFragment {
    private RadioGroup rgGroup;
    private ViewPager mViewPager;
    private ArrayList<BasePager> mPagerlist;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        rgGroup = (RadioGroup) view.findViewById(R.id.rgGroup);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
        mViewPager.setOverScrollMode(mViewPager.OVER_SCROLL_NEVER);//去掉两侧的光晕效果
        return view;
    }

    /**
     * 初始化子界面数据
     */
    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);//默认勾选的首页
        mPagerlist = new ArrayList<BasePager>();
        mPagerlist.add(new HomePager(mActivity));
        mPagerlist.add(new NewsPager(mActivity));
        mPagerlist.add(new SmartService(mActivity));
        mPagerlist.add(new Government(mActivity));
        mPagerlist.add(new Setting(mActivity));

        mViewPager.setAdapter(new ContentAdapter());
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
//                        mViewPager.setCurrentItem(0);//设置当前页面，有切换效果
                        mViewPager.setCurrentItem(0, false);//设置当前页面，去掉切换效果
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smartService:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_government:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });
        mPagerlist.get(0).initData();//手动初始化第一个页面
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

//                System.out.println("主界面");


                mPagerlist.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagerlist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerlist.get(position);
            container.addView(pager.mRootView);
//            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public NewsPager getNewsCenterPager() {
        if (mPagerlist == null) {
            Toast.makeText(mActivity, mActivity.getString(R.string.CheckIntnet), Toast.LENGTH_LONG).show();
            return null;
        } else {
            return (NewsPager) mPagerlist.get(1);
        }
    }
}