package com.datainsights.trainingapp.OpeningCourse;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVOpeningCourseListAdapter extends RecyclerView.Adapter<RVOpeningCourseListAdapter.OpeningCourseListViewHolder>{

    Context context;
    List<OpeningCourseData> openingCourseLists = new ArrayList<>();

    public RVOpeningCourseListAdapter(Context context, List<OpeningCourseData> openingCourseLists) {
        this.context = context;
        this.openingCourseLists = openingCourseLists;
    }

    @NonNull
    @Override
    public OpeningCourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.opening_course_list_adapter,parent,false);
        return new OpeningCourseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpeningCourseListViewHolder holder, int position) {
        OpeningCourseData  openingCourseListData = openingCourseLists.get(position);
        holder.tvOpeningCourseName.setText(openingCourseListData.getOpeningCourseTitle());
        holder.tvCourseDate.setText(openingCourseListData.getOpeningCourseStartDate());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.course);
        requestOptions.error(R.drawable.course);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(openingCourseListData.getOpeningCourseImageURL())
                .into(holder.cvOpeningCourseProfile);


    }

    @Override
    public int getItemCount() {
        return openingCourseLists.size();
    }

    static class  OpeningCourseListViewHolder extends RecyclerView.ViewHolder {
       TextView tvCourseDate;
       TextView tvOpeningCourseName;
       CircleImageView cvOpeningCourseProfile;
        public OpeningCourseListViewHolder(View itemView) {
            super(itemView);
            tvCourseDate = itemView.findViewById(R.id.tv_courseDate);
            tvOpeningCourseName = itemView.findViewById(R.id.tv_openingCourseName);
            cvOpeningCourseProfile = itemView.findViewById(R.id.cv_openingCourseProfile);
        }
    }
}
