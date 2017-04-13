package com.wordplat.quickstart.bean.response;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wordplat.quickstart.BuildConfig;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

/**
 * <p>ServerResponseParser</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class ServerResponseParser implements ResponseParser {
    private static final String TAG = "ServerResponseParser";

    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "##d 服务器返回数据：" + result);
        }

        return JSON.parseObject(result, resultClass);
    }
}
