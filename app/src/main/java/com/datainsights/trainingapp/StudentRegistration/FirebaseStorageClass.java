package com.datainsights.trainingapp.StudentRegistration;

import android.support.annotation.NonNull;

import com.datainsights.trainingapp.Storage.InsertStudentCallback;
import com.datainsights.trainingapp.Storage.StorageService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseStorageClass implements StorageService {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference studentRef = database.getReference("students");

    @Override
    public void insertStudentData(StudentData studentData, final InsertStudentCallback callback) {

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
    public void closeConnection() {

    }
}
