package com.technopradyumn.evstationmap.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.technopradyumn.evstationmap.FullImageActivity;
import com.technopradyumn.evstationmap.LogInActivity;
import com.technopradyumn.evstationmap.MyBookedSlotActivity;
import com.technopradyumn.evstationmap.R;
import com.technopradyumn.evstationmap.databinding.FragmentProfileBinding;
import com.technopradyumn.evstationmap.model.UserModel;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ImageView changeImage;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private Uri getUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        changeImage = binding.changeImage;

        binding.logOutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        binding.imageView.setOnClickListener(v -> {
            if (getUri != null) {
                // Create a bundle for shared element transition
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(),
                        v, "imageTransition");

                // Start FullImageActivity and pass the image URI with shared element transition
                Intent intent = new Intent(requireContext(), FullImageActivity.class);
                intent.putExtra("imageUri", getUri);
                startActivity(intent, options.toBundle());
            } else {
                Toast.makeText(getContext(), "Image not available", Toast.LENGTH_SHORT).show();
            }
        });

        binding.mySlotBtn.setOnClickListener(v ->{
            Intent intent = new Intent(requireContext(), MyBookedSlotActivity.class);
            startActivity(intent);
        });

        changeImage.setOnClickListener(v -> openGallery());

        loadDataFromFirestore();

        return view;
    }

    private void loadDataFromFirestore() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserModel userModel = document.toObject(UserModel.class);
                            if (userModel != null) {
                                binding.name.setText(userModel.getName());
                                binding.email.setText(userModel.getEmail());
                                binding.carName.setText(userModel.getCarName());
                                binding.userId.setText(userModel.getUserID());
                                if (userModel.getImageUrl() != null && !userModel.getImageUrl().isEmpty()) {
                                    getUri = Uri.parse(userModel.getImageUrl());
                                    Glide.with(requireContext())
                                            .load(userModel.getImageUrl())
                                            .placeholder(R.drawable.baseline_person_24)
                                            .into(binding.imageView);
                                }
                            }
                        } else {
                            // Document does not exist
                        }
                    } else {
                        // Error fetching document
                        Toast.makeText(getContext(), "Failed to load user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImage();
        } else {
            Toast.makeText(getContext(), "Image selection cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUri.addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            updateProfileImage(imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateProfileImage(String imageUrl) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .update("imageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                    // Reload user data to display updated profile image
                    loadDataFromFirestore();
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful update
                    Toast.makeText(getContext(), "Failed to update profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getFileExtension(Uri uri) {
        // Get file extension from Uri
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }
}