package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.response.BtcChinaResponse;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>BtcChinaPresenter</p>
 * <p>Date: 2017/4/16</p>
 *
 * @author afon
 */

public class BtcChinaPresenter extends BasePresenter<BtcChinaView> {

    public void getSimple(final int requestCode) {
        baseView.onStartRequest(requestCode);

        Subscription subscription = BtcChinaApiRequest.getHistoryData(5000, 300)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BtcChinaResponse>() {
                    @Override
                    public void call(BtcChinaResponse response) {
                        baseView.onFinishRequest(requestCode);
                        if (response.getBtcList() != null && response.getBtcList().size() > 0) {
                            baseView.onSuccess(requestCode, response);
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
}
