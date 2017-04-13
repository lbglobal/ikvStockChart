package com.wordplat.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <p>With_RecyclerView_Example_Activity</p>
 * <p>Date: 2017/3/31</p>
 *
 * @author afon
 */

public class With_RecyclerView_Example_Activity extends BaseActivity {

    private static final String TAG = "Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 2017/4/11
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, With_RecyclerView_Example_Activity.class);
        return intent;
    }
}
