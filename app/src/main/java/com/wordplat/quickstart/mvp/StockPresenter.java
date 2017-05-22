package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.KLineBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>StockPresenter</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class StockPresenter extends BasePresenter<LoadingView> {

    private static final int LOAD_BETWEEN_DAYS = 100; // 日 K 一次加载多少天
    private static final int LOAD_BETWEEN_WEEK = 50; // 周 K 一次加载多少周
    private static final int LOAD_BETWEEN_MONTH = 50; // 月 K 一次加载多少个月

    private final Calendar currentBeginTime = Calendar.getInstance();
    private final Calendar currentEndTime = Calendar.getInstance();

    private List<KLineBean> kLineList;

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

        Subscription subscription = StockApiRequest.getKLine(stockCode,
                calendar.getTime(),
                currentBeginTime.getTime(),
                getKLineType(kLineType))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<KLineBean>>() {
                    @Override
                    public void call(List<KLineBean> response) {
                        baseView.onFinishRequest(requestCode);

                        if (response == null || response.size() == 0) {
                            baseView.onResultEmpty(requestCode);
                        } else {
                            computeNextTime(currentBeginTime, kLineType, true);

                            kLineList = response;
                            baseView.onSuccess(requestCode);
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

        Subscription subscription = StockApiRequest.getKLine(stockCode,
                currentEndTime.getTime(),
                calendar.getTime(),
                getKLineType(kLineType))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<KLineBean>>() {
                    @Override
                    public void call(List<KLineBean> response) {
                        baseView.onFinishRequest(requestCode);

                        if (response == null || response.size() == 0) {
                            baseView.onResultEmpty(requestCode);
                        } else {
                            computeNextTime(currentEndTime, kLineType, false);

                            kLineList = response;
                            baseView.onSuccess(requestCode);
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

    public List<KLineBean> getkLineList() {
        return kLineList;
    }

    private String getKLineType(KLineType kLineType) {
        String type = "d";
        switch (kLineType) {
            case DAY:
                type = "D";
                break;

            case WEEK:
                type = "W";
                break;

            case MONTH:
                type = "M";
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
