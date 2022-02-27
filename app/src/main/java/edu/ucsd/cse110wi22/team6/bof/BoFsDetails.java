package edu.ucsd.cse110wi22.team6.bof;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

// Class handling viewing BoF details when clicked in
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
                                    // extracting information from previously passed-in parameters
                                    // we parse the information accordingly below
        this.name = extras.getString("name", "User do not exist");
        setTitle(this.name);
        String courseListParsing = extras.getString("courseListParsing");
        this.courseList = Utilities.parseCourseList(courseListParsing);
        this.url = extras.getString("url");

        ImageView image = findViewById(R.id.imageView);
        Glide.with(this) // Using glide library to load image from url
                .load(url)
                .placeholder(R.drawable.placeholder) // setting place holder
                .error(R.drawable.placeholder) // if any part creates an error then show place holder
                .into(image);


        coursesRecyclerView = findViewById(R.id.bof_courses_info);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager); // setting the correct lay out
        courseViewAdapter = new CourseViewAdapter(this.courseList);
        coursesRecyclerView.setAdapter(courseViewAdapter); // set the adapter to display correct view

    }

    // handling the back button being clicked
    public void onGoBackClicked(View view) {
        finish();
    }

    public void onFavoriteClicked(View view) {
        Toast.makeText(this, "favorited!",
                Toast.LENGTH_LONG).show();

        //TODO　add to favorite list
    }
    public void onWaveClicked(View view) {
        Toast.makeText(this, "waved!",
                Toast.LENGTH_LONG).show();

        //TODO send wave through nearby
    }
}