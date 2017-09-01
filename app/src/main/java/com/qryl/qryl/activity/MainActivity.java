package com.qryl.qryl.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.qryl.qryl.R;
import com.qryl.qryl.adapter.RollPagerAdapter;
import com.qryl.qryl.fragment.HomeFragment;
import com.qryl.qryl.fragment.MeFragment;
import com.qryl.qryl.fragment.MsgFragment;
import com.qryl.qryl.fragment.OrderFragment;
import com.qryl.qryl.util.UIUtils;

import java.util.ArrayList;

/**
 * 以及样式还未实现
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    private static final String ME_FRAGMENT = "ME_FRAGMENT";
    private static final String MSG_FRAGMENT = "MSG_FRAGMENT";
    private static final String ORDER_FRAGMENT = "ORDER_FRAGMENT";


    private RadioGroup rgMain;
    private TextView tvTitleMain;
    private RadioButton rbHome, rbOrder, rbMsg, rbMe;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        float density = getResources().getDisplayMetrics().density;
//        Log.e(TAG, "像素密度: " + density);
        initUI();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initFragment();

    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fl_home, new HomeFragment(), HOME_FRAGMENT);
        ft.commit();
    }

    /**
     * 初始化UI
     */
    private void initUI() {

        rgMain = (RadioGroup) findViewById(R.id.rg_main);
        rbHome = (RadioButton) findViewById(R.id.rb_home);
        rbOrder = (RadioButton) findViewById(R.id.rb_order);
        rbMsg = (RadioButton) findViewById(R.id.rb_msg);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        tvTitleMain = (TextView) findViewById(R.id.tv_title_main);
        rbHome.setOnClickListener(this);
        rbOrder.setOnClickListener(this);
        rbMsg.setOnClickListener(this);
        rbMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ft = fm.beginTransaction();
        switch (v.getId()) {
            //首页
            case R.id.rb_home:
                setTitleName("亲仁医疗护理");
                ft.replace(R.id.fl_home, new HomeFragment(), HOME_FRAGMENT);
                break;
            //定单
            case R.id.rb_order:
                setTitleName("订单");
                ft.replace(R.id.fl_home, new OrderFragment(), ORDER_FRAGMENT);
                break;
            //消息
            case R.id.rb_msg:
                setTitleName("消息");
                ft.replace(R.id.fl_home, new MsgFragment(), MSG_FRAGMENT);
                break;
            //我的
            case R.id.rb_me:
                setTitleName("我的");
                ft.replace(R.id.fl_home, new MeFragment(), ME_FRAGMENT);
                break;
        }
        ft.commit();
    }


    /**
     * 点击最下面四个按钮式切换标题的名字
     *
     * @param name 需要传入修改的name
     */
    private void setTitleName(String name) {
        tvTitleMain.setText(name);
    }


}
