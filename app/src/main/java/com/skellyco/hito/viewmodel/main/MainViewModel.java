package com.skellyco.hito.viewmodel.main;

import android.app.Activity;

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
    private String loggedInUid;
    private LiveData<Resource<List<User>, FetchDataError>> localUsers;

    public MainViewModel()
    {
        this.userService = DependencyProvider.getUserService();
    }

    public void setLoggedInUid(String uid)
    {
        this.loggedInUid = uid;
    }

    public String getLoggedInUid()
    {
        return loggedInUid;
    }

    public LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(Activity activity)
    {
        if(localUsers == null)
        {
            localUsers = userService.getLocalUsers(activity, loggedInUid);
        }
        return localUsers;
    }
}
