package edu.ucsd.cse110wi22.team6.bof;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.Session;
import edu.ucsd.cse110wi22.team6.bof.model.SessionChangeListener;
import edu.ucsd.cse110wi22.team6.bof.model.SizeComparator;
import edu.ucsd.cse110wi22.team6.bof.model.WaveListener;

// Activity to display List of BoFs
public class MainActivity extends NearbyActivity implements SessionChangeListener, WaveListener {
    private static final String TAG = "MainActivity";
    public static final int DEFAULT_YEAR = 2021;
    public static final String DEFAULT_QUARTER = "WI";

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

        user = storage.getUser();
        Log.d(TAG, "Current User: " + user);

        storage.registerWaveListener(this);
        sessionManager.registerSessionChangeListener(this);

        personsViewAdapter.setUser(user);
        updateComparators();
        updateBoFList();
        updateToggleButtonName();
    }

    // Updating UI to display all nearbyPeople
    void updateBoFList() {
        List<IPerson> nearbyStudents= sessionManager
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
                Comparator
                        // Existence of waves takes the most priority in sorting
                        .comparingInt((IPerson person) -> storage.isWavingToUser(person) ? 1 : 0)
                        // Then compare by other criterion (Ex. number of courses in common)
                        .thenComparing(
                            comparators.get(preferences_dropdown.getSelectedItemPosition())
                        )
        ));
    }

    void updateComparators() {
        comparators = Arrays.asList(
                // Sort by number of classes in common
                Utilities.getCompareByNumCourses(user),
                // prioritize small classes
                new SizeComparator(user, (course -> storage.getCourseSize(course))),
                // prioritize recent
                new RecencyComparator(user, year, quarter)
                // This quarter only is postponed indefinitely
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

    void stopCurrentSession() {
        Log.d(TAG, "Stopping current session");
        sessionManager.stopSession(this);
        updateToggleButtonName();
    }

    public List<? extends IPerson> getPeopleList() {
        return personsViewAdapter.getPeopleList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity.onCreate() called");

        // Add a <- arrow button to allow going back easily
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        sessionManager = SessionManager.getInstance(this);
        storage = Utilities.getStorageInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            year = DEFAULT_YEAR;
            quarter = DEFAULT_QUARTER;
        }
        else {
            year = extras.getInt("year", DEFAULT_YEAR);
            quarter = extras.getString("quarter", DEFAULT_QUARTER);
        }
        Log.d(TAG, "Year: " + year);
        Log.d(TAG, "Quarter: " + quarter);

        toggleButton = findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(view -> {
            if (sessionManager.isRunning()) {
                // Stopping a running session

                // If the session already has a name, don't prompt again
                if (sessionManager.getCurrentSession().getName() != null) {
                    stopCurrentSession();
                    return;
                }
                // If session is a fresh one (no name assigned)
                setSessionName("Save Session", sessionManager.getCurrentSession(), () -> {}, this::stopCurrentSession);
            }
            else{
                // Starting/resuming a session
                List<Session> sessions = storage.getSessionList();
                String[] pastSessions = Stream.concat(
                        Stream.of("New Session"),
                        sessions.stream().map(session -> "Resume \"" + session.getDisplayName() + "\"")
                ).toArray(String[]::new);

                AlertDialog.Builder resume_alert = new AlertDialog.Builder(MainActivity.this);
                View spView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                Spinner spinner = (Spinner)spView.findViewById(R.id.spinner);

                ArrayAdapter<String> resumeAdapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pastSessions);
                resumeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(resumeAdapter);

                //Alert Dialog for start/resuming a session after clicking start button
                resume_alert.setTitle("Start/Resume Session");
                resume_alert.setPositiveButton("Start", (dialog, id) -> {
                    int itemPosition = spinner.getSelectedItemPosition();
                    if (itemPosition == 0) {
                        Log.d(TAG, "Starting new session");
                        sessionManager.startNewSession(this);
                    } else {
                        Log.d(TAG, "Resuming existing session");
                        Session resumeSession = sessions.get(itemPosition - 1);
                        if (resumeSession.getName() == null) {
                            setSessionName("Name this session", resumeSession, () -> sessionManager.startSession(resumeSession, this), () -> {
                                updateToggleButtonName();
                                updateBoFList();
                            });
                            return;
                        }
                        sessionManager.startSession(resumeSession, this);
                    }
                    updateToggleButtonName();
                    updateBoFList();

                });
                resume_alert.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
                resume_alert.setView(spView);
                resume_alert.show();
            }
        });

        bofRecyclerView = findViewById(R.id.bof_list);

        personsLayoutManager = new LinearLayoutManager(this);
        bofRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new BoFsViewAdapter(nobody, person -> storage.isFavorited(person), person -> storage.isWavingToUser(person));
        bofRecyclerView.setAdapter(personsViewAdapter);

        //Dropdown for list of preferences that the user can sort by for bofs
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

    // Helper method to handle two instances where a set name is required
    // One is on first time a new session is stopped
    // Another is when a session stopped without being assigned a name and is restarted
    private void setSessionName(String title, Session session, Runnable beforeSaveAction, Runnable afterSaveAction) {
        final EditText edittext = new EditText(MainActivity.this);
        edittext.setContentDescription("edit");
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setView(edittext);
        alert.setTitle(title);
        alert.setMessage("Enter Session Name");
        alert.setPositiveButton("Save", (dialog, id) -> {
            String sessionName = edittext.getText().toString();
            if (sessionName.isEmpty()) {
                Utilities.showAlert(this, "Session name cannot be empty");
                return;
            }
            if (storage.isSessionNameTaken(sessionName)) {
                Utilities.showAlert(this, "A session with name \"" + sessionName + "\" already exists");
                return;
            }
            beforeSaveAction.run();
            session.setName(sessionName);
            afterSaveAction.run();
        });
        alert.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        alert.show();
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
        storage.unregisterWaveListener(this);
        sessionManager.unregisterSessionChangeListener(this);

        super.onStop();
    }

    @Override
    public void onSessionModified(Session session) {
        updateBoFList();
    }

    @Override
    public void onWaveInformationChanged() {
        updateBoFList();
    }
}