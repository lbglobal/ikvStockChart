package com.wordplat.quickstart.widget.pulllistview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.DefaultLoadMoreViewFooter;
import com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory;
import com.wordplat.ikvstockchart.compat.GestureMoveActionCompat;

/**
 * Created by liutao on 16/9/6.
 */

public class PullListLayout extends PtrFrameLayout {
    private ListRefreshView listRefreshView = null;

    private boolean isRefresh = true;

    private GestureMoveActionCompat gestureCompat;

    public PullListLayout(Context context) {
        this(context, null);
    }

    public PullListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullListLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {
        gestureCompat = new GestureMoveActionCompat(null);

        setRatioOfHeaderHeightToRefresh(1f);
        setResistance(3f);
        setDurationToCloseHeader(500);

        listRefreshView = new ListRefreshView(getContext());
        setHeaderView(listRefreshView);
        addPtrUIHandler(listRefreshView);

        ILoadMoreViewFactory loadMoreViewFactory = new DefaultLoadMoreViewFooter();
        setFooterView(loadMoreViewFactory);
    }

    public void setStatusHintColor(int colorResId) {
        listRefreshView.setStatusHintColor(colorResId);
    }

    public void setUpdateTimeColor(int colorResId) {
        listRefreshView.setUpdateTimeColor(colorResId);
    }

    public void setPullRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        // 解决 PullListLayout 垂直滑动（下拉刷新）与横向滑动冲突
        if (gestureCompat.onTouchEvent(e, e.getRawX(), e.getRawY())) {
            return dispatchTouchEventSupper(e);
        }
        // 解决双指缩放的冲突
        if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_POINTER_DOWN) {
            isRefresh = false;
        }

        if (isRefresh) {
            return super.dispatchTouchEvent(e);
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isRefresh = true;
                return super.dispatchTouchEvent(e);
        }

        return dispatchTouchEventSupper(e);
    }
}