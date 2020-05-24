package com.skellyco.hito.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;

public class ChatActivity extends AppCompatActivity {

    public static String TAG = "ChatActivity";
    public static String EXTRA_LOGGED_IN_UID = "EXTRA_LOGGED_IN_UID";
    public static String EXTRA_INTERLOCUTOR_UID = "INTERLOCUTOR_UID";

    private ImageButton btnBack;
    private TextView tvDisplayName;
    private EditText etMessageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Slidr.attach(this);
        String loggedInUid = getIntent().getStringExtra(EXTRA_LOGGED_IN_UID);
        String id = getIntent().getStringExtra(EXTRA_INTERLOCUTOR_UID);
        Log.e(TAG, loggedInUid + "\n" + id);

        initializeViews();
        initializeViewModel();
        initializeListeners();
    }

    private void initializeViews()
    {
        btnBack = findViewById(R.id.btnBack);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        etMessageInput = findViewById(R.id.etMessageInput);
    }

    private void initializeViewModel()
    {

    }

    private void initializeListeners()
    {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
