package com.skellyco.hito.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.r0adkll.slidr.Slidr;
import com.skellyco.hito.R;
import com.skellyco.hito.core.entity.dto.ResetPasswordDTO;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etEmail;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Slidr.attach(this);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews()
    {
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        btnConfirm = findViewById(R.id.btnConfirm);
    }

    private void initializeListeners()
    {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(email);
                resetPassword(resetPasswordDTO);
            }
        });
    }

    private void resetPassword(ResetPasswordDTO resetPasswordDTO)
    {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }
}
