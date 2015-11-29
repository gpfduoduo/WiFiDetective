package com.guo.duoduo.wifidetective.core.devicescan;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import android.content.Context;
import android.text.TextUtils;

import com.guo.duoduo.wifidetective.R;


/**
 * Created by 郭攀峰 on 2015/11/29.
 */
public class Manufacture
{

    private static Properties mManufacture = null;
    private static Manufacture mInstance;

    public static Manufacture getInstance()
    {
        if (mInstance == null)
        {
            synchronized (Manufacture.class)
            {
                if (mInstance == null)
                {
                    mInstance = new Manufacture();
                }
            }
        }

        return mInstance;
    }

    public final String getManufacture(String mac, Context context)
    {
        String vendor;

        if (TextUtils.isEmpty(mac) || (mac.length() < 8))
            return null;

        synchronized (this)
        {
            if (mManufacture == null)
            {

                mManufacture = new Properties();

                try
                {
                    InputStream in = context.getResources().openRawResource(
                        R.raw.manufacture);
                    mManufacture.load(in);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }

            String key = mac.substring(0, 2) + mac.substring(3, 5) + mac.substring(6, 8);

            // if can not find, return null
            vendor = mManufacture.getProperty(key);

            if (vendor != null)
            {
                try
                {
                    vendor = new String(vendor.getBytes("ISO8859-1"), "utf-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return vendor;
    }
}
