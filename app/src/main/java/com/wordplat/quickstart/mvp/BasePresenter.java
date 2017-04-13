package com.wordplat.quickstart.mvp;

import android.text.TextUtils;
import android.widget.EditText;

import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.R;
import com.wordplat.quickstart.mvp.exception.NetworkTimeOutException;
import com.wordplat.quickstart.mvp.exception.NoNetworkException;
import com.wordplat.quickstart.mvp.exception.ResultEmptyException;
import com.wordplat.quickstart.mvp.exception.ResultFailedException;
import com.wordplat.quickstart.mvp.exception.ResultParseException;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>BasePresenter</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public abstract class BasePresenter<T extends BaseView> {

    protected T baseView;

    private CompositeSubscription compositeSubscription;

    /**
     * 通常在 onResume 时调用此方法
     *
     * @param baseView
     */
    public void attachView(T baseView) {
        this.baseView = baseView;

        if(compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
            initSubscription();
        }
    }

    /**
     * 通常在 onPause 时调用此方法
     */
    public void detachView() {
        if(compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    protected void initSubscription() {}

    protected void addSubscription(Subscription subscription) {
        if(compositeSubscription == null) {
            subscription.unsubscribe();
        } else {
            compositeSubscription.add(subscription);
        }
    }

    /**
     * 取消所有订阅
     */
    public void cancel() {
        if(compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = new CompositeSubscription();
        }
    }

    public boolean isEmpty(EditText editText) {
        if(editText == null || editText.getText() == null || TextUtils.isEmpty(editText.getText().toString())) {
            return true;
        }
        return false;
    }

    protected void handleError(int requestCode, Throwable throwable) {
        if(BuildConfig.DEBUG) {
            throwable.printStackTrace();
        }

        if(throwable instanceof NoNetworkException) { // 没有网络
            baseView.onNoNetworkError(requestCode);

        } else if(throwable instanceof NetworkTimeOutException) { // 网络超时
            baseView.onNetworkTimeOutError(requestCode);

        } else if(throwable instanceof ResultParseException) { // 服务器返回的 JSON 数据解析错误
            baseView.onResultParseError(requestCode);

        } else if(throwable instanceof ResultEmptyException) { // 服务器返回结果为空
            baseView.onResultEmpty(requestCode);

        } else if(throwable instanceof ResultFailedException) { // 服务器返回错误码
            baseView.onResultFailed(requestCode,
                    ((ResultFailedException) throwable).getErrCode(),
                    ((ResultFailedException) throwable).getErrMessage());

        } else { // 其它不知道的错误，除非在设置UI时发生了错误，否则不会走到这里
            baseView.onShowWarning(requestCode, R.string.Warning_Unknow);
        }
    }
}
