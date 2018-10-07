package com.datainsights.trainingapp.StudentRegistration;

import java.io.Serializable;

public class StudentList implements Serializable {
    private String name;
    private String imageUrl;

    public StudentList(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
