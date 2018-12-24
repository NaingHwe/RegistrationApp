package com.datainsights.trainingapp.CourseEntry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.datainsights.trainingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CourseListActivity extends AppCompatActivity {
    ProgressBar progressLoading;
    Button btnCourseBack;
    RecyclerView rvCourseList;
    FloatingActionButton fabCourse;
    List<CourseData> courseListArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        btnCourseBack = findViewById(R.id.btn_CourseBack);

        fabCourse = findViewById(R.id.fab_course);
        progressLoading = findViewById(R.id.simpleProgressBar);
        progressLoading.setVisibility(View.VISIBLE);
        btnCourseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fabCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);
                startActivity(intent);
            }
        });


        rvCourseList = findViewById(R.id.rv_courseList);
       /* rvCourseList.addOnItemTouchListener(
                new ItemClickListener(getApplicationContext(), rvCourseList, new ItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever

                        Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);

                        intent.putExtra("CourseObj", courseListArray.get(position));
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("courses");
        courseListArray = new ArrayList<>();
// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CourseData courseData = postSnapshot.getValue(CourseData.class);
                    courseData.setCourseId(postSnapshot.getKey());
                    courseListArray.add(courseData);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvCourseList.setLayoutManager(layoutManager);
                RVCourseListAdapter rvCourseListAdapter = new RVCourseListAdapter(getApplicationContext(), courseListArray, new RVCourseListAdapter.CourseListClickListener() {
                    @Override
                    public void onClick(CourseData courseData) {
                        Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);

                        intent.putExtra("CourseObj", courseData);
                        startActivity(intent);
                    }
                });
                rvCourseList.setAdapter(rvCourseListAdapter);
                progressLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


}
