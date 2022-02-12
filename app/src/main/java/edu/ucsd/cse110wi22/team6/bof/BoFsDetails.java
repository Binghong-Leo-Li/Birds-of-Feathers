package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BoFsDetails extends AppCompatActivity {
    private String name;
    private List<Course> courseList;
    private String url;

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CourseViewAdapter courseViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bof_details);


        Bundle extras = getIntent().getExtras();
        this.name = extras.getString("name", "User do not exist");
        setTitle(this.name);
        String courseListParsing = extras.getString("courseListParsing");
        this.courseList = Utilities.parseCourseList(courseListParsing);
        this.url = extras.getString("url");

//        TextView counterView = findViewById(R.id.bof_name_info);
//        counterView.setText(String.valueOf(this.name));

        ImageView image = findViewById(R.id.imageView);
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(image);


        coursesRecyclerView = findViewById(R.id.bof_courses_info);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        courseViewAdapter = new CourseViewAdapter(this.courseList);
        coursesRecyclerView.setAdapter(courseViewAdapter);

    }

    public void onGoBackClicked(View view) {
        finish();
    }
}