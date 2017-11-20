package com.qryl.qryl.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qryl.qryl.R;
import com.qryl.qryl.fragment.one.HomeFragment;
import com.qryl.qryl.fragment.one.MeFragment;
import com.qryl.qryl.fragment.one.MsgFragment;
import com.qryl.qryl.fragment.one.OrderFragment;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    private static final String ME_FRAGMENT = "ME_FRAGMENT";
    private static final String MSG_FRAGMENT = "MSG_FRAGMENT";
    private static final String ORDER_FRAGMENT = "ORDER_FRAGMENT";

    private TextView tvTitle;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;
    private TextView tvLocation;
    private LinearLayout llSetting;
    private TextView tvHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        setTitleName("亲仁医疗护理");
        ft.replace(R.id.fl_home, new HomeFragment(), HOME_FRAGMENT);
        //ft.addToBackStack(null);//模拟栈
        ft.commit();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        RadioButton rbHome = (RadioButton) findViewById(R.id.rb_home);
        RadioButton rbOrder = (RadioButton) findViewById(R.id.rb_order);
        RadioButton rbMsg = (RadioButton) findViewById(R.id.rb_msg);
        RadioButton rbMe = (RadioButton) findViewById(R.id.rb_me);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        llSetting = (LinearLayout) findViewById(R.id.ll_setting);
        tvHelp = (TextView) findViewById(R.id.tv_help);
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
                llSetting.setVisibility(View.GONE);
                tvHelp.setVisibility(View.VISIBLE);
                tvLocation.setVisibility(View.VISIBLE);
                ft.replace(R.id.fl_home, new HomeFragment(), HOME_FRAGMENT);
                break;
            //定单
            case R.id.rb_order:
                setTitleName("订单");
                llSetting.setVisibility(View.GONE);
                tvHelp.setVisibility(View.VISIBLE);
                tvLocation.setVisibility(View.GONE);
                ft.replace(R.id.fl_home, new OrderFragment(), ORDER_FRAGMENT);
                break;
            //消息
            case R.id.rb_msg:
                setTitleName("消息");
                llSetting.setVisibility(View.GONE);
                tvHelp.setVisibility(View.VISIBLE);
                tvLocation.setVisibility(View.GONE);
                ft.replace(R.id.fl_home, new MsgFragment(), MSG_FRAGMENT);
                break;
            //我的
            case R.id.rb_me:
                setTitleName("我的");
                llSetting.setVisibility(View.VISIBLE);
                tvHelp.setVisibility(View.GONE);
                tvLocation.setVisibility(View.GONE);
//                llSetting.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(MainActivity.this, MeSettingActivity.class));
//                    }
//                });
                ft.replace(R.id.fl_home, new MeFragment(), ME_FRAGMENT);
                break;
        }

//        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * 点击最下面四个按钮式切换标题的名字
     *
     * @param name 需要传入修改的name
     */
    private void setTitleName(String name) {
        tvTitle.setText(name);
    }

}
