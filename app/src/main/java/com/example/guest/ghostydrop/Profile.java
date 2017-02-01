package com.example.guest.ghostydrop;

import java.util.ArrayList;

public class Profile {
    String DisplayName;
    String Bio;
    String Age;
    ArrayList<String>FriendsList;
    ArrayList<String>CollectedPhotos;

    public Profile(String displayName, String bio, String age, ArrayList<String> friendsList, ArrayList<String> collectedPhotos) {
        DisplayName = displayName;
        Bio = bio;
        Age = age;
        FriendsList = friendsList;
        CollectedPhotos = collectedPhotos;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getBio() {
        return Bio;
    }

    public String getAge() {
        return Age;
    }

    public ArrayList<String> getFriendsList() {
        return FriendsList;
    }

    public ArrayList<String> getCollectedPhotos() {
        return CollectedPhotos;
    }
}
