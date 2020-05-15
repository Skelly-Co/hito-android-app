package com.skellyco.hito.core.domain;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.entity.dto.ResetPasswordDTO;
import com.skellyco.hito.core.error.CreateAccountError;
import com.skellyco.hito.core.error.LoginError;
import com.skellyco.hito.core.error.ResetPasswordError;

public interface IAuthenticationRepository {

    LiveData<Resource<User, LoginError>> login(LoginDTO loginDTO);

    LiveData<Resource<Void, CreateAccountError>> createAccount(CreateAccountDTO createAccountDTO);

    LiveData<Resource<Void, ResetPasswordError>> resetPassword(ResetPasswordDTO resetPasswordDTO);

}
