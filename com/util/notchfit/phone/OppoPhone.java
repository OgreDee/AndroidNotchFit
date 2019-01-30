package com.util.notchfit.phone;
import android.content.Context;


/*
 *  https://blog.csdn.net/DJY1992/article/details/80688802
 */
class OppoPhone extends VirtualPhone {

    @Override
    protected boolean isNotchEnable_O(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /*
     *  Oppo采用固定宽度为324px, 高度为80px
     */
    @Override
    protected int[] getNotchSize_O(Context context) {
        return new int[]{324, 80};
    }
}
