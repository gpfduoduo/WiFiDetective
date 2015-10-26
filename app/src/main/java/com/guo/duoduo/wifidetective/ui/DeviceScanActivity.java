package com.guo.duoduo.wifidetective.ui;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.core.devicescan.DeviceScanManager;
import com.guo.duoduo.wifidetective.core.devicescan.DeviceScanResult;


public class DeviceScanActivity extends BaseActivity
{

    private DeviceScanManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        manager = new DeviceScanManager();
        manager.startScan(getApplicationContext(), new DeviceScanResult()
        {
            @Override
            public void deviceScanResult()
            {

            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (manager != null)
        {
            manager.stopScan();
        }
    }
}
