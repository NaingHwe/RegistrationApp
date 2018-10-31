package com.datainsights.trainingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.datainsights.trainingapp.CourseEntry.CourseListActivity;
import com.datainsights.trainingapp.OpeningCourse.OpeningCourseList;
import com.datainsights.trainingapp.OpeningCourse.OpeningCourseListActivity;
import com.datainsights.trainingapp.StudentRegistration.StudentListActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.linearlayout_studentRegistration).setOnClickListener(this);
        findViewById(R.id.linearlayout_courseEnty).setOnClickListener(this);
        findViewById(R.id.linearlayout_openingCourse).setOnClickListener(this);

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
        Intent intent = null;
       switch (view.getId())
       {
           case  R.id.linearlayout_studentRegistration :
            intent = new Intent(HomeActivity.this, StudentListActivity.class);
           startActivity(intent);
           break;
           case  R.id.linearlayout_courseEnty :
               intent = new Intent(HomeActivity.this, CourseListActivity.class);
               startActivity(intent);
               break;
           case  R.id.linearlayout_openingCourse :
               intent = new Intent(HomeActivity.this, OpeningCourseListActivity.class);
               startActivity(intent);
               break;




       }

    }
}
