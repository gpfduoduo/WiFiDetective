package com.guo.duoduo.wifidetective.core.devicescan;


import java.io.IOException;
import java.net.DatagramPacket;

import android.util.Log;

import com.guo.duoduo.wifidetective.util.Constant;


/**
 * Created by 郭攀峰 on 2015/10/23.
 */
public class NetBios extends UdpCommunicate
{
    private static final String tag = NetBios.class.getSimpleName();
    private String mIP;
    private int mPort;

    public NetBios(String ip) throws IOException
    {
        super();
        this.mIP = ip;
        this.mPort = Constant.NETBIOS_PORT;
    }

    public NetBios() throws IOException
    {
        super();
    }

    public void setIp(String ip)
    {
        this.mIP = ip;
    }

    public void setPort(int port)
    {
        this.mPort = port;
    }

    @Override
    public String getPeerIp()
    {
        return mIP;
    }

    @Override
    public int getPort()
    {
        return mPort;
    }

    @Override
    public byte[] getSendContent()
    {
        byte[] t_ns = new byte[50];
        t_ns[0] = 0x00;
        t_ns[1] = 0x00;
        t_ns[2] = 0x00;
        t_ns[3] = 0x10;
        t_ns[4] = 0x00;
        t_ns[5] = 0x01;
        t_ns[6] = 0x00;
        t_ns[7] = 0x00;
        t_ns[8] = 0x00;
        t_ns[9] = 0x00;
        t_ns[10] = 0x00;
        t_ns[11] = 0x00;
        t_ns[12] = 0x20;
        t_ns[13] = 0x43;
        t_ns[14] = 0x4B;
        for (int i = 15; i < 45; i++)
        {
            t_ns[i] = 0x41;
        }
        t_ns[45] = 0x00;
        t_ns[46] = 0x00;
        t_ns[47] = 0x21;
        t_ns[48] = 0x00;
        t_ns[49] = 0x01;

        return t_ns;
    }

    public String getNbName() throws IOException
    {
        byte[] data;

        send();
        DatagramPacket dp = receive();
        data = dp.getData();

        Log.d(tag, "net bios receive = " + new String(data));
        if (data.length > 56)
        {
            StringBuffer str = new StringBuffer(15);
            for (int i = 1; i < 16; i++)
            {
                str.append((char) (0xFF & data[56 + i]));
            }

            close();
            return str.toString().trim();
        }

        return null;
    }
}
