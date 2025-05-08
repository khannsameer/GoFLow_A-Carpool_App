package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RIdeConfirmActivity extends AppCompatActivity {

    ImageView checkIcon;
    TextView confirmationText;
    Button goHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ride_confirm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkIcon = findViewById(R.id.checkmarkIcon);
        confirmationText = findViewById(R.id.confirmationMessage);
        goHomeButton = findViewById(R.id.goHomeButton);

        // Load animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(2000); // 1 second
        checkIcon.startAnimation(fadeIn);
        confirmationText.startAnimation(fadeIn);
        goHomeButton.startAnimation(fadeIn);

        goHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RIdeConfirmActivity.this, RideBooking.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // ← here’s the animation
            finish();
        });
    }
}
