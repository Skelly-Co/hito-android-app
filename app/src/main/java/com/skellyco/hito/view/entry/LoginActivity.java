package com.skellyco.hito.view.entry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.skellyco.hito.R;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.util.security.Decryptor;
import com.skellyco.hito.core.util.security.EncryptionResult;
import com.skellyco.hito.core.util.security.Encryptor;
import com.skellyco.hito.view.main.MainActivity;
import com.skellyco.hito.view.util.AlertBuilder;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.view.util.LoginDataManager;
import com.skellyco.hito.view.util.ViewHelper;
import com.skellyco.hito.viewmodel.entry.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    public static final int CREATE_ACCOUNT_ACTIVITY = 1;
    public static final int FORGOT_PASSWORD_ACTIVITY = 2;

    private ScrollView scrMainContainer;
    private EditText etEmail;
    private TextView tvEmailError;
    private EditText etPassword;
    private TextView tvPasswordError;
    private Button btnLogin;
    private TextView tvCreateAccount;
    private TextView tvForgotPassword;
    private RelativeLayout relLoadingPanel;

    private LoginViewModel loginViewModel;
    private LoginDataManager loginDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        initializeListeners();
        initializeViewModel();
        initializeLoginDataManager();
    }

    private void initializeViews()
    {
        scrMainContainer = findViewById(R.id.scrMainContainer);
        etEmail = findViewById(R.id.etEmail);
        tvEmailError = findViewById(R.id.tvEmailError);
        etPassword = findViewById(R.id.etPassword);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        relLoadingPanel = findViewById(R.id.relLoadingPanel);
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

    private void initializeLoginDataManager()
    {
        loginDataManager = new LoginDataManager(this);
    }

    private void login(final LoginDTO loginDTO)
    {
        clearErrors();
        showLoading();
        LiveData<Resource<String, LoginError>> loginResource = loginViewModel.login(loginDTO);
        LiveDataUtil.observeOnce(loginResource, new Observer<Resource<String, LoginError>>() {
            @Override
            public void onChanged(Resource<String, LoginError> resource) {
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    loginDataManager.saveLoginData(loginDTO);
                    hideLoading();
                    startMainActivity();
                    finish();
                }
                else
                {
                    hideLoading();
                    displayError(resource.getError());
                }
            }
        });
    }

    private void showLoading()
    {
        relLoadingPanel.setVisibility(View.VISIBLE);
        ViewHelper.setViewGroupEnabled(scrMainContainer, false);
    }

    private void hideLoading()
    {
        relLoadingPanel.setVisibility(View.GONE);
        ViewHelper.setViewGroupEnabled(scrMainContainer, true);
    }

    private void displayError(LoginError error)
    {
        switch(error.getType())
        {
            case EMPTY_EMAIL:
            {
                displayEmailError("A valid email is required.");
                break;
            }
            case INVALID_EMAIL:
            {
                displayEmailError("The entered email is not valid.");
                break;
            }
            case EMPTY_PASSWORD:
            {
                displayPasswordError("Password is required.");
                break;
            }
            case USER_NOT_FOUND:
            {
                displayEmailError("The email address does not exist.");
                break;
            }
            case WRONG_PASSWORD:
            {
                displayPasswordError("Incorrect password.");
                break;
            }
            case TOO_MANY_ATTEMPTS:
            {
                displayPasswordError("Too many attempts - try again later.");
                break;
            }
            case NETWORK_ERROR:
            {
                displayGenericError("A network error has occurred. Please check your internet connection or try again later.");
                break;
            }
            case UNKNOWN:
            default:
            {
                displayGenericError("Something went wrong. Try again later or contact support.");
                break;
            }
        }
    }

    private void displayEmailError(String errorMessage)
    {
        LayoutParams params = (LayoutParams) etPassword.getLayoutParams();
        params.setMargins(32, 32, 32, 0);
        etPassword.setLayoutParams(params);
        etEmail.setBackgroundResource(R.drawable.edit_text_error);
        tvEmailError.setText(errorMessage);
        tvEmailError.setVisibility(View.VISIBLE);
    }

    private void displayPasswordError(String errorMessage)
    {
        LayoutParams params = (LayoutParams) btnLogin.getLayoutParams();
        params.setMargins(32,42,32,0);
        btnLogin.setLayoutParams(params);
        etPassword.setBackgroundResource(R.drawable.edit_text_error);
        tvPasswordError.setText(errorMessage);
        tvPasswordError.setVisibility(View.VISIBLE);
    }

    private void displayGenericError(String errorMessage)
    {
        AlertDialog errorDialog = AlertBuilder.buildInformationDialog(this, errorMessage);
        errorDialog.show();
    }

    private void clearErrors()
    {
        LayoutParams passwordParams = (LayoutParams) etPassword.getLayoutParams();
        passwordParams.setMargins(32,72,32,0);
        etPassword.setLayoutParams(passwordParams);

        LayoutParams loginParams = (LayoutParams) btnLogin.getLayoutParams();
        loginParams.setMargins(32,72,32,0);
        btnLogin.setLayoutParams(loginParams);

        etEmail.setBackgroundResource(R.drawable.edit_text);
        etPassword.setBackgroundResource(R.drawable.edit_text);
        tvEmailError.setText("");
        tvEmailError.setVisibility(View.GONE);
        tvPasswordError.setText("");
        tvPasswordError.setVisibility(View.GONE);

    }

    private void startCreateAccountActivity()
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivityForResult(intent, CREATE_ACCOUNT_ACTIVITY);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    private void startForgotPasswordActivity()
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivityForResult(intent, FORGOT_PASSWORD_ACTIVITY);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREATE_ACCOUNT_ACTIVITY)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String email = data.getStringExtra(CreateAccountActivity.EXTRA_EMAIL);
                String password = data.getStringExtra(CreateAccountActivity.EXTRA_PASSWORD);
                etEmail.setText(email);
                etPassword.setText(password);
                final LoginDTO loginDTO = new LoginDTO(email, password);
                //Logging in is slightly delayed here for a better user experience.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login(loginDTO);
                    }
                }, 300);
            }
        }
        if(requestCode == FORGOT_PASSWORD_ACTIVITY)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String email = data.getStringExtra(ForgotPasswordActivity.EXTRA_EMAIL);
                etEmail.setText(email);
            }
        }
    }
}
