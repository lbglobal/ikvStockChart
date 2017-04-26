package com.wordplat.quickstart.mvp;

import com.wordplat.quickstart.bean.KLineBean;

import java.util.List;

/**
 * <p>StockView</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public interface StockView extends BaseView {

    void onStartRequest(int requestCode);

    void onFinishRequest(int requestCode);

    void onSuccess(int requestCode, List<KLineBean> response);
}
