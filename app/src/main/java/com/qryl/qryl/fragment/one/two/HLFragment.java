package com.qryl.qryl.fragment.one.two;

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

import com.qryl.qryl.R;
import com.qryl.qryl.adapter.HlAdapter;
import com.qryl.qryl.adapter.MenuListAdapter;
import com.qryl.qryl.util.UIUtils;
import com.qryl.qryl.view.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hp on 2017/8/18.
 * 选择护理按钮后出现的护工列表界面
 */

public class HLFragment extends Fragment {

    private List<String> stringList = new ArrayList<>();
    private ListView listView2;//创建的性别的布局
    private ListView listView1;//创建的工作时长布局
    private MenuListAdapter mAdapter1;
    private MenuListAdapter mAdapter2;
    private DropDownMenu mDropDownMenu;


    private String headers[] = {"工作时长", "性别"};
    private String[] workTimes = {"8小时", "12小时", "24小时"};
    private String[] sexs = {"不限", "男", "女"};

    private List<View> popupViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hl, null);
        initView(view);
        initData();
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
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);
        //这是每个下拉菜单后的布局
        listView1 = new ListView(UIUtils.getContext());
        listView1 = new ListView(UIUtils.getContext());

        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);

        mAdapter1 = new MenuListAdapter(UIUtils.getContext(), Arrays.asList(workTimes));
        mAdapter2 = new MenuListAdapter(UIUtils.getContext(), Arrays.asList(sexs));

        listView1.setAdapter(mAdapter1);
        listView2.setAdapter(mAdapter2);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(workTimes[position]);
                mDropDownMenu.closeMenu();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(sexs[position]);
                mDropDownMenu.closeMenu();
            }
        });

        //添加内容显示区域
        //这里添加 内容显示区域,可以是任何布局
        TextView contentView = new TextView(UIUtils.getContext());
        contentView.setText("这里是内容区域");
        contentView.setTextSize(20);
        contentView.setGravity(Gravity.CENTER);


        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }
}
