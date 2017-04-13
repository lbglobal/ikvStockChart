package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <p>Multi_Color_Dynamic_Change_Configuration_Activity</p>
 * <p>Date: 2017/3/31</p>
 *
 * @author afon
 */

public class Multi_Color_Dynamic_Change_Configuration_Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 2017/4/11
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, Multi_Color_Dynamic_Change_Configuration_Activity.class);
        return intent;
    }
}
