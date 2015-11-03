package com.guo.duoduo.wifidetective.ui;


import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.receiver.WiFiBroadcastReceiver;
import com.guo.duoduo.wifidetective.util.NetworkUtil;
import com.guo.duoduo.wifidetective.util.ToastUtils;


public class WiFiScanActivity extends BaseActivity {

    private WiFiBroadcastReceiver mWiFiBroadcastReceiver = null;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        initWiFi();

    }

    private void initWiFi() {
        mWifiManager = NetworkUtil.getWifiManager(getApplicationContext());
        if (mWifiManager == null) {
            ToastUtils.showTextToast(getApplicationContext(), R.string.net_error);
            finish();
            return;
        }

        mWiFiBroadcastReceiver = new WiFiBroadcastReceiver(mWifiManager);
        IntentFilter intentFilter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mWiFiBroadcastReceiver, intentFilter);

        mWifiManager.startScan();
    }

    public void onDestroy() {
        super.onDestroy();

        try {
            if (mWiFiBroadcastReceiver != null)
                unregisterReceiver(mWiFiBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
}
