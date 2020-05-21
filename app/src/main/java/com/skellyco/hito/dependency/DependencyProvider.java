package com.skellyco.hito.dependency;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.application.service.AuthenticationService;
import com.skellyco.hito.core.application.service.UserService;
import com.skellyco.hito.model.firebase.AuthenticationRepository;
import com.skellyco.hito.model.firebase.UserRepository;

public class DependencyProvider {

    private static IAuthenticationService authenticationService;
    private static IUserService userService;

    public static IAuthenticationService getAuthenticationService()
    {
        if(authenticationService == null)
        {
            authenticationService = new AuthenticationService(new AuthenticationRepository());
        }
        return authenticationService;
    }

    public static IUserService getUserService()
    {
        if(userService == null)
        {
            userService = new UserService(new UserRepository());
        }
        return userService;
    }

}
