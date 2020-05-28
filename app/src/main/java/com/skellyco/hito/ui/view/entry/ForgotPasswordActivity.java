package com.skellyco.hito.ui.view.entry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;
import com.skellyco.hito.core.model.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.ResetPasswordError;
import com.skellyco.hito.ui.view.util.AlertBuilder;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.ui.view.util.ViewHelper;
import com.skellyco.hito.ui.viewmodel.entry.ForgotPasswordViewModel;

/**
 * Role of this activity is to allow the user to reset the password to his account.
 */
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

    /**
     * Initializes the views - assigns layout's elements to the instance variables.
     */
    private void initializeViews()
    {
        scrMainContainer = findViewById(R.id.scrMainContainer);
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        tvEmailError = findViewById(R.id.tvEmailError);
        btnConfirm = findViewById(R.id.btnConfirm);
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

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(email);
                resetPassword(resetPasswordDTO);
            }
        });
    }

    /**
     *  Initializes the ViewModel.
     */
    private void initializeViewModel()
    {
        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
    }

    /**
     * Uses the ForgotPasswordViewModel to reset the users password.
     * If resetting password was successful it invokes the displaySuccessInformation method and closes the activity.
     * If resetting password was unsuccessful it invokes the displayError method.
     *
     * Since resetting the password is a one-time call we are using LiveDataUtil observeOnce method
     * to automatically stop the observation after first invocation of the onChanged method.
     *
     * @param resetPasswordDTO reset password form.
     */
    private void resetPassword(final ResetPasswordDTO resetPasswordDTO)
    {
        clearErrors();
        showLoading();
        LiveData<Resource<Void, ResetPasswordError>> resetPasswordResource = forgotPasswordViewModel.resetPassword(this, resetPasswordDTO);
        LiveDataUtil.observeOnce(resetPasswordResource, new Observer<Resource<Void, ResetPasswordError>>() {
            @Override
            public void onChanged(Resource<Void, ResetPasswordError> resource) {
                hideLoading();
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    displaySuccessInformation(resetPasswordDTO);
                    ////Closing Activity in is slightly delayed here for a better user experience.
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeActivity(resetPasswordDTO);
                        }
                    }, 500);
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
     * be displayed and invokes the proper method for displaying an error: displayEmailError or
     * displayGenericError.
     *
     * @param error error to display.
     */
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

    /**
     * Displays the email error, right below the email edit text.
     *
     * @param errorMessage error message to display.
     */
    private void displayEmailError(String errorMessage)
    {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) btnConfirm.getLayoutParams();
        params.setMargins(32, 32, 32, 72);
        btnConfirm.setLayoutParams(params);
        etEmail.setBackgroundResource(R.drawable.edit_text_error);
        tvEmailError.setText(errorMessage);
        tvEmailError.setVisibility(View.VISIBLE);
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
     * Displays the toast to inform the user that instructions on how to reset the password were sent
     * to his email address.
     *
     * @param resetPasswordDTO reset password form.
     */
    private void displaySuccessInformation(ResetPasswordDTO resetPasswordDTO)
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                HtmlCompat.fromHtml("We sent instructions to change your password to " + "<b>" + resetPasswordDTO.getEmail() + "</b>. " +
                        "Please check both your inbox and spam folder.", HtmlCompat.FROM_HTML_MODE_LEGACY),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 200);
        toast.show();
    }

    /**
     * Clears the all of the error messages and aligns the views properly.
     * Note - When error messages are being displayed the margins has to be different
     * than when they are hidden for the better visual experience.
     */
    private void clearErrors()
    {
        ConstraintLayout.LayoutParams createAccountParams = (ConstraintLayout.LayoutParams) btnConfirm.getLayoutParams();
        createAccountParams.setMargins(32,72,32,72);
        btnConfirm.setLayoutParams(createAccountParams);

        etEmail.setBackgroundResource(R.drawable.edit_text);
        tvEmailError.setText("");
        tvEmailError.setVisibility(View.GONE);
    }

    /**
     * Finishes the activity and puts the data of reset password from in the intent.
     *
     * @param resetPasswordDTO reset password form.
     */
    private void closeActivity(ResetPasswordDTO resetPasswordDTO)
    {
        Intent userDataIntent = new Intent();
        userDataIntent.putExtra(EXTRA_EMAIL, resetPasswordDTO.getEmail());
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
