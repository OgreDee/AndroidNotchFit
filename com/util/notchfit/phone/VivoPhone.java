package com.util.notchfit.phone;
import android.content.Context;
import java.lang.reflect.Method;
import android.util.Log;

import com.util.notchfit.ScreenAdaptation;

/*
* https://blog.csdn.net/DJY1992/article/details/80683693
*/
class VivoPhone extends VirtualPhone {
    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    @Override
    protected boolean isNotchEnable_O(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e(ScreenAdaptation.LOG_TAG, "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e(ScreenAdaptation.LOG_TAG, "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }

    /*
     *  Vivo采用固定宽度为100px, 高度为27px
     */
    @Override
    protected int[] getNotchSize_O(Context context) {
        return new int[]{100,27};
    }
}
