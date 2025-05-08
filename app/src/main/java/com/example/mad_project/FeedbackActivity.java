package com.example.mad_project;

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

//import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    ImageView backbtn;
    EditText etFeedback;
    Button btnSubmit;
//    LottieAnimationView lottieSuccess;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backbtn = findViewById(R.id.backbtn);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmit = findViewById(R.id.btnSubmit);
//        lottieSuccess = findViewById(R.id.lottieSuccess);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backbtn.setOnClickListener(v -> {
            startActivity(new Intent(FeedbackActivity.this, RideBooking.class));
            finish();
        });

        btnSubmit.setOnClickListener(v -> {
            String feedbackText = etFeedback.getText().toString().trim();

            if (feedbackText.isEmpty()) {
                Toast.makeText(FeedbackActivity.this, "Please enter your feedback!", Toast.LENGTH_SHORT).show();
            } else {
                saveFeedbackToFirestore(feedbackText);
            }
        });
    }

    private void saveFeedbackToFirestore(String feedbackText) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "anonymous";

        Map<String, Object> feedback = new HashMap<>();
        feedback.put("userId", userId);
        feedback.put("feedback", feedbackText);
        feedback.put("timestamp", System.currentTimeMillis());

        db.collection("Feedback")
                .add(feedback)
                .addOnSuccessListener(documentReference -> {
                    etFeedback.setText("");
                    btnSubmit.setVisibility(View.GONE);
                    etFeedback.setVisibility(View.GONE);
//                    lottieSuccess.setVisibility(View.VISIBLE);
//                    lottieSuccess.playAnimation();
                    Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
