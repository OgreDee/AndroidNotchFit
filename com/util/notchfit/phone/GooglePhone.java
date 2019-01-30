package com.util.notchfit.phone;

import android.content.Context;

final class GooglePhone extends VirtualPhone{

    @Override
    protected boolean isNotchEnable_O(Context context) {
        return false;
    }

    @Override
    protected int[] getNotchSize_O(Context context) {
        return new int[]{0,0};
    }

    @Override
    protected boolean isNotchEnable_P(Context context) {
        return super.isNotchEnable_P(context);
    }

    @Override
    protected int[] getNotchSize_P(Context context) {
        return super.getNotchSize_P(context);
    }
}
