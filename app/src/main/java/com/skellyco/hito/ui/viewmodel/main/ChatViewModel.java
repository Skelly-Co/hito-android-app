package com.skellyco.hito.ui.viewmodel.main;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IPrivateConversationService;
import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.model.entity.PrivateConversation;
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.model.dto.MessageDTO;
import com.skellyco.hito.core.model.dto.PrivateConversationDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.core.shared.error.InsertDataError;
import com.skellyco.hito.dependency.DependencyProvider;

/**
 * ViewModel for ChatActivity
 */
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

    /**
     * Sets the informations about users which is required for data fetching.
     *
     * @param loggedInUid logged in user id.
     * @param interlocutorUid interlocutor id.
     */
    public void setUsers(String loggedInUid, String interlocutorUid)
    {
        this.loggedInUid = loggedInUid;
        this.interlocutorUid = interlocutorUid;
    }

    /**
     * Returns logged in user id.
     * @return logged in user id.
     */
    public String getLoggedInUid()
    {
        return loggedInUid;
    }

    /**
     * Uses the UserService to fetch the interlocutor.
     *
     * @param activity activity that invokes the method.
     */
    public void fetchInterlocutor(Activity activity)
    {
        interlocutorUser = userService.getUser(activity, interlocutorUid);
    }

    /**
     * Returns LiveData with Resource that contains information about about the status of the call,
     * interlocutor and error (if it occurred)
     *
     * @return interlocutor LiveData
     */
    public LiveData<Resource<User, FetchDataError>> getInterlocutor()
    {
        return interlocutorUser;
    }

    /**
     * Uses the PrivateConversationService to fetch the PrivateConversation.
     *
     * @param activity activity that invokes the method.
     */
    public void fetchPrivateConversation(Activity activity)
    {
        privateConversation = privateConversationService.getPrivateConversation(activity, loggedInUid, interlocutorUid);
    }

    /**
     * Returns LiveData with Resource that contains information about the status of the call,
     * PrivateConversation and error (if it occurred)
     *
     * @return private conversation LiveData.
     */
    public LiveData<Resource<PrivateConversation, FetchDataError>> getPrivateConversation()
    {
        return privateConversation;
    }

    /**
     * Uses PrivateConversationService to send the message.
     * If PrivateConversation between logged in user and interlocutor does not exist,
     * uses PrivateConversationService to create a conversation (with message attached to the call)
     *
     * @param activity activity that invokes the method.
     * @param messageDTO message form.
     * @return LiveData with Resource that contains informations about operation status and error (if occurred).
     */
    public LiveData<Resource<Void, InsertDataError>> sendMessage(Activity activity, MessageDTO messageDTO)
    {
        if(privateConversation.getValue().getData() == null)
        {
            PrivateConversationDTO privateConversationDTO = new PrivateConversationDTO(loggedInUid, interlocutorUid);
            return privateConversationService.createPrivateConversation(activity, privateConversationDTO, messageDTO);
        }
        else
        {
            String privateConversationId = privateConversation.getValue().getData().getId();
            return privateConversationService.insertMessage(activity, privateConversationId, messageDTO);
        }
    }
}
