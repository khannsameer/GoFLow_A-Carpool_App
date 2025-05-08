package com.example.mad_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText etFullName, etEmail, etPhoneNumber, etDOB, etPassword, etConfirmPassword;
    Button btnSignUp;
    TextView tvLoginLink;
    ImageView backbtn;

    RadioGroup rgGender;
    RadioButton rbMale, rbFemale, rbOther;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI Elements
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etDOB = findViewById(R.id.etDOB);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        backbtn = findViewById(R.id.backbtn);

        // Gender RadioButtons
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);

        // Listeners
        btnSignUp.setOnClickListener(v -> registerUser());
        tvLoginLink.setOnClickListener(v -> startActivity(new Intent(SignUp.this, LogInPage.class)));
        backbtn.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, LogInPage.class));
            finish();
        });
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String dob = etDOB.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Get gender from selected radio button
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            showToast("Please select your gender");
            return;
        }

        RadioButton selectedGenderBtn = findViewById(selectedGenderId);
        String gender = selectedGenderBtn.getText().toString();

        // Validation
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(dob) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            etFullName.setError("Please Enter Valid Name");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            etPhoneNumber.setError("Enter a valid 10-digit phone number");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToFirestore(firebaseUser.getUid(), fullName, email, phone, dob, gender);
                        }
                    } else {
                        showToast("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private void saveUserToFirestore(String userId, String fullName, String email, String phone, String dob, String gender) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", fullName);
        userMap.put("email", email);
        userMap.put("phone", phone);
        userMap.put("dob", dob);
        userMap.put("gender", gender);
        userMap.put("createdAt", System.currentTimeMillis());

        db.collection("Users").document(userId).set(userMap)
                .addOnSuccessListener(aVoid -> {
                    showToast("Registration successful!");

                    Intent intent = new Intent(SignUp.this, LogInPage.class);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("dob", dob);
                    intent.putExtra("gender", gender);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        showToast("Firestore error: " + e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
    }
}
