package com.skellyco.hito.ui.view.main;

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
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.core.shared.Resource;
import com.skellyco.hito.core.shared.error.FetchDataError;
import com.skellyco.hito.ui.view.entry.LoginActivity;
import com.skellyco.hito.ui.view.main.adapter.UserAdapter;
import com.skellyco.hito.core.util.LoginDataManager;
import com.skellyco.hito.ui.view.util.animation.ResizeWidthAnimation;
import com.skellyco.hito.ui.viewmodel.main.MainViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Role of this Activity is to allow the user to see his conversation history, groups and local users.
 * For right now groups and history are not implemented, so the user is only able to see the list of
 * the local users. After clicking on some group / history item / local user, user will be redirected to
 * ChatActivity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Defines the navigation bar items.
     */
    private enum NavbarItem {
        GROUPS(0), LOCAL_USERS(1), HISTORY(2);

        private final int value;

        NavbarItem(int value)
        {
            this.value = value;
        }

        /**
         * Returns the value of the NavbarItem
         * @return value of the NavbarItem.
         */
        public int getValue()
        {
            return value;
        }

        /**
         *
         * @param value value of the NavbarItem.
         * @return NavbarItem associated with given value.
         */
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
    public static final String EXTRA_LOGGED_IN_UID = "EXTRA_LOGGED_IN_UID";
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
        String loggedInUid = getIntent().getStringExtra(EXTRA_LOGGED_IN_UID);

        initializeViews();
        initializeViewModel(loggedInUid);
        initializeListeners();
        initializeSettingsPopup();
        initializeRecyclerViewAndAdapters();
        initializeChatListObservers();
        initializeLoginDataManager();
        setUpStartingState(savedInstanceState);
    }

    /**
     * Initializes the views - assigns layout's elements to the instance variables.
     */
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

    /**
     *  Initializes the ViewModel.
     */
    private void initializeViewModel(String loggedInUid)
    {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setLoggedInUid(loggedInUid);
    }

    /**
     * Initializes the listeners - specifies the actions that should happen during the interaction between
     * the user and the activity.
     */
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

    /**
     * Initializes the settings popup. Saves the width of the settings popup and hides it.
     */
    private void initializeSettingsPopup()
    {
        settingsPopupWidth = linSettingsPopup.getLayoutParams().width;
        linSettingsPopup.getLayoutParams().width = SETTINGS_POPUP_HIDDEN_WIDTH;
        linSettingsPopup.requestLayout();
    }

    /**
     * Initializes the RecyclerView and its different adapters.
     *
     * Since right now only local users functionality is implemented it only
     * initializes localUsersAdapter.
     */
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
        //TO DO - initialize groups & history adapters
    }

    /**
     * Initializes the observers for the local users, history and groups.
     * We also have to store the observer in the instance variable in order to be
     * able to stop the observation associated with selected NavbarItem, while
     * switching to the different NavbarItem.
     *
     * Since right now only local users functionality is implemented it only
     * initializes localUsersObserver.
     */
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
        //TO DO - initialize groups & history observers
    }

    /**
     * Initializes LoginDataManager.
     */
    private void initializeLoginDataManager()
    {
        loginDataManager = new LoginDataManager(this);
    }

    /**
     * Sets up the activity during the startup. It checks whether the activity
     * is started for the first time or if it is being recreated after
     * configuration changes / orientation change / activity is being killed etc.
     *
     * If it's being started for the first time it sets it to the initial state
     * by setting the selectedNavbarItem to local users (this is the main and the
     * default functionality) and hiding the settings popup.
     *
     * If it's being recreated it restores it to the given saved state.
     *
     * @param savedInstanceState saved state of the activity.
     */
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

    /**
     * Invokes fetchData method on the MainViewModel to fetch the needed data into the model.
     * Afterwards it checks which navbar item was set during setUpStartingState method (that was invoked
     * in onCreate method) and loads the appropriate data (local users, groups or history).
     *
     */
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

    /**
     * Saves the current application state into the given outState in order to later
     * make it able to restore this state when configuration changes / orientation changes / activity is being killed etc.
     *
     * @param outState state that we save our data into.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_NAVBAR_ITEM, selectedNavbarItem.getValue());
        outState.putBoolean(STATE_SETTINGS_POPUP_SHOWN, settingsPopupShown);
    }

    /**
     * Checks which NavbarItem is currently selected and invokes the updateFilter method
     * on the adapter associated with the selectedNavbarItem.
     *
     * @param filter filter for updating the recycler view.
     */
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

    /**
     * Checks which NavbarItem is currently selected and removes the observer that
     * is associated with it.
     */
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

    /**
     * Since groups functionality is not implemented yet, this method just clears the adapter.
     */
    private void loadGroups()
    {
        //TO DO - start observing groups
        recChatList.setAdapter(null);
    }

    /**
     * Sets the RecyclerView adapter to localUsersAdapter and starts observing on the local users.
     * Since we are passing the activity to observe method of the LiveData we are making it Lifecycle aware,
     * which means that we don't have to implement onStop method and stop observing data there, since it will
     * be done automatically for us.
     */
    private void loadLocalUsers()
    {
        recChatList.setAdapter(localUsersAdapter);
        mainViewModel.getLocalUsers(this).observe(this, localUsersObserver);
    }

    /**
     * Since history functionality is not implemented yet, this method just clears the adapter.
     */
    private void loadHistory()
    {
        //TO DO - start observing history
        recChatList.setAdapter(null);
    }

    /**
     * Toggles the settings popup - shows it or hides it, depending on the current
     * value of settingPopupShown field.
     */
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

    /**
     * Shows the settings popup
     *
     * @param animate specifies if settings popup should be displayed with animation.
     */
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

    /**
     * Hides the settings popup.
     *
     * @param animate specifies if settings popup should be hidden with animation.
     */
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

    /**
     * Invokes clearSavedLoginData on LoginDataManager to clear the informations about the logged in user
     * and finishes the activity.
     */
    private void logout()
    {
        loginDataManager.clearSavedLoginData();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
        finish();
    }

    /**
     * Starts the ChatActivity.
     *
     * @param loggedInUid id of the logged in user.
     * @param interlocutorId id of the interlocutor.
     */
    private void startChatActivity(String loggedInUid, String interlocutorId)
    {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_LOGGED_IN_UID, loggedInUid);
        intent.putExtra(ChatActivity.EXTRA_INTERLOCUTOR_UID, interlocutorId);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
