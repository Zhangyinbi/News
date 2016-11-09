package com.example.viewpager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private WebView mWebView;
    private ImageButton btnBack;
    private ImageButton btnShare;
    private ImageButton btnSize;
    private ProgressBar progressBar;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        mWebView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnSize = (ImageButton) findViewById(R.id.btn_size);
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnSize.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.pb_progress);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//让其支持JS  默认为false
        settings.setBuiltInZoomControls(true);//显示缩小放大按钮
        settings.setUseWideViewPort(true);//支持双击缩放

        mWebView.setWebViewClient(new WebViewClient() {
            /**
             * @param view   网页开始加载的时候的回调
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            /**
             * @param view   网页加载结束时候的回调
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }

            /**
             * @param view  所有的链接跳转都会在此方法中回调
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }
        });
//        mWebView.goBack();mWebView.goForward();往前一步往后一步

//        mWebView.loadUrl("http://www.yunhe.cn/");

        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * 进度发生变化
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println("进度————" + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 获取网页标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.loadUrl(link);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:

                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
        }
    }

    /**
     * 显示对话框
     */
    private int mCurrentChooseItem;//记录当前选择的item，点击确定之前
    private int mCurrentItem=2;//确定已经选择的item

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (mCurrentChooseItem) {
                    case 0:
//                        settings.setTextSize(WebSettings.TextSize.LARGER);//方法过时了
                        settings.setTextZoom(200);//Api版本14以上才可以用
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(75);
                        break;
                    case 4:
                        settings.setTextZoom(50);
                        break;
                    default:
                        break;
                }
                mCurrentItem=mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();

    }
}
