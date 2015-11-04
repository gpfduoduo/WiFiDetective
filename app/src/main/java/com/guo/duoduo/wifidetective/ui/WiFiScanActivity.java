package com.guo.duoduo.wifidetective.ui;


import java.lang.ref.WeakReference;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.core.wifiscan.WiFiBroadcastReceiver;
import com.guo.duoduo.wifidetective.util.Constant;
import com.guo.duoduo.wifidetective.util.NetworkUtil;
import com.guo.duoduo.wifidetective.util.ToastUtils;


public class WiFiScanActivity extends BaseActivity {

    private static final String tag = WiFiScanActivity.class.getSimpleName();
    private WiFiBroadcastReceiver mWiFiBroadcastReceiver = null;
    private WifiManager mWifiManager;
    private WiFiScanHandler mWiFiScanHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        mWiFiScanHandler = new WiFiScanHandler(this);
        initWiFi();
    }

    private void initWiFi() {
        mWifiManager = NetworkUtil.getWifiManager(getApplicationContext());
        if (mWifiManager == null) {
            ToastUtils.showTextToast(getApplicationContext(), R.string.net_error);
            finish();
            return;
        }

        mWiFiBroadcastReceiver = new WiFiBroadcastReceiver(mWifiManager, mWiFiScanHandler);
        IntentFilter intentFilter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mWiFiBroadcastReceiver, intentFilter);

        mWifiManager.startScan();
    }

    public void onDestroy() {
        super.onDestroy();

        try {
            if (mWiFiBroadcastReceiver != null) {
                unregisterReceiver(mWiFiBroadcastReceiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public static class WiFiScanHandler extends android.os.Handler {

        private WeakReference<WiFiScanActivity> mWifiScanActivity;

        public WiFiScanHandler(WiFiScanActivity activity) {
            mWifiScanActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WiFiScanActivity activity = mWifiScanActivity.get();
            if (activity == null)
                return;

            switch (msg.what) {
                case Constant.MSG.WIFI_SCAN_RESULT: {

                    activity.mWifiManager.startScan();//处理后冲洗扫描
                    break;
                }
            }

        }
    }
}
