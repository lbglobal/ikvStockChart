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
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

import java.text.DecimalFormat;

/**
 * <p>StockIndexYLabelDrawing</p>
 * <p>Date: 2017/3/28</p>
 *
 * @author afon
 */

public class StockIndexYLabelDrawing implements IDrawing {

    private Paint yLabelPaint; // Y 轴标签的画笔
    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics(); // 用于 labelPaint 计算文字位置
    private final DecimalFormat decimalFormatter = new DecimalFormat("0.00");

    private final RectF indexRect = new RectF();

    private YLabelAlign yLabelAlign; // Y 轴标签对齐方向

    @Override
    public void onInit(RectF contentRect, AbstractRender render) {
        final SizeColor sizeColor = render.getSizeColor();

        if (yLabelPaint == null) {
            yLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            yLabelPaint.setTextSize(sizeColor.getYLabelSize());
        }
        yLabelPaint.setColor(sizeColor.getYLabelColor());
        yLabelPaint.getFontMetrics(fontMetrics);
        yLabelAlign = sizeColor.getYLabelAlign();
        if (yLabelAlign == YLabelAlign.RIGHT) {
            yLabelPaint.setTextAlign(Paint.Align.RIGHT);
        }

        indexRect.set(contentRect);
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {

    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        float labelX = yLabelAlign == YLabelAlign.LEFT ? indexRect.left + 5 : indexRect.right - 5;

        canvas.drawText(
                decimalFormatter.format(maxY),
                labelX,
                indexRect.top - fontMetrics.top,
                yLabelPaint);

        canvas.drawText(
                decimalFormatter.format(minY),
                labelX,
                indexRect.bottom - fontMetrics.bottom,
                yLabelPaint);
    }

    @Override
    public void onDrawOver(Canvas canvas) {

    }
}
