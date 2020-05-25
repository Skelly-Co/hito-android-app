package com.skellyco.hito.model.firebase.dao;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PrivateConversationDAO {

    private static final String PRIVATE_CONVERSATIONS_COLLECTION = "privateConversations";
    public static final String FIELD_ID = "id";
    public static final String FIELD_FIRST_INTERLOCUTOR_ID = "firstInterlocutorId";
    public static final String FIELD_SECOND_INTERLOCUTOR_ID = "secondInterlocutorId";

    private static  final String MESSAGES_SUBCOLLECTION = "messages";
    public static final String MESSAGES_FIELD_ID = "id";
    public static final String MESSAGES_FIELD_INTERLOCUTOR_ID = "interlocutorId";
    public static final String MESSAGES_FIELD_POST_TIME = "postTime";
    public static final String MESSAGES_FIELD_TEXT = "text";

    private FirebaseFirestore firestore;

    public PrivateConversationDAO()
    {
        firestore = FirebaseFirestore.getInstance();
    }

    public Query getPrivateConversation(String firstInterlocutorId, String secondInterlocutorId)
    {
        return firestore.collection(PRIVATE_CONVERSATIONS_COLLECTION)
                .whereEqualTo(FIELD_FIRST_INTERLOCUTOR_ID, firstInterlocutorId)
                .whereEqualTo(FIELD_SECOND_INTERLOCUTOR_ID, secondInterlocutorId);
    }

    public Query getMessages(String conversationId)
    {
        return firestore.collection(PRIVATE_CONVERSATIONS_COLLECTION)
                .document(conversationId)
                .collection(MESSAGES_SUBCOLLECTION)
                .orderBy(MESSAGES_FIELD_POST_TIME);
    }
}
