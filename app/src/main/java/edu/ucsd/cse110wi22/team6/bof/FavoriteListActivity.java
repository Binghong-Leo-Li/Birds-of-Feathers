package edu.ucsd.cse110wi22.team6.bof;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

public class FavoriteListActivity extends AppCompatActivity {
    private static final String TAG = "FavoriteListActivity";
    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private IPerson user;
    private AppStorage storage;

    private final List<IPerson> nobody = Collections.emptyList();

    private static List<IPerson> nearbyPeople = new ArrayList<>();


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "MainActivity.onStart() called");

        Log.d(TAG, "App has gone through first time setup already");
        user = storage.getUser();

        personsViewAdapter.setUser(user);
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

        // It is redundant to put stars since every one must be favorited to begin with
        // so return false on every person to have no stars in this view
        personsViewAdapter = new BoFsViewAdapter(nobody, person -> false);
        bofRecyclerView.setAdapter(personsViewAdapter);
    }

    public void updateUI() {
        personsViewAdapter.setPeopleList(Utilities.getBofList(user, nearbyPeople));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}