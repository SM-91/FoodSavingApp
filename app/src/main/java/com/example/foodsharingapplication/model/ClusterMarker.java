package com.example.foodsharingapplication.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;

public class ClusterMarker implements ClusterItem {

    private LatLng position; // required field
    private String title; // required field
    private String snippet; // required field
    private String iconPicture;
    private ArrayList<String> mArrayString;
    private String imageFromArray;
    private User user;

    public ClusterMarker() {
    }

    public ClusterMarker(LatLng position, String title, String snippet, String iconPicture, String imageFromArray, User user) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.iconPicture = iconPicture;
        this.imageFromArray = imageFromArray;
        this.user = user;
    }

    public String getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(String iconPicture) {
        this.iconPicture = iconPicture;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getmArrayString() {
        return imageFromArray;
    }

    public void setmArrayString(ArrayList<String> mArrayString) {
        imageFromArray = mArrayString.get(0);
        //this.mArrayString = mArrayString;
    }


}
