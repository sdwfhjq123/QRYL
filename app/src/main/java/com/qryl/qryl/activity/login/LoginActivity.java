package com.qryl.qryl.activity.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.BaseActivity;
import com.qryl.qryl.activity.MainActivity;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.view.PasswordToggleEditText;

import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private PasswordToggleEditText etPsd;//输入密码
    private AppCompatEditText etUser;//输入用户名
    private CheckBox cbAuto;
    private ProgressDialog progressDialog;
    private int id;
    private SharedPreferences prefs;
    private String registrationID;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //自动登录逻辑
        prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        if (prefs.getBoolean("is_auto_login", false)) {
            cbAuto.setChecked(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initView() {
        //隐藏标题部分控件
        hiddenView();
        etPsd = (PasswordToggleEditText) findViewById(R.id.et_psd);
        etUser = (AppCompatEditText) findViewById(R.id.et_user);
        cbAuto = (CheckBox) findViewById(R.id.cb_auto_login);
        TextView tvForgot = (TextView) findViewById(R.id.tv_forgot);
        Button btnRegister = (Button) findViewById(R.id.btn_register);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        //判断是否自动登录
        cbAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAuto.isChecked()) {
                    cbAuto.setChecked(true);
                    prefs.edit().putBoolean("is_auto_login", cbAuto.isChecked()).apply();
                } else {
                    cbAuto.setChecked(false);
                    prefs.edit().clear().apply();
                }

            }
        });
        //注册
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果点击自动登录但是用户名字为空
                if (cbAuto.isChecked() && TextUtils.isEmpty(etUser.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "自动登录无法保存您的账号", Toast.LENGTH_SHORT).show();
                } else if (cbAuto.isChecked() || !cbAuto.isChecked()) {
                    String psd = etPsd.getText().toString();
                    String user = etUser.getText().toString();
                    //注册极光唯一registrationId
                    registrationID = JPushInterface.getRegistrationID(LoginActivity.this);

                    if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(user)) {
                        postData(user, psd);
                    } else {
                        Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //忘记密码
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPsdActivity.class));
            }
        });

    }

    /**
     * 请求网络
     *
     * @param user 用户输入的登录的id
     * @param psd  用户输入的登录的密码
     */
    private void postData(String user, String psd) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在登录");
        progressDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("loginName", user);
        builder.add("password", psd);
        builder.add("registrationId", String.valueOf(registrationID));
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(ConstantValue.URL + "/patientUser/login")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    String resultCode = jsonObject.getString("resultCode");
                    if (resultCode.equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        id = data.getInt("loginId");
                        token = data.getString("token");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                                prefs.edit().putString("user_id", String.valueOf(id)).apply();
                                prefs.edit().putString("token", token).apply();
                                prefs.edit().putBoolean("is_force_offline", false).apply();
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    } else if (resultCode.equals("500")) {
                        final String erroMessage = jsonObject.getString("erroMessage");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(LoginActivity.this, erroMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void hiddenView() {
        TextView tvReturn = (TextView) findViewById(R.id.tv_return);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
        LinearLayout llSetting = (LinearLayout) findViewById(R.id.ll_setting);
        TextView tvHelp = (TextView) findViewById(R.id.tv_help);
        tvHelp.setVisibility(View.GONE);
        tvReturn.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);
        llSetting.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("用户登录");
    }
}
