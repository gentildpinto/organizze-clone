package com.example.organizze.model;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.organizze.config.FirebaseConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class User {
    public String name, email;
    public Double totalIncome = 0.0, totalExpense = 0.0;
    private final String id;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = Base64.encodeToString(email.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public void save() {
        DatabaseReference dbRef = FirebaseConfig.GetDatabaseReference();
        dbRef.child("users")
                .child(this.id)
                .setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error setting data", task.getException());
                        }
                    }
                });
    }
}
