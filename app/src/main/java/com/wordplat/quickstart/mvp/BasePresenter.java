package com.wordplat.quickstart.mvp;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.R;
import com.wordplat.quickstart.app.AppRuntime;
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

    public String getString(int stringResId) {
        if (stringResId != 0) {
            if (AppRuntime.sContext != null) {
                return AppRuntime.sContext.getResources().getString(stringResId);
            }
        }

        return "";
    }

    protected void handleError(int requestCode, Throwable throwable) {
        final String errMessage;

        if(throwable instanceof NoNetworkException) { // 没有网络
            errMessage = "onNoNetworkError --> " + requestCode;
            baseView.onNoNetworkError(requestCode);

        } else if(throwable instanceof NetworkTimeOutException) { // 网络超时
            errMessage = "onNetworkTimeOutError --> " + requestCode;
            baseView.onNetworkTimeOutError(requestCode);

        } else if(throwable instanceof ResultParseException) { // 服务器返回的 JSON 数据解析错误
            errMessage = "onResultParseError --> " + requestCode;
            baseView.onResultParseError(requestCode);

        } else if(throwable instanceof ResultEmptyException) { // 服务器返回结果为空
            errMessage = "onResultEmpty --> " + requestCode;
            baseView.onResultEmpty(requestCode);

        } else if(throwable instanceof ResultFailedException) { // 服务器返回错误码
            errMessage = "onResultFailed --> " + requestCode;
            baseView.onResultFailed(requestCode,
                    ((ResultFailedException) throwable).getErrCode(),
                    ((ResultFailedException) throwable).getErrMessage());

        } else { // 其它不知道的错误，除非在设置UI时发生了错误，否则不会走到这里
            errMessage = "Warning_Unknow -->" + requestCode;
            baseView.onShowWarning(requestCode, R.string.Warning_Unknow);
        }

        if(BuildConfig.DEBUG) {
            Log.e("BasePresenter", "##d handleError: " + errMessage);
            throwable.printStackTrace();
        }
    }
}
