package com.wordplat.quickstart.mvp;

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

import rx.Subscriber;

/**
 * <p>BaseRequest</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class BaseRequest {

    /**
     * 网络请求 返回数据是一个对象
     *
     * @param subscriber     subscriber
     * @param requestParams  请求参数
     * @param responseObject 返回数据的 Class 类型
     */
    protected static <R extends IResultResponse> void requestObject(Subscriber<? super R> subscriber, RequestParams requestParams, Class<R> responseObject) {
        R response = handleRequest(subscriber, requestParams, responseObject);
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
     * @return 成功返回 responseObject 对象，失败返回空对象
     */
    private static <T, R extends IResultResponse> R handleRequest(Subscriber<? super T> subscriber, RequestParams requestParams, Class<R> responseObject) {
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
}
