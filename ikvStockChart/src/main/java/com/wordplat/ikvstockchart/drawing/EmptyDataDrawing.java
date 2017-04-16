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

import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

/**
 * <p>EmptyDataDrawing</p>
 * <p>Date: 2017/3/21</p>
 *
 * @author afon
 */

public class EmptyDataDrawing implements IDrawing {

    private Paint textPaint;
    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    private AbstractRender render;
    private SizeColor sizeColor;
    private final RectF contentRect = new RectF();

    @Override
    public void onInit(RectF contentRect, AbstractRender render) {
        this.render = render;
        this.contentRect.set(contentRect);
        this.sizeColor = render.getSizeColor();

        if (textPaint == null) {
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {

    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {

    }

    @Override
    public void onDrawOver(Canvas canvas) {
        if (render.getEntrySet().getEntryList().size() == 0) {
            final String drawText;
            if (render.getEntrySet().isLoadingStatus()) {
                textPaint.setTextSize(sizeColor.getLoadingTextSize());
                textPaint.setColor(sizeColor.getLoadingTextColor());
                drawText = sizeColor.getLoadingText();
            } else {
                textPaint.setTextSize(sizeColor.getErrorTextSize());
                textPaint.setColor(sizeColor.getErrorTextColor());
                drawText = sizeColor.getErrorText();
            }

            textPaint.getFontMetrics(fontMetrics);

            canvas.drawText(drawText,
                    contentRect.width() / 2,
                    (contentRect.top + contentRect.bottom - fontMetrics.top - fontMetrics.bottom) / 2,
                    textPaint);
        }
    }
}
