package com.technopradyumn.evstationmap.model;

public class StationModel {
    private String stationId;
    private String name;
    private int availableChargingPoints;
    private double latitude;
    private double longitude;
    private String supplierName;

    public StationModel() {
    }

    public StationModel(String stationId, String name, int availableChargingPoints, double latitude, double longitude, String supplierName) {
        this.stationId = stationId;
        this.name = name;
        this.availableChargingPoints = availableChargingPoints;
        this.latitude = latitude;
        this.longitude = longitude;
        this.supplierName = supplierName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableChargingPoints() {
        return availableChargingPoints;
    }

    public void setAvailableChargingPoints(int availableChargingPoints) {
        this.availableChargingPoints = availableChargingPoints;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}