package com.skellyco.hito.viewmodel;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.domain.IAuthenticationRepository;
import com.skellyco.hito.core.domain.Resource;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.error.CreateAccountError;
import com.skellyco.hito.model.RepositoryProvider;

public class CreateAccountViewModel {

    private IAuthenticationRepository authenticationRepository;

    public CreateAccountViewModel()
    {
        this.authenticationRepository = RepositoryProvider.getAuthenticationRepository();
    }

    public LiveData<Resource<Void, CreateAccountError>> createAccount(CreateAccountDTO createAccountDTO)
    {
        return authenticationRepository.createAccount(createAccountDTO);
    }
}
