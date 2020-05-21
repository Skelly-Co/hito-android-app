package com.skellyco.hito.viewmodel.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.dependency.DependencyProvider;

import java.util.List;

public class MainViewModel extends ViewModel {

    private IUserService userService;

    public MainViewModel()
    {
        this.userService = DependencyProvider.getUserService();
    }

    public LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(String uid)
    {
        return userService.getLocalUsers(uid);
    }
}
