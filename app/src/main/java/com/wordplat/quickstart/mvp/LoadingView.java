package com.wordplat.quickstart.mvp;

/**
 * <p>LoadingView</p>
 * <p>Date: 2017/5/15</p>
 *
 * @author afon
 */

public interface LoadingView extends BaseView {

    void onStartRequest(int requestCode);

    void onFinishRequest(int requestCode);

    void onSuccess(int requestCode);
}
