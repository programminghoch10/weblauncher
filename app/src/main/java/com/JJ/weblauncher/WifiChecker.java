package com.JJ.weblauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class WifiChecker extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifi.getWifiState();
        Log.i(TAG, "onReceive: Got Wifi State: "+state);
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

}
