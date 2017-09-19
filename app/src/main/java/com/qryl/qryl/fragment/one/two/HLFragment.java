package com.qryl.qryl.fragment.one.two;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.HgPersonVO.Data;
import com.qryl.qryl.VO.HgPersonVO.Hg;
import com.qryl.qryl.adapter.HlAdapter;
import com.qryl.qryl.adapter.MenuListAdapter;
import com.qryl.qryl.util.UIUtils;
import com.qryl.qryl.view.DropDownMenu;

import java.io.IOException;
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


    private String headers[] = {"工作时长", "性别"};
    private String[] workTimes = {"8小时", "12小时", "24小时"};
    private String[] sexs = {"不限", "男", "女"};

    private List<View> popupViews = new ArrayList<>();

    private int gender = 2;
    private int hours = 0;
    private String startTime = null;
    private String endTime = null;

    private List<Data> datas=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hl, null);
        initView(view);
        initData();
        return view;
    }


    private void initData() {
        postData(gender, startTime, endTime, hours);
    }

    /**
     * 加载数据
     */
    private void postData(int gender, String startTime, String endTime, int hours) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("gender", String.valueOf(gender));
        builder.add("startTime", startTime);
        builder.add("endTime", endTime);
        builder.add("hours", String.valueOf(hours));
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
                String result = response.body().string();
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
        List<Data> data = hg.getData();
    }

    /**
     * 加载布局
     *
     * @param view
     */
    private void initView(View view) {
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);
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
                assessValueOfWork(position);
                mDropDownMenu.closeMenu();
            }
        });

        //选择性别
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(sexs[position]);
                assessValueOfSex(position);
                mDropDownMenu.closeMenu();
            }
        });

        //这里添加 内容显示区域,可以是任何布局
        View contentView = View.inflate(getActivity(), R.layout.hg, null);
        SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    private void assessValueOfSex(int position) {

    }

    private void assessValueOfWork(int position) {

    }

}