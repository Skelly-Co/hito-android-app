package com.skellyco.hito.ui.view.entry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;
import com.skellyco.hito.core.model.dto.CreateAccountDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.ui.view.util.AlertBuilder;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.ui.view.util.ViewHelper;
import com.skellyco.hito.ui.viewmodel.entry.CreateAccountViewModel;

/**
 * Role of this activity is to allow the user to create an account.
 */
public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccountActivity";
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";


    private ScrollView scrMainContainer;
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

    /**
     * Initializes the views - assigns layout's elements to the instance variables.
     */
    private void initializeViews()
    {
        scrMainContainer = findViewById(R.id.scrMainContainer);
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

    /**
     * Initializes the listeners - specifies the actions that should happen during the interaction between
     * the user and the activity.
     */
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

    /**
     *  Initializes the ViewModel.
     */
    private void initializeViewModel()
    {
        createAccountViewModel = new ViewModelProvider(this).get(CreateAccountViewModel.class);
    }

    /**
     * Uses the CreateAccountViewModel to create an account.
     * If creating an account was successful it closes the activity.
     * If creating an account was unsuccessful it invokes the displayError method.
     *
     * Since creating an account is a one-time call we are using LiveDataUtil observeOnce method
     * to automatically stop the observation after first invocation of the onChanged method.
     *
     * @param createAccountDTO create account form.
     */
    private void createAccount(final CreateAccountDTO createAccountDTO)
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
                    closeActivity(createAccountDTO);
                }
                else
                {
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
     * displayUsernameError, displayPasswordError or displayGenericError.
     *
     * @param error error to display.
     */
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
            case INVALID_USERNAME:
            {
                displayUsernameError("The entered username is not valid.");
                break;
            }
            case EMPTY_PASSWORD:
            {
                displayPasswordError("Password is required.");
                break;
            }
            case WEAK_PASSWORD:
            {
                displayPasswordError("The entered password is too weak.");
                break;
            }
            case EMAIL_IN_USE:
            {
                displayEmailError("The entered email is already in use.");
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
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) etUsername.getLayoutParams();
        params.setMargins(32, 32, 32, 0);
        etUsername.setLayoutParams(params);
        etEmail.setBackgroundResource(R.drawable.edit_text_error);
        tvEmailError.setText(errorMessage);
        tvEmailError.setVisibility(View.VISIBLE);
    }

    /**
     * Displays the username error, right below the username edit text.
     *
     * @param errorMessage error message to display.
     */
    private void displayUsernameError(String errorMessage)
    {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) etPassword.getLayoutParams();
        params.setMargins(32, 32, 32, 0);
        etPassword.setLayoutParams(params);
        etUsername.setBackgroundResource(R.drawable.edit_text_error);
        tvUsernameError.setText(errorMessage);
        tvUsernameError.setVisibility(View.VISIBLE);
    }

    /**
     * Displays the password error, right below the password edit text.
     *
     * @param errorMessage error message to display.
     */
    private void displayPasswordError(String errorMessage)
    {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) btnCreateAccount.getLayoutParams();
        params.setMargins(32,42,32,72);
        btnCreateAccount.setLayoutParams(params);
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
        ConstraintLayout.LayoutParams usernameParams = (ConstraintLayout.LayoutParams) etUsername.getLayoutParams();
        usernameParams.setMargins(32,72,32,0);
        etUsername.setLayoutParams(usernameParams);

        ConstraintLayout.LayoutParams passwordParams = (ConstraintLayout.LayoutParams) etPassword.getLayoutParams();
        passwordParams.setMargins(32,72,32,0);
        etPassword.setLayoutParams(passwordParams);

        ConstraintLayout.LayoutParams createAccountParams = (ConstraintLayout.LayoutParams) btnCreateAccount.getLayoutParams();
        createAccountParams.setMargins(32,72,32,72);
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

    /**
     * Finishes the activity and puts the data of the created user in the intent.
     *
     * @param createAccountDTO create account form.
     */
    private void closeActivity(CreateAccountDTO createAccountDTO)
    {
        Intent userDataIntent = new Intent();
        userDataIntent.putExtra(EXTRA_EMAIL, createAccountDTO.getEmail());
        userDataIntent.putExtra(EXTRA_PASSWORD, createAccountDTO.getPassword());
        setResult(Activity.RESULT_OK, userDataIntent);
        finish();
    }

    /**
     * Overrides the finish method to add the sliding animation on finish.
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }
}
