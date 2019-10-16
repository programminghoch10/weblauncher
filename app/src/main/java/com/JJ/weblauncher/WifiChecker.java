package com.JJ.weblauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class WifiChecker extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {
		try {
			PackageManager localPackageManager = context.getPackageManager();
			Intent homeintent = new Intent("android.intent.action.MAIN");
			homeintent.addCategory("android.intent.category.HOME");
			String str = localPackageManager.resolveActivity(homeintent, PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
			//Log.d("WIFIChecker", "Current launcher Package Name: " + str);
			//Log.d("WIFIChecker", "Current weblauncher Package Name: " + this.getClass().getPackage().getName());
			if (str.equals(this.getClass().getPackage().getName())) {
				WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				int state = wifi.getWifiState();
				//Log.d(TAG, "onReceive: Got Wifi State: "+state);
				switch(state) {
					case WifiManager.WIFI_STATE_DISABLED:
						Log.i(TAG, "onReceive: Wifi disabled, enabling now!");
						wifi.setWifiEnabled(true);
						break;
					case WifiManager.WIFI_STATE_ENABLED:
					case WifiManager.WIFI_STATE_DISABLING:
					case WifiManager.WIFI_STATE_ENABLING:
					default:
						break;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "onReceive: There was an error during Wifi checking process ", e);
		}
	}
	
}
