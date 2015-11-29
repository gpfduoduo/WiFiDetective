package com.guo.duoduo.wifidetective.core.devicescan;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.guo.duoduo.wifidetective.util.Constant;


/**
 * Created by 郭攀峰 on 2015/10/23.
 */
public abstract class UdpCommunicate
{
    private static final String tag = UdpCommunicate.class.getSimpleName();

    private byte[] mBuffer = new byte[1024];
    private byte[] mBytes;

    private DatagramSocket mUdpSocket;

    public abstract String getPeerIp();

    public abstract int getPort();

    public abstract byte[] getSendContent();

    protected UdpCommunicate() throws SocketException
    {
        mUdpSocket = new DatagramSocket();
        mUdpSocket.setSoTimeout(Constant.UPD_TIMEOUT);
    }

    protected void send() throws IOException
    {
        mBytes = getSendContent();
        DatagramPacket dp = new DatagramPacket(mBytes, mBytes.length,
            InetAddress.getByName(getPeerIp()), getPort());
        mUdpSocket.send(dp);
    }

    protected DatagramPacket receive() throws IOException
    {
        DatagramPacket dp = new DatagramPacket(mBuffer, mBuffer.length);
        mUdpSocket.receive(dp);
        return dp;
    }

    protected void close()
    {
        if (mUdpSocket != null)
        {
            mUdpSocket.close();
        }
    }

}
