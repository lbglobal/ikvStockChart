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

package com.wordplat.ikvstockchart.compat;

import android.view.MotionEvent;

/**
 * <p>横向移动、垂直移动 识别，解决滑动冲突用的</p>
 * <p>Date: 2017/1/22</p>
 *
 * @author afon
 */

public class GestureMoveActionCompat {
    private OnGestureMoveListener gestureMoveListener;

    /**
     * 本次 ACTION_DOWN 事件的坐标 x
     */
    private float lastMotionX;

    /**
     * 本次 ACTION_DOWN 事件的坐标 y
     */
    private float lastMotionY;

    /**
     * 当前滑动的方向。0 无滑动（视为点击）；1 垂直滑动；2 横向滑动
     */
    private int interceptStatus = 0;

    /**
     * 是否响应点击事件
     *
     * 因为有手指抖动的影响，有时候会产生少量的 ACTION_MOVE 事件，造成程序识别错误。
     * 如果需要减少识别错误的几率，使用 {@link GestureMoveDetectorCompat} 这个类。
     */
    private boolean mEnableClick = true;

    /**
     * 避免程序识别错误的一个阀值。只有触摸移动的距离大于这个阀值时，才认为是一个有效的移动。
     */
    private int touchSlop = 20;

    private boolean dragging = false;

    public GestureMoveActionCompat(OnGestureMoveListener onGestureMoveListener) {
        gestureMoveListener = onGestureMoveListener;
    }

    public void enableClick(boolean enableClick) {
        mEnableClick = enableClick;
    }

    public void setTouchSlop(int touchSlop) {
        this.touchSlop = touchSlop;
    }

    public boolean isDragging() {
        return dragging;
    }

    /**
     * @param e 事件 e
     * @param x 本次事件的坐标 x。可以是 e.getRawX() 或是 e.getX()，具体看情况
     * @param y 本次事件的坐标 y。可以是 e.getRawY() 或是 e.getY()，具体看情况
     *
     * @return 事件是否是横向滑动
     */
    public boolean onTouchEvent(MotionEvent e, float x, float y) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastMotionY = y;
                lastMotionX = x;
                interceptStatus = 0;
                dragging = false;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaY = Math.abs(y - lastMotionY);
                float deltaX = Math.abs(x - lastMotionX);

                /**
                 * 如果之前是垂直滑动，即使现在是横向滑动，仍然认为它是垂直滑动的
                 * 如果之前是横向滑动，即使现在是垂直滑动，仍然认为它是横向滑动的
                 * 防止在一个方向上来回滑动时，发生垂直滑动和横向滑动的频繁切换，造成识别错误
                 */
                if (interceptStatus != 1 &&
                        (dragging || deltaX > deltaY && deltaX > touchSlop)) {
                    interceptStatus = 2;
                    dragging = true;

                    if (gestureMoveListener != null) {
                        gestureMoveListener.onHorizontalMove(e, x, y);
                    }
                } else if (interceptStatus != 2 &&
                        (dragging || deltaX < deltaY && deltaY > touchSlop)) {
                    interceptStatus = 1;
                    dragging = true;

                    if (gestureMoveListener != null) {
                        gestureMoveListener.onVerticalMove(e, x, y);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (interceptStatus == 0) {
                    if (mEnableClick && gestureMoveListener != null) {
                        gestureMoveListener.onClick(e, x, y);
                    }
                }
                interceptStatus = 0;
                dragging = false;
                break;
        }
        return interceptStatus == 2;
    }

    public interface OnGestureMoveListener {

        /**
         * 横向移动
         */
        void onHorizontalMove(MotionEvent e, float x, float y);

        /**
         * 垂直移动
         */
        void onVerticalMove(MotionEvent e, float x, float y);

        /**
         * 点击事件
         */
        void onClick(MotionEvent e, float x, float y);
    }
}
