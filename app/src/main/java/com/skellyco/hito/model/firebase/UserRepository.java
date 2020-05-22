package com.skellyco.hito.model.firebase;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.skellyco.hito.core.domain.IUserRepository;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.model.firebase.dao.UserDAO;

import java.util.List;

public class UserRepository implements IUserRepository {

    private static final String TAG = "UserRepository";

    private UserDAO userDAO;
    private String loggedInUid;
    private MutableLiveData<Resource<List<User>, FetchDataError>> localUsers;
    private ListenerRegistration localUsersListener;

    public UserRepository()
    {
        userDAO = new UserDAO();
    }

    @Override
    public LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(String uid)
    {
        // We want to add snapshot listener only on first invocation or if logged in user changed.
        if(localUsers == null || loggedInUid == null || !loggedInUid.equals(uid))
        {
            if(localUsers == null)
            {
                localUsers = new MutableLiveData<>();
            }
            // If logged in user changed we have to remove old snapshot listener and update loggedInUid variable
            if(loggedInUid != null && !loggedInUid.equals(uid))
            {
                localUsersListener.remove();
            }
            loggedInUid = uid;
            localUsersListener = userDAO.getUsers().addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e == null)
                    {
                        List<User> users = queryDocumentSnapshots.toObjects(User.class);
                        Resource<List<User>, FetchDataError> resource = new Resource<>(Resource.Status.SUCCESS, users, null);
                        localUsers.setValue(resource);
                    }
                    else
                    {
                        Log.e(TAG, e.getMessage());
                        FetchDataError error = new FetchDataError(FetchDataError.Type.UNKNOWN);
                        Resource<List<User>, FetchDataError> resource = new Resource<>(Resource.Status.ERROR, localUsers.getValue().getData(), error);
                        localUsers.setValue(resource);
                    }
                }
            });
        }
        return localUsers;
    }
}
