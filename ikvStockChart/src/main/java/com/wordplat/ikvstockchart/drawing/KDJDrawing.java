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
 * <p>KDJDrawing</p>
 * <p>Date: 2017/3/15</p>
 *
 * @author afon
 */

public class KDJDrawing implements IDrawing {

    private Paint axisPaint; // X 轴和 Y 轴的画笔
    private Paint kPaint;
    private Paint dPaint;
    private Paint jPaint;

    private final RectF indexRect = new RectF();
    private AbstractRender render;

    private float[] xPointBuffer = new float[4];
    private float[] kBuffer = new float[4];
    private float[] dBuffer = new float[4];
    private float[] jBuffer = new float[4];

    private float[] gridBuffer = new float[2];

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

        if (kPaint == null) {
            kPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            kPaint.setStyle(Paint.Style.STROKE);
        }

        if (dPaint == null) {
            dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            dPaint.setStyle(Paint.Style.STROKE);
        }

        if (jPaint == null) {
            jPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            jPaint.setStyle(Paint.Style.STROKE);
        }

        kPaint.setStrokeWidth(sizeColor.getKdjLineSize());
        dPaint.setStrokeWidth(sizeColor.getKdjLineSize());
        jPaint.setStrokeWidth(sizeColor.getKdjLineSize());

        kPaint.setColor(sizeColor.getKdjKLineColor());
        dPaint.setColor(sizeColor.getKdjDLineColor());
        jPaint.setColor(sizeColor.getKdjJLineColor());

        indexRect.set(contentRect);
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {
        final int count = (maxIndex - minIndex) * 4;
        if (xPointBuffer.length < count) {
            xPointBuffer = new float[count];
            kBuffer = new float[count];
            dBuffer = new float[count];
            jBuffer = new float[count];
        }

        final EntrySet entrySet = render.getEntrySet();
        final Entry entry = entrySet.getEntryList().get(currentIndex);
        final int i = currentIndex - minIndex;

        if (currentIndex < maxIndex - 1) {
            xPointBuffer[i * 4 + 0] = currentIndex + 0.5f;
            xPointBuffer[i * 4 + 1] = 0;
            xPointBuffer[i * 4 + 2] = currentIndex + 1 + 0.5f;
            xPointBuffer[i * 4 + 3] = 0;

            kBuffer[i * 4 + 0] = 0;
            kBuffer[i * 4 + 1] = entry.getK();
            kBuffer[i * 4 + 2] = 0;
            kBuffer[i * 4 + 3] = entrySet.getEntryList().get(currentIndex + 1).getK();

            dBuffer[i * 4 + 0] = 0;
            dBuffer[i * 4 + 1] = entry.getD();
            dBuffer[i * 4 + 2] = 0;
            dBuffer[i * 4 + 3] = entrySet.getEntryList().get(currentIndex + 1).getD();

            jBuffer[i * 4 + 0] = 0;
            jBuffer[i * 4 + 1] = entry.getJ();
            jBuffer[i * 4 + 2] = 0;
            jBuffer[i * 4 + 3] = entrySet.getEntryList().get(currentIndex + 1).getJ();
        }
    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        canvas.save();
        canvas.clipRect(indexRect);

        canvas.drawRect(indexRect, axisPaint);

        gridBuffer[0] = 0;
        gridBuffer[1] = (maxY + minY) / 2;
        render.mapPoints(null, gridBuffer);

        canvas.drawLine(indexRect.left, gridBuffer[1], indexRect.right, gridBuffer[1], axisPaint);

        render.mapPoints(xPointBuffer);
        render.mapPoints(null, kBuffer);
        render.mapPoints(null, dBuffer);
        render.mapPoints(null, jBuffer);

        final int count = (maxIndex - minIndex) * 4;

        for (int i = 0 ; i < count ; i = i + 4) {
            kBuffer[i + 0] = xPointBuffer[i + 0];
            kBuffer[i + 2] = xPointBuffer[i + 2];

            dBuffer[i + 0] = xPointBuffer[i + 0];
            dBuffer[i + 2] = xPointBuffer[i + 2];

            jBuffer[i + 0] = xPointBuffer[i + 0];
            jBuffer[i + 2] = xPointBuffer[i + 2];
        }

        canvas.drawLines(kBuffer, 0, count, kPaint);
        canvas.drawLines(dBuffer, 0, count, dPaint);
        canvas.drawLines(jBuffer, 0, count, jPaint);

        canvas.restore();
    }

    @Override
    public void onDrawOver(Canvas canvas) {

    }
}
