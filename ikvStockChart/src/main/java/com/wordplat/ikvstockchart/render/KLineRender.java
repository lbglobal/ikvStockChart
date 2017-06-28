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

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import com.wordplat.ikvstockchart.drawing.CandleDrawing;
import com.wordplat.ikvstockchart.drawing.EmptyDataDrawing;
import com.wordplat.ikvstockchart.drawing.HighlightDrawing;
import com.wordplat.ikvstockchart.drawing.IDrawing;
import com.wordplat.ikvstockchart.drawing.KLineGridAxisDrawing;
import com.wordplat.ikvstockchart.drawing.MADrawing;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.StockIndex;
import com.wordplat.ikvstockchart.marker.IMarkerView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>KLineRender K线图</p>
 * <p>Date: 2017/3/3</p>
 *
 * @author afon
 */

public class KLineRender extends AbstractRender {
    private static final String TAG = "KLineRender";

    private static final float ZOOM_IN_FACTOR = 1.4f;
    private static final float ZOOM_OUT_FACTOR = 0.7f;
    private static final int ZOOM_DURATION = 1000;

    private static final float LANDSCAPE_PORTRAIT_FACTOR = 1.8235294f;

    private final Context context;
    private final RectF kLineRect = new RectF(); // K 线图显示区域

    private final float[] extremumY = new float[2];
    private final float[] contentPts = new float[2];

    /**
     * 当前缩放下显示的 entry 数量
     */
    private int currentVisibleCount = -1;

    /**
     * 竖屏时各级别缩放下显示的 entry 数量
     */
    private int[] portraitVisibleCountBuffer = new int[7];

    /**
     * 横屏时各级别缩放下显示的 entry 数量
     */
    private int[] landscapeVisibleCountBuffer = new int[7];

    /**
     * 用户手势控制的缩放次数。
     * 此值为 0 时，表示无缩放，为正值时，表示放大了 zoomTimes 倍，为负值时，表示缩小了 zoomTimes 倍
     * 此值受 {@link #zoomInTimes} 和 {@link #zoomOutTimes} 限制
     */
    private int zoomTimes = 0;

    /**
     * 最多放大倍数
     */
    private int zoomInTimes = 3;

    /**
     * 最多缩小倍数
     */
    private int zoomOutTimes = 3;

    /**
     * 缩放动画
     */
    private final ValueAnimator zoomAnimator = new ValueAnimator();
    private float zoomPivotX;
    private float zoomPivotY;

    private int minVisibleIndex;
    private int maxVisibleIndex;

    private final List<IDrawing> kLineDrawingList = new ArrayList<>();
    private final KLineGridAxisDrawing kLineGridAxisDrawing = new KLineGridAxisDrawing();
    private final CandleDrawing candleDrawing = new CandleDrawing();
    private final MADrawing maDrawing = new MADrawing();
    private final EmptyDataDrawing emptyDataDrawing = new EmptyDataDrawing();
    private final HighlightDrawing highlightDrawing = new HighlightDrawing();

    private final List<StockIndex> stockIndexList = new ArrayList<>(); // 股票指标列表

    public KLineRender(Context context) {
        this.context = context;

        kLineDrawingList.add(kLineGridAxisDrawing);
        kLineDrawingList.add(candleDrawing);
        kLineDrawingList.add(maDrawing);
        kLineDrawingList.add(emptyDataDrawing);
        kLineDrawingList.add(highlightDrawing);

        zoomAnimator.setDuration(ZOOM_DURATION);
        zoomAnimator.setInterpolator(new LinearInterpolator());
        zoomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int count = (int) animation.getAnimatedValue();

                zoom(kLineRect, count, zoomPivotX, zoomPivotY);
            }
        });
    }

    public void addDrawing(IDrawing drawing) {
        kLineDrawingList.add(drawing);
    }

    public void clearDrawing() {
        kLineDrawingList.clear();
    }

    public void addStockIndex(StockIndex stockIndex) {
        stockIndexList.add(stockIndex);
    }

    public void removeStockIndex(StockIndex stockIndex) {
        stockIndexList.remove(stockIndex);
    }

    public void clearStockIndex() {
        stockIndexList.clear();
    }

    public int getZoomTimes() {
        return zoomTimes;
    }

    public void setZoomTimes(int zoomTimes) {
        this.zoomTimes = zoomTimes;
    }

    public RectF getKLineRect() {
        return kLineRect;
    }

    public void addMarkerView(IMarkerView markerView) {
        highlightDrawing.addMarkerView(markerView);
    }

    @Override
    public void setEntrySet(EntrySet entrySet) {
        super.setEntrySet(entrySet);

        computeVisibleCount();

        postMatrixTouch(kLineRect.width(), currentVisibleCount);

        computeExtremumValue(extremumY, entrySet.getMinY(), entrySet.getDeltaY());
        postMatrixValue(kLineRect.width(), kLineRect.height(), extremumY[0], extremumY[1]);

        postMatrixOffset(kLineRect.left, kLineRect.top);
    }

    @Override
    public void onViewRect(RectF viewRect) {
        final float candleBottom = viewRect.bottom - sizeColor.getXLabelViewHeight();
        final int remainHeight = (int) (candleBottom - viewRect.top);

        int calculateHeight = 0;
        for (StockIndex stockIndex : stockIndexList) {
            if (stockIndex.isEnable()) {
                stockIndex.setEnable(stockIndex.getHeight() > 0
                        && calculateHeight + stockIndex.getHeight() < remainHeight);

                calculateHeight += stockIndex.getHeight();
            }
        }

        kLineRect.set(viewRect.left, viewRect.top, viewRect.right, candleBottom - calculateHeight);

        initDrawingList(kLineRect, kLineDrawingList);

        calculateHeight = 0;
        for (StockIndex stockIndex : stockIndexList) {
            if (stockIndex.isEnable()) {
                calculateHeight += stockIndex.getHeight();

                float top = kLineRect.bottom + sizeColor.getXLabelViewHeight() + calculateHeight - stockIndex.getHeight();
                float bottom = kLineRect.bottom + sizeColor.getXLabelViewHeight() + calculateHeight;

                stockIndex.setRect(
                        viewRect.left + stockIndex.getPaddingLeft(),
                        top + stockIndex.getPaddingTop(),
                        viewRect.right - stockIndex.getPaddingRight(),
                        bottom - stockIndex.getPaddingBottom());

                initDrawingList(stockIndex.getRect(), stockIndex.getDrawingList());
            }
        }
    }

    /**
     * 放大
     *
     * @param x 在点(x, y)上放大
     * @param y 在点(x, y)上放大
     */
    @Override
    public void zoomIn(float x, float y) {
        if (entrySet.getEntryList().size() == 0) {
            return ;
        }
        final int visibleCount = getCurrentVisibleCount(++zoomTimes);

        if (visibleCount != -1) {
            currentVisibleCount = visibleCount;

            zoom(kLineRect, currentVisibleCount, x, y);
        } else {
            zoomTimes = zoomOutTimes;
        }
    }

    /**
     * 缩小
     *
     * @param x 在点(x, y)上缩小
     * @param y 在点(x, y)上缩小
     */
    @Override
    public void zoomOut(float x, float y) {
        if (entrySet.getEntryList().size() == 0) {
            return ;
        }
        final int visibleCount = getCurrentVisibleCount(--zoomTimes);

        if (visibleCount != -1) {
            currentVisibleCount = visibleCount;

            zoom(kLineRect, currentVisibleCount, x, y);
        } else {
            zoomTimes = -zoomInTimes;
        }
    }

    @Override
    public void render(Canvas canvas) {
        final int count = entrySet.getEntryList().size();

        computeVisibleIndex();

        final float minY = count > 0 ? entrySet.getEntryList().get(entrySet.getMinYIndex()).getLow() : Float.NaN;
        final float maxY = count > 0 ? entrySet.getEntryList().get(entrySet.getMaxYIndex()).getHigh() : Float.NaN;
        renderDrawingList(canvas, kLineDrawingList, minY, maxY);

        for (StockIndex stockIndex : stockIndexList) {
            if (stockIndex.isEnable()) {
                float deltaY = stockIndex.getDeltaY();

                if (deltaY > 0) {
                    computeExtremumValue(extremumY,
                            stockIndex.getMinY(),
                            deltaY,
                            stockIndex.getExtremumYScale(),
                            stockIndex.getExtremumYDelta());
                    postMatrixValue(stockIndex.getMatrix(), stockIndex.getRect(), extremumY[0], extremumY[1]);

                    renderDrawingList(canvas, stockIndex.getDrawingList(), stockIndex.getMinY(), stockIndex.getMaxY());

                } else {
                    postMatrixValue(stockIndex.getMatrix(), stockIndex.getRect(), Float.NaN, Float.NaN);

                    renderDrawingList(canvas, stockIndex.getDrawingList(), Float.NaN, Float.NaN);
                }
            }
        }
    }

    private void zoomAnimate(int visibleCount, float pivotX, float pivotY) {
        zoomAnimator.setIntValues(currentVisibleCount, visibleCount);
        zoomPivotX = pivotX;
        zoomPivotY = pivotY;

        currentVisibleCount = visibleCount;

        zoomAnimator.start();
    }

    private void initDrawingList(RectF rect, List<IDrawing> drawingList) {
        for (IDrawing drawing : drawingList) {
            drawing.onInit(rect, this);
        }
    }

    private void renderDrawingList(Canvas canvas, List<IDrawing> drawingList, float minY, float maxY) {
        for (int i = minVisibleIndex ; i < maxVisibleIndex ; i++) {
            for (IDrawing drawing : drawingList) {
                drawing.computePoint(minVisibleIndex, maxVisibleIndex, i);
            }
        }

        for (IDrawing drawing : drawingList) {
            drawing.onComputeOver(canvas, minVisibleIndex, maxVisibleIndex, minY, maxY);
        }

        for (IDrawing drawing : drawingList) {
            drawing.onDrawOver(canvas);
        }
    }

    /**
     * 计算全部缩放条件下的 visibleCount 数值
     */
    private void computeVisibleCount() {
        zoomInTimes = Math.abs(sizeColor.getZoomInTimes() == 0 ? 3 : sizeColor.getZoomInTimes());
        zoomOutTimes = Math.abs(sizeColor.getZoomOutTimes() == 0 ? 3 : sizeColor.getZoomOutTimes());
        final int maxZoomTimes = zoomInTimes + zoomOutTimes + 1;

        if (portraitVisibleCountBuffer.length < maxZoomTimes) {
            portraitVisibleCountBuffer = new int[maxZoomTimes];
        }

        if (currentVisibleCount == -1) {
            currentVisibleCount = sizeColor.getPortraitDefaultVisibleCount();
            portraitVisibleCountBuffer[zoomOutTimes] = currentVisibleCount;

            for (int i = zoomInTimes ; i > 0 ; i--) {
                portraitVisibleCountBuffer[zoomOutTimes - i] = getZoomOutVisibleCount(currentVisibleCount, i);
            }

            for (int i = zoomOutTimes ; i > 0 ; i--) {
                portraitVisibleCountBuffer[zoomOutTimes + i] = getZoomInVisibleCount(currentVisibleCount, i);
            }
        }

        // 横屏时应该改变显示的 entry 数量，否则蜡烛图太粗了，不好看
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (landscapeVisibleCountBuffer.length < maxZoomTimes) {
                landscapeVisibleCountBuffer = new int[maxZoomTimes];
            }

            for (int i = 0 ; i <= zoomOutTimes + zoomInTimes ; i++) {
                landscapeVisibleCountBuffer[i] = (int) (portraitVisibleCountBuffer[i] * LANDSCAPE_PORTRAIT_FACTOR);
            }

            currentVisibleCount = getCurrentVisibleCount(zoomTimes);
        }
    }

    /**
     * 计算当前显示区域内的 X 轴范围
     */
    private void computeVisibleIndex() {
        contentPts[0] = kLineRect.left;
        contentPts[1] = 0;
        invertMapPoints(contentPts);

        minVisibleIndex = contentPts[0] <= 0 ? 0 : (int) contentPts[0];
        maxVisibleIndex = minVisibleIndex + currentVisibleCount + 1;
        if (maxVisibleIndex > entrySet.getEntryList().size()) {
            maxVisibleIndex = entrySet.getEntryList().size();
        }

        // 计算当前显示区域内 entry 在 Y 轴上的最小值和最大值
        entrySet.computeMinMax(minVisibleIndex, maxVisibleIndex, stockIndexList);

        computeExtremumValue(extremumY, entrySet.getMinY(), entrySet.getDeltaY());
        postMatrixValue(kLineRect.width(), kLineRect.height(), extremumY[0], extremumY[1]);
    }

    private int getCurrentVisibleCount(int zoomTimes) {
        final int index = zoomOutTimes + zoomTimes;
        if (0 <= index && index <= zoomOutTimes + zoomInTimes) {
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return landscapeVisibleCountBuffer[index];
            } else {
                return portraitVisibleCountBuffer[index];
            }
        }
        return -1;
    }

    private int getZoomInVisibleCount(int currentVisibleCount, int nZoomInTimes) {
        if (nZoomInTimes > 1) {
            return (int) (getZoomInVisibleCount(currentVisibleCount, nZoomInTimes - 1) / ZOOM_IN_FACTOR) + 1;
        } else {
            return (int) (currentVisibleCount / ZOOM_IN_FACTOR) + 1;
        }
    }

    private int getZoomOutVisibleCount(int currentVisibleCount, int nZoomOutTimes) {
        if (nZoomOutTimes > 1) {
            return (int) (getZoomOutVisibleCount(currentVisibleCount, nZoomOutTimes - 1) / ZOOM_OUT_FACTOR) + 1;
        } else {
            return (int) (currentVisibleCount / ZOOM_OUT_FACTOR) + 1;
        }
    }
}
