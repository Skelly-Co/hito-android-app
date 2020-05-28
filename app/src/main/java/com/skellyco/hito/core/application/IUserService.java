package com.skellyco.hito.core.application;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;

import java.util.List;

public interface IUserService {

    LiveData<Resource<User, FetchDataError>> getUser(Activity activity, String uid);

    LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(Activity activity, String uid);

}
