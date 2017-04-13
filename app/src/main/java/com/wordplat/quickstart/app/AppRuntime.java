package com.wordplat.quickstart.app;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Created by afon on 2017/1/24.
 */

public class AppRuntime {

    public static Context sContext; // 静态类型的 Context，你懂的
    public static LinkedList<Activity> sActivities = new LinkedList<>(); // 查找当前的 Activity 栈用的
    public static WeakReference<Activity> sActivityStops = new WeakReference<>(null); // 快速查找位于栈顶的 Activity 用的

    public static String serverVersion = "1.0"; // 与服务器通信接口版本号。本值会自动读取到 gradle.properties 文件中定义的，不需要手动修改 *
    public static String appVersion = "1.0.0"; // 本APP的版本号。本值会自动读取到 AndroidManifest.xml 文件中定义的，不需要手动修改 *
}
