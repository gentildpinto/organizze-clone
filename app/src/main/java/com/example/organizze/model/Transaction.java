package com.example.organizze.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.organizze.config.FirebaseConfig;
import com.example.organizze.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Transaction {
    @Exclude
    public static final String INCOME_TRANSACTION_TYPE = "i", EXPENSE_TRANSACTION_TYPE = "e";

    public String category, date, description, type, key;
    public Double value;

    public Transaction(String category, String date, String description, String type, Double value) {
        this.category = category;
        this.date = date;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    public Transaction() {}

    public void save() {
        String currentUserEmail = FirebaseConfig.getFirebaseInstance().getCurrentUser().getEmail();
        DatabaseReference reference = FirebaseConfig.getDatabaseReference();
        String[] dateParts = this.date.split("/");
        String date = dateParts[1] + dateParts[2];

        reference.child("transactions")
                .child(Util.encodeBase64(currentUserEmail))
                .child(date)
                .push()
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error setting transaction data", task.getException());
                        }
                    }
                });
    }
}
