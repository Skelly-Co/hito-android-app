package com.skellyco.hito.dependency;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.application.service.AuthenticationService;
import com.skellyco.hito.model.firebase.AuthenticationRepository;

public class DependencyProvider {

    private static IAuthenticationService authenticationService;

    public static IAuthenticationService getAuthenticationService()
    {
        if(authenticationService == null)
        {
            authenticationService = new AuthenticationService(new AuthenticationRepository());
        }
        return authenticationService;
    }

}
