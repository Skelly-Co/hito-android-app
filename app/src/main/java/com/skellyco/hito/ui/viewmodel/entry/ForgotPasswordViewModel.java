package com.skellyco.hito.ui.viewmodel.entry;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.model.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.ResetPasswordError;
import com.skellyco.hito.dependency.DependencyProvider;

public class ForgotPasswordViewModel extends ViewModel {

    private IAuthenticationService authenticationService;

    public ForgotPasswordViewModel()
    {
        this.authenticationService = DependencyProvider.getAuthenticationService();
    }

    public LiveData<Resource<Void, ResetPasswordError>> resetPassword(Activity activity, ResetPasswordDTO resetPasswordDTO)
    {
        return authenticationService.resetPassword(activity, resetPasswordDTO);
    }
}
