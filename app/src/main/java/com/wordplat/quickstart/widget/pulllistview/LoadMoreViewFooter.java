package com.wordplat.quickstart.widget.pulllistview;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory;

/**
 * Created by afon on 2016/12/9.
 */

public class LoadMoreViewFooter implements ILoadMoreViewFactory {

    private LoadMoreHelper loadMoreHelper;

    public LoadMoreHelper getLoadMoreView() {
        return loadMoreHelper;
    }

    @Override
    public ILoadMoreView madeLoadMoreView() {
        loadMoreHelper =  new LoadMoreHelper();
        return loadMoreHelper;
    }

    public static class LoadMoreHelper implements ILoadMoreView {

        protected View footerView;
        protected TextView footerTv;
        protected ProgressBar footerBar;

        protected View.OnClickListener onClickRefreshListener;

        @Override
        public void init(FootViewAdder footViewHolder, View.OnClickListener onClickRefreshListener) {
            footerView = footViewHolder.addFootView(com.chanven.lib.cptr.R.layout.loadmore_default_footer);
            footerTv = (TextView) footerView.findViewById(com.chanven.lib.cptr.R.id.loadmore_default_footer_tv);
            footerBar = (ProgressBar) footerView.findViewById(com.chanven.lib.cptr.R.id.loadmore_default_footer_progressbar);
            footerView.setClickable(true);
            footerView.setFocusable(true);
            footerView.setFocusableInTouchMode(true);
            this.onClickRefreshListener = onClickRefreshListener;
            showNormal();
        }

        @Override
        public void showNormal() {
            footerTv.setText("点击加载更多");
            footerBar.setVisibility(View.GONE);
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showLoading() {
            footerTv.setText("正在加载中...");
            footerBar.setVisibility(View.VISIBLE);
            footerView.setOnClickListener(null);
        }

        @Override
        public void showFail(Exception exception) {
            footerTv.setText("加载失败，点击重新加载");
            footerBar.setVisibility(View.GONE);
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showNomore() {
            footerTv.setText("已经加载完毕");
            footerBar.setVisibility(View.GONE);
            footerView.setOnClickListener(null);
        }

        @Override
        public void setFooterVisibility(boolean isVisible) {
            footerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
