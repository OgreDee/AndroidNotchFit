package com.util.notchfit.phone;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.util.notchfit.ScreenAdaptation;


/*
 * https://blog.csdn.net/djy1992/article/details/80688376
 * https://dev.mi.com/console/doc/detail?pId=1293
 */
class MiPhone extends VirtualPhone {
    //    0x00000100 | 0x00000200 竖屏绘制到耳朵区
//    0x00000100 | 0x00000400 横屏绘制到耳朵区
//    0x00000100 | 0x00000200 | 0x00000400 横竖屏都绘制到耳朵区
    int notchFlag = 0x00000100 | 0x00000200 | 0x00000400;

    @Override
    protected boolean isNotchEnable_O(Context context) {
        return isHardwareNotchEnable(context) && isSettingNotchEnable(context) && isSoftAppNotchEnable(context);
    }

    @Override
    protected int[] getNotchSize_O(Context context) {
        int[] ret = new int[]{0,0};
        try
        {
            ret[0] = GetNotchWidth(context);
            ret[1] = GetNotchHeight(context);
        }
        catch (Exception e)
        {
            Log.e(ScreenAdaptation.LOG_TAG, "XiaoMi GetNotchSize");
        }

        return ret;
    }

    protected boolean isHardwareNotchEnable(Context context) {
        boolean result = false;
        try {
            Class<?> aClass = Class.forName("android.os.SystemProperties");
            Method getInt = aClass.getMethod("getInt", String.class, int.class);
            int invoke = (int) getInt.invoke(null, "ro.miui.notch", 0);
            result = invoke == 1;
            Log.e(ScreenAdaptation.LOG_TAG, "curResult:"+result);
        } catch (ClassNotFoundException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "error hasNotchInScreen_Xiaomi ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "error hasNotchInScreen_Xiaomi NoSuchMethodException");
        } catch (Exception e) {
            Log.e(ScreenAdaptation.LOG_TAG, "error hasNotchInScreen_Xiaomi Exception:" + e.getMessage());
        } finally {
            return result;
        }
    }

    /**
     * 系统设置中是否开启了刘海区域使用
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isSettingNotchEnable(Context context) {
        Activity activity = (Activity)context;
        //系统设置是否支持刘海屏使用。0:开启，1:关闭
        boolean isNotchSettingOpen = Settings.Global.getInt(activity.getContentResolver(), "force_black", 0) == 0;
        return isNotchSettingOpen;
    }

    /**
     * app中是否开启了刘海区域使用
     * @param context
     * @return
     */
    private boolean isSoftAppNotchEnable(Context context) {
        Activity activity = (Activity)context;
        try {
            Field extraFlagsField = activity.getWindow().getAttributes().getClass().getField("extraFlags");
            extraFlagsField.setAccessible(true);
            int extraFlags = (int) extraFlagsField.get(activity.getWindow().getAttributes());
            if((extraFlags & notchFlag) == notchFlag){
                return true;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    int GetNotchHeight(Context context)
    {
        int width = 0;
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            width = context.getResources().getDimensionPixelSize(resourceId);
        }

        return  width;
    }

    int GetNotchWidth(Context context)
    {
        int width = 0;
        int resourceId = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (resourceId > 0) {
            width = context.getResources().getDimensionPixelSize(resourceId);
        }

        return  width;
    }
}
