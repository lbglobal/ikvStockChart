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

package com.wordplat.ikvstockchart.drawing;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.wordplat.ikvstockchart.render.AbstractRender;

/**
 * <p>IDrawing</p>
 * <p>Date: 2017/3/9</p>
 *
 * @author afon
 */

public interface IDrawing {

    /**
     * 初始化
     *
     * @param contentRect 视图区域
     * @param render render
     */
    void onInit(RectF contentRect, AbstractRender render);

    /**
     * 计算预绘制的坐标
     *
     * @param minIndex 当前视图区域的可见 entry 最小索引
     * @param maxIndex 当前视图区域的可见 entry 最大索引
     * @param currentIndex 当前循环中的 entry 索引
     */
    void computePoint(int minIndex, int maxIndex, int currentIndex);

    /**
     * 计算结束，开始绘制
     *
     * @param canvas canvas
     * @param minIndex 当前视图区域的可见 entry 最小索引
     * @param maxIndex 当前视图区域的可见 entry 最大索引
     * @param minY 当前视图区域的 Y 轴最小值
     * @param maxY 当前视图区域的 Y 轴最大值
     */
    void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY);

    /**
     * 绘制结束
     *
     * @param canvas canvas
     */
    void onDrawOver(Canvas canvas);
}
