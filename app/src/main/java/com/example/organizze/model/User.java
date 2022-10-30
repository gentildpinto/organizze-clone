package com.example.organizze.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.organizze.config.FirebaseConfig;
import com.example.organizze.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class User {
    public String name, email;
    public Double totalIncome = 0.0, totalExpense = 0.0;
    private String id;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = Util.encodeBase64(email);
    }

    public User() {}

    public void save() {
        DatabaseReference dbRef = FirebaseConfig.getDatabaseReference();
        dbRef.child("users")
                .child(this.id)
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error setting user data", task.getException());
                        }
                    }
                });
    }

    public static DatabaseReference getUserReference() {
        String email = Objects.requireNonNull(FirebaseConfig.getFirebaseInstance().getCurrentUser()).getEmail();
        String id = Util.encodeBase64(email);

        return FirebaseConfig.getDatabaseReference().child("users").child(id);
    }
}
