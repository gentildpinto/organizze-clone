package com.example.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {
    private static FirebaseAuth auth;
    private static DatabaseReference dbRef;

    public static FirebaseAuth getFirebaseInstance() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static DatabaseReference getDatabaseReference() {
        if (dbRef == null) {
            dbRef = FirebaseDatabase.getInstance().getReference();
        }
        return dbRef;
    }
}
