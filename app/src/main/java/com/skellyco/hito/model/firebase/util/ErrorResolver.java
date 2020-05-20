package com.skellyco.hito.model.firebase.util;

import android.util.Log;

import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.shared.error.ResetPasswordError;

public class ErrorResolver {

    public static final String TAG = "ErrorResolver";

    public static LoginError resolveLoginError(String errorMessage)
    {
        LoginError loginError = new LoginError();
        switch(errorMessage)
        {
            case "The password is invalid or the user does not have a password.":
            {
                loginError.setType(LoginError.Type.WRONG_PASSWORD);
                break;
            }
            case "There is no user record corresponding to this identifier. The user may have been deleted.":
            {
                loginError.setType(LoginError.Type.USER_NOT_FOUND);
                break;
            }
            case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
            {
                loginError.setType(LoginError.Type.NETWORK_ERROR);
                break;
            }
            case "We have blocked all requests from this device due to unusual activity. Try again later. [ Too many unsuccessful login attempts. Please try again later. ]":
            {
                loginError.setType(LoginError.Type.TOO_MANY_ATTEMPTS);
                break;
            }
            default:
            {
                Log.e(TAG, errorMessage);
                loginError.setType(LoginError.Type.UNKNOWN);
                break;
            }
        }
        return loginError;
    }

    public static CreateAccountError resolveCreateAccountError(String errorMessage)
    {
        CreateAccountError createAccountError = new CreateAccountError();
        switch(errorMessage)
        {
            case "The email address is already in use by another account.":
            {
                createAccountError.setType(CreateAccountError.Type.EMAIL_IN_USE);
                break;
            }
            case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
            {
                createAccountError.setType(CreateAccountError.Type.NETWORK_ERROR);
                break;
            }
            default:
            {
                Log.e(TAG, errorMessage);
                createAccountError.setType(CreateAccountError.Type.UNKNOWN);
                break;
            }
        }
        return createAccountError;
    }

    public static ResetPasswordError resolveResetPasswordError(String errorMessage)
    {
        ResetPasswordError resetPasswordError = new ResetPasswordError();
        switch (errorMessage)
        {
            case "There is no user record corresponding to this identifier. The user may have been deleted.":
            {
                resetPasswordError.setType(ResetPasswordError.Type.EMAIL_NOT_FOUND);
                break;
            }
            case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
            {
                resetPasswordError.setType(ResetPasswordError.Type.NETWORK_ERROR);
                break;
            }
            default:
            {
                Log.e(TAG, errorMessage);
                resetPasswordError.setType(ResetPasswordError.Type.UNKNOWN);
                break;
            }
        }
        return resetPasswordError;
    }

}
