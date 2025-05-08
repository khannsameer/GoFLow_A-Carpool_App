package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    ImageView backArrow;
    TextView tvFullName, tvEmail, tvPhone, tvDOB, tvGender, tvPhoneDetail;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase setup
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        backArrow = findViewById(R.id.back_arrow);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvDOB = findViewById(R.id.tvDOB);
        tvGender = findViewById(R.id.tvGender);
        tvPhoneDetail = findViewById(R.id.tvPhoneDetail);

        // Back navigation
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, RideBooking.class));
            finish();
        });

        // Load user profile
        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("Users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("fullName");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");
                            String dob = documentSnapshot.getString("dob");
                            String gender = documentSnapshot.getString("gender");

                            tvFullName.setText(name != null ? name : "N/A");
                            tvEmail.setText(email != null ? email : "N/A");
                            tvPhone.setText(phone != null ? phone : "N/A");
                            tvPhoneDetail.setText(phone != null ? phone : "Required");
                            tvDOB.setText(dob != null ? dob : "Required");
                            tvGender.setText(gender != null ? gender : "Required");
                        } else {
                            Toast.makeText(ProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }
}
