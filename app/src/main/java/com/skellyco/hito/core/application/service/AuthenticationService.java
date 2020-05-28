package com.skellyco.hito.core.application.service;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.application.service.validator.DTOValidator;
import com.skellyco.hito.core.application.service.validator.ValidationResult;
import com.skellyco.hito.core.domain.IAuthenticationRepository;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.model.dto.CreateAccountDTO;
import com.skellyco.hito.core.model.dto.LoginDTO;
import com.skellyco.hito.core.model.dto.ResetPasswordDTO;
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
    public LiveData<Resource<String, LoginError>> login(Activity activity, LoginDTO loginDTO) {
        ValidationResult<LoginError> validationResult = DTOValidator.validateLoginDTO(loginDTO);
        if(validationResult.isValid())
        {
            return authenticationRepository.login(activity, loginDTO);
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
    public LiveData<Resource<Void, CreateAccountError>> createAccount(Activity activity, CreateAccountDTO createAccountDTO) {
        ValidationResult<CreateAccountError> validationResult = DTOValidator.validateCreateAccountDTO(createAccountDTO);
        if(validationResult.isValid())
        {
            return authenticationRepository.createAccount(activity, createAccountDTO);
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
    public LiveData<Resource<Void, ResetPasswordError>> resetPassword(Activity activity, ResetPasswordDTO resetPasswordDTO) {
        ValidationResult<ResetPasswordError> validationResult = DTOValidator.validateResetPasswordDTO(resetPasswordDTO);
        if(validationResult.isValid())
        {
            return authenticationRepository.resetPassword(activity, resetPasswordDTO);
        }
        else
        {
            ResetPasswordError error = validationResult.getError();
            MutableLiveData<Resource<Void, ResetPasswordError>> resetPasswordLiveData = new MutableLiveData<>();
            Resource<Void, ResetPasswordError> resetPasswordResource = new Resource<>(Resource.Status.ERROR, null, error);
            resetPasswordLiveData.setValue(resetPasswordResource);
            return resetPasswordLiveData;
        }
    }
}
