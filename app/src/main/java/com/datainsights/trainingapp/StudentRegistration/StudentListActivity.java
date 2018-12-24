package com.datainsights.trainingapp.StudentRegistration;

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
import com.datainsights.trainingapp.RVStudentListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements RVStudentListAdapter.StudentListClickListener {
    RecyclerView rvStudentList;
    ProgressBar progressLoading;
    FloatingActionButton fab;
    Button btnRegisterBack;
     List<StudentData> studentListList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        btnRegisterBack = findViewById(R.id.btn_RegisterBack);
        getWindow().setBackgroundDrawableResource(R.drawable.background_pic1);
         btnRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentListActivity.this, StudentDetailActivity.class);
                startActivity(intent);

            }
        });

        progressLoading = findViewById(R.id.simpleProgressBar);
        progressLoading.setVisibility(View.VISIBLE);
        rvStudentList = findViewById(R.id.rv_studentList);

     /*   rvStudentList.addOnItemTouchListener(
                new ItemClickListener(getApplicationContext(), rvStudentList, new ItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever

                        Intent intent = new Intent(StudentListActivity.this, StudentDetailActivity.class);

                        intent.putExtra("StudentObj", studentListList.get(position));
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
        DatabaseReference ref = database.getReference("students");
        studentListList = new ArrayList<>();
// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // StudentData post = dataSnapshot.getValue(StudentData.class);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //System.out.println("for loop data = " + postSnapshot.getValue().toString() + "," + postSnapshot.getKey());

                    StudentData  studata = postSnapshot.getValue(StudentData.class);
                    studata.setUserId(postSnapshot.getKey());
                    studentListList.add(studata);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvStudentList.setLayoutManager(layoutManager);
                RVStudentListAdapter rvStudentListAdapter = new RVStudentListAdapter(getApplicationContext(), studentListList, new RVStudentListAdapter.StudentListClickListener() {
                    @Override
                    public void onClick(StudentData studentData) {
                        Intent intent = new Intent(StudentListActivity.this, StudentDetailActivity.class);

                        intent.putExtra("StudentObj", studentData);
                        startActivity(intent);
                    }
                });
                rvStudentList.setAdapter(rvStudentListAdapter);
                progressLoading.setVisibility(View.GONE);
               // System.out.println("onresume state = " + post);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onClick(StudentData studentData) {
        Intent intent = new Intent(StudentListActivity.this, StudentDetailActivity.class);

        //intent.putExtra("StudentObj", studentListList.get(position));
        intent.putExtra("StudentObj", studentData);
        startActivity(intent);
    }

   /* @Override
    public void onClick(StudentData studentData) {
        Intent intent = new Intent(StudentListActivity.this, StudentDetailActivity.class);

        //intent.putExtra("StudentObj", studentListList.get(position));
        intent.putExtra("StudentObj",studentData);
        startActivity(intent);
    }*/
}
