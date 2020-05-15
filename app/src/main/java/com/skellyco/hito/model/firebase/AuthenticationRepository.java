package com.skellyco.hito.model.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.skellyco.hito.core.domain.IAuthenticationRepository;
import com.skellyco.hito.core.domain.Resource;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.entity.dto.ResetPasswordDTO;
import com.skellyco.hito.core.error.CreateAccountError;
import com.skellyco.hito.core.error.LoginError;
import com.skellyco.hito.core.error.ResetPasswordError;
import com.skellyco.hito.model.firebase.dao.AuthenticationDAO;
import com.skellyco.hito.model.firebase.dao.UserDAO;

public class AuthenticationRepository implements IAuthenticationRepository {

    public static final String TAG = "AuthRepository";

    private AuthenticationDAO authenticationDAO;
    private UserDAO userDAO;
    private MutableLiveData<Resource<User, LoginError>> loggedInUser;

    public AuthenticationRepository()
    {
        authenticationDAO = new AuthenticationDAO();
        userDAO = new UserDAO();
        loggedInUser = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Resource<User, LoginError>> login(LoginDTO loginDTO)
    {
        authenticationDAO.login(loginDTO).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String loggedInUserUid = task.getResult().getUser().getUid();
                    fetchAndObserveUser(loggedInUserUid);
                }
                else
                {
                    //handle error - authentication failed
                    Log.e(TAG, task.getException().toString());
                }
            }
        });
        return loggedInUser;
    }

    @Override
    public MutableLiveData<Resource<Void, CreateAccountError>> createAccount(final CreateAccountDTO createAccountDTO)
    {
        final MutableLiveData<Resource<Void, CreateAccountError>> createAccountResource = new MutableLiveData<>();
        authenticationDAO.createAccount(createAccountDTO).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                //handle error - cannot create user
                                Log.e(TAG, task.getException().toString());
                            }
                        }
                    });
                }
                else
                {
                    //handle error - cant create auth user
                    Log.e(TAG, task.getException().toString());
                }
            }
        });
        return createAccountResource;
    }

    @Override
    public MutableLiveData<Resource<Void, ResetPasswordError>> resetPassword(ResetPasswordDTO resetPasswordDTO)
    {
        final MutableLiveData<Resource<Void, ResetPasswordError>> resetPasswordResource = new MutableLiveData<>();
        authenticationDAO.resetPassword(resetPasswordDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Resource<Void, ResetPasswordError> resource = new Resource<>(Resource.Status.SUCCESS, null, null);
                    resetPasswordResource.setValue(resource);
                }
                else
                {
                    //handle error
                    Log.e(TAG, task.getException().toString());
                }
            }
        });
        return resetPasswordResource;
    }

    private void fetchAndObserveUser(String uid)
    {
        userDAO.getUser(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    User user = documentSnapshot.toObject(User.class);
                    Resource<User, LoginError> resource = new Resource<>(Resource.Status.SUCCESS, user, null);
                    loggedInUser.setValue(resource);
                }
                else
                {
                    //handle error - user not found
                    Log.e(TAG, "User not found");
                }
            }
        });
    }

}
