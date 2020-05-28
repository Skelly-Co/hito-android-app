package com.skellyco.hito.core.domain;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.model.dto.CreateAccountDTO;
import com.skellyco.hito.core.model.dto.LoginDTO;
import com.skellyco.hito.core.model.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.shared.error.ResetPasswordError;
import com.skellyco.hito.core.shared.Resource;

public interface IAuthenticationRepository {

    LiveData<Resource<String, LoginError>> login(LoginDTO loginDTO);

    LiveData<Resource<Void, CreateAccountError>> createAccount(CreateAccountDTO createAccountDTO);

    LiveData<Resource<Void, ResetPasswordError>> resetPassword(ResetPasswordDTO resetPasswordDTO);

}
