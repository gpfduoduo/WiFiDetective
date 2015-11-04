package com.guo.duoduo.wifidetective.core.wifiscan;


import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.guo.duoduo.wifidetective.entity.RouterInfo;
import com.guo.duoduo.wifidetective.entity.RouterList;
import com.guo.duoduo.wifidetective.ui.WiFiScanActivity;
import com.guo.duoduo.wifidetective.util.Constant;
import com.guo.duoduo.wifidetective.util.NetworkUtil;
import com.guo.duoduo.wifidetective.util.RouterComparator;


/**
 * Created by 郭攀峰 on 2015/11/3.
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver
{
    private static final String tag = WiFiBroadcastReceiver.class.getSimpleName();
    private WifiManager mWiFiManager;
    private List<ScanResult> mScanResult;
    private WiFiScanActivity.WiFiScanHandler mWiFiScanHandler;

    private SparseArray<RouterList> mRoutersInDifferentChannel;
    private RouterComparator mRouterComparator;

    public WiFiBroadcastReceiver(WifiManager wifiManager,
            WiFiScanActivity.WiFiScanHandler handler)
    {
        mWiFiManager = wifiManager;
        mWiFiScanHandler = handler;
        mRouterComparator = new RouterComparator();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent == null)
            return;

        if (intent.getAction() != WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            return;

        if (mWiFiManager != null)
        {
            mScanResult = mWiFiManager.getScanResults();
            if (mScanResult != null)
            {
                mRoutersInDifferentChannel = new SparseArray<>();

                for (ScanResult scanResult : mScanResult)
                {
                    RouterInfo routerInfo = new RouterInfo();
                    routerInfo.mSsid = scanResult.SSID;
                    routerInfo.mMac = scanResult.BSSID;
                    routerInfo.mChannel = NetworkUtil
                            .getChannelFromFrequency(scanResult.frequency);
                    routerInfo.mStrength = WifiManager.calculateSignalLevel(
                        scanResult.level, 5);

                    RouterList routerList = mRoutersInDifferentChannel
                            .get(routerInfo.mChannel);
                    if (routerList == null)
                    {
                        RouterList list = new RouterList();
                        list.mRouterList.add(routerInfo);
                        mRoutersInDifferentChannel.put(routerInfo.mChannel, list);
                    }
                    else
                    {
                        routerList.mRouterList.add(routerInfo);
                    }
                }
                sortResult();

                Message message = mWiFiScanHandler.obtainMessage(
                    Constant.MSG.WIFI_SCAN_RESULT, mRoutersInDifferentChannel);
                mWiFiScanHandler.sendMessage(message);
            }
        }
    }

    private void sortResult()
    {
        int key;
        Log.d(tag, "the channel number is " + mRoutersInDifferentChannel.size());

        for (int i = 0, length = mRoutersInDifferentChannel.size(); i < length; i++)
        {
            key = mRoutersInDifferentChannel.keyAt(i);
            RouterList routerList = mRoutersInDifferentChannel.get(key);
            Log.d(tag, "routers in channel = " + key + " number is "
                + routerList.mRouterList.size());

            if (routerList.mRouterList.size() > 1)
                Collections.sort(routerList.mRouterList, mRouterComparator);

            for (RouterInfo routerInfo : routerList.mRouterList)
            {
                Log.d(tag, routerInfo.toString());
            }

            Log.d(tag, "---------------------------------------------------------");
        }
    }
}
