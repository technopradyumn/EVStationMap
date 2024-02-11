package com.technopradyumn.evstationmap.model;

// StationDataSource.java
import static com.technopradyumn.evstationmap.model.Constants.STATIONS;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technopradyumn.evstationmap.model.StationModel;
import java.util.ArrayList;
import java.util.List;

public class StationDataSource {
    private FirebaseFirestore firestore;
    public StationDataSource() {
        firestore = FirebaseFirestore.getInstance();
    }
    public LiveData<List<StationModel>> getStations() {
        MutableLiveData<List<StationModel>> stationsLiveData = new MutableLiveData<>();

        firestore.collection(STATIONS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<StationModel> stations = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            StationModel station = document.toObject(StationModel.class);
                            stations.add(station);
                        }
                        stationsLiveData.setValue(stations);
                    }
                });

        return stationsLiveData;
    }
}

