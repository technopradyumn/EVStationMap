package com.technopradyumn.evstationmap.repository;

// StationRepository.java
import androidx.lifecycle.LiveData;

import com.technopradyumn.evstationmap.model.StationDataSource;
import com.technopradyumn.evstationmap.model.StationModel;
import java.util.List;

public class StationRepository {
    private StationDataSource stationDataSource;

    public StationRepository() {
        stationDataSource = new StationDataSource();
    }

    public LiveData<List<StationModel>> getStations() {
        return stationDataSource.getStations();
    }
}
