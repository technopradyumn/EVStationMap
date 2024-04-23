package com.technopradyumn.evstationmap.fragments;

import static com.technopradyumn.evstationmap.model.Constants.COLLECTION_ENVIRONMENTAL_DATA;
import static com.technopradyumn.evstationmap.model.Constants.DOCUMENT_STATISTICS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technopradyumn.evstationmap.MapsActivity;
import com.technopradyumn.evstationmap.R;
//import com.technopradyumn.evstationmap.adapter.StationAdapter;
import com.technopradyumn.evstationmap.adapter.StationAdapter;
import com.technopradyumn.evstationmap.databinding.FragmentHomeBinding;
import com.technopradyumn.evstationmap.model.StationModel;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;
    private TextView greenhouseGasTextView, energySavingsTextView,airPollutionTextView;
    private RecyclerView recyclerView;
    private StationAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.mapActivityBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MapsActivity.class));
        });

        db = FirebaseFirestore.getInstance();

//        loadDataFromFirestore();

        firestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StationAdapter();
        recyclerView.setAdapter(adapter);

        loadDataFromFirestore();

        return view;
    }

    private void loadDataFromFirestore() {
        // Assuming you have a "stations" collection in Firestore
        firestore.collection("stations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<StationModel> stations = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        StationModel station = document.toObject(StationModel.class);
                        stations.add(station);
                    }
                    adapter.setStations(stations);
                })
                .addOnFailureListener(e -> {

                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}