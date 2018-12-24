package com.datainsights.trainingapp.CourseEntry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.datainsights.trainingapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVCourseListAdapter extends RecyclerView.Adapter<RVCourseListAdapter.CourseListViewHolder>{


    Context context;
    List<CourseData> courseLists = new ArrayList<>();

    private CourseListClickListener courseListClickListener;

    public RVCourseListAdapter(Context context, List<CourseData> courseLists, CourseListClickListener courseListClickListener) {
        this.context = context;
        this.courseLists = courseLists;
        this.courseListClickListener = courseListClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        final CourseData courseList = courseLists.get(position);
        holder.tvCourseName.setText(courseList.getCourseTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.course);
        requestOptions.error(R.drawable.course);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(courseList.getCourseImageURL())
                .into(holder.cvCourseProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseListClickListener.onClick(courseList);
            }
        });

       /* Picasso.with(context)
                .load(R.drawable.course)
                .error(R.drawable.ic_launcher_background)
                .into(holder.cvCourseProfile);*/
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_list_adapter, parent, false);


        return new CourseListViewHolder(view);
    }

    public interface CourseListClickListener {
        void onClick(CourseData courseData);
    }

    @Override
    public int getItemCount() {
        return courseLists.size();
    }

    static class CourseListViewHolder extends RecyclerView.ViewHolder {

        TextView tvCourseName;
        CircleImageView cvCourseProfile;



        public CourseListViewHolder(View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tv_courseName);
            cvCourseProfile = itemView.findViewById(R.id.cv_courseProfile);

        }
    }
}
