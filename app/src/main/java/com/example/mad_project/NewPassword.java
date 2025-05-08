package com.example.mad_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewPassword extends AppCompatActivity {

    EditText etNewPassword, etConfirmNewPassword;
    Button btnChangePassword;
    ImageView backbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        backbtn = findViewById(R.id.backbtn);

        if (backbtn != null) {
            backbtn.setOnClickListener(view -> {
                Intent intent = new Intent(NewPassword.this, LogInPage.class);
                startActivity(intent);
                finish();
            });
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmNewPassword.getText().toString().trim();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(NewPassword.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(NewPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewPassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewPassword.this, LogInPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}