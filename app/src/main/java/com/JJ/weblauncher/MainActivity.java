package com.JJ.weblauncher;

import android.annotation.SuppressLint;
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

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
	
	static final String TAG = "MainActivity";
	public static int buttoncount = 0;
	public static boolean WiFiCheckerEnabled = false;
	private static int currentWebView = 0;
	SharedPreferences sharedPreferences;
	
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
		Log.d(TAG, "onCreate: " + sharedPreferences.getAll());
		
		//WebView Setup
		WebView[] webviews = {
				findViewById(R.id.webview1),
				findViewById(R.id.webview2),
		};
		for (int i = 0; i < webviews.length; i++) {
			WebView webView = webviews[i];
			setupWebView(webView, i + 1);
		}
		
		int startMode = sharedPreferences.getInt("StartMode", getResources().getInteger(R.integer.defaultStartMode));
		switchWebView(startMode);
		
		boolean keepScreenOn = sharedPreferences.getBoolean("KeepScreenOn", getResources().getBoolean(R.bool.defaultKeepScreenOn));
		View[] keepScreenOnViews = {
				webviews[0],
				webviews[1],
				findViewById(R.id.overlay),
		};
		for (View view : keepScreenOnViews) {
			view.setKeepScreenOn(keepScreenOn);
		}
		
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		
		final View decorView = getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
	protected void onResume() {
		super.onResume();
		final View decorView = getWindow().getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
		WebView webView1 = findViewById(R.id.webview1);
		WebView webView2 = findViewById(R.id.webview2);
		//button count: <0 is voldown and >0 is volup
		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (buttoncount < 0) buttoncount = 0;
				buttoncount++;
				switch (buttoncount) {
					case 1:
						switchWebView(1);
						break;
					default:
						switchToDefaultWebsite(webView1, 1);
						break;
				}
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (buttoncount > 0) buttoncount = 0;
				buttoncount--;
				switch (buttoncount) {
					case -1:
						switchWebView(2);
						break;
					default:
						switchToDefaultWebsite(webView2, 2);
						break;
				}
				break;
			case KeyEvent.KEYCODE_BACK:
				WebView webView = currentWebView == 1 ? webView1 : webView2;
				boolean backEnabled = sharedPreferences.getBoolean("WebView" + currentWebView + "back", getResources().getBoolean(getResources().getIdentifier("default" + "WebView" + currentWebView + "back", "bool", getPackageName())));
				if (webView.canGoBack() && backEnabled) {
					webView.goBack();
					return true;
				}
				break;
			default:
				break;
		}
		onWindowFocusChanged(true);
		if (Math.abs(buttoncount) >= MainActivity.this.getResources().getInteger(R.integer.paniccount)) {
			Log.i(TAG, "onKeyDown: Panic Mode activated, stopping weblauncher");
			buttoncount = 0;
			//not optimal solution
			//Intent intent = getIntent();
			finish();
			//startActivity(intent);
			// disabling restart because it creates a new activity instead restarting the current one
		}
		Log.d(TAG, "onKeyDown: buttoncount is " + buttoncount);
		return true;
	}
	
	private void switchWebView(int webviewid) {
		WebView webView1 = findViewById(R.id.webview1);
		WebView webView2 = findViewById(R.id.webview2);
		String webView1URL = sharedPreferences.getString("WebView" + 1 + "URL", getResources().getString(R.string.defaultWebView1URL));
		boolean webView1unload = sharedPreferences.getBoolean("WebView" + 1 + "unload", getResources().getBoolean(R.bool.defaultWebView1unload));
		boolean webView1enabled = sharedPreferences.getBoolean("WebView" + 1 + "enabled", getResources().getBoolean(R.bool.defaultWebView1enabled));
		String webView2URL = sharedPreferences.getString("WebView" + 2 + "URL", getResources().getString(R.string.defaultWebView2URL));
		boolean webView2unload = sharedPreferences.getBoolean("WebView" + 2 + "unload", getResources().getBoolean(R.bool.defaultWebView2unload));
		boolean webView2enabled = sharedPreferences.getBoolean("WebView" + 2 + "enabled", getResources().getBoolean(R.bool.defaultWebView2enabled));
		boolean webView1interact = sharedPreferences.getBoolean("WebView" + 1 + "interact", getResources().getBoolean(R.bool.defaultWebView1interact));
		boolean webView2interact = sharedPreferences.getBoolean("WebView" + 2 + "interact", getResources().getBoolean(R.bool.defaultWebView2interact));
		View overlay = findViewById(R.id.overlay);
		String unloadURL = getResources().getString(R.string.unloadURL);
		if (webviewid < 0 | webviewid > 2) return;
		if (webviewid == 1 & !webView1enabled) return;
		if (webviewid == 2 & !webView2enabled) return;
		int[][] visibilities = {
				{View.GONE, View.GONE},
				{View.VISIBLE, View.INVISIBLE},
				{View.INVISIBLE, View.VISIBLE},
		};
		webView1.setVisibility(visibilities[webviewid][0]);
		webView2.setVisibility(visibilities[webviewid][1]);
		switch (webviewid) {
			case 1:
				if (webView1unload) webView1.loadUrl(webView1URL);
				if (webView2unload) webView2.loadUrl(unloadURL);
				if (webView1interact) {
					overlay.setVisibility(View.GONE);
				} else {
					overlay.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				if (webView1unload) webView1.loadUrl(unloadURL);
				if (webView2unload) webView2.loadUrl(webView2URL);
				if (webView2interact) {
					overlay.setVisibility(View.GONE);
				} else {
					overlay.setVisibility(View.VISIBLE);
				}
				break;
			case 0:
			default:
				if (webView1unload) webView1.loadUrl(unloadURL);
				if (webView2unload) webView2.loadUrl(unloadURL);
				overlay.setVisibility(View.VISIBLE);
				break;
		}
		currentWebView = webviewid;
	}
	
	private void switchToDefaultWebsite(WebView webView, int id) {
		String url = sharedPreferences.getString("WebView" + id + "URL", getResources().getString(R.string.defaultWebView1URL));
		webView.loadUrl(url);
	}
	
	private void collectProperties() {
		String prefix = getResources().getString(R.string.buildpropprefix);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		for (int i = 1; i <= 2; i++) {
			String[] string_values = {
					"URL",
					"useragent"
			};
			for (String value : string_values) {
				String property = getSystemProperty(prefix + getResources().getString(getResources().getIdentifier("WebView" + i + "URL", "string", getPackageName())));
				editor.putString("WebView" + i + value, property != null ? property : getResources().getString(getResources().getIdentifier("default" + "WebView" + i + value, "string", getPackageName())));
			}
			String[] bool_values = {
					"enabled",
					"unload",
					"interact",
					"js",
					"cache",
					"haptic",
					"back"
			};
			for (String value : bool_values) {
				String property = getSystemProperty(prefix + getResources().getString(getResources().getIdentifier("WebView" + i + value, "string", getPackageName())));
				editor.putBoolean("WebView" + i + value, property != null ? Boolean.parseBoolean(property) : getResources().getBoolean(getResources().getIdentifier("default" + "WebView" + i + value, "bool", getPackageName())));
			}
		}
		String WiFiCheckerEnabledProp = getSystemProperty(prefix + getResources().getString(R.string.WiFiAlwaysOn));
		WiFiCheckerEnabled = WiFiCheckerEnabledProp != null ? Boolean.parseBoolean(WiFiCheckerEnabledProp) : getResources().getBoolean(R.bool.defaultWiFiAlwaysOn);
		String startMode = getSystemProperty(prefix + getResources().getString(R.string.StartMode));
		editor.putInt("StartMode", startMode != null ? Integer.parseInt(startMode) : getResources().getInteger(R.integer.defaultStartMode));
		String keepScreenOn = getSystemProperty(prefix + getResources().getString(R.string.KeepScreenOn));
		editor.putBoolean("keepScreenOn", keepScreenOn != null ? Boolean.parseBoolean(keepScreenOn) : getResources().getBoolean(R.bool.defaultKeepScreenOn));
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
		} catch (Exception ignored) {
		} //Do nothing - can't get property value
		propertyValue = "".equals(propertyValue) ? null : propertyValue;
		return propertyValue;
	}
	
	private void setupWebView(WebView webView, int id) {
		if (id < 1 | id > 2) return;
		boolean enabled = sharedPreferences.getBoolean("WebView" + id + "enabled", getResources().getBoolean(getResources().getIdentifier("default" + "WebView" + id + "enabled", "bool", getPackageName())));
		boolean js = sharedPreferences.getBoolean("WebView" + id + "js", getResources().getBoolean(getResources().getIdentifier("default" + "WebView" + id + "js", "bool", getPackageName())));
		boolean cache = sharedPreferences.getBoolean("WebView" + id + "cache", getResources().getBoolean(getResources().getIdentifier("default" + "WebView" + id + "cache", "bool", getPackageName())));
		boolean haptic = sharedPreferences.getBoolean("WebView" + id + "haptic", getResources().getBoolean(getResources().getIdentifier("default" + "WebView" + id + "haptic", "bool", getPackageName())));
		String url = sharedPreferences.getString("WebView" + id + "URL", getResources().getString(getResources().getIdentifier("default" + "WebView" + id + "URL", "string", getPackageName())));
		String useragent = sharedPreferences.getString("WebView" + id + "useragent", getResources().getString(getResources().getIdentifier("default" + "WebView" + id + "useragent", "string", getPackageName())));
		if (enabled) setupWebView(webView, url, js, cache, haptic, useragent);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void setupWebView(WebView webView, String url, boolean jsenabled, boolean cacheenabled, boolean hapticfeedbackenabled, String useragent) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(jsenabled);
		webView.setWebViewClient(new WebViewClient());
		webSettings.setAppCacheEnabled(cacheenabled);
		webSettings.setCacheMode(cacheenabled ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_NO_CACHE);
		if (!useragent.isEmpty()) webSettings.setUserAgentString(useragent);
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
}
