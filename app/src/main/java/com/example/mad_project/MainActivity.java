package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    static int SPLASH_SCREEN = 4000;

    Animation logoAnim, carAnim, textAnim;
    ImageView logo, carLogo;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Register views
        logo = findViewById(R.id.logo);
        carLogo = findViewById(R.id.carLogo);
        text = findViewById(R.id.text);

        // Load animations
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        carAnim = AnimationUtils.loadAnimation(this, R.anim.car_animation);
        textAnim = AnimationUtils.loadAnimation(this, R.anim.text_animation);

        // Set animations to views
        logo.startAnimation(logoAnim);
        carLogo.startAnimation(carAnim);
        text.startAnimation(textAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Welcome.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}