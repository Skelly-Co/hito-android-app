package com.skellyco.hito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvCreateAccount;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews()
    {
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void initializeListeners()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateAccountActivity();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForgotPasswordActivity();
            }
        });
    }

    private void login()
    {

    }

    private void startCreateAccountActivity()
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    private void startForgotPasswordActivity()
    {

    }
}
