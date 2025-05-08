package com.example.mad_project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WalletActivity extends AppCompatActivity {

    private TextView totalBalanceAmount, walletTopupDate, transactionHistoryLabel;
    private Button topUpButton;
    private ImageView backbtn;
    private RecyclerView historyRecyclerView;
    private TopUpHistoryAdapter adapter;
    private List<String> historyList;

    private double totalBalance = 0.0;
    private double customAmount = 100.0;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        totalBalanceAmount = findViewById(R.id.totalBalanceAmount);
        walletTopupDate = findViewById(R.id.walletTopupDate);
        topUpButton = findViewById(R.id.topUpButton);
        transactionHistoryLabel = findViewById(R.id.transactionHistoryLabel);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        backbtn = findViewById(R.id.backbtn);

        historyList = new ArrayList<>();
        adapter = new TopUpHistoryAdapter(historyList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            loadWalletFromFirestore();
        }

        backbtn.setOnClickListener(view -> {
            Intent intent = new Intent(WalletActivity.this, RideBooking.class);
            startActivity(intent);
            finish();
        });

        totalBalanceAmount.setOnClickListener(v -> showAmountInputDialog());

        topUpButton.setOnClickListener(v -> performTopUp(customAmount));

        transactionHistoryLabel.setOnClickListener(v -> {
            transactionHistoryLabel.setSelected(true);
            transactionHistoryLabel.setTextColor(getResources().getColor(android.R.color.black));
            Toast.makeText(this, "Transaction History Selected", Toast.LENGTH_SHORT).show();
        });
    }

    private void showAmountInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Top-up Amount");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("e.g. 50.00");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            try {
                customAmount = Double.parseDouble(input.getText().toString());
                Toast.makeText(this, "Top-up set to ₹" + customAmount, Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void performTopUp(double amount) {
        totalBalance += amount;
        updateWallet(amount);
        saveWalletToFirestore(totalBalance);
    }

    private void updateWallet(double amount) {
        totalBalanceAmount.setText("₹ " + String.format(Locale.getDefault(), "%.2f", totalBalance));

        String formattedDate = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(new Date());
        walletTopupDate.setText(formattedDate);

        String transaction = "Added ₹" + String.format(Locale.getDefault(), "%.2f", amount) + " on " + formattedDate;
        historyList.add(0, transaction);
        adapter.notifyItemInserted(0);
        historyRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadWalletFromFirestore() {
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("walletBalance")) {
                totalBalance = documentSnapshot.getDouble("walletBalance");
                totalBalanceAmount.setText("₹ " + String.format(Locale.getDefault(), "%.2f", totalBalance));
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load wallet: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void saveWalletToFirestore(double updatedBalance) {
        db.collection("Users").document(userId)
                .update("walletBalance", updatedBalance)
                .addOnSuccessListener(unused -> {
                    // Successfully updated
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update wallet: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
