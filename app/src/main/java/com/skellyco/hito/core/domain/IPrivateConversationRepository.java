package com.skellyco.hito.core.domain;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.model.entity.PrivateConversation;
import com.skellyco.hito.core.model.dto.MessageDTO;
import com.skellyco.hito.core.model.dto.PrivateConversationDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.core.shared.error.InsertDataError;

public interface IPrivateConversationRepository {

    LiveData<Resource<Void, InsertDataError>> createPrivateConversation(Activity activity, PrivateConversationDTO privateConversationDTO, MessageDTO messageDTO);

    LiveData<Resource<Void, InsertDataError>> insertMessage(Activity activity, String privateConversationId, MessageDTO messageDTO);

    LiveData<Resource<PrivateConversation, FetchDataError>> getPrivateConversation(final Activity activity, final String firstInterlocutorId, final String secondInterlocutorId);
}
