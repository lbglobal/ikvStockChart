/*
 * Copyright (C) 2017 WordPlat Open Source Project
 *
 *      https://wordplat.com/InteractiveKLineView/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wordplat.ikvstockchart.compat;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>统计方法执行时间的分析器</p>
 * <p>Date: 2016/12/29</p>
 *
 * @author afon
 */

public class PerformenceAnalyser {
    private static final String TAG = "PerformenceAnalyser";

    private static volatile PerformenceAnalyser instance;

    private long methodStartTime = 0;

    private Map<String, CountStops> countList = new HashMap<>();

    private AnalyserCallback analyserCallback;

    private PerformenceAnalyser() {
        analyserCallback = new AnalyserCallback() {
            @Override
            public void onWatcherFinish(String key, int item, long ns, long ms) {
                Log.d(TAG, "##d onWatcherFinish: " + key + "[" + item + "][" + ms + "ms]");
            }
        };
    }

    public static PerformenceAnalyser getInstance() {
        if (instance == null) {
            synchronized (PerformenceAnalyser.class) {
                if (instance == null) {
                    instance = new PerformenceAnalyser();
                }
            }
        }
        return instance;
    }

    /**
     * 设置观察点回调
     */
    public void setAnalyserCallback(AnalyserCallback analyserCallback) {
        this.analyserCallback = analyserCallback;
    }

    /**
     * 添加一个观察点
     */
    public void addWatcher() {
        methodStartTime = System.nanoTime();

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements != null) {
            if (stackTraceElements.length > 3) {
                String key = stackTraceElements[3].getClassName() + "->" + stackTraceElements[3].getMethodName();
                if (countList.containsKey(key)) {
                    CountStops count = countList.get(key);
                    count.stops++;

                    if (analyserCallback != null) {
                        analyserCallback.onWatcherFinish(key, count.stops, (methodStartTime - count.time), ((methodStartTime - count.time) / 1000000));
                    }

                    count.time = methodStartTime;
                } else {
                    CountStops count = new CountStops();
                    count.stops = 0;
                    count.time = methodStartTime;
                    countList.put(key, count);
                }
            }
        }
    }

    private static class CountStops {
        int stops;
        long time;
    }

    public interface AnalyserCallback {

        /**
         * 观察完成
         *
         * @param key key
         * @param item 第几项
         * @param ns 纳秒
         * @param ms 毫秒
         */
        void onWatcherFinish(String key, int item, long ns, long ms);
    }
}
