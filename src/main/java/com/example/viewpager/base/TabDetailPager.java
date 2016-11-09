package com.example.viewpager.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viewpager.NewsDetailActivity;
import com.example.viewpager.R;
import com.example.viewpager.Utils.PrefUtils;
import com.example.viewpager.demain.NewsData;
import com.example.viewpager.demain.TabData;
import com.example.viewpager.global.GlobalContants;
import com.example.viewpager.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    ArrayList<TabData.TabDataile.TapDataConteng.ChildContent> mNewsList;

    ArrayList<NewsData.NewsMenuData.NewsTabData> channelList;
    TabData mTabDetailData;
    private ViewPager mViewpager;
    private String mUrl;
    private ArrayList<ImageView> mImagelist;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    private ArrayList<String> mTopNewsList;
    private CirclePageIndicator mIndicator;
    private int position;
    private RefreshListView lvList;
    private ProgressBar pbBar;
    private String mMoreUrl;
    private NewsAdapter newsAdapter;

    public TabDetailPager(Activity activity, ArrayList<NewsData.NewsMenuData.NewsTabData> newsTabData) {
        super(activity);
        channelList = newsTabData;
        mUrl = GlobalContants.NEWS_TAB_URL;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    @Override
    protected View initViews() {
      /*tvText=new TextView(mActivity);
        tvText.setGravity(Gravity.CENTER);
        tvText.setTextSize(29);
        tvText.setText("页签详情页");
        tvText.setTextColor(Color.RED);*/
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        pbBar = (ProgressBar) view.findViewById(R.id.pb_progress);
        View header = View.inflate(mActivity, R.layout.list_header_topnews, null);//加载头布局
        ViewUtils.inject(this, header);


//        tvTitle = (TextView) header.findViewById(R.id.tv_title);
        lvList = (RefreshListView) view.findViewById(R.id.lv_list);
        lvList.addHeaderView(header);
        lvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {//下拉刷新监听

            @Override
            public void onRefresh() {
                getDataFromService();
            }
            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromService();
                } else {
                    Toast.makeText(mActivity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                    lvList.OnRefreshComplete(false);//
                }
            }
        });


        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

        mViewpager = (ViewPager) view.findViewById(R.id.vp_news);
        mViewpager.setOverScrollMode(mViewpager.OVER_SCROLL_NEVER);//没起作用


        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //浏览记录保存在本地
                String ids = PrefUtils.getString(mActivity, "read_ids", "");
                //已经保存过的  不再重复保存
                if (!ids.contains(mNewsList.get(position).title)){
                    ids=ids+mNewsList.get(position).title+",";
                    PrefUtils.setString(mActivity,"read_ids",ids);
                }
//                newsAdapter.notifyDataSetChanged();这个是为了重置ListView  但是性能较差
                changReadState(view);//实现局部刷新
                Intent intent=new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("link",mNewsList.get(position).link);//直接跳转网页，因为是新闻link不是url链接
//                intent.putExtra("link","http://world.gmw.cn/newspaper/2015-03/17/content_105205337.htm");
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    private void changReadState(View view) {
        TextView tTitle = (TextView) view.findViewById(R.id.tv_title);
        tTitle.setTextColor(Color.GRAY);
    }

    private void getMoreDataFromService() {
        new Thread() {
            @Override
            public void run() {
                Message mes;
                try {
                    URL url = new URL(mUrl + mMoreUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(2000);
                    int code = conn.getResponseCode();
                    byte[] by = new byte[1024];
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        int len = 0;
                        while ((len = is.read(by)) != -1) {
                            bos.write(by, 0, len);
                        }
                        String result = new String(bos.toByteArray());
                        mes = Message.obtain();
                        mes.what = 3;
                        mes.obj = result;
                        mhander.sendMessage(mes);
                        is.close();
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public void initData() {

        //得到网络图片
        getDataFromService();
    }

    private void getDataFromService() {
       /* //网络获取图片资源，本来是动态的  现在设置为静态的  因为网络格式限制
        HttpUtils utiles= new HttpUtils();
        utiles.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.e("哈哈哈哈哈哈啊哈哈哈哈哈哈哈",result );
                parseData(result);
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });*/
        new Thread() {
            @Override
            public void run() {
                Message mes;
                try {
//                    Log.e("初始化新闻LiseView---", "点击"+position );
                    Log.e("初始化新闻id---", "点击" + mUrl + "&" + channelList.get(position).channelId);
                    URL url = new URL(mUrl + "&channelId=" + channelList.get(position).channelId);
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
            if (msg.what == 3) {
                String result = (String) msg.obj;
                parseData(result,true);
                lvList.OnRefreshComplete(true);
                pbBar.setVisibility(View.INVISIBLE);
            } else if (msg.what == 0) {
                String result = (String) msg.obj;
                parseData(result,false);
                lvList.OnRefreshComplete(true);
                pbBar.setVisibility(View.INVISIBLE);
//                setCurrentMenuDetailPager(0);
            } else {
                //传递缓存数据  这个实在不联网的情况下需要调用另一个方法，给界面填充数据内容，读取缓存
//                paseData("null");
//                setCurrentMenuDetailPager(0);
                //没有网络服务的时候走的是这个程序
//                Log.e("哈哈哈哈哈哈啊哈哈哈哈哈哈哈","111111" );
                lvList.OnRefreshComplete(false);

                Toast.makeText(mActivity, mActivity.getString(R.string.CheckIntnet), Toast.LENGTH_LONG).show();
            }
        }
    };

    protected void parseData(String result,boolean boo) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        //给mMoreUrl重新赋值
        mMoreUrl = null;//应该冲网络中获取数据赋值给mMoreUrl1


        if (mTabDetailData == null) {
            return;
        }
        if (mTabDetailData.showapi_res_body == null) {
            return;
        }
        if (mTabDetailData.showapi_res_body.pagebean == null) {
            return;
        }
        if (!boo){
            mNewsList = mTabDetailData.showapi_res_body.pagebean.contentlist;

//        System.out.println("详情111" + mTabDetailData);         为何打印两次
            mTopNewsList = new ArrayList<String>();
            mTopNewsList.add("梦想家园");
            mTopNewsList.add("男人专属");
            mTopNewsList.add("最美微笑");
            mTopNewsList.add("绚丽人生");
            //给mImagelist设置数据  应该网络上获取的，这里设置静态的
            mImagelist = new ArrayList<ImageView>();
            ImageView view1 = new ImageView(mActivity);
            view1.setScaleType(ImageView.ScaleType.FIT_XY);//设置自动匹配父布局
            view1.setImageResource(R.drawable.imagefirst);
            ImageView view2 = new ImageView(mActivity);
            view2.setScaleType(ImageView.ScaleType.FIT_XY);
            view2.setImageResource(R.drawable.imagesecond);
            ImageView view3 = new ImageView(mActivity);
            view3.setScaleType(ImageView.ScaleType.FIT_XY);
            view3.setImageResource(R.drawable.imagethree);
            ImageView view4 = new ImageView(mActivity);
            view4.setScaleType(ImageView.ScaleType.FIT_XY);
            view4.setImageResource(R.drawable.imagefour);
            mImagelist.add(view1);
            mImagelist.add(view2);
            mImagelist.add(view3);
            mImagelist.add(view4);
            mViewpager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return mImagelist.size();
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    container.addView(mImagelist.get(position));
               /* BitmapUtils uTiles=new BitmapUtils(mActivity);
                uTiles.configDefaultLoadingImage(R.drawable.b);//设置默认加载的图片
                uTiles.display(new View(mActivity),url图片的Url地址);*/
                    return mImagelist.get(position);
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }
            });
            mIndicator.setViewPager(mViewpager);
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }


                @Override
                public void onPageSelected(int position) {
                    //网络上接收一个数据
                    tvTitle.setText(mTopNewsList.get(position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mIndicator.onPageSelected(0);
            mIndicator.setSnap(true);//快照显示,禁掉了滑动效果
            tvTitle.setText(mTopNewsList.get(0));
            newsAdapter = new NewsAdapter();
            lvList.setAdapter(newsAdapter);
        }else {//如果是加载下一页  需要追加
            ArrayList<TabData.TabDataile.TapDataConteng.ChildContent> news = mTabDetailData.showapi_res_body.pagebean.contentlist;
            mNewsList.addAll(news);
            newsAdapter.notifyDataSetChanged();//刷新ListView

        }




    }

    class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabDataile.TapDataConteng.ChildContent getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_data);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TabData.TabDataile.TapDataConteng.ChildContent item = getItem(position);
//            Log.e("重置数据", "数据设置" + item.title);
            holder.tvDate.setText(item.pubDate);
            holder.tvTitle.setText(item.title);
            String ids = PrefUtils.getString(mActivity, "read_ids", "");
            //判断这条新闻是否已经被看过
            if (ids.contains(getItem(position).title)){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
    }
}
