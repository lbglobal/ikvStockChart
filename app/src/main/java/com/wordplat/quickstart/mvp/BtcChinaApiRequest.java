package com.wordplat.quickstart.mvp;

import android.util.Log;

import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.bean.BtcBean;

import org.xutils.http.RequestParams;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * <p>BtcChinaApiRequest</p>
 * <p>Date: 2017/4/16</p>
 *
 * @author afon
 */

public class BtcChinaApiRequest extends BaseRequest {
    private static final String TAG = "BtcChinaApiRequest";

    private static final String URL = "https://data.btcchina.com/data/historydata?since=%d&limit=%d";

    public static Observable<List<BtcBean>> getHistoryData(final int id, final int limit) {
        return Observable.create(new Observable.OnSubscribe<List<BtcBean>>() {
            @Override
            public void call(Subscriber<? super List<BtcBean>> subscriber) {
                String url = String.format(Locale.ENGLISH,
                        URL,
                        id,
                        limit);

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "##d 请求参数: " + url);
                }

                requestArray(subscriber, new RequestParams(url), BtcBean.class);
            }
        }).subscribeOn(Schedulers.io());
    }
}
