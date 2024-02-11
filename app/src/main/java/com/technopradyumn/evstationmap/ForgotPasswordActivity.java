package com.technopradyumn.evstationmap;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.protobuf.Empty;
import com.technopradyumn.evstationmap.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();

        binding.forgotPassword.setOnClickListener(v -> {

            if(binding.emailEdt.getText() == null || binding.emailEdt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
            } else {
                String emailAddress = String.valueOf(binding.emailEdt.getText());

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Email sent.", Toast.LENGTH_LONG).show();
                            } else {
                                // If the task is not successful, retrieve the exception and show an appropriate error message
                                Exception e = task.getException();
                                if (e != null) {
                                    String errorMessage = e.getMessage();
                                    // Show the error message in a toast
                                    Toast.makeText(ForgotPasswordActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                } else {
                                    // If no specific error message is available, show a generic error message
                                    Toast.makeText(ForgotPasswordActivity.this, "Password reset email could not be sent.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }


        });


    }
}