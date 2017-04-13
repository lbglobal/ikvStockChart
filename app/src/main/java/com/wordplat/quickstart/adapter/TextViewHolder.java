package com.wordplat.quickstart.adapter;

import android.view.View;
import android.widget.TextView;

import com.wordplat.quickstart.R;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by afon on 2017/2/10.
 */

public class TextViewHolder extends BaseViewHolder {

    @ViewInject(R.id.text) TextView text;

    public TextViewHolder(View itemView) {
        super(itemView);
    }
}
