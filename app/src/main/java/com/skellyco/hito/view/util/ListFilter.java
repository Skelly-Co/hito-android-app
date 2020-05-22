package com.skellyco.hito.view.util;

import com.skellyco.hito.core.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ListFilter {

    public static List<User> filterUserList(List<User> userList, String filter)
    {
        List<User> filteredList = new ArrayList<>();
        for(User user : userList)
        {
            String username = user.getUsername();
            if(username.length() < filter.length())
            {
                if(filter.substring(0, username.length()).toLowerCase().equals(username.toLowerCase()))
                {
                    filteredList.add(user);
                }
            }
            else
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
