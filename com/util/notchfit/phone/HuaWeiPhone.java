package com.util.notchfit.phone;
import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.provider.Settings;
import android.util.Log;
import android.view.Window;

import com.util.notchfit.ScreenAdaptation;

/*
* https://developer.huawei.com/consumer/cn/devservice/doc/50114
*/
class HuaWeiPhone extends VirtualPhone {
    /*刘海屏全屏显示FLAG*/
    public static final int FLAG_NOTCH_SUPPORT=0x00010000;

    @Override
    protected boolean isNotchEnable_O(Context context) {
        return isHardwareNotchEnable(context) && isSettingNotchEnable(context) && isSoftAppNotchEnable(context);
    }

    @Override
    protected int[] getNotchSize_O(Context context) {
        int[] ret = new int[]{0, 0};

        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            Log.e(ScreenAdaptation.LOG_TAG, "getNotchSize Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 设备硬件是否是刘海屏。若设备无法获取属性值时，默认返回true，由其它条件做判断
     * @param context
     * @return
     */
    private boolean isHardwareNotchEnable(Context context) {
        boolean ret = false;

        try {

            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e(ScreenAdaptation.LOG_TAG, "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 系统设置中是否开启了刘海区域使用
     * @param context
     * @return
     */
    private boolean isSettingNotchEnable(Context context) {
        Activity activity = (Activity)context;
        //判断刘海屏系统设置开关是否关闭“隐藏显示区域”
        String DISPLAY_NOTCH_STATUS = "display_notch_status";
        // 0表示“默认开启”，1表示“隐藏显示区域”
        int mIsNotchSwitchOpen = Settings.Secure.getInt(activity.getContentResolver(),DISPLAY_NOTCH_STATUS, 0);
        boolean isSettingEnable = mIsNotchSwitchOpen == 0;
        Log.i(ScreenAdaptation.LOG_TAG ,"huawei setting enable: "+isSettingEnable);
        if(isSettingEnable){
            return true;
        }
        return false;
    }

    /**
     * app是否开启了刘海区域使用
     * @param context
     * @return
     */
    private boolean isSoftAppNotchEnable(Context context) {
        Activity activity = (Activity)context;

        //优先判断华为手机程序控制，检查是否用用程序开启了刘海区域的使用
        try {
            Window window = activity.getWindow();
            Field hwFlagsField = window.getAttributes().getClass().getField("hwFlags");
            hwFlagsField.setAccessible(true);
            int hwFlags = (int) hwFlagsField.get(window.getAttributes());
            boolean isAppSoftEnable = (hwFlags & FLAG_NOTCH_SUPPORT) == FLAG_NOTCH_SUPPORT;
            Log.i(ScreenAdaptation.LOG_TAG ,"huawei app soft enable:"+isAppSoftEnable);
            if (isAppSoftEnable) {
                return true;
            }
        }catch (Exception e){
            Log.i(ScreenAdaptation.LOG_TAG ,"huawei " + e.getMessage());
        }
        return false;
    }
}
