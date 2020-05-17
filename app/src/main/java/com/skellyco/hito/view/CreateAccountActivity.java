package com.skellyco.hito.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.view.util.AlertBuilder;
import com.skellyco.hito.view.util.LiveDataUtil;
import com.skellyco.hito.viewmodel.CreateAccountViewModel;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccountActivity";

    private ImageButton btnBack;
    private EditText etEmail;
    private TextView tvEmailError;
    private EditText etUsername;
    private TextView tvUsernameError;
    private EditText etPassword;
    private TextView tvPasswordError;
    private Button btnCreateAccount;
    private RelativeLayout relLoadingPanel;

    private CreateAccountViewModel createAccountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Slidr.attach(this);

        initializeViews();
        initializeListeners();
        initializeViewModel();
    }

    private void initializeViews()
    {
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        tvEmailError = findViewById(R.id.tvEmailError);
        etUsername = findViewById(R.id.etUsername);
        tvUsernameError = findViewById(R.id.tvUsernameError);
        etPassword = findViewById(R.id.etPassword);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        relLoadingPanel = findViewById(R.id.relLoadingPanel);
    }

    private void initializeListeners()
    {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                CreateAccountDTO createAccountDTO = new CreateAccountDTO(email, username, password);
                createAccount(createAccountDTO);
            }
        });
    }

    private void initializeViewModel()
    {
        createAccountViewModel = new ViewModelProvider(this).get(CreateAccountViewModel.class);
    }

    private void createAccount(CreateAccountDTO createAccountDTO)
    {
        clearErrors();
        showLoading();
        LiveData<Resource<Void, CreateAccountError>> createAccountResource = createAccountViewModel.createAccount(createAccountDTO);
        LiveDataUtil.observeOnce(createAccountResource, new Observer<Resource<Void, CreateAccountError>>() {
            @Override
            public void onChanged(Resource<Void, CreateAccountError> resource) {
                hideLoading();
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {

                }
                else
                {
                    displayError(resource.getError());
                }
            }
        });
    }

    private void showLoading()
    {
        relLoadingPanel.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideLoading()
    {
        relLoadingPanel.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void displayError(CreateAccountError error)
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
            case EMPTY_USERNAME:
            {
                displayUsernameError("Username is required.");
                break;
            }
            case EMPTY_PASSWORD:
            {
                displayPasswordError("Password is required.");
                break;
            }
            case WEAK_PASSWORD:
            {
                displayPasswordError("The entered password is too weak");
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
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) etUsername.getLayoutParams();
        params.setMargins(32, 32, 32, 0);
        etUsername.setLayoutParams(params);
        etEmail.setBackgroundResource(R.drawable.edit_text_error);
        tvEmailError.setText(errorMessage);
        tvEmailError.setVisibility(View.VISIBLE);
    }

    private void displayUsernameError(String errorMessage)
    {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) etPassword.getLayoutParams();
        params.setMargins(32, 32, 32, 0);
        etPassword.setLayoutParams(params);
        etUsername.setBackgroundResource(R.drawable.edit_text_error);
        tvUsernameError.setText(errorMessage);
        tvUsernameError.setVisibility(View.VISIBLE);
    }

    private void displayPasswordError(String errorMessage)
    {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) btnCreateAccount.getLayoutParams();
        params.setMargins(32,42,32,0);
        btnCreateAccount.setLayoutParams(params);
        etPassword.setBackgroundResource(R.drawable.edit_text_error);
        tvPasswordError.setText(errorMessage);
        tvPasswordError.setVisibility(View.VISIBLE);
    }

    private void displayGenericError(String errorMessage)
    {
        AlertDialog errorDialog = AlertBuilder.buildErrorDialog(this, errorMessage);
        errorDialog.show();
    }

    private void clearErrors()
    {
        ConstraintLayout.LayoutParams usernameParams = (ConstraintLayout.LayoutParams) etUsername.getLayoutParams();
        usernameParams.setMargins(32,72,32,0);
        etUsername.setLayoutParams(usernameParams);

        ConstraintLayout.LayoutParams passwordParams = (ConstraintLayout.LayoutParams) etPassword.getLayoutParams();
        passwordParams.setMargins(32,72,32,0);
        etPassword.setLayoutParams(passwordParams);

        ConstraintLayout.LayoutParams createAccountParams = (ConstraintLayout.LayoutParams) btnCreateAccount.getLayoutParams();
        createAccountParams.setMargins(32,72,32,0);
        btnCreateAccount.setLayoutParams(createAccountParams);

        etEmail.setBackgroundResource(R.drawable.edit_text);
        etUsername.setBackgroundResource(R.drawable.edit_text);
        etPassword.setBackgroundResource(R.drawable.edit_text);
        tvEmailError.setText("");
        tvEmailError.setVisibility(View.GONE);
        tvUsernameError.setText("");
        tvUsernameError.setVisibility(View.GONE);
        tvPasswordError.setText("");
        tvPasswordError.setVisibility(View.GONE);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }
}
