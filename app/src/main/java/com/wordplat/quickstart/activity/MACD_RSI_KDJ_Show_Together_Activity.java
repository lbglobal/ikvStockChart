package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.wordplat.quickstart.R;
import com.wordplat.quickstart.utils.AppUtils;
import com.wordplat.ikvstockchart.InteractiveKLineView;
import com.wordplat.ikvstockchart.compat.PerformenceAnalyser;
import com.wordplat.ikvstockchart.drawing.HighlightDrawing;
import com.wordplat.ikvstockchart.drawing.KDJDrawing;
import com.wordplat.ikvstockchart.drawing.MACDDrawing;
import com.wordplat.ikvstockchart.drawing.RSIDrawing;
import com.wordplat.ikvstockchart.drawing.StockIndexYLabelDrawing;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.StockDataTest;
import com.wordplat.ikvstockchart.entry.StockKDJIndex;
import com.wordplat.ikvstockchart.entry.StockMACDIndex;
import com.wordplat.ikvstockchart.entry.StockRSIIndex;
import com.wordplat.ikvstockchart.marker.XAxisTextMarkerView;
import com.wordplat.ikvstockchart.marker.YAxisTextMarkerView;
import com.wordplat.ikvstockchart.render.KLineRender;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.InputStream;

/**
 * <p>MACD_RSI_KDJ_Show_Together_Activity</p>
 * <p>Date: 2017/3/31</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_macd_rsi_kdj_show_together)
public class MACD_RSI_KDJ_Show_Together_Activity extends BaseActivity {

    @ViewInject(R.id.kLineView) private InteractiveKLineView kLineView = null;

    private KLineRender kLineRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        loadKLineData();
    }

    private void initUI() {
        kLineView.setEnableLeftRefresh(false);
        kLineView.setEnableLeftRefresh(false);
        kLineRender = (KLineRender) kLineView.getRender();

        final int paddingTop = AppUtils.dpTopx(mActivity, 10);
        final int stockMarkerViewHeight = AppUtils.dpTopx(mActivity, 15);

        // MACD
        HighlightDrawing macdHighlightDrawing = new HighlightDrawing();
        macdHighlightDrawing.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));

        StockMACDIndex macdIndex = new StockMACDIndex();
        macdIndex.addDrawing(new MACDDrawing());
        macdIndex.addDrawing(new StockIndexYLabelDrawing());
        macdIndex.addDrawing(macdHighlightDrawing);
        macdIndex.setPaddingTop(paddingTop);
        kLineRender.addStockIndex(macdIndex);

        // RSI
        HighlightDrawing rsiHighlightDrawing = new HighlightDrawing();
        rsiHighlightDrawing.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));

        StockRSIIndex rsiIndex = new StockRSIIndex();
        rsiIndex.addDrawing(new RSIDrawing());
        rsiIndex.addDrawing(new StockIndexYLabelDrawing());
        rsiIndex.addDrawing(rsiHighlightDrawing);
        rsiIndex.setPaddingTop(paddingTop);
        kLineRender.addStockIndex(rsiIndex);

        // KDJ
        HighlightDrawing kdjHighlightDrawing = new HighlightDrawing();
        kdjHighlightDrawing.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));

        StockKDJIndex kdjIndex = new StockKDJIndex();
        kdjIndex.addDrawing(new KDJDrawing());
        kdjIndex.addDrawing(new StockIndexYLabelDrawing());
        kdjIndex.addDrawing(kdjHighlightDrawing);
        kdjIndex.setPaddingTop(paddingTop);
        kLineRender.addStockIndex(kdjIndex);

        kLineRender.addMarkerView(new YAxisTextMarkerView(stockMarkerViewHeight));
        kLineRender.addMarkerView(new XAxisTextMarkerView(stockMarkerViewHeight));
    }

    private void loadKLineData() {
        new AsyncTask<Void, Void, Void>() {

            private EntrySet entrySet;

            @Override
            protected Void doInBackground(Void... params) {

                PerformenceAnalyser.getInstance().addWatcher();

                String kLineData = "";
                try {
                    InputStream in = getResources().getAssets().open("kline1.txt");
                    int length = in.available();
                    byte[] buffer = new byte[length];
                    in.read(buffer);
                    kLineData = new String(buffer, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                PerformenceAnalyser.getInstance().addWatcher();

                entrySet = StockDataTest.parseKLineData(kLineData);

                PerformenceAnalyser.getInstance().addWatcher();

                entrySet.computeStockIndex();

                PerformenceAnalyser.getInstance().addWatcher();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                kLineView.setEntrySet(entrySet);

                PerformenceAnalyser.getInstance().addWatcher();

                kLineView.notifyDataSetChanged();

                PerformenceAnalyser.getInstance().addWatcher();
            }
        }.execute();
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MACD_RSI_KDJ_Show_Together_Activity.class);
        return intent;
    }
}
