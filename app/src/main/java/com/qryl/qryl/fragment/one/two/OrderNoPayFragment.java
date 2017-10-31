package com.qryl.qryl.fragment.one.two;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.OrderVO.Order;
import com.qryl.qryl.VO.OrderVO.OrderInfoArea;
import com.qryl.qryl.activity.H5.OrderInfoActivity;
import com.qryl.qryl.activity.MainActivity;
import com.qryl.qryl.activity.PayActivity;
import com.qryl.qryl.adapter.OrderNopayAdapter;
import com.qryl.qryl.adapter.OrderUnderwayAdapter;
import com.qryl.qryl.util.AliPay;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.PayResult;

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

public class OrderNoPayFragment extends BaseFragment {

    private static final String TAG = "OrderNoPayFragment";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;

    private List<OrderInfoArea> datas = new ArrayList<>();
    private OrderNopayAdapter adapter = new OrderNopayAdapter(datas);
    private int page = 1;
    private int lastVisibleItemPosition;
    private boolean isLoading;
    private String userId;

    private static final int SDK_PAY_FLAG = 1001;
    public static final String APPID = "";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.i("Pay", "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void loadData() {
        //请求网络数据
        postData(String.valueOf(page));
    }

    /**
     * 请求网络数据
     */
    private void postData(final String page) {
        for (int i = 0; i < 3; i++) {
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("status", "0");
            builder.add("orderType", String.valueOf(i));
            builder.add("puId", userId);//动态获取，需要写缓存
            builder.add("page", page);
            builder.add("limit", "20");
            FormBody formBody = builder.build();
            final Request request = new Request.Builder()
                    .url(ConstantValue.URL + "/order/getOrderListByStatus")
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
                    //Log.i(TAG, "onResponse: 页数" + page);
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
                                //Log.i(TAG, "onResponse: " + result);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 处理获取下来的json
     *
     * @param result
     */
    private void handleJson(String result) {
        Gson gson = new Gson();
        Order order = gson.fromJson(result, Order.class);
        List<OrderInfoArea> data = order.getData().getData();
        for (int i = 0; i < data.size(); i++) {
            datas.add(data.get(i));
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
        adapter.setOnItemClickListener(new OrderNopayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 点击了订单列表" + position);
                if (getActivity() instanceof MainActivity) {
                    OrderInfoActivity.actionStart(getActivity(), datas.get(position).getId(), datas.get(position).getOrderType());
                }
            }

            @Override
            public void onDeleteItemClick(View view, final int position) {//删除订单
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("取消订单");
                builder.setMessage("您确定要取消订单吗");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOrderItem(position);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }

            @Override
            public void onPayItemClick(View view, int position) {//支付订单
//                int orderId = datas.get(position).getId();
//                int orderType = datas.get(position).getOrderType();

//                orderPay(orderId, orderType);
                if (getActivity() instanceof MainActivity) {
                    //点击跳转PayActivity
                    PayActivity.actionStart(getActivity(), datas.get(position).getPrice());
                }
            }
        });
        return view;
    }

    /**
     * 删除订单条目
     *
     * @param position
     */
    private void deleteOrderItem(final int position) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("orderId", String.valueOf(datas.get(position).getId()));
        builder.add("orderType", String.valueOf(datas.get(position).getOrderType()));
        builder.add("puId", userId);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(ConstantValue.URL + "/order/delOrder")
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
                Log.i(TAG, "onResponse: 删除订单接口后的回调数据" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String resultCode = jsonObject.getString("resultCode");
                    if (resultCode.equals("500")) {
                        final String erroMessage = jsonObject.getString("erroMessage");
                        if (getActivity() instanceof MainActivity) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), erroMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else if (resultCode.equals("200")) {
                        if (getActivity() instanceof MainActivity) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                    datas.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 支付订单
     *
     * @param orderId   订单id
     * @param orderType 订单类型
     */
    private void orderPay(int orderId, int orderType) {
        if (getActivity() instanceof MainActivity) {
            AliPay.Builder builder = new AliPay.Builder(getActivity());
            builder.setSELLER("2354507474@qq.com")
                    .setPARTNER("2222")
                    .setNotifyURL("alipay.trade.app.pay")
                    .setPrice("0.01")
                    .setOrderTitle("测试商品")
                    .setSubTitle("测试")
                    .setPayCallBackListener(new AliPay.Builder.PayCallBackListener() {
                        @Override
                        public void onPayCallBack(int status, String resultStatus, String progress) {
                            Toast.makeText(getActivity(), progress + "123", Toast.LENGTH_LONG).show();
                            Log.i(TAG, "onPayCallBack: 支付宝测试" + resultStatus);
                        }
                    });
            builder.pay();
        }

    }
}
