package com.skellyco.hito.ui.view.util;

import com.skellyco.hito.core.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ListFilter {

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
