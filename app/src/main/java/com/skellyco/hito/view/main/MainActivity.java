package com.skellyco.hito.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.skellyco.hito.R;
import com.skellyco.hito.core.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.view.entry.LoginActivity;
import com.skellyco.hito.view.main.adapter.UserAdapter;
import com.skellyco.hito.view.util.LoginDataManager;
import com.skellyco.hito.view.util.animation.ResizeWidthAnimation;
import com.skellyco.hito.viewmodel.main.MainViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private enum NavbarItem {
        GROUPS(0), LOCAL_USERS(1), HISTORY(2);

        private final int value;

        NavbarItem(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static NavbarItem getNavbarItem(int value)
        {
            NavbarItem[] navbarItems = NavbarItem.values();
            if(value < 0 || value >= navbarItems.length)
            {
                return null;
            }
            return navbarItems[value];
        }
    }

    public static final String TAG = "MainActivity";
    public static final String EXTRA_UID = "EXTRA_UID";
    private static final String STATE_NAVBAR_ITEM = "STATE_NAVBAR_ITEM";
    private static final String STATE_SETTINGS_POPUP_SHOWN = "SETTINGS_POPUP_SHOWN";
    private static final int SETTINGS_POPUP_ANIM_DURATION = 350;
    private static final int SETTINGS_POPUP_HIDDEN_WIDTH = 1;

    private CircleImageView imgProfilePicture;
    private LinearLayout linSettingsPopup;
    private ImageButton btnLogout;
    private ImageButton btnSettings;
    private EditText etSearch;
    private RecyclerView recChatList;
    private RadioButton rbtGroups;
    private RadioButton rbtLocalUsers;
    private RadioButton rbtHistory;

    private MainViewModel mainViewModel;
    private LoginDataManager loginDataManager;

    private Observer<Resource<List<User>, FetchDataError>> localUsersObserver;
    private UserAdapter localUsersAdapter;
    private NavbarItem selectedNavbarItem;
    private int settingsPopupWidth;
    private boolean settingsPopupShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String loggedInUid = getIntent().getStringExtra(EXTRA_UID);

        initializeViews();
        initializeViewModel(loggedInUid);
        initializeListeners();
        initializeSettingsPopup();
        initializeRecyclerViewAndAdapters();
        initializeChatListObservers();
        initializeLoginDataManager();
        setUpStartingState(savedInstanceState);
    }

    private void initializeViews()
    {
        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        linSettingsPopup = findViewById(R.id.linSettingsPopup);
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnSettings);
        etSearch = findViewById(R.id.etSearch);
        recChatList = findViewById(R.id.recChatList);
        rbtGroups = findViewById(R.id.rbtGroups);
        rbtLocalUsers = findViewById(R.id.rbtLocalUsers);
        rbtHistory = findViewById(R.id.rbtHistory);
    }

    private void initializeViewModel(String loggedInUid)
    {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setLoggedInUid(loggedInUid);
    }

    private void initializeListeners()
    {
        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSettingsPopup();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterChatList(s.toString());
            }
        });

        rbtGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedNavbarItem != NavbarItem.GROUPS)
                {
                    removeChatListObserver();
                    loadGroups();
                    selectedNavbarItem = NavbarItem.GROUPS;
                }
            }
        });
        rbtLocalUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedNavbarItem != NavbarItem.LOCAL_USERS)
                {
                    removeChatListObserver();
                    loadLocalUsers();
                    selectedNavbarItem = NavbarItem.LOCAL_USERS;
                }
            }
        });
        rbtHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedNavbarItem != NavbarItem.HISTORY)
                {
                    removeChatListObserver();
                    loadHistory();
                    selectedNavbarItem = NavbarItem.HISTORY;
                }
            }
        });
    }

    private void initializeSettingsPopup()
    {
        settingsPopupWidth = linSettingsPopup.getLayoutParams().width;
        linSettingsPopup.getLayoutParams().width = SETTINGS_POPUP_HIDDEN_WIDTH;
        linSettingsPopup.requestLayout();
    }

    private void initializeRecyclerViewAndAdapters()
    {
        recChatList.setLayoutManager(new LinearLayoutManager(this));
        localUsersAdapter = new UserAdapter();
        localUsersAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                startChatActivity(mainViewModel.getLoggedInUid(), user.getUid());
            }
        });
    }

    private void initializeChatListObservers()
    {
        localUsersObserver = new Observer<Resource<List<User>, FetchDataError>>()
        {
            @Override
            public void onChanged(Resource<List<User>, FetchDataError> resource)
            {
                if(resource.getStatus() == Resource.Status.SUCCESS)
                {
                    localUsersAdapter.submitList(resource.getData());
                }
                // For right now I have not discovered any possible errors that can happen during fetching the data.
                // If some error will occur, it will be logged in domain layer so it won't pass unnoticed.
                // If this will happen, I will update error handling for FetchDataError both in domain and here.
            }
        };
    }

    private void initializeLoginDataManager()
    {
        loginDataManager = new LoginDataManager(this);
    }

    private void setUpStartingState(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            boolean showPopup = savedInstanceState.getBoolean(STATE_SETTINGS_POPUP_SHOWN, false);
            if(showPopup)
            {
                showSettingsPopup(false);
            }

            int navbarItemValue = savedInstanceState.getInt(STATE_NAVBAR_ITEM, NavbarItem.LOCAL_USERS.getValue());
            selectedNavbarItem = NavbarItem.getNavbarItem(navbarItemValue);

        }
        else
        {
            selectedNavbarItem = NavbarItem.LOCAL_USERS;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainViewModel.fetchData(this);
        switch(selectedNavbarItem)
        {
            case GROUPS:
            {
                rbtGroups.setChecked(true);
                loadGroups();
                break;
            }
            case LOCAL_USERS:
            {
                rbtLocalUsers.setChecked(true);
                loadLocalUsers();
                break;
            }
            case HISTORY:
            {
                rbtHistory.setChecked(true);
                loadHistory();
                break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_NAVBAR_ITEM, selectedNavbarItem.getValue());
        outState.putBoolean(STATE_SETTINGS_POPUP_SHOWN, settingsPopupShown);
    }

    private void filterChatList(String filter)
    {
        switch (selectedNavbarItem)
        {
            case GROUPS:
            {
                //TO DO - filter groups
                break;
            }
            case LOCAL_USERS:
            {
                localUsersAdapter.updateFilter(filter);
                break;
            }
            case HISTORY:
            {
                //TO DO - filter history
                break;
            }
        }
    }

    private void removeChatListObserver()
    {
        switch (selectedNavbarItem)
        {
            case GROUPS:
            {
                //TO DO - remove groups observer
                break;
            }
            case LOCAL_USERS:
            {
                mainViewModel.getLocalUsers(this).removeObserver(localUsersObserver);
                break;
            }
            case HISTORY:
            {
                //TO DO - remove history observer
                break;
            }
        }
    }

    private void loadGroups()
    {
        //TO DO - start observing groups
        recChatList.setAdapter(null);
    }

    private void loadLocalUsers()
    {
        recChatList.setAdapter(localUsersAdapter);
        mainViewModel.getLocalUsers(this).observe(this, localUsersObserver);
    }

    private void loadHistory()
    {
        //TO DO - start observing history
        recChatList.setAdapter(null);
    }

    private void toggleSettingsPopup()
    {
        if(settingsPopupShown)
        {
            hideSettingsPopup(true);
        }
        else
        {
            showSettingsPopup(true);
        }
    }

    private void showSettingsPopup(boolean animate)
    {
        settingsPopupShown = true;
        if(animate)
        {
            ResizeWidthAnimation resizeAnimation = new ResizeWidthAnimation(linSettingsPopup, settingsPopupWidth);
            resizeAnimation.setDuration(SETTINGS_POPUP_ANIM_DURATION);
            linSettingsPopup.startAnimation(resizeAnimation);
        }
        else
        {
            linSettingsPopup.getLayoutParams().width = settingsPopupWidth;
            linSettingsPopup.requestLayout();
        }
    }

    private void hideSettingsPopup(boolean animate)
    {
        settingsPopupShown = false;
        if(animate)
        {
            ResizeWidthAnimation resizeAnimation = new ResizeWidthAnimation(linSettingsPopup, SETTINGS_POPUP_HIDDEN_WIDTH);
            resizeAnimation.setDuration(SETTINGS_POPUP_ANIM_DURATION);
            linSettingsPopup.startAnimation(resizeAnimation);
        }
        else
        {
            linSettingsPopup.getLayoutParams().width = SETTINGS_POPUP_HIDDEN_WIDTH;
            linSettingsPopup.requestLayout();
        }
    }

    private void logout()
    {
        loginDataManager.clearSavedLoginData();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
        finish();
    }

    private void startChatActivity(String loggedInUid, String interlocutorId)
    {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_LOGGED_IN_UID, loggedInUid);
        intent.putExtra(ChatActivity.EXTRA_INTERLOCUTOR_UID, interlocutorId);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
