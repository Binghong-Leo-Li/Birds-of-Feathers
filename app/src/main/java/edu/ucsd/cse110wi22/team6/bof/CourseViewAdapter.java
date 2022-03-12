package edu.ucsd.cse110wi22.team6.bof;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adapter from course to view
public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {
    private final List<Course> courses;

    // constructor
    public CourseViewAdapter(List<Course> courses) {
        super();
        this.courses = courses;
    }

    // Initialization
    @NonNull
    @Override
    public CourseViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        return new ViewHolder(view);
    }

    // binding course to position in recycler view
    @Override
    public void onBindViewHolder(@NonNull CourseViewAdapter.ViewHolder holder, int position) {
        holder.setCourse(courses.get(position));
    }

    // Adding to view
    public void addCourse(Course course) {
        courses.add(course);
        notifyItemInserted(courses.size() - 1); // Updating recycler view
    }

    // getter
    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    // ViewHolder class of CourseViewAdapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseInfoView;
        private Course course;

        // Constructor
        ViewHolder(View itemView) {
            super(itemView);
            this.courseInfoView = itemView.findViewById(R.id.bof_course_info);
        }

        // Setter to update
        public void setCourse(Course course) {
            this.course = course;
            System.out.print(course.toString());
            this.courseInfoView.setText(course.toString());
        }
    }
}