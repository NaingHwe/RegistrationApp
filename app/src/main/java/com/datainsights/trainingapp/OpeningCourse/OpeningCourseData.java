package com.datainsights.trainingapp.OpeningCourse;

import java.io.Serializable;

public class OpeningCourseData implements Serializable {

    private String openingCourseId;
    private String openingCourseTitle;
    private String openingCourseDuration;
    private String openingCourseStartDate;
    private String openingCourseEndDate;
    private double openingCourseFee;
    private String openingCourseImageURL;
    private String openingCourseStartTime;
    private String openingCourseEndTime;

    public String getOpeningCourseStartTime() {
        return openingCourseStartTime;
    }

    public void setOpeningCourseStartTime(String openingCourseStartTime) {
        this.openingCourseStartTime = openingCourseStartTime;
    }

    public String getOpeningCourseEndTime() {
        return openingCourseEndTime;
    }

    public void setOpeningCourseEndTime(String openingCourseEndTime) {
        this.openingCourseEndTime = openingCourseEndTime;
    }

    public String getOpeningCourseId() {
        return openingCourseId;
    }

    public void setOpeningCourseId(String openingCourseId) {
        this.openingCourseId = openingCourseId;
    }

    public String getOpeningCourseTitle() {
        return openingCourseTitle;
    }

    public void setOpeningCourseTitle(String openingCourseTitle) {
        this.openingCourseTitle = openingCourseTitle;
    }

    public String getOpeningCourseDuration() {
        return openingCourseDuration;
    }

    public void setOpeningCourseDuration(String openingCourseDuration) {
        this.openingCourseDuration = openingCourseDuration;
    }

    public String getOpeningCourseStartDate() {
        return openingCourseStartDate;
    }

    public void setOpeningCourseStartDate(String openingCourseStartDate) {
        this.openingCourseStartDate = openingCourseStartDate;
    }

    public String getOpeningCourseEndDate() {
        return openingCourseEndDate;
    }

    public void setOpeningCourseEndDate(String openingCourseEndDate) {
        this.openingCourseEndDate = openingCourseEndDate;
    }

    public double getOpeningCourseFee() {
        return openingCourseFee;
    }

    public void setOpeningCourseFee(double openingCourseFee) {
        this.openingCourseFee = openingCourseFee;
    }

    public String getOpeningCourseImageURL() {
        return openingCourseImageURL;
    }

    public void setOpeningCourseImageURL(String openingCourseImageURL) {
        this.openingCourseImageURL = openingCourseImageURL;
    }
}
