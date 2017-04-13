package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.wordplat.quickstart.R;
import com.wordplat.quickstart.fragment.KLineFragment;
import com.wordplat.quickstart.mvp.YahooStockPresenter;
import com.wordplat.quickstart.widget.pulllistview.PullListLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * <p>With_Fragment_And_TabLayout_Switcher_Example_Activity</p>
 * <p>Date: 2017/3/31</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_with_fragment_and_tablayout_switcher)
public class With_Fragment_And_TabLayout_Switcher_Example_Activity extends BaseActivity {

    @ViewInject(R.id.tabLayout) private TabLayout tabLayout = null;
    @ViewInject(R.id.pullListLayout) private PullListLayout pullListLayout = null;

    private KLineFragment dayKLineFragment;
    private KLineFragment weekKLineFragment;
    private KLineFragment monthKLineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {
        pullListLayout.setPtrHandler(ptrHandler);

        dayKLineFragment = KLineFragment.newInstance(YahooStockPresenter.KLineType.DAY);
        weekKLineFragment = KLineFragment.newInstance(YahooStockPresenter.KLineType.WEEK);
        monthKLineFragment = KLineFragment.newInstance(YahooStockPresenter.KLineType.MONTH);

        final String[] tabStrings = new String[]{"日K", "周K", "月K"};

        tabLayout.removeAllTabs();
        for (int i = 0; i < tabStrings.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabStrings[i]);
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        addFragment(dayKLineFragment);
    }

    ///////////////////////////////////////////////////////////////////////////
    // fragment 显示隐藏
    ///////////////////////////////////////////////////////////////////////////

    private void hideAllFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!dayKLineFragment.isHidden()) {
            ft.hide(dayKLineFragment);
        }
        if (!weekKLineFragment.isHidden()) {
            ft.hide(weekKLineFragment);
        }
        if (!monthKLineFragment.isHidden()) {
            ft.hide(monthKLineFragment);
        }
        ft.commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    private void addFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.kLineContent, fragment);
            ft.commit();
        }
    }

    private void switchChart(int position) {
        switch (position) {
            case 0:
                showFragment(dayKLineFragment);
                break;

            case 1:
                addFragment(weekKLineFragment);
                showFragment(weekKLineFragment);
                break;

            case 2:
                addFragment(monthKLineFragment);
                showFragment(monthKLineFragment);
                break;
        }
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            hideAllFragment();
            switchChart(tab.getPosition());
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}
        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    ///////////////////////////////////////////////////////////////////////////
    // 结束：fragment 显示隐藏
    ///////////////////////////////////////////////////////////////////////////

    private PtrDefaultHandler ptrHandler = new PtrDefaultHandler() {
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            pullListLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullListLayout.refreshComplete();
                }
            }, 2000);
        }
    };

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, With_Fragment_And_TabLayout_Switcher_Example_Activity.class);
        return intent;
    }
}
