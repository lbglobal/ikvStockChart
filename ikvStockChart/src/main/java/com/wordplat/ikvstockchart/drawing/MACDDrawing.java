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
import android.graphics.Paint;
import android.graphics.RectF;

import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

/**
 * <p>MACDDrawing</p>
 * <p>Date: 2017/3/14</p>
 *
 * @author afon
 */

public class MACDDrawing implements IDrawing {
    private static final String TAG = "MACDDrawing";

    private Paint axisPaint; // X 轴和 Y 轴的画笔
    private Paint deaPaint;
    private Paint difPaint;
    private Paint macdPaint;

    private final RectF indexRect = new RectF();
    private AbstractRender render;

    private float candleSpace = 0.1f;

    private float[] xPointBuffer = new float[4];
    private float[] deaBuffer = new float[4];
    private float[] diffBuffer = new float[4];

    private float[] gridBuffer = new float[2];

    private float[] xRectBuffer = new float[4];
    private float[] macdBuffer = new float[4];

    @Override
    public void onInit(RectF contentRect, AbstractRender render) {
        this.render = render;
        final SizeColor sizeColor = render.getSizeColor();

        if (axisPaint == null) {
            axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            axisPaint.setStyle(Paint.Style.STROKE);
        }
        axisPaint.setStrokeWidth(sizeColor.getAxisSize());
        axisPaint.setColor(sizeColor.getAxisColor());

        if (deaPaint == null) {
            deaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            deaPaint.setStyle(Paint.Style.STROKE);
        }

        if (difPaint == null) {
            difPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            difPaint.setStyle(Paint.Style.STROKE);
        }

        if (macdPaint == null) {
            macdPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            macdPaint.setStyle(Paint.Style.FILL);
            macdPaint.setStrokeWidth(sizeColor.getMacdLineSize());
        }
        macdPaint.setColor(sizeColor.getIncreasingColor());

        deaPaint.setStrokeWidth(sizeColor.getMacdLineSize());
        difPaint.setStrokeWidth(sizeColor.getMacdLineSize());

        deaPaint.setColor(sizeColor.getDeaLineColor());
        difPaint.setColor(sizeColor.getDiffLineColor());

        indexRect.set(contentRect);
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {
        final int count = (maxIndex - minIndex) * 4;
        if (xPointBuffer.length < count) {
            xPointBuffer = new float[count];
            deaBuffer = new float[count];
            diffBuffer = new float[count];
        }

        final EntrySet entrySet = render.getEntrySet();
        final Entry entry = entrySet.getEntryList().get(currentIndex);
        final int i = currentIndex - minIndex;

        if (currentIndex < maxIndex - 1) {
            xPointBuffer[i * 4 + 0] = currentIndex + 0.5f;
            xPointBuffer[i * 4 + 1] = 0;
            xPointBuffer[i * 4 + 2] = currentIndex + 1 + 0.5f;
            xPointBuffer[i * 4 + 3] = 0;

            deaBuffer[i * 4 + 0] = 0;
            deaBuffer[i * 4 + 1] = entry.getDea();
            deaBuffer[i * 4 + 2] = 0;
            deaBuffer[i * 4 + 3] = entrySet.getEntryList().get(currentIndex + 1).getDea();

            diffBuffer[i * 4 + 0] = 0;
            diffBuffer[i * 4 + 1] = entry.getDiff();
            diffBuffer[i * 4 + 2] = 0;
            diffBuffer[i * 4 + 3] = entrySet.getEntryList().get(currentIndex + 1).getDiff();
        }
    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        final EntrySet entrySet = render.getEntrySet();
        final SizeColor sizeColor = render.getSizeColor();

        canvas.save();
        canvas.clipRect(indexRect);

        canvas.drawRect(indexRect, axisPaint);

        gridBuffer[0] = 0;
        gridBuffer[1] = 0;
        render.mapPoints(null, gridBuffer);

        canvas.drawLine(indexRect.left, gridBuffer[1], indexRect.right, gridBuffer[1], axisPaint);

        render.mapPoints(xPointBuffer);
        render.mapPoints(null, deaBuffer);
        render.mapPoints(null, diffBuffer);

        for (int i = minIndex; i < maxIndex; i++) {
            Entry entry = entrySet.getEntryList().get(i);

            xRectBuffer[0] = i + candleSpace;
            xRectBuffer[1] = 0;
            xRectBuffer[2] = i + 1 - candleSpace;
            xRectBuffer[3] = 0;
            render.mapPoints(xRectBuffer);

            macdBuffer[0] = 0;
            macdBuffer[2] = 0;

            if (entry.getMacd() >= 0) {
                macdBuffer[1] = entry.getMacd();
                macdBuffer[3] = 0;
            } else {
                macdBuffer[1] = 0;
                macdBuffer[3] = entry.getMacd();
            }
            render.mapPoints(null, macdBuffer);

            if (macdBuffer[3] <= gridBuffer[1]) {
                macdPaint.setColor(sizeColor.getIncreasingColor());
            } else {
                macdPaint.setColor(sizeColor.getDecreasingColor());
            }

            canvas.drawRect(xRectBuffer[0], macdBuffer[1], xRectBuffer[2], macdBuffer[3], macdPaint);
        }

        final int count = (maxIndex - minIndex) * 4;

        for (int i = 0 ; i < count ; i = i + 4) {
            deaBuffer[i + 0] = xPointBuffer[i + 0];
            deaBuffer[i + 2] = xPointBuffer[i + 2];

            diffBuffer[i + 0] = xPointBuffer[i + 0];
            diffBuffer[i + 2] = xPointBuffer[i + 2];
        }

        canvas.drawLines(deaBuffer, 0, count, deaPaint);
        canvas.drawLines(diffBuffer, 0, count, difPaint);

        canvas.restore();
    }

    @Override
    public void onDrawOver(Canvas canvas) {

    }
}
