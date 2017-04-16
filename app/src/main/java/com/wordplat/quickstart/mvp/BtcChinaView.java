package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.response.BtcChinaResponse;

/**
 * <p>BtcChinaView</p>
 * <p>Date: 2017/4/16</p>
 *
 * @author afon
 */

public interface BtcChinaView extends BaseView {

    void onStartRequest(int requestCode);

    void onFinishRequest(int requestCode);

    void onSuccess(int requestCode, BtcChinaResponse response);
}
