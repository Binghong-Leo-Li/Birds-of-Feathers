package edu.ucsd.cse110wi22.team6.bof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FavoriteListActivity extends AppCompatActivity {
    private static final String TAG = "FavoriteListActivity";
    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private IPerson user;
    private IUserInfoStorage storage;
    private MessageListener messageListener;

    private final List<IPerson> nobody = Collections.emptyList();

    private static List<IPerson> nearbyPeople = new ArrayList<>();


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "MainActivity.onStart() called");

        Log.d(TAG, "App has gone through first time setup already");
        user = new Person(storage.getName(),
                storage.getCourseList(),
                storage.getPhotoUrl());

        personsViewAdapter.setUser(user);
        //updateUI();



        //Nearby.getMessagesClient(this).subscribe(messageListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        Log.d(TAG, "FavoriteListActivity.onCreate() called");

        storage = Utilities.getStorageInstance(this);

        bofRecyclerView = findViewById(R.id.favorite_list);

        personsLayoutManager = new LinearLayoutManager(this);
        bofRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new BoFsViewAdapter(nobody);
        bofRecyclerView.setAdapter(personsViewAdapter);







    }

    public void updateUI() {
        for (IPerson person : nearbyPeople) {
            Log.d(TAG, person.getName() +
                    ": " +
                    Utilities.numCoursesTogether(user, person) +
                    " classes in common");
        }

        personsViewAdapter.setPeopleList(Utilities.getBofList(user, nearbyPeople));
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }



}