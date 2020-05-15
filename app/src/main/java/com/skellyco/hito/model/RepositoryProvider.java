package com.skellyco.hito.model;

import com.skellyco.hito.core.domain.IAuthenticationRepository;
import com.skellyco.hito.model.firebase.AuthenticationRepository;

public class RepositoryProvider {

    private static IAuthenticationRepository authenticationRepository;

    public static IAuthenticationRepository getAuthenticationRepository()
    {
        if(authenticationRepository == null)
        {
            authenticationRepository = new AuthenticationRepository();
        }
        return authenticationRepository;
    }
}
