package com.datainsights.trainingapp.StudentRegistration;

import java.io.Serializable;

public class StudentData implements Serializable{

    private String userId;
    private int id;
    private String name;
    private long firstPhone;
    private long secondPhone;
    private String email;
    private String gender;
    private String profileImageURL;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFirstPhone() {
        return firstPhone;
    }

    public void setFirstPhone(long firstPhone) {
        this.firstPhone = firstPhone;
    }

    public long getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(long secondPhone) {
        this.secondPhone = secondPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
