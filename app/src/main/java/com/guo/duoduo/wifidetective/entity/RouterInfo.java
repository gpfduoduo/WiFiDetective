package com.guo.duoduo.wifidetective.entity;


/**
 * Created by 郭攀峰 on 2015/10/23.
 */
public class RouterInfo
{
    public String mSsid = null;//WIFI的SSID
    public String mMac = null;//路由器的MAC地址
    public String mSecurity = null; //加密方式
    public int mFrequence = 0; //频率
    public int mChannel = 0;//WIFI的信道
    public int mStrength = 0;//信号强度：0-4等级

    @Override
    public String toString()
    {
        return "名称：'" + mSsid + '\'' + ", MAC：'" + mMac + '\'' + ", 信道：" + mChannel
            + ", 强度：" + mStrength + '\'' + "加密：" + mSecurity + '\'' + ", 频率："
            + mFrequence;
    }
}
