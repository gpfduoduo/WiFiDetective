package com.guo.duoduo.wifidetective.util;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


/**
 * Created by 郭攀峰 on 2015/10/20.
 */
public class NetworkUtil
{

    private static final String tag = NetworkUtil.class.getSimpleName();

    public static WifiInfo getWifiInfo(Context context)
    {
        WifiManager wifiManager = getWifiManager(context);
        if (wifiManager != null)
        {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo;
        }
        else
            return null;
    }

    public static WifiManager getWifiManager(Context context)
    {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager;
    }

    public static DhcpInfo getDhcpInfo(Context context)
    {
        WifiManager wifiManager = getWifiManager(context);
        if (wifiManager != null)
        {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            return dhcpInfo;
        }
        else
            return null;
    }

    public static String getLocalMac(Context context)
    {
        String localMac = null;
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null)
        {
            localMac = wifiInfo.getMacAddress();
        }

        Log.d(tag, "local mac = " + localMac);
        return localMac;
    }

    public static String getLocalIp(Context contenxt)
    {
        String localIp = null;

        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements())
            {
                NetworkInterface networkInterface = en.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface
                        .getInetAddresses();
                while (inetAddresses.hasMoreElements())
                {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                        && inetAddress instanceof Inet4Address)
                    {
                        localIp = inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        Log.d(tag, "local ip = " + localIp);
        return localIp;
    }
}
