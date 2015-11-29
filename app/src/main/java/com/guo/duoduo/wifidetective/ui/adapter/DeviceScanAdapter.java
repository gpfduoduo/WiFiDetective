package com.guo.duoduo.wifidetective.ui.adapter;


import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.core.devicescan.IP_MAC;


/**
 * Created by 郭攀峰 on 2015/11/27.
 */
public class DeviceScanAdapter
    extends
        RecyclerView.Adapter<DeviceScanAdapter.DeviceHolder>
{

    private Context mContext;
    private List<IP_MAC> mDeviceList;
    private String mLocalIp;
    private String mGateIp;

    public DeviceScanAdapter(Context context, List<IP_MAC> list, String localIp,
            String gateip)
    {
        this.mContext = context;
        this.mDeviceList = list;
        this.mLocalIp = localIp;
        this.mGateIp = gateip;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new DeviceHolder(LayoutInflater.from(mContext).inflate(
            R.layout.view_deivce_san_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position)
    {
        IP_MAC ip_mac = mDeviceList.get(position);
        if (ip_mac != null)
        {
            holder.mDeviceIp.setText(String.format(
                mContext.getResources().getString(R.string.ip_address), ip_mac.mIp));
            holder.mDeviceMac.setText(String.format(
                mContext.getResources().getString(R.string.mac_address), ip_mac.mMac));
            if (ip_mac.mIp.equals(mLocalIp))
            {
                holder.mDeviceName.setText(mContext.getString(R.string.your_phone) + " "
                    + ip_mac.mDeviceName);
            }
            else if (ip_mac.mIp.equals(mGateIp))
            {
                holder.mDeviceName.setText(mContext.getString(R.string.gate_net));
            }
            else
            {
                holder.mDeviceName.setText(ip_mac.mDeviceName);
            }

            holder.mDeviceManufacture.setText(ip_mac.mManufacture);
        }
    }

    @Override
    public int getItemCount()
    {
        return mDeviceList.size();
    }

    class DeviceHolder extends RecyclerView.ViewHolder
    {
        TextView mDeviceIp;
        TextView mDeviceMac;
        TextView mDeviceName;
        TextView mDeviceManufacture;

        public DeviceHolder(View itemView)
        {
            super(itemView);
            mDeviceIp = (TextView) itemView.findViewById(R.id.device_ip);
            mDeviceMac = (TextView) itemView.findViewById(R.id.device_mac);
            mDeviceName = (TextView) itemView.findViewById(R.id.device_name);
            mDeviceManufacture = (TextView) itemView
                    .findViewById(R.id.device_manufacture);
        }
    }
}
