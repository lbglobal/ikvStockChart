package com.wordplat.quickstart.mvp;

/**
 * <p>StockListener</p>
 * <p>Date: 2017/4/11</p>
 *
 * @author afon
 */

public abstract class StockListener implements StockView {

    @Override
    public void onNoNetworkError(int requestCode) {

    }

    @Override
    public void onNetworkTimeOutError(int requestCode) {

    }

    @Override
    public void onSuccess(int requestCode) {

    }

    @Override
    public void onResultParseError(int requestCode) {

    }

    @Override
    public void onResultFailed(int requestCode, int errCode, String errMessage) {

    }

    @Override
    public void onResultEmpty(int requestCode) {

    }

    @Override
    public void onShowWarning(int requestCode, int errMessageResId) {

    }
}
