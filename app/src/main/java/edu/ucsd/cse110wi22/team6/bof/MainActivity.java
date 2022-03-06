package edu.ucsd.cse110wi22.team6.bof;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.SizeComparator;

// Activity to display List of BoFs
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private Spinner preferences_dropdown;
    private Button toggleButton;

    private IPerson user;
    private AppStorage storage;

    // Current year and quarter
    private int year;
    private String quarter;

    // Dummy
    private final List<IPerson> nobody = Collections.emptyList();

    private List<Comparator<IPerson>> comparators;
    private SessionManager sessionManager;

    // handling start up of the app
    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "MainActivity.onStart() called");

        Log.d(TAG, "App has gone through first time setup already");
        user = storage.getUser();
        Log.d(TAG, "Current User: " + user);

        personsViewAdapter.setUser(user);
        updateComparators();
        updateBoFList();
        updateToggleButtonName();
    }

    // Updating UI to display all nearbyPeople
    void updateBoFList() {
        List<IPerson> nearbyStudents= SessionManager.getInstance(this)
                .getCurrentSession()
                .getNearbyStudentList();
        for (IPerson person : nearbyStudents) {
            Log.d(TAG, person.getName() +
                    ": " +
                    Utilities.numCoursesTogether(user, person) +
                    " classes in common");
        }

        // the Adapter handles updating display
        personsViewAdapter.setPeopleList(Utilities.getBofList(user,
                nearbyStudents,
                comparators.get(preferences_dropdown.getSelectedItemPosition())));
    }

    void updateComparators() {
        comparators = Arrays.asList(
                // Sort by number of classes in common
                Utilities.getCompareByNumCourses(user),
                // prioritize small classes
                new SizeComparator(user, (course -> storage.getCourseSize(course))),
                // prioritize recent
                new RecencyComparator(user, year, quarter),
                // Postponed: this quarter only
                Utilities.getCompareByNumCourses(user) // TODO: delete this option
        );
        updateBoFList();
    }

    void setYear(int year) {
        this.year = year;
        updateComparators();
    }

    void setQuarter(String quarter) {
        this.quarter = quarter;
        updateComparators();
    }

    void updateToggleButtonName() {
        boolean running = sessionManager.isRunning();
        toggleButton.setText(running ? R.string.session_stop : R.string.session_start);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity.onCreate() called");

        sessionManager = SessionManager.getInstance(this);

        toggleButton = findViewById(R.id.toggle_button);

        year = 2022; // TODO: change year and quarter based on intent parameters and time selection
        quarter = "WI";

        toggleButton.setOnClickListener(view -> {
            if (sessionManager.isRunning()) {
                Log.d(TAG, "Stop clicked");
                sessionManager.stopSession();

                // TODO: enable alert to save
//                final EditText edittext = new EditText(MainActivity.this);
//                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//                alert.setView(edittext);
//                alert.setTitle("Save Session");
//                alert.setMessage("Enter Session Name");
//                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        String session_name = edittext.getText().toString();
//                        //startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                        toggle_button.setText(R.string.session_start);
//                        //Hide BoF List
//                    }
//                });
//                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//                alert.show();
            }
            else{
                Log.d(TAG, "Start clicked");
                // TODO: allowing resuming old sessions
                sessionManager.startNewSession();
                updateBoFList();
            }
            updateToggleButtonName();
        });

        storage = Utilities.getStorageInstance(this);

        bofRecyclerView = findViewById(R.id.bof_list);

        personsLayoutManager = new LinearLayoutManager(this);
        bofRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new BoFsViewAdapter(nobody);
        bofRecyclerView.setAdapter(personsViewAdapter);

        preferences_dropdown=findViewById(R.id.preferences_dropdown);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.preferences, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        preferences_dropdown.setAdapter(adapter);
        preferences_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected position " + i);
                updateBoFList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "onNothingSelected");
                updateBoFList();
            }
        });

        findViewById(R.id.mock_ui_button).setOnClickListener(view -> {
            // for mocking purpose
            Intent intent = new Intent(this, MockingPasting.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}