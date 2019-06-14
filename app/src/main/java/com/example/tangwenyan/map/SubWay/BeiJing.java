package com.example.tangwenyan.map.SubWay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.tangwenyan.map.R;

public class BeiJing extends AppCompatActivity {
    private static final String TAG = "activiity_subway";

    private WebView myWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        myWebView = (WebView)findViewById(R.id.webview1);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());

        myWebView.loadUrl("http://subway.lyxueit.com/BeiJing.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_BACK) {

            if (myWebView.canGoBack()) {
                myWebView.goBack();
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

}
