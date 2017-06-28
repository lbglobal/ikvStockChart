package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordplat.quickstart.R;
import com.wordplat.ikvstockchart.InteractiveKLineLayout;
import com.wordplat.ikvstockchart.KLineHandler;
import com.wordplat.ikvstockchart.compat.PerformenceAnalyser;
import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.entry.StockDataTest;
import com.wordplat.ikvstockchart.render.KLineRender;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Enable_Left_And_Right_Refresh_Activity</p>
 * <p>Date: 2017/3/10</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_left_and_right_refresh)
public class Enable_Left_And_Right_Refresh_Activity extends BaseActivity {
    private static final String TAG = "Activity";

    @ViewInject(R.id.kLineLayout) InteractiveKLineLayout kLineLayout = null;
    @ViewInject(R.id.MA_Text) private TextView MA_Text = null;
    @ViewInject(R.id.StockIndex_Text) private TextView StockIndex_Text = null;
    @ViewInject(R.id.Volume_Text) private TextView Volume_Text = null;
    @ViewInject(R.id.Left_Loading_Image) private ImageView Left_Loading_Image = null;
    @ViewInject(R.id.Right_Loading_Image) private ImageView Right_Loading_Image = null;

    private EntrySet entrySet;
    private int loadStartPos = 5500;
    private int loadEndPos = 6000;
    private int loadCount = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        loadKLineData();
    }

    private void initUI() {
        kLineLayout.setKLineHandler(new KLineHandler() {
            @Override
            public void onHighlight(Entry entry, int entryIndex, float x, float y) {
                final SizeColor sizeColor = kLineLayout.getKLineView().getRender().getSizeColor();

                String maString = String.format(getResources().getString(R.string.ma_highlight),
                        entry.getMa5(),
                        entry.getMa10(),
                        entry.getMa20());

                MA_Text.setText(getSpannableString(maString,
                        sizeColor.getMa5Color(),
                        sizeColor.getMa10Color(),
                        sizeColor.getMa20Color()));

                String volumeString = String.format(getResources().getString(R.string.volume_highlight),
                        entry.getVolumeMa5(),
                        entry.getVolumeMa10());

                Volume_Text.setText(getSpannableString(volumeString,
                        sizeColor.getMa5Color(),
                        sizeColor.getMa10Color(),
                        0));

                SpannableString spanString = new SpannableString("");
                if (kLineLayout.isShownMACD()) {
                    String str = String.format(getResources().getString(R.string.macd_highlight),
                            entry.getDiff(),
                            entry.getDea(),
                            entry.getMacd());

                    spanString = getSpannableString(str,
                            sizeColor.getDiffLineColor(),
                            sizeColor.getDeaLineColor(),
                            sizeColor.getMacdHighlightTextColor());

                } else if (kLineLayout.isShownKDJ()) {
                    String str = String.format(getResources().getString(R.string.kdj_highlight),
                            entry.getK(),
                            entry.getD(),
                            entry.getJ());

                    spanString = getSpannableString(str,
                            sizeColor.getKdjKLineColor(),
                            sizeColor.getKdjDLineColor(),
                            sizeColor.getKdjJLineColor());

                } else if (kLineLayout.isShownRSI()) {
                    String str = String.format(getResources().getString(R.string.rsi_highlight),
                            entry.getRsi1(),
                            entry.getRsi2(),
                            entry.getRsi3());

                    spanString = getSpannableString(str,
                            sizeColor.getRsi1LineColor(),
                            sizeColor.getRsi2LineColor(),
                            sizeColor.getRsi3LineColor());

                } else if (kLineLayout.isShownBOLL()) {
                    String str = String.format(getResources().getString(R.string.boll_highlight),
                            entry.getMb(),
                            entry.getUp(),
                            entry.getDn());

                    spanString = getSpannableString(str,
                            sizeColor.getBollMidLineColor(),
                            sizeColor.getBollUpperLineColor(),
                            sizeColor.getBollLowerLineColor());
                }
                StockIndex_Text.setText(spanString);
            }

            @Override
            public void onCancelHighlight() {
                String maString = getResources().getString(R.string.ma_normal);
                MA_Text.setText(maString);
                Volume_Text.setText("");

                String stockIndexString = "";
                if (kLineLayout.isShownMACD()) {
                    stockIndexString = getResources().getString(R.string.macd_normal);
                } else if (kLineLayout.isShownKDJ()) {
                    stockIndexString = getResources().getString(R.string.kdj_normal);
                } else if (kLineLayout.isShownRSI()) {
                    stockIndexString = getResources().getString(R.string.rsi_normal);
                } else if (kLineLayout.isShownBOLL()) {
                    stockIndexString = getResources().getString(R.string.boll_normal);
                }
                StockIndex_Text.setText(stockIndexString);
            }

            @Override
            public void onSingleTap(MotionEvent e, float x, float y) {
                final KLineRender kLineRender = (KLineRender) kLineLayout.getKLineView().getRender();

                if (kLineRender.getKLineRect().contains(x, y)) {
                    Toast.makeText(mActivity, "single tab [" + x + ", " + y + "]", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDoubleTap(MotionEvent e, float x, float y) {
                final KLineRender kLineRender = (KLineRender) kLineLayout.getKLineView().getRender();

                if (kLineRender.getKLineRect().contains(x, y)) {
                    kLineRender.zoomIn(x, y);
                }
            }

            @Override
            public void onLeftRefresh() {
                Left_Loading_Image.setVisibility(View.VISIBLE);
                ((Animatable) Left_Loading_Image.getDrawable()).start();
                // 模拟耗时
                kLineLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Left_Loading_Image.setVisibility(View.GONE);
                        ((Animatable) Left_Loading_Image.getDrawable()).stop();

                        List<Entry> entries = insertEntries();

                        kLineLayout.getKLineView().getRender().getEntrySet().insertFirst(entries);
                        kLineLayout.getKLineView().notifyDataSetChanged();
                        kLineLayout.getKLineView().refreshComplete(entries.size() > 0);

                        if (entries.size() == 0) {
                            Toast.makeText(mActivity, "已经到达最左边了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onRightRefresh() {
                Right_Loading_Image.setVisibility(View.VISIBLE);
                ((Animatable) Right_Loading_Image.getDrawable()).start();
                // 模拟耗时
                kLineLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Right_Loading_Image.setVisibility(View.GONE);
                        ((Animatable) Right_Loading_Image.getDrawable()).start();

                        List<Entry> entries = addEntries();

                        kLineLayout.getKLineView().getRender().getEntrySet().addEntries(entries);
                        kLineLayout.getKLineView().notifyDataSetChanged();
                        kLineLayout.getKLineView().refreshComplete(entries.size() > 0);

                        if (entries.size() == 0) {
                            Toast.makeText(mActivity, "已经到达最右边了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);
            }
        });
    }

    private SpannableString getSpannableString(String str, int partColor0, int partColor1, int partColor2) {
        String[] splitString = str.split("[●]");
        SpannableString spanString = new SpannableString(str);

        int pos0 = splitString[0].length();
        int pos1 = pos0 + splitString[1].length() + 1;
        int end = str.length();

        spanString.setSpan(new ForegroundColorSpan(partColor0),
                pos0, pos1, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);

        if (splitString.length > 2) {
            int pos2 = pos1 + splitString[2].length() + 1;

            spanString.setSpan(new ForegroundColorSpan(partColor1),
                    pos1, pos2, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);

            spanString.setSpan(new ForegroundColorSpan(partColor2),
                    pos2, end, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            spanString.setSpan(new ForegroundColorSpan(partColor1),
                    pos1, end, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        return spanString;
    }

    private void loadKLineData() {
        new AsyncTask<Void, Void, Void>() {
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
                kLineLayout.getKLineView().setEntrySet(loadFirst());

                PerformenceAnalyser.getInstance().addWatcher();

                kLineLayout.getKLineView().notifyDataSetChanged();

                PerformenceAnalyser.getInstance().addWatcher();
            }
        }.execute();
    }

    private EntrySet loadFirst() {
        EntrySet set = new EntrySet();

        for (int i = loadStartPos ; i < loadEndPos ; i++) {
            set.addEntry(entrySet.getEntryList().get(i));
        }
        return set;
    }

    private List<Entry> addEntries() {
        List<Entry> entries = new ArrayList<>();

        int addCount = 0;
        for (int i = loadEndPos ; i < loadEndPos + loadCount && i < entrySet.getEntryList().size() ; i++) {
            addCount++;

            entries.add(entrySet.getEntryList().get(i));
        }
        loadEndPos += addCount;

        return entries;
    }

    private List<Entry> insertEntries() {
        List<Entry> entries = new ArrayList<>();

        int insertCount = 0;
        for (int i = loadStartPos ; i > loadStartPos - loadCount && i > -1 ; i--) {
            insertCount++;

            entries.add(entrySet.getEntryList().get(i));
        }
        loadStartPos -= insertCount;

        return entries;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, Enable_Left_And_Right_Refresh_Activity.class);
        return intent;
    }
}
