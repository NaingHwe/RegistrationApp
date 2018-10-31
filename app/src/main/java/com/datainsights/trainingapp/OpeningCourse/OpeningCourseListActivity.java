package com.datainsights.trainingapp.OpeningCourse;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.datainsights.trainingapp.R;

import java.util.Arrays;
import java.util.List;

public class OpeningCourseListActivity extends AppCompatActivity {
    Button btnOpeningCourseBack;
    RecyclerView rvOpeningCourseList;
    FloatingActionButton fabOpeningCourse;
    List<OpeningCourseList> openingCourseListArray = Arrays.asList(
            new OpeningCourseList("Professional Android Development","R.drawable.profile_pic","20/03/2018"),
            new OpeningCourseList("Oracle","R.drawable.profile_pic","20/06/2018"),
            new OpeningCourseList("Professional Java Programming","R.drawable.profile_pic","20/09/2018"),
            new OpeningCourseList("Core Kotlin Programming","R.drawable.profile_pic","20/12/2018"),
            new OpeningCourseList("Big Data","R.drawable.profile_pic","20/03/2018"),
            new OpeningCourseList("Basic Programming Language","R.drawable.profile_pic","20/06/2018"),
            new OpeningCourseList("Database Management System","R.drawable.profile_pic","20/09/2018"),
            new OpeningCourseList("Web Development","R.drawable.profile_pic","20/12/2018"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_course_list);
        btnOpeningCourseBack = findViewById(R.id.btn_openingCourseBack);
        btnOpeningCourseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fabOpeningCourse = findViewById(R.id.fab_openingCourse);
        rvOpeningCourseList = findViewById(R.id.rv_openingCourseList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvOpeningCourseList.setLayoutManager(layoutManager);
        RVOpeningCourseListAdapter rvOpeningCourseListAdapter = new RVOpeningCourseListAdapter(this,openingCourseListArray);
        rvOpeningCourseList.setAdapter(rvOpeningCourseListAdapter);


        fabOpeningCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(OpeningCourseListActivity.this,OpeningCourseDetailActivity.class);
               startActivity(intent);
            }
        });
    }
}
