package com.qryl.qryl.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hp on 2017/8/21.
 */

public class ServiceFragment extends BaseFragment {

    @Override
    public void loadData() {

    }

    @Override
    public View initView() {
        TextView mView = new TextView(context);
        mView.setText("服务项目列表");
        mView.setTextSize(18);
        mView.setTextColor(Color.BLACK);
        return mView;
    }
}
