package com.qryl.qryl.fragment.one.two;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.VO.DataArea;
import com.qryl.qryl.activity.H5.HgxqActivity;
import com.qryl.qryl.activity.MainActivity;
import com.qryl.qryl.adapter.HlAdapter;
import com.qryl.qryl.adapter.MenuListAdapter;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.UIUtils;
import com.qryl.qryl.view.DropDownMenu;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private String gender = "";
    private String hours = "";
    private String startTime;
    private String endTime = "2018-12-31 00:00:00";

    private List<DataArea> datas = new ArrayList<>();
    private HlAdapter adapter = new HlAdapter(datas);
    private TimeSelector timeSelectorStart;
    private int lastVisibleItemPosition;
    private boolean isLoading = false;
    private int page = 1;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private int total;
    private TimeSelector timeSelectorEnd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hl, null);
        startTime = getCurrentTime();
        initView(view);
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
        swipeRefresh.setRefreshing(true);
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("gender", gender);
        builder.add("startTime", startTime);
        builder.add("endTime", endTime);
        builder.add("hours", hours);
        builder.add("page", String.valueOf(page));
        builder.add("limit", String.valueOf(20));
        Log.i(TAG, "postData: 参数：gender:" + gender + " ,start:" + startTime + ",end:" + endTime + ",hours:" + hours);
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url(ConstantValue.URL + "/carer/getCarerList")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG, "onResponse: " + result);
                Log.i(TAG, "onResponse: page大小" + page);
                handleJson(result);
            }
        });
    }

    /**
     * 解析json
     *
     * @param result 获取的网络的数据
     */
    private void handleJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            final JSONObject data = jsonObject.getJSONObject("data");
            total = data.getInt("total");
            JSONArray jsonArray = data.getJSONArray("data");
            Log.i(TAG, "handleJson: jsonArray size:" + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                Log.i(TAG, "handleJson: jo size:" + jo.length());
                int id = jo.getInt("loginId");
                int listId = jo.getInt("id");
                String realName = jo.getString("realName");
                int gender = jo.getInt("gender");
                int age = jo.getInt("age");
                int workYears = jo.getInt("workYears");
                String headshotImg = jo.getString("headshotImg");
                datas.add(new DataArea(listId, id, realName, gender, age, workYears, headshotImg));
            }
            if (getActivity() instanceof MainActivity) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: data.size()" + datas.size());
                        adapter.setData(datas);
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载布局
     *
     * @param view fragment
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
                timeSelectorEnd = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handle(String time) {
                        tvTime.setText(time + ":00");
                        endTime = tvTime.getText().toString();
                        Toast.makeText(getActivity(), time, Toast.LENGTH_SHORT).show();
                        datas.clear();
                        page = 1;
                        postData(String.valueOf(gender), startTime, endTime, String.valueOf(hours));
                    }
                }, startTime, "2018-12-31 00:00");
                timeSelectorEnd.setTitle("请选择结束时间");
                timeSelectorEnd.show();

                timeSelectorStart = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handle(String time) {
                        tvTime.setText(time + ":00");
                        startTime = tvTime.getText().toString();
                        //Toast.makeText(getActivity(), time, Toast.LENGTH_SHORT).show();
                        //datas.clear();
                        //page = 1;
                        // postData(String.valueOf(gender), startTime, endTime, String.valueOf(hours));
                    }
                }, startTime, "2017-7-31 00:00");
                timeSelectorStart.setTitle("请选择开始时间");
                timeSelectorStart.show();


            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datas.clear();
                String gender = "";
                String starTime = "2017-07-07 00:00:00";
                String endTime = "2018-12-12 00:00:00";
                String hours = "";
                page = 1;
                postData(gender, starTime, endTime, hours);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
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
                        page = 1;
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        swipeRefresh.setRefreshing(false);
                    }
                    if (!isLoading) {
                        isLoading = true;
                        page += 1;
                        if (page <= total) {
                            postData(String.valueOf(gender), startTime, endTime, String.valueOf(hours));
                        } else {
                            Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT).show();

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
                int id = datas.get(position).getId();
                int loginId = datas.get(position).getLoginId();
                Log.i(TAG, "onItemClick: 点击的id" + id);
                Log.i(TAG, "onItemClick: 点击的loginId" + loginId);
                Intent intent = new Intent(getActivity(), HgxqActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("login_id", loginId);
                startActivity(intent);
            }
        });
    }

    private void assessValueOfSex(String position) {
        switch (position) {
            case "男":
                gender = String.valueOf(0);
                break;
            case "女":
                gender = String.valueOf(1);
                break;
            case "不限":
                gender = "";
                break;
        }
        page = 1;
        datas.clear();
        postData(gender, startTime, endTime, hours);
    }

    private void assessValueOfWork(String position) {
        //private String[] workTimes = {"不限", "8小时", "12小时", "24小时"};
        switch (position) {
            case "不限":
                hours = "";
                break;
            case "8小时":
                hours = String.valueOf("8");
                break;
            case "12小时":
                hours = String.valueOf("12");
                break;
            case "24小时":
                hours = String.valueOf("24");
                break;
        }
        page = 1;
        datas.clear();
        postData(gender, startTime, endTime, hours);
    }

    public String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
        return "2017-07-07 00:00:00";
    }
}