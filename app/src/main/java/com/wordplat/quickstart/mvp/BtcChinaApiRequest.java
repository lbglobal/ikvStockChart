package com.wordplat.quickstart.mvp;

import android.util.Log;

import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.bean.response.BtcChinaResponse;

import org.xutils.http.RequestParams;

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

    public static Observable<BtcChinaResponse> getHistoryData(final int id, final int limit) {
        return Observable.create(new Observable.OnSubscribe<BtcChinaResponse>() {
            @Override
            public void call(Subscriber<? super BtcChinaResponse> subscriber) {
                String url = String.format(Locale.ENGLISH,
                        URL,
                        id,
                        limit);

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "##d 请求参数: " + url);
                }

                requestObject(subscriber, new RequestParams(url), BtcChinaResponse.class);
            }
        }).subscribeOn(Schedulers.io());
    }
}
