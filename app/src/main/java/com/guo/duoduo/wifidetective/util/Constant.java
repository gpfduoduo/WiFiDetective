package com.guo.duoduo.wifidetective.util;


/**
 * Created by 郭攀峰 on 2015/10/22.
 */
public class Constant
{
    public static final int COUNT = 255;

    public static final int SCAN_COUNT = 3;

    /***
     * 137端口的主要作用是在局域网中提供计算机的名字或IP地址查询服务
     */
    public static final int NETBIOS_PORT = 137;

    public static final int UPD_TIMEOUT = 500;

    public interface MSG
    {
        public static final int START = 0;
        public static final int STOP = -1;
        public static final int SCAN_ONE = 1;
        public static final int SCAN_OVER = 2;

        public static final int WIFI_SCAN_RESULT = 10;

    }

}
