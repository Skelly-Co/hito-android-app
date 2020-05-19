package com.skellyco.hito.view.entry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;
import com.skellyco.hito.core.entity.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.ResetPasswordError;
import com.skellyco.hito.view.util.AlertBuilder;
import com.skellyco.hito.view.util.LiveDataUtil;
import com.skellyco.hito.view.util.ViewHelper;
import com.skellyco.hito.viewmodel.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    public static final String TAG = "ForgotPasswordActivity";
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    private ScrollView scrMainContainer;
    private ImageButton btnBack;
    private EditText etEmail;
    private TextView tvEmailError;
    private Button btnConfirm;
    private RelativeLayout relLoadingPanel;

    private ForgotPasswordViewModel forgotPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Slidr.attach(this);

        initializeViews();
        initializeListeners();
        initializeViewModel();
    }

    private void initializeViews()
    {
        scrMainContainer = findViewById(R.id.scrMainContainer);
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        tvEmailError = findViewById(R.id.tvEmailError);
        btnConfirm = findViewById(R.id.btnConfirm);
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

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(email);
                resetPassword(resetPasswordDTO);
            }
        });
    }

    private void initializeViewModel()
    {
        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
    }

    private void resetPassword(final ResetPasswordDTO resetPasswordDTO)
    {
        clearErrors();
        showLoading();
        LiveData<Resource<Void, ResetPasswordError>> resetPasswordResource = forgotPasswordViewModel.resetPassword(resetPasswordDTO);
        LiveDataUtil.observeOnce(resetPasswordResource, new Observer<Resource<Void, ResetPasswordError>>() {
            @Override
            public void onChanged(Resource<Void, ResetPasswordError> resource) {
                hideLoading();
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    displaySuccessInformation();
                    ////Closing Activity in is slightly delayed here for a better user experience.
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goBackToLoginActivity(resetPasswordDTO);
                        }
                    }, 300);
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
        ViewHelper.setViewGroupEnabled(scrMainContainer, false);
    }

    private void hideLoading()
    {
        relLoadingPanel.setVisibility(View.GONE);
        ViewHelper.setViewGroupEnabled(scrMainContainer, true);
    }

    private void displayError(ResetPasswordError error)
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
            case EMAIL_NOT_FOUND:
            {
                displayEmailError("The email address does not exist.");
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
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) btnConfirm.getLayoutParams();
        params.setMargins(32, 32, 32, 72);
        btnConfirm.setLayoutParams(params);
        etEmail.setBackgroundResource(R.drawable.edit_text_error);
        tvEmailError.setText(errorMessage);
        tvEmailError.setVisibility(View.VISIBLE);
    }

    private void displayGenericError(String errorMessage)
    {
        AlertDialog errorDialog = AlertBuilder.buildInformationDialog(this, errorMessage);
        errorDialog.show();
    }

    private void displaySuccessInformation()
    {

    }

    private void goBackToLoginActivity(ResetPasswordDTO resetPasswordDTO)
    {
        Intent userDataIntent = new Intent();
        userDataIntent.putExtra(EXTRA_EMAIL, resetPasswordDTO.getEmail());
        setResult(Activity.RESULT_OK, userDataIntent);
        finish();
    }

    private void clearErrors()
    {
        ConstraintLayout.LayoutParams createAccountParams = (ConstraintLayout.LayoutParams) btnConfirm.getLayoutParams();
        createAccountParams.setMargins(32,72,32,72);
        btnConfirm.setLayoutParams(createAccountParams);

        etEmail.setBackgroundResource(R.drawable.edit_text);
        tvEmailError.setText("");
        tvEmailError.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }
}
