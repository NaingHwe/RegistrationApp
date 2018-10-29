package com.datainsights.trainingapp.CourseEntry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.datainsights.trainingapp.R;

public class CourseDetailActivity extends AppCompatActivity {
    Button btnCourseDetailBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        btnCourseDetailBack = findViewById(R.id.btn_course_detail_back);

        btnCourseDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
