package com.example.viewpager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.viewpager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class RefreshListView extends ListView implements OnItemClickListener {
    private View mHeaderView;
    int startY = -1;
    int mHeaderViewheight;
    private static final int STATE_PULL_REFRESH = 0;//下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;//松开刷新
    private static final int STATE_REFRESHING = 2;//正在刷新
    private int mCurrentState = STATE_PULL_REFRESH;//当前的状态
    ProgressBar pbProgress;
    ImageView ivArrow;
    TextView tvTime;
    TextView tvTitle;
    private RotateAnimation animDown;
    private RotateAnimation animUp;
    private View mFooterView;
    private int mFooterViewHeight;


    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    private boolean isLondingMore;

    public void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        this.addFooterView(mFooterView);
        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    if (getLastVisiblePosition() == getCount() - 1 && !isLondingMore) {//滑到最后
                        mFooterView.setPadding(0, 0, 0, 0);
                        setSelection(getCount());//改变listView的显示位置
                        isLondingMore = true;
                        if (mListener != null) {
                            mListener.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }


    private void initHeaderView() {

        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
        this.addHeaderView(mHeaderView);
        mHeaderView.measure(0, 0);
        mHeaderViewheight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewheight, 0, 0);//隐藏头布局
        initArrowAnim();

        tvTime.setText("最后刷新时间" + getCurrentTime());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getRawY();
                }
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;
                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    int padding = dy - mHeaderViewheight;
                    mHeaderView.setPadding(0, padding, 0, 0);
                    if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {//状态改为松开刷新
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding <= 0 && mCurrentState != STATE_PULL_REFRESH) {//状态为下拉刷新
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    mHeaderView.setPadding(0, 0, 0, 0);//正常显示
                } else if (mCurrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewheight, 0, 0);//隐藏
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的样式
     */
    private void refreshState() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        String str = formatter.format(calendar.getTime());
        switch (mCurrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(VISIBLE);
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(VISIBLE);
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();
                ivArrow.setVisibility(INVISIBLE);
                pbProgress.setVisibility(VISIBLE);
                if (mListener != null) {
                    mListener.onRefresh();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        //箭头向上动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(300);
        animUp.setFillAfter(true);
        //箭头向下动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(300);
        animDown.setFillAfter(true);
    }

    OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    OnItemClickListener mItemClickListener;

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }

    }


    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * 关闭刷新的控件
     */
    public void OnRefreshComplete(boolean boo) {
        if (isLondingMore) {//正在加载更多
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
            isLondingMore = false;
        } else {
            mCurrentState = STATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");
            ivArrow.setVisibility(VISIBLE);
            pbProgress.setVisibility(INVISIBLE);
            mHeaderView.setPadding(0, -mHeaderViewheight, 0, 0);
            if (boo) {
                tvTime.setText("最后刷新时间" + getCurrentTime());
            }
        }
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-HH-dd  HH-mm-ss");
        return format.format(new Date());
    }
}
