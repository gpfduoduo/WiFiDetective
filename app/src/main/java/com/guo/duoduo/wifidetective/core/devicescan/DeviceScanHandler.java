package com.guo.duoduo.wifidetective.core.devicescan;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.guo.duoduo.wifidetective.util.Constant;
import com.guo.duoduo.wifidetective.util.NetworkUtil;


/**
 * Created by 郭攀峰 on 2015/10/22.
 */
public class DeviceScanHandler extends Handler
{

    private static final String tag = DeviceScanHandler.class.getSimpleName();

    /**
     * 此构造函数必须实现
     * 
     * @param looper
     */
    public DeviceScanHandler(Looper looper)
    {
        super(looper);
    }

    private Context mContext;
    /**
     * 被扫描的所有ip地址
     */
    private List<String> mScanList = new ArrayList<>();
    public List<IP_MAC> mIpMacInLan = Collections.synchronizedList(new ArrayList());

    private DeviceScanGroup[] mScanGroupArray = null;
    private int mGroupIndex;
    private DeviceScanManager.DeviceScanManagerHandler mUiHandler;

    public void init(Context context, DeviceScanManager.DeviceScanManagerHandler uiHandler)
    {
        mContext = context;
        mUiHandler = uiHandler;

        String localIp = NetworkUtil.getLocalIp();
        String routerIp = NetworkUtil.getGateWayIp(mContext);
        if (TextUtils.isEmpty(localIp) || TextUtils.isEmpty(routerIp))
            return;

        mScanList.clear();
        String netIp = localIp.substring(0, localIp.lastIndexOf(".") + 1);
        for (int i = 1; i < Constant.COUNT; i++)
        {
            mScanList.add(netIp + i);
        }

        mScanList.remove(localIp);
        mScanList.remove(routerIp);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case Constant.MSG.START :
                Log.d(tag, "receive message: start scan");
                sendQueryPacket();
                groupProcessing();
                break;
            case Constant.MSG.SCAN_ONE :
                IP_MAC ip_mac = (IP_MAC) msg.obj;
                if (ip_mac != null)
                {
                    Log.d(tag, "scan one device: " + ip_mac.toString());
                    if (mUiHandler != null)
                    {
                        mUiHandler.sendMessage(mUiHandler.obtainMessage(
                            Constant.MSG.SCAN_ONE, ip_mac));
                    }
                }
                break;
            case Constant.MSG.STOP :
                Log.d(tag, "receive message: stop scan");
                stop();
                break;
        }
    }

    /**
     * 发送包进行arp操作
     */
    private void sendQueryPacket()
    {
        NetBios netBios = null;
        try
        {
            netBios = new NetBios();
            for (int i = 0; i < mScanList.size(); i++)
            {
                netBios.setIp(mScanList.get(i));
                netBios.setPort(Constant.NETBIOS_PORT);
                netBios.send();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        netBios.close();
    }

    /**
     * 分3组进行网络操作：
     */
    private void groupProcessing()
    {
        this.mIpMacInLan.clear();
        mGroupIndex = 0;
        mScanGroupArray = new DeviceScanGroup[Constant.SCAN_COUNT];
        Trigger trigger = new Trigger();
        for (int i = 0; i < Constant.SCAN_COUNT; i++)
        {
            mScanGroupArray[i] = null;
            if (i == 0)
            {
                this.postDelayed(trigger, 500);
            }
            else
            {
                if (i == 1)
                {
                    postDelayed(trigger, 1000);
                }
                else
                {
                    sendQueryPacket();
                    postDelayed(trigger, 3000);
                }
            }
        }
    }

    private void stop()
    {
        synchronized (this)
        {
            if (mScanGroupArray != null)
            {
                for (int i = 0; i < mScanGroupArray.length; i++)
                {
                    if (mScanGroupArray[i].mThread != null)
                    {
                        mScanGroupArray[i].mThread.interrupt();
                        mScanGroupArray[i].stop();
                        mScanGroupArray[i].mThread = null;
                    }
                }
            }
        }
    }

    private class Trigger implements Runnable
    {
        DeviceScanGroup mGroup;

        public void run()
        {
            synchronized (this)
            {
                mGroup = new DeviceScanGroup(DeviceScanHandler.this, mGroupIndex);
                if (mScanGroupArray.length > mGroupIndex)
                {
                    mGroup.mThread = new Thread(mGroup, "Device Scan Group Index = "
                        + mGroupIndex);
                    mGroup.mThread.setPriority(Thread.MAX_PRIORITY);
                    mGroup.mThread.start();
                    mScanGroupArray[mGroupIndex] = mGroup;
                    mGroupIndex++;
                }
            }
        }
    }
}
