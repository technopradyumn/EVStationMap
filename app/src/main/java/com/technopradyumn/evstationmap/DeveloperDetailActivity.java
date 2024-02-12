package com.technopradyumn.evstationmap;

import static com.technopradyumn.evstationmap.model.Constants.openLinkInBrowser;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.technopradyumn.evstationmap.databinding.ActivityDeveloperDetailBinding;

public class DeveloperDetailActivity extends AppCompatActivity {

    private ActivityDeveloperDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        binding = ActivityDeveloperDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.pradyumnGithub.setOnClickListener(v -> {
            openLinkInBrowser(this, "https://github.com/technopradyumn");
        });

        binding.pradyumnLinkedIn.setOnClickListener(v -> {
            openLinkInBrowser(this, "https://www.linkedin.com/in/pradyumn-prajapati-1755021a9/");
        });

//        binding.pradyumnGithub.setOnClickListener(v -> {
//            openLinkInBrowser(this, "");
//        });
//
//        binding.pradyumnLinkedIn.setOnClickListener(v -> {
//            openLinkInBrowser(this, "");
//        });
//
//        binding.pradyumnGithub.setOnClickListener(v -> {
//            openLinkInBrowser(this, "");
//        });
//
//        binding.pradyumnLinkedIn.setOnClickListener(v -> {
//            openLinkInBrowser(this, "");
//        });

    }
}