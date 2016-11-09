package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashActivity extends Activity{
    private ImageView rlRoot;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rlRoot = (ImageView) findViewById(R.id.rl_Root);//找到需要旋转的控件
        tv= (TextView) findViewById(R.id.tv_txt);
        startAnimation();//启动动漫（一张图片的旋转）
    }

    /**
     * 启动动漫
     */
    private void startAnimation() {

        //RotateAnimation 旋转动画效果
        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);//动画执行时间
        ra.setFillAfter(true);//动画执行完后保持最终状态


        //AlphaAnimation 透明度动画效果
        AlphaAnimation aa = new AlphaAnimation(0f, 1f);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        //ScaleAnimation 缩放动画效果
        ScaleAnimation sa = new ScaleAnimation(0f, 1f, 0f, 1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);

        //增加一个动漫的集合，把效果都添加进去
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(ra);
        set.addAnimation(aa);
        set.addAnimation(sa);

        //设置动画的监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 动漫开始的时候
             * @param animation
             */
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**
             * 动画结束
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                //跳转到引导界面
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                if (sp.getString("name", "").equals("name")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    finish();//结束当前页面
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlRoot.startAnimation(set);
        tv.startAnimation(set);
    }


}


/*
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private PagerTabStrip pager;
    private List<View> views = new ArrayList<View>();
    String[] str = {"item1", "item2", "item3", "item4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pager = (PagerTabStrip) findViewById(R.id.pager);
        initViews();
        viewPager.setAdapter(new myPagerAdapter());
    }

    private void initViews() {
        views.add(getLayoutInflater().inflate(R.layout.item1, null));
        views.add(getLayoutInflater().inflate(R.layout.item2, null));
        views.add(getLayoutInflater().inflate(R.layout.item3, null));
        views.add(getLayoutInflater().inflate(R.layout.item4, null));
        pager.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        pager.setTabIndicatorColor(getResources().getColor(android.R.color.holo_blue_bright));
        pager.setTextColor(Color.WHITE);
        viewPager.setOnPageChangeListener(this);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position==3){
            View v=views.get(position);
            Button btn= (Button) v.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,"position",Toast.LENGTH_LONG).show();
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
            View v=views.get(position);
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

        @Override
        public CharSequence getPageTitle(int position) {
            return str[position];
        }
    }
}
*/
