package com.example.organizze.util;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Util {
    @NonNull
    public static String encodeBase64(String text) {
        text = Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
        return text;
    }

    public static String getCurrentDate() {
        long current = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        return format.format(current);
    }
}
