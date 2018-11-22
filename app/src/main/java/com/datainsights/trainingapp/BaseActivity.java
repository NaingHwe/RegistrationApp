package com.datainsights.trainingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog loading;
    protected android.app.ProgressDialog uploadLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new ProgressDialog();
        uploadLoading = new android.app.ProgressDialog(this);
        uploadLoading.setTitle("Uploading...");
    }

    protected void uploadingDialog(boolean show){
        if (show) {
            if (uploadLoading != null) {
                uploadLoading.show();
            }
        } else {
            if (loading != null) {
                uploadLoading.dismiss();
            }
        }
    }

    protected void loading(boolean show) {
        if (show) {
            if (loading != null) {
                loading.show(getSupportFragmentManager(), "Loading");
            }
        } else {
            if (loading != null) {
                loading.dismiss();
            }
        }
    }
}
