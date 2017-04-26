package com.wordplat.quickstart.mvp;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.app.AppRuntime;
import com.wordplat.quickstart.bean.response.IResultResponse;
import com.wordplat.quickstart.mvp.exception.NetworkTimeOutException;
import com.wordplat.quickstart.mvp.exception.NoNetworkException;
import com.wordplat.quickstart.mvp.exception.ResultEmptyException;
import com.wordplat.quickstart.mvp.exception.ResultFailedException;
import com.wordplat.quickstart.utils.AppUtils;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.List;

import rx.Subscriber;

/**
 * <p>BaseRequest</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class BaseRequest {
    private static final String TAG = "BaseRequest";

    /**
     * 网络请求 返回数据是一个 ServerResponse 对象
     *
     * @param subscriber     subscriber
     * @param requestParams  请求参数
     * @param responseObject 返回数据的 Class 类型
     */
    protected static <R extends IResultResponse> void requestServer(Subscriber<? super R> subscriber, RequestParams requestParams, Class<R> responseObject) {
        R response = handleRequestServer(subscriber, requestParams, responseObject);
        if (response != null) {
            subscriber.onNext(response);
            subscriber.onCompleted();
        }
    }

    /**
     * 网络请求 返回数据是一个 普通 对象
     *
     * @param subscriber     subscriber
     * @param requestParams  请求参数
     * @param responseObject 返回数据的 Class 类型
     */
    protected static <R> void requestObject(Subscriber<? super R> subscriber, RequestParams requestParams, Class<R> responseObject) {
        R response = handleRequestObject(subscriber, requestParams, responseObject);
        if (response != null) {
            subscriber.onNext(response);
            subscriber.onCompleted();
        }
    }

    /**
     * 网络请求 返回数据是一个 数组
     *
     * @param subscriber     subscriber
     * @param requestParams  请求参数
     * @param responseObject 返回数据的 Class 类型
     */
    protected static <R> void requestArray(Subscriber<? super List<R>> subscriber, RequestParams requestParams, Class<R> responseObject) {
        List<R> response = handleRequestArray(subscriber, requestParams, responseObject);
        if (response != null) {
            subscriber.onNext(response);
            subscriber.onCompleted();
        }
    }

    /**
     * 处理网络请求 含有基本逻辑判定
     *
     * @param subscriber     subscriber
     * @param requestParams  请求参数
     * @param responseObject 返回数据的 Class 类型
     * @return 成功返回 ServerResponse 对象，失败返回空对象
     */
    private static <T, R extends IResultResponse> R handleRequestServer(Subscriber<? super T> subscriber, RequestParams requestParams, Class<R> responseObject) {
        if (!AppUtils.isConnected(AppRuntime.sContext)) {
            subscriber.onError(new NoNetworkException());
            return null;
        }
        try {
            R result = x.http().postSync(requestParams, responseObject);

            if (result == null) {
                subscriber.onError(new ResultEmptyException());

            } else if (result.getResultCode() != 0) {
                if (result.getResultCode() == 1001) { // 单独抽出服务器返回为空数据时的错误码
                    subscriber.onError(new ResultEmptyException());

                } else {
                    subscriber.onError(new ResultFailedException(result.getResultCode(), result.getResultDescr()));

                }
            } else {
                return result;

            }

        } catch (Throwable throwable) {
            if (BuildConfig.DEBUG) {
                throwable.printStackTrace();
            }

            if (throwable instanceof SocketTimeoutException) { // 网络超时错误
                subscriber.onError(new NetworkTimeOutException());

            } else { // 其它情况一般是 JSON 数据解析操作空指针的错误，但也设置成空错误
                subscriber.onError(new ResultEmptyException());

            }
        }
        return null;
    }

    private static <T, R> R handleRequestObject(Subscriber<? super T> subscriber, RequestParams requestParams, Class<R> responseObject) {
        if (!AppUtils.isConnected(AppRuntime.sContext)) {
            subscriber.onError(new NoNetworkException());
            return null;
        }
        try {
            String result = x.http().postSync(requestParams, String.class);

            if (BuildConfig.DEBUG) {
                Log.i(TAG, "##d handleRequestArray: 服务器返回数据：" + result);
            }

            if (TextUtils.isEmpty(result)) {
                subscriber.onError(new ResultEmptyException());

            } else {
                R object = JSON.parseObject(result, responseObject);

                if (object == null) {
                    subscriber.onError(new ResultEmptyException());
                } else {
                    return object;
                }
            }

        } catch (Throwable throwable) {
            if (BuildConfig.DEBUG) {
                throwable.printStackTrace();
            }

            if (throwable instanceof SocketTimeoutException) { // 网络超时错误
                subscriber.onError(new NetworkTimeOutException());

            } else { // 其它情况一般是 JSON 数据解析操作空指针的错误，但也设置成空错误
                subscriber.onError(new ResultEmptyException());

            }
        }
        return null;
    }

    private static <T, R> List<R> handleRequestArray(Subscriber<? super T> subscriber, RequestParams requestParams, Class<R> responseObject) {
        if (!AppUtils.isConnected(AppRuntime.sContext)) {
            subscriber.onError(new NoNetworkException());
            return null;
        }
        try {
            String result = x.http().postSync(requestParams, String.class);

            if (BuildConfig.DEBUG) {
                Log.i(TAG, "##d handleRequestArray: 服务器返回数据：" + result);
            }

            if (TextUtils.isEmpty(result)) {
                subscriber.onError(new ResultEmptyException());

            } else {
                List<R> object = JSON.parseArray(result, responseObject);

                if (object == null || object.size() == 0) {
                    subscriber.onError(new ResultEmptyException());
                } else {
                    return object;
                }
            }

        } catch (Throwable throwable) {
            if (BuildConfig.DEBUG) {
                throwable.printStackTrace();
            }

            if (throwable instanceof SocketTimeoutException) { // 网络超时错误
                subscriber.onError(new NetworkTimeOutException());

            } else { // 其它情况一般是 JSON 数据解析操作空指针的错误，但也设置成空错误
                subscriber.onError(new ResultEmptyException());

            }
        }
        return null;
    }
}
