package com.wordplat.quickstart.mvp;

import android.text.TextUtils;
import android.widget.Toast;

import com.wordplat.quickstart.R;
import com.wordplat.quickstart.app.AppRuntime;

/**
 * <p>BaseViewListener</p>
 * <p>Date: 2017/5/15</p>
 *
 * @author afon
 */

public class BaseViewListener implements BaseView {
    @Override
    public void onNoNetworkError(int requestCode) {
        Toast.makeText(AppRuntime.sContext,
                AppRuntime.sContext.getResources().getString(R.string.Warning_No_Network),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkTimeOutError(int requestCode) {
        Toast.makeText(AppRuntime.sContext,
                AppRuntime.sContext.getResources().getString(R.string.Warning_Network_Timeout),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(int requestCode) {

    }

    @Override
    public void onResultParseError(int requestCode) {

    }

    @Override
    public void onResultFailed(int requestCode, int errCode, String errMessage) {
        if (!TextUtils.isEmpty(errMessage)) {
            Toast.makeText(AppRuntime.sContext, errMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResultEmpty(int requestCode) {

    }

    @Override
    public void onShowWarning(int requestCode, int errMessageResId) {
        Toast.makeText(AppRuntime.sContext,
                AppRuntime.sContext.getResources().getString(errMessageResId),
                Toast.LENGTH_SHORT).show();
    }
}
