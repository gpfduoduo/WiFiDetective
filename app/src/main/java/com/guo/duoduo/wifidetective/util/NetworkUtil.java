package com.guo.duoduo.wifidetective.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.util.SparseArray;


/**
 * 关于频率和信道
 * http://www.radio-electronics.com/info/wireless/wi-fi/80211-channels-number
 * -frequencies-bandwidth.php
 * <p/>
 * Created by 郭攀峰 on 2015/10/20.
 */
public class NetworkUtil
{

    private static final String tag = NetworkUtil.class.getSimpleName();

    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected())
        {
            return true;
        }

        return false;
    }

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

    public static String getCurrentSsid(Context context)
    {
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null)
            return wifiInfo.getSSID();
        else
            return null;
    }

    /**
     * 获取设备连接的网关的ip地址
     *
     * @param context
     * @return
     */
    public static String getGateWayIp(Context context)
    {
        String gatewayIp = null;
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        if (dhcpInfo != null)
        {
            gatewayIp = Int2String(dhcpInfo.gateway);
        }

        Log.d(tag, "gate way ip = " + gatewayIp);
        return gatewayIp;
    }

    /**
     * 获取设备的mac地址
     *
     * @param context
     * @return
     */
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

    /**
     * 互殴去你所在网络的广播地址
     *
     * @param context
     * @return
     * @throws UnknownHostException
     */
    public static InetAddress getBroadcastAddress(Context context)
            throws UnknownHostException
    {
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        if (dhcpInfo == null)
        {
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (dhcpInfo.ipAddress & dhcpInfo.netmask) | ~dhcpInfo.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);

        return InetAddress.getByAddress(quads);
    }

    /**
     * 获取设备的ip地址
     *
     * @return
     */
    public static String getLocalIp()
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

    public final static String Int2String(int IP)
    {
        String ipStr = "";

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            ipStr += String.valueOf(0xFF & IP);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 8);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 16);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 24);
        }
        else
        {

            ipStr += String.valueOf(0xFF & IP >> 24);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 16);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP >> 8);
            ipStr += ".";
            ipStr += String.valueOf(0xFF & IP);
        }

        return ipStr;
    }

    private final static ArrayList<Integer> channelsFrequency = new ArrayList<Integer>(
        Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447, 2452, 2457,
            2462, 2467, 2472, 2484));

    private static SparseArray<Integer> mChannelFrequency = new SparseArray<>();

    static
    {
        mChannelFrequency.put(1, 2412);
        mChannelFrequency.put(2, 2417);
        mChannelFrequency.put(3, 2422);
        mChannelFrequency.put(4, 2427);
        mChannelFrequency.put(5, 2432);
        mChannelFrequency.put(6, 2437);
        mChannelFrequency.put(7, 2442);
        mChannelFrequency.put(8, 2447);
        mChannelFrequency.put(9, 2452);
        mChannelFrequency.put(10, 2457);
        mChannelFrequency.put(11, 2462);
        mChannelFrequency.put(12, 2467);
        mChannelFrequency.put(13, 2472);
        mChannelFrequency.put(14, 2484);

        mChannelFrequency.put(36, 5180);
        mChannelFrequency.put(40, 5200);
        mChannelFrequency.put(44, 5220);
        mChannelFrequency.put(48, 5240);
        mChannelFrequency.put(52, 5260);
        mChannelFrequency.put(56, 5280);
        mChannelFrequency.put(60, 5300);
        mChannelFrequency.put(64, 5320);
        mChannelFrequency.put(100, 5500);
        mChannelFrequency.put(104, 5520);
        mChannelFrequency.put(108, 5540);
        mChannelFrequency.put(112, 5560);
        mChannelFrequency.put(116, 5580);
        mChannelFrequency.put(120, 5600);
        mChannelFrequency.put(124, 5620);
        mChannelFrequency.put(128, 5640);
        mChannelFrequency.put(132, 5660);
        mChannelFrequency.put(136, 5680);
        mChannelFrequency.put(140, 5700);
        mChannelFrequency.put(149, 5745);
        mChannelFrequency.put(153, 5765);
        mChannelFrequency.put(157, 5785);
        mChannelFrequency.put(161, 5805);
    }

    /**
     * 2.4G
     *
     * @param frequency 频率
     * @return
     */
    public static int getChannelFromFrequency(int frequency)
    {
        return channelsFrequency.indexOf(Integer.valueOf(frequency));
    }

    /**
     * 2.4G and 5G
     *
     * @param frequency
     * @return
     */
    public static int getChannel(int frequency)
    {
        for (int i = 0; i < mChannelFrequency.size(); i++)
        {
            if (mChannelFrequency.valueAt(i) == frequency)
            {
                return mChannelFrequency.keyAt(i);
            }
        }

        return -1;
    }

    /**
     * 是否ping通
     * 
     * @param ip
     * @return
     */
    public static boolean isPingOk(String ip)
    {
        try
        {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 10 -w 4 " + ip);
            if (p == null)
            {
                return false;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                if (line.contains("bytes from"))
                {
                    return true;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static String ping(String ip, String count)
    {
        String result = "";
        try
        {
            Process p = Runtime.getRuntime().exec(
                "/system/bin/ping -c " + count + " -w 4 " + ip);
            if (p == null)
            {
                return result;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                if (line.contains("bytes from"))
                {
                    Log.d(tag, "ping result = " + line);
                    result += line + "\n";
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 该ip的端口某些端口是否打开
     * 
     * @param ip
     * @return
     */
    public static boolean isAnyPortOk(String ip)
    {
        int portArray[] = {80, 135, 137, 139, 8081, 3389, 3511, 3526, 62078};

        Selector selector;
        try
        {
            selector = Selector.open();
            for (int i = 0; i < portArray.length; i++)
            {
                SocketChannel channel = SocketChannel.open();
                SocketAddress address = new InetSocketAddress(ip, portArray[i]);
                channel.configureBlocking(false);
                channel.connect(address);
                channel.register(selector, SelectionKey.OP_CONNECT, address);
                if (selector.select(1500) != 0)
                {
                    selector.close();
                    return true;
                }
                else
                {
                    selector.close();
                    return false;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
