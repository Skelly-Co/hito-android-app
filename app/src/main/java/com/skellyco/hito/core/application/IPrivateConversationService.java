package com.skellyco.hito.core.application;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.entity.PrivateConversation;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;

public interface IPrivateConversationService {

    LiveData<Resource<PrivateConversation, FetchDataError>> getPrivateConversation(final Activity activity, final String firstInterlocutorId, final String secondInterlocutorId);

}
