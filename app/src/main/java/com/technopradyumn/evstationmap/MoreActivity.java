package com.technopradyumn.evstationmap;

import static com.technopradyumn.evstationmap.model.Constants.openLinkInBrowser;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.technopradyumn.evstationmap.databinding.ActivityMoreBinding;

public class MoreActivity extends AppCompatActivity {
    ActivityMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(iew -> {
            onBackPressed();
        });

        binding.privacyPolicyTxt.setOnClickListener(view -> {
            Intent intent = new Intent(MoreActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        binding.feedbackTxt.setOnClickListener(view -> {
            Intent intent = new Intent(MoreActivity.this, FeedBackActivity.class);
            startActivity(intent);
        });

        binding.developerTxt.setOnClickListener(v -> {
            Intent intent = new Intent(MoreActivity.this, DeveloperDetailActivity.class);
            startActivity(intent);
        });

    }
}