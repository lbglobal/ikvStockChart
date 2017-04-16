package com.wordplat.quickstart.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordplat.quickstart.R;
import com.wordplat.quickstart.activity.Disable_Left_And_Right_Refresh_Activity;
import com.wordplat.quickstart.activity.Enable_Left_And_Right_Refresh_Activity;
import com.wordplat.quickstart.activity.MACD_RSI_KDJ_Show_Together_Activity;
import com.wordplat.quickstart.activity.Multi_Color_Dynamic_Change_Configuration_Activity;
import com.wordplat.quickstart.activity.Simple_TimeLine_Example_Activity;
import com.wordplat.quickstart.activity.With_Fragment_And_TabLayout_Switcher_Example_Activity;
import com.wordplat.quickstart.activity.With_Pull_To_Refresh_Example_Activity;
import com.wordplat.quickstart.activity.With_RecyclerView_Example_Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afon on 2017/2/10.
 */

public class TextAdapter extends RecyclerView.Adapter<TextViewHolder> {

    private static List<String> textList = new ArrayList<>();

    static {
        textList.add("左滑右滑加载(Enable left and right refresh)");
        textList.add("禁用左滑右滑加载");
        textList.add("多个指标共同显示、联动(MACD, RSI, KDJ)");
        textList.add("在Fragment中使用");
        textList.add("带有下拉刷新的需求中使用");
//        textList.add("动态改变颜色、尺寸配置"); // 还没有准备好
//        textList.add("在 RecyclerView 列表中使用"); // 还没有准备好
        textList.add("简单分时图");
    }

    private Activity mActivity;

    public TextAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_text, parent, false);

        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, final int position) {
        holder.text.setText(textList.get(position));
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = Enable_Left_And_Right_Refresh_Activity.createIntent(mActivity);
                        break;

                    case 1:
                        intent = Disable_Left_And_Right_Refresh_Activity.createIntent(mActivity);
                        break;

                    case 2:
                        intent = MACD_RSI_KDJ_Show_Together_Activity.createIntent(mActivity);
                        break;

                    case 3:
                        intent = With_Fragment_And_TabLayout_Switcher_Example_Activity.createIntent(mActivity);
                        break;

                    case 4:
                        intent = With_Pull_To_Refresh_Example_Activity.createIntent(mActivity);
                        break;

//                    case 5:
//                        intent = Multi_Color_Dynamic_Change_Configuration_Activity.createIntent(mActivity);
//                        break;

//                    case 6:
//                        intent = With_RecyclerView_Example_Activity.createIntent(mActivity);
//                        break;

                    case 5:
                        intent = Simple_TimeLine_Example_Activity.createIntent(mActivity);
                        break;

                    default:
                        break;
                }

                if (intent != null) {
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }
}
