package com.guo.duoduo.wifidetective.util;


import java.util.Comparator;

import com.guo.duoduo.wifidetective.entity.RouterInfo;


/**
 * Created by 郭攀峰 on 2015/11/4.
 */
public class RouterComparator implements Comparator<Object>
{
    /**
     * 升序排列 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
     */
    @Override
    public int compare(Object o1, Object o2)
    {
        return compare(((RouterInfo) o1).mStrength, ((RouterInfo) o2).mStrength);
    }

    private int compare(Integer o1, Integer o2)
    {
        int val1 = o1.intValue();
        int val2 = o2.intValue();
        return (val1 < val2 ? -1 : (val1 == val2 ? 0 : 1));

    }
}
