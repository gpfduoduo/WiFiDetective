package com.guo.duoduo.wifidetective.sdk;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.guo.duoduo.wifidetective.util.NetworkUtil;


/**
 * Created by 郭攀峰 on 2015/10/22.
 */
public class DeviceScanHandler extends Handler
{

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
    private String mLocalIp;
    private String mLocalMac;

    public void init(Context context)
    {
        mContext = context;
        mLocalIp = NetworkUtil.getLocalIp(mContext);
        mLocalMac = NetworkUtil.getLocalMac(mContext);

        
    }

    @Override
    public void handleMessage(Message msg)
    {

    }
}
