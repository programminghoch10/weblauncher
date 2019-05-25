package com.JJ.weblauncher;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    static String StartPage1 = "http://google.de";
    static String StartPage2 = "http://example.com";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String Page1 = getSystemProperty("JJ.Page1");
        if (Page1 == null) {Page1 = "";}
        if (Page1.equals("")) {
            Page1 = StartPage1;
        }

        WebView myWebView1 = findViewById(R.id.webview);
        myWebView1.loadUrl(Page1);
        WebSettings webSettings1 = myWebView1.getSettings();
        webSettings1.setJavaScriptEnabled(true);
        myWebView1.setWebViewClient(new WebViewClient());
        webSettings1.setAppCacheEnabled(false);
        webSettings1.setCacheMode(WebSettings.LOAD_NO_CACHE);

        WebView myWebView2 = findViewById(R.id.webview2);
        myWebView2.loadUrl("about:blank");
        WebSettings webSettings2 = myWebView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);
        myWebView2.setWebViewClient(new WebViewClient());
        webSettings2.setAppCacheEnabled(false);
        webSettings2.setCacheMode(WebSettings.LOAD_NO_CACHE);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView myWebView1 = findViewById(R.id.webview);
        WebView myWebView2 = findViewById(R.id.webview2);
        String Page1 = getSystemProperty("JJ.Page1");
        String Page2 = getSystemProperty("JJ.Page2");
        if (Page1 == null) {Page1 = "";}
        if (Page2 == null) {Page2 = "";}
        if (Page1.equals("")) {
            Page1 = StartPage1;
        }
        if (Page2.equals("")) {
            Page2 = StartPage2;
        }
        Log.i("weblauncher", "onKeyDown: Page1: "+Page1);
        Log.i("weblauncher", "onKeyDown: Page2: "+Page2);

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            if(myWebView2.isShown() || myWebView2.getUrl().equals("about:blank")) {
                myWebView2.loadUrl(Page2);
            }
            myWebView2.setVisibility(View.VISIBLE);
            myWebView1.setVisibility(View.INVISIBLE);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(myWebView1.isShown()) {
                myWebView1.loadUrl(Page1);
            }
            myWebView1.setVisibility(View.VISIBLE);
            myWebView2.setVisibility(View.INVISIBLE);
            myWebView2.loadUrl("about:blank");
            return true;
        }
        /*
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView2.canGoBack()) {
            myWebView2.goBack();
            return true;
        }
        */
        onWindowFocusChanged(true);
        return true;
    }

    private String getSystemProperty(String propertyName) {
        String propertyValue = null;
        try {
            Process getPropProcess = Runtime.getRuntime().exec("getprop " + propertyName);
            BufferedReader osRes =
                    new BufferedReader(new InputStreamReader(getPropProcess.getInputStream()));
            propertyValue = osRes.readLine();
            osRes.close();
        } catch (Exception e) {
            // Do nothing - can't get property value
        }
        return propertyValue;
    }
}
