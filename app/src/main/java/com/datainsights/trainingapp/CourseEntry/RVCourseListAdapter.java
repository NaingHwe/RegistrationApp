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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVCourseListAdapter extends RecyclerView.Adapter<RVCourseListAdapter.CourseListViewHolder>{


    Context context;
    List<CourseData> courseLists = new ArrayList<>();

    public RVCourseListAdapter(Context context, List<CourseData> courseLists) {
        this.context = context;
        this.courseLists = courseLists;
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_list_adapter,parent,false);

       
        return new CourseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        CourseData courseList = courseLists.get(position);
        holder.tvCourseName.setText(courseList.getCourseTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.course);
        requestOptions.error(R.drawable.course);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(courseList.getCourseImageURL())
                .into(holder.cvCourseProfile);

       /* Picasso.with(context)
                .load(R.drawable.course)
                .error(R.drawable.ic_launcher_background)
                .into(holder.cvCourseProfile);*/
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
