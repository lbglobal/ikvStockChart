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

package com.wordplat.ikvstockchart.compat;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.wordplat.ikvstockchart.R;
import com.wordplat.ikvstockchart.entry.SizeColor;

/**
 * <p>ViewUtils</p>
 * <p>Date: 2017/3/29</p>
 *
 * @author afon
 */

public class ViewUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dpTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 初始化 SizeColor
     */
    public static SizeColor getSizeColor(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.InteractiveKLineView, defStyleAttr, defStyleAttr);

        final SizeColor sizeColor = new SizeColor();

        try {
            // 与轴、网格有关的属性
            sizeColor.setXLabelSize(a.getDimension(R.styleable.InteractiveKLineView_xLabelSize,
                    sizeColor.getXLabelSize()));

            sizeColor.setXLabelColor(a.getColor(R.styleable.InteractiveKLineView_xLabelColor,
                    sizeColor.getXLabelColor()));

            sizeColor.setXLabelViewHeight(a.getDimension(R.styleable.InteractiveKLineView_xLabelViewHeight,
                    sizeColor.getXLabelViewHeight()));

            sizeColor.setYLabelSize(a.getDimension(R.styleable.InteractiveKLineView_yLabelSize,
                    sizeColor.getYLabelSize()));

            sizeColor.setYLabelColor(a.getColor(R.styleable.InteractiveKLineView_yLabelColor,
                    sizeColor.getYLabelColor()));

            sizeColor.setYLabelAlign(a.getInteger(R.styleable.InteractiveKLineView_yLabelAlign, 1));

            sizeColor.setAxisSize(a.getDimension(R.styleable.InteractiveKLineView_axisSize,
                    sizeColor.getAxisSize()));

            sizeColor.setAxisColor(a.getColor(R.styleable.InteractiveKLineView_axisColor,
                    sizeColor.getAxisColor()));

            sizeColor.setGridSize(a.getDimension(R.styleable.InteractiveKLineView_gridSize,
                    sizeColor.getGridSize()));

            sizeColor.setGridColor(a.getColor(R.styleable.InteractiveKLineView_gridColor,
                    sizeColor.getGridColor()));

            // 与高亮、MarkerView 有关的属性
            sizeColor.setHighlightSize(a.getDimension(R.styleable.InteractiveKLineView_highlightSize,
                    sizeColor.getHighlightSize()));

            sizeColor.setHighlightColor(a.getColor(R.styleable.InteractiveKLineView_highlightColor,
                    sizeColor.getHighlightColor()));

            sizeColor.setMarkerBorderSize(a.getDimension(R.styleable.InteractiveKLineView_markerBorderSize,
                    sizeColor.getMarkerBorderSize()));

            sizeColor.setMarkerBorderColor(a.getColor(R.styleable.InteractiveKLineView_markerBorderColor,
                    sizeColor.getMarkerBorderColor()));

            sizeColor.setMarkerTextSize(a.getDimension(R.styleable.InteractiveKLineView_markerTextSize,
                    sizeColor.getMarkerTextSize()));

            sizeColor.setMarkerTextColor(a.getColor(R.styleable.InteractiveKLineView_markerTextColor,
                    sizeColor.getMarkerTextColor()));

            // 与分时图有关的属性
            sizeColor.setTimeLineSize(a.getDimension(R.styleable.InteractiveKLineView_timeLineSize,
                    sizeColor.getTimeLineSize()));

            sizeColor.setTimeLineColor(a.getColor(R.styleable.InteractiveKLineView_timeLineColor,
                    sizeColor.getTimeLineColor()));

            // 与蜡烛图有关的属性
            sizeColor.setCandleBorderSize(a.getDimension(R.styleable.InteractiveKLineView_candleBorderSize,
                    sizeColor.getCandleBorderSize()));

            sizeColor.setCandleExtremumLabelSize(a.getDimension(R.styleable.InteractiveKLineView_candleExtremumLabelSize,
                    sizeColor.getCandleExtremumLabelSize()));

            sizeColor.setCandleExtremumLableColor(a.getColor(R.styleable.InteractiveKLineView_candleExtremumLableColor,
                    sizeColor.getCandleExtremumLableColor()));

            sizeColor.setShadowSize(a.getDimension(R.styleable.InteractiveKLineView_shadowSize,
                    sizeColor.getShadowSize()));

            sizeColor.setIncreasingColor(a.getColor(R.styleable.InteractiveKLineView_increasingColor,
                    sizeColor.getIncreasingColor()));

            sizeColor.setDecreasingColor(a.getColor(R.styleable.InteractiveKLineView_decreasingColor,
                    sizeColor.getDecreasingColor()));

            sizeColor.setNeutralColor(a.getColor(R.styleable.InteractiveKLineView_neutralColor,
                    sizeColor.getNeutralColor()));

            sizeColor.setPortraitDefaultVisibleCount(a.getInteger(R.styleable.InteractiveKLineView_portraitDefaultVisibleCount,
                    sizeColor.getPortraitDefaultVisibleCount()));

            sizeColor.setZoomInTimes(a.getInteger(R.styleable.InteractiveKLineView_zoomInTimes,
                    sizeColor.getZoomInTimes()));

            sizeColor.setZoomOutTimes(a.getInteger(R.styleable.InteractiveKLineView_zoomOutTimes,
                    sizeColor.getZoomOutTimes()));

            // 与股票指标有关的属性
            sizeColor.setMaLineSize(a.getDimension(R.styleable.InteractiveKLineView_maLineSize,
                    sizeColor.getMaLineSize()));

            sizeColor.setMa5Color(a.getColor(R.styleable.InteractiveKLineView_ma5Color,
                    sizeColor.getMa5Color()));

            sizeColor.setMa10Color(a.getColor(R.styleable.InteractiveKLineView_ma10Color,
                    sizeColor.getMa10Color()));

            sizeColor.setMa20Color(a.getColor(R.styleable.InteractiveKLineView_ma20Color,
                    sizeColor.getMa20Color()));

            sizeColor.setBollLineSize(a.getDimension(R.styleable.InteractiveKLineView_bollLineSize,
                    sizeColor.getBollLineSize()));

            sizeColor.setBollMidLineColor(a.getColor(R.styleable.InteractiveKLineView_bollMidLineColor,
                    sizeColor.getBollMidLineColor()));

            sizeColor.setBollUpperLineColor(a.getColor(R.styleable.InteractiveKLineView_bollUpperLineColor,
                    sizeColor.getBollUpperLineColor()));

            sizeColor.setBollLowerLineColor(a.getColor(R.styleable.InteractiveKLineView_bollLowerLineColor,
                    sizeColor.getBollLowerLineColor()));

            sizeColor.setKdjLineSize(a.getDimension(R.styleable.InteractiveKLineView_kdjLineSize,
                    sizeColor.getKdjLineSize()));

            sizeColor.setKdjKLineColor(a.getColor(R.styleable.InteractiveKLineView_kdjKLineColor,
                    sizeColor.getKdjKLineColor()));

            sizeColor.setKdjDLineColor(a.getColor(R.styleable.InteractiveKLineView_kdjDLineColor,
                    sizeColor.getKdjDLineColor()));

            sizeColor.setKdjJLineColor(a.getColor(R.styleable.InteractiveKLineView_kdjJLineColor,
                    sizeColor.getKdjJLineColor()));

            sizeColor.setMacdLineSize(a.getDimension(R.styleable.InteractiveKLineView_macdLineSize,
                    sizeColor.getMacdLineSize()));

            sizeColor.setMacdHighlightTextColor(a.getColor(R.styleable.InteractiveKLineView_macdHighlightTextColor,
                    sizeColor.getMacdHighlightTextColor()));

            sizeColor.setDeaLineColor(a.getColor(R.styleable.InteractiveKLineView_deaLineColor,
                    sizeColor.getDeaLineColor()));

            sizeColor.setDiffLineColor(a.getColor(R.styleable.InteractiveKLineView_diffLineColor,
                    sizeColor.getDiffLineColor()));

            sizeColor.setRsiLineSize(a.getDimension(R.styleable.InteractiveKLineView_rsiLineSize,
                    sizeColor.getRsiLineSize()));

            sizeColor.setRsi1LineColor(a.getColor(R.styleable.InteractiveKLineView_rsi1LineColor,
                    sizeColor.getRsi1LineColor()));

            sizeColor.setRsi2LineColor(a.getColor(R.styleable.InteractiveKLineView_rsi2LineColor,
                    sizeColor.getRsi2LineColor()));

            sizeColor.setRsi3LineColor(a.getColor(R.styleable.InteractiveKLineView_rsi3LineColor,
                    sizeColor.getRsi3LineColor()));

            sizeColor.setMaTextSize(a.getDimension(R.styleable.InteractiveKLineView_maTextSize,
                    sizeColor.getMaTextSize()));

            sizeColor.setMaTextColor(a.getColor(R.styleable.InteractiveKLineView_maTextColor,
                    sizeColor.getMaTextColor()));

            sizeColor.setBollTextSize(a.getDimension(R.styleable.InteractiveKLineView_bollTextSize,
                    sizeColor.getBollTextSize()));

            sizeColor.setBollTextColor(a.getColor(R.styleable.InteractiveKLineView_bollTextColor,
                    sizeColor.getBollTextColor()));

            sizeColor.setKdjTextSize(a.getDimension(R.styleable.InteractiveKLineView_kdjTextSize,
                    sizeColor.getKdjTextSize()));

            sizeColor.setKdjTextColor(a.getColor(R.styleable.InteractiveKLineView_kdjTextColor,
                    sizeColor.getKdjTextColor()));

            sizeColor.setMacdTextSize(a.getDimension(R.styleable.InteractiveKLineView_macdTextSize,
                    sizeColor.getMacdTextSize()));

            sizeColor.setMacdTextColor(a.getColor(R.styleable.InteractiveKLineView_macdTextColor,
                    sizeColor.getMacdTextColor()));

            sizeColor.setRsiTextSize(a.getDimension(R.styleable.InteractiveKLineView_rsiTextSize,
                    sizeColor.getRsiTextSize()));

            sizeColor.setRsiTextColor(a.getColor(R.styleable.InteractiveKLineView_rsiTextColor,
                    sizeColor.getRsiTextColor()));

            // 其它
            sizeColor.setLoadingTextSize(a.getDimension(R.styleable.InteractiveKLineView_loadingTextSize,
                    sizeColor.getLoadingTextSize()));

            sizeColor.setLoadingTextColor(a.getColor(R.styleable.InteractiveKLineView_loadingTextColor,
                    sizeColor.getLoadingTextColor()));

            String loadingText = a.getString(R.styleable.InteractiveKLineView_loadingText);
            if (!TextUtils.isEmpty(loadingText)) {
                sizeColor.setLoadingText(loadingText);
            }

            sizeColor.setErrorTextSize(a.getDimension(R.styleable.InteractiveKLineView_errorTextSize,
                    sizeColor.getErrorTextSize()));

            sizeColor.setErrorTextColor(a.getColor(R.styleable.InteractiveKLineView_errorTextColor,
                    sizeColor.getErrorTextColor()));

            String errorText = a.getString(R.styleable.InteractiveKLineView_errorText);
            if (!TextUtils.isEmpty(errorText)) {
                sizeColor.setErrorText(errorText);
            }

        } finally {
            a.recycle();
        }

        return sizeColor;
    }
}
