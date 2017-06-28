package com.wordplat.ikvstockchart.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.wordplat.ikvstockchart.compat.ViewUtils;
import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.AbstractRender;

/**
 * <p>KLineVolumeDrawing K线成交量的绘制</p>
 * <p>Date: 2017/6/28</p>
 *
 * @author afon
 */

public class KLineVolumeDrawing implements IDrawing {
    private static final String TAG = "KLineVolumeDrawing";

    private Paint axisPaint; // X 轴和 Y 轴的画笔
    private Paint ma5Paint; // 成交量的5日平均线画笔
    private Paint ma10Paint; // 成交量的10日平均线画笔
    private Paint candlePaint; // 成交量画笔

    private final RectF candleRect = new RectF(); // 绘图区域
    private AbstractRender render;

    // 计算 MA(5, 10) 线条坐标用的
    private float[] xPointBuffer = new float[4];
    private float[] ma5Buffer = new float[4];
    private float[] ma10Buffer = new float[4];

    private float candleSpace = 0.1f; // entry 与 entry 之间的间隙，默认 0.1f (10%)
    private float[] xRectBuffer = new float[4];
    private float[] candleBuffer = new float[4];

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

        if (ma5Paint == null) {
            ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ma5Paint.setStyle(Paint.Style.STROKE);
        }

        if (ma10Paint == null) {
            ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ma10Paint.setStyle(Paint.Style.STROKE);
        }

        if (candlePaint == null) {
            candlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            candlePaint.setStyle(Paint.Style.FILL);
            candlePaint.setStrokeWidth(sizeColor.getCandleBorderSize());
        }

        ma5Paint.setStrokeWidth(sizeColor.getMaLineSize());
        ma10Paint.setStrokeWidth(sizeColor.getMaLineSize());

        ma5Paint.setColor(sizeColor.getMa5Color());
        ma10Paint.setColor(sizeColor.getMa10Color());

        candleRect.set(contentRect);
    }

    @Override
    public void computePoint(int minIndex, int maxIndex, int currentIndex) {
        final int count = (maxIndex - minIndex) * 4;
        if (xPointBuffer.length < count) {
            xPointBuffer = new float[count];
            ma5Buffer = new float[count];
            ma10Buffer = new float[count];
        }

        final EntrySet entrySet = render.getEntrySet();
        final Entry entry = entrySet.getEntryList().get(currentIndex);
        final int i = currentIndex - minIndex;

        if (currentIndex < maxIndex - 1) {
            xPointBuffer[i * 4 + 0] = currentIndex + 0.5f;
            xPointBuffer[i * 4 + 1] = 0;
            xPointBuffer[i * 4 + 2] = currentIndex + 1 + 0.5f;
            xPointBuffer[i * 4 + 3] = 0;

            ma5Buffer[i * 4 + 0] = 0;
            ma5Buffer[i * 4 + 1] = (float) entry.getVolumeMa5();
            ma5Buffer[i * 4 + 2] = 0;
            ma5Buffer[i * 4 + 3] = (float) entrySet.getEntryList().get(currentIndex + 1).getVolumeMa5();

            ma10Buffer[i * 4 + 0] = 0;
            ma10Buffer[i * 4 + 1] = (float) entry.getVolumeMa10();
            ma10Buffer[i * 4 + 2] = 0;
            ma10Buffer[i * 4 + 3] = (float) entrySet.getEntryList().get(currentIndex + 1).getVolumeMa10();
        }
    }

    @Override
    public void onComputeOver(Canvas canvas, int minIndex, int maxIndex, float minY, float maxY) {
        final EntrySet entrySet = render.getEntrySet();
        final SizeColor sizeColor = render.getSizeColor();

        canvas.save();
        canvas.clipRect(candleRect);

        canvas.drawRect(candleRect, axisPaint);

        for (int i = minIndex; i < maxIndex; i++) {
            // 设置画笔颜色
            Entry entry = ViewUtils.setUpCandlePaint(candlePaint, entrySet, i, sizeColor);

            // 计算 成交量的矩形卓坐标
            xRectBuffer[0] = i + candleSpace;
            xRectBuffer[1] = 0;
            xRectBuffer[2] = i + 1 - candleSpace;
            xRectBuffer[3] = 0;
            render.mapPoints(xRectBuffer);

            candleBuffer[0] = 0;
            candleBuffer[1] = entry.getVolume();
            candleBuffer[2] = 0;
            candleBuffer[3] = minY;

            render.mapPoints(null, candleBuffer);

            if (Math.abs(candleBuffer[1] - candleBuffer[3]) < 1.f) { // 成交量非常小画一条直线
                canvas.drawRect(xRectBuffer[0], candleBuffer[1] - 2, xRectBuffer[2], candleBuffer[1], candlePaint);
            } else {
                canvas.drawRect(xRectBuffer[0], candleBuffer[1], xRectBuffer[2], candleBuffer[3] - axisPaint.getStrokeWidth(), candlePaint);
            }
        }

        // 映射坐标，绘制成交量的5日平均线和10日平均线
        render.mapPoints(xPointBuffer);
        render.mapPoints(null, ma5Buffer);
        render.mapPoints(null, ma10Buffer);

        final int count = (maxIndex - minIndex) * 4;

        for (int i = 0 ; i < count ; i = i + 4) {
            ma5Buffer[i + 0] = xPointBuffer[i + 0];
            ma5Buffer[i + 2] = xPointBuffer[i + 2];

            ma10Buffer[i + 0] = xPointBuffer[i + 0];
            ma10Buffer[i + 2] = xPointBuffer[i + 2];
        }

        // 使用 drawLines 方法比依次调用 drawLine 方法要快
        canvas.drawLines(ma5Buffer, 0, count, ma5Paint);
        canvas.drawLines(ma10Buffer, 0, count, ma10Paint);

        canvas.restore();
    }

    @Override
    public void onDrawOver(Canvas canvas) {

    }
}
