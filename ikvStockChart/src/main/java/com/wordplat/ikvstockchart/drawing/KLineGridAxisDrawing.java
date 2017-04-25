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

import com.wordplat.ikvstockchart.align.YLabelAlign;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;
import com.wordplat.ikvstockchart.render.KLineRender;

import java.text.DecimalFormat;

/**
 * <p>KLineGridAxisDrawing</p>
 * <p>Date: 2017/3/9</p>
 *
 * @author afon
 */

public class KLineGridAxisDrawing implements IDrawing {

    private Paint xLabelPaint; // X 轴标签的画笔
    private Paint yLabelPaint; // Y 轴标签的画笔
    private Paint axisPaint; // X 轴和 Y 轴的画笔
    private Paint gridPaint; // k线图网格线画笔
    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics(); // 用于 labelPaint 计算文字位置
    private final DecimalFormat decimalFormatter = new DecimalFormat("0.00");

    private final RectF kLineRect = new RectF(); // K 线图显示区域
    private KLineRender render;

    private final float[] pointCache = new float[2];
    private float lineHeight;

    private YLabelAlign yLabelAlign; // Y 轴标签对齐方向

    @Override
    public void onInit(RectF contentRect, AbstractRender render) {
        this.render = (KLineRender) render;
        final SizeColor sizeColor = render.getSizeColor();

        if (xLabelPaint == null) {
            xLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        xLabelPaint.setTextSize(sizeColor.getXLabelSize());
        xLabelPaint.setColor(sizeColor.getXLabelColor());
        xLabelPaint.setTextAlign(Paint.Align.CENTER);
        xLabelPaint.getFontMetrics(fontMetrics);

        if (yLabelPaint == null) {
            yLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        yLabelPaint.setTextSize(sizeColor.getYLabelSize());
        yLabelPaint.setColor(sizeColor.getYLabelColor());
        yLabelAlign = sizeColor.getYLabelAlign();
        if (yLabelAlign == YLabelAlign.RIGHT) {
            yLabelPaint.setTextAlign(Paint.Align.RIGHT);
        }

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

        kLineRect.set(contentRect);

        lineHeight = kLineRect.height() / 4;
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {

    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        final EntrySet entrySet = render.getEntrySet();
        final SizeColor sizeColor = render.getSizeColor();
        // 绘制 最外层大框框
        canvas.drawRect(kLineRect, axisPaint);

        // 绘制 三条横向网格线
        for (int i = 0 ; i < 3 ; i++) {
            float lineTop = kLineRect.top + (i + 1) * lineHeight;
            canvas.drawLine(kLineRect.left, lineTop, kLineRect.right, lineTop, gridPaint);
        }

        canvas.save();
        canvas.clipRect(kLineRect.left, kLineRect.top, kLineRect.right, kLineRect.bottom + sizeColor.getXLabelViewHeight());

        // 每隔特定个 entry，绘制一条竖向网格线和 X 轴 label
        final int count = render.getZoomTimes() < 0 ? Math.abs(7 * render.getZoomTimes()) + 2 : 7;
        final int lastIndex = entrySet.getEntryList().size() - 1;
        for (int i = minIndex; i < maxIndex; i++) {
            // 跳过首个 entry 和最后一个 entry，因为画出来不好看
            if (i == 0 || i == lastIndex) {
                continue;
            }
            if (i % count == 0) {
                pointCache[0] = i + 0.5f;
                render.mapPoints(pointCache);

                canvas.drawText(
                        entrySet.getEntryList().get(i).getXLabel(),
                        pointCache[0],
                        kLineRect.bottom + render.getSizeColor().getXLabelSize(),
                        xLabelPaint);

                // 跳过超出显示区域的线
                if (pointCache[0] < kLineRect.left || pointCache[0] > kLineRect.right) {
                    continue;
                }

                canvas.drawLine(pointCache[0], kLineRect.top, pointCache[0], kLineRect.bottom, gridPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public void onDrawOver(Canvas canvas) {
        // 绘制 Y 轴 label
        for (int i = 0 ; i < 5 ; i++) {
            float lineTop = kLineRect.top + i * lineHeight;
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

            float labelX = yLabelAlign == YLabelAlign.LEFT ? kLineRect.left + 5 : kLineRect.right - 5;

            canvas.drawText(value, labelX, pointCache[0], yLabelPaint);
        }
    }
}
