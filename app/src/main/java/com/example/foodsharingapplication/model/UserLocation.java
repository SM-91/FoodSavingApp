package com.example.foodsharingapplication.model;


import android.location.Location;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserLocation {


    private double Latitude;
    private double Longitude;


    public UserLocation(double latitude, double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public UserLocation() {
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }
}
