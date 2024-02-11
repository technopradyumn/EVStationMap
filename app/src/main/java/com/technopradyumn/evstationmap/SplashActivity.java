package com.technopradyumn.evstationmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.technopradyumn.evstationmap.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(this::checkUserLoginStatus, SPLASH_DELAY);
    }

    private void checkUserLoginStatus() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, navigate to MainActivity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            // User is not logged in, navigate to LogInActivity
            startActivity(new Intent(SplashActivity.this, LogInActivity.class));
        }
        // Finish splash activity to prevent user from going back to it
        finish();
    }
}