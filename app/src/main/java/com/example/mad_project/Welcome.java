package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn= findViewById(R.id.btn);

        btn.setOnClickListener(this);  // Set click listener
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            // Handle button click
            Toast.makeText(this, "Login Clicked", Toast.LENGTH_SHORT).show();

            //Navigate to another activity
            Intent intent = new Intent(Welcome.this, LogInPage.class);
            startActivity(intent);
        }
    }
}