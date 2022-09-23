package com.example.organizze;

import android.os.Bundle;

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
                .canGoForward(false)
                .build()
        );
    }
}