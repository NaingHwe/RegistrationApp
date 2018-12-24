package com.datainsights.trainingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.datainsights.trainingapp.StudentRegistration.StudentData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//(2) recyclerview adapter declare

public class RVStudentListAdapter extends RecyclerView.Adapter<RVStudentListAdapter.StudentListViewHolder> {


    /*
    * Parameter constructor
    * @param context
    * */
    private Context context;
    private List<StudentData> studentLists = new ArrayList<>();


    public StudentListClickListener studentListClickListener;

    public RVStudentListAdapter(Context context, List<StudentData> studentLists, StudentListClickListener studentListClickListener) {
        this.context = context;
        this.studentLists = studentLists;
        this.studentListClickListener = studentListClickListener;
    }

    @NonNull
    @Override
    public StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.student_list_adapter,parent,false);

        return new StudentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListViewHolder holder, int position) {

        final StudentData studentList = studentLists.get(position);
        holder.tvStudentName.setText(studentList.getName());
        //studentList.getImageUrl()
        //R.drawable.person_pic
     /*   Picasso.with(context)
                .load(studentList.getProfileImageURL())
                .error(R.drawable.ic_launcher_background)
                .into(holder.cvStudentProfile);*/
     /*   Glide.with(context)
                .load("http://via.placeholder.com/300.png")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imagenotfound)
                .into(ivImg);*/
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_error);
        requestOptions.error(R.drawable.ic_error);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(studentList.getProfileImageURL())
                .into(holder.cvStudentProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentListClickListener.onClick(studentList);
            }
        });


    }

    public interface StudentListClickListener {
        void onClick(StudentData studentData);
    }

    @Override
    public int getItemCount() {
        return studentLists.size();
    }

    //(1)create view holder
    static class StudentListViewHolder extends RecyclerView.ViewHolder{
         TextView tvStudentName;
        CircleImageView cvStudentProfile;

        StudentListViewHolder(View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_studentName);
            cvStudentProfile = itemView.findViewById(R.id.cv_studentProfile);

        }

    }

}
