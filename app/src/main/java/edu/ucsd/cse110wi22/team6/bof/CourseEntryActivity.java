package edu.ucsd.cse110wi22.team6.bof;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CourseEntryActivity extends AppCompatActivity {
    private static final String TAG = "CourseEntryActivity";
    private RecyclerView courseListRecyclerView;
    private LinearLayoutManager courseLayoutManager;
    private final List<Course> courses = new ArrayList<>();
    private CourseViewAdapter courseViewAdapter; // TODO: Allow deleting courses
    private Course previousEntry;

    ActivityResultLauncher<Intent> courseEntryFormLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Course newCourse = Utilities.parseCourse(data.getStringExtra("course"));
                    Log.d(TAG, "Adding course " + newCourse);
                    courseViewAdapter.addCourse(newCourse);
                    previousEntry = newCourse;
                }
            }
    );

    private String serializePreviousEntry() {
        return previousEntry == null ? "" : previousEntry.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Enter past courses");
        setContentView(R.layout.activity_course_entry);

        if (savedInstanceState != null) {
            courses.addAll(Utilities.parseCourseList(
                    savedInstanceState.getString("courseList")));
            String prevCourseEntry = savedInstanceState.getString("prevCourseEntry");
            if (!prevCourseEntry.isEmpty())
                previousEntry = Utilities.parseCourse(prevCourseEntry);
            Log.d(TAG, "Recovered courses from bundle: " + courses);
            Log.d(TAG, "Recovered previous course entry from bundle: " + previousEntry);
        }

        Button doneButton = findViewById(R.id.course_entry_done_button);
        doneButton.setOnClickListener(this::onDone);
        Button addOneClassButton = findViewById(R.id.add_one_class_button);
        addOneClassButton.setOnClickListener(this::onAddCourse);

        courseListRecyclerView = findViewById(R.id.added_course_list);

        courseLayoutManager = new LinearLayoutManager(this);
        courseListRecyclerView.setLayoutManager(courseLayoutManager);

        courseViewAdapter = new CourseViewAdapter(courses);
        courseListRecyclerView.setAdapter(courseViewAdapter);
    }

    private void onAddCourse(View view) {
        Intent intent = new Intent(this, AddNewCourseActivity.class);
        intent.putExtra("preFillCourse", serializePreviousEntry());
        courseEntryFormLauncher.launch(intent);
    }

    private void onDone(View view) {
        IUserInfoStorage storage = Utilities.getStorageInstance(this);

        storage.setCourseList(courses);
        storage.setInitialized(true);

        // Return to home
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("prevCourseEntry", serializePreviousEntry());
        outState.putString("courseList", Utilities.encodeCourseList(courses));
    }
}