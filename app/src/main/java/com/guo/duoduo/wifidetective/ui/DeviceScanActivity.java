package com.guo.duoduo.wifidetective.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.core.devicescan.DeviceScanManager;
import com.guo.duoduo.wifidetective.core.devicescan.DeviceScanResult;
import com.guo.duoduo.wifidetective.core.devicescan.IP_MAC;
import com.guo.duoduo.wifidetective.ui.adapter.DeviceScanAdapter;
import com.guo.duoduo.wifidetective.ui.view.DividerDecoration;
import com.guo.duoduo.wifidetective.ui.view.StatusBarCompat;
import com.guo.duoduo.wifidetective.util.NetworkUtil;
import com.guo.duoduo.wifidetective.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceScanActivity extends BaseActivity
        implements OnProgressBarListener {
    private static final String tag = DeviceScanActivity.class.getSimpleName();
    private DeviceScanManager manager;
    private RecyclerView mRecyclerView;
    private NumberProgressBar mProgressbar;
    private Toolbar mToolbar;
    private DeviceScanAdapter mAdapter;
    private List<IP_MAC> mDeviceList = new ArrayList<IP_MAC>();


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);

        if (!NetworkUtil.isWifiConnected(this)) {
            ToastUtils.showTextToast(this,
                    getString(R.string.connect_wifi_please));
            finish();
            return;
        }
        StatusBarCompat.compat(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mProgressbar = (NumberProgressBar) findViewById(R.id.numberbar);
        mProgressbar.setOnProgressBarListener(this);
        mProgressbar.setProgress(0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override public void run() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        mProgressbar.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);

        manager = new DeviceScanManager();
        manager.startScan(getApplicationContext(), new DeviceScanResult() {
            @Override public void deviceScanResult(IP_MAC ip_mac) {
                if (!mDeviceList.contains(ip_mac)) {
                    mDeviceList.add(ip_mac);
                    mAdapter.notifyDataSetChanged();
                    mToolbar.setTitle(
                            getString(R.string.title_activity_device_scan) +
                                    Integer.toString(mDeviceList.size()));
                }
            }
        });

        String localIp = NetworkUtil.getLocalIp();
        String gateIp = NetworkUtil.getGateWayIp(this);
        String localMac = NetworkUtil.getLocalMac(this);
        IP_MAC myself = new IP_MAC(localIp, localMac);
        myself.mManufacture = Build.MANUFACTURER;
        myself.mDeviceName = Build.MODEL;
        mDeviceList.add(myself);
        mRecyclerView = (RecyclerView) findViewById(R.id.device_recycleview);
        mAdapter = new DeviceScanAdapter(this, mDeviceList, localIp, gateIp);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerDecoration(this));
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override public void onProgressChange(int current, int max) {
        if (current == max) mProgressbar.setVisibility(View.GONE);
    }


    @Override public void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.stopScan();
        }
    }
}
