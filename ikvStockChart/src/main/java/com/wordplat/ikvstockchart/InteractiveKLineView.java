/*
 * Copyright (C) 2017 WordPlat Open Source Project
 *
 *      https://wordplat.com/InteractiveKLineView/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wordplat.ikvstockchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;

import com.wordplat.ikvstockchart.compat.ViewUtils;
import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.StockDataTest;
import com.wordplat.ikvstockchart.compat.GestureMoveActionCompat;
import com.wordplat.ikvstockchart.render.AbstractRender;
import com.wordplat.ikvstockchart.render.KLineRender;

/**
 * <p>交互式 K 线图</p>
 * <p>Date: 2017/3/10</p>
 *
 * @author afon
 */

public class InteractiveKLineView extends View {
    private static final String TAG = "InteractiveKLineView";
    private static final boolean DEBUG = false;

    // 与视图大小相关的属性
    private final RectF viewRect;
    private final float viewPadding;

    // 与数据加载、渲染相关的属性
    private AbstractRender render;
    private EntrySet entrySet;
    private KLineHandler kLineHandler;

    // 与滚动控制、滑动加载数据相关的属性
    private static final int OVERSCROLL_DURATION = 500; // dragging 松手之后回中的时间，单位：毫秒
    private static final int OVERSCROLL_THRESHOLD = 220; // dragging 的偏移量大于此值时即是一个有效的滑动加载
    private static final int KLINE_STATUS_IDLE = 0; // 空闲
    private static final int KLINE_STATUS_RELEASE_BACK = 2; // 放手，回弹到 loading 位置
    private static final int KLINE_STATUS_LOADING = 3; // 加载中
    private static final int KLINE_STATUS_SPRING_BACK = 4; // 加载结束，回弹到初始位置
    private int kLineStatus = KLINE_STATUS_IDLE;
    private int lastFlingX = 0;
    private int lastScrollDx = 0;
    private int lastEntrySize = 0; // 上一次的 entry 列表大小，用于判断是否成功加载了数据
    private int lastHighlightIndex = -1; // 上一次高亮的 entry 索引，用于减少回调
    private final ScrollerCompat scroller;

    // 与手势控制相关的属性
    private boolean onTouch = false;
    private boolean onLongPress = false;
    private boolean onDoubleFingerPress = false;
    private boolean onVerticalMove = false;
    private boolean onDragging = false;
    private boolean enableLeftRefresh = true;
    private boolean enableRightRefresh = true;

    public InteractiveKLineView(Context context) {
        this(context, null);
    }

    public InteractiveKLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InteractiveKLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        viewRect = new RectF();
        viewPadding = ViewUtils.dpTopx(context, 10);

        render = new KLineRender(context);

        gestureDetector.setIsLongpressEnabled(true);

        int touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        gestureCompat.setTouchSlop(touchSlop);

        final Interpolator interpolator = new Interpolator() {
            public float getInterpolation(float t) {
                t -= 1.0f;
                return t * t * t * t * t + 1.0f;
            }
        };

        scroller = ScrollerCompat.create(context, interpolator);

        render.setSizeColor(ViewUtils.getSizeColor(context, attrs, defStyleAttr));
    }

    public void setEntrySet(EntrySet set) {
        entrySet = set;
    }

    public void notifyDataSetChanged() {
        notifyDataSetChanged(true);
    }

    public void notifyDataSetChanged(boolean invalidate) {
        render.setViewRect(viewRect);
        render.onViewRect(viewRect);
        render.setEntrySet(entrySet);

        if (invalidate) {
            invalidate();
        }
    }

    public void setRender(AbstractRender render) {
        render.setSizeColor(this.render.getSizeColor());
        this.render = render;
    }

    public AbstractRender getRender() {
        return render;
    }

    public void setKLineHandler(KLineHandler kLineHandler) {
        this.kLineHandler = kLineHandler;
    }

    public RectF getViewRect() {
        return viewRect;
    }

    private final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onLongPress(MotionEvent e) {
            if (onTouch) {
                onLongPress = true;
                highlight(e.getX(), e.getY());
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (kLineHandler != null) {
                kLineHandler.onDoubleTap(e, e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (kLineHandler != null) {
                kLineHandler.onSingleTap(e, e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!onLongPress && !onDoubleFingerPress && !onVerticalMove) {
                if (onDragging && !render.canScroll(distanceX) && render.canDragging(distanceX)) {
                    dragging((int) distanceX);

                    if (DEBUG) {
                        Log.v(TAG, "##d dragging: -------> " + distanceX + ", maxScrollOffset = "
                                + render.getMaxScrollOffset() + ", tranX = " + render.getCurrentTransX());
                    }
                } else {
                    scroll((int) distanceX);

                    if (DEBUG) {
                        Log.v(TAG, "##d scroll: ------->  " + distanceX + ", maxScrollOffset = "
                                + render.getMaxScrollOffset() + ", tranX = " + render.getCurrentTransX());
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            lastFlingX = 0;
            if (!onLongPress && !onDoubleFingerPress && !onVerticalMove && render.canScroll(0)) {
                if (DEBUG) {
                    Log.d(TAG, "##d onFling: ------->");
                }

                scroller.fling(0, 0, (int) -velocityX, 0,
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                return true;
            } else {
                return false;
            }
        }
    });

    private final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            float f = detector.getScaleFactor();

            if (f < 1.0f) {
                render.zoomOut(detector.getFocusX(), detector.getFocusY());
            } else if (f > 1.0f) {
                render.zoomIn(detector.getFocusX(), detector.getFocusY());
            }
        }
    });

    private GestureMoveActionCompat gestureCompat = new GestureMoveActionCompat(null);

    private void highlight(float x, float y) {
        render.onHighlight(x, y);
        invalidate();

        int highlightIndex = render.getEntrySet().getHighlightIndex();
        Entry entry = render.getEntrySet().getHighlightEntry();

        if (entry != null && lastHighlightIndex != highlightIndex) {
            if (kLineHandler != null) {
                kLineHandler.onHighlight(entry, highlightIndex, x, y);
            }
            lastHighlightIndex = highlightIndex;
        }
    }

    private void cancelHighlight() {
        render.onCancelHighlight();
        invalidate();

        if (kLineHandler != null) {
            kLineHandler.onCancelHighlight();
        }
        lastHighlightIndex = -1;
    }

    /**
     * 滚动，这里只会进行水平滚动，到达边界时将不能继续滑动
     *
     * @param dx 变化量
     */
    public void scroll(float dx) {
        render.scroll(dx);
        invalidate();
    }

    /**
     * 拖动，不同于滚动，当 K 线图到达边界时，依然可以滑动，用来支持加载更多
     *
     * @param dx 变化量
     */
    private void dragging(float dx) {
        if (render.getMaxScrollOffset() < 0 || dx < 0) {
            render.updateCurrentTransX(dx);
            render.updateOverScrollOffset(dx);
            invalidate();
        }
    }

    /**
     * 更新滚动的距离，用于拖动松手后回中
     *
     * @param dx 变化量
     */
    private void releaseBack(float dx) {
        render.updateCurrentTransX(dx);
        render.updateOverScrollOffset(dx);
        invalidate();
    }

    /**
     * 更新滚动的距离，用于加载数据完成后滚动或者回中
     *
     * @param dx 变化量
     */
    private void springBack(float dx) {
        if (entrySet.getEntryList().size() > lastEntrySize) {
            scroll(dx);
        } else {
            releaseBack(dx);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewRect.set(viewPadding, viewPadding, w - viewPadding, h - viewPadding);

        if (DEBUG) {
            Log.i(TAG, "##d onSizeChanged: " + viewRect);
        }

        // 在 Android Studio 预览模式下，添加一些测试数据，可以把 K 线图预览出来
        if (isInEditMode()) {
            EntrySet entrySet = StockDataTest.parseKLineData(StockDataTest.KLINE);
            if (entrySet != null) {
                entrySet.computeStockIndex();
            }

            setEntrySet(entrySet);
        }
        if (entrySet == null) {
            entrySet = new EntrySet();
        }

        notifyDataSetChanged();
    }

    @Override
    public void computeScroll() {
        if (onVerticalMove) {
            return ;
        }

        if (scroller.computeScrollOffset()) {
            final int x = scroller.getCurrX();
            final int dx = x - lastFlingX;
            lastFlingX = x;

            if (onTouch) {
                scroller.abortAnimation();
            } else {
                if (kLineStatus == KLINE_STATUS_RELEASE_BACK) {
                    releaseBack(dx);
                } else if (kLineStatus == KLINE_STATUS_SPRING_BACK) {
                    springBack(dx);
                } else {
                    scroll(dx);
                }

                if (render.canScroll(dx) && !scroller.isFinished()) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }

            if (DEBUG) {
                Log.i(TAG, "##d computeScrollOffset: canScroll = " + render.canScroll(dx)
                        + ", overScrollOffset = " + render.getOverScrollOffset()
                        + ", dx = " + dx + ", tranX = " + render.getCurrentTransX());
            }
        } else {
            final float overScrollOffset = render.getOverScrollOffset();
            if (DEBUG) {
                Log.d(TAG, "##d overScrollOffset: canScroll = " + render.canScroll(0)
                        + ", overScrollOffset = " + overScrollOffset);
            }

            if (!onTouch && overScrollOffset != 0 && kLineStatus == KLINE_STATUS_IDLE) {
                lastScrollDx = 0;
                float dx = overScrollOffset;

                if (Math.abs(overScrollOffset) > OVERSCROLL_THRESHOLD) {
                    if (enableLeftRefresh && overScrollOffset > 0) {
                        lastScrollDx = (int) overScrollOffset - OVERSCROLL_THRESHOLD;

                        dx = lastScrollDx;
                    }

                    if (enableRightRefresh && overScrollOffset < 0) {
                        lastScrollDx = (int) overScrollOffset + OVERSCROLL_THRESHOLD;

                        dx = lastScrollDx;
                    }
                }

                if (DEBUG) {
                    Log.d(TAG, "##d startScroll: LOADING... dx = " + dx);
                }

                kLineStatus = KLINE_STATUS_RELEASE_BACK;
                lastFlingX = 0;
                scroller.startScroll(0, 0, (int) dx, 0, OVERSCROLL_DURATION);
                ViewCompat.postInvalidateOnAnimation(this);

            } else if (kLineStatus == KLINE_STATUS_RELEASE_BACK) {
                kLineStatus = KLINE_STATUS_LOADING;

                if (kLineHandler != null) {
                    lastEntrySize = entrySet.getEntryList().size();
                    if (lastScrollDx > 0) {
                        kLineHandler.onLeftRefresh();
                    } else if (lastScrollDx < 0) {
                        kLineHandler.onRightRefresh();
                    }
                } else {
                    refreshComplete();
                }
            } else {
                kLineStatus = KLINE_STATUS_IDLE;
            }
        }
    }

    /**
     * 加载完成
     */
    public void refreshComplete() {
        refreshComplete(false);
    }

    /**
     * 加载完成
     *
     * @param reverse 是否反转滚动的方向
     */
    public void refreshComplete(boolean reverse) {
        final int overScrollOffset = (int) render.getOverScrollOffset();

        if (DEBUG) {
            Log.i(TAG, "##d refreshComplete: refreshComplete... overScrollOffset = " + overScrollOffset);
        }

        if (overScrollOffset != 0) {
            kLineStatus = KLINE_STATUS_SPRING_BACK;
            lastFlingX = 0;
            scroller.startScroll(0, 0, reverse ? -overScrollOffset : overScrollOffset, 0, OVERSCROLL_DURATION);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean isRefreshing() {
        return kLineStatus == KLINE_STATUS_LOADING;
    }

    public void setEnableLeftRefresh(boolean enableLeftRefresh) {
        this.enableLeftRefresh = enableLeftRefresh;
    }

    public void setEnableRightRefresh(boolean enableRightRefresh) {
        this.enableRightRefresh = enableRightRefresh;
    }

    public boolean isHighlighting() {
        return render.isHighlight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean onHorizontalMove = gestureCompat.onTouchEvent(event, event.getX(), event.getY());
        final int action = MotionEventCompat.getActionMasked(event);

        onVerticalMove = false;

        if (action == MotionEvent.ACTION_MOVE) {
            if (!onHorizontalMove && !onLongPress && !onDoubleFingerPress && gestureCompat.isDragging()) {
                onTouch = false;
                onVerticalMove = true;
            }
        }

        getParent().requestDisallowInterceptTouchEvent(!onVerticalMove);

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        final int action = MotionEventCompat.getActionMasked(e);

        gestureDetector.onTouchEvent(e);
        scaleDetector.onTouchEvent(e);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                onTouch = true;
                onDragging = false;
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                onDoubleFingerPress = true;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                onDragging = true;
                if (onLongPress) {
                    highlight(e.getX(), e.getY());
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                onLongPress = false;
                onDoubleFingerPress = false;
                onTouch = false;
                onDragging = false;

                cancelHighlight();

                break;
            }
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        render.render(canvas);
    }
}
