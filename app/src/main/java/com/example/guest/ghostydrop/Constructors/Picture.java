package com.example.guest.ghostydrop.Constructors;


import java.util.ArrayList;

//this is my constructor for every picture that is taken with my app will contain these properties//
public class Picture {
    String caption;
    String imageUrl;
    String latitude;
    String longitude;
    String ownerUid;
    ArrayList<String> comments;

    public Picture(String caption, String imageUrl, String latitude, String longitude, String ownerUid, ArrayList<String> comments) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerUid = ownerUid;
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

    public String getOwnerUid() {
        return ownerUid;
    }

    public ArrayList<String> getComments() {
        return comments;
    }
}

