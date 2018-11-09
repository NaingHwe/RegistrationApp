package com.datainsights.trainingapp;

import android.app.Application;

import com.datainsights.trainingapp.Storage.StorageHelper;

public class TrainingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StorageHelper.createFirebaseService();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        StorageHelper.getStorageService().closeConnection();
    }
}
