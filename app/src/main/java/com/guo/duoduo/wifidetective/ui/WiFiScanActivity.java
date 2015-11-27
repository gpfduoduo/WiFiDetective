package com.guo.duoduo.wifidetective.ui;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.core.wifiscan.WiFiBroadcastReceiver;
import com.guo.duoduo.wifidetective.entity.RouterInfo;
import com.guo.duoduo.wifidetective.entity.RouterList;
import com.guo.duoduo.wifidetective.ui.adapter.WiFiScanAdapter;
import com.guo.duoduo.wifidetective.util.Constant;
import com.guo.duoduo.wifidetective.util.NetworkUtil;
import com.guo.duoduo.wifidetective.ui.view.StatusBarCompat;
import com.guo.duoduo.wifidetective.util.ToastUtils;


public class WiFiScanActivity extends BaseActivity
{

    private static final String tag = WiFiScanActivity.class.getSimpleName();
    private WiFiBroadcastReceiver mWiFiBroadcastReceiver = null;
    private WifiManager mWifiManager;
    private WiFiScanHandler mWiFiScanHandler;

    private RecyclerView mScanRecycleView;
    private WiFiScanAdapter mWiFiScanAdapter;
    private ArrayList<RouterInfo> mRouterInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);

        StatusBarCompat.compat(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        mWiFiScanHandler = new WiFiScanHandler(this);
        initWiFi();

        initWidget();
    }

    private void initWiFi()
    {
        mWifiManager = NetworkUtil.getWifiManager(getApplicationContext());
        if (mWifiManager == null)
        {
            Log.d(tag, "wifi error");
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

    private void initWidget()
    {
        mScanRecycleView = (RecyclerView) findViewById(R.id.wifi_scan_list);
        mWiFiScanAdapter = new WiFiScanAdapter(this, mRouterInfo);
        mScanRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mScanRecycleView.setAdapter(mWiFiScanAdapter);
    }

    public void onDestroy()
    {
        super.onDestroy();

        try
        {
            if (mWiFiBroadcastReceiver != null)
            {
                unregisterReceiver(mWiFiBroadcastReceiver);
            }
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    public static class WiFiScanHandler extends android.os.Handler
    {

        private WeakReference<WiFiScanActivity> mWifiScanActivity;

        public WiFiScanHandler(WiFiScanActivity activity)
        {
            mWifiScanActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            WiFiScanActivity activity = mWifiScanActivity.get();
            if (activity == null)
                return;

            switch (msg.what)
            {
                case Constant.MSG.WIFI_SCAN_RESULT :
                {
                    if (msg.obj != null)
                    {
                        activity.mRouterInfo.clear();
                        activity.mWiFiScanAdapter.notifyDataSetChanged();
                        SparseArray<RouterList> routerListSparseArray = (SparseArray<RouterList>) msg.obj;
                        for (int i = 0; i < routerListSparseArray.size(); i++)
                        {
                            activity.mRouterInfo
                                    .addAll(routerListSparseArray.valueAt(i).mRouterList);
                        }
                    }
                    activity.mWiFiScanAdapter.notifyDataSetChanged();
                    activity.mWifiManager.startScan();//处理后冲洗扫描
                    break;
                }
            }

        }
    }
}
