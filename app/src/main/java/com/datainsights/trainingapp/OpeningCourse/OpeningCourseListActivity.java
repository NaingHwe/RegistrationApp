package com.datainsights.trainingapp.OpeningCourse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.datainsights.trainingapp.R;
import com.datainsights.trainingapp.StudentRegistration.ItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpeningCourseListActivity extends AppCompatActivity {
    ProgressBar progressLoading;
    Button btnOpeningCourseBack;
    RecyclerView rvOpeningCourseList;
    FloatingActionButton fabOpeningCourse;
    ArrayList<OpeningCourseData> openCourseListArray ;
   /* List<OpeningCourseList> openingCourseListArray = Arrays.asList(
            new OpeningCourseList("Professional Android Development","R.drawable.profile_pic","20/03/2018"),
            new OpeningCourseList("Oracle","R.drawable.profile_pic","20/06/2018"),
            new OpeningCourseList("Professional Java Programming","R.drawable.profile_pic","20/09/2018"),
            new OpeningCourseList("Core Kotlin Programming","R.drawable.profile_pic","20/12/2018"),
            new OpeningCourseList("Big Data","R.drawable.profile_pic","20/03/2018"),
            new OpeningCourseList("Basic Programming Language","R.drawable.profile_pic","20/06/2018"),
            new OpeningCourseList("Database Management System","R.drawable.profile_pic","20/09/2018"),
            new OpeningCourseList("Web Development","R.drawable.profile_pic","20/12/2018"));*/
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
        progressLoading = findViewById(R.id.progressBarOpenCourse);
        progressLoading.setVisibility(View.VISIBLE);


        fabOpeningCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(OpeningCourseListActivity.this,OpeningCourseDetailActivity.class);
               startActivity(intent);
            }
        });



        rvOpeningCourseList.addOnItemTouchListener(
                new ItemClickListener(getApplicationContext(), rvOpeningCourseList, new ItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever

                        Intent intent = new Intent(OpeningCourseListActivity.this, OpeningCourseDetailActivity.class);

                        intent.putExtra("OpeningCourseObj", openCourseListArray.get(position));
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("OpenCourses");
        openCourseListArray = new ArrayList<>();
// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    OpeningCourseData openCourseData = postSnapshot.getValue(OpeningCourseData.class);
                    openCourseData.setOpeningCourseId(postSnapshot.getKey());
                    openCourseListArray.add(openCourseData);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvOpeningCourseList.setLayoutManager(layoutManager);
                RVOpeningCourseListAdapter rvOpeningCourseListAdapter = new RVOpeningCourseListAdapter(getApplicationContext(),openCourseListArray);
                rvOpeningCourseList.setAdapter(rvOpeningCourseListAdapter);
                progressLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
