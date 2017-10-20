package com.qryl.qryl.fragment.one.two.three;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.ServiceVO.Data;
import com.qryl.qryl.VO.ServiceVO.ItemList;
import com.qryl.qryl.VO.ServiceVO.ServiceVO;
import com.qryl.qryl.activity.H5.XzServicexqActivity;
import com.qryl.qryl.activity.H5.XzxqActivity;
import com.qryl.qryl.adapter.XzServiceAdapter;
import com.qryl.qryl.fragment.one.two.BaseFragment;
import com.qryl.qryl.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hp on 2017/8/21.
 * 服务项目的九宫格
 */

public class XzServiceFragment extends BaseFragment {
    private static final String TAG = "XzServiceFragment";

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;

    private List<ItemList> datas = new ArrayList<>();
    private XzServiceAdapter adapter = new XzServiceAdapter(datas);

    /**
     * 加载数据
     */
    @Override
    public void loadData() {
        postData();
    }

    private void postData() {
        HttpUtil.handleDataFromService("2", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "onResponse: " + result);
                handleJson(result);
            }
        });
    }

    /**
     * 解析json
     *
     * @param result
     */
    private void handleJson(String result) {
        Gson gson = new Gson();
        ServiceVO serviceVO = gson.fromJson(result, ServiceVO.class);
        List<Data> data = serviceVO.getData();
        for (int i = 0; i < data.size(); i++) {
            List<ItemList> itemList = data.get(i).getItemList();
            for (int j = 0; j < itemList.size(); j++) {
                datas.add(new ItemList(itemList.get(j).getId(), itemList.get(j).getName(), itemList.get(j).getHeadshotImg(), itemList.get(j).getAbstracts()));
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_service, null);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datas.clear();
                boolean isRefreshing = swipeRefresh.isRefreshing();
                if (isRefreshing) {
                    postData();
                }
            }
        });
        adapter.setOnItemClickListener(new XzServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 点击了条目:" + position);
                int id = datas.get(position).getId();
                Log.i(TAG, "onItemClick: 点击的id:" + id);
                Intent intent = new Intent(getActivity(), XzServicexqActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        return view;
    }
}
