package com.skellyco.hito.viewmodel.main;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IPrivateConversationService;
import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.entity.PrivateConversation;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.entity.dto.MessageDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.dependency.DependencyProvider;

public class ChatViewModel extends ViewModel {

    private String loggedInUid;
    private String interlocutorUid;
    private IUserService userService;
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

    public String getLoggedInUid()
    {
        return loggedInUid;
    }

    public void fetchInterlocutor(Activity activity)
    {
        interlocutorUser = userService.getUser(activity, interlocutorUid);
    }

    public LiveData<Resource<User, FetchDataError>> getInterlocutor()
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

    public void sendMessage(MessageDTO messageDTO)
    {

    }
}
