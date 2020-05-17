package com.skellyco.hito.core.application.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.application.service.validator.DTOValidator;
import com.skellyco.hito.core.application.service.validator.ValidationResult;
import com.skellyco.hito.core.domain.IAuthenticationRepository;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.entity.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.shared.error.ResetPasswordError;

public class AuthenticationService implements IAuthenticationService {

    private IAuthenticationRepository authenticationRepository;

    public AuthenticationService(IAuthenticationRepository authenticationRepository)
    {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public LiveData<Resource<String, LoginError>> login(LoginDTO loginDTO) {
        ValidationResult<LoginError> validationResult = DTOValidator.validateLoginDTO(loginDTO);
        if(validationResult.isValid())
        {
            return authenticationRepository.login(loginDTO);
        }
        else
        {
            LoginError error = validationResult.getError();
            MutableLiveData<Resource<String, LoginError>> loginLiveData = new MutableLiveData<>();
            Resource<String, LoginError> loginResource = new Resource<>(Resource.Status.ERROR, null, error);
            loginLiveData.setValue(loginResource);
            return loginLiveData;
        }
    }

    @Override
    public LiveData<Resource<Void, CreateAccountError>> createAccount(CreateAccountDTO createAccountDTO) {
        ValidationResult<CreateAccountError> validationResult = DTOValidator.validateCreateAccountDTO(createAccountDTO);
        if(validationResult.isValid())
        {
            //return authenticationRepository.createAccount(createAccountDTO);
            return null;
        }
        else
        {
            CreateAccountError error = validationResult.getError();
            MutableLiveData<Resource<Void, CreateAccountError>> createAccountLiveData = new MutableLiveData<>();
            Resource<Void, CreateAccountError> createAccountResource = new Resource<>(Resource.Status.ERROR, null, error);
            createAccountLiveData.setValue(createAccountResource);
            return createAccountLiveData;
        }
    }

    @Override
    public LiveData<Resource<Void, ResetPasswordError>> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        return authenticationRepository.resetPassword(resetPasswordDTO);
    }
}
