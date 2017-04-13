package com.wordplat.quickstart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.wordplat.easydivider.RecyclerViewCornerRadius;
import com.wordplat.easydivider.RecyclerViewLinearDivider;
import com.wordplat.quickstart.R;
import com.wordplat.quickstart.adapter.TextAdapter;
import com.wordplat.quickstart.utils.AppUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @ViewInject(R.id.textList) private RecyclerView textList = null;

    private TextAdapter textAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textAdapter = new TextAdapter(mActivity);
        textList.setLayoutManager(new LinearLayoutManager(mActivity));
        textList.setAdapter(textAdapter);

        RecyclerViewCornerRadius cornerRadius = new RecyclerViewCornerRadius(textList);
        cornerRadius.setCornerRadius(AppUtils.dpTopx(mActivity, 10));

        RecyclerViewLinearDivider linearDivider = new RecyclerViewLinearDivider(mActivity, LinearLayoutManager.VERTICAL);
        linearDivider.setDividerSize(1);
        linearDivider.setDividerColor(0xff888888);
        linearDivider.setDividerMargin(AppUtils.dpTopx(mActivity, 10), AppUtils.dpTopx(mActivity, 10));
        linearDivider.setDividerBackgroundColor(0xffffffff);
        linearDivider.setShowHeaderDivider(false);
        linearDivider.setShowFooterDivider(false);

        // 圆角背景必须第一个添加
        textList.addItemDecoration(cornerRadius);
        textList.addItemDecoration(linearDivider);

        for (int i = 0 ; i < 16 ; i++) {
            String result = Integer.toHexString(255 - i * 12);
            Log.i(TAG, "##d onCreate: " + result);
        }
    }

    /**
     * 点击两次退出
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
