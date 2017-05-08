package com.dealfaro.luca.listviewexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebActivity extends AppCompatActivity {
    WebView webView;

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.wtf("lv-ex", "url changed " + url);
            if (url.contains(Uri.parse(url).getHost())) {
                Log.wtf("lv-ex", "same host");
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site,
            // so launch another Activity that handles URLs
            Log.wtf("lv-ex", "not same host");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url"); // looks for url in the bundle
        Log.wtf("lv-ex", "url: " + url);
        Log.wtf("lv-ex", "uri: " + Uri.parse(url).getHost());
        webView = (WebView) findViewById(R.id.webview2);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Binds the Javascript interface
        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        webView.loadUrl(url);
        webView.loadUrl("javascript:alert(\"Hello\")");
    }

    public class JavaScriptInterface {
        Context mContext; // Having the context is useful for lots of things,
        // like accessing preferences.

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void myFunction(String args) {
        }
    }

    // proper back button in WebView
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {webView.goBack();}
                    else {finish();}
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
