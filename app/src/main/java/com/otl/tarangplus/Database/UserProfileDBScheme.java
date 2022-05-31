package com.otl.tarangplus.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "userProfile")
public class UserProfileDBScheme {
    @PrimaryKey
    @NonNull
    private int profile_id;
    private String firstName;
    private String lastName;
    private int age;
    private String profilePicture;
    private String gender;
    private boolean childProfile;

    @Ignore
    public UserProfileDBScheme() {
    }

    public UserProfileDBScheme(@NonNull int profile_id, String firstName, String lastName, int age, String profilePicture, String gender, boolean childProfile) {
        this.profile_id = profile_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.profilePicture = profilePicture;
        this.gender = gender;
        this.childProfile = childProfile;
    }

    @NonNull
    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(@NonNull int profile_id) {
        this.profile_id = profile_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isChildProfile() {
        return childProfile;
    }

    public void setChildProfile(boolean childProfile) {
        this.childProfile = childProfile;
    }
}
