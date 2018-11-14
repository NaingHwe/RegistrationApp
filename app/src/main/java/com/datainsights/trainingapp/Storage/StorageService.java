package com.datainsights.trainingapp.Storage;

import com.datainsights.trainingapp.CourseEntry.CourseData;
import com.datainsights.trainingapp.StudentRegistration.StudentData;

public interface StorageService {

    void insertStudentData(StudentData studentData, InsertCallback callback);
    void closeConnection();

    void insertCourseData(CourseData courseData,InsertCallback callback);

}
