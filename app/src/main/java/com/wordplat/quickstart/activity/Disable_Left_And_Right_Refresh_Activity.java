package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <p>Disable_Left_And_Right_Refresh_Activity</p>
 * <p>Date: 2017/3/31</p>
 *
 * @author afon
 */

public class Disable_Left_And_Right_Refresh_Activity extends Enable_Left_And_Right_Refresh_Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kLineLayout.getKLineView().setEnableLeftRefresh(false);
        kLineLayout.getKLineView().setEnableRightRefresh(false);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, Disable_Left_And_Right_Refresh_Activity.class);
        return intent;
    }
}
