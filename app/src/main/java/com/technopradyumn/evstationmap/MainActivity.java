package com.technopradyumn.evstationmap;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationBarView;
import com.technopradyumn.evstationmap.databinding.ActivityMainBinding;
import com.technopradyumn.evstationmap.fragments.HomeFragment;
import com.technopradyumn.evstationmap.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

//        binding.fab.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, SlotBookingActivity.class);
//            startActivity(intent);
//        });

        binding.moreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MoreActivity.class);
            startActivity(intent);
        });

        binding.bottomNavView.setOnItemSelectedListener((NavigationBarView.OnItemSelectedListener) item -> {
            int menuItemId = item.getItemId();

            if (menuItemId == R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (menuItemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
                return true;
            }else{
                Intent intent = new Intent(MainActivity.this, SlotBookingActivity.class);
                startActivity(intent);
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

}