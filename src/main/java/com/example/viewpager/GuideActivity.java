package com.example.viewpager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();
    private LinearLayout liear;
    //    private  int current;
    private ImageView[] imageViews;
    private ImageView iv;
    private int mpointwith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            liear = (LinearLayout) findViewById(R.id.linear);
            initViews();
            viewPager.setOverScrollMode(viewPager.OVER_SCROLL_NEVER);
            viewPager.setAdapter(new myPagerAdapter());
            iv = (ImageView) findViewById(R.id.iv);
    }

    private void point() {
        imageViews = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            ImageView v = new ImageView(this);
            v.setBackgroundResource(R.drawable.bt1);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            if (i > 0) {
                params.leftMargin = 10;
            }
            v.setLayoutParams(params);
//            v.setImageResource(R.drawable.bt1);
            imageViews[i] = v;
            liear.addView(v);
//            imageViews[i]= (ImageView) liear.getChildAt(i);
            final int j = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(j);
                }
            });
        }

//        current=0;
//        imageViews[current].setImageResource(R.drawable.bt2);
    }

    private void initViews() {
        views.add(getLayoutInflater().inflate(R.layout.item1, null));
        views.add(getLayoutInflater().inflate(R.layout.item2, null));
        views.add(getLayoutInflater().inflate(R.layout.item3, null));
        views.add(getLayoutInflater().inflate(R.layout.item4, null));
        viewPager.setOnPageChangeListener(this);
        point();
        //得到两个小圆点之间的间距
        liear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                liear.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mpointwith = liear.getChildAt(1).getLeft() - liear.getChildAt(0).getLeft();
            }
        });
    }

    /**
     * 设置iv在组件RelativeLayout中距离左边距的边距
     *
     * @param position             第几个小圆点
     * @param positionOffset       偏移量
     * @param positionOffsetPixels ？？？？？
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int len = (int) (mpointwith * positionOffset) + position * mpointwith;//距离左侧的边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        params.leftMargin = len;
        iv.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
//        imageViews[position].setImageResource(R.drawable.bt2);
//        imageViews[current].setImageResource(R.drawable.bt1);
//        current=position;
        if (position == 3) {
            View v = views.get(position);
            Button btn = (Button) v.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    GuideActivity.this.startActivity(intent);
                    SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("name", "name");
                    editor.commit();
                    finish();
                }
            });
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class myPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
