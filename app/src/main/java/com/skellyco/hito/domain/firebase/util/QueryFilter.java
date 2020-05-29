package com.skellyco.hito.domain.firebase.util;

import com.skellyco.hito.core.model.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for filtering queries. Since firebase has very poor querying system,
 * we have to use this helper class for additional filtering that we cannot achieve
 * by simply querying the firestore directly.
 */
public class QueryFilter {

    /**
     * Filters the list of all users and returns the list of local users.
     *
     * Note - Since we haven't implemented local users functionality yet in our system, this method
     * for right now returns the list of all users except the logged in user.
     *
     * @param loggedInUid logged in user id.
     * @param allUsers list of all users.
     * @return filtered local users.
     */
    public static List<User> filterLocalUsers(String loggedInUid, List<User> allUsers)
    {
        List<User> filteredList = new ArrayList<>();
        for(User user : allUsers)
        {
            if(!user.getUid().equals(loggedInUid))
            {
                filteredList.add(user);
            }
        }
        return filteredList;
    }
}
