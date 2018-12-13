package com.datainsights.trainingapp.Storage;

import android.support.annotation.NonNull;

import com.datainsights.trainingapp.CourseEntry.CourseData;
import com.datainsights.trainingapp.OpeningCourse.OpeningCourseData;
import com.datainsights.trainingapp.StudentRegistration.StudentData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseStorageClass implements StorageService {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference studentRef = database.getReference("students");
    private DatabaseReference courseRef = database.getReference("courses");
    private DatabaseReference openCourseRef = database.getReference("OpenCourses");

    @Override
    public void insertStudentData(StudentData studentData, final InsertCallback callback) {

        studentRef.push().setValue(studentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess("Successful insert");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("Fail to insert");
                    }
                });
    }

    @Override
    public void updateStudentData(StudentData studentData, final InsertCallback callback) {
        studentRef.child(studentData.getUserId()).setValue(studentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess("Successful insert");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("Fail to insert");
                    }
                });
    }

    @Override
    public void insertCourseData(CourseData courseData, final InsertCallback callback) {
        courseRef.push().setValue(courseData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess("Successful insert");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("Fail to insert");
                    }
                });
    }

    @Override
    public void updateCourseData(CourseData courseData, final InsertCallback callback) {
        courseRef.child(courseData.getCourseId()).setValue(courseData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess("Success Update");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("Fail to Update");
                    }
                });
    }

    @Override
    public void insertOpeningCourseData(OpeningCourseData openCourseData, final InsertCallback callback) {
        openCourseRef.push().setValue(openCourseData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess("Successful insert");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("Fail to insert");
                    }
                });
    }

    @Override
    public void updateOpeningCourseData(OpeningCourseData openCourseData, final InsertCallback callback) {
        openCourseRef.child(openCourseData.getOpeningCourseId()).setValue(openCourseData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess("Success Update");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("Fail to Update");
                    }
                });
    }

    @Override
    public void closeConnection() {

    }
}
