package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.KLineBean;
import com.wordplat.quickstart.bean.request.ServerRequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * <p>自己搭建的股票 API 服务器接口</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class StockApiRequest extends BaseRequest {
    private static final String TAG = "StockApiRequest";

    private static final String URL = "https://api.wordplat.com/ts/v1/get_k_data/%s";

    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public static Observable<List<KLineBean>> getKLine(final String stockCode,
                                                       final Date startDate,
                                                       final Date endData,
                                                       final String type) {
        return Observable.create(new Observable.OnSubscribe<List<KLineBean>>() {
            @Override
            public void call(Subscriber<? super List<KLineBean>> subscriber) {
                String url = String.format(Locale.ENGLISH, URL, stockCode);

                ServerRequestParams requestParams = new ServerRequestParams(url);
                requestParams.addRequestParams("start", sDateFormat.format(startDate));
                requestParams.addRequestParams("end", sDateFormat.format(endData));
                requestParams.addRequestParams("ktype", type);
                requestParams.commit();

                requestArray(subscriber, requestParams, KLineBean.class);
            }
        }).subscribeOn(Schedulers.io());
    }
}
