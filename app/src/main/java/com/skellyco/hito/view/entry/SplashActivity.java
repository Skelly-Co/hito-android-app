package com.skellyco.hito.view.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.skellyco.hito.R;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.view.main.MainActivity;
import com.skellyco.hito.view.util.LoginDataManager;
import com.skellyco.hito.viewmodel.entry.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViewModel();
        startApplication();
    }

    private void initializeViewModel()
    {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

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

    private void startLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    private void startMainActivity(String uid)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_UID, uid);
        startActivity(intent);
    }

}
