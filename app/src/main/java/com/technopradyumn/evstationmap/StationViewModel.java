package com.technopradyumn.evstationmap;

// StationViewModel.java
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import com.technopradyumn.evstationmap.model.StationModel;
import com.technopradyumn.evstationmap.repository.StationRepository;

import java.util.List;

public class StationViewModel extends ViewModel {
    private StationRepository stationRepository;

    public StationViewModel() {
        stationRepository = new StationRepository();
    }

    public LiveData<List<StationModel>> getStations() {
        return stationRepository.getStations();
    }
}
