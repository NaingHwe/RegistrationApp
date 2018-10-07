package com.datainsights.trainingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new ProgressDialog();
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
