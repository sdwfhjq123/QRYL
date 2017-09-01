package com.qryl.qryl.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qryl.qryl.R;
import com.qryl.qryl.util.VerificationCountDownTimer;

public class RegisterActivity extends AppCompatActivity {

    private Button btnVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    private void initView() {
        btnVerification = (Button) findViewById(R.id.btn_verification_register);
        //实现倒计时并发送请求并获取验证码的功能
        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实现倒计时
                countDownTimer();
            }
        });
    }

    /**
     * 倒计时功能
     */
    private void countDownTimer() {
        VerificationCountDownTimer timer = new VerificationCountDownTimer(60000, 1000) {
            //倒计时过程
            @Override
            public void onTick(long millisUntilFinished) {
                super.onTick(millisUntilFinished);
                btnVerification.setClickable(false);//防止重新倒计时
                btnVerification.setText(millisUntilFinished / 1000 + "s");
            }

            //计时完毕的方法
            @Override
            public void onFinish() {
                //重新给Button设置文字
                btnVerification.setText("重新获取验证码");
                //设置可点击
                btnVerification.setClickable(true);
            }
        };
        timer.start();
    }

}
