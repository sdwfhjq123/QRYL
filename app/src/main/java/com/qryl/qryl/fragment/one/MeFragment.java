package com.qryl.qryl.fragment.one;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qryl.qryl.R;
import com.qryl.qryl.util.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hp on 2017/8/16.
 */

public class MeFragment extends android.support.v4.app.Fragment {

    private LinearLayout llSetting;
    private TextView tvHelp;
    private int id;
    private TextView tvInfo;
    private ImageView imageView;
    private TextView tvId;
    private TextView tvName;
    private TextView tvGender;
    private TextView tvYbh;
    private TextView tvTel;
    private TextView tvLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = UIUtils.inflate(R.layout.fragment_me);
        Intent intent = getActivity().getIntent();
        id = intent.getIntExtra("id", 0);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        postData();
    }

    private void postData() {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id", String.valueOf(id));
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url("http://192.168.2.134:8080/qryl/patientUser/getById")
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
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            int id = data.getInt("id");
            final String userName = data.getString("userName");
            final String realName = data.getString("realName");
            int age = data.getInt("age");
            final int gender = data.getInt("gender");
            String idNum = data.getString("idNum");
            final String healthCareNum = data.getString("healthCareNum");
            int height = data.getInt("height");
            int weight = data.getInt("weight");
            final String mobile = data.getString("mobile");
            int provinceId = data.getInt("provinceId");
            int cityId = data.getInt("cityId");
            int districtId = data.getInt("districtId");
            final String provinceName = data.getString("provinceName");
            final String cityName = data.getString("cityName");
            final String districtName = data.getString("districtName");
            final String headshotImg = data.getString("headshotImg");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvInfo.setText(realName);
                    Glide.with(getActivity()).load(headshotImg).into(imageView);
                    tvId.setText(userName);
                    tvName.setText(realName);
                    tvGender.setText(gender == 0 ? "男" : "女");
                    tvYbh.setText(healthCareNum);
                    tvTel.setText(mobile);
                    tvLocation.setText(provinceName + cityName + districtName);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化控件
     *
     * @param view 绑定的layout
     */
    private void initView(View view) {
        //修改标头
        View viewTitle = View.inflate(UIUtils.getContext(), R.layout.title, null);
        llSetting = (LinearLayout) viewTitle.findViewById(R.id.ll_setting);
        tvHelp = (TextView) viewTitle.findViewById(R.id.tv_help);
        llSetting.setVisibility(View.VISIBLE);
        tvHelp.setVisibility(View.GONE);
        //需要刷新的列表
        tvInfo = (TextView) view.findViewById(R.id.tv_info);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        LinearLayout llCompile = (LinearLayout) view.findViewById(R.id.ll_compile);
        tvId = (TextView) view.findViewById(R.id.tv_id);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvGender = (TextView) view.findViewById(R.id.tv_gender);
        tvYbh = (TextView) view.findViewById(R.id.tv_ybh);
        tvTel = (TextView) view.findViewById(R.id.tv_tel);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);

    }
}
