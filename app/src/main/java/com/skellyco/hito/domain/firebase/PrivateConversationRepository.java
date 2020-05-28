package com.skellyco.hito.domain.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.skellyco.hito.core.domain.IPrivateConversationRepository;
import com.skellyco.hito.core.model.entity.Message;
import com.skellyco.hito.core.model.entity.PrivateConversation;
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.model.dto.MessageDTO;
import com.skellyco.hito.core.model.dto.PrivateConversationDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.core.shared.error.InsertDataError;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.domain.firebase.dao.PrivateConversationDAO;
import com.skellyco.hito.domain.firebase.dao.UserDAO;

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
    public LiveData<Resource<Void, InsertDataError>> createPrivateConversation(final Activity activity,
                                                                               final PrivateConversationDTO privateConversationDTO,
                                                                               final MessageDTO messageDTO)
    {
        final MutableLiveData<Resource<Void, InsertDataError>> createPrivateConversationResource = new MutableLiveData<>();
        // It is important to notice here that we are on purpose not passing the activity to the first onCompleteListener
        // We wanna make sure that after creating a conversation the insertMessage method will be invoked and that the given
        // message will be inserted, no matter if passed activity is alive or not.
        privateConversationDAO.createPrivateConversation(privateConversationDTO)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                {
                    String privateConversationId = task.getResult().getId();
                    LiveDataUtil.observeOnce(insertMessage(activity, privateConversationId, messageDTO), new Observer<Resource<Void, InsertDataError>>() {
                        @Override
                        public void onChanged(Resource<Void, InsertDataError> insertMessageResource) {
                            if(insertMessageResource.getStatus() == Resource.Status.SUCCESS)
                            {
                                Resource<Void, InsertDataError> resource = new Resource<>(Resource.Status.SUCCESS, null, null);
                                createPrivateConversationResource.setValue(resource);
                            }
                            else
                            {
                                InsertDataError error = insertMessageResource.getError();
                                Resource<Void, InsertDataError> resource = new Resource<>(Resource.Status.ERROR, null, error);
                                createPrivateConversationResource.setValue(resource);
                            }
                        }
                    });
                }
                else
                {
                    Log.e(TAG, task.getException().getMessage());
                    InsertDataError error = new InsertDataError(InsertDataError.Type.UNKNOWN);
                    Resource<Void, InsertDataError> resource = new Resource<>(Resource.Status.ERROR, null, error);
                    createPrivateConversationResource.setValue(resource);
                }
            }
        });
        return createPrivateConversationResource;
    }

    @Override
    public LiveData<Resource<Void, InsertDataError>> insertMessage(Activity activity, final String privateConversationId, final MessageDTO messageDTO)
    {
        final MutableLiveData<Resource<Void, InsertDataError>> insertMessageResource = new MutableLiveData<>();
        privateConversationDAO.insertMessage(privateConversationId, messageDTO)
                .addOnCompleteListener(activity, new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                {
                    Resource<Void, InsertDataError> resource = new Resource<>(Resource.Status.SUCCESS, null, null);
                    insertMessageResource.setValue(resource);
                }
                else
                {
                    Log.e(TAG, task.getException().getMessage());
                    InsertDataError error = new InsertDataError(InsertDataError.Type.UNKNOWN);
                    Resource<Void, InsertDataError> resource = new Resource<>(Resource.Status.ERROR, null, error);
                    insertMessageResource.setValue(resource);
                }
            }
        });
        return insertMessageResource;
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
                        // If conversation was not found, we also have to check for swapped order of interlocutors
                        // This is not the most efficient solution, but the only one since firebase does not support OR operator.
                        privateConversationDAO.getPrivateConversation(secondInterlocutorId, firstInterlocutorId)
                                .addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                                        if(snapshots.isEmpty())
                                        {
                                            //Private conversation not found - there is no conversation between users
                                            //If conversation will be created in the future this method will be invoked and we will hit else clause
                                            Resource<PrivateConversation, FetchDataError> resource =
                                                    new Resource<>(Resource.Status.SUCCESS, null, null);
                                            privateConversationResource.setValue(resource);
                                        }
                                        else
                                        {
                                            DocumentSnapshot conversationDoc = snapshots.get(0);
                                            final String privateConversationId = conversationDoc.getId();
                                            fetchUsersAndMessages(privateConversationResource, activity,
                                                    privateConversationId, secondInterlocutorId, firstInterlocutorId);
                                        }
                                    }
                                });
                    }
                    else
                    {
                        DocumentSnapshot conversationDoc = snapshots.get(0);
                        final String privateConversationId = conversationDoc.getId();
                        fetchUsersAndMessages(privateConversationResource, activity,
                                privateConversationId, firstInterlocutorId, secondInterlocutorId);
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

    private void fetchUsersAndMessages(final MutableLiveData<Resource<PrivateConversation, FetchDataError>> privateConversationResource,
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
                                fetchMessages(privateConversationResource, activity,
                                        privateConversationId, firstInterlocutor, secondInterlocutor);
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

    private void fetchMessages(final MutableLiveData<Resource<PrivateConversation, FetchDataError>> privateConversationResource,
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
                        Resource<PrivateConversation, FetchDataError> resource =
                                new Resource<>(Resource.Status.SUCCESS, null, null);
                        privateConversationResource.setValue(resource);
                    }
                    else
                    {
                        List<Message> messages = convertMessages(snapshots, firstInterlocutor, secondInterlocutor);
                        PrivateConversation privateConversation =
                                new PrivateConversation(privateConversationId, firstInterlocutor, secondInterlocutor, messages);
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
            String id = snapshot.getId();
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
