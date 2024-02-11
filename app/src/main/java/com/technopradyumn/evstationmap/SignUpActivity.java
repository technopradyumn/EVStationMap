package com.technopradyumn.evstationmap;

import static com.technopradyumn.evstationmap.model.Constants.IMAGE_URL;
import static com.technopradyumn.evstationmap.model.Constants.USERS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.technopradyumn.evstationmap.databinding.ActivitySignUpBinding;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.technopradyumn.evstationmap.model.UserModel;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.signUpBtn.setOnClickListener(view -> {

            String email = binding.emailEdt.getText().toString().trim();
            String password = binding.passwordEdt.getText().toString().trim();
            String name = binding.nameEdt.getText().toString().trim();
            String carName = binding.carNameEdt.getText().toString().trim();


            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || carName.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }else{
                binding.progressBar.setVisibility(View.VISIBLE);
                signUp(email,password,name,carName);
            }

        });
    }

    private void signUp(String email, String password, String name, String carName) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up successful, save additional user data to Firestore
                        String userId = mAuth.getCurrentUser().getUid();
                        UserModel user = new UserModel(userId,name, email, carName,IMAGE_URL);
                        mFirestore.collection(USERS).document(userId)
                                .set(user)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to save user data: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                })
                                .addOnFailureListener(task3 ->{

                                });
                    } else {
                        // Sign up failed
                        Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
