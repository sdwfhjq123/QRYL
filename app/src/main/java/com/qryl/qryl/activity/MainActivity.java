package com.qryl.qryl.activity;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qryl.qryl.R;
import com.qryl.qryl.fragment.one.HomeFragment;
import com.qryl.qryl.fragment.one.MeFragment;
import com.qryl.qryl.fragment.one.MsgFragment;
import com.qryl.qryl.fragment.one.OrderFragment;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;

/**
 * 以及样式还未实现
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    private static final String ME_FRAGMENT = "ME_FRAGMENT";
    private static final String MSG_FRAGMENT = "MSG_FRAGMENT";
    private static final String ORDER_FRAGMENT = "ORDER_FRAGMENT";

    /**
     * 微信应用的appKey
     */
    private static final String APP_ID_WX = "";
    /**
     * IWXAPI是第三方app和微信通信的openapi接口
     */
    private IWXAPI api;

    private RadioGroup rgMain;
    private TextView tvTitle;
    private RadioButton rbHome, rbOrder, rbMsg, rbMe;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;
    private TextView tvLocation;
    private LinearLayout llSetting;
    private TextView tvHelp, tvReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register2WX();
        //注册极光唯一registrationId
        String registrationID = JPushInterface.getRegistrationID(this);
        Log.i(TAG, "[MyReceiver] 接收Registration Id2 : " + registrationID);
//        float density = getResources().getDisplayMetrics().density;
//        Log.e(TAG, "像素密度: " + density);
        initUI();
        initData();
    }

    /**
     * 注册到微信
     */
    private void register2WX() {
        //通过WXAPIFactroy工厂，获取IWXAPI实例
        api = WXAPIFactory.createWXAPI(this, APP_ID_WX, true);
        api.registerApp(APP_ID_WX);
        //初始化一个微信WXTextObject对象
        WXTextObject textObject = new WXTextObject();
        textObject.text = "测试数据";
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = "测试数据";
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());//transaction用于唯一标识一个请求
        req.message = msg;
        //调用api接口发送数据到微信
        api.sendReq(req);
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
        //ft.addToBackStack(null);
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
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        llSetting = (LinearLayout) findViewById(R.id.ll_setting);
        tvHelp = (TextView) findViewById(R.id.tv_help);
        tvReturn = (TextView) findViewById(R.id.tv_return);
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
