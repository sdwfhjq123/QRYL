package com.qryl.qryl.fragment.second.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.fragment.BaseFragment;
import com.qryl.qryl.fragment.FragmentFactory;
import com.qryl.qryl.util.UIUtils;

/**
 * Created by hp on 2017/8/21.
 * 巡诊等三个的点开后的fragment
 */

public class XzTnMyFragment extends Fragment {

    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;

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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//点击第一次的tab选项回调
                Toast.makeText(UIUtils.getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//上一次的tab回调

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {//再次点击同一个tab的回调
                Toast.makeText(UIUtils.getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
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

        public mTabLayoutAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
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
}
