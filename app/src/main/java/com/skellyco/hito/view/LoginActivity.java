package com.skellyco.hito.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.skellyco.hito.R;
import com.skellyco.hito.entity.dto.LoginDTO;
import com.skellyco.hito.model.AuthenticationRepository;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etEmail;
    private EditText etPassword;
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
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void initializeListeners()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                LoginDTO loginDTO = new LoginDTO(email, password);
                login(loginDTO);
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

    private void login(LoginDTO loginDTO)
    {
        AuthenticationRepository repo = new AuthenticationRepository();
        repo.login(loginDTO);
    }

    private void startCreateAccountActivity()
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    private void startForgotPasswordActivity()
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
