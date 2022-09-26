package com.example.organizze;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.organizze.activity.SignInActivity;
import com.example.organizze.activity.SignUpActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_one)
                .background(android.R.color.white)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_two)
                .background(android.R.color.white)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_three)
                .background(android.R.color.white)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_four)
                .background(android.R.color.white)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .fragment(R.layout.intro_auth)
                .background(android.R.color.white)
                .canGoForward(false)
                .build()
        );
    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void signIn(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }
}