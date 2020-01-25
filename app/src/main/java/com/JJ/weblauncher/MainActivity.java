package com.JJ.weblauncher;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
	
	SharedPreferences sharedPreferences;
	
	public static int buttoncount = 0;
	static final String TAG = "MainActivity";
	public static boolean WiFiCheckerEnabled = false;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
				WindowManager.LayoutParams.FLAG_SECURE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//collect webview properties
		sharedPreferences = getSharedPreferences(MainActivity.this.getString(R.string.sharedpreferencesfilename), MODE_PRIVATE);
		collectProperties();
		
		//WebView Setup
		WebView[] webviews = {
				findViewById(R.id.webview1),
				findViewById(R.id.webview2),
		};
        for (int i = 0; i < webviews.length; i++) {
            WebView webView = webviews[i];
            setupWebView(webView, i);
        }
		
		setWebViewVisibilities(1);
  
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		
		final View decorView = getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					decorView.setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE
									| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
									| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_FULLSCREEN
									| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
				}
			}
		});
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		final View decorView = getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					decorView.setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE
									| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
									| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_FULLSCREEN
									| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
				}
			}
		});
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
		WebView myWebView1 = findViewById(R.id.webview1);
		WebView myWebView2 = findViewById(R.id.webview2);
		String Page1 = sharedPreferences.getString(getResources().getString(R.string.WebView1URL), getResources().getString(R.string.defaultWebView1URL));
		String Page2 = sharedPreferences.getString(getResources().getString(R.string.WebView2URL), getResources().getString(R.string.defaultWebView2URL));
		//Log.i("weblauncher", "onKeyDown: Page1: "+Page1);
		//Log.i("weblauncher", "onKeyDown: Page2: "+Page2);
		
		//button count: <0 is voldown and >0 is volup
		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (buttoncount > 0) buttoncount = 0;
				buttoncount--;
				switch (buttoncount) {
					default:
						setWebViewVisibilities(2);
						break;
					case -2:
						myWebView2.loadUrl(Page2);
						break;
					case -10:
						finish();
						break;
				}
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (buttoncount < 0) buttoncount = 0;
				buttoncount++;
				switch (buttoncount) {
					default:
						setWebViewVisibilities(1);
						myWebView2.loadUrl("about:blank");
						break;
					case 2:
						myWebView1.loadUrl(Page1);
						break;
				}
				break;
			case KeyEvent.KEYCODE_BACK:
				//currently unused
				/*
				if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView2.canGoBack()) {
				   myWebView2.goBack();
				   return true;
				}
				*/
			default:
				onWindowFocusChanged(true);
				break;
		}
		if (Math.abs(buttoncount) >= MainActivity.this.getResources().getInteger(R.integer.paniccount)) {
			buttoncount = 0;
			//not optimal solution
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		Log.d(TAG, "onKeyDown: buttoncount is " + buttoncount);
		return true;
	}
	
	public void setWebViewVisibilities(int webviewid) {
	    //TODO: load and unload pages
		if (webviewid < 0 | webviewid > 2) return;
		int[][] visibiltys = {
				{View.GONE, View.GONE},
				{View.VISIBLE, View.INVISIBLE},
				{View.INVISIBLE, View.VISIBLE},
		};
		findViewById(R.id.webview1).setVisibility(visibiltys[webviewid][0]);
		findViewById(R.id.webview2).setVisibility(visibiltys[webviewid][1]);
	}
	
	private void collectProperties() {
		String prefix = getResources().getString(R.string.buildpropprefix);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		for (int i = 1; i <= 2; i++) {
			String url = getSystemProperty(prefix + getResources().getString(getResources().getIdentifier("WebView" + i + "URL", null, null)));
			editor.putString("WebView" + i + "URL", url != null ? url : getResources().getString(getResources().getIdentifier("default" + "WebView" + i + "URL", null, null)));
			
			String[] values = {
					"unload",
					"js",
					"cache",
					"haptic",
					"back",
			};
			for (String value: values) {
				String property = getSystemProperty(prefix + getResources().getString(getResources().getIdentifier("WebView" + i + value, null, null)));
				editor.putBoolean("WebView" + i + value, property != null ? Boolean.parseBoolean(property) : getResources().getBoolean(getResources().getIdentifier("default" + "WebView" + i + value, null, null)));
			}
		}
		String WiFiCheckerEnabledProp = getSystemProperty(prefix + getResources().getString(R.string.WiFiAlwaysOn));
		WiFiCheckerEnabled = WiFiCheckerEnabledProp != null ? Boolean.parseBoolean(WiFiCheckerEnabledProp) : getResources().getBoolean(R.bool.defaultWiFiAlwaysOn);
		editor.apply();
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
    
    void setupWebView(WebView webView, int id) {
        //TODO: read values from defaults and preferences manager and depending on id
        boolean js = true;
        boolean cache = false;
        boolean hapticFeedback = false;
        String url = sharedPreferences.getString("WebView" + id + "URL", getResources().getString(getResources().getIdentifier("default" + "WebView" + id + "URL", null, null)));
        setupWebView(webView, url, js, cache, hapticFeedback);
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(WebView webView, String url, boolean jsenabled, boolean cacheenabled, boolean hapticfeedbackenabled) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(jsenabled);
        webView.setWebViewClient(new WebViewClient());
        webSettings.setAppCacheEnabled(cacheenabled);
        webSettings.setCacheMode(cacheenabled ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_NO_CACHE);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(hapticfeedbackenabled);
        webView.loadUrl(url);
    }
    
    void setwebviewpage(WebView webView, String url) {
        webView.loadUrl(url);
    }
}
