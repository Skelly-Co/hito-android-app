package com.skellyco.hito.model.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.skellyco.hito.core.domain.IPrivateConversationRepository;
import com.skellyco.hito.core.entity.Message;
import com.skellyco.hito.core.entity.PrivateConversation;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.model.firebase.dao.PrivateConversationDAO;
import com.skellyco.hito.model.firebase.dao.UserDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrivateConversationRepository implements IPrivateConversationRepository {

    private static final String TAG = "PrivConvRepo";

    private PrivateConversationDAO privateConversationDAO;
    private UserDAO userDAO;

    public PrivateConversationRepository()
    {
        privateConversationDAO = new PrivateConversationDAO();
        userDAO = new UserDAO();
    }

    @Override
    public LiveData<Resource<PrivateConversation, FetchDataError>> getPrivateConversation(final Activity activity,
                                                                                          final String firstInterlocutorId,
                                                                                          final String secondInterlocutorId)
    {
        final MutableLiveData<Resource<PrivateConversation, FetchDataError>> privateConversationResource = new MutableLiveData<>();
        privateConversationDAO.getPrivateConversation(firstInterlocutorId, secondInterlocutorId)
                .addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e == null)
                {
                    List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                    if(snapshots.isEmpty())
                    {
                        //Private conversation not found - there is no conversation between users
                        //If conversation will be created in the future this method will be invoked and we will hit else clause
                        Resource<PrivateConversation, FetchDataError> resource = new Resource<>(Resource.Status.SUCCESS, null, null);
                        privateConversationResource.setValue(resource);
                    }
                    else
                    {
                        DocumentSnapshot conversationDoc = snapshots.get(0);
                        final String privateConversationId = conversationDoc.getString(PrivateConversationDAO.FIELD_ID);
                        addUsersAndMessages(privateConversationResource, activity, privateConversationId, firstInterlocutorId, secondInterlocutorId);
                    }
                }
                else
                {
                    Log.e(TAG, e.getMessage());
                    FetchDataError error = new FetchDataError(FetchDataError.Type.UNKNOWN);
                    Resource<PrivateConversation, FetchDataError> resource = new Resource<>(Resource.Status.ERROR,
                            privateConversationResource.getValue().getData(), error);
                    privateConversationResource.setValue(resource);
                }
            }
        });
        return privateConversationResource;
    }

    private void addUsersAndMessages(final MutableLiveData<Resource<PrivateConversation, FetchDataError>> privateConversationResource,
                                     final Activity activity, final String privateConversationId,
                                     final String firstInterlocutorId, final String secondInterlocutorId)
    {
        userDAO.getUser(firstInterlocutorId).addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null)
                {
                    final User firstInterlocutor = documentSnapshot.toObject(User.class);
                    userDAO.getUser(secondInterlocutorId).addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if(e == null)
                            {
                                final User secondInterlocutor = documentSnapshot.toObject(User.class);
                                addMessages(privateConversationResource, activity, privateConversationId, firstInterlocutor, secondInterlocutor);
                            }
                            else
                            {
                                Log.e(TAG, e.getMessage());
                                FetchDataError error = new FetchDataError(FetchDataError.Type.UNKNOWN);
                                Resource<PrivateConversation, FetchDataError> resource = new Resource<>(Resource.Status.ERROR,
                                        privateConversationResource.getValue().getData(), error);
                                privateConversationResource.setValue(resource);
                            }
                        }
                    });
                }
                else
                {
                    Log.e(TAG, e.getMessage());
                    FetchDataError error = new FetchDataError(FetchDataError.Type.UNKNOWN);
                    Resource<PrivateConversation, FetchDataError> resource = new Resource<>(Resource.Status.ERROR,
                            privateConversationResource.getValue().getData(), error);
                    privateConversationResource.setValue(resource);
                }
            }
        });
    }

    private void addMessages(final MutableLiveData<Resource<PrivateConversation, FetchDataError>> privateConversationResource,
                             final Activity activity, final String privateConversationId,
                             final User firstInterlocutor, final User secondInterlocutor)
    {
        privateConversationDAO.getMessages(privateConversationId).addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e == null)
                {
                    List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                    if(snapshots.isEmpty())
                    {
                        // No messages connected with private conversation.
                        // This can occur while creating a private conversation.
                        // When messages will be added we will hit the else clause.
                        Resource<PrivateConversation, FetchDataError> resource = new Resource<>(Resource.Status.SUCCESS, null, null);
                        privateConversationResource.setValue(resource);
                    }
                    else
                    {
                        List<Message> messages = convertMessages(snapshots, firstInterlocutor, secondInterlocutor);
                        PrivateConversation privateConversation =
                                new PrivateConversation(firstInterlocutor, secondInterlocutor, messages);
                        Resource<PrivateConversation, FetchDataError> resource =
                                new Resource<>(Resource.Status.SUCCESS, privateConversation, null);
                        privateConversationResource.setValue(resource);
                    }
                }
                else
                {
                    Log.e(TAG, e.getMessage());
                    FetchDataError error = new FetchDataError(FetchDataError.Type.UNKNOWN);
                    Resource<PrivateConversation, FetchDataError> resource = new Resource<>(Resource.Status.ERROR,
                            privateConversationResource.getValue().getData(), error);
                    privateConversationResource.setValue(resource);
                }
            }
        });
    }

    private List<Message> convertMessages(List<DocumentSnapshot> snapshots, User firstInterlocutor, User secondInterlocutor)
    {
        List<Message> messages = new ArrayList<>();
        for(DocumentSnapshot snapshot : snapshots)
        {
            User interlocutor;
            Date postTime;
            String text;
            String id = snapshot.getString(PrivateConversationDAO.MESSAGES_FIELD_ID);
            String interlocutorId = snapshot.getString(PrivateConversationDAO.MESSAGES_FIELD_INTERLOCUTOR_ID);
            if(interlocutorId.equals(firstInterlocutor.getUid()))
            {
                interlocutor = firstInterlocutor;
            }
            else
            {
                interlocutor = secondInterlocutor;
            }
            postTime = snapshot.getTimestamp(PrivateConversationDAO.MESSAGES_FIELD_POST_TIME).toDate();
            text = snapshot.getString(PrivateConversationDAO.MESSAGES_FIELD_TEXT);
            messages.add(new Message(id, interlocutor, postTime, text));
        }
        return messages;
    }

}
