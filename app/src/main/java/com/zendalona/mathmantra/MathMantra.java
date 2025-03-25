package com.zendalona.mathmantra;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class MathMantra extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enforce Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
