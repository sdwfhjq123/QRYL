package com.qryl.qryl.fragment.one;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.Picture;
import com.qryl.qryl.adapter.RollPagerAdapter;
import com.qryl.qryl.fragment.one.two.HLFragment;
import com.qryl.qryl.fragment.one.two.MyFragment;
import com.qryl.qryl.fragment.one.two.TnFragment;
import com.qryl.qryl.fragment.one.two.XzFragment;
import com.qryl.qryl.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by hp on 2017/8/16.
 */

public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;

    private View view;
    private LinearLayout llHl, llXz, llTn, llMy;
    private LinearLayout llFourHome;
    private RollPagerView mRollPagerView;
    private List<Picture> mImgList = new ArrayList<>();
    private RollPagerAdapter rollPagerAdapter = new RollPagerAdapter(mImgList);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = UIUtils.inflate(R.layout.fragment_home);
        initUI();
        initData();
        initRPV();
        initFragment();

        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        postData();

    }

    /**
     * 获取网络图片
     */
    private void postData() {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("positionName", "shouye");
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("http://192.168.2.134:8080/qryl/common/getAdverListByPositionName")
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
                handldJson(result);
            }
        });
    }

    /**
     * 解析json
     *
     * @param result
     */
    private void handldJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jb = data.getJSONObject(i);
                //"id": 2,
                //"imgUrl": "new url img",
                //"positionId": 2,
                //"httpUrl": "new httpurl",
                //"note": "new note"
                int id = jb.getInt("id");
                int positionId = jb.getInt("positionId");
                String imgUrl = jb.getString("imgUrl");
                String httpUrl = jb.getString("httpUrl");
                String note = jb.getString("note");
                mImgList.add(new Picture(id, imgUrl, positionId, httpUrl, note));
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rollPagerAdapter.setData(mImgList);
                    rollPagerAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {

        llHl = (LinearLayout) view.findViewById(R.id.ll_hl);
        llXz = (LinearLayout) view.findViewById(R.id.ll_xz);
        llTn = (LinearLayout) view.findViewById(R.id.ll_tn);
        llMy = (LinearLayout) view.findViewById(R.id.ll_my);
        llFourHome = (LinearLayout) view.findViewById(R.id.ll_four_home);
        mRollPagerView = (RollPagerView) view.findViewById(R.id.rpv_home);
        llHl.setOnClickListener(this);
        llXz.setOnClickListener(this);
        llTn.setOnClickListener(this);
        llMy.setOnClickListener(this);
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
    }

    @Override
    public void onClick(View v) {
        ft = fm.beginTransaction();
        switch (v.getId()) {
            //护理
            case R.id.ll_hl:
                changeTitleName("护理");
                ft.replace(R.id.ll_home, new HLFragment());
                llFourHome.setVisibility(View.GONE);
                break;
            //巡诊
            case R.id.ll_xz:
                changeTitleName("巡诊");
                ft.replace(R.id.ll_home, new XzFragment());
                llFourHome.setVisibility(View.GONE);
                break;
            //推拿
            case R.id.ll_tn:
                changeTitleName("推拿");
                ft.replace(R.id.ll_home, new TnFragment());
                llFourHome.setVisibility(View.GONE);
                break;
            //母婴
            case R.id.ll_my:
                changeTitleName("母婴");
                ft.replace(R.id.ll_home, new MyFragment());
                llFourHome.setVisibility(View.GONE);
                break;
        }
        ft.commit();
    }

    /**
     * 点击每个条目时修改密码
     *
     * @param name
     */
    private void changeTitleName(String name) {
        TextView view = (TextView) getActivity().findViewById(R.id.tv_title);
        view.setText(name);
    }

    /**
     * 初始化rollPagerView方法
     */
    private void initRPV() {
        //设置播放时间间隔
        mRollPagerView.setPlayDelay(1500);
        //设置透明度
        mRollPagerView.setAnimationDurtion(500);
        //自定义指示器图片
        //mRollPagerView.setHintView(new IconHintView((this, R.drawable.)));
        //设置圆点指示器颜色
        mRollPagerView.setHintView(new ColorPointHintView(UIUtils.getContext(), Color.YELLOW, Color.WHITE));
        //设置文字指示器
        //mRollPagerView.setHintView(new TextHintView(UIUtils.getContext()));
        //隐藏指示器
        //mRollPagerView.setHintView(null);
        //设置适配器
        mRollPagerView.setAdapter(rollPagerAdapter);
        rollPagerAdapter.notifyDataSetChanged();
    }
}
