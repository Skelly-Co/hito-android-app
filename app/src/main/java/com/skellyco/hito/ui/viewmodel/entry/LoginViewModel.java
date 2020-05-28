package com.skellyco.hito.ui.viewmodel.entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.model.dto.LoginDTO;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.dependency.DependencyProvider;

public class LoginViewModel extends ViewModel {

    private IAuthenticationService authenticationService;

    public LoginViewModel()
    {
        authenticationService = DependencyProvider.getAuthenticationService();
    }

    public LiveData<Resource<String, LoginError>> login(LoginDTO loginDTO)
    {
        return authenticationService.login(loginDTO);
    }
}
