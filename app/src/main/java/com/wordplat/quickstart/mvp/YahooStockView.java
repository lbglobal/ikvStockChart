package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.response.YahooResponse;

/**
 * <p>YahooStockView</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public interface YahooStockView extends BaseView {

    void onStartRequest(int requestCode);

    void onFinishRequest(int requestCode);

    void onSuccess(int requestCode, YahooResponse response);
}
