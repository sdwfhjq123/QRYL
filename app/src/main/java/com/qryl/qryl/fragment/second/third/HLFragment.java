package com.qryl.qryl.fragment.second.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qryl.qryl.R;
import com.qryl.qryl.adapter.HlAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/8/18.
 * 选择护理按钮后出现的护工列表界面
 */

public class HLFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private List<String> stringList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hl, null);
        initView(view);
        initData();
        HlAdapter adapter = new HlAdapter(stringList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initData() {
        stringList.clear();
        for (int i = 0; i < 20; i++) {
            stringList.add(i + "");
        }
    }

    /**
     * 加载布局
     *
     * @param view
     */
    private void initView(View view) {
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }
}
