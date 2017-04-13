package com.wordplat.quickstart.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.wordplat.quickstart.activity.MainActivity;
import com.wordplat.quickstart.utils.AppUtils;

import java.lang.ref.WeakReference;

/**
 * APP 初始化程序
 * 
 * 用于 APP 运行时环境配置
 * 
 * Created by afon on 2016/12/20.
 */

public enum AppRuntimeInitializer {

    INSTANCE;

    private AppRuntimeInitializer() {

    }

    public void initRuntime(Application application) {
        final String TAG = "AppRuntime";

        AppRuntime.sContext = application.getApplicationContext();

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppRuntime.sActivities.add(activity);

                if (activity instanceof MainActivity) {
                    requestIMEIforMIfNeeded(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                AppRuntime.sActivityStops = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                AppRuntime.sActivityStops = new WeakReference<>(null);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppRuntime.sActivities.remove(activity);
            }
        });

        initVersion(application);
        initIMEI();
    }

    private void initVersion(Application application) {
        final String TAG = "AppRuntime";

        try {
            ApplicationInfo appInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            AppRuntime.serverVersion = appInfo.metaData.getString("server_http_url_version");
            if(AppRuntime.serverVersion.startsWith("ver")) {
                AppRuntime.serverVersion = AppRuntime.serverVersion.substring(3);
            }
            AppRuntime.appVersion = AppUtils.getAppVersionName(application);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, "### initVersion: serverVersion = " + AppRuntime.serverVersion + ", appVersion = " + AppRuntime.appVersion);
    }

    private void initIMEI() {
        final String TAG = "AppRuntime";

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(AppRuntime.sContext,
                android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) AppRuntime.sContext.getSystemService(Context.TELEPHONY_SERVICE);
            DeviceRuntime.IMEI = tm.getDeviceId();
            DeviceRuntime.DEVICE_ID = Settings.Secure.getString(AppRuntime.sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        Log.e(TAG, "### initIMEI: IMEI = " + DeviceRuntime.IMEI + ", DEVICE_ID = " + DeviceRuntime.DEVICE_ID);
    }

    private static final int REQUEST_IMEI_PERMISSION = 1346;

    /** Android 6.0 以上系统请求 IMEI 和设备 ID */
    private void requestIMEIforMIfNeeded(Activity activity) {
        if (TextUtils.isEmpty(DeviceRuntime.IMEI) || TextUtils.isEmpty(DeviceRuntime.DEVICE_ID)) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_IMEI_PERMISSION);
        }
    }

    /** 处理权限请求成功之后回调 */
    public void dealOnRequestPermissionsResult(Context context, int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMEI_PERMISSION:
                if (grantResults.length == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        initIMEI();
                    }
                }
                break;

            default:
                break;
        }
    }
}
