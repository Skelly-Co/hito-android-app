package com.skellyco.hito.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.skellyco.hito.R;
import com.skellyco.hito.view.entry.LoginActivity;
import com.skellyco.hito.view.util.LoginDataManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView imgProfilePicture;
    private LinearLayout linSettingsPopup;
    private ImageButton btnLogout;
    private ImageButton btnSettings;
    private RadioButton rbtGroups;
    private RadioButton rbtLocalUsers;
    private RadioButton rbtHistory;

    private LoginDataManager loginDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeListeners();
        initializeLoginDataManager();
    }

    private void initializeViews()
    {
        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        linSettingsPopup = findViewById(R.id.linSettingsPopup);
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnSettings);
        rbtGroups = findViewById(R.id.rbtGroups);
        rbtLocalUsers = findViewById(R.id.rbtLocalUsers);
        rbtHistory = findViewById(R.id.rbtHistory);
    }

    private void initializeListeners()
    {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        rbtGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGroups();
            }
        });
        rbtLocalUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLocalUsers();
            }
        });
        rbtHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHistory();
            }
        });
    }

    private void initializeLoginDataManager()
    {
        loginDataManager = new LoginDataManager(this);
    }

    private void loadGroups()
    {

    }

    private void loadLocalUsers()
    {

    }

    private void loadHistory()
    {

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
