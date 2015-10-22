package com.guo.duoduo.wifidetective.sdk;


import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


/**
 * Created by 郭攀峰 on 2015/10/22.
 */
public class DeviceScanManager
{
    private static final String tag = DeviceScanManager.class.getSimpleName();

    private DeviceScanManagerHandler mUiHandler;
    private DeviceScanHandler mDeviceScanHandler;
    private CustomHandlerThread mHandlerThread;

    private DeviceScanResult mScanResult;

    public DeviceScanManager()
    {
        mUiHandler = new DeviceScanManagerHandler(this);
    }

    public void startScan(Context context, DeviceScanResult scanResult)
    {
        mScanResult = scanResult;

        mHandlerThread = new CustomHandlerThread("DeviceScanThread",
            DeviceScanHandler.class);
        mHandlerThread.start();
        mHandlerThread.isReady();

        mDeviceScanHandler = (DeviceScanHandler) mHandlerThread.getLooperHandler();
        mDeviceScanHandler.init(context);
    }

    public void stopScan()
    {
        if (mHandlerThread != null)
        {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    private static class DeviceScanManagerHandler extends Handler
    {
        private WeakReference<DeviceScanManager> weakReference;

        public DeviceScanManagerHandler(DeviceScanManager manager)
        {
            weakReference = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg)
        {
            DeviceScanManager manager = weakReference.get();
            if (manager == null)
                return;

        }
    }
}
