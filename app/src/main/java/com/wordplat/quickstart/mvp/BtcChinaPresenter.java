package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.BtcBean;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>BtcChinaPresenter</p>
 * <p>Date: 2017/4/16</p>
 *
 * @author afon
 */

public class BtcChinaPresenter extends BasePresenter<LoadingView> {

    private List<BtcBean> btcList;

    public void getSimple(final int requestCode) {
        baseView.onStartRequest(requestCode);

        Subscription subscription = BtcChinaApiRequest.getHistoryData(5000, 600)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BtcBean>>() {
                    @Override
                    public void call(List<BtcBean> response) {
                        baseView.onFinishRequest(requestCode);
                        if (response != null && response.size() > 0) {
                            btcList = response;

                            baseView.onSuccess(requestCode);
                        } else {
                            baseView.onResultEmpty(requestCode);
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

    public List<BtcBean> getBtcList() {
        return btcList;
    }
}
