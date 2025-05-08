package com.example.mad_project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class AddPaymentDetails extends AppCompatActivity {

    Button scanCardButton, confirmButton;
    ImageView googlePay, phonePe, paytm;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_payment_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        scanCardButton = findViewById(R.id.scanCardButton);
        confirmButton = findViewById(R.id.confirmPaymentButton);

        googlePay = findViewById(R.id.googlepay);
        phonePe = findViewById(R.id.phonepe);
        paytm = findViewById(R.id.paytmLogo);

        scanCardButton.setOnClickListener(v ->
                Toast.makeText(this, "Scan Card feature coming soon!", Toast.LENGTH_SHORT).show()
        );

        confirmButton.setOnClickListener(v -> {
            Toast.makeText(this, "Payment Confirmed!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, RIdeConfirmActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        googlePay.setOnClickListener(v -> openUpiApp("Google Pay", "com.google.android.apps.nbu.paisa.user"));
        phonePe.setOnClickListener(v -> openUpiApp("PhonePe", "com.phonepe.app"));
        paytm.setOnClickListener(v -> openUpiApp("Paytm", "net.one97.paytm"));
    }

    private void openUpiApp(String appName, String packageName) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "sk8717108@ibl") // Replace with your UPI ID
                .appendQueryParameter("pn", "GoFlow Rides")
                .appendQueryParameter("tn", "Payment for Ride")
                .appendQueryParameter("am", "00.00") // Replace with actual amount
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(packageName);

        PackageManager pm = getPackageManager();

        if (intent.resolveActivity(pm) != null) {
            try {
                startActivity(intent);
                logPaymentMethod(appName);
            } catch (Exception e) {
                Intent chooser = Intent.createChooser(new Intent(Intent.ACTION_VIEW, uri), "Pay with");
                startActivity(chooser);
                logPaymentMethod(appName + " (fallback)");
            }
        } else {
            try {
                Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(Intent.createChooser(fallbackIntent, "Pay with"));
                logPaymentMethod(appName + " (chooser fallback)");
            } catch (Exception e) {
                Toast.makeText(this, "No UPI app found to handle payment.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logPaymentMethod(String methodName) {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            Map<String, Object> data = new HashMap<>();
            data.put("method", methodName);
            data.put("timestamp", System.currentTimeMillis());

            firestore.collection("PaymentLogs")
                    .document(userId)
                    .collection("Attempts")
                    .add(data)
                    .addOnSuccessListener(doc -> Toast.makeText(this, "Logged: " + methodName, Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to log payment method", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not logged in. Cannot log payment.", Toast.LENGTH_SHORT).show();
        }
    }
}
