package edu.ucsd.cse110wi22.team6.bof;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// class handling UI showing added courses and giving option to add another new course
public class CourseEntryActivity extends AppCompatActivity {
    private static final String TAG = "CourseEntryActivity";
    private RecyclerView courseListRecyclerView;
    private LinearLayoutManager courseLayoutManager;
    private final List<Course> courses = new ArrayList<>();
    private CourseViewAdapter courseViewAdapter; // TODO: Allow deleting courses
    private Course previousEntry;

    // Checking if we have saved data from add new course activity
    ActivityResultLauncher<Intent> courseEntryFormLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // only update if we have received another new course from AddNewCourseActivity
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Course newCourse = Utilities.parseCourse(data.getStringExtra("course"));
                    Log.d(TAG, "Adding course " + newCourse);

                    // adding the new course to the view through view adapter
                    courseViewAdapter.addCourse(newCourse);

                    // setting previous entry
                    previousEntry = newCourse;
                }
            }
    );

    // Returning valid previous entry as string or null if empty
    private String serializePreviousEntry() {
        return previousEntry == null ? "" : previousEntry.toString();
    }

    // initialization of the UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Enter past courses");
        setContentView(R.layout.activity_course_entry);

        if (savedInstanceState != null) { // if there are new values, then update
            courses.addAll(Utilities.parseCourseList(
                    savedInstanceState.getString("courseList")));
            String prevCourseEntry = savedInstanceState.getString("prevCourseEntry");
            if (!prevCourseEntry.isEmpty()) // setting correct previous entry
                previousEntry = Utilities.parseCourse(prevCourseEntry);
            Log.d(TAG, "Recovered courses from bundle: " + courses);
            Log.d(TAG, "Recovered previous course entry from bundle: " + previousEntry);
        } // else no need to update

        // button to handle when finished with the whole course adding
        Button doneButton = findViewById(R.id.course_entry_done_button);
        doneButton.setOnClickListener(this::onDone);

        // button to handle when want to add one more course and move on to AddNewCourseActivity
        Button addOneClassButton = findViewById(R.id.add_one_class_button);
        addOneClassButton.setOnClickListener(this::onAddCourse);

        courseListRecyclerView = findViewById(R.id.added_course_list);

        courseLayoutManager = new LinearLayoutManager(this);
        // setting layout
        courseListRecyclerView.setLayoutManager(courseLayoutManager);

        courseViewAdapter = new CourseViewAdapter(courses);
        // updating the new view
        courseListRecyclerView.setAdapter(courseViewAdapter);
    }

    // button for add course, moving on to AddNewCourseActivity
    private void onAddCourse(View view) {
        Intent intent = new Intent(this, AddNewCourseActivity.class);
        intent.putExtra("preFillCourse", serializePreviousEntry());
        courseEntryFormLauncher.launch(intent);
    }

    // button for done, moving on to MainActivity
    private void onDone(View view) {
        IUserInfoStorage storage = Utilities.getStorageInstance(this);

        storage.setCourseList(courses);
        storage.setInitialized(true);

        // Return to home
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // putting extra information when moving on to next state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("prevCourseEntry", serializePreviousEntry());
        outState.putString("courseList", Utilities.encodeCourseList(courses));
    }
}