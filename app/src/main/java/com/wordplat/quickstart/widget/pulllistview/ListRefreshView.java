package com.wordplat.quickstart.widget.pulllistview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrUIHandler;
import com.chanven.lib.cptr.indicator.PtrIndicator;
import com.wordplat.quickstart.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉列表头部布局
 *
 * Created by liutao on 16/9/6.
 */

public class ListRefreshView extends FrameLayout implements PtrUIHandler {
    private TextView statusHint = null;
    private TextView updateTime = null;
    private ImageView loadingImage = null;

    private String UPDATE_TIME_KEY = null;
    private String mLastUpdateTimeKey;
    private SimpleDateFormat sDataFormat = null;
    private LastUpdateTimeUpdater mLastUpdateTimeUpdater = null;
    private long mLastUpdateTime = -1;
    private boolean mShouldShowLastUpdate;

    public ListRefreshView(Context context) {
        this(context, null);
    }

    public ListRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_list_refresh, null);
        statusHint = (TextView) view.findViewById(R.id.list_refresh_status);
        updateTime = (TextView) view.findViewById(R.id.list_refresh_updateTime);
        loadingImage = (ImageView) view.findViewById(R.id.list_refresh_loading_image);
        loadingImage.setVisibility(GONE);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(view);

        sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();
        try {
            mLastUpdateTimeKey = ((Activity)getContext()).getLocalClassName();
            UPDATE_TIME_KEY = getContext().getPackageName();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStatusHintColor(int colorResId) {
        statusHint.setTextColor(getResources().getColor(colorResId));
    }

    public void setUpdateTimeColor(int colorResId) {
        updateTime.setTextColor(getResources().getColor(colorResId));
    }

//    public void setRefreshAnimRes(int refreshImageAnimRes) {
//        loadingImage.setBackgroundResource(refreshImageAnimRes);
//    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mShouldShowLastUpdate = true;
        updateLastTime();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mShouldShowLastUpdate = true;
        updateLastTime();
        mLastUpdateTimeUpdater.start();
        crossRotateLineFromBottomUnderTouch(frame);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mShouldShowLastUpdate = false;
        statusHint.setText(com.chanven.lib.cptr.R.string.cube_ptr_refreshing);
        updateLastTime();
        mLastUpdateTimeUpdater.stop();
        loadingImage.setVisibility(VISIBLE);
        ((Animatable) loadingImage.getDrawable()).start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        statusHint.setText(com.chanven.lib.cptr.R.string.cube_ptr_refresh_complete);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(UPDATE_TIME_KEY, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();
        }
        ((Animatable) loadingImage.getDrawable()).stop();
        loadingImage.setVisibility(GONE);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            statusHint.setText(com.chanven.lib.cptr.R.string.cube_ptr_release_to_refresh);
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        if (frame.isPullToRefresh()) {
            statusHint.setText(com.chanven.lib.cptr.R.string.cube_ptr_pull_down_to_refresh);
        } else {
            statusHint.setText(com.chanven.lib.cptr.R.string.cube_ptr_pull_down);
        }
    }

    private void updateLastTime() {
        if (!TextUtils.isEmpty(mLastUpdateTimeKey) && !mShouldShowLastUpdate) {
            String time = getLastUpdateTime();
            if (!TextUtils.isEmpty(time)) {
                updateTime.setVisibility(VISIBLE);
                updateTime.setText(time);
            } else {
                updateTime.setVisibility(GONE);
            }
        }
    }

    private String getLastUpdateTime() {
        if (mLastUpdateTime == -1 && !TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = getContext().getSharedPreferences(UPDATE_TIME_KEY, 0).getLong(mLastUpdateTimeKey, -1);
        }
        if (mLastUpdateTime == -1) {
            return null;
        }
        long diffTime = new Date().getTime() - mLastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("最后更新: ");

        if (seconds < 60) {
            sb.append(seconds + getContext().getString(com.chanven.lib.cptr.R.string.cube_ptr_seconds_ago));
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(mLastUpdateTime);
                    sb.append(sDataFormat.format(date));
                } else {
                    sb.append(hours + getContext().getString(com.chanven.lib.cptr.R.string.cube_ptr_hours_ago));
                }

            } else {
                sb.append(minutes + getContext().getString(com.chanven.lib.cptr.R.string.cube_ptr_minutes_ago));
            }
        }
        return sb.toString();
    }

    private class LastUpdateTimeUpdater implements Runnable {
        private boolean mRunning = false;
        private void start() {
            if (TextUtils.isEmpty(mLastUpdateTimeKey)) {
                return;
            }
            mRunning = true;
            run();
        }

        private void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            updateLastTime();
            if (mRunning) {
                postDelayed(this, 1000);
            }
        }
    }
}