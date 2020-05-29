package com.skellyco.hito.ui.viewmodel.main;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skellyco.hito.core.application.IUserService;
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.dependency.DependencyProvider;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */
public class MainViewModel extends ViewModel {

    private IUserService userService;
    private String loggedInUid;
    private LiveData<Resource<List<User>, FetchDataError>> localUsers;

    public MainViewModel()
    {
        this.userService = DependencyProvider.getUserService();
    }

    /**
     * Sets the information about logged in user which is required for data fetching.
     *
     * @param uid logged in user id.
     */
    public void setLoggedInUid(String uid)
    {
        this.loggedInUid = uid;
    }

    /**
     * Uses UserService to fetch the local users.
     * Since we haven't implemented functionality for groups & history this method
     * only fetches local users. However in the future it will also fetch the groups
     * and history.
     *
     * @param activity activity that invokes the method.
     */
    public void fetchData(Activity activity)
    {
        localUsers = userService.getLocalUsers(activity, loggedInUid);
        //fetch groups & history
    }

    /**
     * Returns logged in user id.
     *
     * @return logged in user id.
     */
    public String getLoggedInUid()
    {
        return loggedInUid;
    }

    /**
     * Returns LiveData with Resource that contains information about about the status of the call,
     * list of local users and error (if it occurred)
     * @param activity that invokes the method
     * @return local users LiveData
     */
    public LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(Activity activity)
    {
        return localUsers;
    }
}
