package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.wordplat.quickstart.R;
import com.wordplat.quickstart.widget.pulllistview.PullListLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * <p>With_Pull_To_Refresh_Example_Activity</p>
 * <p>Date: 2017/3/31</p>
 *
 * @author afon
 */

@ContentView(R.layout.activity_with_pull_to_refresh)
public class With_Pull_To_Refresh_Example_Activity extends Enable_Left_And_Right_Refresh_Activity {

    @ViewInject(R.id.pullListLayout) private PullListLayout pullListLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {
        pullListLayout.setPtrHandler(ptrHandler);
    }

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
        Intent intent = new Intent(context, With_Pull_To_Refresh_Example_Activity.class);
        return intent;
    }
}
