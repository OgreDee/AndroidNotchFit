package com.util.notchfit.phone;

import android.content.Context;
import android.util.Log;

import com.util.notchfit.NotchProperty;
import com.util.notchfit.ScreenAdaptation;

public class PhoneFactory {
    public static NotchProperty QueryNotch(Context context)
    {
        VirtualPhone phone = GetAndroidPhone();
        return phone.Excute(context);
    }

    private static VirtualPhone GetAndroidPhone() {
        String manufacturer = android.os.Build.MANUFACTURER;
        String phoneUpperModel = manufacturer.toUpperCase();

        Log.i(ScreenAdaptation.LOG_TAG, "phoneUpperModel:" + phoneUpperModel);

        //map HUAWEI com.classname
        VirtualPhone phone = null;
        if (phoneUpperModel.contains("HUAWEI")) {
            phone = new HuaWeiPhone();
        } else if (phoneUpperModel.contains("XIAOMI")) {
            phone = new MiPhone();
        } else if (phoneUpperModel.contains("OPPO")) {
            phone = new OppoPhone();
        } else if (phoneUpperModel.contains("VIVO")) {
            phone = new VivoPhone();
        } else
        {
            phone = new GooglePhone();
        }

        phone.SetManufacturer(manufacturer);
        return phone;
    }
}
