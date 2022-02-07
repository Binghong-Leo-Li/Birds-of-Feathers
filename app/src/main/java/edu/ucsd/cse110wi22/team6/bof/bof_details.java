package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class bof_details extends AppCompatActivity {
    private String name;
    private List<Course> courseList;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bof_details);

        Bundle extras = getIntent().getExtras();
        this.name = extras.getString("name", "User do not exist");
        String courseListParsing = extras.getString("courseListParsing");
        this.courseList = Utilities.parseCourseList(courseListParsing);
        this.url = extras.getString("url");
    }

    public void onGoBackClicked(View view) {
        Intent intent = new Intent(this, TODO.class);
        startActivity(intent);
        setContentView(R.layout.activity_bof_details);
    }
}