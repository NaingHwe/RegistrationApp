package com.datainsights.trainingapp.OpeningCourse;

import java.io.Serializable;
import java.sql.Date;

public class OpeningCourseList implements Serializable {
    private String name;
    private String imageUrl;
    private String dateData;

    public OpeningCourseList(String name, String imageUrl, String dateData) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.dateData = dateData;
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

    public String getDateData() {
        return dateData;
    }

    public void setDateData(String dateData) {
        this.dateData = dateData;
    }
}
