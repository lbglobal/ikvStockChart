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

package com.wordplat.ikvstockchart;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wordplat.ikvstockchart.compat.ViewUtils;
import com.wordplat.ikvstockchart.drawing.BOLLDrawing;
import com.wordplat.ikvstockchart.drawing.HighlightDrawing;
import com.wordplat.ikvstockchart.drawing.KDJDrawing;
import com.wordplat.ikvstockchart.drawing.KLineVolumeDrawing;
import com.wordplat.ikvstockchart.drawing.KLineVolumeHighlightDrawing;
import com.wordplat.ikvstockchart.drawing.MACDDrawing;
import com.wordplat.ikvstockchart.drawing.RSIDrawing;
import com.wordplat.ikvstockchart.drawing.StockIndexYLabelDrawing;
import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.StockBOLLIndex;
import com.wordplat.ikvstockchart.entry.StockKDJIndex;
import com.wordplat.ikvstockchart.entry.StockKLineVolumeIndex;
import com.wordplat.ikvstockchart.entry.StockMACDIndex;
import com.wordplat.ikvstockchart.entry.StockRSIIndex;
import com.wordplat.ikvstockchart.marker.XAxisTextMarkerView;
import com.wordplat.ikvstockchart.marker.YAxisTextMarkerView;
import com.wordplat.ikvstockchart.render.KLineRender;

/**
 * <p>InteractiveKLineLayout</p>
 * <p>Date: 2017/3/22</p>
 *
 * @deprecated 这是一个含有股票技术指标的K线图示例，建议不要使用这个类用于真实项目中，此示例对如何编写自己的自定义布局提供参考。
 * @author afon
 */

@Deprecated
public class InteractiveKLineLayout extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "InteractiveKLineLayout";

    private Context context;

    private InteractiveKLineView kLineView;
    private KLineHandler kLineHandler;
    private KLineRender kLineRender;

    private StockMACDIndex macdIndex;
    private StockRSIIndex rsiIndex;
    private StockKDJIndex kdjIndex;
    private StockBOLLIndex bollIndex;

    private int stockMarkerViewHeight;
    private int stockIndexViewHeight;
    private int stockIndexTabHeight;
    private RectF currentRect;

    private RadioGroup But_Group;
    private RadioButton MACD_But;
    private RadioButton RSI_But;
    private RadioButton KDJ_But;
    private RadioButton BOLL_But;

    public InteractiveKLineLayout(Context context) {
        this(context, null);
    }

    public InteractiveKLineLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InteractiveKLineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        stockMarkerViewHeight = context.getResources().getDimensionPixelOffset(R.dimen.stock_marker_view_height);
        stockIndexViewHeight = context.getResources().getDimensionPixelOffset(R.dimen.stock_index_view_height);
        stockIndexTabHeight = context.getResources().getDimensionPixelOffset(R.dimen.stock_index_tab_height);

        initUI(context, attrs, defStyleAttr);
    }

    private void initUI(Context context, AttributeSet attrs, int defStyleAttr) {
        kLineView = new InteractiveKLineView(context);
        kLineRender = (KLineRender) kLineView.getRender();

        kLineRender.setSizeColor(ViewUtils.getSizeColor(context, attrs, defStyleAttr));

        kLineView.setKLineHandler(new KLineHandler() {
            @Override
            public void onLeftRefresh() {
                if (kLineHandler != null) {
                    kLineHandler.onLeftRefresh();
                }
            }

            @Override
            public void onRightRefresh() {
                if (kLineHandler != null) {
                    kLineHandler.onRightRefresh();
                }
            }

            @Override
            public void onSingleTap(MotionEvent e, float x, float y) {
                if (kLineHandler != null) {
                    kLineHandler.onSingleTap(e, x, y);
                }

                onTabClick(x, y);
            }

            @Override
            public void onDoubleTap(MotionEvent e, float x, float y) {
                if (kLineHandler != null) {
                    kLineHandler.onDoubleTap(e, x, y);
                }
            }

            @Override
            public void onHighlight(Entry entry, int entryIndex, float x, float y) {
                if (kLineHandler != null) {
                    kLineHandler.onHighlight(entry, entryIndex, x, y);
                }
            }

            @Override
            public void onCancelHighlight() {
                if (kLineHandler != null) {
                    kLineHandler.onCancelHighlight();
                }
            }
        });

        // 成交量
        StockKLineVolumeIndex kLineVolumeIndex = new StockKLineVolumeIndex(stockIndexViewHeight);
        kLineVolumeIndex.addDrawing(new KLineVolumeDrawing());
        kLineVolumeIndex.addDrawing(new KLineVolumeHighlightDrawing());
        kLineRender.addStockIndex(kLineVolumeIndex);

        // MACD
        HighlightDrawing macdHighlightDrawing = new HighlightDrawing();
        macdHighlightDrawing.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));

        macdIndex = new StockMACDIndex(stockIndexViewHeight);
        macdIndex.addDrawing(new MACDDrawing());
        macdIndex.addDrawing(new StockIndexYLabelDrawing());
        macdIndex.addDrawing(macdHighlightDrawing);
        macdIndex.setPaddingTop(stockIndexTabHeight);
        kLineRender.addStockIndex(macdIndex);

        // RSI
        HighlightDrawing rsiHighlightDrawing = new HighlightDrawing();
        rsiHighlightDrawing.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));

        rsiIndex = new StockRSIIndex(stockIndexViewHeight);
        rsiIndex.addDrawing(new RSIDrawing());
        rsiIndex.addDrawing(new StockIndexYLabelDrawing());
        rsiIndex.addDrawing(rsiHighlightDrawing);
        rsiIndex.setPaddingTop(stockIndexTabHeight);
        kLineRender.addStockIndex(rsiIndex);

        // KDJ
        HighlightDrawing kdjHighlightDrawing = new HighlightDrawing();
        kdjHighlightDrawing.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));

        kdjIndex = new StockKDJIndex(stockIndexViewHeight);
        kdjIndex.addDrawing(new KDJDrawing());
        kdjIndex.addDrawing(new StockIndexYLabelDrawing());
        kdjIndex.addDrawing(kdjHighlightDrawing);
        kdjIndex.setPaddingTop(stockIndexTabHeight);
        kLineRender.addStockIndex(kdjIndex);

        // BOLL
        HighlightDrawing bollHighlightDrawing = new HighlightDrawing();
        bollHighlightDrawing.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));

        bollIndex = new StockBOLLIndex(stockIndexViewHeight);
        bollIndex.addDrawing(new BOLLDrawing());
        bollIndex.addDrawing(new StockIndexYLabelDrawing());
        bollIndex.addDrawing(bollHighlightDrawing);
        bollIndex.setPaddingTop(stockIndexTabHeight);
        kLineRender.addStockIndex(bollIndex);

        kLineRender.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));
        kLineRender.addMarkerView(new XAxisTextMarkerView(stockMarkerViewHeight));

        addView(kLineView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        But_Group = (RadioGroup) findViewById(R.id.But_Group);
        MACD_But = (RadioButton) findViewById(R.id.MACD_But);
        RSI_But = (RadioButton) findViewById(R.id.RSI_But);
        KDJ_But = (RadioButton) findViewById(R.id.KDJ_But);
        BOLL_But = (RadioButton) findViewById(R.id.BOLL_But);

        MACD_But.setOnClickListener(this);
        RSI_But.setOnClickListener(this);
        KDJ_But.setOnClickListener(this);
        BOLL_But.setOnClickListener(this);

        showMACD();
    }

    public InteractiveKLineView getKLineView() {
        return kLineView;
    }

    public void setKLineHandler(KLineHandler kLineHandler) {
        this.kLineHandler = kLineHandler;
    }

    private void onTabClick(float x, float y) {
        if (currentRect.contains(x, y)) {
            if (macdIndex.isEnable()) {
                showRSI();
            } else if (rsiIndex.isEnable()) {
                showKDJ();
            } else if (kdjIndex.isEnable()) {
                showBOLL();
            } else {
                showMACD();
            }

            if (kLineHandler != null) {
                kLineHandler.onCancelHighlight();
            }

            kLineView.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.MACD_But) {
            showMACD();

        } else if (id == R.id.RSI_But) {
            showRSI();

        } else if (id == R.id.KDJ_But) {
            showKDJ();

        } else if (id == R.id.BOLL_But) {
            showBOLL();

        }

        if (kLineHandler != null) {
            kLineHandler.onCancelHighlight();
        }

        kLineView.notifyDataSetChanged();
    }

    public void showMACD() {
        macdIndex.setEnable(true);
        rsiIndex.setEnable(false);
        kdjIndex.setEnable(false);
        bollIndex.setEnable(false);

        But_Group.clearCheck();
        MACD_But.setChecked(true);

        currentRect = macdIndex.getRect();
    }

    public void showRSI() {
        macdIndex.setEnable(false);
        rsiIndex.setEnable(true);
        kdjIndex.setEnable(false);
        bollIndex.setEnable(false);

        But_Group.clearCheck();
        RSI_But.setChecked(true);

        currentRect = rsiIndex.getRect();
    }

    public void showKDJ() {
        macdIndex.setEnable(false);
        rsiIndex.setEnable(false);
        kdjIndex.setEnable(true);
        bollIndex.setEnable(false);

        But_Group.clearCheck();
        KDJ_But.setChecked(true);

        currentRect = kdjIndex.getRect();
    }

    public void showBOLL() {
        macdIndex.setEnable(false);
        rsiIndex.setEnable(false);
        kdjIndex.setEnable(false);
        bollIndex.setEnable(true);

        But_Group.clearCheck();
        BOLL_But.setChecked(true);

        currentRect = bollIndex.getRect();
    }

    public boolean isShownMACD() {
        return macdIndex.isEnable();
    }

    public boolean isShownRSI() {
        return rsiIndex.isEnable();
    }

    public boolean isShownKDJ() {
        return kdjIndex.isEnable();
    }

    public boolean isShownBOLL() {
        return bollIndex.isEnable();
    }
}
