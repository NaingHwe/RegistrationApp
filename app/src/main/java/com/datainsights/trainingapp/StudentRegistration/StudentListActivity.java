package com.datainsights.trainingapp.StudentRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.datainsights.trainingapp.R;
import com.datainsights.trainingapp.RVStudentListAdapter;

import java.util.Arrays;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    RecyclerView rvStudentList;
    FloatingActionButton fab;
    Button btnRegisterBack;
List<StudentList> studentListList = Arrays.asList(
        new StudentList("Mg Mg","R.drawable.profile_pic"),
        new StudentList("Aye Aye","R.drawable.profile_pic"),
        new StudentList("Yummy","R.drawable.profile_pic"),
        new StudentList("Aung Aung","R.drawable.profile_pic"),
        new StudentList("Hla Hla","R.drawable.profile_pic"),
        new StudentList("Sandar","R.drawable.profile_pic"),
        new StudentList("Yu Yu","R.drawable.profile_pic"),
        new StudentList("Mg Mg","R.drawable.profile_pic")
);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        btnRegisterBack = findViewById(R.id.btn_RegisterBack);
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        btnRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
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
