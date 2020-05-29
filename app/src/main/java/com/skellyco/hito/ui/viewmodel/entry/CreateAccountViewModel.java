package com.skellyco.hito.ui.viewmodel.entry;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.model.dto.CreateAccountDTO;
import com.skellyco.hito.core.shared.error.CreateAccountError;
import com.skellyco.hito.dependency.DependencyProvider;

/**
 * ViewModel for the CreateAccountActivity.
 */
public class CreateAccountViewModel extends ViewModel {

    private IAuthenticationService authenticationService;

    public CreateAccountViewModel()
    {
        this.authenticationService = DependencyProvider.getAuthenticationService();
    }

    /**
     * Uses authentication to create an account.
     *
     * @param activity activity that requested creating an account.
     * @param createAccountDTO create account form.
     * @return LiveData with Resource that contains informations about operation status and error (if occurred).
     */
    public LiveData<Resource<Void, CreateAccountError>> createAccount(Activity activity, CreateAccountDTO createAccountDTO)
    {
        return authenticationService.createAccount(activity, createAccountDTO);
    }
}
