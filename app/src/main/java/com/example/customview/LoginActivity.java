package com.example.customview;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.customview.App;
import com.example.customview.customview.loginpage.LoginPageView;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final LoginPageView loginPageView = this.findViewById(R.id.login_page_view);
        loginPageView.setOnLoginPageActionListener(new LoginPageView.OnLoginPageActionListener() {
            @Override
            public void onGetVerifyCode(String phoneNumber) {
                //todo 去获取验证码
                Toast.makeText(LoginActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onOpenAgreement() {
                //todo 打开一个协议页面
            }

            @Override
            public void onConfimr(String verifyCode, String phoneNumber) {
                //todo 登录
                App.getmHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        loginPageView.onVerifyCoedError();
                    }
                },4000);
            }
        });
    }
}
