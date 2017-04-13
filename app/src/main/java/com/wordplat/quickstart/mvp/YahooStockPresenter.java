package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.response.YahooResponse;

import java.util.Calendar;
import java.util.Date;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>YahooStockPresenter</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class YahooStockPresenter extends BasePresenter<YahooStockView> {

    private static final int LOAD_BETWEEN_DAYS = 100; // 日 K 一次加载多少天
    private static final int LOAD_BETWEEN_WEEK = 50; // 周 K 一次加载多少周
    private static final int LOAD_BETWEEN_MONTH = 50; // 月 K 一次加载多少个月

    private final Calendar currentBeginTime = Calendar.getInstance();
    private final Calendar currentEndTime = Calendar.getInstance();

    public enum KLineType {
        DAY,
        WEEK,
        MONTH
    }

    public void loadFirst(int reqeustCode, String stockCode, KLineType kLineType) {
        currentEndTime.setTime(new Date());
        currentBeginTime.setTime(currentEndTime.getTime());

        loadPrev(reqeustCode, stockCode, kLineType);
    }

    public void loadPrev(final int requestCode, String stockCode, final KLineType kLineType) {
        baseView.onStartRequest(requestCode);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentBeginTime.getTime());
        computeNextTime(calendar, kLineType, true);

        Subscription subscription = YahooApiRequest.getKLine(stockCode,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                currentBeginTime.get(Calendar.YEAR),
                currentBeginTime.get(Calendar.MONTH),
                currentBeginTime.get(Calendar.DAY_OF_MONTH),
                getKLineType(kLineType))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<YahooResponse>() {
                    @Override
                    public void call(YahooResponse yahooResponse) {
                        baseView.onFinishRequest(requestCode);

                        if (yahooResponse.getKLineList().size() == 0) {
                            baseView.onResultEmpty(requestCode);
                        } else {
                            computeNextTime(currentBeginTime, kLineType, true);
                            baseView.onSuccess(requestCode, yahooResponse);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        baseView.onFinishRequest(requestCode);
                        handleError(requestCode, throwable);
                    }
                });

        addSubscription(subscription);
    }

    public void loadNext(final int requestCode, String stockCode, final KLineType kLineType) {
        baseView.onStartRequest(requestCode);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentEndTime.getTime());
        computeNextTime(calendar, kLineType, false);

        Subscription subscription = YahooApiRequest.getKLine(stockCode,
                currentEndTime.get(Calendar.YEAR),
                currentEndTime.get(Calendar.MONTH),
                currentEndTime.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                getKLineType(kLineType))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<YahooResponse>() {
                    @Override
                    public void call(YahooResponse yahooResponse) {
                        baseView.onFinishRequest(requestCode);

                        if (yahooResponse.getKLineList().size() == 0) {
                            baseView.onResultEmpty(requestCode);
                        } else {
                            computeNextTime(currentEndTime, kLineType, false);
                            baseView.onSuccess(requestCode, yahooResponse);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        baseView.onFinishRequest(requestCode);
                        handleError(requestCode, throwable);
                    }
                });

        addSubscription(subscription);
    }

    private String getKLineType(KLineType kLineType) {
        String type = "d";
        switch (kLineType) {
            case DAY:
                type = "d";
                break;

            case WEEK:
                type = "w";
                break;

            case MONTH:
                type = "m";
                break;
        }

        return type;
    }

    private void computeNextTime(Calendar calendar, KLineType kLineType, boolean reverse) {
        switch (kLineType) {
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, reverse ? -LOAD_BETWEEN_DAYS : LOAD_BETWEEN_DAYS);
                break;

            case WEEK:
                calendar.add(Calendar.WEEK_OF_MONTH, reverse ? -LOAD_BETWEEN_WEEK : LOAD_BETWEEN_WEEK);
                break;

            case MONTH:
                calendar.add(Calendar.MONTH, reverse ? -LOAD_BETWEEN_MONTH : LOAD_BETWEEN_MONTH);
                break;
        }
    }
}
