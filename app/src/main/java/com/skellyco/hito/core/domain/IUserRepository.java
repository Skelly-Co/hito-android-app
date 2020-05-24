package com.skellyco.hito.core.domain;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;

import java.util.List;

public interface IUserRepository {

    LiveData<Resource<User, FetchDataError>> getUser(Activity activity, String uid);

    LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(Activity activity, String uid);
}
