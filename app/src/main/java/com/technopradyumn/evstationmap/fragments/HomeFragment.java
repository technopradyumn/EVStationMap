package com.technopradyumn.evstationmap.fragments;

import static com.technopradyumn.evstationmap.model.Constants.COLLECTION_ENVIRONMENTAL_DATA;
import static com.technopradyumn.evstationmap.model.Constants.DOCUMENT_STATISTICS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.technopradyumn.evstationmap.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;
    private TextView greenhouseGasTextView, energySavingsTextView,airPollutionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        greenhouseGasTextView = binding.greenhouseGasTextView;
        energySavingsTextView = binding.energySavingsTextView;
        airPollutionTextView = binding.airPollutionTextView;

        db = FirebaseFirestore.getInstance();

        loadDataFromFirestore();

        return view;
    }

    private void loadDataFromFirestore() {
        db.collection(COLLECTION_ENVIRONMENTAL_DATA).document(DOCUMENT_STATISTICS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from Firestore document
                                String greenhouseGas = document.getString("greenhouseGas");
                                String energySavings = document.getString("energySavings");
                                String airPollution = document.getString("airPollution");

                                // Set data to respective TextViews
                                binding.greenhouseGasTextView.setText("Greenhouse Gas Reduction: " + greenhouseGas);
                                binding.energySavingsTextView.setText("Energy Savings: " + energySavings);
                                binding.airPollutionTextView.setText("Air Pollution Reduction: " + airPollution);
                            } else {
                                // Handle the case where the document does not exist
                            }
                        } else {
                            // Handle failures
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}