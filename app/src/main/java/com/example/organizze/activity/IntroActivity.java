package com.example.organizze.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.organizze.R;
import com.example.organizze.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import java.util.ArrayList;

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {
    private static final String SHARED_PREFERENCES = "OrganizzePrefs";
    private static final String SHARED_PREFERENCES_FIRST_OPEN_KEY = "FirstOpen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUser();

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        ArrayList<FragmentSlide> slides = buildSlides();
        addSlides(slides);
    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void signIn(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }

    private ArrayList<FragmentSlide> buildSlides() {
        ArrayList<FragmentSlide> slides = new ArrayList<>();
        slides.add(new FragmentSlide.Builder()
                .fragment(R.layout.intro_one)
                .background(android.R.color.white)
                .build());
        slides.add(new FragmentSlide.Builder()
                .fragment(R.layout.intro_two)
                .background(android.R.color.white)
                .build()
        );
        slides.add(new FragmentSlide.Builder()
                .fragment(R.layout.intro_three)
                .background(android.R.color.white)
                .build()
        );
        slides.add(new FragmentSlide.Builder()
                .fragment(R.layout.intro_four)
                .background(android.R.color.white)
                .build()
        );
        slides.add(new FragmentSlide.Builder()
                .fragment(R.layout.intro_auth)
                .background(android.R.color.white)
                .canGoForward(false)
                .build()
        );
        return slides;
    }

    // It verifies if the app is opened for the first time
    // If not skip this activity
    private void skipActivityAfterFirstOpen() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        if (preferences.contains(SHARED_PREFERENCES_FIRST_OPEN_KEY)
                && preferences.getBoolean(SHARED_PREFERENCES_FIRST_OPEN_KEY, false)
        ) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SHARED_PREFERENCES_FIRST_OPEN_KEY, true);
            editor.apply();
        }
    }

    // Checks if exists authenticated user
    private void checkUser() {
        FirebaseUser user = FirebaseConfig.GetFirebaseInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            skipActivityAfterFirstOpen();
        }
    }
}