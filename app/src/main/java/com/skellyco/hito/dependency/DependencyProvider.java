package com.skellyco.hito.dependency;

import com.skellyco.hito.core.application.IAuthenticationService;
import com.skellyco.hito.core.application.IPrivateConversationService;
import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.application.service.AuthenticationService;
import com.skellyco.hito.core.application.service.PrivateConversationService;
import com.skellyco.hito.core.application.service.UserService;
import com.skellyco.hito.domain.firebase.AuthenticationRepository;
import com.skellyco.hito.domain.firebase.PrivateConversationRepository;
import com.skellyco.hito.domain.firebase.UserRepository;

/**
 * Class used for providing dependencies across our application.
 * This is just a temporary solution.
 * It should be replaced in the future by the proper implementation of Dagger 2.
 */
public class DependencyProvider {

    private static IAuthenticationService authenticationService;
    private static IUserService userService;
    private static IPrivateConversationService privateConversationService;

    /**
     * Returns the singleton instance of the AuthenticationService.
     *
     * @return singleton instance of AuthenticationService.
     */
    public static IAuthenticationService getAuthenticationService()
    {
        if(authenticationService == null)
        {
            authenticationService = new AuthenticationService(new AuthenticationRepository());
        }
        return authenticationService;
    }

    /**
     * Returns the singleton instance of the UserService.
     *
     * @return singleton instance of UserService.
     */
    public static IUserService getUserService()
    {
        if(userService == null)
        {
            userService = new UserService(new UserRepository());
        }
        return userService;
    }

    /**
     * Returns the singleton instance of the PrivateConversationService.
     *
     * @return singleton instance of PrivateConversationService.
     */
    public static IPrivateConversationService getPrivateConversationService()
    {
        if(privateConversationService == null)
        {
            privateConversationService = new PrivateConversationService(new PrivateConversationRepository());
        }
        return privateConversationService;
    }

}
