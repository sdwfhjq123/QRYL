package com.qryl.qryl.fragment.one.two;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.qryl.qryl.R;
import com.qryl.qryl.util.UIUtils;
import java.lang.reflect.Field;

/**
 * Created by hp on 2017/8/21.
 * 巡诊等三个的点开后的fragment
 */

public class MyFragment extends Fragment {

    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xztnmy, null);
        initUI();
        initData();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mTabLayoutAdapter adapter = new mTabLayoutAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        //绑定适配器
        tabLayout.setupWithViewPager(viewPager);
        //给viewpager设置点击监听事件,绑定tablayout
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //设置下划线宽度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 50, 50);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//点击第一次的tab选项回调
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//上一次的tab回调
                //Toast.makeText(UIUtils.getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {//再次点击同一个tab的回调
                //Toast.makeText(UIUtils.getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 初始化UI
     */
    private void initUI() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
    }

    class mTabLayoutAdapter extends FragmentPagerAdapter {

        private final String[] mTabNames;

        mTabLayoutAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }

    }

    /**
     * 利用反射设置tablayout下划线的大小
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    private void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
