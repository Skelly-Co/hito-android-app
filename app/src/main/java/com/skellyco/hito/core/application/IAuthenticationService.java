package com.skellyco.hito.core.application;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.model.dto.CreateAccountDTO;
import com.skellyco.hito.core.model.dto.LoginDTO;
import com.skellyco.hito.core.model.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.shared.error.ResetPasswordError;

public interface IAuthenticationService {

    LiveData<Resource<String, LoginError>> login(Activity activity, LoginDTO loginDTO);

    LiveData<Resource<Void, CreateAccountError>> createAccount(Activity activity, CreateAccountDTO createAccountDTO);

    LiveData<Resource<Void, ResetPasswordError>> resetPassword(Activity activity, ResetPasswordDTO resetPasswordDTO);
}
