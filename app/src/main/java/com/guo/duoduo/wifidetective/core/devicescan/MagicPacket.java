package com.guo.duoduo.wifidetective.core.devicescan;


/**
 * Created by 郭攀峰 on 2015/10/23.
 * 
 * 要实现网络唤醒，网卡要支持WOL（Wake On LAN）技术。 网络唤醒包 网络唤醒包，也叫唤醒帧，英文名为Magic
 * Packet。是由AMD公司拥有及注册的专利技 术，虽然并非世界公认的标准，但是仍然受到很多网卡制造商的支持，因此，许多具有网络
 * 唤醒功能的网卡都能与之兼容，Magic Packet包含有6个字节的"F"和连续重复16次的MAC地 址。当一台电脑的网卡在数据帧中发现Magic
 * Packet，并且其包含的是自己的MAC地址时，
 * 便会将电脑唤醒。假设一台电脑的MAC地址为：A1:B2:C3:D4:F5:G6.那么当电脑的网卡在数据帧内发现以 下的片段后便会将电脑唤醒：
 * FFFFFFFFFFFFA1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6
 * A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6
 * A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6A1B2C3D4F5G6
 */
public class MagicPacket
{

}
