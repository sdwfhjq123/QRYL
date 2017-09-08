package com.qryl.qryl.fragment.one.two.three;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qryl.qryl.R;
import com.qryl.qryl.adapter.TnServerAdapter;
import com.qryl.qryl.adapter.XzServerAdapter;
import com.qryl.qryl.fragment.one.two.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/8/21.
 */

public class TnServerFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;

    private List<String> stringList = new ArrayList<>();
    private TnServerAdapter adapter;

    //加载数据
    @Override
    public void loadData() {
        stringList.clear();
        for (int i = 0; i < 20; i++) {
            stringList.add(i + "");
        }
        adapter = new TnServerAdapter(stringList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //GridLayoutManager layoutManager = new GridLayoutManager(UIUtils.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView();
            }
        });

    }

    /**
     * 刷新布局
     */
    private void refreshView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public View initView() {
//        TextView mView = new TextView(context);
//        mView.setText("服务项目列表");
//        mView.setTextSize(18);
//        mView.setTextColor(Color.BLACK);
//        return mView;
        View view = View.inflate(mContext, R.layout.fragment_service, null);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }
}
