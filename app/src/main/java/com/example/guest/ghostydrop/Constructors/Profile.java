package com.example.guest.ghostydrop.Constructors;

import java.util.ArrayList;

    public class Profile {
        String firstName;
        String lastName;
        String displayName;
        String bio;
        String birthday;
        String email;
        String facebookId;
        String picture;
        private String pushId;
        ArrayList<String> CollectedPhotos;

        public Profile(String firstName, String lastName, String displayName, String bio, String birthday, String email, String facebookId, String picture, ArrayList<String> collectedPhotos) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.displayName = displayName;
            this.bio = bio;
            this.birthday = birthday;
            this.email = email;
            this.facebookId = facebookId;
            this.picture = picture;
            CollectedPhotos = collectedPhotos;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getBio() {
            return bio;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getEmail() {
            return email;
        }

        public String getFacebookId() {
            return facebookId;
        }

        public String getPicture() {
            return picture;
        }

        public String getPushId() {
            return pushId;
        }

        public ArrayList<String> getCollectedPhotos() {
            return CollectedPhotos;
        }

        public void setPushId(String pushId) {
            this.pushId = pushId;
        }
    }

