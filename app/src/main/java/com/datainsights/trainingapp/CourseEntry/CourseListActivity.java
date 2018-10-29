package com.datainsights.trainingapp.CourseEntry;

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

public class CourseListActivity extends AppCompatActivity {

    Button btnCourseBack;
    RecyclerView rvCourseList;
    FloatingActionButton fabCourse;
    List<CourseList> courseListArray = Arrays.asList(
            new CourseList("Professional Android Development","R.drawable.profile_pic"),
            new CourseList("Oracle","R.drawable.profile_pic"),
            new CourseList("Professional Java Programming","R.drawable.profile_pic"),
            new CourseList("Core Kotlin Programming","R.drawable.profile_pic"),
            new CourseList("Big Data","R.drawable.profile_pic"),
            new CourseList("Basic Programming Language","R.drawable.profile_pic"),
            new CourseList("Database Management System","R.drawable.profile_pic"),
            new CourseList("Web Development","R.drawable.profile_pic"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        btnCourseBack = findViewById(R.id.btn_CourseBack);
        fabCourse = findViewById(R.id.fab_course);

        btnCourseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fabCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseListActivity.this,CourseDetailActivity.class);
                startActivity(intent);
            }
        });


        rvCourseList = findViewById(R.id.rv_courseList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCourseList.setLayoutManager(layoutManager);
        RVCourseListAdapter rvCourseListAdapter = new RVCourseListAdapter(this,courseListArray);
        rvCourseList.setAdapter(rvCourseListAdapter);
    }

}
