package com.guo.duoduo.wifidetective.ui.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.entity.RouterInfo;


/**
 * Created by 郭攀峰 on 2015/11/14.
 */
public class WiFiScanAdapter extends RecyclerView.Adapter<WiFiScanAdapter.MyViewHolder>
{

    private Context mContext;
    private ArrayList<RouterInfo> mRouterList;

    public WiFiScanAdapter(Context context, ArrayList<RouterInfo> routeList)
    {
        this.mContext = context;
        this.mRouterList = routeList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        RouterInfo routerInfo = mRouterList.get(position);
        holder.mWiFiName.setText(routerInfo.mSsid);
        holder.mWiFiSecurity.setText("加密：" + routerInfo.mSecurity);
        holder.mWiFiChannel.setText("信道：" + Integer.toString(routerInfo.mChannel));
        holder.mWiFiMac.setText("MAC：" + routerInfo.mMac);
        holder.mWiFiStrength.setText("强度：" + Integer.toString(routerInfo.mStrength));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(
            R.layout.view_wifi_scan_item, parent, false)); //必须通过三个参数的方式，否则UI有问题
    }

    @Override
    public int getItemCount()
    {
        return mRouterList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView mWiFiName;
        TextView mWiFiMac;
        TextView mWiFiChannel;
        TextView mWiFiStrength;
        TextView mWiFiSecurity;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            mWiFiName = (TextView) itemView.findViewById(R.id.item_wifi_name);
            mWiFiSecurity = (TextView) itemView.findViewById(R.id.item_wifi_security);
            mWiFiMac = (TextView) itemView.findViewById(R.id.item_wifi_mac);
            mWiFiChannel = (TextView) itemView.findViewById(R.id.item_wifi_chanel);
            mWiFiStrength = (TextView) itemView.findViewById(R.id.item_wifi_strength);
        }
    }
}
