package com.guo.duoduo.wifidetective.ui;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.util.StatusBarCompat;


public class MainActivity extends AppCompatActivity
{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarCompat.compat(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
            toolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this), 0, 0);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.id_nv_menu);
        setupDrawerContent(mNavigationView);
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        menuItem.setChecked(false);
                        switch (menuItem.getItemId())
                        {
                            case R.id.nav_device_scan :
                                menuItem.setChecked(false);
                                startActivity(new Intent(MainActivity.this,
                                    DeviceScanActivity.class));
                                break;
                            case R.id.nav_wifi_scan :
                                startActivity(new Intent(MainActivity.this,
                                    WiFiScanActivity.class));
                                break;
                            case R.id.nav_hot_spot :
                                startActivity(new Intent(MainActivity.this,
                                    HotSpotActivity.class));
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(findViewById(R.id.id_nv_menu)))
            mDrawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

}
