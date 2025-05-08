package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RideDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ride_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get intent extras
        String time = getIntent().getStringExtra("time");
        String route = getIntent().getStringExtra("route");
        String price = getIntent().getStringExtra("price");

        // Initialize views
        TextView tvTime = findViewById(R.id.rideDetailsText);
        TextView tvPrice = findViewById(R.id.ridePriceText);
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton searchButton = findViewById(R.id.searchButton);
        Button bookRideButton = findViewById(R.id.bookRideButton);

        // Set ride details
        tvTime.setText("Departure: " + time);
        tvPrice.setText("₹ " + price);

        // Back button
        backButton.setOnClickListener(v -> onBackPressed());

        // Search Again button
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(RideDetails.this, SearchRide.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Book Ride and save to Firestore
        bookRideButton.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";

            // Create booking data
            Map<String, Object> booking = new HashMap<>();
            booking.put("userId", userId);
            booking.put("time", time);
            booking.put("route", route);
            booking.put("price", price);
            booking.put("status", "confirmed");

            // Save to Firestore
            db.collection("Bookings")
                    .add(booking)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(RideDetails.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();

                        // Proceed to payment screen
                        Intent intent = new Intent(RideDetails.this, AddPaymentDetails.class);
                        intent.putExtra("price", price);
                        intent.putExtra("time", time);
                        intent.putExtra("route", route);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RideDetails.this, "Booking Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
