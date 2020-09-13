package com.beige.camera.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.AppUtils;

@Route(path = PageIdentity.APP_WEBVIEW)
public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private ProgressBar progressBar;
    private ImageView icBack;
    private TextView tvTitle;


    @Autowired(name = "url")
    String url ;

    @Override
    protected void setupActivityComponent() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initViews() {
        mWebView = new WebView(AppUtils.getAppContext());
        FrameLayout frameLayout = findViewById(R.id.xwalk_view);
        progressBar = findViewById(R.id.web_progress);
        icBack = findViewById(R.id.ic_back);
        tvTitle = findViewById(R.id.tv_title);
        frameLayout.addView(mWebView);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoBack()){
                    mWebView.goBack();
                }else{
                    finish();
                }
            }
        });
    }

    @Override
    public void initData() {
    }

    private void loadUrl(){
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void configViews() {
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            }
        });

        mWebView.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);

        WebSettings webSettings = mWebView.getSettings();
// webview启用javascript支持 用于访问页面中的javascript
        webSettings.setJavaScriptEnabled(true);
//设置WebView缓存模式 默认断网情况下不缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//断网情况下加载本地缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//让WebView支持DOM storage API
        webSettings.setDomStorageEnabled(true);
//让WebView支持缩放
        webSettings.setSupportZoom(true);
//启用WebView内置缩放功能
        webSettings.setBuiltInZoomControls(true);
//让WebView支持可任意比例缩放
        webSettings.setUseWideViewPort(true);
//让WebView支持播放插件
        webSettings.setPluginState(WebSettings.PluginState.ON);
//设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上
        webSettings.setDisplayZoomControls(false);
//设置在WebView内部是否允许访问文件
        webSettings.setAllowFileAccess(true);
//设置WebView的访问UserAgent
//        webSettings.setUserAgentString(WebViewUtil.getUserAgent(getActivity(), webSettings));
//设置脚本是否允许自动打开弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

// 加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
// 开启Application H5 Caches 功能
        webSettings.setAppCacheEnabled(true);
// 设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");

        loadUrl();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen","是否有上一个页面:"+mWebView.canGoBack());
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            mWebView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_WEBVIEW;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
    }


    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen", "拦截url:" + url);
//            if(url.equals("http://www.google.com/")){
//                Toast.makeText(MainActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
//                return true;//表示我已经处理过了
//            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen", "网页标题:" + title);
            tvTitle.setText(title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };


}
