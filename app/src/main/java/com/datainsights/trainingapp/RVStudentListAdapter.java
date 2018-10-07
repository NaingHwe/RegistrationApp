package com.datainsights.trainingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datainsights.trainingapp.StudentRegistration.StudentList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//(2) recyclerview adapter declare
public class RVStudentListAdapter extends RecyclerView.Adapter<RVStudentListAdapter.StudentListViewHolder>{

    /*
    * Parameter constructor
    * @param context
    * */
    Context context;
    List<StudentList> studentLists = new ArrayList<>();
    public RVStudentListAdapter(Context context,List<StudentList> studentLists){
        this.context = context;
        this.studentLists = studentLists;
    }

    @NonNull
    @Override
    public StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.student_list_adapter,parent,false);
        return new StudentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListViewHolder holder, int position) {

        StudentList studentList = studentLists.get(position);
        holder.tvStudentName.setText(studentList.getName());
        //studentList.getImageUrl()
        Picasso.with(context)
                .load(R.drawable.person_pic)
                .error(R.drawable.ic_launcher_background)
                .into(holder.cvStudentProfile);
    }

    @Override
    public int getItemCount() {
        return studentLists.size();
    }

    //(1)create view holder
    static class StudentListViewHolder extends RecyclerView.ViewHolder{
         TextView tvStudentName;
        CircleImageView cvStudentProfile;
        public StudentListViewHolder(View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_studentName);
            cvStudentProfile = itemView.findViewById(R.id.cv_studentProfile);

        }
    }
}
