package com.wordplat.quickstart.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.wordplat.quickstart.BuildConfig;

import org.xutils.x;

/**
 * Created by afon on 2017/1/24.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.APPLICATION_ID.equals(getCurProcessName(this))) {
            AppRuntimeInitializer.INSTANCE.initRuntime(this);
        }

        x.Ext.init(this);
    }

    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}
