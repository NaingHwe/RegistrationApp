package com.datainsights.trainingapp.StudentRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.datainsights.trainingapp.R;
import com.datainsights.trainingapp.RVStudentListAdapter;

import java.util.Arrays;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
RecyclerView rvStudentList;
    FloatingActionButton fab;
List<StudentList> studentListList = Arrays.asList(
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic")
);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentListActivity.this,StudentDetailActivity.class);
                startActivity(intent);
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        rvStudentList = findViewById(R.id.rv_studentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvStudentList.setLayoutManager(layoutManager);
        RVStudentListAdapter rvStudentListAdapter = new RVStudentListAdapter(this,studentListList);
        rvStudentList.setAdapter(rvStudentListAdapter);
    }

}
