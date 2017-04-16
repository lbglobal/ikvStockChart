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

package com.wordplat.ikvstockchart.render;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;

/**
 * <p>AbstractRender</p>
 * <p>Date: 2017/3/3</p>
 *
 * @author afon
 */

public abstract class AbstractRender {
    private static final String TAG = "AbstractRender";
    private static final boolean DEBUG = false;

    protected EntrySet entrySet; // entry 值

    protected SizeColor sizeColor = new SizeColor(); // 画笔大小和颜色配置

    protected final RectF viewRect = new RectF(); // 整个的视图区域大小
    protected boolean viewRectChange = false; // 视图区域是否发生变化

    private final Matrix matrixValue = new Matrix(); // 把值映射到屏幕像素的矩阵
    private final Matrix matrixTouch = new Matrix(); // 缩放和平移矩阵
    private final Matrix matrixOffset = new Matrix(); // 偏移矩阵
    private final Matrix matrixSimple = new Matrix(); // 单一矩阵
    private final Matrix matrixInvert = new Matrix(); // 用于缓存反转矩阵

    private final float[] touchPts = new float[2];
    private final float[] touchValues = new float[9]; // 存储缩放和平移信息
    private final float[] extremumY = new float[2]; // 当前显示区域内的 Y 轴范围
    private float extremumYScale = 1.3f; // Y 轴极值缩放因子
    private float extremumYDelta = 0; // Y 轴极值增量

    private boolean highlight = false;
    private final float[] highlightPoint = new float[2];

    private float minScrollOffset = 0; // 最小滚动量
    private float maxScrollOffset = 0; // 最大滚动量
    private float lastMaxScrollOffset = 0; // 上一次的最大滚动量
    private float overScrollOffset = 0; // 超出边界的滚动量

    /**
     * 根据给出的 view 坐标信息，进行视图上的一些设置
     */
    public abstract void onViewRect(RectF viewRect);

    /**
     * 放大
     *
     * @param x 在点(x, y)上放大
     * @param y 在点(x, y)上放大
     */
    public abstract void zoomIn(float x, float y);

    /**
     * 缩小
     *
     * @param x 在点(x, y)上缩小
     * @param y 在点(x, y)上缩小
     */
    public abstract void zoomOut(float x, float y);

    /**
     * 绘制
     */
    public abstract void render(Canvas canvas);

    /**
     * 获取 entry 数据
     */
    public EntrySet getEntrySet() {
        return entrySet;
    }

    /**
     * 设置 entry 数据
     */
    public void setEntrySet(EntrySet entrySet) {
        this.entrySet = entrySet;
    }

    /**
     * 获取颜色尺寸配置
     */
    public SizeColor getSizeColor() {
        return sizeColor;
    }

    /**
     * 设置颜色尺寸配置
     */
    public void setSizeColor(SizeColor sizeColor) {
        this.sizeColor = sizeColor;
    }

    /**
     * 获取整个的视图区域大小
     */
    public RectF getViewRect() {
        return viewRect;
    }

    /**
     * 设置整个的视图区域大小
     */
    public void setViewRect(RectF viewRect) {
        if (this.viewRect.height() > 0 && !this.viewRect.equals(viewRect)) {
            viewRectChange = true;
        }

        this.viewRect.set(viewRect);
    }

    /**
     * 获取 Y 轴极值缩放因子
     */
    public float getExtremumYScale() {
        return extremumYScale;
    }

    /**
     * 设置 Y 轴极值缩放因子
     */
    public void setExtremumYScale(float extremumYScale) {
        this.extremumYScale = extremumYScale;
    }

    /**
     * 获取 Y 轴极值增量
     */
    public float getExtremumYDelta() {
        return extremumYDelta;
    }

    /**
     * 设置 Y 轴极值增量
     */
    public void setExtremumYDelta(float extremumYDelta) {
        this.extremumYDelta = extremumYDelta;
    }

    public void onHighlight(float x, float y) {
        if (entrySet.getEntryList().size() > 0) {
            highlight = true;

            highlightPoint[0] = x;
            highlightPoint[1] = y;
        }
    }

    public void onCancelHighlight() {
        highlight = false;

        highlightPoint[0] = -1;
        highlightPoint[1] = -1;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public float[] getHighlightPoint() {
        return highlightPoint;
    }

    /**
     * 是否可以滚动
     */
    public boolean canScroll(float dx) {
        final float offset = touchValues[Matrix.MTRANS_X] - dx;

        if (offset < -maxScrollOffset && touchValues[Matrix.MTRANS_X] <= -maxScrollOffset) {
            return false;
        }
        if (offset > minScrollOffset && touchValues[Matrix.MTRANS_X] >= minScrollOffset) {
            return false;
        }

        return true;
    }

    /**
     * 是否可以拖动
     *
     * @param dx 变化量
     */
    public boolean canDragging(float dx) {
        return true;
    }

    /**
     * 获取超出边界滚动的距离
     */
    public float getOverScrollOffset() {
        return overScrollOffset;
    }

    /**
     * 更新超出边界滚动的距离
     *
     * @param dx 变化量
     */
    public void updateOverScrollOffset(float dx) {
        overScrollOffset += -dx;
    }

    /**
     * 获取最大滚动量
     */
    public float getMaxScrollOffset() {
        return -maxScrollOffset;
    }

    /**
     * 获取当前滚动量
     */
    public float getCurrentTransX() {
        return touchValues[Matrix.MTRANS_X];
    }

    /**
     * 无边界检测地更新当前滚动量
     *
     * @param dx 变化量
     */
    public void updateCurrentTransX(float dx) {
        matrixTouch.getValues(touchValues);

        touchValues[Matrix.MTRANS_X] += -dx;

        matrixTouch.setValues(touchValues);
    }

    /**
     * 无边界检测地直接设置当前滚动量
     *
     * @param transX 当前滚动位置。此值为正时将被程序视为负，因为这里的滚动量用负数表示。
     */
    public void setCurrentTransX(float transX) {
        matrixTouch.getValues(touchValues);

        touchValues[Matrix.MTRANS_X] = transX > 0 ? -transX : transX;

        matrixTouch.setValues(touchValues);
    }

    /**
     * 获取给定的 entryIndex 对应的滚动偏移量
     *
     * @param entryIndex entry 索引
     */
    public float getTransX(int entryIndex) {
        touchPts[0] = entryIndex;
        touchPts[1] = 0;

        matrixValue.mapPoints(touchPts);
        matrixTouch.mapPoints(touchPts);

        matrixTouch.getValues(touchValues);

        return touchValues[Matrix.MTRANS_X] + touchPts[0];
    }

    /**
     * 更新当前滚动量，当滚动到边界时将不能再滚动
     *
     * @param dx 变化量
     */
    public void scroll(float dx) {
        matrixTouch.getValues(touchValues);

        touchValues[Matrix.MTRANS_X] += -dx;
        overScrollOffset = 0;

        if (touchValues[Matrix.MTRANS_X] < -maxScrollOffset) {
            touchValues[Matrix.MTRANS_X] = -maxScrollOffset;
        }
        if (touchValues[Matrix.MTRANS_X] > minScrollOffset) {
            touchValues[Matrix.MTRANS_X] = minScrollOffset;
        }

        matrixTouch.setValues(touchValues);
    }

    /**
     * 缩放
     *
     * @param contentRect 当前显示区域
     * @param visibleCount 当前显示区域的 X 轴方向上需要显示多少个 entry 值
     * @param x 在点(x, y)上缩放
     * @param y 在点(x, y)上缩放。由于 K 线图只会进行水平滚动，因此 y 值被忽略
     */
    public void zoom(RectF contentRect, float visibleCount, float x, float y) {
        if (x < contentRect.left) {
            x = contentRect.left;
        }
        if (x > contentRect.right) {
            x = contentRect.right;
        }

        matrixTouch.getValues(touchValues);

        final int minVisibleIndex;
        final int toMinVisibleIndex = (int) (visibleCount * (x - contentRect.left) / contentRect.width());

        touchPts[0] = x;
        touchPts[1] = 0;
        invertMapPoints(touchPts);

        if (touchPts[0] <= toMinVisibleIndex) {
            minVisibleIndex = 0;
        } else {
            minVisibleIndex = (int) Math.abs(touchPts[0] - toMinVisibleIndex);
        }

        touchValues[Matrix.MSCALE_X] = entrySet.getEntryList().size() / visibleCount;

        computeScrollRange(contentRect.width(), touchValues[Matrix.MSCALE_X]);

        touchValues[Matrix.MTRANS_X] = getTransX(visibleCount, minVisibleIndex);

        matrixTouch.setValues(touchValues);

        if (DEBUG) {
            Log.i(TAG, "##d zoom: touchValues[Matrix.MSCALE_X] = " + touchValues[Matrix.MSCALE_X]
                    + ", touchValues[Matrix.MTRANS_X] = " + touchValues[Matrix.MTRANS_X]
                    + ", visibleCount = " + visibleCount
                    + ", minVisibleIndex = " + minVisibleIndex);
        }
    }

    /**
     * 利用矩阵将 entry 的值映射到屏幕像素上
     *
     * @param pts 浮点数序列 [x0, y0, x1, y1, ...]
     */
    public void mapPoints(float[] pts) {
        matrixValue.mapPoints(pts);
        matrixTouch.mapPoints(pts);
        matrixOffset.mapPoints(pts);
    }

    /**
     * 按给定矩阵将 entry 的值映射到屏幕像素上
     *
     * @param matrix 矩阵
     * @param pts 浮点数序列 [x0, y0, x1, y1, ...]
     */
    public void mapPoints(Matrix matrix, float[] pts) {
        if (matrix != null) {
            matrixSimple.set(matrix);
        }

        if (matrixSimple.isIdentity()) {
            mapPoints(pts);
        } else {
            matrixSimple.mapPoints(pts);
        }
    }

    /**
     * 将基于屏幕像素的坐标反转成 entry 的值
     *
     * @param pts 浮点数序列 [x0, y0, x1, y1, ...]
     */
    public void invertMapPoints(float[] pts) {
        matrixInvert.reset();

        matrixOffset.invert(matrixInvert);
        matrixInvert.mapPoints(pts);

        matrixTouch.invert(matrixInvert);
        matrixInvert.mapPoints(pts);

        matrixValue.invert(matrixInvert);
        matrixInvert.mapPoints(pts);
    }

    /**
     * 将基于屏幕像素的坐标按给定矩阵反转到值
     *
     * @param matrix 矩阵
     * @param pts 浮点数序列 [x0, y0, x1, y1, ...]
     */
    public void invertMapPoints(Matrix matrix, float[] pts) {
        if (matrix != null) {
            matrixSimple.set(matrix);
        }

        if (matrixSimple.isIdentity()) {
            invertMapPoints(pts);

        } else {
            matrixInvert.reset();

            matrixSimple.invert(matrixInvert);
            matrixInvert.mapPoints(pts);
        }
    }

    /**
     * 值矩阵运算
     *
     * @param matrix 矩阵 matrix
     * @param rect 视图 rect
     * @param minY 较小的 Y 值
     * @param deltaY Y 轴范围
     */
    protected void postMatrixValue(Matrix matrix, RectF rect, float minY, float deltaY) {
        final float maxY = minY + deltaY;

        if (maxY > 0) {
            if (minY >= 0) {
                matrix.reset();
                matrix.postTranslate(0, -minY);
                matrix.postScale(1, -(rect.height() / deltaY));
                matrix.postTranslate(0, rect.bottom);
            } else {
                matrix.reset();
                matrix.postScale(1, -(rect.height() / deltaY));
                matrix.postTranslate(0, rect.top + maxY / deltaY * rect.height());
            }
        } else {
            matrix.reset();
            matrix.postTranslate(0, -maxY);
            matrix.postScale(1, -(rect.height() / deltaY));
            matrix.postTranslate(0, rect.top);
        }

        matrixSimple.set(matrix);
    }

    /**
     * 值矩阵运算
     *
     * @param width 当前显示区域的宽
     * @param height 当前显示区域的高
     * @param minY 显示区域内可见的 entry 的最小值
     * @param deltaY 显示区域内可见的 entry 最大值与最小值之差
     */
    protected void postMatrixValue(float width, float height, float minY, float deltaY) {
        final float scaleX = width / entrySet.getEntryList().size();
        final float scaleY = height / deltaY;

        matrixValue.reset();
        matrixValue.postTranslate(0, -minY);
        matrixValue.postScale(scaleX, -scaleY);
        matrixValue.postTranslate(0, height);

        matrixSimple.reset();
    }

    /**
     * 手势滑动缩放矩阵运算
     *
     * @param width 当前显示区域的宽
     * @param visibleCount 当前显示区域的 X 轴方向上需要显示多少个 entry 值
     */
    protected void postMatrixTouch(float width, float visibleCount) {
        final float scaleX = entrySet.getEntryList().size() / visibleCount;

        matrixTouch.reset();
        matrixTouch.postScale(scaleX, 1);

        if (maxScrollOffset != 0) {
            lastMaxScrollOffset = maxScrollOffset;
        }
        computeScrollRange(width, scaleX);

        if (DEBUG) {
            Log.i(TAG, "##d postMatrixTouch: currentOffset = " + touchValues[Matrix.MTRANS_X]
                    + ", maxScrollOffset = " + maxScrollOffset
                    + ", lastMaxScrollOffset = " + lastMaxScrollOffset
                    + ", overScrollOffset = " + overScrollOffset);
        }

        // 数据不满一屏，不需要滚动定位
        if (scaleX > 1) {
            if (touchValues[Matrix.MTRANS_X] > 0) {
                // 左滑加载完成之后定位到之前滚动的位置
                matrixTouch.postTranslate(touchValues[Matrix.MTRANS_X] - (maxScrollOffset - lastMaxScrollOffset), 0);

            } else if (touchValues[Matrix.MTRANS_X] < 0) {
                if (overScrollOffset != 0 && !viewRectChange) {
                    // 右滑加载完成之后定位到之前滚动的位置
                    matrixTouch.postTranslate(touchValues[Matrix.MTRANS_X], 0);

                } else {
                    // TODO 左滑、右滑加载中时转动屏幕方向，定位仍然有 BUG，我将花更多时间找到好的办法解决
                    // 转动屏幕方向导致矩形变化，定位到之前相同比例的滚动位置
                    touchValues[Matrix.MTRANS_X] = touchValues[Matrix.MTRANS_X] / lastMaxScrollOffset * maxScrollOffset;

                    if (DEBUG) {
                        Log.i(TAG, "##d postMatrixTouch: transX = " + touchValues[Matrix.MTRANS_X] + ", viewRectChange = " + viewRectChange);
                    }

                    matrixTouch.postTranslate(touchValues[Matrix.MTRANS_X], 0);

                    viewRectChange = false;
                }
            } else {
                // 通常首次加载时定位到最末尾
                setCurrentTransX(-maxScrollOffset);
            }
        }
    }

    /**
     * 偏移矩阵运算
     *
     * @param offsetX 偏移量 X
     * @param offsetY 偏移量 Y
     */
    protected void postMatrixOffset(float offsetX, float offsetY) {
        matrixOffset.reset();
        matrixOffset.postTranslate(offsetX, offsetY);
    }

    /**
     * 获取给定的 entryIndex 对应的滚动偏移量。在调用 {@link #computeScrollRange} 之后才能调用此方法
     *
     * @param visibleCount 当前显示区域的 X 轴方向上需要显示多少个 entry 值
     * @param entryIndex entry 索引
     */
    protected float getTransX(float visibleCount, int entryIndex) {
        final int entrySetSize = entrySet.getEntryList().size();
        if (entrySetSize <= visibleCount) {
            return 0;
        }
        float result = maxScrollOffset * entryIndex / (entrySetSize - visibleCount);
        result = Math.min(maxScrollOffset, result);

        return -result;
    }

    /**
     * 计算当前缩放下，X 轴方向的最小滚动值和最大滚动值
     *
     * @param width 当前显示区域的宽
     * @param scaleX X 轴方向的缩放
     */
    protected void computeScrollRange(float width, float scaleX) {
        minScrollOffset = 0;
        if (scaleX == 0) {
            lastMaxScrollOffset = 0;
            maxScrollOffset = 0;
        } else {
            maxScrollOffset = width * (scaleX - 1f);
        }
    }

    /**
     * 扩大显示区域内 Y 轴的范围，这看起来会好看一点
     */
    protected void computeExtremumValue(float[] extremumY, float minY, float deltaY) {
        computeExtremumValue(extremumY, minY, deltaY, extremumYScale, extremumYDelta);
    }

    /**
     * 扩大显示区域内 Y 轴的范围，这看起来会好看一点
     */
    protected void computeExtremumValue(float[] extremumY, float minY, float deltaY, float extremumYScale, float extremumYDelta) {
        final float deltaYScale = deltaY * extremumYScale - deltaY;
        extremumY[0] = minY - deltaYScale / 2 - extremumYDelta;
        extremumY[1] = deltaY + deltaYScale + extremumYDelta;
    }
}
