package com.example.mad_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPassword extends AppCompatActivity {

    EditText etEmail;
    Button btnNext;

    ImageView backbtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        btnNext = findViewById(R.id.btnNext);
        backbtn= findViewById(R.id.backbtn);

        if (backbtn != null) {
            backbtn.setOnClickListener(v -> {
                Intent intent = new Intent(ForgotPassword.this, LogInPage.class);
                startActivity(intent);
                finish();
            });
        }

        btnNext.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPassword.this, "Enter your email", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ForgotPassword.this, SecurityPinActivity.class);
                startActivity(intent);
            }
        });
    }
}