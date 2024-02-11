package com.technopradyumn.evstationmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.technopradyumn.evstationmap.databinding.ActivityLogInBinding;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        binding.forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        binding.signUp.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.logInBtn.setOnClickListener(view -> {
            String email = binding.emailEdt.getText().toString().trim();
            String password = binding.passwordEdt.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LogInActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Show progress bar
                binding.progressBar.setVisibility(View.VISIBLE);

                // Sign in user with email and password
                signInWithEmailAndPassword(email, password);
            }
        });
    }

    private void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LogInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Hide progress bar
                        binding.progressBar.setVisibility(View.INVISIBLE);

                        // Navigate to MainActivity
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LogInActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        // Hide progress bar
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }



}
