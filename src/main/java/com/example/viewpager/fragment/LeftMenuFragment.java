package com.example.viewpager.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viewpager.MainActivity;
import com.example.viewpager.R;
import com.example.viewpager.base.BasePager;
import com.example.viewpager.base.impl.NewsPager;
import com.example.viewpager.demain.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * 左侧菜单的Fragment
 * Created by Administrator on 2016/2/4 0004.
 */
public class LeftMenuFragment extends BaseFragment{
    private ListView lvList;
    public ArrayList<String> mMenuList;
    private int mCurrentPos;
    private MenuAdapter mMenuAdapter;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
        lvList= (ListView) view.findViewById(R.id.lv_list);
        return view;
    }

    @Override
    public void initData() {
        //给mMenuList设置数据和下面的setMenuData方法作用是一样的，setMenuData是给mMenuList设置网络数据
        mMenuList=new ArrayList<String>();
        mMenuList.add("新闻");
        mMenuList.add("专题");
        mMenuList.add("组图");
        mMenuList.add("互动");
        mMenuAdapter=new MenuAdapter();
        lvList.setAdapter(mMenuAdapter);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mMenuAdapter.notifyDataSetChanged();
//                Log.e("哈哈哈哈哈", " "+position );
                setCurrentMenuDetailPager(position);
                toggleSlidingMenu();
            }
        });
//        setCurrentMenuDetailPager(0);
    }

    /**
     * 切换SlidingMenu的状态
     */
    private void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
    }

    private void setCurrentMenuDetailPager(int position) {
//        System.out.println("点击也签");












        MainActivity mainUi= (MainActivity) mActivity;
        ContentFragment fragment= mainUi.getContentFragment();//获取主页面fragment
        NewsPager pager=fragment.getNewsCenterPager();//获取主页面
        pager.setCurrentMenuDetailPager(position);//设置当前菜单详情页
    }

    //设置数据
    public void setMenuData(NewsData data) {
        ArrayList<NewsData.NewsMenuData.NewsTabData> list=data.showapi_res_body.channelList;
        mMenuList=new ArrayList<String>();
        for (int i=0;i<list.size();i++){
            mMenuList.add(list.get(i).name);
        }
        lvList.setAdapter(new MenuAdapter());

    }


    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public String getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(mActivity,R.layout.list_menu_item,null);
            TextView tvTitle= (TextView) view.findViewById(R.id.tv_NewsMenu);
            String str=getItem(position);
            tvTitle.setText(str);
            if (mCurrentPos==position){
                tvTitle.setEnabled(true);
            }else{
                tvTitle.setEnabled(false);
            }
            return view;
        }
    }
}
























