package edu.ucsd.cse110wi22.team6.bof;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {
    private final List<Course> courses;

    public CourseViewAdapter(List<Course> courses) {
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

    public void addCourse(Course course) {
        courses.add(course);
        notifyItemInserted(courses.size() - 1);
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        private final TextView courseInfoView;
        private Course course;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseInfoView = itemView.findViewById(R.id.bof_course_info);
        }

        public void setCourse(Course course) {
            this.course = course;
            System.out.print(course.toString());
            this.courseInfoView.setText(course.toString());
        }
    }
}