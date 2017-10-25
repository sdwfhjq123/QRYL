package com.qryl.qryl.fragment.one.two.three;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.XzVO.Xz;
import com.qryl.qryl.VO.XzVO.XzData;
import com.qryl.qryl.VO.XzVO.XzInfo;
import com.qryl.qryl.activity.H5.XzxqActivity;
import com.qryl.qryl.adapter.XzServerAdapter;
import com.qryl.qryl.fragment.one.two.BaseFragment;
import com.qryl.qryl.util.ConstantValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hp on 2017/8/21.
 */

public class TnServerFragment extends BaseFragment {

    private static final String TAG = "TnServerFragment";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;

    private List<XzInfo> datas = new ArrayList<>();
    private XzServerAdapter adapter = new XzServerAdapter(datas);
    private int page = 1;
    private int lastVisibleItemPosition;
    private boolean isLoading;
    private int total;

    @Override
    public void loadData() {
        //请求网络数据
        postData(String.valueOf(page));
    }

    /**
     * 请求网络数据
     */
    private void postData(final String page) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("page", page);
        builder.add("limit", "10");
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url(ConstantValue.URL+"/massager/getMasagerList")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
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
     * 处理获取下来的json
     *
     * @param result
     */
    private void handleJson(String result) {
        Gson gson = new Gson();
        Xz xz = gson.fromJson(result, Xz.class);
        XzData xzdata = xz.getData();
        total = xzdata.getTotal();
        List<XzInfo> data = xzdata.getData();
        for (int i = 0; i < data.size(); i++) {
            datas.add(data.get(i));
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setData(datas);
                adapter.notifyDataSetChanged();
                adapter.notifyItemRemoved(adapter.getItemCount());
                swipeRefresh.setRefreshing(false);
            }
        });

    }


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_order_container, null);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

                    boolean isRefreshing = swipeRefresh.isRefreshing();
                    if (isRefreshing) {
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                    }
                    if (!isLoading) {
                        isLoading = true;
                        page += 1;
                        Log.i(TAG, "onScrolled: page=" + page);
                        if (page <= total) {
                            postData(String.valueOf(page));
                        }
                        isLoading = false;
                    }
                }
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                datas.clear();
                postData(String.valueOf(1));
            }
        });

        adapter.setOnItemClickListener(new XzServerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 点击了条目:" + position);
                //获取医护端登录的id
                int id = datas.get(position).getLoginId();
                //获取人员列表的id
                int listId = datas.get(position).getId();
                Log.i(TAG, "onItemClick: 点击的巡诊的id" + id);
                Intent intent = new Intent(getActivity(), XzxqActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", 3);
                intent.putExtra("list_id",listId);
                startActivity(intent);
            }

            @Override
            public void onDeleteItemClick(View view, int position) {

            }
        });
        return view;
    }

}
