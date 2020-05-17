package com.skellyco.hito.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.domain.Resource;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.error.CreateAccountError;
import com.skellyco.hito.dependency.DependencyProvider;

public class CreateAccountViewModel extends ViewModel {

    private IAuthenticationService authenticationService;

    public CreateAccountViewModel()
    {
        this.authenticationService = DependencyProvider.getAuthenticationService();
    }

    public LiveData<Resource<Void, CreateAccountError>> createAccount(CreateAccountDTO createAccountDTO)
    {
        return authenticationService.createAccount(createAccountDTO);
    }
}
