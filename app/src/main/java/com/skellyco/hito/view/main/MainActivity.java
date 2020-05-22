package com.skellyco.hito.view.main;

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
        GROUPS, LOCAL_USERS, HISTORY
    }

    public static final String TAG = "MainActivity";
    public static final String EXTRA_UID = "EXTRA_UID";
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

    private String loggedInUid;
    private Observer<Resource<List<User>, FetchDataError>> localUsersObserver;
    private UserAdapter localUsersAdapter;
    private NavbarItem selectedNavbarItem;
    private int settingsPopupWidth;
    private boolean settingsPopupShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loggedInUid = getIntent().getStringExtra(EXTRA_UID);

        initializeViews();
        initializeSettingsPopup();
        initializeListeners();
        initializeViewModel();
        initializeLoginDataManager();
        initializeRecyclerViewAndAdapters();
        initializeChatListObservers();
        rbtLocalUsers.performClick();
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

    private void initializeSettingsPopup()
    {
        settingsPopupWidth = linSettingsPopup.getLayoutParams().width;
        linSettingsPopup.getLayoutParams().width = SETTINGS_POPUP_HIDDEN_WIDTH;
        linSettingsPopup.requestLayout();
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
                    loadHistory();
                    selectedNavbarItem = NavbarItem.HISTORY;
                }
            }
        });
    }

    private void initializeViewModel()
    {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void initializeLoginDataManager()
    {
        loginDataManager = new LoginDataManager(this);
    }

    private void initializeRecyclerViewAndAdapters()
    {
        recChatList.setLayoutManager(new LinearLayoutManager(this));
        recChatList.setHasFixedSize(true);
        localUsersAdapter = new UserAdapter();
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

    private void filterChatList(String filter)
    {
        switch (selectedNavbarItem)
        {
            case GROUPS:
            {
                //TO DO - filter groups
            }
            case LOCAL_USERS:
            {
                localUsersAdapter.updateFilter(filter);
            }
            case HISTORY:
            {
                //TO DO - filter history
            }
        }
    }

    private void loadGroups()
    {
        if(selectedNavbarItem == NavbarItem.LOCAL_USERS)
        {
            mainViewModel.getLocalUsers(loggedInUid).removeObserver(localUsersObserver);
        }
        else if (selectedNavbarItem == NavbarItem.HISTORY)
        {
            //TO DO - stop observing history
        }
        //TO DO - start observing groups
    }

    private void loadLocalUsers()
    {
        if(selectedNavbarItem == NavbarItem.GROUPS)
        {
            //TO DO - stop observing groups
        }
        else if(selectedNavbarItem == NavbarItem.HISTORY)
        {
            //TO DO - stop observing history
        }
        recChatList.setAdapter(localUsersAdapter);
        mainViewModel.getLocalUsers(loggedInUid).observe(this, localUsersObserver);
    }

    private void loadHistory()
    {
        if(selectedNavbarItem == NavbarItem.GROUPS)
        {
            //TO DO - stop observing groups
        }
        else if(selectedNavbarItem == NavbarItem.LOCAL_USERS)
        {
            mainViewModel.getLocalUsers(loggedInUid).removeObserver(localUsersObserver);
        }
        //TO DO - start observing history
    }

    private void toggleSettingsPopup()
    {
        if(settingsPopupShown)
        {
            hideSettingsPopup();
        }
        else
        {
            showSettingsPopup();
        }
    }

    private void showSettingsPopup()
    {
        settingsPopupShown = true;
        ResizeWidthAnimation resizeAnimation = new ResizeWidthAnimation(linSettingsPopup, settingsPopupWidth);
        resizeAnimation.setDuration(SETTINGS_POPUP_ANIM_DURATION);
        linSettingsPopup.startAnimation(resizeAnimation);
    }

    private void hideSettingsPopup()
    {
        settingsPopupShown = false;
        ResizeWidthAnimation resizeAnimation = new ResizeWidthAnimation(linSettingsPopup, SETTINGS_POPUP_HIDDEN_WIDTH);
        resizeAnimation.setDuration(SETTINGS_POPUP_ANIM_DURATION);
        linSettingsPopup.startAnimation(resizeAnimation);
    }

    private void logout()
    {
        loginDataManager.clearSavedLoginData();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
        finish();
    }
}
