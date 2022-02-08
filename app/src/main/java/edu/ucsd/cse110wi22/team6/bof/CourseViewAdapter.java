package edu.ucsd.cse110wi22.team6.bof;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {
    private List<? extends Course> courses;

    public List<? extends Course> getListCourses(){
        return this.courses;
    }

    public CourseViewAdapter(List<? extends Course> courses) {
        super();
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewAdapter.ViewHolder holder, int position) {
        holder.setCourse(courses.get(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCoursesList(List<? extends Course> newCourses) {
        courses = newCourses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView courseInfoView;
        private Course course;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseInfoView = itemView.findViewById(R.id.bof_course_info);
            itemView.setOnClickListener(this);
        }

        public void setCourse(Course course) {
            this.course = course;
            System.out.print(course.toString());
            this.courseInfoView.setText(course.toString());
        }

        @Override
        public void onClick(View view) {
//            Context context = view.getContext();
//            Intent intent = new Intent(context, bof_details.class);
//            intent.putExtra("name", this.person.getName());
//            intent.putExtra("courseListParsing", Utilities.encodeCourseList(this.person.getCourseList()));
//            intent.putExtra("url", this.person.getUrl());
//            context.startActivity(intent);
            // TODO: view BoF details
            // TODO: maybe add some logging here
        }
    }
}