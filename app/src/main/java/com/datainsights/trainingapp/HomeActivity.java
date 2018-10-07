package com.datainsights.trainingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.datainsights.trainingapp.StudentRegistration.StudentListActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.linearlayout_studentRegistration).setOnClickListener(this);

       /* cvStudentRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, StudentRegistrationActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onClick(View view) {
       switch (view.getId())
       {
           case  R.id.linearlayout_studentRegistration :
           Intent intent = new Intent(HomeActivity.this, StudentListActivity.class);
           startActivity(intent);
           break;


       }

    }
}
