package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SearchRide extends AppCompatActivity {

    LinearLayout rideCard1, rideCard2, rideCard3;
    TextView rideTime1, rideTime2, rideTime3;
    TextView rideRoute1, rideRoute2, rideRoute3;
    TextView ridePrice1, ridePrice2, ridePrice3;

    FirebaseFirestore db;
    boolean ridesFound = false; // Track if any rides were loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_ride);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receive data from RideBooking
        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        int passengers = getIntent().getIntExtra("passengers", 1); // default 1 if not sent

        Log.d("SearchRide", "Received: " + from + ", " + to + ", " + date + ", " + time + ", Passengers: " + passengers);

        // Initialize Views
        rideCard1 = findViewById(R.id.rideCard1);
        rideCard2 = findViewById(R.id.rideCard2);
        rideCard3 = findViewById(R.id.rideCard3);

        rideTime1 = findViewById(R.id.rideTime1);
        rideTime2 = findViewById(R.id.rideTime2);
        rideTime3 = findViewById(R.id.rideTime3);

        rideRoute1 = findViewById(R.id.rideRoute1);
        rideRoute2 = findViewById(R.id.rideRoute2);
        rideRoute3 = findViewById(R.id.rideRoute3);

        ridePrice1 = findViewById(R.id.ridePrice1);
        ridePrice2 = findViewById(R.id.ridePrice2);
        ridePrice3 = findViewById(R.id.ridePrice3);

        db = FirebaseFirestore.getInstance();

        fetchRidesFromFirestore();

        // Card click listeners
        rideCard1.setOnClickListener(v -> navigateToRideDetails(
                rideTime1.getText().toString(),
                rideRoute1.getText().toString(),
                ridePrice1.getText().toString()));

        rideCard2.setOnClickListener(v -> navigateToRideDetails(
                rideTime2.getText().toString(),
                rideRoute2.getText().toString(),
                ridePrice2.getText().toString()));

        rideCard3.setOnClickListener(v -> navigateToRideDetails(
                rideTime3.getText().toString(),
                rideRoute3.getText().toString(),
                ridePrice3.getText().toString()));

        // Search Again button
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            if (!ridesFound) {
                Intent intent = new Intent(SearchRide.this, RideBooking.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Rides found. Tap a ride or change filters in RideBooking.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRidesFromFirestore() {
        db.collection("rides")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> rides = queryDocumentSnapshots.getDocuments();
                    if (rides.size() >= 1) {
                        ridesFound = true;
                    }

                    if (rides.size() >= 3) {
                        bindRideData(rides.get(0), rideTime1, rideRoute1, ridePrice1);
                        bindRideData(rides.get(1), rideTime2, rideRoute2, ridePrice2);
                        bindRideData(rides.get(2), rideTime3, rideRoute3, ridePrice3);
                    } else if (rides.size() == 2) {
                        bindRideData(rides.get(0), rideTime1, rideRoute1, ridePrice1);
                        bindRideData(rides.get(1), rideTime2, rideRoute2, ridePrice2);
                    } else if (rides.size() == 1) {
                        bindRideData(rides.get(0), rideTime1, rideRoute1, ridePrice1);
                    } else {
                        Toast.makeText(this, "No rides found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load rides: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("SearchRide", "Firestore error", e);
                });
    }

    private void bindRideData(DocumentSnapshot doc, TextView time, TextView route, TextView price) {
        time.setText(doc.getString("time"));
        route.setText(doc.getString("route"));
        price.setText(doc.getString("price"));
    }

    private void navigateToRideDetails(String time, String route, String price) {
        Intent intent = new Intent(SearchRide.this, RideDetails.class);
        intent.putExtra("time", time);
        intent.putExtra("route", route);
        intent.putExtra("price", price);
        startActivity(intent);
    }
}
