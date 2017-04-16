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

package com.wordplat.ikvstockchart.entry;

import android.graphics.Matrix;
import android.graphics.RectF;

import com.wordplat.ikvstockchart.drawing.IDrawing;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>股票指标</p>
 * <p>Date: 2017/3/14</p>
 *
 * @author afon
 */

public abstract class StockIndex {

    static final int STANDARD_HEIGHT = 300; // 股票指标视图预设高度，单位：像素

    private final Matrix matrix = new Matrix(); // 把值映射到屏幕像素的矩阵

    private final int height; // 股票指标视图高度，单位：像素

    private final RectF rect = new RectF();

    private final List<IDrawing> drawingList = new ArrayList<>();

    private int paddingTop = 0;

    private int paddingBottom = 0;

    private int paddingLeft = 0;

    private int paddingRight = 0;

    private boolean enable = true;

    /**
     * Y 轴上指标的最大值
     */
    private float maxY;

    /**
     * Y 轴上指标的最小值
     */
    private float minY;

    /**
     * Y 轴极值缩放因子
     */
    private float extremumYScale = 1.05f;

    /**
     * Y 轴极值增量
     */
    private float extremumYDelta = 0;

    public StockIndex(int height) {
        this.height = height;
    }

    public abstract void computeMinMax(int currentIndex, Entry entry);

    public void resetMinMax() {
        minY = Float.MAX_VALUE;
        maxY = -Float.MAX_VALUE;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public int getHeight() {
        return height;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(float left, float top, float right, float bottom) {
        rect.set(left, top, right, bottom);
    }

    public List<IDrawing> getDrawingList() {
        return drawingList;
    }

    public void addDrawing(IDrawing drawing) {
        drawingList.add(drawing);
    }

    public void removeDrawing(IDrawing drawing) {
        drawingList.remove(drawing);
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getExtremumYScale() {
        return extremumYScale;
    }

    public void setExtremumYScale(float extremumYScale) {
        this.extremumYScale = extremumYScale;
    }

    public float getExtremumYDelta() {
        return extremumYDelta;
    }

    public void setExtremumYDelta(float extremumYDelta) {
        this.extremumYDelta = extremumYDelta;
    }

    public float getDeltaY() {
        return maxY - minY;
    }
}
