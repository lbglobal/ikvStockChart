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

package com.wordplat.ikvstockchart.entry;

import android.graphics.Paint;

import com.wordplat.ikvstockchart.align.XMarkerAlign;
import com.wordplat.ikvstockchart.align.YLabelAlign;
import com.wordplat.ikvstockchart.align.YMarkerAlign;

/**
 * <p>线条大小、颜色配置类</p>
 * <p>Date: 2017/3/8</p>
 *
 * @author afon
 */

public class SizeColor {

    ///////////////////////////////////////////////////////////////////////////
    // 与轴、网格有关的属性和方法
    ///////////////////////////////////////////////////////////////////////////

    private float xLabelSize = 21; // X 轴标签字符大小
    private int xLabelColor = 0xff282b34; // X 轴标签字符颜色
    private float xLabelViewHeight = 50; // X 轴 Label 区域的高度
    private float yLabelSize = 21; // Y 轴标签字符大小
    private int yLabelColor = 0xff282b34; // Y 轴标签字符颜色
    private YLabelAlign yLabelAlign = YLabelAlign.LEFT; // Y 轴标签对齐方向

    private float axisSize = 2f; // 轴线条大小
    private int axisColor = 0xffdddddd; // 轴线条颜色
    private float gridSize = 2f; // 网格线大小
    private int gridColor = 0xffdddddd; // 网格线颜色

    public float getXLabelSize() {
        return xLabelSize;
    }

    public void setXLabelSize(float xLabelSize) {
        this.xLabelSize = xLabelSize;
    }

    public int getXLabelColor() {
        return xLabelColor;
    }

    public void setXLabelColor(int xLabelColor) {
        this.xLabelColor = xLabelColor;
    }

    public float getXLabelViewHeight() {
        return xLabelViewHeight;
    }

    public void setXLabelViewHeight(float xLabelViewHeight) {
        this.xLabelViewHeight = xLabelViewHeight;
    }

    public float getYLabelSize() {
        return yLabelSize;
    }

    public void setYLabelSize(float yLabelSize) {
        this.yLabelSize = yLabelSize;
    }

    public int getYLabelColor() {
        return yLabelColor;
    }

    public void setYLabelColor(int yLabelColor) {
        this.yLabelColor = yLabelColor;
    }

    public YLabelAlign getYLabelAlign() {
        return yLabelAlign;
    }

    public void setYLabelAlign(YLabelAlign yLabelAlign) {
        this.yLabelAlign = yLabelAlign;
    }

    public float getAxisSize() {
        return axisSize;
    }

    public void setAxisSize(float axisSize) {
        this.axisSize = axisSize;
    }

    public int getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(int axisColor) {
        this.axisColor = axisColor;
    }

    public float getGridSize() {
        return gridSize;
    }

    public void setGridSize(float gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridColor() {
        return gridColor;
    }

    public void setGridColor(int gridColor) {
        this.gridColor = gridColor;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 与高亮、MarkerView 有关的属性和方法
    ///////////////////////////////////////////////////////////////////////////

    private float highlightSize = 2f; // 高亮线条大小
    private int highlightColor = 0xff282b34; // 高亮线条颜色 0xff1c232e

    private float markerBorderSize = 2f; // MarkerView 边框大小
    private int markerBorderColor = 0xff282b34; // MarkerView 边框颜色
    private float markerTextSize = 21; // MarkerView 字符大小
    private int markerTextColor = 0xff282b34; // MarkerView 字符颜色
    private XMarkerAlign xMarkerAlign = XMarkerAlign.AUTO; // X 轴 MarkerView 对齐方向
    private YMarkerAlign yMarkerAlign = YMarkerAlign.AUTO; // Y 轴 MarkerView 对齐方向

    public float getHighlightSize() {
        return highlightSize;
    }

    public void setHighlightSize(float highlightSize) {
        this.highlightSize = highlightSize;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public float getMarkerBorderSize() {
        return markerBorderSize;
    }

    public void setMarkerBorderSize(float markerBorderSize) {
        this.markerBorderSize = markerBorderSize;
    }

    public int getMarkerBorderColor() {
        return markerBorderColor;
    }

    public void setMarkerBorderColor(int markerBorderColor) {
        this.markerBorderColor = markerBorderColor;
    }

    public float getMarkerTextSize() {
        return markerTextSize;
    }

    public void setMarkerTextSize(float markerTextSize) {
        this.markerTextSize = markerTextSize;
    }

    public int getMarkerTextColor() {
        return markerTextColor;
    }

    public void setMarkerTextColor(int markerTextColor) {
        this.markerTextColor = markerTextColor;
    }

    public XMarkerAlign getXMarkerAlign() {
        return xMarkerAlign;
    }

    public void setXMarkerAlign(XMarkerAlign xMarkerAlign) {
        this.xMarkerAlign = xMarkerAlign;
    }

    public YMarkerAlign getYMarkerAlign() {
        return yMarkerAlign;
    }

    public void setYMarkerAlign(YMarkerAlign yMarkerAlign) {
        this.yMarkerAlign = yMarkerAlign;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 与分时图有关的属性和方法
    ///////////////////////////////////////////////////////////////////////////

    private float timeLineSize = 2f; // 分时线大小
    private int timeLineColor = 0xff82b1ff; // 分时线颜色
    private int timeLineMaxCount = 240; // 分时图 entry 最多个数。注：此值与 entrySet 里的 entries.size() 意义不同，这里指 X 轴上最多能容纳多少个 entry

    public float getTimeLineSize() {
        return timeLineSize;
    }

    public void setTimeLineSize(float timeLineSize) {
        this.timeLineSize = timeLineSize;
    }

    public int getTimeLineColor() {
        return timeLineColor;
    }

    public void setTimeLineColor(int timeLineColor) {
        this.timeLineColor = timeLineColor;
    }

    public int getTimeLineMaxCount() {
        return timeLineMaxCount;
    }

    public void setTimeLineMaxCount(int timeLineMaxCount) {
        this.timeLineMaxCount = timeLineMaxCount;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 与蜡烛图有关的属性和方法
    ///////////////////////////////////////////////////////////////////////////

    private float candleBorderSize = 3f; // 蜡烛图矩形边框大小
    private float candleExtremumLabelSize = 21; // 蜡烛图极值字符大小
    private int candleExtremumLableColor = 0xff282b34; // 蜡烛图极值字符颜色

    private float shadowSize = 4f; // 影线大小
    private int increasingColor = 0xffe05959; // 上涨颜色
    private int decreasingColor = 0xff4fb86a; // 下跌颜色
    private int neutralColor = 0xff282b34; // 不涨不跌颜色

    private int portraitDefaultVisibleCount = 50; // 竖屏状态下的默认缩放倍数下显示多少个蜡烛图。注：横屏时会自动根据视图宽高变化比例计算，不需要手工设置
    private int zoomInTimes = 3; // 最多放大次数
    private int zoomOutTimes = 3; // 最多缩小次数

    private Paint.Style increasingStyle = Paint.Style.FILL; // 上涨蜡烛图填充样式。默认实心
    private Paint.Style decreasingStyle = Paint.Style.STROKE; // 下跌蜡烛图填充样式，默认实心

    public float getCandleBorderSize() {
        return candleBorderSize;
    }

    public void setCandleBorderSize(float candleBorderSize) {
        this.candleBorderSize = candleBorderSize;
    }

    public float getCandleExtremumLabelSize() {
        return candleExtremumLabelSize;
    }

    public void setCandleExtremumLabelSize(float candleExtremumLabelSize) {
        this.candleExtremumLabelSize = candleExtremumLabelSize;
    }

    public int getCandleExtremumLableColor() {
        return candleExtremumLableColor;
    }

    public void setCandleExtremumLableColor(int candleExtremumLableColor) {
        this.candleExtremumLableColor = candleExtremumLableColor;
    }

    public float getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(float shadowSize) {
        this.shadowSize = shadowSize;
    }

    public int getIncreasingColor() {
        return increasingColor;
    }

    public void setIncreasingColor(int increasingColor) {
        this.increasingColor = increasingColor;
    }

    public int getDecreasingColor() {
        return decreasingColor;
    }

    public void setDecreasingColor(int decreasingColor) {
        this.decreasingColor = decreasingColor;
    }

    public int getNeutralColor() {
        return neutralColor;
    }

    public void setNeutralColor(int neutralColor) {
        this.neutralColor = neutralColor;
    }

    public int getPortraitDefaultVisibleCount() {
        return portraitDefaultVisibleCount;
    }

    public void setPortraitDefaultVisibleCount(int portraitDefaultVisibleCount) {
        this.portraitDefaultVisibleCount = portraitDefaultVisibleCount;
    }

    public int getZoomInTimes() {
        return zoomInTimes;
    }

    public void setZoomInTimes(int zoomInTimes) {
        this.zoomInTimes = zoomInTimes;
    }

    public int getZoomOutTimes() {
        return zoomOutTimes;
    }

    public void setZoomOutTimes(int zoomOutTimes) {
        this.zoomOutTimes = zoomOutTimes;
    }

    public Paint.Style getIncreasingStyle() {
        return increasingStyle;
    }

    public void setIncreasingStyle(Paint.Style increasingStyle) {
        this.increasingStyle = increasingStyle;
    }

    public Paint.Style getDecreasingStyle() {
        return decreasingStyle;
    }

    public void setDecreasingStyle(Paint.Style decreasingStyle) {
        this.decreasingStyle = decreasingStyle;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 与股票指标有关的属性和方法
    ///////////////////////////////////////////////////////////////////////////

    private float maLineSize = 2f; // MA 平均线大小
    private int ma5Color = 0xff82b1ff; // MA5 平均线颜色
    private int ma10Color = 0xffffab40; // MA10 平均线颜色
    private int ma20Color = 0xfff06292; // MA20 平均线颜色

    private float bollLineSize = 2f; // BOLL 线条大小
    private int bollMidLineColor = 0xff82b1ff; // BOLL MID 线条颜色
    private int bollUpperLineColor = 0xffffab40; // BOLL UPPER 线条颜色
    private int bollLowerLineColor = 0xfff06292; // BOLL LOWER 线条颜色

    private float kdjLineSize = 2f; // KDJ 线条大小
    private int kdjKLineColor = 0xff82b1ff; // KDJ K 线条颜色
    private int kdjDLineColor = 0xffffab40; // KDJ D 线条颜色
    private int kdjJLineColor = 0xfff06292; // KDJ J 线条颜色

    private float macdLineSize = 2f; // MACD 两条线大小
    private int macdHighlightTextColor = 0xfff06292; // 高亮的 MACD 字符颜色，此值与 macdTextColor 不同
    private int deaLineColor = 0xff82b1ff; // DEA 线条颜色
    private int diffLineColor = 0xffffab40; // DIFF 线条颜色

    private float rsiLineSize = 2f; // RSI 线条大小
    private int rsi1LineColor = 0xff82b1ff; // RSI 第一条线颜色
    private int rsi2LineColor = 0xffffab40; // RSI 第二条线颜色
    private int rsi3LineColor = 0xfff06292; // RSI 第三条线颜色

    private float maTextSize = 21; // MA 字符大小
    private int maTextColor = 0xff282b34; // MA 字符颜色

    private float bollTextSize = 21; // BOLL 字符大小
    private int bollTextColor = 0xff282b34; // BOLL 字符颜色

    private float kdjTextSize = 21; // KDJ 字符大小
    private int kdjTextColor = 0xff282b34; // KDJ 字符颜色

    private float macdTextSize = 21; // MACD 字符大小
    private int macdTextColor = 0xff282b34; // MACD 字符颜色

    private float rsiTextSize = 21; // RSI 字符大小
    private int rsiTextColor = 0xff282b34; // RSI 字符颜色

    public float getMaLineSize() {
        return maLineSize;
    }

    public void setMaLineSize(float maLineSize) {
        this.maLineSize = maLineSize;
    }

    public int getMa5Color() {
        return ma5Color;
    }

    public void setMa5Color(int ma5Color) {
        this.ma5Color = ma5Color;
    }

    public int getMa10Color() {
        return ma10Color;
    }

    public void setMa10Color(int ma10Color) {
        this.ma10Color = ma10Color;
    }

    public int getMa20Color() {
        return ma20Color;
    }

    public void setMa20Color(int ma20Color) {
        this.ma20Color = ma20Color;
    }

    public float getBollLineSize() {
        return bollLineSize;
    }

    public void setBollLineSize(float bollLineSize) {
        this.bollLineSize = bollLineSize;
    }

    public int getBollMidLineColor() {
        return bollMidLineColor;
    }

    public void setBollMidLineColor(int bollMidLineColor) {
        this.bollMidLineColor = bollMidLineColor;
    }

    public int getBollUpperLineColor() {
        return bollUpperLineColor;
    }

    public void setBollUpperLineColor(int bollUpperLineColor) {
        this.bollUpperLineColor = bollUpperLineColor;
    }

    public int getBollLowerLineColor() {
        return bollLowerLineColor;
    }

    public void setBollLowerLineColor(int bollLowerLineColor) {
        this.bollLowerLineColor = bollLowerLineColor;
    }

    public float getKdjLineSize() {
        return kdjLineSize;
    }

    public void setKdjLineSize(float kdjLineSize) {
        this.kdjLineSize = kdjLineSize;
    }

    public int getKdjKLineColor() {
        return kdjKLineColor;
    }

    public void setKdjKLineColor(int kdjKLineColor) {
        this.kdjKLineColor = kdjKLineColor;
    }

    public int getKdjDLineColor() {
        return kdjDLineColor;
    }

    public void setKdjDLineColor(int kdjDLineColor) {
        this.kdjDLineColor = kdjDLineColor;
    }

    public int getKdjJLineColor() {
        return kdjJLineColor;
    }

    public void setKdjJLineColor(int kdjJLineColor) {
        this.kdjJLineColor = kdjJLineColor;
    }

    public float getMacdLineSize() {
        return macdLineSize;
    }

    public void setMacdLineSize(float macdLineSize) {
        this.macdLineSize = macdLineSize;
    }

    public int getMacdHighlightTextColor() {
        return macdHighlightTextColor;
    }

    public void setMacdHighlightTextColor(int macdHighlightTextColor) {
        this.macdHighlightTextColor = macdHighlightTextColor;
    }

    public int getDeaLineColor() {
        return deaLineColor;
    }

    public void setDeaLineColor(int deaLineColor) {
        this.deaLineColor = deaLineColor;
    }

    public int getDiffLineColor() {
        return diffLineColor;
    }

    public void setDiffLineColor(int diffLineColor) {
        this.diffLineColor = diffLineColor;
    }

    public float getRsiLineSize() {
        return rsiLineSize;
    }

    public void setRsiLineSize(float rsiLineSize) {
        this.rsiLineSize = rsiLineSize;
    }

    public int getRsi1LineColor() {
        return rsi1LineColor;
    }

    public void setRsi1LineColor(int rsi1LineColor) {
        this.rsi1LineColor = rsi1LineColor;
    }

    public int getRsi2LineColor() {
        return rsi2LineColor;
    }

    public void setRsi2LineColor(int rsi2LineColor) {
        this.rsi2LineColor = rsi2LineColor;
    }

    public int getRsi3LineColor() {
        return rsi3LineColor;
    }

    public void setRsi3LineColor(int rsi3LineColor) {
        this.rsi3LineColor = rsi3LineColor;
    }

    public float getMaTextSize() {
        return maTextSize;
    }

    public void setMaTextSize(float maTextSize) {
        this.maTextSize = maTextSize;
    }

    public int getMaTextColor() {
        return maTextColor;
    }

    public void setMaTextColor(int maTextColor) {
        this.maTextColor = maTextColor;
    }

    public float getBollTextSize() {
        return bollTextSize;
    }

    public void setBollTextSize(float bollTextSize) {
        this.bollTextSize = bollTextSize;
    }

    public int getBollTextColor() {
        return bollTextColor;
    }

    public void setBollTextColor(int bollTextColor) {
        this.bollTextColor = bollTextColor;
    }

    public float getKdjTextSize() {
        return kdjTextSize;
    }

    public void setKdjTextSize(float kdjTextSize) {
        this.kdjTextSize = kdjTextSize;
    }

    public int getKdjTextColor() {
        return kdjTextColor;
    }

    public void setKdjTextColor(int kdjTextColor) {
        this.kdjTextColor = kdjTextColor;
    }

    public float getMacdTextSize() {
        return macdTextSize;
    }

    public void setMacdTextSize(float macdTextSize) {
        this.macdTextSize = macdTextSize;
    }

    public int getMacdTextColor() {
        return macdTextColor;
    }

    public void setMacdTextColor(int macdTextColor) {
        this.macdTextColor = macdTextColor;
    }

    public float getRsiTextSize() {
        return rsiTextSize;
    }

    public void setRsiTextSize(float rsiTextSize) {
        this.rsiTextSize = rsiTextSize;
    }

    public int getRsiTextColor() {
        return rsiTextColor;
    }

    public void setRsiTextColor(int rsiTextColor) {
        this.rsiTextColor = rsiTextColor;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 其它
    ///////////////////////////////////////////////////////////////////////////

    private float loadingTextSize = 21;
    private int loadingTextColor = 0xff282b34;
    private String loadingText = "Loading...";

    private float errorTextSize = 21;
    private int errorTextColor = 0xff282b34;
    private String errorText = "Empty";

    public float getLoadingTextSize() {
        return loadingTextSize;
    }

    public void setLoadingTextSize(float loadingTextSize) {
        this.loadingTextSize = loadingTextSize;
    }

    public int getLoadingTextColor() {
        return loadingTextColor;
    }

    public void setLoadingTextColor(int loadingTextColor) {
        this.loadingTextColor = loadingTextColor;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public float getErrorTextSize() {
        return errorTextSize;
    }

    public void setErrorTextSize(float errorTextSize) {
        this.errorTextSize = errorTextSize;
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public void setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
