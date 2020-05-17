package com.skellyco.hito.core.application;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.entity.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.shared.error.ResetPasswordError;

public interface IAuthenticationService {

    LiveData<Resource<String, LoginError>> login(LoginDTO loginDTO);

    LiveData<Resource<Void, CreateAccountError>> createAccount(CreateAccountDTO createAccountDTO);

    LiveData<Resource<Void, ResetPasswordError>> resetPassword(ResetPasswordDTO resetPasswordDTO);
}
