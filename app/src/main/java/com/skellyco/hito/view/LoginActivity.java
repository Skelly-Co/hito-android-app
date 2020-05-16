package com.skellyco.hito.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skellyco.hito.R;
import com.skellyco.hito.core.domain.Resource;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.error.LoginError;
import com.skellyco.hito.view.util.LiveDataUtil;
import com.skellyco.hito.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private Button btnLogin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvCreateAccount;
    private TextView tvForgotPassword;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        initializeListeners();
        initializeViewModel();
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

    private void initializeViewModel()
    {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void login(LoginDTO loginDTO)
    {
        btnLogin.setEnabled(false);
        final LiveData<Resource<String, LoginError>> loginResource = loginViewModel.login(loginDTO);
        LiveDataUtil.observeOnce(loginResource, new Observer<Resource<String, LoginError>>() {
            @Override
            public void onChanged(Resource<String, LoginError> resource) {
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    Log.e(TAG, resource.getData());
                    startMainActivity();
                    finish();
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),resource.getError().getType().toString(),Toast. LENGTH_SHORT);
                    toast. setMargin(50,50);
                    toast. show();
                    Log.e(TAG, "onChanged called!");
                    btnLogin.setEnabled(true);
                }
            }
        });
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

    private void startMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
