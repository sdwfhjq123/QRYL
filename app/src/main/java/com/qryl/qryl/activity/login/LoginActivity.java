package com.qryl.qryl.activity.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.BaseActivity;
import com.qryl.qryl.activity.MainActivity;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.view.PasswordToggleEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private RelativeLayout rlIsCheck;//相对布局包裹tv和cb
    private PasswordToggleEditText etPsd;//输入密码
    private AppCompatEditText etUser;//输入用户名
    private CheckBox cbAuto;
    private ProgressDialog progressDialog;
    private int id;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //自动登录逻辑
        prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");
        if (prefs.getBoolean("is_auto_login", false) == true) {
            Log.i(TAG, "onCreate: 得到的cb的值" + prefs.getBoolean("is_auto_login", false));
            cbAuto.setChecked(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (prefs.getBoolean("is_auto_login", false) == false) {
            Log.i(TAG, "onCreate: 得到的cb的值" + prefs.getBoolean("is_auto_login", false));
            return;
        }

    }

    private void initView() {
        //隐藏标题部分控件
        hiddenView();
        rlIsCheck = (RelativeLayout) findViewById(R.id.rl_isCheck);
        etPsd = (PasswordToggleEditText) findViewById(R.id.et_psd);
        etUser = (AppCompatEditText) findViewById(R.id.et_user);
        cbAuto = (CheckBox) findViewById(R.id.cb_auto_login);
        TextView tvForgot = (TextView) findViewById(R.id.tv_forgot);
        Button btnRegister = (Button) findViewById(R.id.btn_register);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        //判断是否登录
        cbAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAuto.isChecked()) {
                    cbAuto.setChecked(true);
                } else {
                    cbAuto.setChecked(false);
                }
                prefs.edit().putBoolean("is_auto_login", cbAuto.isChecked()).commit();
                Log.i(TAG, "保存checkbox状态:" + cbAuto.isChecked());
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
                String psd = etPsd.getText().toString();
                String user = etUser.getText().toString();
                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(user)) {
                    postData(user, psd);
                } else {
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
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
     * @param user
     * @param psd
     */
    private void postData(String user, String psd) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在登录");
        progressDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("loginName", user);
        builder.add("password", psd);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(ConstantValue.URL + "/patientUser/login")
                //.url("http://118.190.201.19/patientUser/login")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = response.body().string();
                try {
                    Log.i(TAG, "onResponse: " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    id = jsonObject.getInt("userId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                        prefs.edit().putString("user_id", String.valueOf(id)).commit();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        });
    }

    private void hiddenView() {
        View view = View.inflate(this, R.layout.title, null);
        TextView tvReturn = (TextView) view.findViewById(R.id.tv_return);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvLocation = (TextView) view.findViewById(R.id.tv_location);
        LinearLayout llSetting = (LinearLayout) view.findViewById(R.id.ll_setting);
        TextView tvHelp = (TextView) view.findViewById(R.id.tv_help);
        tvHelp.setVisibility(View.GONE);
        tvReturn.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);
        llSetting.setVisibility(View.GONE);
        tvTitle.setText("用户登录");
    }
}
