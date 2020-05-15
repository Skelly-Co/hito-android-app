package com.skellyco.hito.model.firebase.dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.skellyco.hito.core.entity.dto.CreateAccountDTO;
import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.entity.dto.ResetPasswordDTO;

public class AuthenticationDAO {

    public static final String TAG = "AuthenticationDAO";

    private FirebaseAuth firebaseAuth;

    public AuthenticationDAO()
    {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> login(LoginDTO loginDTO)
    {
        return firebaseAuth.signInWithEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
    }

    public Task<AuthResult> createAccount(CreateAccountDTO createAccountDTO)
    {
        return firebaseAuth.createUserWithEmailAndPassword(createAccountDTO.getEmail(), createAccountDTO.getPassword());
    }

    public Task<Void> resetPassword(ResetPasswordDTO resetPasswordDTO)
    {
        return firebaseAuth.sendPasswordResetEmail(resetPasswordDTO.getEmail());
    }
}
