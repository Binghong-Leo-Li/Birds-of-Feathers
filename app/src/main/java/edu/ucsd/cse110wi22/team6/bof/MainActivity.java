package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private IPerson user;
    private IUserInfoStorage storage;

    // Dummy
    public List<IPerson> nobody = Collections.emptyList();

    // User has 2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11
    public List<IPerson> dummyNearbyPeople = Arrays.asList(
            /*1*/ new Person("Rick", Utilities.parseCourseList("2022 WI CSE 110"), ""),
            /*1*/ new Person("Gary", Utilities.parseCourseList("2021 FA CSE 100"), ""),
            /*0*/ new Person("Guy", Utilities.parseCourseList(""), ""),
            /*0*/ new Person("Greg", Utilities.parseCourseList("2022 SP CSE 110,2021 FA MATH 100,2021 FA ECE 35,2019 FA CSE 11"), ""),
            /*2*/ new Person("Bill", Utilities.parseCourseList("2021 FA ECE 65,2020 FA CSE 11"), ""),
            /*2*/ new Person("Will", Utilities.parseCourseList("2020 FA CSE 11,2021 FA ECE 65"), ""),
            /*4*/ new Person("Replicant", Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11"), ""),
            /*1*/ new Person("Bob", Utilities.parseCourseList("2020 FA CSE 11,2022 WI CSE 11"), "")
    );

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity.onStart() called");
        if (storage.isInitialized()) {
            Log.d(TAG, "App has gone through first time setup already");
            user = new Person(storage.getName(),
                    storage.getCourseList(),
                    storage.getPhotoUrl());

            for (IPerson person : dummyNearbyPeople) {
                Log.d(TAG, person.getName() +
                        ": " +
                        Utilities.numCoursesTogether(user, person) +
                        " classes in common");
            }

            personsViewAdapter.setPeopleList(Utilities.getBofList(user, dummyNearbyPeople));
            personsViewAdapter.setUser(user);
        } else {
            // First time setup
            Log.d(TAG, "First time setup detected");
            Intent intent = new Intent(this, NameEntryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity.onCreate() called");

        storage = Utilities.getStorageInstance(this);

        bofRecyclerView = findViewById(R.id.bof_list);

        personsLayoutManager = new LinearLayoutManager(this);
        bofRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new BoFsViewAdapter(nobody);
        bofRecyclerView.setAdapter(personsViewAdapter);
    }

    public void onForceReset(View view) {
        // DEBUG feature
        storage.setInitialized(false);
        Intent intent = new Intent(this, CourseEntryActivity.class);
        startActivity(intent);
    }
}