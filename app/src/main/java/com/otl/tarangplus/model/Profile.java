package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Profile implements Parcelable {

    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("child")
    @Expose
    private boolean child;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("default_profile")
    @Expose
    private boolean isDefaultProfile;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("mobile_number")
    @Expose
    private String mobile;
    @SerializedName("user_mail")
    @Expose
    private String usermail;

    public Profile() {

    }

    public Profile(Parcel in) {
        profileId = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        age = in.readString();
        profilePicture = in.readString();
        gender = in.readString();
        isDefaultProfile = in.readByte() != 0;
        dob = in.readString();
        mobile = in.readString();
        usermail = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean isDefaultProfile() {
        return isDefaultProfile;
    }

    public void setDefaultProfile(boolean defaultProfile) {
        isDefaultProfile = defaultProfile;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(profileId, profile.profileId) &&
                Objects.equals(firstname, profile.firstname) &&
                Objects.equals(lastname, profile.lastname) &&
                Objects.equals(age, profile.age) &&
                Objects.equals(profilePicture, profile.profilePicture) &&
                Objects.equals(gender, profile.gender);
    }

    @Override
    public int hashCode() {

        return Objects.hash(profileId, firstname, lastname, age, profilePicture, child, gender);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileId='" + profileId + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age='" + age + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", child=" + child +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(profileId);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
        parcel.writeString(age);
        parcel.writeString(profilePicture);
        parcel.writeString(gender);
        parcel.writeByte((byte) (isDefaultProfile ? 1 : 0));
        parcel.writeString(dob);
        parcel.writeString(mobile);
        parcel.writeString(usermail);
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }

    public static Creator<Profile> getCREATOR() {
        return CREATOR;
    }
}