package com.skellyco.hito.viewmodel.main;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.dependency.DependencyProvider;

public class ChatViewModel extends ViewModel {

    private String loggedInUid;
    private String interlocutorUid;
    private IUserService userService;
    private LiveData<Resource<User, FetchDataError>> loggedInUser;
    private LiveData<Resource<User, FetchDataError>> interlocutorUser;

    public ChatViewModel()
    {
        userService = DependencyProvider.getUserService();
    }

    public void setUsers(String loggedInUid, String interlocutorUid)
    {
        this.loggedInUid = loggedInUid;
        this.interlocutorUid = interlocutorUid;
    }

    public void fetchData(Activity activity)
    {
        loggedInUser = userService.getUser(activity, loggedInUid);
        interlocutorUser = userService.getUser(activity, interlocutorUid);
    }

}
