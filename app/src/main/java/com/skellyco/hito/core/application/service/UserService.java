package com.skellyco.hito.core.application.service;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.domain.IUserRepository;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;

import java.util.List;

public class UserService implements IUserService {

    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public LiveData<Resource<User, FetchDataError>> getUser(Activity activity, String uid) {
        return userRepository.getUser(activity, uid);
    }

    @Override
    public LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(Activity activity, String uid)
    {
        return userRepository.getLocalUsers(activity, uid);
    }
}
