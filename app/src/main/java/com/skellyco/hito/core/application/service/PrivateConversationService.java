package com.skellyco.hito.core.application.service;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.application.IPrivateConversationService;
import com.skellyco.hito.core.domain.IPrivateConversationRepository;
import com.skellyco.hito.core.entity.PrivateConversation;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;

public class PrivateConversationService implements IPrivateConversationService {

    private IPrivateConversationRepository privateConversationRepository;

    public PrivateConversationService(IPrivateConversationRepository privateConversationRepository)
    {
        this.privateConversationRepository = privateConversationRepository;
    }

    @Override
    public LiveData<Resource<PrivateConversation, FetchDataError>> getPrivateConversation(Activity activity, String firstInterlocutorId, String secondInterlocutorId)
    {
        return privateConversationRepository.getPrivateConversation(activity, firstInterlocutorId, secondInterlocutorId);
    }
}
