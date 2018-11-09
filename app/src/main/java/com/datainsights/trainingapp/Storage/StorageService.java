package com.datainsights.trainingapp.Storage;

import com.datainsights.trainingapp.StudentRegistration.StudentData;

public interface StorageService {

    void insertStudentData(StudentData studentData, InsertStudentCallback callback);
    void closeConnection();
}
