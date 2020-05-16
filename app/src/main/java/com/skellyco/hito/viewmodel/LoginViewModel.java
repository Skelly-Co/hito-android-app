package com.skellyco.hito.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.domain.IAuthenticationRepository;
import com.skellyco.hito.core.domain.Resource;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.error.LoginError;
import com.skellyco.hito.model.RepositoryProvider;

public class LoginViewModel extends ViewModel {

    private IAuthenticationRepository authenticationRepository;

    public LoginViewModel()
    {
        authenticationRepository = RepositoryProvider.getAuthenticationRepository();
    }

    public LiveData<Resource<String, LoginError>> login(LoginDTO loginDTO)
    {
        return authenticationRepository.login(loginDTO);
    }
}
