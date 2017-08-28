package com.mieasy.whrt_app_android_4.act.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mieasy.whrt_app_android_4.R;

/**
 * Created by alan on 16-12-26.
 */
public class WebViewActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview_activity);

        WebView  tv_webview =(WebView)findViewById(R.id.tv_webview);
        if (tv_webview != null) {

            WebSettings webSettings = tv_webview.getSettings();
            //设置WebView属性，能够执行Javascript脚本
            webSettings.setJavaScriptEnabled(true);
            //设置可以访问文件
            webSettings.setAllowFileAccess(true);
            //设置支持缩放
            webSettings.setBuiltInZoomControls(true);
            //加载需要显示的网页
            tv_webview.loadUrl("http://www.whrt.gov.cn");
            //设置Web视图
            tv_webview.setWebViewClient(new webViewClient());

        }
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
