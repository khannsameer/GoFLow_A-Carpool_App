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

public class SecurityPinActivity extends AppCompatActivity {

    EditText etPin;
    Button btnVerify, btnSendAgain;
    ImageView backbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_security_pin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etPin = findViewById(R.id.etPin);
        btnVerify = findViewById(R.id.btnVerify);
        btnSendAgain = findViewById(R.id.btnSendAgain);
        backbtn = findViewById(R.id.backbtn);

        if (backbtn != null) {
            backbtn.setOnClickListener(view -> {
                Intent intent = new Intent(SecurityPinActivity.this, LogInPage.class);
                startActivity(intent);
                finish();
            });
        }

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = etPin.getText().toString().trim();
                if (pin.isEmpty()) {
                    Toast.makeText(SecurityPinActivity.this, "Please enter the security PIN", Toast.LENGTH_SHORT).show();
                } else if (pin.equals("1234")) {  // Example PIN check
                    Intent intent = new Intent(SecurityPinActivity.this, NewPassword.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SecurityPinActivity.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecurityPinActivity.this, "A new PIN has been sent to your email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}