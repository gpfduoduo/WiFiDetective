package com.guo.duoduo.wifidetective.core.devicescan;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.text.TextUtils;
import android.util.Log;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.entity.DeviceInfo;
import com.guo.duoduo.wifidetective.ui.MainApplication;
import com.guo.duoduo.wifidetective.util.Constant;


/**
 * Created by 郭攀峰 on 2015/10/24. 针对确定的IP进行ping操作和端口（一些常用的端口）检测，看是否在线
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
                String manufacture = parseHostInfo(mIpMac.mMac); //解析机器名称
                Log.e(tag, "device manufacture = " + manufacture);
                if (!TextUtils.isEmpty(manufacture))
                {
                    mIpMac.mManufacture = manufacture;
                }

                try
                {
                    NetBios nb = new NetBios(mIpMac.mIp);
                    String deviceName = nb.getNbName();
                    Log.d(tag, "device name = " + deviceName);
                    if (!TextUtils.isEmpty(deviceName))
                    {
                        mIpMac.mDeviceName = deviceName;
                    }
                    else
                    {
                        mIpMac.mDeviceName = MainApplication.getInstance().getResources()
                                .getString(R.string.unknown);
                    }
                }
                catch (IOException e)
                {
                    mIpMac.mDeviceName = MainApplication.getInstance().getResources()
                            .getString(R.string.unknown);
                    e.printStackTrace();
                }

                if (mDeviceScanHandler != null)
                {
                    mDeviceScanHandler.sendMessage(mDeviceScanHandler.obtainMessage(
                        Constant.MSG.SCAN_ONE, mIpMac));
                }
            }
        }
    }

    private String parseHostInfo(String mac)
    {
        return Manufacture.getInstance().getManufacture(mac,
            MainApplication.getInstance().getApplicationContext());
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
