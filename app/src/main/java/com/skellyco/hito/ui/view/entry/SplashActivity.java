package com.skellyco.hito.ui.view.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.skellyco.hito.R;
import com.skellyco.hito.core.model.dto.LoginDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.ui.view.main.MainActivity;
import com.skellyco.hito.ui.view.util.LoginDataManager;
import com.skellyco.hito.ui.viewmodel.entry.SplashViewModel;

/**
 * Splash Activity that is shown at the start of the application.
 * The role of this Activity is to show the splash screen during the application startup
 * and to redirect the user either to the LoginActivity or MainActivity.
 */
public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViewModel();
        startApplication();
    }

    /**
     * Initializes the ViewModel.
     */
    private void initializeViewModel()
    {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    /**
     * Gets the information about saved login data from LoginDataManager and checks whether
     * it is null or not.
     *
     * If saved login data is null it means that there is no information in the system about
     * the saved logged in user so it starts the LoginActivity. If saved login data exists
     * it invokes the login method to perform the logging in using saved user credentials.
     */
    private void startApplication()
    {
        LoginDataManager loginDataManager = new LoginDataManager(this);
        LoginDTO loginDTO = loginDataManager.getSavedLoginData();
        if(loginDTO == null)
        {
            startLoginActivity();
            finish();
        }
        else
        {
            login(loginDTO);
        }
    }

    /**
     * Uses SplashViewModel to perform the logging in using the passed credentials.
     * If logging in is successful it starts the MainActivity. If logging in did not
     * succeeded for any reason, it starts the LoginActivity.
     *
     * @param loginDTO login credentials.
     */
    private void login(LoginDTO loginDTO)
    {
        final LiveData<Resource<String, LoginError>> loginResource = splashViewModel.login(loginDTO);
        LiveDataUtil.observeOnce(loginResource, new Observer<Resource<String, LoginError>>() {
            @Override
            public void onChanged(Resource<String, LoginError> resource) {
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    startMainActivity(resource.getData());
                }
                else
                {
                    startLoginActivity();
                }
                finish();
            }
        });
    }

    /**
     * Starts the LoginActivity.
     */
    private void startLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    /**
     * Starts the MainActivity.
     *
     * @param loggedInUid logged in user id.
     */
    private void startMainActivity(String loggedInUid)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_LOGGED_IN_UID, loggedInUid);
        startActivity(intent);
    }

}
