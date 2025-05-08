package com.example.mad_project;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class ReferralActivity extends AppCompatActivity {

    String referralCode = "WXYACG123"; // You can generate dynamically per user if needed
    TextView txtReferralCode, txtCopyCode;
    ImageView telegram, whatsapp, facebook, instagram, backButton;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_referral);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        txtReferralCode = findViewById(R.id.referralCode);
        txtCopyCode = findViewById(R.id.copyCode);
        telegram = findViewById(R.id.shareTelegram);
        whatsapp = findViewById(R.id.shareWhatsApp);
        facebook = findViewById(R.id.shareFacebook);
        instagram = findViewById(R.id.shareInstagram);
        backButton = findViewById(R.id.backbtn);

        txtReferralCode.setText(referralCode);

        txtCopyCode.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Referral Code", referralCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ReferralActivity.this, "Referral code copied!", Toast.LENGTH_SHORT).show();
        });

        telegram.setOnClickListener(v -> shareCode("org.telegram.messenger", "Telegram"));
        whatsapp.setOnClickListener(v -> shareCode("com.whatsapp", "WhatsApp"));
        facebook.setOnClickListener(v -> shareCode("com.facebook.katana", "Facebook"));
        instagram.setOnClickListener(v -> shareCode("com.instagram.android", "Instagram"));

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReferralActivity.this, RideBooking.class);
            startActivity(intent);
            finish();
        });
    }

    private void shareCode(String packageName, String appName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Use my referral code " + referralCode +
                " to sign up and earn 100 loyalty points on GoFlow!");

        if (isAppInstalled(packageName)) {
            intent.setPackage(packageName);
            startActivity(intent);
            logReferralShare(appName);
        } else {
            Intent chooser = Intent.createChooser(intent, "Share using");
            startActivity(chooser);
            logReferralShare(appName + " (fallback)");
            Toast.makeText(this, appName + " not installed. Sharing via other apps.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void logReferralShare(String platformName) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "guest_user";

        Map<String, Object> data = new HashMap<>();
        data.put("platform", platformName);
        data.put("referralCode", referralCode);
        data.put("timestamp", System.currentTimeMillis());

        firestore.collection("ReferralShares")
                .document(userId)
                .collection("Shares")
                .add(data)
                .addOnSuccessListener(doc -> {
                    // Optional: Toast or log success
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to log referral share", Toast.LENGTH_SHORT).show();
                });
    }
}
