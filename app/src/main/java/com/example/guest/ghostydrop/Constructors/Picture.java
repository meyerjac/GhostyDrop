package com.example.guest.ghostydrop.Constructors;


public class Picture {
    String comment;
    String imageUrl;
    String latitude;
    String longitude;

    public Picture(){};

    public Picture(String comment, String imageUrl, String latitude, String longitude) {
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getComment() {
        return comment;
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
}