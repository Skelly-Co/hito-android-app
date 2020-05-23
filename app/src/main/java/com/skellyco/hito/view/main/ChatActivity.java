package com.skellyco.hito.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;

public class ChatActivity extends AppCompatActivity {

    public static String TAG = "ChatActivity";
    public static String EXTRA_LOGGED_IN_UID = "EXTRA_LOGGED_IN_UID";
    public static String EXTRA_INTERLOCUTOR_UID = "INTERLOCUTOR_UID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Slidr.attach(this);
        String loggedInUid = getIntent().getStringExtra(EXTRA_LOGGED_IN_UID);
        String id = getIntent().getStringExtra(EXTRA_INTERLOCUTOR_UID);
        Log.e(TAG, loggedInUid + "\n" + id);
    }
}
