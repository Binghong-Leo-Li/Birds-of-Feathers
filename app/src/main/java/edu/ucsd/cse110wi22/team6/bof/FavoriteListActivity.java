package edu.ucsd.cse110wi22.team6.bof;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

public class FavoriteListActivity extends AppCompatActivity {
    private static final String TAG = "FavoriteListActivity";
    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private IPerson user;
    private AppStorage storage;

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Favorites");

        Log.d(TAG, "MainActivity.onStart() called");

        Log.d(TAG, "App has gone through first time setup already");
        user = storage.getUser();

        personsViewAdapter.setUser(user);
        personsViewAdapter.setPeopleList(storage.getFavoriteList());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        Log.d(TAG, "FavoriteListActivity.onCreate() called");

        // Add a <- arrow button to allow going back easily
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        storage = Utilities.getStorageInstance(this);

        bofRecyclerView = findViewById(R.id.favorite_list);

        personsLayoutManager = new LinearLayoutManager(this);
        bofRecyclerView.setLayoutManager(personsLayoutManager);

        // It is redundant to put stars since every one must be favorited to begin with
        // so return false on every person to have no stars in this view
        personsViewAdapter = new BoFsViewAdapter(storage.getFavoriteList(), person -> false);
        bofRecyclerView.setAdapter(personsViewAdapter);
    }

    // This allows the "<-" button to work
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}