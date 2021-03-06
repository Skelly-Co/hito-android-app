package com.skellyco.hito.domain.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.skellyco.hito.core.domain.IUserRepository;
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.domain.firebase.dao.UserDAO;
import com.skellyco.hito.domain.firebase.util.QueryFilter;

import java.util.List;

public class UserRepository implements IUserRepository {

    private static final String TAG = "UserRepository";

    private UserDAO userDAO;

    public UserRepository()
    {
        userDAO = new UserDAO();
    }

    @Override
    public LiveData<Resource<User, FetchDataError>> getUser(Activity activity, String uid) {
        final MutableLiveData<Resource<User, FetchDataError>> userResource = new MutableLiveData<>();
        userDAO.getUser(uid).addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null)
                {
                    User user = documentSnapshot.toObject(User.class);
                    Resource<User, FetchDataError> resource = new Resource<>(Resource.Status.SUCCESS, user, null);
                    userResource.setValue(resource);
                }
                else
                {
                    Log.e(TAG, e.getMessage());
                    FetchDataError error = new FetchDataError(FetchDataError.Type.UNKNOWN);
                    Resource<User, FetchDataError> resource = new Resource<>(Resource.Status.ERROR, userResource.getValue().getData(), error);
                    userResource.setValue(resource);
                }
            }
        });
        return userResource;
    }

    @Override
    public LiveData<Resource<List<User>, FetchDataError>> getLocalUsers(Activity activity, final String uid)
    {
        final MutableLiveData<Resource<List<User>, FetchDataError>> localUsers = new MutableLiveData<>();
        userDAO.getUsers().addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e == null)
                {
                    List<User> allUsers = queryDocumentSnapshots.toObjects(User.class);
                    // Since firebase queries are really limited we have to use our in-code query filter.
                    // Fetching all users and querying them in-code is not a good solution, but it is the only one.
                    List<User> filteredUsers = QueryFilter.filterLocalUsers(uid, allUsers);
                    Resource<List<User>, FetchDataError> resource = new Resource<>(Resource.Status.SUCCESS, filteredUsers, null);
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
        return localUsers;
    }
}
