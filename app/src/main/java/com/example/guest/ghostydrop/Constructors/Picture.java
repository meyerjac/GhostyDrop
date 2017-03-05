package com.example.guest.ghostydrop.Constructors;


//this is my constructor for every picture that is taken with my app will contain these properties//
public class Picture {
    String caption;
    String imageUrl;
    String latitude;
    String longitude;
    String ownerUid;
    private String pushId;

    //constructore with no arguments
    public Picture() {
    }

    public Picture(String caption, String imageUrl, String latitude, String longitude, String ownerUid) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerUid = ownerUid;
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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}

