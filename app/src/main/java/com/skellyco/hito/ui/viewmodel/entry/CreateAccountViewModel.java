package com.skellyco.hito.ui.viewmodel.entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.model.dto.CreateAccountDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
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
