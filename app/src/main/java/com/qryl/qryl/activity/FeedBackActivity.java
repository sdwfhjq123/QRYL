package com.qryl.qryl.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FeedBackActivity extends AppCompatActivity {

    private static final String TAG = "FeedBackActivity";

    private Button mSubmitButton;
    private EditText mInfoEdittext;

    private ProgressDialog dialog;

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        SharedPreferences prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        mUserId = prefs.getString("user_id", "");

        initView();
    }

    private void initView() {
        hiddenSomeView();
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mInfoEdittext = (EditText) findViewById(R.id.info_edittext);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                submitInfo();
            }
        });
    }

    private void submitInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("roleType", String.valueOf(-1));
        map.put("content", String.valueOf(mInfoEdittext.getText().toString()));
        map.put("userId", mUserId);
        HttpUtil.postAsyn(ConstantValue.URL + "/common/addAdvice", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String resultCode = jsonObject.getString("resultCode");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            if (resultCode.equals("500")) {
                                Toast.makeText(FeedBackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("200")) {
                                Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void showProgressDialog() {
        if (dialog != null) {
            dialog = new ProgressDialog(FeedBackActivity.this);
            dialog.setMessage("正在提交，请稍等");
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void hiddenSomeView() {
        TextView tvReturn = (TextView) findViewById(R.id.tv_return);
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvHelp = (TextView) findViewById(R.id.tv_help);
        LinearLayout llSetting = (LinearLayout) findViewById(R.id.ll_setting);
        tvReturn.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvHelp.setVisibility(View.GONE);
        llSetting.setVisibility(View.GONE);

        tvTitle.setText("意见反馈");
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
