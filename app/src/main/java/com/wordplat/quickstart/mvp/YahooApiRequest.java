package com.wordplat.quickstart.mvp;

import android.util.Log;

import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.bean.response.YahooResponse;

import org.xutils.http.RequestParams;

import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * <p>雅虎财经股票 API 接口请求</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class YahooApiRequest extends BaseRequest {
    private static final String TAG = "YahooApiRequest";

    private static final String URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=%s&ignore=.csv";

    public static Observable<YahooResponse> getKLine(final String stockCode,
                                              final int startYear,
                                              final int startMonth,
                                              final int startDay,
                                              final int endYear,
                                              final int endMonth,
                                              final int endDay,
                                              final String type) {
        return Observable.create(new Observable.OnSubscribe<YahooResponse>() {
            @Override
            public void call(Subscriber<? super YahooResponse> subscriber) {
                String url = String.format(Locale.ENGLISH,
                        URL,
                        stockCode,
                        startMonth,
                        startDay,
                        startYear,
                        endMonth,
                        endDay,
                        endYear,
                        type);

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "##d 请求参数: " + url);
                }

                requestObject(subscriber, new RequestParams(url), YahooResponse.class);
            }
        }).subscribeOn(Schedulers.io());
    }
}
