package com.wordplat.ikvstockchart.drawing;

import android.graphics.Canvas;

/**
 * <p>KLineVolumeHighlightDrawing K线成交量的高亮绘制</p>
 * <p>Date: 2017/6/28</p>
 *
 * @author afon
 */

public class KLineVolumeHighlightDrawing extends HighlightDrawing {

    @Override
    public void onDrawOver(Canvas canvas) {
        // 绘制高亮 成交量的高亮线条不需要垂直移动
        if (render.isHighlight()) {
            final float[] highlightPoint = render.getHighlightPoint();

            canvas.save();
            canvas.clipRect(contentRect);

            canvas.drawLine(highlightPoint[0], contentRect.top, highlightPoint[0], contentRect.bottom, highlightPaint);

            canvas.restore();
        }
    }
}
