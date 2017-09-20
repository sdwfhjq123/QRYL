package com.qryl.qryl.fragment.one.two;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.HgPersonVO.Data;
import com.qryl.qryl.VO.HgPersonVO.DataArea;
import com.qryl.qryl.VO.HgPersonVO.Hg;
import com.qryl.qryl.adapter.HlAdapter;
import com.qryl.qryl.adapter.MenuListAdapter;
import com.qryl.qryl.util.UIUtils;
import com.qryl.qryl.view.DropDownMenu;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hp on 2017/8/18.
 * 选择护理按钮后出现的护工列表界面
 */

public class HLFragment extends Fragment {

    private ListView listView2;//创建的性别的布局
    private ListView listView1;//创建的工作时长布局
    private MenuListAdapter mAdapter1;
    private MenuListAdapter mAdapter2;
    private DropDownMenu mDropDownMenu;
    private static final String TAG = "HLFragment";

    private String headers[] = {"工作时长", "性别"};
    private String[] workTimes = {"不限", "8小时", "12小时", "24小时"};
    private String[] sexs = {"不限", "男", "女"};

    private List<View> popupViews = new ArrayList<>();

    private String gender = null;
    private String hours = null;
    private String startTime;
    private String endTime = "2018-12-31 00:00:00";

    private List<DataArea> datas = new ArrayList<>();
    private HlAdapter adapter = new HlAdapter(datas);
    private TimeSelector timeSelector;
    private int lastVisibleItemPosition;
    private boolean isLoading = false;
    private int page = 1;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private int total;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hl, null);
        startTime = getCurrentTime();
        initView(view);
        swipeRefresh.setRefreshing(true);
        initData();
        return view;
    }


    private void initData() {
        postData(String.valueOf(gender), startTime, endTime, String.valueOf(hours));
    }

    /**
     * 加载数据
     */
    private void postData(String gender, String startTime, String endTime, String hours) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("gender", gender);
        builder.add("startTime", startTime);
        builder.add("endTime", endTime);
        builder.add("hours", hours);
        builder.add("page", String.valueOf(page));
        builder.add("limit", String.valueOf(20));
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url("http://192.168.2.134:8080/qryl/carer/getCarerList")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG, "onResponse: " + result);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    }
                });
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
        Hg hg = gson.fromJson(result, Hg.class);
        total = hg.getData().getTotal();
        List<DataArea> data = hg.getData().getData();
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

    /**
     * 加载布局
     *
     * @param view
     */
    private void initView(View view) {
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);
        final TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
        //init menu listview
        //这里是每个下拉菜单之后的布局,目前只是简单的listview作为展示
        listView1 = new ListView(getActivity());
        listView2 = new ListView(getActivity());

        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);

        mAdapter1 = new MenuListAdapter(getActivity(), Arrays.asList(workTimes));
        mAdapter2 = new MenuListAdapter(getActivity(), Arrays.asList(sexs));

        listView1.setAdapter(mAdapter1);
        listView2.setAdapter(mAdapter2);

        popupViews.add(listView1);
        popupViews.add(listView2);

        //选择工作时间
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                mDropDownMenu.setTabText(workTimes[position]);
                assessValueOfWork(workTimes[position]);
                mDropDownMenu.closeMenu();
            }
        });

        //选择性别
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(sexs[position]);
                assessValueOfSex(sexs[position]);
                mDropDownMenu.closeMenu();
            }
        });

        //这里添加 内容显示区域,可以是任何布局
        View contentView = View.inflate(getActivity(), R.layout.hg, null);
        swipeRefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        tvTime.setText(time + ":00");
                        endTime = tvTime.getText().toString();
                        Toast.makeText(getActivity(), time, Toast.LENGTH_SHORT).show();
                    }
                }, startTime, "2018-12-31 00:00");
                timeSelector.show();
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                initData();
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(TAG, "onScrollStateChanged: " + newState);
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    boolean isRefreshing = swipeRefresh.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        swipeRefresh.setRefreshing(false);
                    }
                    if (isLoading) {
                        isLoading = true;
                        page += 1;
                        if (page <= total) {
                            initData();
                        }
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: ");
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
        adapter.setOnItemClickListener(new HlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 点击了条目:" + position);
            }
        });
    }

    private void assessValueOfSex(String position) {
        if (position.equals("男")) {
            gender = String.valueOf(0);
        } else if (position.equals("女")) {
            gender = String.valueOf(1);
        } else if (position.equals("不限")) {
            gender = null;
        }
        postData(gender, startTime, endTime, hours);
    }

    private void assessValueOfWork(String position) {
        //private String[] workTimes = {"不限", "8小时", "12小时", "24小时"};
        if (position.equals("不限")) {
            hours = null;
        } else if (position.equals("8小时")) {
            hours = String.valueOf("8");
        } else if (position.equals("12小时")) {
            hours = String.valueOf("12");
        } else if (position.equals("24小时")) {
            hours = String.valueOf("24");
        }
        postData(gender, startTime, endTime, hours);
    }

    public String getCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
        return date;
    }
}