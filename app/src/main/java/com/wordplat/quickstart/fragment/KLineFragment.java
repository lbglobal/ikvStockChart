package com.wordplat.quickstart.fragment;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordplat.quickstart.R;
import com.wordplat.quickstart.bean.YahooKLineBean;
import com.wordplat.quickstart.bean.response.YahooResponse;
import com.wordplat.quickstart.mvp.YahooStockListener;
import com.wordplat.quickstart.mvp.YahooStockPresenter;
import com.wordplat.ikvstockchart.InteractiveKLineLayout;
import com.wordplat.ikvstockchart.KLineHandler;
import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.KLineRender;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * <p>KLineFragment</p>
 * <p>Date: 2017/4/5</p>
 *
 * @author afon
 */

@ContentView(R.layout.fragment_kline)
public class KLineFragment extends BaseFragment {
    private static final String TAG = "KLineFragment";

    @ViewInject(R.id.kLineLayout) private InteractiveKLineLayout kLineLayout = null;
    @ViewInject(R.id.MA_Text) private TextView MA_Text = null;
    @ViewInject(R.id.StockIndex_Text) private TextView StockIndex_Text = null;
    @ViewInject(R.id.Left_Loading_Image) private ImageView Left_Loading_Image = null;
    @ViewInject(R.id.Right_Loading_Image) private ImageView Right_Loading_Image = null;

    private static final String STOCK_CODE = "XOM";
    private static final int REQUEST_XOM_FIRST = 1;
    private static final int REQUEST_XOM_PREV = 2;
    private static final int REQUEST_XOM_NEXT = 3;

    private YahooStockPresenter.KLineType kLineType;

    private final EntrySet entrySet = new EntrySet();

    private final YahooStockPresenter presenter = new YahooStockPresenter();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.attachView(yahooStockListener);
        presenter.loadFirst(REQUEST_XOM_FIRST, STOCK_CODE, kLineType);
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.detachView();
    }

    private void initUI() {
        kLineType = (YahooStockPresenter.KLineType) getArguments().getSerializable(A);

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
                presenter.loadPrev(REQUEST_XOM_PREV, STOCK_CODE, kLineType);
            }

            @Override
            public void onRightRefresh() {
                presenter.loadNext(REQUEST_XOM_NEXT, STOCK_CODE, kLineType);
            }
        });
    }

    private SpannableString getSpannableString(String str, int partColor0, int partColor1, int partColor2) {
        String[] splitString = str.split("[●]");
        SpannableString spanString = new SpannableString(str);

        int pos0 = splitString[0].length();
        int pos1 = pos0 + splitString[1].length() + 1;
        int pos2 = pos1 + splitString[2].length() + 1;
        int end = str.length();

        spanString.setSpan(new ForegroundColorSpan(partColor0),
                pos0, pos1, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);

        spanString.setSpan(new ForegroundColorSpan(partColor1),
                pos1, pos2, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);

        spanString.setSpan(new ForegroundColorSpan(partColor2),
                pos2, end, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);

        return spanString;
    }

    private YahooStockListener yahooStockListener = new YahooStockListener() {
        @Override
        public void onStartRequest(int requestCode) {
            switch (requestCode) {
                case REQUEST_XOM_PREV:
                    Left_Loading_Image.setVisibility(View.VISIBLE);
                    ((Animatable) Left_Loading_Image.getDrawable()).start();
                    break;

                case REQUEST_XOM_NEXT:
                    Right_Loading_Image.setVisibility(View.VISIBLE);
                    ((Animatable) Right_Loading_Image.getDrawable()).start();
                    break;
            }
        }

        @Override
        public void onFinishRequest(int requestCode) {
            switch (requestCode) {
                case REQUEST_XOM_PREV:
                    Left_Loading_Image.setVisibility(View.GONE);
                    ((Animatable) Left_Loading_Image.getDrawable()).stop();
                    break;

                case REQUEST_XOM_NEXT:
                    Right_Loading_Image.setVisibility(View.GONE);
                    ((Animatable) Right_Loading_Image.getDrawable()).start();
                    break;
            }
        }

        @Override
        public void onSuccess(int requestCode, YahooResponse response) {
            switch (requestCode) {
                case REQUEST_XOM_FIRST:
                    for (YahooKLineBean yahooKLineBean : response.getKLineList()) {
                        entrySet.addEntry(new Entry(yahooKLineBean.getOpen(),
                                yahooKLineBean.getHigh(),
                                yahooKLineBean.getLow(),
                                yahooKLineBean.getClose(),
                                yahooKLineBean.getVolume(),
                                yahooKLineBean.getDate()));
                    }
                    entrySet.computeStockIndex();
                    kLineLayout.getKLineView().setEntrySet(entrySet);
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    break;

                case REQUEST_XOM_PREV:
                    for (int i = response.getKLineList().size() - 1 ; i >= 0 ; i--) {
                        YahooKLineBean yahooKLineBean = response.getKLineList().get(i);
                        entrySet.insertFirst(new Entry(yahooKLineBean.getOpen(),
                                yahooKLineBean.getHigh(),
                                yahooKLineBean.getLow(),
                                yahooKLineBean.getClose(),
                                yahooKLineBean.getVolume(),
                                yahooKLineBean.getDate()));
                    }
                    entrySet.computeStockIndex();
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    kLineLayout.getKLineView().refreshComplete(response.getKLineList().size() > 0);
                    break;

                case REQUEST_XOM_NEXT:
                    for (YahooKLineBean yahooKLineBean : response.getKLineList()) {
                        entrySet.addEntry(new Entry(yahooKLineBean.getOpen(),
                                yahooKLineBean.getHigh(),
                                yahooKLineBean.getLow(),
                                yahooKLineBean.getClose(),
                                yahooKLineBean.getVolume(),
                                yahooKLineBean.getDate()));
                    }
                    entrySet.computeStockIndex();
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    kLineLayout.getKLineView().refreshComplete(response.getKLineList().size() > 0);
                    break;
            }
        }

        @Override
        public void onResultEmpty(int requestCode) {
            switch (requestCode) {
                case REQUEST_XOM_FIRST:
                    break;

                case REQUEST_XOM_PREV:
                    kLineLayout.getKLineView().refreshComplete(false);
                    Toast.makeText(mActivity, "已经到达最左边了", Toast.LENGTH_SHORT).show();
                    break;

                case REQUEST_XOM_NEXT:
                    kLineLayout.getKLineView().refreshComplete(false);
                    Toast.makeText(mActivity, "已经到达最右边了", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private static final String A = "a";

    public static KLineFragment newInstance(YahooStockPresenter.KLineType kLineType) {
        Bundle args = new Bundle();
        args.putSerializable(A, kLineType);

        KLineFragment fragment = new KLineFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
