package com.wordplat.quickstart.bean.response;

import android.util.Log;

import com.wordplat.quickstart.BuildConfig;
import com.wordplat.quickstart.bean.YahooKLineBean;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>YahooResponseParser</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public class YahooResponseParser implements ResponseParser {
    private static final String TAG = "YahooResponseParser";

    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "##d 服务器返回数据：" + result);
        }

        YahooResponse response = new YahooResponse();

        List<YahooKLineBean> yahooKLineBeanList = new ArrayList<>();

        StringReader stringReader = new StringReader(result);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        bufferedReader.readLine(); // 跳过首行

        String line;
        while((line = bufferedReader.readLine()) != null) {
            String v[] = line.split("[,]");
            float open = Float.parseFloat(v[1]);
            float high = Float.parseFloat(v[2]);
            float low = Float.parseFloat(v[3]);
            float close = Float.parseFloat(v[4]);

            int volume = Integer.parseInt(v[5]);

            yahooKLineBeanList.add(0, new YahooKLineBean(open, high, low, close, volume, v[0]));
        }

        response.setKLineList(yahooKLineBeanList);

        return response;
    }
}
