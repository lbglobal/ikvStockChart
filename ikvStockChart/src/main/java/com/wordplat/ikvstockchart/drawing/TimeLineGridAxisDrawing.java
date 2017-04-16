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

import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

import java.text.DecimalFormat;

/**
 * <p>TimeLineGridAxisDrawing</p>
 * <p>Date: 2017/3/10</p>
 *
 * @author afon
 */

public class TimeLineGridAxisDrawing implements IDrawing {

    private Paint xLabelPaint; // X 轴标签的画笔
    private Paint yLabelPaint; // Y 轴标签的画笔
    private Paint axisPaint; // X 轴和 Y 轴的画笔
    private Paint gridPaint; // k线图网格线画笔
    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics(); // 用于 labelPaint 计算文字位置
    private final DecimalFormat decimalFormatter = new DecimalFormat("0.00");

    private final RectF chartRect = new RectF(); // 分时图显示区域
    private AbstractRender render;
    private SizeColor sizeColor;

    private final float[] pointCache = new float[2];
    private final float[] valueCache = new float[5];
    private float lineHeight;
    private float lineWidth;

    private int entrySetSize;
    
    @Override
    public void onInit(RectF contentRect, AbstractRender render) {
        this.render = render;
        this.sizeColor = render.getSizeColor();

        if (xLabelPaint == null) {
            xLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        xLabelPaint.setTextSize(sizeColor.getXLabelSize());
        xLabelPaint.setColor(sizeColor.getXLabelColor());
        xLabelPaint.getFontMetrics(fontMetrics);

        if (yLabelPaint == null) {
            yLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        yLabelPaint.setTextSize(sizeColor.getYLabelSize());

        if (axisPaint == null) {
            axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            axisPaint.setStyle(Paint.Style.STROKE);
        }

        if (gridPaint == null) {
            gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            gridPaint.setStyle(Paint.Style.STROKE);
        }

        axisPaint.setStrokeWidth(sizeColor.getAxisSize());
        axisPaint.setColor(sizeColor.getAxisColor());

        gridPaint.setStrokeWidth(sizeColor.getGridSize());
        gridPaint.setColor(sizeColor.getGridColor());

        chartRect.set(contentRect);

        lineHeight = chartRect.height() / 4;
        lineWidth = chartRect.width() / 4;
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {

    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        final EntrySet entrySet = render.getEntrySet();
        entrySetSize = entrySet.getEntryList().size();
        // 绘制 最外层大框框
        canvas.drawRect(chartRect, axisPaint);

        // 绘制 三条横向网格线
        for (int i = 0 ; i < 3 ; i++) {
            float lineTop = chartRect.top + (i + 1) * lineHeight;
            canvas.drawLine(chartRect.left, lineTop, chartRect.right, lineTop, gridPaint);
        }

        for (int i = 0 ; i < 5 ; i++) {
            float lineLeft = chartRect.left + i * lineWidth;

            if (i != 0 && i != 4) {
                // 绘制 三条竖向网格线
                canvas.drawLine(lineLeft, chartRect.top, lineLeft, chartRect.bottom, gridPaint);
                xLabelPaint.setTextAlign(Paint.Align.CENTER);
            } else if (i == 0) {
                xLabelPaint.setTextAlign(Paint.Align.LEFT);
            } else {
                xLabelPaint.setTextAlign(Paint.Align.RIGHT);
            }

            // 绘制 X 轴 label
            if (entrySetSize > 0) {
                canvas.drawText(
                        entrySet.getEntryList().get(i).getXLabel(),
                        lineLeft,
                        chartRect.bottom + render.getSizeColor().getXLabelSize(),
                        xLabelPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas canvas) {
        if (entrySetSize < 1) {
            return ;
        }
        // 绘制 Y 轴左边 label
        for (int i = 0 ; i < 5 ; i++) {
            float lineTop = chartRect.top + i * lineHeight;
            pointCache[1] = lineTop;
            render.invertMapPoints(pointCache);
            String value = decimalFormatter.format(pointCache[1]);

            if (i == 0) {
                pointCache[0] = lineTop - fontMetrics.top;
            } else if (i == 4) {
                pointCache[0] = lineTop - fontMetrics.bottom;
            } else {
                pointCache[0] = lineTop + fontMetrics.bottom;
            }

            if (i == 2) {
                yLabelPaint.setColor(sizeColor.getNeutralColor());
            } else if (i > 2) {
                yLabelPaint.setColor(sizeColor.getDecreasingColor());
            } else {
                yLabelPaint.setColor(sizeColor.getIncreasingColor());
            }
            yLabelPaint.setTextAlign(Paint.Align.LEFT);

            canvas.drawText(value, chartRect.left + 5, pointCache[0], yLabelPaint);

            valueCache[i] = pointCache[1];
        }

        // 绘制 Y 轴右边 label
        for (int i = 0 ; i < 5 ; i++) {
            float percent = (valueCache[i] - valueCache[2]) / valueCache[2] * 100;
            String value = decimalFormatter.format(percent);
            float lineTop = chartRect.top + i * lineHeight;

            if (i == 0) {
                pointCache[0] = lineTop - fontMetrics.top;
            } else if (i == 4) {
                pointCache[0] = lineTop - fontMetrics.bottom;
            } else {
                pointCache[0] = lineTop + fontMetrics.bottom;
            }

            if (i == 2) {
                yLabelPaint.setColor(sizeColor.getNeutralColor());
            } else if (i > 2) {
                yLabelPaint.setColor(sizeColor.getDecreasingColor());
            } else {
                yLabelPaint.setColor(sizeColor.getIncreasingColor());
            }
            yLabelPaint.setTextAlign(Paint.Align.RIGHT);

            canvas.drawText(value + "%", chartRect.right - 5, pointCache[0], yLabelPaint);
        }
    }
}
