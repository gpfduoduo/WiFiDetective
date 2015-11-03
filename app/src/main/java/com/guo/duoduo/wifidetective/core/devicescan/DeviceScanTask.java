package com.guo.duoduo.wifidetective.core.devicescan;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.util.Log;

import com.guo.duoduo.wifidetective.entity.DeviceInfo;


/**
 * Created by 郭攀峰 on 2015/10/24.
 */
public class DeviceScanTask
{
    private static final String tag = DeviceScanTask.class.getSimpleName();

    private DeviceScanGroup mDeviceScanGroup;
    private DeviceInfo mDeivceInfo;
    public Thread mThread;
    public DeviceScanRunnable mRunnable;

    private IP_MAC mIpMac;
    private DeviceScanHandler mDeviceScanHandler;

    public DeviceScanTask(DeviceScanGroup group)
    {
        this.mDeviceScanGroup = group;
        mRunnable = new DeviceScanRunnable();
        mDeivceInfo = new DeviceInfo();
    }

    public void setParams(IP_MAC ip_mac, DeviceScanHandler handler)
    {
        this.mIpMac = ip_mac;
        this.mDeviceScanHandler = handler;
    }

    private class DeviceScanRunnable implements Runnable
    {
        public void run()
        {
            if (isPingOk(mIpMac.mIp) || isAnyPortOk())
            {
                Log.e(tag, "the device is in wifi : " + mIpMac.toString());
            }
        }
    }

    private boolean isPingOk(String ip)
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

    private boolean isAnyPortOk()
    {
        int portArray[] = {80, 135, 137, 139, 8081, 3389, 3511, 3526, 62078};

        Selector selector;
        try
        {
            selector = Selector.open();
            for (int i = 0; i < portArray.length; i++)
            {
                SocketChannel channel = SocketChannel.open();
                SocketAddress address = new InetSocketAddress(mIpMac.mIp, portArray[i]);
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
