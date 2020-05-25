package com.skellyco.hito.viewmodel.main;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IPrivateConversationService;
import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.entity.PrivateConversation;
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
    private IPrivateConversationService privateConversationService;
    private LiveData<Resource<PrivateConversation, FetchDataError>> privateConversation;

    public ChatViewModel()
    {
        userService = DependencyProvider.getUserService();
        privateConversationService = DependencyProvider.getPrivateConversationService();
    }

    public void setUsers(String loggedInUid, String interlocutorUid)
    {
        this.loggedInUid = loggedInUid;
        this.interlocutorUid = interlocutorUid;
    }

    public void fetchUsers(Activity activity)
    {
        loggedInUser = userService.getUser(activity, loggedInUid);
        interlocutorUser = userService.getUser(activity, interlocutorUid);
    }

    public LiveData<Resource<User, FetchDataError>> getLoggedInUser()
    {
        return loggedInUser;
    }

    public LiveData<Resource<User, FetchDataError>> getInterlocutorUser()
    {
        return interlocutorUser;
    }

    public void fetchPrivateConversation(Activity activity)
    {
        privateConversation = privateConversationService.getPrivateConversation(activity, loggedInUid, interlocutorUid);
    }

    public LiveData<Resource<PrivateConversation, FetchDataError>> getPrivateConversation()
    {
        return privateConversation;
    }
}
