package com.example.guest.ghostydrop;


public class Pictures {
    String comment;
    String imageUrl;
    String latitude;
    String longitude;

    public Pictures(String comment, String imageUrl, String latitude, String longitude) {
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