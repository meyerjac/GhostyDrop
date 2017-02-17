package com.example.guest.ghostydrop.Constructors;


import java.util.ArrayList;

public class Picture {
    String caption;
    String imageUrl;
    String latitude;
    String longitude;
    String uid;
    ArrayList<String> comments;

    public Picture(String caption, String imageUrl, String latitude, String longitude, String ownerUid, ArrayList<String> comments) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = uid;
        this.comments = comments;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String uid() {
        return uid;
    }

    public ArrayList<String> getComments() {
        return comments;
    }
}

