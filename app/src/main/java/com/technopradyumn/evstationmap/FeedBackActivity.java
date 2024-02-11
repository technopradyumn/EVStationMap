package com.technopradyumn.evstationmap;

import static com.technopradyumn.evstationmap.model.Constants.FEEDBACK;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.technopradyumn.evstationmap.databinding.ActivityFeedBackBinding;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity {

    private ActivityFeedBackBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            binding.email.setText(user.getEmail());
        }

        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.submitBtn.setOnClickListener(view -> {
            saveFeedback();
        });
    }

    private void saveFeedback() {
        if (user != null) {
            String email = user.getEmail();
            String feedbackText = binding.feedbackEditText.getText().toString().trim();

            // Check if feedback text is empty
            if (feedbackText.isEmpty()) {
                Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new feedback object
            Map<String, Object> feedback = new HashMap<>();
            feedback.put("userId", user.getUid());
            feedback.put("email", email);
            feedback.put("feedbackText", feedbackText);

            // Add the feedback to Firestore
            db.collection(FEEDBACK)
                    .add(feedback)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                binding.feedbackEditText.setText("");
                                Toast.makeText(FeedBackActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("FeedBackActivity", "Error adding feedback", task.getException());
                                Toast.makeText(FeedBackActivity.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


}