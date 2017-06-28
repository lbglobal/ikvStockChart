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
import com.wordplat.quickstart.bean.KLineBean;
import com.wordplat.quickstart.mvp.LoadingViewListener;
import com.wordplat.quickstart.mvp.StockPresenter;
import com.wordplat.ikvstockchart.InteractiveKLineLayout;
import com.wordplat.ikvstockchart.KLineHandler;
import com.wordplat.ikvstockchart.entry.Entry;
import com.wordplat.ikvstockchart.entry.EntrySet;
import com.wordplat.ikvstockchart.entry.SizeColor;
import com.wordplat.ikvstockchart.render.KLineRender;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

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
    @ViewInject(R.id.Volume_Text) private TextView Volume_Text = null;
    @ViewInject(R.id.Left_Loading_Image) private ImageView Left_Loading_Image = null;
    @ViewInject(R.id.Right_Loading_Image) private ImageView Right_Loading_Image = null;

    private static final String STOCK_CODE = "600030"; // 中信证券
    private static final int REQUEST_STOCK_FIRST = 1;
    private static final int REQUEST_STOCK_PREV = 2;
    private static final int REQUEST_STOCK_NEXT = 3;

    private StockPresenter.KLineType kLineType;

    private final EntrySet entrySet = new EntrySet();

    private final StockPresenter presenter = new StockPresenter();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.attachView(viewListener);
        presenter.loadFirst(REQUEST_STOCK_FIRST, STOCK_CODE, kLineType);
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.detachView();
    }

    private void initUI() {
        kLineType = (StockPresenter.KLineType) getArguments().getSerializable(A);

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
                presenter.loadPrev(REQUEST_STOCK_PREV, STOCK_CODE, kLineType);
            }

            @Override
            public void onRightRefresh() {
                presenter.loadNext(REQUEST_STOCK_NEXT, STOCK_CODE, kLineType);
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

    private LoadingViewListener viewListener = new LoadingViewListener() {
        @Override
        public void onStartRequest(int requestCode) {
            switch (requestCode) {
                case REQUEST_STOCK_PREV:
                    Left_Loading_Image.setVisibility(View.VISIBLE);
                    ((Animatable) Left_Loading_Image.getDrawable()).start();
                    break;

                case REQUEST_STOCK_NEXT:
                    Right_Loading_Image.setVisibility(View.VISIBLE);
                    ((Animatable) Right_Loading_Image.getDrawable()).start();
                    break;
            }
        }

        @Override
        public void onFinishRequest(int requestCode) {
            switch (requestCode) {
                case REQUEST_STOCK_PREV:
                    Left_Loading_Image.setVisibility(View.GONE);
                    ((Animatable) Left_Loading_Image.getDrawable()).stop();
                    break;

                case REQUEST_STOCK_NEXT:
                    Right_Loading_Image.setVisibility(View.GONE);
                    ((Animatable) Right_Loading_Image.getDrawable()).start();
                    break;
            }
        }

        @Override
        public void onSuccess(int requestCode) {
            List<KLineBean> response = presenter.getkLineList();

            switch (requestCode) {
                case REQUEST_STOCK_FIRST:
                    for (KLineBean kLineBean : response) {
                        entrySet.addEntry(new Entry(kLineBean.getOpen(),
                                kLineBean.getHigh(),
                                kLineBean.getLow(),
                                kLineBean.getClose(),
                                kLineBean.getVolume(),
                                kLineBean.getDate()));
                    }
                    entrySet.computeStockIndex();
                    kLineLayout.getKLineView().setEntrySet(entrySet);
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    break;

                case REQUEST_STOCK_PREV:
                    for (int i = response.size() - 1 ; i >= 0 ; i--) {
                        KLineBean kLineBean = response.get(i);
                        entrySet.insertFirst(new Entry(kLineBean.getOpen(),
                                kLineBean.getHigh(),
                                kLineBean.getLow(),
                                kLineBean.getClose(),
                                kLineBean.getVolume(),
                                kLineBean.getDate()));
                    }
                    entrySet.computeStockIndex();
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    kLineLayout.getKLineView().refreshComplete(response.size() > 0);
                    break;

                case REQUEST_STOCK_NEXT:
                    for (KLineBean kLineBean : response) {
                        entrySet.addEntry(new Entry(kLineBean.getOpen(),
                                kLineBean.getHigh(),
                                kLineBean.getLow(),
                                kLineBean.getClose(),
                                kLineBean.getVolume(),
                                kLineBean.getDate()));
                    }
                    entrySet.computeStockIndex();
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    kLineLayout.getKLineView().refreshComplete(response.size() > 0);
                    break;
            }
        }

        @Override
        public void onResultEmpty(int requestCode) {
            switch (requestCode) {
                case REQUEST_STOCK_FIRST:
                    entrySet.setLoadingStatus(false);
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    break;

                case REQUEST_STOCK_PREV:
                    kLineLayout.getKLineView().refreshComplete(false);
                    Toast.makeText(mActivity, "已经到达最左边了", Toast.LENGTH_SHORT).show();
                    break;

                case REQUEST_STOCK_NEXT:
                    kLineLayout.getKLineView().refreshComplete(false);
                    Toast.makeText(mActivity, "已经到达最右边了", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onNetworkTimeOutError(int requestCode) {
            super.onNetworkTimeOutError(requestCode);

            switch (requestCode) {
                case REQUEST_STOCK_FIRST:
                    entrySet.setLoadingStatus(false);
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onNoNetworkError(int requestCode) {
            super.onNoNetworkError(requestCode);

            switch (requestCode) {
                case REQUEST_STOCK_FIRST:
                    entrySet.setLoadingStatus(false);
                    kLineLayout.getKLineView().notifyDataSetChanged();
                    break;
            }
        }
    };

    private static final String A = "a";

    public static KLineFragment newInstance(StockPresenter.KLineType kLineType) {
        Bundle args = new Bundle();
        args.putSerializable(A, kLineType);

        KLineFragment fragment = new KLineFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
