package com.guo.duoduo.wifidetective.ui.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by 郭攀峰 on 2015/11/13.
 */
public class StatusBarCompat
{
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#3F51B5");

    public static void compat(Activity activity, int statusColor)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (statusColor != INVALID_VAL)
            {
                activity.getWindow().setStatusBarColor(statusColor);
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity
                    .findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL)
            {
                color = statusColor;
            }
            View statusBarView = contentView.getChildAt(0);
            //改变颜色时避免重复添加statusBarView
            if (statusBarView != null
                && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity))
            {
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(activity);
            int statusBarHeight = getStatusBarHeight(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }
    }

    public static void compat(Activity activity)
    {
        compat(activity, INVALID_VAL);
    }

    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
            "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
