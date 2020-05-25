package com.skellyco.hito.dependency;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.application.IPrivateConversationService;
import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.application.service.AuthenticationService;
import com.skellyco.hito.core.application.service.PrivateConversationService;
import com.skellyco.hito.core.application.service.UserService;
import com.skellyco.hito.model.firebase.AuthenticationRepository;
import com.skellyco.hito.model.firebase.PrivateConversationRepository;
import com.skellyco.hito.model.firebase.UserRepository;

public class DependencyProvider {

    private static IAuthenticationService authenticationService;
    private static IUserService userService;
    private static IPrivateConversationService privateConversationService;

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

    public static IPrivateConversationService getPrivateConversationService()
    {
        if(privateConversationService == null)
        {
            privateConversationService = new PrivateConversationService(new PrivateConversationRepository());
        }
        return privateConversationService;
    }

}
