package com.technopradyumn.evstationmap.model;

import java.util.Date;

public class BookedSlot {
    private String slotId;
    private String userId;
    private String car;
    private String chargerType;
    private Date bookingTime;
    private Date expiryTime; // Add a field for expiry time

    public BookedSlot() {
        // Required empty public constructor for Firestore
    }

    public BookedSlot(String slotId, String userId, String car, String chargerType, Date bookingTime, Date expiryTime) {
        this.slotId = slotId;
        this.userId = userId;
        this.car = car;
        this.chargerType = chargerType;
        this.bookingTime = bookingTime;
        this.expiryTime = expiryTime;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getChargerType() {
        return chargerType;
    }

    public void setChargerType(String chargerType) {
        this.chargerType = chargerType;
    }

    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }
}