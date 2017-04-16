package com.wordplat.quickstart.mvp;

/**
 * <p>BtcChinaListener</p>
 * <p>Date: 2017/4/16</p>
 *
 * @author afon
 */

public abstract class BtcChinaListener implements BtcChinaView {

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
