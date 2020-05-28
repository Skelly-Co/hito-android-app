package com.skellyco.hito.ui.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;
import com.skellyco.hito.core.model.entity.PrivateConversation;
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.model.dto.MessageDTO;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.core.shared.error.InsertDataError;
import com.skellyco.hito.core.util.LiveDataUtil;
import com.skellyco.hito.ui.view.main.adapter.MessageAdapter;
import com.skellyco.hito.ui.viewmodel.main.ChatViewModel;

import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    public static String TAG = "ChatActivity";
    public static String EXTRA_LOGGED_IN_UID = "EXTRA_LOGGED_IN_UID";
    public static String EXTRA_INTERLOCUTOR_UID = "INTERLOCUTOR_UID";
    private static String MESSAGE_PROMPT = "Message to @";

    private ImageButton btnBack;
    private TextView tvDisplayName;
    private RecyclerView recMessages;
    private EditText etMessageInput;
    private ImageButton btnSendMessage;

    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Slidr.attach(this);
        String loggedInUid = getIntent().getStringExtra(EXTRA_LOGGED_IN_UID);
        String interlocutorId = getIntent().getStringExtra(EXTRA_INTERLOCUTOR_UID);

        initializeViews();
        initializeViewModel(loggedInUid, interlocutorId);
        initializeListeners();
        initializeRecyclerViewAndAdapter();
        initializeMessageInput();
    }

    /**
     * Initializes the views - assigns layout's elements to the instance variables.
     */
    private void initializeViews()
    {
        btnBack = findViewById(R.id.btnBack);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        recMessages = findViewById(R.id.recMessages);
        etMessageInput = findViewById(R.id.etMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);
    }

    /**
     *  Initializes the ViewModel.
     */
    private void initializeViewModel(String loggedInUid, String interlocutorUid)
    {
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.setUsers(loggedInUid, interlocutorUid);
    }

    /**
     * Initializes the listeners - specifies the actions that should happen during the interaction between
     * the user and the activity.
     */
    private void initializeListeners()
    {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etMessageInput.getText().toString().isEmpty())
                {
                    btnSendMessage.setEnabled(false);
                }
                else
                {
                    btnSendMessage.setEnabled(true);
                }
            }
        });
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    /**
     * Initializes the RecyclerView with messages and the adapter for it.
     * Sets the listeners on RecyclerView and MessageAdapter to keep the scroll
     * of the RecyclerView on the bottom when new Message appears or when the soft keyboard
     * appears.
     */
    private void initializeRecyclerViewAndAdapter()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recMessages.setLayoutManager(layoutManager);
        recMessages.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    recMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });

        messageAdapter = new MessageAdapter();
        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recMessages.scrollToPosition(messageAdapter.getItemCount()-1);
            }
        });
        recMessages.setAdapter(messageAdapter);
    }

    /**
     * Initializes the message input and send message button by disabling them.
     * During the creation of the activity, the PrivateConversation is still not
     * loaded so sending a message should be disabled.
     */
    private void initializeMessageInput()
    {
        etMessageInput.setEnabled(false);
        btnSendMessage.setEnabled(false);
    }

    /**
     * Invokes fetchInterlocutor method on the ChatViewModel to fetch the data into the model
     * and afterwards invokes getInterlocutor method to observe on the interlocutor and display
     * the interlocutor's data in the topbar.
     *
     * Also after interlocutor is successfully fetched it
     * invokes loadPrivateConversationAndMessages method to fetch the conversation between the logged in
     * user and the interlocutor.
     *
     * It is important to notice that we are passing the Activity into the observe method which makes our
     * observer lifecycle-aware. It means that we do not have to override the onStop method and stop observing on the LiveData
     * there, since it will be done automatically.
     */
    @Override
    protected void onStart() {
        super.onStart();
        chatViewModel.fetchInterlocutor(this);
        chatViewModel.getInterlocutor().observe(this, new Observer<Resource<User, FetchDataError>>() {
            @Override
            public void onChanged(Resource<User, FetchDataError> resource) {
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    tvDisplayName.setText(resource.getData().getUsername());
                    etMessageInput.setHint(MESSAGE_PROMPT + resource.getData().getUsername());
                    loadPrivateConversationAndMessages();
                }
                // For right now I have not discovered any possible errors that can happen during fetching the data.
                // If some error will occur, it will be logged in domain layer so it won't pass unnoticed.
                // If this will happen, I will update error handling for FetchDataError both in domain and here.
            }
        });
    }

    private void loadPrivateConversationAndMessages()
    {
        chatViewModel.fetchPrivateConversation(this);
        chatViewModel.getPrivateConversation().observe(this, new Observer<Resource<PrivateConversation, FetchDataError>>() {
            @Override
            public void onChanged(Resource<PrivateConversation, FetchDataError> resource) {
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    etMessageInput.setEnabled(true);
                    //We are checking here if conversation between users exists
                    if(resource.getData() != null)
                    {
                        messageAdapter.submitList(resource.getData().getMessages());
                    }
                }
            }
        });
    }

    private void sendMessage()
    {
        String interlocutorId = chatViewModel.getLoggedInUid();
        String text = etMessageInput.getText().toString();
        Date postTime = Calendar.getInstance().getTime();
        MessageDTO messageDTO = new MessageDTO(interlocutorId, postTime, text);

        btnSendMessage.setEnabled(false);
        LiveDataUtil.observeOnce(chatViewModel.sendMessage(this, messageDTO), new Observer<Resource<Void, InsertDataError>>() {
            @Override
            public void onChanged(Resource<Void, InsertDataError> resource) {
                btnSendMessage.setEnabled(true);
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    etMessageInput.setText("");
                }
                // For right now I have not discovered any possible errors that can happen during sending a message.
                // If some error will occur, it will be logged in domain layer so it won't pass unnoticed.
                // If this will happen, I will update error handling for FetchDataError both in domain and here.
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }

}
