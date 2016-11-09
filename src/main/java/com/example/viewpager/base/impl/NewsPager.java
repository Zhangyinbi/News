package com.example.viewpager.base.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.viewpager.R;
import com.example.viewpager.base.BaseMenuDetailPager;
import com.example.viewpager.base.BasePager;
import com.example.viewpager.base.menudetail.InteractMenuDstailPager;
import com.example.viewpager.base.menudetail.NewsMenuDetailPager;
import com.example.viewpager.base.menudetail.PhotoMenuDstailPager;
import com.example.viewpager.base.menudetail.TopicMenuDstailPager;
import com.example.viewpager.demain.NewsData;
import com.example.viewpager.fragment.LeftMenuFragment;
import com.example.viewpager.global.GlobalContants;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class NewsPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mPagers;//四个菜单详情页的集合
    private ArrayList<String> mMenuList;

    public NewsPager(Activity activity) {
        super(activity);
    }

    private LeftMenuFragment leftMenuFragment;



    @Override
    public void initData() {
//      Log.e("test",count1+"==");
        if (count1!=0){
            return;
        }
        count1++;
       /* tvTitle.setText("新闻");
        TextView text = new TextView(mActivity);
        text.setText("新闻中心");
        text.setTextColor(Color.RED);
        text.setTextSize(30);
        text.setGravity(Gravity.CENTER);
        flLayout.addView(text);*/

        //传递缓存数据  null必须是json格式的字符串，否则下面从网络获取数据会出异常
//        paseData("null");
        tvTitle.setText("新闻");
        getDataFromService();

        setSlidingMenuEnable(true);


    }

    /**
     * 从网络中获取数据
     */
    private void getDataFromService() {

       /* HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.NEWS_MENU_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.e("哈哈哈哈哈哈啊哈哈哈哈哈哈哈",result );
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
*/
        new Thread() {
            @Override
            public void run() {
                Message mes;
                try {
                    URL url = new URL(GlobalContants.NEWS_MENU_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(2000);
                    int code = conn.getResponseCode();
                    byte[] by = new byte[1024];
                    if (code == 200) {
//                        Log.e("哈哈哈哈哈哈啊哈哈哈哈哈哈哈", "2");
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        int len = 0;
                        while ((len = is.read(by)) != -1) {
                            bos.write(by, 0, len);
                        }
                        String result = new String(bos.toByteArray());
//                        Log.e("哈哈哈子线程", result);
//                        paseData(result);//解析网络返回的数据，json格式的
                        mes = Message.obtain();
                        mes.what = 0;
                        mes.obj = result;
                        mhander.sendMessage(mes);
                        is.close();
                        bos.close();
                    }
                } catch (IOException e) {
                    mes = Message.obtain();
                    mes.what = 1;
                    mhander.sendMessage(mes);
//                    Message  mes = Message.obtain();
//                    mes.what=1;
//                    mhander.sendMessage(mes);
//                    Toast.makeText(mActivity, mActivity.getString(R.string.CheckIntnet), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }.start();
    }
    final Handler mhander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String result = (String) msg.obj;
                paseData(result);
//                setCurrentMenuDetailPager(0);
            } else if (msg.what == 1) {
                //传递缓存数据  这个实在不联网的情况下需要调用另一个方法，给界面填充数据内容，读取缓存
//                paseData("null");
//                setCurrentMenuDetailPager(0);
                //没有网络服务的时候走的是这个程序
//                Log.e("哈哈哈哈哈哈啊哈哈哈哈哈哈哈","111111" );
                Toast.makeText(mActivity, mActivity.getString(R.string.CheckIntnet), Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * @param result 传的是一个json格式的字符串  如果选择网络获取，其他格式会出出一次
     */
    private void paseData(String result) {
     /*   //给侧边栏设置网络数据
        Gson gson = new Gson();
        NewsData data = gson.fromJson(result, NewsData.class);
        Log.e("哈哈哈哈哈", data.toString());
        MainActivity mainUi = (MainActivity) mActivity;
        leftMenuFragment = mainUi.getLeftMenuFragment();
        leftMenuFragment.setMenuData(data);*/
        //准备四个菜单页
        Gson gson = new Gson();
        NewsData data = gson.fromJson(result, NewsData.class);
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mActivity,data.showapi_res_body.channelList));
        mPagers.add(new TopicMenuDstailPager(mActivity));
        mPagers.add(new PhotoMenuDstailPager(mActivity));
        mPagers.add(new InteractMenuDstailPager(mActivity));
        setCurrentMenuDetailPager(0);
    }

    /**
     * @return 取得标题的文字，本来应该在网络数据data中提取
     * 这里从LsftLeftMenuFragment中提取
     */
    public void title() {
        mMenuList = new ArrayList<String>();
        mMenuList.add("新闻");
        mMenuList.add("专题");
        mMenuList.add("组图");
        mMenuList.add("互动");
    }

    public void setCurrentMenuDetailPager(int position) {
      // Log.e("test","begin"+mPagers);
        if (mPagers == null) {
            //mPagers为空的时候
//            Log.e("哈哈哈哈哈哈啊哈哈哈哈哈哈哈", "1112222222222111");
        } else {
            BaseMenuDetailPager pager = mPagers.get(position);//获取当前要显示的菜单详情
            flLayout.removeAllViews();
            title();
            tvTitle.setText(mMenuList.get(position));
            flLayout.addView(pager.mRootView);//讲菜单详情设置给帧布局
            pager.initData();//初始化当前页面的数据
        }
    }
}
