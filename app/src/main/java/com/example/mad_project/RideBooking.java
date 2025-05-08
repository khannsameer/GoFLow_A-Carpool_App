package com.example.mad_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RideBooking extends AppCompatActivity {

    EditText etFrom, etTo;
    TextView tvSelectedDate, tvPassengerCount;
    Button confirmBtn;
    ImageView ivCalendar, backbtn;
    ImageButton btnPlus, btnMinus, btnMenu;
    Button btnMorning, btnAfternoon, btnEvening, btnNight;

    String selectedDate = "";
    String selectedTime = "";
    int passengerCount = 1;

    DrawerLayout drawerLayout;
    ListView navList;

    FirebaseFirestore db;

    String[] menuItems = {
            "Profile", "Wallet", "History", "Feedback", "Notification", "Settings", "Help", "Referral", "About", "Logout"
    };

    int[] menuIcons = {
            R.drawable.icons8_placeholder_user_48,
            R.drawable.baseline_account_balance_wallet_24,
            R.drawable.icons8_history_45,
            R.drawable.icons8_feedback_40,
            R.drawable.icons8_notification_50,
            R.drawable.icons8_setting_50,
            R.drawable.icons8_sos_48,
            R.drawable.icons8_share_48,
            R.drawable.icons8_about_48,
            R.drawable.icons8_logout_40,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ride_booking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navList = findViewById(R.id.nav_list);
        btnMenu = findViewById(R.id.btnMenu);

        CustomDrawerAdapter adapter = new CustomDrawerAdapter(this, menuItems, menuIcons);
        navList.setAdapter(adapter);

        navList.setOnItemClickListener((parent, view, position, id) -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            Intent intent = null;
            switch (position) {
                case 0: intent = new Intent(this, ProfileActivity.class); break;
                case 1: intent = new Intent(this, WalletActivity.class); break;
                case 2: intent = new Intent(this, HistoryActivity.class); break;
                case 3: intent = new Intent(this, FeedbackActivity.class); break;
                case 4: intent = new Intent(this, NotificationActivity.class); break;
                case 5: intent = new Intent(this, SettingsActivity.class); break;
                case 6: intent = new Intent(this, HelpActivity.class); break;
                case 7: intent = new Intent(this, ReferralActivity.class); break;
                case 8: intent = new Intent(this, AboutActivity.class); break;
                case 9:
                    // Show logout dialog instead of opening new activity
                    showLogoutDialog();
                    break;
            }
            if (intent != null) startActivity(intent);
        });

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        etFrom = findViewById(R.id.From);
        etTo = findViewById(R.id.To);
        tvSelectedDate = findViewById(R.id.tvDate);
        confirmBtn = findViewById(R.id.confirmbtn);
        ivCalendar = findViewById(R.id.ivCalendar);
        tvPassengerCount = findViewById(R.id.tvPassengerCount);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        backbtn = findViewById(R.id.backbtn);

        btnMorning = findViewById(R.id.btnMorning);
        btnAfternoon = findViewById(R.id.btnAfternoon);
        btnEvening = findViewById(R.id.btnEvening);
        btnNight = findViewById(R.id.btnNight);

        db = FirebaseFirestore.getInstance();

        backbtn.setOnClickListener(view -> {
            startActivity(new Intent(RideBooking.this, LogInPage.class));
            finish();
        });

        tvPassengerCount.setText(String.valueOf(passengerCount));

        ivCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RideBooking.this,
                    (view, year, month, day) -> {
                        selectedDate = day + "/" + (month + 1) + "/" + year;
                        tvSelectedDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        View.OnClickListener timeClickListener = view -> {
            Button selectedBtn = (Button) view;
            selectedTime = selectedBtn.getText().toString();
            Toast.makeText(this, "Selected time: " + selectedTime, Toast.LENGTH_SHORT).show();
            resetTimeButtonStyles();
            selectedBtn.setBackgroundResource(R.drawable.time_button);
            selectedBtn.setTextColor(getResources().getColor(android.R.color.white));
        };

        btnMorning.setOnClickListener(timeClickListener);
        btnAfternoon.setOnClickListener(timeClickListener);
        btnEvening.setOnClickListener(timeClickListener);
        btnNight.setOnClickListener(timeClickListener);

        btnPlus.setOnClickListener(v -> {
            if (passengerCount < 10) {
                passengerCount++;
                tvPassengerCount.setText(String.valueOf(passengerCount));
            }
        });

        btnMinus.setOnClickListener(v -> {
            if (passengerCount > 1) {
                passengerCount--;
                tvPassengerCount.setText(String.valueOf(passengerCount));
            }
        });

        confirmBtn.setOnClickListener(v -> {
            String from = etFrom.getText().toString().trim();
            String to = etTo.getText().toString().trim();

            if (from.isEmpty() || to.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(RideBooking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> rideData = new HashMap<>();
                rideData.put("from", from);
                rideData.put("to", to);
                rideData.put("date", selectedDate);
                rideData.put("time", selectedTime);
                rideData.put("passengers", passengerCount);
                rideData.put("route", from + " to " + to); // for SearchRide
                rideData.put("price", "₹150"); // optional dummy price

                db.collection("Rides")
                        .add(rideData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(RideBooking.this, "Ride booked!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RideBooking.this, SearchRide.class);
                            intent.putExtra("from", from);
                            intent.putExtra("to", to);
                            intent.putExtra("date", selectedDate);
                            intent.putExtra("time", selectedTime);
                            intent.putExtra("passengers", passengerCount);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(RideBooking.this, "Failed to book ride: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void resetTimeButtonStyles() {
        Button[] timeButtons = {btnMorning, btnAfternoon, btnEvening, btnNight};
        for (Button btn : timeButtons) {
            btn.setBackgroundResource(R.drawable.time_button_default);
            btn.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(RideBooking.this, LogInPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
