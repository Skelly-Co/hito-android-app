package com.skellyco.hito.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.skellyco.hito.R;

public class MainActivity extends AppCompatActivity {

    private RadioButton rbtGroups;
    private RadioButton rbtLocalUsers;
    private RadioButton rbtHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews()
    {
        rbtGroups = findViewById(R.id.rbtGroups);
        rbtLocalUsers = findViewById(R.id.rbtLocalUsers);
        rbtHistory = findViewById(R.id.rbtHistory);
    }

    private void initializeListeners()
    {
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

    private void loadGroups()
    {

    }

    private void loadLocalUsers()
    {

    }

    private void loadHistory()
    {

    }
}
