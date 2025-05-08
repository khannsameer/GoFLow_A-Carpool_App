package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInPage extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignUp;
    private TextView tvForgotPassword, tvSignUpLink;
    private ImageView ivGoogleSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase Auth initialization
        mAuth = FirebaseAuth.getInstance();

        // Binding UI Elements
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);
        ivGoogleSignIn = findViewById(R.id.ivGoogleSignIn);

        // Handle Login Button Click
        btnLogin.setOnClickListener(view -> loginUser());

        // Navigate to SignUp Page
        btnSignUp.setOnClickListener(view -> startActivity(new Intent(LogInPage.this, SignUp.class)));
        tvSignUpLink.setOnClickListener(view -> startActivity(new Intent(LogInPage.this, SignUp.class)));

        // Handle Forgot Password
        tvForgotPassword.setOnClickListener(view -> startActivity(new Intent(LogInPage.this, ForgotPassword.class)));

        // Google Sign-In Placeholder
        ivGoogleSignIn.setOnClickListener(view -> Toast.makeText(LogInPage.this, "Google Sign-In coming soon!", Toast.LENGTH_SHORT).show());
    }

    private void loginUser() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etUsername.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Incorrect password");
            return;
        }

        // Check for spaces or whitespace in password
        if (password.contains(" ") || password.matches(".*\\s+.*")) {
            etPassword.setError("Incorrect password");
            return;
        }

        // Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LogInPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogInPage.this, RideBooking.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // ← THIS LINE ADDED
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LogInPage.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
