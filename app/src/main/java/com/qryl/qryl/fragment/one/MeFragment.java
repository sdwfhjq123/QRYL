package com.qryl.qryl.fragment.one;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qryl.qryl.R;
import com.qryl.qryl.util.UIUtils;

/**
 * Created by hp on 2017/8/16.
 */

public class MeFragment extends android.support.v4.app.Fragment {

    private LinearLayout llSetting;
    private TextView tvHelp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = UIUtils.inflate(R.layout.fragment_me);
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view 绑定的layout
     */
    private void initView(View view) {
        //修改标头
        View viewTitle = View.inflate(UIUtils.getContext(), R.layout.title, null);
        llSetting = (LinearLayout) viewTitle.findViewById(R.id.ll_setting);
        tvHelp = (TextView) viewTitle.findViewById(R.id.tv_help);
        llSetting.setVisibility(View.VISIBLE);
        tvHelp.setVisibility(View.GONE);
    }
}
