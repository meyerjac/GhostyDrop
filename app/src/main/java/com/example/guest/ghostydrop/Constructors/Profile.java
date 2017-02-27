package com.example.guest.ghostydrop.Constructors;

//every profile of this MVP will contain these properties//

public class Profile {
    String firstName;
    String lastName;
    String displayName;
    String bio;
    String birthday;
    String email;
    String facebookId;
    String latitude;
    String longitude;
    Boolean firstLogin;
    String picture;
    private String pushId;

    public Profile(String firstName, String lastName, String displayName, String bio, String birthday, String email, String facebookId, String latitude, String longitude, Boolean firstLogin, String picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.bio = bio;
        this.birthday = birthday;
        this.email = email;
        this.facebookId = facebookId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.firstLogin = firstLogin;
        this.picture = picture;
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Boolean getFirstLogin() {
        return firstLogin;
    }

    public String getPicture() {
        return picture;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}


