package com.guo.duoduo.wifidetective.core.devicescan;


/**
 * Created by 郭攀峰 on 2015/10/25.
 */
public class IP_MAC
{
    public String mIp;
    public String mMac;
    public String mManufacture;
    public String mDeviceName;

    public IP_MAC(String ip, String mac)
    {
        this.mIp = ip;
        this.mMac = mac;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IP_MAC ip_mac = (IP_MAC) o;

        return mIp.equals(ip_mac.mIp) && mMac.equals(ip_mac.mMac);
    }

    @Override
    public int hashCode()
    {
        int result = mIp.hashCode() + mMac.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "IP_MAC{" + "mIp='" + mIp + '\'' + ", mMac='" + mMac + '\'' + '}';
    }
}
