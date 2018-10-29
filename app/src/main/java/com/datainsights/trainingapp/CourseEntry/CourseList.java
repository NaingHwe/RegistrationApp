package com.datainsights.trainingapp.CourseEntry;

import java.io.Serializable;

public class CourseList implements Serializable{
    private String name;
    private String imageUrl;

    public CourseList(String name, String imageUrl) {
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
