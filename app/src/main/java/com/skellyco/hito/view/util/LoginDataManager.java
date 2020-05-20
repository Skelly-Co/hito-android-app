package com.skellyco.hito.view.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.skellyco.hito.core.entity.dto.LoginDTO;
import com.skellyco.hito.core.util.security.Decryptor;
import com.skellyco.hito.core.util.security.EncryptionResult;
import com.skellyco.hito.core.util.security.Encryptor;

public class LoginDataManager {

    public static final String TAG = "LoginDataMgr";
    private static final String passwordKeyAlias = "UserPasswordKey";

    private static final String PREF_NAME = "LOGIN_PREF";
    private static final String EMAIL_PREF = "Email";
    private static final String PASSWORD_PREF = "Password";
    private static final String IV_PREF = "PREF";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public LoginDataManager(Context ctx)
    {
        preferences = ctx.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveLoginData(LoginDTO loginDTO)
    {
        try
        {
            String email = loginDTO.getEmail();
            EncryptionResult passwordEncryptionResult = Encryptor.encryptData(passwordKeyAlias, loginDTO.getPassword());
            String encryptedPassword = Base64.encodeToString(passwordEncryptionResult.getEncryptedData(), Base64.DEFAULT);
            String encryptionIv = Base64.encodeToString(passwordEncryptionResult.getIv(), Base64.DEFAULT);
            editor.putString(EMAIL_PREF, email);
            editor.putString(PASSWORD_PREF, encryptedPassword);
            editor.putString(IV_PREF, encryptionIv);
            editor.commit();
        }
        catch(Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    public LoginDTO getSavedLoginData()
    {
        try
        {
            String email = preferences.getString(EMAIL_PREF, null);
            String encryptedPassword = preferences.getString(PASSWORD_PREF, null);
            String encryptionIv = preferences.getString(IV_PREF, null);
            if(email == null || encryptedPassword == null || encryptionIv == null)
            {
                return null;
            }
            String decryptedPassword = Decryptor.decryptData(passwordKeyAlias,
                    Base64.decode(encryptedPassword, Base64.DEFAULT),
                    Base64.decode(encryptionIv, Base64.DEFAULT));
            return new LoginDTO(email, decryptedPassword);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public void clearSavedLoginData()
    {
        editor.remove(EMAIL_PREF);
        editor.remove(PASSWORD_PREF);
        editor.remove(IV_PREF);
        editor.commit();
    }
}
