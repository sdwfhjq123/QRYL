package com.qryl.qryl.fragment.one.two;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.MakeList.DataArea;
import com.qryl.qryl.VO.MakeList.MakeList;
import com.qryl.qryl.VO.OrderVO.Order;
import com.qryl.qryl.VO.OrderVO.OrderInfoArea;
import com.qryl.qryl.activity.H5.MakeListActivity;
import com.qryl.qryl.activity.H5.OrderInfoActivity;
import com.qryl.qryl.activity.MainActivity;
import com.qryl.qryl.activity.login.LoginActivity;
import com.qryl.qryl.adapter.OrderMakeListAdapter;
import com.qryl.qryl.adapter.OrderNopayAdapter;
import com.qryl.qryl.fragment.one.two.BaseFragment;
import com.qryl.qryl.util.ConstantValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hp on 2017/9/12.
 */

public class OrderMakeListFragment extends BaseFragment {

    private static final String TAG = "OrderMakeListFragment";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;

    private List<DataArea> datas = new ArrayList<>();
    private OrderMakeListAdapter adapter = new OrderMakeListAdapter(datas);
    private int page = 1;
    private int lastVisibleItemPosition;
    private boolean isLoading;
    private String userId;

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
//        builder.add("puId", userId);//动态获取，需要写缓存
        builder.add("puId", userId);//动态获取，需要写缓存
        builder.add("page", page);
        builder.add("limit", "1");
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url(ConstantValue.URL + "/order/getPrescribeList")
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
                Log.i(TAG, "开单子获取的数据 " + result);
                Log.i(TAG, "onResponse: 页数" + page);
                //判断data里面resultCode是否有500然后判断是否有数据或者
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String resultCode = jsonObject.getString("resultCode");
                    if (resultCode.equals("500")) {
                        return;
                    } else if (resultCode.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (data != null) {
                            handleJson(result);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        MakeList makeList = gson.fromJson(result, MakeList.class);
        final List<DataArea> data = makeList.getData().getData();
        for (int i = 0; i < data.size(); i++) {
            datas.add(data.get(i));
            Log.i(TAG, "run: 是否存储了drug" + datas.get(i).getDrugsList().get(i).getDrugName());
        }
        if (getActivity() instanceof MainActivity) {
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

    }

    @Override
    public View initView() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
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
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        swipeRefresh.setRefreshing(false);
                    }
                    if (!isLoading) {
                        isLoading = true;
                        page += 1;
                        Log.i(TAG, "onScrolled: page=" + page);
                        postData(String.valueOf(page));
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
        adapter.setOnItemClickListener(new OrderMakeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 点击了订单列表" + position);
                if (getActivity() instanceof MainActivity) {
                    if (getActivity() instanceof MainActivity) {
                        //启动h5开单子详情
                        MakeListActivity.actionStart(getActivity(), datas.get(position).getId());
                    }
                }
            }

            @Override
            public void onDeleteItemClick(View view, int position) {//删除订单
                Log.i(TAG, "onItemClick: 点击了删除按钮" + position);
            }

            @Override
            public void onPayItemClick(View view, int position) {//支付订单
                Log.i(TAG, "onItemClick: 点击了支付按钮" + position);
            }
        });
        return view;
    }


    /**
     * 支付订单
     *
     * @param orderId   订单id
     * @param orderType 订单类型
     */
    private void orderPay(int orderId, int orderType) {
        final String orderInfo = null;//获取下来的订单信息
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                //msg.what=SDK_PAY_FLAG:
            }
        };
    }
}
