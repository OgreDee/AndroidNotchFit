package com.util.notchfit.phone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.DisplayCutout;

import com.util.notchfit.IPhone;
import com.util.notchfit.NotchProperty;
import com.util.notchfit.ScreenAdaptation;

import java.util.List;

abstract class VirtualPhone implements IPhone {
    //制造商
    protected String manufacturer = "";

    public  void SetManufacturer(String i_manufacturer)
    {
        this.manufacturer = i_manufacturer;
    }
    @Override
    public NotchProperty Excute(Context context)
    {
        NotchProperty notchProperty = new NotchProperty();

        try
        {
            notchProperty.setManufacturer(manufacturer);

            if(IsAndroidP())
            {
                excuteAndroidO(context, notchProperty);
            }
            else
            {
                excuteAndroidP(context, notchProperty);
            }
        }
        catch (Exception e)
        {
            Log.e(ScreenAdaptation.LOG_TAG, "NotchProperty Excute Error : " + e.getMessage());
        }

        return  notchProperty;
    }

    private  void excuteAndroidO(Context context, NotchProperty notchProperty)
    {
        notchProperty.setNotchEnable(isNotchEnable_P(context));

        if(notchProperty.isNotchEnable())
        {
            int[] size = getNotchSize_P(context);

            notchProperty.setNotchWidth(size[0]);
            notchProperty.setNotchHeight(size[1]);
        }
    }

    private  void excuteAndroidP(Context context, NotchProperty notchProperty)
    {
        notchProperty.setNotchEnable(isNotchEnable_O(context));

        if(notchProperty.isNotchEnable())
        {
            int[] size = getNotchSize_O(context);

            notchProperty.setNotchWidth(size[0]);
            notchProperty.setNotchHeight(size[1]);
        }
    }

    /*
     * 是否是AndroidP
     */
    private  boolean IsAndroidP()
    {
        return  Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    /**
     * 判断获取O版本的设备是否显示刘海屏
     * @param context
     * @return
     */
    protected abstract boolean isNotchEnable_O(Context context);

    /**
     * 获取O版本及以前版本的非谷歌标准的刘海屏宽高尺寸
     * @param context
     * @return 返回刘海屏尺寸数组，int[0]：刘海屏宽度，int[1]：刘海屏高度
     */
    protected abstract int[] getNotchSize_O(Context context);

    /**
     * 判断获取P版本及以后版本的设备是否显示刘海屏
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.P)
    protected boolean isNotchEnable_P(Context context){
        Activity activity = (Activity)context;
        DisplayCutout displayCutout = activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
        if(displayCutout == null || displayCutout.getBoundingRects() == null || displayCutout.getBoundingRects().size() == 0){
            Log.i(ScreenAdaptation.LOG_TAG, manufacturer+" P notch enable: false");
            return false;
        }
        Log.i(ScreenAdaptation.LOG_TAG, manufacturer+" P notch enable: true");
        return true;
    }
    /**
     * 获取谷歌标准P版本及以上的刘海屏宽高尺寸
     * @param context
     * @return 返回刘海屏尺寸数组，int[0]：刘海屏宽度，int[1]：刘海屏高度
     */
    @TargetApi(Build.VERSION_CODES.P)
    protected int[] getNotchSize_P(Context context){
        Activity activity = (Activity)context;
        int[] notchSize = new int[]{0,0};
        DisplayCutout displayCutout = activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
        List<Rect> boundingRects = displayCutout.getBoundingRects();
        if(boundingRects.size() != 0){
            Rect rect = boundingRects.get(0);
            notchSize[0] = rect.width();
            notchSize[1] = rect.height();
            Log.i(ScreenAdaptation.LOG_TAG,manufacturer + " O notch size: " + "width> " + notchSize[0] + " height>" + notchSize[1]);
        }
        return notchSize;
    }
}
