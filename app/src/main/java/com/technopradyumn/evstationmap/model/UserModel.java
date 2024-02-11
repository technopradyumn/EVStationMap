package com.technopradyumn.evstationmap.model;

public class UserModel {
    private String userID;
    private String name;
    private String email;
    private String carName;
    private String imageUrl; // New field for profile image URL

    public UserModel() {}

    public UserModel(String userID, String name, String email, String carName, String imageUrl) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.carName = carName;
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}