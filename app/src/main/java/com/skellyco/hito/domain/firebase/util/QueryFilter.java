package com.skellyco.hito.domain.firebase.util;

import com.skellyco.hito.core.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class QueryFilter {

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
