package com.example.guest.ghostydrop.Constructors;

public class likeObject {
    String likeOwnerUid;
    private String pushId;

    public likeObject() {
    }

    public likeObject(String likeOwnerUid) {
        this.likeOwnerUid = likeOwnerUid;
    }

    public String getLikeOwnerUid() {
        return likeOwnerUid;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}