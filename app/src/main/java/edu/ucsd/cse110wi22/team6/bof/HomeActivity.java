package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Spinner currentQuarterDropDown;

    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private IPerson user;
    private IUserInfoStorage storage;
    private MessageListener messageListener;



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "HomeActivity.onStart() called");
        if (storage.isInitialized()) {
            Log.d(TAG, "App has gone through first time setup already");
            //user = new Person(storage.getName(),
                    //storage.getCourseList(),
                    //storage.getPhotoUrl());

            //personsViewAdapter.setUser(user);
            //updateUI();
        } else {
            // First time setup
            Log.d(TAG, "First time setup detected");
            Intent intent = new Intent(this, NameEntryActivity.class);
            startActivity(intent);
        }

        //Nearby.getMessagesClient(this).subscribe(messageListener);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Home");
        setContentView(R.layout.activity_home);

        storage = Utilities.getStorageInstance(this);

        currentQuarterDropDown=findViewById(R.id.currentQuarter_dropdown);
        ArrayAdapter<CharSequence> quarterAdapter=ArrayAdapter.createFromResource(this, R.array.currentQuarter, android.R.layout.simple_spinner_item);
        currentQuarterDropDown.setAdapter(quarterAdapter);

        Button startButton = findViewById(R.id.buttonStart3);
        startButton.setOnClickListener(this::onButtonStart);

        Button coursesButton = findViewById(R.id.buttonCourses);
        coursesButton.setOnClickListener(this::onButtonCourses);

        Button favoritesButton = findViewById(R.id.buttonFavorites);
        favoritesButton.setOnClickListener(this::onButtonFavorites);


    }
    private void onButtonStart(View view) {


        // go to main
        Log.d(TAG, "start button called");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onButtonCourses(View view) {


        // go to courses
        Log.d(TAG, "courses button called");
        Intent intent = new Intent(this, CourseEntryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onButtonFavorites(View view) {


        // go to favorites
        //Intent intent = new Intent(this, activity_favorites.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
    }

}