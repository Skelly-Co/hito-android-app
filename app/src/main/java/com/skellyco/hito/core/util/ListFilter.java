package com.skellyco.hito.core.util;

import com.skellyco.hito.core.model.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Role of this class is to provide methods for filtering list of the entities
 * defined in our application.
 */
public class ListFilter {

    /**
     * Filters the given list using the given filter.
     * It filters users by their username.
     *
     * @param userList list to filter.
     * @param filter filter to apply.
     * @return filtered list.
     */
    public static List<User> filterUserList(List<User> userList, String filter)
    {
        List<User> filteredList = new ArrayList<>();
        if(filter == null || filter.isEmpty())
        {
            filteredList.addAll(userList);
            return filteredList;
        }
        for(User user : userList)
        {
            String username = user.getUsername();
            if(username.length() >= filter.length())
            {
                if(username.substring(0, filter.length()).toLowerCase().equals(filter.toLowerCase()))
                {
                    filteredList.add(user);
                }
            }
        }
        return filteredList;
    }

}
