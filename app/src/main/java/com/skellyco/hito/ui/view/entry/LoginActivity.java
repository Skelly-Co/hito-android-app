package com.skellyco.hito.ui.view.entry;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.skellyco.hito.R;
import com.skellyco.hito.core.model.dto.LoginDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.ui.view.main.MainActivity;
import com.skellyco.hito.ui.view.util.AlertBuilder;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.ui.view.util.LoginDataManager;
import com.skellyco.hito.ui.view.util.ViewHelper;
import com.skellyco.hito.ui.viewmodel.entry.LoginViewModel;

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

    /**
     * Initializes the views - assigns layout's elements to the instance variables.
     */
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

    /**
     * Initializes the listeners - specifies the actions that should happen during the interaction between
     * the user and the activity.
     */
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

    /**
     *  Initializes the ViewModel.
     */
    private void initializeViewModel()
    {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    /**
     * Initializes the LoginDataManager.
     */
    private void initializeLoginDataManager()
    {
        loginDataManager = new LoginDataManager(this);
    }

    /**
     * Uses LoginViewModel to perform the logging in using the passed credentials.
     * If logging in is successful it saves the login credentials using LoginDataManager
     * and starts the MainActivity. If logging in did not succeeded, it uses displayError
     * method to display the error.
     *
     * @param loginDTO login credentials.
     */
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
                    startMainActivity(resource.getData());
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

    /**
     * Shows the loading spinner.
     */
    private void showLoading()
    {
        relLoadingPanel.setVisibility(View.VISIBLE);
        ViewHelper.setViewGroupEnabled(scrMainContainer, false);
    }

    /**
     * Hides the loading spinner.
     */
    private void hideLoading()
    {
        relLoadingPanel.setVisibility(View.GONE);
        ViewHelper.setViewGroupEnabled(scrMainContainer, true);
    }

    /**
     * Checks the type of the error and depending on the error type defines the error message to
     * be displayed and invokes the proper method for displaying an error: displayEmailError,
     * displayUsernameError or displayGenericError.
     *
     * @param error error to display.
     */
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

    /**
     * Displays the email error, right below the email edit text.
     *
     * @param errorMessage error message to display.
     */
    private void displayEmailError(String errorMessage)
    {
        LayoutParams params = (LayoutParams) etPassword.getLayoutParams();
        params.setMargins(32, 32, 32, 0);
        etPassword.setLayoutParams(params);
        etEmail.setBackgroundResource(R.drawable.edit_text_error);
        tvEmailError.setText(errorMessage);
        tvEmailError.setVisibility(View.VISIBLE);
    }

    /**
     * Displays the password error, right below the password edit text.
     *
     * @param errorMessage error message to display.
     */
    private void displayPasswordError(String errorMessage)
    {
        LayoutParams params = (LayoutParams) btnLogin.getLayoutParams();
        params.setMargins(32,42,32,0);
        btnLogin.setLayoutParams(params);
        etPassword.setBackgroundResource(R.drawable.edit_text_error);
        tvPasswordError.setText(errorMessage);
        tvPasswordError.setVisibility(View.VISIBLE);
    }

    /**
     * Uses AlertBuilder class to create an error dialog and shows it with
     * the given error message.
     *
     * @param errorMessage error message to display.
     */
    private void displayGenericError(String errorMessage)
    {
        AlertDialog errorDialog = AlertBuilder.buildErrorDialog(this, errorMessage);
        errorDialog.show();
    }

    /**
     * Clears the all of the error messages and aligns the views properly.
     * Note - When error messages are being displayed the margins has to be different
     * than when they are hidden for the better visual experience.
     */
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

    /**
     * Starts the CreateAccountActivity.
     */
    private void startCreateAccountActivity()
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivityForResult(intent, CREATE_ACCOUNT_ACTIVITY);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    /**
     * Starts the ForgotPasswordActivity.
     */
    private void startForgotPasswordActivity()
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivityForResult(intent, FORGOT_PASSWORD_ACTIVITY);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    /**
     * Starts the MainActivity.
     *
     * @param uid logged in user id.
     */
    private void startMainActivity(String uid)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_LOGGED_IN_UID, uid);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    /**
     * Gets the activity result either from the CreateAccountActivity or ForgotPasswordActivity.
     *
     * If result comes from the CreateAccountActivity and the result code status is positive, it means
     * that account was successfully created, so it fills the username and password edit texts (for better user experience)
     * and invokes the login method passing the given login data.
     *
     * If result comes from the ForgotPassword activity and the result code status is positive, it means
     * that user successfully sent the reset password form, so it fills the email edit text and prepares
     * the user for logging in after he successfully resets his password.
     *
     * @param requestCode specifies the activity that sent the result.
     * @param resultCode specifies the result's status.
     * @param data data passed as a result.
     */
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
                //Invoking the login method is slightly delayed here for a better user experience.
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
