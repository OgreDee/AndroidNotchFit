/*
 *  https://blog.csdn.net/xj1009420846/article/details/80731855
 *  https://blog.csdn.net/u011810352/article/details/80587531
 *  https://blog.csdn.net/yuanpengs/article/details/82770032
 */

package com.util.notchfit;
import android.util.Log;
import android.content.Context;

import com.util.notchfit.phone.PhoneFactory;

public class ScreenAdaptation {
        public static String LOG_TAG = "moyou";


        public static NotchProperty QueryNotch(Context curContext) {

            Log.i(LOG_TAG, "curContext:" + curContext);

            return PhoneFactory.QueryNotch(curContext);
        }
    }
