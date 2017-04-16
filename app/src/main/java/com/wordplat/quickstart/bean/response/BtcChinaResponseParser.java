package com.wordplat.quickstart.bean.response;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.bean.BtcBean;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.List;

/**
 * <p>ServerResponseParser</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class BtcChinaResponseParser implements ResponseParser {
    private static final String TAG = "BtcChinaResponseParser";

    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "##d 服务器返回数据：" + result);
        }

        List<BtcBean> btcList = JSON.parseArray(result, BtcBean.class);

        BtcChinaResponse btcResponse = new BtcChinaResponse();
        btcResponse.setBtcList(btcList);

        return btcResponse;
    }
}
