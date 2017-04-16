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

package com.wordplat.ikvstockchart.marker;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.wordplat.ikvstockchart.render.AbstractRender;

/**
 * <p>IMarkerView</p>
 * <p>Date: 2017/3/23</p>
 *
 * @author afon
 */

public interface IMarkerView {

    /**
     * 初始化 MarkerView
     *
     * @param contentRect 视图区域
     * @param render render
     */
    void onInitMarkerView(RectF contentRect, AbstractRender render);

    /**
     * onDrawMarkerView
     *
     * @param canvas canvas
     * @param highlightPointX 高亮中心坐标 x
     * @param highlightPointY 高亮中心坐标 y
     */
    void onDrawMarkerView(Canvas canvas, float highlightPointX, float highlightPointY);
}