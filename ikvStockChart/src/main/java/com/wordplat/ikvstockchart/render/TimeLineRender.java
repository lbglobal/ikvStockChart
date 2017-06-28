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
import android.graphics.RectF;

import com.wordplat.ikvstockchart.drawing.EmptyDataDrawing;
import com.wordplat.ikvstockchart.drawing.HighlightDrawing;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.drawing.IDrawing;
import com.wordplat.ikvstockchart.drawing.TimeLineDrawing;
import com.wordplat.ikvstockchart.drawing.TimeLineGridAxisDrawing;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>TimeLineRender 分时图</p>
 * <p>Date: 2017/3/9</p>
 *
 * @author afon
 */

public class TimeLineRender extends AbstractRender {

    private final RectF chartRect = new RectF(); // 分时图显示区域

    private final float[] extremumY = new float[2];

    private final List<IDrawing> drawingList = new ArrayList<>();

    public TimeLineRender() {
        drawingList.add(new TimeLineGridAxisDrawing());
        drawingList.add(new TimeLineDrawing());
        drawingList.add(new EmptyDataDrawing());
        drawingList.add(new HighlightDrawing());
    }

    public void addDrawing(IDrawing drawing) {
        drawingList.add(drawing);
    }

    public void clearDrawing() {
        drawingList.clear();
    }

    public RectF getChartRect() {
        return chartRect;
    }

    @Override
    public void setEntrySet(EntrySet entrySet) {
        super.setEntrySet(entrySet);

        postMatrixTouch(chartRect.width(), sizeColor.getTimeLineMaxCount());

        entrySet.computeTimeLineMinMax(0, entrySet.getEntryList().size());
        computeExtremumValue(extremumY, entrySet.getMinY(), entrySet.getDeltaY());
        postMatrixValue(chartRect.width(), chartRect.height(), extremumY[0], extremumY[1]);

        postMatrixOffset(chartRect.left, chartRect.top);
        scroll(0);
    }

    @Override
    public boolean canScroll(float dx) {
        return false;
    }

    @Override
    public boolean canDragging(float dx) {
        return false;
    }

    @Override
    public void onViewRect(RectF viewRect) {
        chartRect.set(viewRect);

        for (IDrawing drawing : drawingList) {
            drawing.onInit(chartRect, this);
        }
    }

    @Override
    public void zoomIn(float x, float y) {

    }

    @Override
    public void zoomOut(float x, float y) {

    }

    @Override
    public void render(Canvas canvas) {
        final int count = entrySet.getEntryList().size();
        final int lastIndex = count - 1;

        for (int i = 0 ; i < count ; i++) {
            for (IDrawing drawing : drawingList) {
                drawing.computePoint(0, lastIndex, i);
            }
        }

        for (IDrawing drawing : drawingList) {
            drawing.onComputeOver(canvas, 0, lastIndex, entrySet.getMinY(), entrySet.getMaxY());
        }

        for (IDrawing drawing : drawingList) {
            drawing.onDrawOver(canvas);
        }
    }
}
