package com.guo.duoduo.wifidetective.receiver;


import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.guo.duoduo.wifidetective.entity.RouterInfo;
import com.guo.duoduo.wifidetective.util.NetworkUtil;


/**
 * Created by 郭攀峰 on 2015/11/3.
 */
public class WiFiBroadcastReceiver extends BroadcastReceiver
{
    private static final String tag = WiFiBroadcastReceiver.class.getSimpleName();
    private WifiManager mWiFiManager;
    private List<ScanResult> mScanResult;

    public WiFiBroadcastReceiver(WifiManager wifiManager)
    {
        mWiFiManager = wifiManager;
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
                for (ScanResult scanResult : mScanResult)
                {
                    RouterInfo routerInfo = new RouterInfo();
                    routerInfo.mSsid = scanResult.SSID;
                    routerInfo.mMac = scanResult.BSSID;
                    routerInfo.mChannel = NetworkUtil
                            .getChannelFromFrequency(scanResult.frequency);
                    routerInfo.mStrength = WifiManager.calculateSignalLevel(
                        scanResult.level, 5);
                    printResult(routerInfo);
                }
            }

            //scan again
            mWiFiManager.startScan();
        }
    }

    private void printResult(RouterInfo routerInfo)
    {
        Log.d(tag, routerInfo.toString());
    }
}
