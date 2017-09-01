package com.qryl.qryl.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.qryl.qryl.R;
import com.qryl.qryl.view.PasswordToggleEditText;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rlIsCheck;//相对布局包裹tv和cb
    private PasswordToggleEditText etPsd;//输入密码
    private AppCompatEditText etUser;//输入用户名
    private CheckBox cbAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        rlIsCheck = (RelativeLayout) findViewById(R.id.rl_isCheck);
        etPsd = (PasswordToggleEditText) findViewById(R.id.et_psd_login);
        etUser = (AppCompatEditText) findViewById(R.id.et_user_login);
        cbAuto = (CheckBox) findViewById(R.id.cb_auto_login);
        rlIsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = cbAuto.isChecked();
                //根据checkBox的状态修改样式
                if (checked) {
                    cbAuto.setChecked(false);
                } else {
                    cbAuto.setChecked(true);
                }
            }
        });
    }
}
