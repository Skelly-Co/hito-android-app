package com.skellyco.hito.domain.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.skellyco.hito.core.domain.IAuthenticationRepository;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.model.dto.CreateAccountDTO;
import com.skellyco.hito.core.model.dto.LoginDTO;
import com.skellyco.hito.core.model.dto.ResetPasswordDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.core.shared.error.LoginError;
import com.skellyco.hito.core.shared.error.ResetPasswordError;
import com.skellyco.hito.domain.firebase.dao.AuthenticationDAO;
import com.skellyco.hito.domain.firebase.dao.UserDAO;
import com.skellyco.hito.domain.firebase.util.ErrorResolver;

public class AuthenticationRepository implements IAuthenticationRepository {

    public static final String TAG = "AuthRepository";

    private AuthenticationDAO authenticationDAO;
    private UserDAO userDAO;

    public AuthenticationRepository()
    {
        authenticationDAO = new AuthenticationDAO();
        userDAO = new UserDAO();
    }

    @Override
    public LiveData<Resource<String, LoginError>> login(Activity activity, LoginDTO loginDTO)
    {
        final MutableLiveData<Resource<String, LoginError>> loginResource = new MutableLiveData<>();
        authenticationDAO.login(loginDTO).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String uid = task.getResult().getUser().getUid();
                    Resource<String, LoginError> resource = new Resource<>(Resource.Status.SUCCESS, uid, null);
                    loginResource.setValue(resource);
                }
                else
                {
                    String errorMessage = task.getException().getMessage();
                    LoginError error = ErrorResolver.resolveLoginError(errorMessage);
                    Resource<String, LoginError> resource = new Resource<>(Resource.Status.ERROR, null, error);
                    loginResource.setValue(resource);
                }
            }
        });
        return loginResource;
    }

    @Override
    public LiveData<Resource<Void, CreateAccountError>> createAccount(Activity activity, final CreateAccountDTO createAccountDTO)
    {
        final MutableLiveData<Resource<Void, CreateAccountError>> createAccountResource = new MutableLiveData<>();
        authenticationDAO.createAccount(createAccountDTO).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    final String uid = task.getResult().getUser().getUid();
                    User user = new User(uid, createAccountDTO.getEmail(), createAccountDTO.getUsername());
                    userDAO.createUser(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Resource<Void, CreateAccountError> resource = new Resource<>(Resource.Status.SUCCESS, null, null);
                                createAccountResource.setValue(resource);
                            }
                            else
                            {
                                Log.e(TAG, task.getException().getMessage());
                                CreateAccountError error = new CreateAccountError(CreateAccountError.Type.UNKNOWN);
                                Resource<Void, CreateAccountError> resource = new Resource<>(Resource.Status.ERROR, null, error);
                                createAccountResource.setValue(resource);
                            }
                        }
                    });
                }
                else
                {
                    String errorMessage = task.getException().getMessage();
                    CreateAccountError createAccountError = ErrorResolver.resolveCreateAccountError(errorMessage);
                    Resource<Void, CreateAccountError> resource = new Resource<>(Resource.Status.ERROR, null, createAccountError);
                    createAccountResource.setValue(resource);
                }
            }
        });
        return createAccountResource;
    }

    @Override
    public LiveData<Resource<Void, ResetPasswordError>> resetPassword(Activity activity, ResetPasswordDTO resetPasswordDTO)
    {
        final MutableLiveData<Resource<Void, ResetPasswordError>> resetPasswordResource = new MutableLiveData<>();
        authenticationDAO.resetPassword(resetPasswordDTO).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Resource<Void, ResetPasswordError> resource = new Resource<>(Resource.Status.SUCCESS, null, null);
                    resetPasswordResource.setValue(resource);
                }
                else
                {
                    String errorMessage = task.getException().getMessage();
                    ResetPasswordError resetPasswordError = ErrorResolver.resolveResetPasswordError(errorMessage);
                    Resource<Void, ResetPasswordError> resource = new Resource<>(Resource.Status.ERROR, null, resetPasswordError);
                    resetPasswordResource.setValue(resource);
                }
            }
        });
        return resetPasswordResource;
    }

}
