package com.wordplat.quickstart.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * APP工具
 *
 * @author liutao
 */

public class AppUtils {

    /**
     * 网络是否连接
     *
     * @param context
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();

        return netInfo == null || !netInfo.isConnected() ? false : true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dpTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取App版本号
     */
    public static String getAppVersionName(Context context) {
        String packageName = null;
        String versionName = null;
        int versionCode = 0;
        try {
            packageName = context.getPackageName();

            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;

            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}