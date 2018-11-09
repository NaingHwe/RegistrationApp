package com.datainsights.trainingapp.Storage;

import com.datainsights.trainingapp.StudentRegistration.FirebaseStorageClass;

public class StorageHelper {
    private static StorageService storageService;
    public static void createFirebaseService(){
        storageService = new FirebaseStorageClass();
    }
    public static StorageService getStorageService(){
        return storageService;
    }
}
